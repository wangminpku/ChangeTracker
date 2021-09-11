package cn.edu.pku.sei.codelinks.member;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.base.ChangeEntity;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;

public class LinkMember2Member {

    public static void checkMethodAssociation(ChangeEntityData changeEntityData, ChangeEntity ce1, ChangeEntity ce2){
        if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_INSERT)){
            // 相似的signature
        }else if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_DELETE)){

        }else if(ce1.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE) &&
                ce2.stageIIBean.getOpt().equals(ChangeEntityDesc.StageIIOpt.OPT_CHANGE)){

        }
    }
}
