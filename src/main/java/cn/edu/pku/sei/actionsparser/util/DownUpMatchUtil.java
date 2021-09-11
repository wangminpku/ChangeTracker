package cn.edu.pku.sei.actionsparser.util;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.generateactions.ActionConstants;

import java.util.List;

public class DownUpMatchUtil {

    public static void setChangePacket(ClusteredActionBean bean){
        if(bean.changePacket.getChangeSet1()==null){
            return;
        }else if(bean.changePacket.getChangeSet1() == null){
            setChangePacket(bean.changePacket,bean.changePacket.getChangeSet1());
        }else{
            //不存在
        }

    }



    private static void setChangePacket(ChangePacket changePacket, List<String> type){
//        changePacket.setOperationSubEntity(OperationTypeConstants.SUB_ENTITY_STRUCTURE_REFURNISH);
//        if(BaseMatchUtil.twoItemInsertAndNullAction(type)){
//        }else if(BaseMatchUtil.twoItemDeleteAndNullAction(type)){
//            changePacket.setOperationType(OperationTypeConstants.DELETE);
//        }else if(BaseMatchUtil.twoItemMoveAndNullAction(type)){
//            changePacket.setOperationType(OperationTypeConstants.MOVE);
//        }else if(BaseMatchUtil.twoItemUpdateAndNullAction(type)){
//            changePacket.setOperationType(OperationTypeConstants.UPDATE);
//        }else {
//            changePacket.setOperationType(OperationTypeConstants.MULTIPLE_EDIT);
//            changePacket.multiEditStr = generateMultiEditString(type);
//        }


    }

    private static String generateMultiEditString(List<String> types){
        String result = "";
        for(String tmp:types){
            if(!tmp.equals(ActionConstants.NULLACTION)){
                result += tmp+"_";
            }
        }
        return result.substring(0,result.length()-1);
    }
}
