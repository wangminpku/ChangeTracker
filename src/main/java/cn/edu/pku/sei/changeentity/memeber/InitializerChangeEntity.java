package cn.edu.pku.sei.changeentity.memeber;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.MemberPlusChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import cn.edu.pku.sei.preprocessdata.MyRange;
import org.eclipse.jdt.core.dom.Initializer;

public class InitializerChangeEntity extends MemberPlusChangeEntity {
    public InitializerChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public InitializerChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange);
        Initializer iid = (Initializer) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_INITIALIZER);
        this.stageIIBean.setThumbnail("{}");

    }


    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.toString2());
        return sb.toString();
    }
}
