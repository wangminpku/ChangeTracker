package evaluation;

import codeshovel.changes.Ychange;
import codeshovel.entities.Yresult;
import codeshovel.execution.ShovelExecution;
import codeshovel.services.RepositoryService;
import codeshovel.services.impl.CachingRepositoryService;
import codeshovel.util.Utl;
import codeshovel.wrappers.Commit;
import codeshovel.wrappers.StartEnvironment;
import com.github.gumtreediff.actions.model.Action;
import com.google.gson.Gson;
import diffparser.DiffParser;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;
import patternmining.FPItem;
import patternmining.FPTree;

import java.io.File;
import java.util.*;

public class EvaFPMining {

    public static final String DATA_DIR = "/Users/wangmin/IdeaProjects/ChangeTracker/src/main/resources/test/";
    public static final String Output_DIR = "/Users/wangmin/Desktop/outputs_cora/";
    public static final String REPO_DIR = "/Users/wangmin/Desktop/Repos/";



    public static List<String>  getConfigName(String path){

        List<String> fileList = new ArrayList<>();
        File dirFile = new File(path);

        if(dirFile.exists()){

            File[] files = dirFile.listFiles();
            for(File file : files){
                if(file.isFile()){
                    String fileName = file.getName();
                    if(fileName.endsWith(".json")) {
                        fileList.add(fileName.substring(0, fileName.indexOf(".")));

                    }
                }
            }
        }
        return fileList;
    }

    public static void getFP(List<String> repoList) throws Exception{

        int count = 0;

        for(String configName: repoList){

            File file = new File(DATA_DIR + configName + ".json");
            String json = FileUtils.readFileToString(file, "utf-8");
            Gson gson = new Gson();
            StartEnvironment startEnv = gson.fromJson(json, StartEnvironment.class);

            LinkedHashMap<String, String> expectedResults = startEnv.getExpectedResult();
            String repositoryName = startEnv.getRepositoryName();
            String repositoryPath = REPO_DIR + repositoryName + "/.git";


            List<List<String>> data = new ArrayList<>();

            for(Map.Entry<String,String> entry : expectedResults.entrySet()){

                List<String> trans = new ArrayList<>();

                List<Action> actionList = DiffParser.diffExtracter(repositoryPath, entry.getKey(),  Output_DIR);

                if(actionList != null){
                    for(Action action : actionList){
                        if(action.getNode().getType() == 42){
                            if(!trans.contains(action.getNode().getLabel())){
                                trans.add(action.getNode().getLabel());
                            }
                        }
                    }
                    trans.add(startEnv.getFunctionName());
                }
                //System.out.println(trans);

                data.add(trans);

            }

            FPTree fptree = new FPTree();
            int min_sup = expectedResults.size()/2;
            min_sup = Math.max(2, min_sup);

            fptree.setMinSuport(min_sup);

            List<FPItem> fpItemList = fptree.FPGrowth(data,null);

            //System.out.println(startEnv.getRepositoryName() + ": " + startEnv.getFunctionName() + ":\t" + fpItemList.size());

            HashSet<String> items = new HashSet<>();

            if(fpItemList != null){
                System.out.println(startEnv.getRepositoryName() + ": " + startEnv.getFunctionName() + ":\t" + fpItemList.size());
                for(FPItem fpItem: fpItemList){
                    if(fpItem.getHeader().equals(startEnv.getFunctionName())){
                        items.addAll(fpItem.getElementsList());
                        //System.out.println(count);
                        System.out.println(fpItem.getHeader() + fpItem.getElementsList());
                    }
                }
            }

            count += items.size();
            System.out.println(count);
            //count += fpItemList.size();

        }

        System.out.println("total: " + count);


    }


    public static void main(String[] args) throws Exception{
        List<String> repoPath = getConfigName(DATA_DIR);
        getFP(repoPath.subList(66,70));


    }



}
