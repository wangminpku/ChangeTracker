package cn.edu.pku.sei.actionsparser;

import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.actionsparser.member.MatchNonStatement;
import com.github.gumtreediff.actions.model.Action;

public class ClusterDownUp extends AbstractCluster{

    public ClusterDownUp(Class mClazz, MiningActionData mminingActionData) {
        super(mClazz, mminingActionData);
    }

    public void doClusterDownUp() {
        int actionCnt = this.actionList.size();
        for(int index =0; index!=actionCnt;index++){
            Action a = this.actionList.get(index);
            if (fp.mGeneratingActionsData.getAllActionMap().get(a) == 1) {
                continue;
            }
            MatchNonStatement.matchNonStatement(fp, a);
        }
    }
}
