package cn.edu.pku.sei.actionsparser.statement;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.actionsparser.util.AstRelations;
import cn.edu.pku.sei.actionsparser.util.BasicTreeTraversal;
import cn.edu.pku.sei.actionsparser.util.DefaultDownUpTraversal;
import cn.edu.pku.sei.actionsparser.util.DefaultUpDownTraversal;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.statement.ReturnChangeEntity;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;


import java.util.ArrayList;
import java.util.List;

public class MatchReturnStatement {
    public static void matchReturnStatement(MiningActionData fp, Action a) {
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,false)){
            DefaultUpDownTraversal.traverseTypeIStatements(a, subActions, changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        ReturnChangeEntity code = new ReturnChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_RETURN_STMT);
//        code.stageIIBean.setOpt2(null);
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(subActions);
        fp.addOneChangeEntity(code);
    }

    public static void matchReturnChangeNewEntity(MiningActionData fp, Action a, Tree queryFather,int treeType, Tree traverseFather){
        ChangePacket changePacket = new ChangePacket();
        List<Action> sameEdits = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,sameEdits,changePacket,false)){
            DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,sameEdits,changePacket);
        }
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_DOWN_UP,a,sameEdits,changePacket,queryFather,treeType);
        ReturnChangeEntity code = new ReturnChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_DUD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        if(a instanceof Move){
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE_MOVE);
            code.stageIIBean.setChangeEntity(((Tree)a.getNode()).getAstClass().getSimpleName());
        }else {
            code.stageIIBean.setOpt(ChangeEntityDesc.StageIIOpt.OPT_CHANGE);
            code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_RETURN_STMT);
        }
//        code.stageIIBean.setOpt2(null);
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(sameEdits);
        fp.addOneChangeEntity(code);
    }

    public static void matchReturnChangeCurrEntity(MiningActionData fp, Action a, ChangeEntity changeEntity, Tree traverseFather){
        ChangePacket changePacket = changeEntity.clusteredActionBean.changePacket;
        List<Action> subActions = null;
            subActions = changeEntity.clusteredActionBean.actions;
        List<Action> newActions = new ArrayList<>();
        if(!BasicTreeTraversal.traverseWhenActionIsMove(a,newActions,changePacket,false)){
            DefaultDownUpTraversal.traverseFatherNodeGetSameNodeActions(traverseFather,newActions,changePacket);

        }
        for(Action tmp:newActions){
            if(fp.mGeneratingActionsData.getAllActionMap().get(tmp)==1){
                continue;
            }
            subActions.add(tmp);
        }
//        changeEntity.linkBean.addAppendedActions(newActions);
        fp.setActionTraversedMap(newActions);

    }
}
