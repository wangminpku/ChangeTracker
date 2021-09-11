package com.github.gumtreediff;

import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class GumTreeApi {

    public static List<Action> getDiffActions(String preFile,String currFile){
        List<Action> actionList = null;

        try {
            JdtTreeGenerator p1 = new JdtTreeGenerator(preFile);
            TreeContext tc = p1.generateFromFile(new File(preFile));
            ITree t = tc.getRoot();

            JdtTreeGenerator p2 = new JdtTreeGenerator(currFile);
            TreeContext tc2 = p2.generateFromFile(new File(currFile));
            ITree t2 = tc2.getRoot();

            Matcher m = Matchers.getInstance().getMatcher(t,t2);
            m.match();
            MappingStore mappings = m.getMappings();
            ActionGenerator g = new ActionGenerator(t,t2,mappings);
            g.generate();
            actionList = g.getActions();

        }catch (IOException e) {
            e.printStackTrace();
        }
        return actionList;

    }

}
