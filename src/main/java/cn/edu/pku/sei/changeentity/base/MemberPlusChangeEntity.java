package cn.edu.pku.sei.changeentity.base;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.preprocessdata.BodyDeclarationPair;
import cn.edu.pku.sei.preprocessdata.MyRange;

public class MemberPlusChangeEntity extends ChangeEntity {

    public BodyDeclarationPair bodyDeclarationPair;

    public MemberPlusChangeEntity(ClusteredActionBean bean){
        super(bean);
    }

    public MemberPlusChangeEntity(String location,String changeType,MyRange myRange){
        super(location,changeType,myRange);

    }

}
