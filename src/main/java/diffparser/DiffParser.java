package diffparser;

import com.github.gumtreediff.GumTreeApi;
import com.github.gumtreediff.actions.model.Action;
import com.github.gumtreediff.tree.Tree;
import diffparser.gitparser.DiffExtractor;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DiffParser {

    public static List<Action> diffExtracter(String repoPath, String commitId, String outputPath) throws GitAPIException, IOException {

        DiffExtractor diffExtractor = new DiffExtractor();
        diffExtractor.run(repoPath, commitId, outputPath);

        List<String> prev = diffExtractor.getPrevDir();
        List<String> curr = diffExtractor.getCurrDir();

        //System.out.println(prev.size() + " : " + curr.size());
        if(prev.size() > 10){
            return null;
        }

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

        return actionList;


    }

    public static void main(String[] args) throws GitAPIException, IOException{
        String repoPath = "/Users/wangmin/Desktop/Repos/checkstyle/.git";
        String commitId = "5fc3ff2bfbed94f88e1af95f38e970a832e4dbe6";
        String outputPath = "/Users/wangmin/Desktop/outputs_cora";

        DiffParser.diffExtracter(repoPath, commitId, outputPath);
    }
}
