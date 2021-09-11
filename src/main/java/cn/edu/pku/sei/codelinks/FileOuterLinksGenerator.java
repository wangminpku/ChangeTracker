package cn.edu.pku.sei.codelinks;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.StatementPlusChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.ClassChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.FieldChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.MethodChangeEntity;
import cn.edu.pku.sei.codelinks.bean.ClassData;
import cn.edu.pku.sei.codelinks.bean.FieldData;
import cn.edu.pku.sei.codelinks.bean.MethodData;
import cn.edu.pku.sei.codelinks.bean.StmtData;
import cn.edu.pku.sei.codelinks.similariity.TreeDistance;
import cn.edu.pku.sei.codelinks.util.Link;
import cn.edu.pku.sei.codelinks.util.LinkUtil;
import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.fileutil.DiffFileReader;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;

import java.io.File;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileOuterLinksGenerator {

    public ChangeEntityData ce1;

    public ChangeEntityData ce2;
    public List<Link> mAssos;

    public FileOuterLinksGenerator() {
        mAssos = new ArrayList<>();
    }


    public void generateOutsideAssociation(ChangeEntityData ca, ChangeEntityData cb, List<String> prevFilePathList, List<String> currFilePathList) {
        mAssos.clear();
        ce1 = ca;
        ce2 = cb;
        List<ChangeEntity> methodAList = methodDeclarations(ca);
        List<ChangeEntity> methodBList = methodDeclarations(cb);

        List<ChangeEntity> fieldAList = fieldDeclarations(ca);
        List<ChangeEntity> fieldBList = fieldDeclarations(cb);

        List<ChangeEntity> stmtAList = stmtChange(ca);
        List<ChangeEntity> stmtBList = stmtChange(cb);

        List<ChangeEntity> classAList = classChange(ca);
        List<ChangeEntity> classBList = classChange(cb);

        checkMethodInvokes(methodAList, stmtBList, 1);
        checkMethodInvokes(methodBList, stmtAList, 0);

        checkClassInvokes(classAList, stmtBList, 1);
        checkClassInvokes(classBList, stmtAList, 0);

        checkStmt2FieldUesd(fieldAList,stmtBList,1,prevFilePathList,currFilePathList);
        checkStmt2FieldUesd(fieldBList,stmtAList,0,prevFilePathList,currFilePathList);

        checkMethod2FieldUesd(fieldAList,methodBList,1,prevFilePathList,currFilePathList);
        checkMethod2FieldUesd(fieldBList,methodAList,0,prevFilePathList,currFilePathList);

        checkMethodInheritage(methodAList, methodBList);


    }

    public List<ChangeEntity> methodDeclarations(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> methodDeclarationList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof MethodChangeEntity) {
                methodDeclarationList.add(ce);
            }
        }
        return methodDeclarationList;
    }

    public List<ChangeEntity> fieldDeclarations(ChangeEntityData ced){
        List<ChangeEntity> fList = ced.mad.getChangeEntityList();
        List<ChangeEntity> fieldDeclarationList = new ArrayList<>();
        for(ChangeEntity ce : fList){
            if(ce instanceof FieldChangeEntity){
                fieldDeclarationList.add(ce);
            }
        }
        return fieldDeclarationList;
    }

    public List<ChangeEntity> stmtChange(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> stmtList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof StatementPlusChangeEntity) {
                stmtList.add(ce);
            }
        }
        return stmtList;
    }

    public List<Link> checkSimilarity(Map<String, ChangeEntityData> mMap, TotalFileLinks totalFileLinks) {
        List<ChangeEntity> totalEntityList = new ArrayList<>();
        Map<Integer,String> changeEntityDataDict = new HashMap<>();
        List<Integer> indexList = new ArrayList<>();
        List<Link> simLink = new ArrayList<>();

        for (Map.Entry<String, ChangeEntityData> entry : mMap.entrySet()) {
            //System.out.println(entry.getKey());
            for (ChangeEntity tmp : entry.getValue().mad.getChangeEntityList()) {
                if (indexList.contains(tmp.getChangeEntityId())) {
                    continue;
                }
                totalEntityList.add(tmp);
                changeEntityDataDict.put(tmp.getChangeEntityId(),entry.getKey());
                indexList.add(tmp.getChangeEntityId());
            }
        }
        List<Class> instances = new ArrayList<>();
        totalEntityList.forEach(a -> {
            if (!instances.contains(a.getClass())) {
                instances.add(a.getClass());
            }
        });
        for (Class clazz : instances) {
            List<ChangeEntity> someTypeOfChangeClassA = new ArrayList<>();
            for (ChangeEntity c : totalEntityList) {
                if (c.getClass().equals(clazz)) {
                    someTypeOfChangeClassA.add(c);
                }
            }
            for (int i = 0; i < someTypeOfChangeClassA.size(); i++) {
                for (int j = i + 1; j < someTypeOfChangeClassA.size(); j++) {
                    ChangeEntity a = someTypeOfChangeClassA.get(i);
                    ChangeEntity b = someTypeOfChangeClassA.get(j);
                    if (!a.stageIIBean.getOpt().equals(b.stageIIBean.getOpt())
                            || a.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE)) {
                        continue;
                    }
                    if (a.clusteredActionBean != null && b.clusteredActionBean != null) {

                        if (a.clusteredActionBean.fafather.getAstClass().equals(b.clusteredActionBean.fafather.getAstClass())
                                && a.clusteredActionBean.actions.size() == b.clusteredActionBean.actions.size() && !a.stageIIBean.getChangeEntity().equals("VariableDeclaration") ) {


                            TreeDistance treeDistance = new TreeDistance(a.clusteredActionBean.fafather, b.clusteredActionBean.fafather);
                            float distance = treeDistance.calculateTreeDistance();
                            //System.out.println(distance);
                            if (distance <= 0.8) {
                                String fileNameA = changeEntityDataDict.get(a.getChangeEntityId()).split("@@@")[1];
                                String fileNameB = changeEntityDataDict.get(b.getChangeEntityId()).split("@@@")[1];
                                if(fileNameA.equals(fileNameB)){
                                    Link link = new Link(fileNameA,a,b,ChangeEntityDesc.StageIIIAssociationType.SIMSILAR_CHANGE,null);
                                    totalFileLinks.addEntry(changeEntityDataDict.get(a.getChangeEntityId()),link);
                                    simLink.add(link);
                                }else{
                                    Link link = new Link(fileNameA,fileNameB,a,b,ChangeEntityDesc.StageIIIAssociationType.SIMSILAR_CHANGE,null);
                                    totalFileLinks.addFile2FileAssos(changeEntityDataDict.get(a.getChangeEntityId()),changeEntityDataDict.get(b.getChangeEntityId()),link);
                                    simLink.add(link);
                                }
                            }
                        }
                    }
                }
            }

        }
        return simLink;
    }


    public List<ChangeEntity> classChange(ChangeEntityData ced) {
        List<ChangeEntity> mList = ced.mad.getChangeEntityList();
        List<ChangeEntity> classList = new ArrayList<>();
        for (ChangeEntity ce : mList) {
            if (ce instanceof ClassChangeEntity) {
                //System.out.println("这是发生了类的级别的更改：" + ce.changeEntityId + "   " + ce.stageIIBean.getChangeEntity());
                classList.add(ce);
            }
        }
        return classList;
    }


    public void checkMethodInvokes(List<ChangeEntity> methodList, List<ChangeEntity> stmtList, int flag) {
        for (ChangeEntity cmethod : methodList) {
            MethodChangeEntity mm = (MethodChangeEntity) cmethod;
            if (mm.linkBean == null) continue;
            MethodData mdata = (MethodData) mm.linkBean;
            for (ChangeEntity cstmt : stmtList) {
                StatementPlusChangeEntity stmt = (StatementPlusChangeEntity) cstmt;
                if (stmt.linkBean != null) {
                    StmtData stmtData = (StmtData) stmt.linkBean;
                    for (String s : stmtData.methodInvocation) {
                        if (mdata.methodName.contains(s)) {
                            Action curAction = stmt.clusteredActionBean.curAction;
                            Tree node = (Tree) curAction.getNode();
                            String methodName = LinkUtil.findResidingMethodName(node);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.METHOD_OUTER_INVOKE, s, methodName);
                            if (flag == 1) {
                                Link link = new Link(ce1.fileName, ce2.fileName, cmethod, cstmt, desc, s);
                                System.out.println(link.toString());
                                mAssos.add(link);
                            } else {
                                Link link = new Link(ce2.fileName, ce1.fileName, cmethod, cstmt, desc, s);
                                System.out.println(link.toString());
                                mAssos.add(link);
                            }
                            break;
                        }
                    }
                }
            }
        }

    }

    public void checkMethodInheritage(List<ChangeEntity> methodListA, List<ChangeEntity> methodListB) {
        String classA = ce1.fileName.substring(0, ce1.fileName.length() - 5);
        //System.out.println(classA);
        String classB = ce2.fileName.substring(0, ce2.fileName.length() - 5);
        //System.out.println(classB);
        List<MethodChangeEntity> mc1 = new ArrayList<>();
        List<MethodChangeEntity> mc2 = new ArrayList<>();
        for (ChangeEntity ce : methodListA) {
            if (ce instanceof MethodChangeEntity) {
                MethodChangeEntity mce = (MethodChangeEntity) ce;
//                if(mce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
                mc1.add(mce);
//                }
            }
        }
        for (ChangeEntity ce : methodListB) {
            if (ce instanceof MethodChangeEntity) {
                MethodChangeEntity mce = (MethodChangeEntity) ce;
//                if(mce.stageIIBean.getEntityCreationStage().equals(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF)){
                mc2.add(mce);
//                }
            }
        }


        //System.out.println("这是出错的文件: " + ce2.fileName + "    " + ce2.mad.preprocessedData);
        if(ce2.mad.preprocessedData != null && ce1.mad.preprocessedData != null){
            List<String> superClasses1 = ce1.mad.preprocessedData.getInterfacesAndFathers();
            List<String> superClasses2 = ce2.mad.preprocessedData.getInterfacesAndFathers();
            for (MethodChangeEntity m : mc1) {
                for (MethodChangeEntity n : mc2) {
                    MethodData methodData1 = (MethodData) m.linkBean;
                    MethodData methodData2 = (MethodData) n.linkBean;
                    //Override-Method: methodName overriden in B.java
                    //Abstract-Method: methodName implemented in A.java
                    //Implement-Method: methodName implemented interface A.java
                    //System.out.println(methodData1.methodName + "  " + methodData2.methodName );
                    if(methodData1.methodName.size()>0&&methodData2.methodName.size()>0){
                        if (methodData1.methodName.get(0).equals(methodData2.methodName.get(0))) {
                            if (superClasses1.contains("superclass---"+classB)) { // B是A的父类
                                if(superClasses2.contains("abstract---"+classB)) {
                                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.ABSTRACT_METHOD, methodData1.methodName.get(0),classB);
                                    Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                    mAssos.add(link);
                                }else{
                                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.OVERRIDE_METHOD, methodData1.methodName.get(0),classB);
                                    Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                    mAssos.add(link);
                                }
                            } else if(superClasses1.contains("interface---"+classB)){ // B是A的接口
                                String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.IMPLEMENT_METHOD, methodData1.methodName.get(0),classB);
                                Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                mAssos.add(link);

                            }

                            if(superClasses2.contains("superclass---"+classA)) { // A是B的父类
                                if(superClasses1.contains("abstract---"+classA)){
                                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.ABSTRACT_METHOD, methodData1.methodName.get(0),classA);
                                    Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                    mAssos.add(link);
                                }else{
                                    String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.OVERRIDE_METHOD, methodData1.methodName.get(0),classA);
                                    Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                    mAssos.add(link);
                                }
                            } else if(superClasses2.contains("interface---"+classA)) { // A是B的接口
                                String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.IMPLEMENT_METHOD, methodData1.methodName.get(0),classA);
                                Link link = new Link(ce1.fileName, ce2.fileName, m, n, desc, null);
                                mAssos.add(link);
                            }
                        }
                    }

                }
            }
        }


    }

    public void checkClassInvokes(List<ChangeEntity> classList, List<ChangeEntity> stmtList, int flag) {
        for (ChangeEntity cclass : classList) {
            ClassChangeEntity mm = (ClassChangeEntity) cclass;
            if (mm.linkBean == null) continue;
            ClassData mdata = (ClassData) mm.linkBean;
            //System.out.println(mdata.clazzName);
            for (ChangeEntity cstmt : stmtList) {
                StatementPlusChangeEntity stmt = (StatementPlusChangeEntity) cstmt;
                if (stmt.linkBean != null) {
                    StmtData stmtData = (StmtData) stmt.linkBean;

                    if (stmtData.classCreation.contains(mdata.clazzName)) {
                        //System.out.println("这是调用语句：" + stmtData.classCreation + "   " + "这是调用的类名：" + mdata.clazzName);
                        String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.CLASS_INSTANCE, stmtData.classCreation.toString(),mdata.clazzName);
                        if (flag == 1) {
                            Link link = new Link(ce1.fileName, ce2.fileName, cclass, cstmt, desc, mdata.clazzName);
                            mAssos.add(link);
                        } else {
                            Link link = new Link(ce2.fileName, ce1.fileName, cclass, cstmt, desc, mdata.clazzName);
                            mAssos.add(link);
                        }
                    }
                    for (String s : stmtData.methodInvocation) {

                        if (mdata.methods.contains(s)) {
                            //System.out.println("这是这个类： "+ mdata.clazzName+ "  包含的方法：" + mdata.methods);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.CLASS_METHOD_INVOKE, s, mdata.clazzName);
                            if (flag == 1) {
                                Link link = new Link(ce1.fileName, ce2.fileName, cclass, cstmt, desc, mdata.clazzName);
                                mAssos.add(link);
                            } else {
                                Link link = new Link(ce2.fileName, ce1.fileName, cclass, cstmt, desc, mdata.clazzName);
                                mAssos.add(link);
                            }
                            break;
                        }
                    }
                }
            }
        }
    }

    public void checkStmt2FieldUesd(List<ChangeEntity> fieldList,List<ChangeEntity> stmtList, int flag,List<String> prevFilePathList,List<String> currFilePathList){
        String reg = "[^a-zA-Z0-9_]";
        Pattern match = Pattern.compile(reg);

        for(ChangeEntity fce:fieldList ){
            FieldChangeEntity fieldChangeEntity = (FieldChangeEntity) fce;
            if(fieldChangeEntity.linkBean == null) continue;
            FieldData fieldData = (FieldData)fieldChangeEntity.linkBean;
            List<String> fieldNameList = fieldData.fieldName;
            for(ChangeEntity sce : stmtList){
                if(flag == 1){
                    String fileName = ce2.fileName;
                    for(String filePath1 : prevFilePathList){
                        String fileNamePath=filePath1.substring(filePath1.lastIndexOf("/")+1,filePath1.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getPrevFile = filePath1;
                            break;
                        }
                    }
                    //System.out.println(Global.prevFilePath);
                    for(String filePath2 : currFilePathList ){
                        String fileNamePath=filePath2.substring(filePath2.lastIndexOf("/")+1,filePath2.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getCurrFile = filePath2;
                            break;
                        }
                    }

                    String range = sce.stageIIIBean.getRange();
                    List<Integer> rangeNum = DiffFileReader.parserRange(range);
                    String actionType = sce.stageIIIBean.getType2();
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

                    for(String fieldName : fieldNameList){
                        if(contentList.contains(fieldName)){
                            //System.out.println(fieldChangeEntity.changeEntityId+ "   " + sce.changeEntityId);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_OUTER_USE, fieldName, sce);
                            Link link = new Link(ce1.fileName, ce2.fileName, fieldChangeEntity, sce, desc, ce2.fileName);
                            mAssos.add(link);
                            break;
                        }
                    }
                }

                if(flag == 0){
                    String fileName = ce1.fileName;
                    for(String filePath1 : prevFilePathList){
                        String fileNamePath=filePath1.substring(filePath1.lastIndexOf("/")+1,filePath1.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getPrevFile = filePath1;
                            break;
                        }
                    }
                    //System.out.println(Global.prevFilePath);
                    for(String filePath2 : currFilePathList ){
                        String fileNamePath=filePath2.substring(filePath2.lastIndexOf("/")+1,filePath2.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getCurrFile = filePath2;
                            break;
                        }
                    }

                    String range = sce.stageIIIBean.getRange();
                    List<Integer> rangeNum = DiffFileReader.parserRange(range);
                    String actionType = sce.stageIIIBean.getType2();
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

                    for(String fieldName : fieldNameList){
                        if(contentList.contains(fieldName)){
                            //System.out.println(fieldChangeEntity.changeEntityId+ "   " + sce.changeEntityId);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_OUTER_USE, fieldName, sce);
                            Link link = new Link(ce2.fileName, ce1.fileName, fieldChangeEntity, sce, desc, ce1.fileName);
                            mAssos.add(link);
                            break;
                        }
                    }
                }

            }
        }
    }


    public void checkMethod2FieldUesd(List<ChangeEntity> fieldList,List<ChangeEntity> methodList, int flag,List<String> prevFilePathList,List<String> currFilePathList){
        String reg = "[^a-zA-Z0-9_]";
        Pattern match = Pattern.compile(reg);

        for(ChangeEntity fce:fieldList ){
            FieldChangeEntity fieldChangeEntity = (FieldChangeEntity) fce;
            if(fieldChangeEntity.linkBean == null) continue;
            FieldData fieldData = (FieldData)fieldChangeEntity.linkBean;
            List<String> fieldNameList = fieldData.fieldName;

            for(ChangeEntity mce : methodList){
                if(flag == 1){
                    String fileName = ce2.fileName;
                    for(String filePath1 : prevFilePathList){
                        String fileNamePath=filePath1.substring(filePath1.lastIndexOf("/")+1,filePath1.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getPrevFile = filePath1;
                            break;
                        }
                    }
                    //System.out.println(Global.prevFilePath);
                    for(String filePath2 : currFilePathList ){
                        String fileNamePath=filePath2.substring(filePath2.lastIndexOf("/")+1,filePath2.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getCurrFile = filePath2;
                            break;
                        }
                    }
                    //System.out.println(Global.getPrevFile);
                    //System.out.println(Global.getCurrFile);

                    String range = mce.stageIIIBean.getRange();
                    //System.out.println(mce.changeEntityId + "---"+ range);
                    //System.out.println(ce2.fileName);
                    List<Integer> rangeNum = DiffFileReader.parserRange(range);
                    String actionType = mce.stageIIIBean.getType2();
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

                    for(String fieldName : fieldNameList){
                        if(contentList.contains(fieldName)){
                            //System.out.println(fieldChangeEntity.changeEntityId+ "   " + mce.changeEntityId);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_OUTER_USE, fieldName, mce);
                            Link link = new Link(ce1.fileName, ce2.fileName, fieldChangeEntity, mce, desc, ce2.fileName);
                            mAssos.add(link);
                            break;
                        }
                    }
                }

                if(flag == 0){
                    String fileName = ce1.fileName;
                    for(String filePath1 : prevFilePathList){
                        String fileNamePath=filePath1.substring(filePath1.lastIndexOf("/")+1,filePath1.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getPrevFile = filePath1;
                            break;
                        }
                    }
                    //System.out.println(Global.prevFilePath);
                    for(String filePath2 : currFilePathList ){
                        String fileNamePath=filePath2.substring(filePath2.lastIndexOf("/")+1,filePath2.length());
                        if(fileNamePath.equals(fileName)){
                            Global.getCurrFile = filePath2;
                            break;
                        }
                    }

                    //System.out.println(Global.getPrevFile);
                    //System.out.println(Global.getCurrFile);

                    String range = mce.stageIIIBean.getRange();
                    //System.out.println(mce.changeEntityId+"---"+range);
                    //System.out.println(ce1.fileName);
                    List<Integer> rangeNum = DiffFileReader.parserRange(range);
                    String actionType = mce.stageIIIBean.getType2();
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

                    for(String fieldName : fieldNameList){
                        if(contentList.contains(fieldName)){
                            //System.out.println(fieldChangeEntity.changeEntityId+ "   " + mce.changeEntityId);
                            String desc = String.format(ChangeEntityDesc.StageIIIAssociationType.DEF_OUTER_USE, fieldName, mce);
                            Link link = new Link(ce2.fileName, ce1.fileName, fieldChangeEntity, mce, desc, ce1.fileName);
                            mAssos.add(link);
                            break;
                        }
                    }
                }

            }
        }
    }
}
