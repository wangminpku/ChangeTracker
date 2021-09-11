package com.github.gumtreediff;

import cn.edu.pku.sei.gitparser.DiffExtractor;
import cn.edu.pku.sei.gitparser.GitExtractor;
import com.github.gumtreediff.actions.ActionGenerator;
import com.github.gumtreediff.actions.model.Action;
//import com.github.gumtreediff.client.List;
import com.github.gumtreediff.client.Run;
import com.github.gumtreediff.gen.Generators;
import com.github.gumtreediff.jdt.JdtTreeGenerator;
import com.github.gumtreediff.matchers.MappingStore;
import com.github.gumtreediff.matchers.Matcher;
import com.github.gumtreediff.matchers.Matchers;
import com.github.gumtreediff.tree.ITree;
import com.github.gumtreediff.tree.TreeContext;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class GumTreeTest {




    public static void main(String[] args) throws IOException {
        Run.initGenerators();

//        DiffExtractor test = new DiffExtractor();
//
//        GitExtractor tsrGit = new GitExtractor("F:\\TSR2\\git\\TSR\\.git");
//        List<String> commitIds = tsrGit.getRepoCommitId();
//        test.initMeta("F:\\TSR2\\git\\TSR\\.git",commitIds.get(3),"F://SRtest//");
//        tsrGit.analyzeOneCommit(this,commitIds.get(3));
//
//
//        Map<String,List<String>> changedFiles = tsrGit.getCommitFileList(commitIds.get(3));
//
//        Map<String,Map<String,List<String>>> parentChangedFiles = tsrGit.getCommitParentMappedFileList(commitIds.get(3));
//       for(Map.Entry<String,List<String>> entry : changedFiles.entrySet()){
//            String key = entry.getKey();
//            //if(key.equals("modifiedFiles")){
//                for(String s : entry.getValue()){
//                    InputStream file = tsrGit.extractAndReturnInputStream(s,commitIds.get(3));
//                    break;
//                }
//           // }
//
//        }




        /*JdtTreeGenerator pref = new JdtTreeGenerator();
        TreeContext p1 = pref.generateFromStream(tsrGit.extractAndReturnInputStream())


        String file1 = "F://cldiff_test//JavaExtractor.java";
        String file2 = "F://cldiff_test//JavaExtractor-1.java";
        JdtTreeGenerator p1 = new JdtTreeGenerator(file1);

        TreeContext tc = p1.generateFromFile(new File(file1));

        ITree t = tc.getRoot();

        JdtTreeGenerator p2 = new JdtTreeGenerator(file2);
        TreeContext tc2 = p2.generateFromFile(new File(file2));
        ITree t2 = tc2.getRoot();

        Matcher m = Matchers.getInstance().getMatcher(t,t2);
        m.match();
        MappingStore mappings = m.getMappings();
        ActionGenerator g = new ActionGenerator(t,t2,mappings);
        g.generate();
        List<Action> actionList;
        actionList = g.getActions();
        for(int i=0;i<actionList.size();i++){
            Action a = actionList.get(i);
            System.out.println(a.toString());
        }*/
        //getAST();



        //System.out.println(t.toShortString());
    }
}
