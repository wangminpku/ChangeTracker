package cn.edu.pku.sei.changeentity.statement;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.StatementPlusChangeEntity;
/**
 * Created by huangkaifeng on 2018/2/8.
 *
 */
public class BreakContinueEntity extends StatementPlusChangeEntity {

    public BreakContinueEntity(ClusteredActionBean bean) {
        super(bean);
    }


    public String toString2(){
        StringBuilder sb = new StringBuilder();
        sb.append(this.changeEntityId);
        sb.append(". ");
        sb.append(this.stageIIBean.getOpt());
        sb.append(" ");
        sb.append(this.stageIIBean.getChangeEntity());
        if(this.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){
            sb.append("'s label ");
//            sb.append("");
//            sb.append("with/by...");
        }
        return sb.toString();
    }
}
