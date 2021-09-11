package cn.edu.pku.sei.changeentity.memeber;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.MemberPlusChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import cn.edu.pku.sei.preprocessdata.MyRange;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class ClassChangeEntity extends MemberPlusChangeEntity {

    /**
     * 预处理 识别的
     */
    public ClassChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        TypeDeclaration cod = (TypeDeclaration)bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        if(cod.isInterface()){
            this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INTERFACE);
        }else{
            this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INNER_CLASS);
        }
        this.stageIIBean.setThumbnail(cod.getName().toString());
        this.bodyDeclarationPair = bodyDeclarationPair;
    }

    /**
     * gumtree 识别的 add/remove/modify
     * @param bean
     */
    public ClassChangeEntity(ClusteredActionBean bean){
        super(bean);
    }



    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.toString2());
        return sb.toString();

    }
}
