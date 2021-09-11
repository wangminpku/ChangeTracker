package cn.edu.pku.sei.changeentity.memeber;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.MemberPlusChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import cn.edu.pku.sei.preprocessdata.MyRange;
import org.eclipse.jdt.core.dom.EnumDeclaration;

import java.util.List;

public class EnumChangeEntity extends MemberPlusChangeEntity {
    public EnumChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    final public static String enumStr = "Enum";

    public List<String> variableList;
    public List<String> methodList;
    public MyRange dstRange;

    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange1,MyRange myRange2){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange1);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());
        this.dstRange = myRange2;


    }

    public EnumChangeEntity(BodyDeclarationPair bodyDeclarationPair, String changeType, MyRange myRange1){
        super(bodyDeclarationPair.getLocationClassString(),changeType,myRange1);
        EnumDeclaration ed = (EnumDeclaration) bodyDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(bodyDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_ENUM);
        this.stageIIBean.setThumbnail(ed.getName().toString());

    }

    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.toString2());
        return sb.toString();
    }
}
