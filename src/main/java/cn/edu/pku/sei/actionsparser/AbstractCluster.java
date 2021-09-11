package cn.edu.pku.sei.actionsparser;

import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.actions.model.Delete;
import com.github.gumtreediff.actions.model.Insert;
import com.github.gumtreediff.actions.model.Move;

import java.util.List;

public class AbstractCluster {

    public List<Action> actionList;
    public MiningActionData fp;

    public AbstractCluster(Class mClazz, MiningActionData mminingActionData) {
        this.fp = mminingActionData;
        Class clazz = mClazz;
        if (Insert.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getInsertActions();
        } else if (Delete.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getDeleteActions();
        } else if (Move.class.equals(clazz)) {
            this.actionList = mminingActionData.mGeneratingActionsData.getMoveActions();
        } else {
            this.actionList = mminingActionData.mGeneratingActionsData.getUpdateActions();
        }
    }
}
