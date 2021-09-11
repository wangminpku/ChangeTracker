package evaluation;

import codeshovel.changes.Ychange;
import codeshovel.entities.Yresult;
import codeshovel.execution.ShovelExecution;
import codeshovel.services.RepositoryService;
import codeshovel.services.impl.CachingRepositoryService;
import codeshovel.util.Utl;
import codeshovel.wrappers.Commit;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class EvaTracking {

    public static final String DATA_DIR = "/Users/wangmin/IdeaProjects/ChangeTracker/src/main/resources/test/";
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


    public static void execute(List<String> repoList) throws Exception{


        int true_count = 0;
        int all_detect = 0;
        int exp_count = 0;

        for(String configName : repoList){
            LinkedHashMap<String,String> results = new LinkedHashMap<>();

            //ClassLoader classLoader = EvaTracking.class.getClassLoader();
            File file = new File(DATA_DIR + configName + ".json");
            String json = FileUtils.readFileToString(file, "utf-8");
            Gson gson = new Gson();

            StartEnvironment startEnv = gson.fromJson(json, StartEnvironment.class);
            String repositoryName = startEnv.getRepositoryName();
            String repositoryPath = REPO_DIR + repositoryName + "/.git";
            startEnv.setRepositoryPath(repositoryPath);

            Repository repository = Utl.createRepository(repositoryPath);
            Git git = new Git(repository);
            RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
            Commit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

            startEnv.setRepositoryService(repositoryService);
            startEnv.setStartCommit(startCommit);
            startEnv.setEnvName(configName);
            startEnv.setFileName(Utl.getFileName(startEnv.getFilePath()));

            Yresult yresult = ShovelExecution.runSingle(startEnv, startEnv.getFilePath(),true);

            LinkedHashMap expectedResults = startEnv.getExpectedResult();

            for(String commit: yresult.keySet()){
                Ychange ychange = yresult.get(commit);
                results.put(commit, ychange.getTypeAsString());
                System.out.println(commit + "\t"+ ychange.getTypeAsString());
                if(expectedResults.containsKey(commit)){
                    if(expectedResults.get(commit).equals(ychange.getTypeAsString())){
                        true_count += 1;
                    }

                }
            }

            all_detect += results.size();
            exp_count += expectedResults.size();

        }

        float precision = 0;
        float recall = 0;

        precision = (float)true_count/all_detect;
        recall = (float)true_count/exp_count;

        System.out.println("total detect: " + all_detect + "\t" + "expected: " + exp_count + "\t" + "true: " + true_count);

        System.out.println("precision: " + precision+ "\t" + "recall: " + recall);

    }

    public static void main(String[] args) throws Exception{
        List<String> firstRepo = getConfigName(DATA_DIR);
        //System.out.println(firstRepo.subList(0,10));

        //execute(firstRepo.subList(0,10));

        //int i=50,j=60;

        //for(; i < 90;){
            //System.out.println("----------------------------------------------------------------");
            execute(firstRepo.subList(45, 50));
            //i += 10;
            //j += 10;
            //System.out.println("-----------------------------------------------------------------");
       // }

    }


}
