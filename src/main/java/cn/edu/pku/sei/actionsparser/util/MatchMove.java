package cn.edu.pku.sei.actionsparser.util;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.memeber.ClassChangeEntity;
import com.github.gumtreediff.actions.model.Action;

import java.util.ArrayList;
import java.util.List;

public class MatchMove {

    public static void matchMove(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        BasicTreeTraversal.traverseWhenActionIsMove(a,subActions,changePacket,true);
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        ClassChangeEntity code = new ClassChangeEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_CLASS);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_CLASS);
//        code.stageIIBean.setOpt2(null);
        code.stageIIBean.setSubEntity(ChangeEntityDesc.StageIISub.SUB_CONDITION_AND_BODY);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(subActions);
        fp.addOneChangeEntity(code);
    }
}
