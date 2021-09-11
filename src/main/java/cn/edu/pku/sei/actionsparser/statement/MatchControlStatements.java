package cn.edu.pku.sei.actionsparser.statement;

import cn.edu.pku.sei.actionsparser.bean.ChangePacket;
import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.actionsparser.util.AstRelations;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.statement.BreakContinueEntity;
import com.github.gumtreediff.actions.model.Action;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by huangkaifeng on 2018/2/7.
 *
 */
public class MatchControlStatements {

    public static void matchBreakStatements(MiningActionData fp, Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        subActions.add(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        BreakContinueEntity code = new BreakContinueEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_BREAK);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.addOneChangeEntity(code);
        fp.setActionTraversedMap(a);
    }
    public static void matchContinueStatements(MiningActionData fp,Action a){
        ChangePacket changePacket = new ChangePacket();
        List<Action> subActions = new ArrayList<>();
        subActions.add(a);
        ClusteredActionBean mBean = new ClusteredActionBean(ChangeEntityDesc.StageITraverseType.TRAVERSE_UP_DOWN,a,subActions,changePacket);
        BreakContinueEntity code = new BreakContinueEntity(mBean);
        code.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_GT_UD);
        code.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_STATEMENT);
        code.stageIIBean.setOpt(ChangeEntityDesc.getChangeEntityDescString(a));
        code.stageIIBean.setChangeEntity(ChangeEntityDesc.StageIIENTITY.ENTITY_CONTINUE);
//        code.stageIIBean.setOpt2(null);// 暂时不设置
        code.stageIIBean.setSubEntity(null);
        code.stageIIBean.setLineRange(code.lineRange.toString());
        code.stageIIBean.setLocation(AstRelations.getLocationString(a.getNode()));
        fp.setActionTraversedMap(a);
        fp.addOneChangeEntity(code);
    }
}
