package cn.edu.pku.sei.codelinks.member;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.memeber.FieldChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.MethodChangeEntity;
import cn.edu.pku.sei.codelinks.bean.FieldData;
import cn.edu.pku.sei.codelinks.bean.MethodData;
import cn.edu.pku.sei.codelinks.bean.StmtData;
import cn.edu.pku.sei.codelinks.util.Link;
import cn.edu.pku.sei.codelinks.util.LinkUtil;
import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.fileutil.DiffFileReader;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LinkStatement2Member {

    public static void checkStmtMethodAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt, MethodChangeEntity method){
        StmtData stmtData = (StmtData)stmt.linkBean;
        MethodData methodData = (MethodData) method.linkBean;
        if(method.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            // method 是否在一个range 以及parameter
            if(LinkUtil.isRangeWithin(stmt,method)==1){
                for(String params:methodData.parameterName){
                    for(String vars:stmtData.variableLocal){
                        if(params.equals(vars)){
                            Action curAction = stmt.clusteredActionBean.curAction;
                            Tree node = (Tree) curAction.getNode();
                            String methodName = LinkUtil.findResidingMethodName(node);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_INNER_USE,vars,methodName);
                            Link link = new Link(changeEntityData.fileName,stmt,method, desc,null);
                            changeEntityData.mLinks.add(link);
                            return;
                        }
                    }
                }
            }
        }else{
            // method name是否在invoke的list里面
            for(String methodInvokes:stmtData.methodInvocation){
                if(methodData.methodName.contains(methodInvokes)){
                    Action curAction = stmt.clusteredActionBean.curAction;
                    Tree node = (Tree) curAction.getNode();
                    String methodName = LinkUtil.findResidingMethodName(node);
                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.METHOD_INNER_INVOKE,methodInvokes, methodName);
                    Link link = new Link(changeEntityData.fileName,stmt,method, desc,null);
                    changeEntityData.mLinks.add(link);
                }
            }

        }

    }

    public static void checkStmtFieldAssociation(ChangeEntityData changeEntityData, ChangeEntity stmt,FieldChangeEntity field){
        StmtData stmtData = (StmtData) stmt.linkBean;
        //System.out.println(stmt.changeEntityId);
        FieldData fieldData = (FieldData) field.linkBean;
        for(String a:fieldData.fieldName){
            //System.out.println("这是域的名称："+a );
            for(String b : stmtData.variableField){
                //System.out.println("这是语句的变量名：" + b);
                if(a.equals(b)){
                    Action curAction = stmt.clusteredActionBean.curAction;
                    Tree node = (Tree) curAction.getNode();
                    String methodName = LinkUtil.findResidingMethodName(node);
                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_INNER_USE,a,methodName);
                    Link link = new Link(changeEntityData.fileName,stmt,field,desc,null);
                    changeEntityData.mLinks.add(link);
                }
            }
        }

    }

    public static void checkFieldMethodAssociation(ChangeEntityData changeEntityData, ChangeEntity method, ChangeEntity field){
        String reg = "[^a-zA-Z0-9_]";
        Pattern match = Pattern.compile(reg);

        MethodData methodData = (MethodData)method.linkBean;
        List<String> methodName = methodData.methodName;
        String range = method.stageIIIBean.getRange();
        List<Integer> rangeNum = DiffFileReader.parserRange(range);
        String actionType = method.stageIIIBean.getType2();
        String diffContent = "";
        if(actionType.equals("Delete")){
            if(rangeNum.size()==4){
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getPrevFile),rangeNum.get(0),rangeNum.get(1));
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getCurrFile),rangeNum.get(2),rangeNum.get(3));
            }else{
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getPrevFile),rangeNum.get(0),rangeNum.get(1));
            }
        }else{
            if(rangeNum.size()==4){
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getPrevFile),rangeNum.get(0),rangeNum.get(1));
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getCurrFile),rangeNum.get(2),rangeNum.get(3));
            }else{
                diffContent += DiffFileReader.readAppointedLineNumber(new File(Global.getCurrFile),rangeNum.get(0),rangeNum.get(1));
            }
        }
        Matcher mp = match.matcher(diffContent);
        String[] content = mp.replaceAll(" ").split(" ");
        List<String> contentList = Arrays.asList(content);

        FieldData fieldData = (FieldData) field.linkBean;
        List<String> fieldName = fieldData.fieldName;
        //System.out.println(diffContent);
        //System.out.println(fieldName);
        for(String singleField : fieldName){
            if(contentList.contains(singleField)){
                String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_INNER_USE,singleField,methodName);
                Link link = new Link(changeEntityData.fileName,method,field,desc,null);
                changeEntityData.mLinks.add(link);
                break;
            }
        }

    }
}
