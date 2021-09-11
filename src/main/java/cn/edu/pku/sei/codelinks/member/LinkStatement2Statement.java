package cn.edu.pku.sei.codelinks.member;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.statement.VariableChangeEntity;
import cn.edu.pku.sei.codelinks.bean.StmtData;
import cn.edu.pku.sei.codelinks.util.Link;
import cn.edu.pku.sei.codelinks.util.LinkUtil;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;

public class LinkStatement2Statement {

    public static void checkStmtAssociation(ChangeEntityData changeEntityData, ChangeEntity ce1, ChangeEntity ce2){

        StmtData linkBean1 = (StmtData) ce1.linkBean;
        StmtData linkBean2 = (StmtData) ce2.linkBean;
//        for(String tmp:linkBean1.variableField){
//            if(linkBean2.variableField.contains(tmp)){
//                Link association = new Link(ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.TYPE_SHARE_FIELD,tmp);
//                changeEntityData.mLinks.add(association);
//                break;
//            }
//        }
        //System.out.println("第一个语句：" + linkBean1 +"第二个语句："+ linkBean2);
        for(String tmp:linkBean1.variableLocal) {
            //System.out.println("第一个语句的变量名："+tmp);
            //System.out.println("第二个语句的变量名列表：" + linkBean2.variableLocal);
            if(linkBean2.variableLocal.contains(tmp)){
                if("".equals(tmp)){
                    continue;
                }
                if(ce1 instanceof VariableChangeEntity || ce2 instanceof VariableChangeEntity) {
                    ///System.out.println("满足了变量类型的条件");
                    Action curAction = ce1.clusteredActionBean.curAction;
                    Tree node = (Tree) curAction.getNode();
                    String methodName = LinkUtil.findResidingMethodName(node);
                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_INNER_USE,tmp,methodName);
                    Link link = new Link(changeEntityData.fileName,ce1,ce2, desc,tmp);
                    changeEntityData.mLinks.add(link);
                    //System.out.println("建立了一条定义使用关系");
                }
                break;
            }
        }


    }

    public static void checkStmtShareField(ChangeEntityData changeEntityData,ChangeEntity ce1,ChangeEntity ce2){
        StmtData linkBean1 = (StmtData) ce1.linkBean;
        StmtData linkBean2 = (StmtData) ce2.linkBean;
        //pass
        for(String l1:linkBean1.variableField){
            for(String l2:linkBean2.variableField){
                if(l1.equals(l2)){
                    Link association = new Link(changeEntityData.fileName,ce1,ce2,ChangeEntityDesc.StageIIIAssociationType.DEF_INNER_USE,l1);
                    changeEntityData.mLinks.add(association);
                }
            }
        }
    }
}
