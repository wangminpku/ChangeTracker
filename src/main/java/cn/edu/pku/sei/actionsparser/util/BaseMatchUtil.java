package cn.edu.pku.sei.actionsparser.util;

import cn.edu.pku.sei.generateactions.ActionConstants;

import java.util.List;
import java.util.Set;

public class BaseMatchUtil {

    public static boolean oneItemInsert(List<String> type){
        if(type.size()==1&&type.contains(ActionConstants.INSERT)){
            return true;
        }
        return false;
    }

    public static boolean oneItemDelete(List<String> type){
        if(type.size()==1&&type.contains(ActionConstants.DELETE)){
            return true;
        }
        return false;

    }

    public static boolean oneItemMoveOrTwoItemMoveAndNullAction(List<String> type){
        if(type.size()==2&&type.contains(ActionConstants.MOVE)&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        if(type.size()==1&&type.contains(ActionConstants.MOVE)){
            return true;
        }
        return false;
    }
    public static boolean oneItemNullAction(Set<String> type){
        if(type.size()==1&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }


    public static boolean twoItemInsertAndNullAction(List<String> type){
        if(type.size()==2 && type.contains(ActionConstants.INSERT)&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }
    public static boolean twoItemDeleteAndNullAction(List<String> type){
        if(type.size()==2 &&type.contains(ActionConstants.DELETE)&& type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }
    public static boolean twoItemUpdateAndNullAction(List<String> type){
        if(type.size()==2&&type.contains(ActionConstants.UPDATE) && type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }
    public static boolean twoItemMoveAndNullAction(List<String> type){
        if(type.size()==2 &&type.contains(ActionConstants.MOVE)&&type.contains(ActionConstants.NULLACTION)){
            return true;
        }
        return false;
    }
}
