package cn.edu.pku.sei.actionsparser.bean;

import cn.edu.pku.sei.actionsparser.util.MyList;

public class ChangePacket {

    public ChangePacket(){

    }
    private MyList<String> changeSet1;
    private MyList<String> changeSet2;

    public MyList<String> getChangeSet1() {
        return changeSet1;
    }

    public MyList<String> getChangeSet2() {
        return changeSet2;
    }



    public void initChangeSet1(){
        this.changeSet1 = new MyList<>();
    }

    public void initChangeSet2(){
        this.changeSet2 = new MyList<>();
    }
}
