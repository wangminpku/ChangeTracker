package cn.edu.pku.sei.changeentity.memeber;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.MemberPlusChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import cn.edu.pku.sei.preprocessdata.MyRange;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import java.util.List;

public class FieldChangeEntity extends MemberPlusChangeEntity {

    /**
     * 预处理识别的
     */
    public FieldChangeEntity(BodyDeclarationPair fieldDeclarationPair, String changeType, MyRange myRange){
        super(fieldDeclarationPair.getLocationClassString(),changeType,myRange);
        FieldDeclaration fd = (FieldDeclaration) fieldDeclarationPair.getBodyDeclaration();
        this.stageIIBean.setLocation(fieldDeclarationPair.getLocationClassString());
        this.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_FIELD);
        this.bodyDeclarationPair = fieldDeclarationPair;
        List<VariableDeclarationFragment> list = fd.fragments();
        String res = "";
        for(VariableDeclarationFragment vd:list){
            res += vd+",";
        }
        this.stageIIBean.setThumbnail(fieldDeclarationPair.getLocationClassString() + res);

    }

    /**
     * gumtree 识别的
     * @param bean
     */
    public FieldChangeEntity(ClusteredActionBean bean){
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
