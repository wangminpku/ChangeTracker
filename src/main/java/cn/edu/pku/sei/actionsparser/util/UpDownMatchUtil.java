package cn.edu.pku.sei.actionsparser.util;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;

import java.util.Set;

public class UpDownMatchUtil {

    public static void setChangePacket2(ChangeEntity changeEntity){
//        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
//        if(changePacket.getChangeSet1() == null){
//            return;
//        }else if(changePacket.getChangeSet2() == null){
//            setChangePacket(bean.changePacket,bean.changePacket.changeSet1);
//        }else{
//            setChangePacket(bean.changePacket,bean.changePacket.changeSet1,bean.changePacket.changeSet2);
//        }
    }

    private static void setChangePacket(ChangeEntity changeEntity){
//        if(BaseMatchUtil.oneItemInsert(types1)&& BaseMatchUtil.oneItemInsert(types2)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }else if(BaseMatchUtil.oneItemDelete(types1)&& BaseMatchUtil.oneItemDelete(types2)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)&& BaseMatchUtil.oneItemNullAction(types2)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }else if(BaseMatchUtil.oneItemInsert(types1)&& BaseMatchUtil.oneItemNullAction(types2)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_WRAPPER);
//        }else if(BaseMatchUtil.oneItemDelete(types1)&& BaseMatchUtil.  (types2)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_WRAPPER);
//        }
    }

    private static void setChangePacket(ChangePacket changePacket, Set<String> types1){
//        if(BaseMatchUtil.oneItemInsert(types1)|| BaseMatchUtil.oneItemDelete(types1)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }else if(BaseMatchUtil.oneItemMoveOrTwoItemMoveAndNullAction(types1)){
//            changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_WHOLE_STRUCTURE);
//        }


    }
}
