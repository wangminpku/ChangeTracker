package cn.edu.pku.sei.actionsparser.member;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.actionsparser.util.AstRelations;
import cn.edu.pku.sei.actionsparser.util.DefaultUpDownTraversal;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.memeber.InitializerChangeEntity;
import com.github.gumtreediff.actions.model.Action;

import java.util.ArrayList;
import java.util.List;

public class MatchInitializerBlock {

    public static void matchInitializerBlock(MiningActionData fp, Action a) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        DefaultUpDownTraversal.traverseInitializer(a,subActions,changePacket);
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        InitializerChangeEntity code = new InitializerChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INITIALIZER);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
//        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.addOneChangeEntity(code);
        fp.setActionTraversedMap(subActions);
//        Tree tt = (Tree) a.getNode();
//        if(tt.getChildren().size()==1){
//            Tree child = (Tree)tt.getChild(0);
//            if(child.getAstNode().getNodeType()== ASTNode.BLOCK){
//                //non static
//            }
//        }else if(tt.getChildren().size()==2){
//            Tree child1 = (Tree)tt.getChild(0);
//            Tree child2 = (Tree)tt.getChild(1);
//            if(child1.getAstNode().getNodeType()==ASTNode.MODIFIER && child2.getAstNode().getNodeType()==ASTNode.BLOCK
//                    &&child1.getLabel()=="static"){
//                //static
//            }
//        }
    }

}

