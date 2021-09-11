package cn.edu.pku.sei.actionsparser.util;

import java.util.ArrayList;

public class MyList<E> extends ArrayList<E> {

    @Override
    public boolean add(E e) {
        if(this.contains(e)){
            return true;
        }
        super.add(e);
        return true;
    }
}
