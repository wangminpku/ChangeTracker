package diffparser.gitparser;

import com.github.gumtreediff.GumTreeApi;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;
import org.eclipse.jgit.treewalk.TreeWalk;
import com.github.gumtreediff.tree.TreeUtils;
import com.github.gumtreediff.tree.ITree;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class testgit {


    public static void main(String[] args) throws GitAPIException, IOException {


        DiffExtractor diffExtractor = new DiffExtractor();
        diffExtractor.run("/Users/wangmin/Desktop/Repos/checkstyle/.git", "5fc3ff2bfbed94f88e1af95f38e970a832e4dbe6", "/Users/wangmin/Desktop/outputs_cora");

        List<String> prev = diffExtractor.getPrevDir();
        List<String> curr = diffExtractor.getCurrDir();

        System.out.println(prev.size() + " : " + curr.size());

        List<Action> actionList = new ArrayList<>();

        for(String f : prev){
            int index = f.lastIndexOf("/");
            String filename1 = f.substring(index+1,f.length());
            //System.out.println(filename1);
            for(String f1 : curr){
                int index1 = f1.lastIndexOf("/");
                String filename2 = f1.substring(index1+1,f1.length());
                //System.out.println(filename2);
                if(filename1.equals(filename2)){
                    //System.out.println(filename1);
                    List<Action> ac1;
                    ac1 = GumTreeApi.getDiffActions(f,f1);
                    //System.out.println(ac1.toString());
                    actionList.addAll(ac1);
                }
            }

        }

        for(Action a : actionList){

            Tree treeNode = (Tree) a.getNode();

            System.out.println(a.getNode().getLabel());
            System.out.println(a.getNode().getType());
            //System.out.println(treeNode.getRangeString());
            //System.out.println(a.getNode().);

            System.out.println("-----");
        }

    }


}
