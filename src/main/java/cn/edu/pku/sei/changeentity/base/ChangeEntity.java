package cn.edu.pku.sei.changeentity.base;

import cn.edu.pku.sei.actionsparser.bean.ClusteredActionBean;
import cn.edu.pku.sei.actionsparser.util.AstRelations;
import cn.edu.pku.sei.codelinks.bean.LinkBean;
import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.preprocessdata.MyRange;
import com.github.gumtreediff.actions.model.Move;
import com.github.gumtreediff.tree.Tree;

public class ChangeEntity {

    public int changeEntityId;

    public ClusteredActionBean clusteredActionBean;

    public StageIIBean stageIIBean;
    public StageIIIBean stageIIIBean;
    public LinkBean linkBean;
    public MyRange lineRange;



    private  void init(){
        changeEntityId = Global.changeEntityId;
        Global.changeEntityId++;
        stageIIIBean = new StageIIIBean();
        stageIIBean = new StageIIBean();
        stageIIIBean.setChangeEntityId(changeEntityId);

    }

    /**
     * 预处理
     * @param location
     * @param changeType
     * @param myRange
     */
    public ChangeEntity(String location,String changeType,MyRange myRange){
        init();
        this.lineRange = myRange;
        this.stageIIBean.setLineRange("("+this.lineRange.startLineNo +","+ this.lineRange.endLineNo+")");
        this.stageIIBean.setEntityCreationStage(ChangeEntityDesc.StageIIGenStage.ENTITY_GENERATION_STAGE_PRE_DIFF);
        this.stageIIBean.setGranularity(ChangeEntityDesc.StageIIGranularity.GRANULARITY_MEMBER);
        this.stageIIBean.setOpt(ChangeEntityDesc.getKeyNameByValue(changeType));
        this.stageIIBean.setLocation(location);
    }


    public ChangeEntity(ClusteredActionBean bean){
        init();
        this.clusteredActionBean = bean;
        if(bean.curAction instanceof Move){
            this.lineRange = AstRelations.getMyRange((Tree)bean.curAction.getNode(),bean.nodeType);
        }else{
            this.lineRange = AstRelations.getMyRange(bean.fafather,bean.nodeType);

        }
    }

    public ChangeEntity(){
        init();
    }

    @Override
    public String toString(){
        return changeEntityId+". "+this.stageIIBean.toString();
    }

    //    // summary
    public String toString2(){
//        return changeEntityId+". "+ this.stageIIBean.toString2();
        return null;
    }


    public int getChangeEntityId() {
        return changeEntityId;
    }

    public ClusteredActionBean getClusteredActionBean() {
        return clusteredActionBean;
    }

    public StageIIIBean getStageIIIBean() {
        return stageIIIBean;
    }

    public StageIIBean getStageIIBean() {
        return stageIIBean;
    }

    public LinkBean getLinkBean() {
        return linkBean;
    }

    public MyRange getLineRange() {
        return lineRange;
    }
}
