package cn.edu.pku.sei.changeentity;

import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.ASTNode;

public class ChangeEntityUtil {

    public static boolean isTypeIIEntity(String entityName) {
        switch (entityName) {
            case ChangeEntityDesc.StageIIENTITY.ENTITY_IF_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_FOR_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_ENHANCED_FOR_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_WHILE_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_DO_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_SYNCHRONIZED_STMT:
            case ChangeEntityDesc.StageIIENTITY.ENTITY_TRY_STMT:
                return true;
            default:
                break;
        }
        return false;
    }

    public static int checkEntityCode(ChangeEntity ce) {
        if (ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)
                && ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_MOVE)) {
            return 1;
        }
        if (ce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD)
                && (ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT) || ce.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE))) {
            if (isTypeIIEntity(ce.stageIIBean.getChangeEntity())) {
                return 2;
            }
        }
        return 0;
    }

    public static boolean simpleMoveWrapperDecision(MiningActionData fp, ChangeEntity move){
        Tree t = (Tree) move.clusteredActionBean.curAction.getNode();
        Tree dstt = (Tree)fp.getMappedDstOfSrcNode(t);
        Tree par = (Tree) dstt.getParent();
        boolean flag =false;
        switch(par.getAstNode().getNodeType()){
            case ASTNode.BLOCK:
            case ASTNode.IF_STATEMENT:
            case ASTNode.FOR_STATEMENT:
            case ASTNode.ENHANCED_FOR_STATEMENT:
            case ASTNode.SYNCHRONIZED_STATEMENT:
            case ASTNode.WHILE_STATEMENT:
            case ASTNode.DO_STATEMENT:
                flag = true;
                break;
            default:break;
        }

        return flag;
    }

    public static boolean isMoveInWrapper(MiningActionData fp, ChangeEntity wrapper, ChangeEntity move) {
        Integer[] range = null;
        if (wrapper.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)) {
            ITree dstITree = fp.getMappedDstOfSrcNode(move.clusteredActionBean.curAction.getNode());
            if (dstITree == null) {
                return false;
            }

            Tree dstTree = (Tree) dstITree;
            range = dstTree.getRange();
        } else if (wrapper.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)) {
            range = ((Tree) move.clusteredActionBean.curAction.getNode()).getRange();
        } else {
            return false;
        }
        Integer[] wrapperRange = ((Tree) wrapper.clusteredActionBean.curAction.getNode()).getRange();
        if (wrapperRange[0] <= range[0] && wrapperRange[1] >= range[1]) {
            if(simpleMoveWrapperDecision(fp,move)){
                return true;
            }
            return false;
        }
        return false;

    }
}
