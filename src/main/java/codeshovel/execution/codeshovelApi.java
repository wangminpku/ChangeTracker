package codeshovel.execution;

import codeshovel.changes.Ychange;
import codeshovel.entities.Yresult;
import codeshovel.services.RepositoryService;
import codeshovel.services.impl.CachingRepositoryService;
import codeshovel.util.Utl;
import codeshovel.wrappers.Commit;
import codeshovel.wrappers.GlobalEnv;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.Gson;
import org.apache.commons.io.FileUtils;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.lib.Repository;

import java.io.File;

public class codeshovelApi {

    private static final String ORACLE_DIR = "oracles/" + GlobalEnv.LANG;

    public static void execute() throws Exception{

        String configName = GlobalEnv.ENV_NAMES.get(0);
        ClassLoader classLoader = codeshovelApi.class.getClassLoader();
        File file = new File(classLoader.getResource(ORACLE_DIR + "/" + configName + ".json").getFile());
        String json = FileUtils.readFileToString(file, "utf-8");
        Gson gson = new Gson();

        StartEnvironment startEnv = gson.fromJson(json, StartEnvironment.class);
        String repositoryName = startEnv.getRepositoryName();
        String repositoryPath = GlobalEnv.REPO_DIR + "/" + repositoryName + "/.git";
        startEnv.setRepositoryPath(repositoryPath);

        Repository repository = Utl.createRepository(repositoryPath);
        Git git = new Git(repository);
        RepositoryService repositoryService = new CachingRepositoryService(git, repository, repositoryName, repositoryPath);
        Commit startCommit = repositoryService.findCommitByName(startEnv.getStartCommitName());

        startEnv.setRepositoryService(repositoryService);
        startEnv.setStartCommit(startCommit);
        startEnv.setEnvName(configName);
        startEnv.setStartCommit(startCommit);
        startEnv.setFileName(Utl.getFileName(startEnv.getFilePath()));

        ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);

    }

    public static Yresult executeSingle(String methodName, String filePath, int startLine, String repoPath, String repoName, String startCommitName) throws Exception{

        Repository repository = Utl.createRepository(repoPath);
        Git git = new Git(repository);
        RepositoryService repositoryService = new CachingRepositoryService(git, repository, repoName,repoPath);
        Commit startCommit = repositoryService.findCommitByName(startCommitName);


        StartEnvironment startEnv = new StartEnvironment(repositoryService);

        startEnv.setRepositoryName(repoName);
        startEnv.setRepositoryPath(repoPath);
        startEnv.setRepositoryService(repositoryService);
        startEnv.setStartCommit(startCommit);
        //startEnv.setEnvName(methodName);
        startEnv.setFilePath(filePath);
        startEnv.setFileName(Utl.getFileName(filePath));
        startEnv.setFunctionName(methodName);
        startEnv.setFunctionStartLine(startLine);
        startEnv.setStartCommitName(startCommitName);



        return ShovelExecution.runSingle(startEnv, startEnv.getFilePath(), true);


    }

    public static void main(String[] args) throws Exception{
        String methodName = "fireErrors";
        String filePath = "src/main/java/com/puppycrawl/tools/checkstyle/Checker.java";
        int startLine = 384;
        String repoPath = "/Users/wangmin/Desktop/Repos/checkstyle/.git";
        String repoName = "checkstyle";
        String startCommit = "119fd4fb33bef9f5c66fc950396669af842c21a3";

//        Yresult y  = executeSingle(methodName, filePath, startLine, repoPath, repoName, startCommit);
//
//        for(String commit: y.keySet()){
//            Ychange ychange = y.get(commit);
//            //System.out.println("this is related commit: " + commit);
//        }


    }

    
}
