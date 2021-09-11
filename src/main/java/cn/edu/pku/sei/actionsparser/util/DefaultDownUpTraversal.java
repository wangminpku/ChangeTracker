package cn.edu.pku.sei.actionsparser.util;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.generateactions.ActionConstants;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.List;

public class DefaultDownUpTraversal extends BasicTreeTraversal{
    public static void traverseClassSignature(Tree node, List<Action> result1, ChangePacket changePacket){
        assert node.getDoAction() ==null;
        if(changePacket.getChangeSet1()==null){
            changePacket.initChangeSet1();
        }
        changePacket.getChangeSet1().add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        for(ITree child :children){
            Tree tmp = (Tree) child;
            if(tmp.getAstNode().getNodeType()== ASTNode.MODIFIER ||
                    tmp.getAstNode().getNodeType() == ASTNode.SIMPLE_NAME ||
                    tmp.getAstNode().getNodeType() == ASTNode.PARAMETERIZED_TYPE||
                    tmp.getAstNode().getNodeType() == ASTNode.SIMPLE_TYPE){
                traverseNodeSubTree(tmp,result1,changePacket.getChangeSet1());
            }
        }
    }

    public static void traverseMethodSignature(Tree node,List<Action> result1,ChangePacket changePacket){
        assert(node.getDoAction() == null);
        if(changePacket.getChangeSet1()==null){
            changePacket.initChangeSet1();
        }
        changePacket.getChangeSet1().add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        for(ITree child:children){
            Tree tmp = (Tree) child;
            if(tmp.getAstNode().getNodeType() == ASTNode.BLOCK){
                break;
            }
            traverseNodeSubTree(tmp,result1,changePacket.getChangeSet1());
        }
    }

    /**
     * 最简单的从father node往下找edit 节点，然后标记
     * @param fafather root节点
     * @param editAction editAction
     * @param changePacket changePacket
     */
    public static void traverseFatherNodeGetSameNodeActions(Tree fafather,List<Action> editAction,ChangePacket changePacket){
        if(changePacket.getChangeSet1()==null){
            changePacket.initChangeSet1();
        }
        traverseNodeSubTree(fafather,editAction,changePacket.getChangeSet1());
    }

    public static void traverseIfPredicate(Tree node,List<Action> result1,ChangePacket changePacket){
        assert(node.getDoAction() == null);
        if(changePacket.getChangeSet1()==null){
            changePacket.initChangeSet1();
        }
        changePacket.getChangeSet1().add(ActionConstants.NULLACTION);
        List<ITree> children = node.getChildren();
        int i;
        for(i =0;i<children.size();i++){
            Tree tmp = (Tree) children.get(i);
            if(tmp.getAstNode().getNodeType() == ASTNode.BLOCK){
                break;
            }
        }
        int bound;
        if(i!=children.size()-1){
            bound=i;

        }else{
            bound=children.size()-1;
        }
        for(int j=0;j<bound;j++){
            Tree tmp = (Tree) children.get(j);
            traverseNodeSubTree(tmp,result1,changePacket.getChangeSet1());
        }
    }

    public static void traverseDoWhileCondition(Tree node,List<Action> result,ChangePacket changePacket){
//        assert(node.getDoAction() == null);
//        assert(node.getChildren().size()==2);
        changePacket.initChangeSet1();
        Tree secondChild = (Tree) node.getChild(1);
        traverseNodeSubTree(secondChild,result,changePacket.getChangeSet1());
    }

    public static void traverseSwitchCondition(Tree node,List<Action> result,ChangePacket changePacket){
        assert(node.getDoAction() == null);
        assert(node.getChildren().size()==2);
        changePacket.initChangeSet1();
        List<ITree> children = node.getChildren();
        for(ITree tmp:children){
            Tree tmp2 = (Tree) tmp;
            if(tmp2.getAstNode().getNodeType() == ASTNode.SWITCH_CASE){
                break;
            }
            traverseOneType(node,result,changePacket);
        }

    }
}
