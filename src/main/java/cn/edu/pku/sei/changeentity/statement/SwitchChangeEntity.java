package cn.edu.pku.sei.changeentity.statement;


import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.StatementPlusChangeEntity;

/**
 * Created by huangkaifeng on 2018/1/23.
 *
 */
public class SwitchChangeEntity extends StatementPlusChangeEntity {
    final static public String switchStatement = "Switch";
    final static public String switchCase = "Switch_Case";
    final static public String defaultCase = "Default SwitchCase";

    public SwitchChangeEntity(ClusteredActionBean bean) {
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
            if(this.stageIIBean.getChangeEntity().equals(ChangeEntityDesc.StageIIENTITY.ENTITY_SWITCH_CASE)){
                if(this.stageIIBean.getSubEntity().equals(ChangeEntityDesc.StageIISub.SUB_SWITCH_CASE_DEFAULT)){
                    sb.append(" ");
                    sb.append(this.stageIIBean.getSubEntity());
                }
            }else{
                sb.append(" ");
                sb.append(this.stageIIBean.getSubEntity());
                sb.append(" with/by...");
            }
        }else{
//            sb.append(" ");
//            sb.append(this.stageIIBean.getSubEntity());
        }
        return sb.toString();
    }


}
