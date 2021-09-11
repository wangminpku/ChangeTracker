package cn.edu.pku.sei.codelinks;

import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.actionsparser.util.MyList;
import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.LayeredChangeEntityContainer;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.StatementPlusChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.ClassChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.FieldChangeEntity;
import cn.edu.pku.sei.changeentity.memeber.MethodChangeEntity;
import cn.edu.pku.sei.codelinks.bean.ClassData;
import cn.edu.pku.sei.codelinks.bean.FieldData;
import cn.edu.pku.sei.codelinks.bean.MethodData;
import cn.edu.pku.sei.codelinks.bean.StmtData;
import cn.edu.pku.sei.codelinks.member.LinkMember2Member;
import cn.edu.pku.sei.codelinks.member.LinkStatement2Member;
import cn.edu.pku.sei.codelinks.member.LinkStatement2Statement;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FileInnerLinksGenerator {

    private ChangeEntityData changeEntityData;

    public FileInnerLinksGenerator(ChangeEntityData mod){
        this.changeEntityData = mod;
        this.changeEntityData.mLinks = new MyList<>();
    }

    public void initLinkBean(ChangeEntity ce, MiningActionData miningActionData){

        if(ce instanceof ClassChangeEntity){
            ce.linkBean = new ClassData((ClassChangeEntity)ce);
        }else if(ce instanceof FieldChangeEntity){
            ce.linkBean = new FieldData((FieldChangeEntity)ce);
        }else if(ce instanceof MethodChangeEntity){
            ce.linkBean = new MethodData((MethodChangeEntity)ce,miningActionData);
        }else if(ce instanceof StatementPlusChangeEntity){
            ce.linkBean = new StmtData((StatementPlusChangeEntity)ce,miningActionData.preprocessedData);
        }else{
            System.err.println("[ERR]not included");
        }

    }

    public void checkStatement2StatementAssoInMethod(List<ChangeEntity> stmts){
        for (int i = 0; i < stmts.size(); i++) {
            ChangeEntity ce1 = stmts.get(i);
            for (int j = i + 1; j < stmts.size(); j++) {
                ChangeEntity ce2 = stmts.get(j);
                if(ce1 instanceof StatementPlusChangeEntity && ce2 instanceof StatementPlusChangeEntity) {
                    //System.out.println("存在这样的两个单个实体满足一个方法内定义使用关系");
                    LinkStatement2Statement.checkStmtAssociation(changeEntityData, ce1, ce2);
                }
            }
        }
    }

    public void checkStatement2StatementShareField(List<ChangeEntity> stmtChangeEntity){
        for(int i =0;i<stmtChangeEntity.size();i++){
            for(int j =i+1;j<stmtChangeEntity.size();j++){
                ChangeEntity ce1 = stmtChangeEntity.get(i);
                ChangeEntity ce2 = stmtChangeEntity.get(j);
                LinkStatement2Statement.checkStmtShareField(changeEntityData,ce1,ce2);
            }
        }
    }

    public void checkMethod2MethodAssoInClass(List<ChangeEntity> methodChangeEntity){
        for (int i = 0; i < methodChangeEntity.size(); i++) {
            for (int j = i + 1; j < methodChangeEntity.size(); j++) {
                ChangeEntity ce1 = methodChangeEntity.get(i);
                ChangeEntity ce2 = methodChangeEntity.get(j);
                // method与method之间 todo
                LinkMember2Member.checkMethodAssociation(changeEntityData,ce1, ce2);
            }
        }
    }

    public void checkStmt2Method(List<ChangeEntity> stmts,List<ChangeEntity> methods){
        for(ChangeEntity ce:stmts) {
            for(ChangeEntity methodEntity:methods) {
                LinkStatement2Member.checkStmtMethodAssociation(changeEntityData, ce, (MethodChangeEntity) methodEntity);
            }
        }
    }

    public void checkStmt2Field(List<ChangeEntity> stmts,List<ChangeEntity> fieldChangeEntity){
        for(ChangeEntity ce:stmts) {
            fieldChangeEntity.forEach(a -> {
                //System.out.println("这是域和语句的关联关系的实体判别。。。");
                LinkStatement2Member.checkStmtFieldAssociation(changeEntityData, ce, (FieldChangeEntity) a);
            });
        }
    }

    public void checkField2Method(List<ChangeEntity> methods,List<ChangeEntity> fieldChangeEntity){
        for(ChangeEntity mce : methods){
            fieldChangeEntity.forEach(a -> {
                LinkStatement2Member.checkFieldMethodAssociation(changeEntityData,mce,(FieldChangeEntity) a);
            });
        }
    }

    /**
     * main entrance
     */
    public void generateFile() {
        LayeredChangeEntityContainer container = this.changeEntityData.entityContainer;
        List<ChangeEntity> entities = this.changeEntityData.mad.getChangeEntityList();
        //System.out.println(entities.size());
        for(ChangeEntity a:entities) {
            initLinkBean(a, changeEntityData.mad);
        }
        if(container==null){
            System.out.println("没有任何更改实体");
            //return;
        }else{
            Map<BodyDeclarationPair, List<ChangeEntity>> mMap = container.getLayerMap();
            //System.out.println(mMap.size());
            List<ChangeEntity> methodChangeEntity = new ArrayList<>();
            List<ChangeEntity> fieldChangeEntity = new ArrayList<>();
            List<ChangeEntity> innerClassChangeEntity = new ArrayList<>();
            List<ChangeEntity> stmtChangeEntity = new ArrayList<>();

            for (Map.Entry<BodyDeclarationPair, List<ChangeEntity>> entry : mMap.entrySet()) {
                BodyDeclarationPair key = entry.getKey();
                if (key.getBodyDeclaration() instanceof MethodDeclaration) {
                    List<ChangeEntity> mList = entry.getValue();
                    //System.out.println("这是方法体对的个数：" + mList.size());
                    mList.forEach(a->{
                        if(a instanceof StatementPlusChangeEntity){
                            stmtChangeEntity.add(a);
                            //System.out.println("这是更改实体编号: "+ a.changeEntityId);
                        }
                    });
                    checkStatement2StatementAssoInMethod(mList);// method内stmt之间 定义本地变量 随后被use
                }
                if(key.getBodyDeclaration() instanceof TypeDeclaration){
                    List<ChangeEntity> mList = entry.getValue();
                    for(ChangeEntity tmp:mList){
                        if(tmp instanceof MethodChangeEntity){
                            methodChangeEntity.add(tmp);
                        }
                        if(tmp instanceof ClassChangeEntity){
                            innerClassChangeEntity.add(tmp);
                        }
                        if(tmp instanceof FieldChangeEntity){
                            fieldChangeEntity.add(tmp);
                        }
                    }
                }
            }
            //System.out.println("这是语句的实体个数："+stmtChangeEntity.size()+"这是域的实体个数："+ fieldChangeEntity.size());
            checkStmt2Field(stmtChangeEntity,fieldChangeEntity); // stmt field access 与field insert 定义field，随后被use
            checkStmt2Method(stmtChangeEntity,methodChangeEntity);// stmt invoke method 定义method，然后调用method
            checkField2Method(methodChangeEntity,fieldChangeEntity);
//        checkMethod2MethodAssoInClass(methodChangeEntity); // method之间
           //checkStatement2StatementShareField(stmtChangeEntity); // stmt跨方法之间 共享field
        }

    }

}
