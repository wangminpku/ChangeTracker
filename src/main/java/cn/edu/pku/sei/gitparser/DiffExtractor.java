package cn.edu.pku.sei.gitparser;

import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.gitparser.commitutil.CommitFile;
import cn.edu.pku.sei.gitparser.commitutil.IHandleCommit;
import cn.edu.pku.sei.gitparser.commitutil.Meta;
import cn.edu.pku.sei.fileutil.PathUtil;
import cn.edu.pku.sei.smartreview.SmartReview;
import org.eclipse.jgit.revwalk.RevCommit;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class DiffExtractor implements IHandleCommit {

    public GitExtractor gitExtractor;
    public Meta meta;
    public List<String> prevDir = new ArrayList<>();
    public List<String> currDir = new ArrayList<>();

    public List<String> getPrevDir() {
        return prevDir;
    }

    public List<String> getCurrDir() {
        return currDir;
    }

    //public void run(String repo, String commitId, String outputDir,String graphDirPath){
    public void run(String repo, String commitId, String outputDir){
        gitExtractor = new GitExtractor(repo);
        initMeta(repo,commitId,outputDir);
        gitExtractor.analyzeOneCommit(this,commitId);
        //SmartReview smartReview = new SmartReview(outputDir,meta);
        //smartReview.generateDiffMinerOutput();
    }

//    public void runCC(String repo, String commitId, String outputDir, String graphDirPath){
//
//        gitExtractor = new GitExtractor(repo);
//        initMeta(repo, commitId, outputDir);
//        gitExtractor.analyzeOneCommit(this,commitId);
//        ClusterChanges clusterChanges = new ClusterChanges(outputDir,meta);
//        clusterChanges.generateDiffMinerOutput(graphDirPath);
//    }


    public void initMeta(String repo,String commitId,String outputDir){
        meta = new Meta();
        meta.setCommit_hash(commitId);
        meta.setProject_name(PathUtil.getGitProjectNameFromGitFullPath(repo));
        meta.setActions(null);
        meta.setAuthor(null);
        meta.setCommit_log(null);
        meta.setCommitter(null);
        meta.setDate_time(null);
        meta.setOutputDir(outputDir+'/'+PathUtil.getGitProjectNameFromGitFullPath(repo)+'/'+commitId);
        meta.setLinkPath(meta.getOutputDir()+"/link.json");
        Global.mmeta = meta;

    }

    public void loadCommitMeta(String author,int timeSeconds,String committer,String commitLog){
        meta.setAuthor(author);
        meta.setCommitter(committer);
        meta.setCommit_log(commitLog);
        Calendar c=Calendar.getInstance();
        long millions=new Long(timeSeconds).longValue()*1000;
        c.setTimeInMillis(millions);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = sdf.format(c.getTime());
        meta.setDate_time(dateString);
    }

    public void handleCommit(Map<String, Map<String, List<String>>> changedFiles, String commitId, RevCommit commit){
        loadCommitMeta(commit.getAuthorIdent().getName(),commit.getCommitTime(),commit.getCommitterIdent().getName(),commit.getShortMessage()+"\n\n\n"+commit.getFullMessage());
        int cnt = 0;
        for (Map.Entry<String, Map<String, List<String>>> entry : changedFiles.entrySet()) {
            String parentCommitId = entry.getKey();
            meta.addParentCommit(parentCommitId);
            Map<String, List<String>> changedFileEntry = entry.getValue();
            if (changedFileEntry.containsKey("modifiedFiles")) {
                List<String> modifiedFile = changedFileEntry.get("modifiedFiles");

                if(modifiedFile !=null && modifiedFile.size()!=0) {
                    for (String file : modifiedFile) {
                        boolean isFiltered = GitExtractor.isFilter(file);
                        //System.out.println(file);
                        setCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        meta.addAction("modified");
                        cnt += 1;
                    }
                }
            }
           if(changedFileEntry.containsKey("addedFiles")){
                List<String> addedFile = changedFileEntry.get("addedFiles");
                if(addedFile!=null && addedFile.size()!=0) {
                    for (String file : addedFile) {
                        boolean isFiltered = GitExtractor.isFilter(file);
                        setAddedCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        meta.addAction("added");
                        cnt += 1;
                    }
                }
            }
            if(changedFileEntry.containsKey("deletedFiles")){
                List<String> deleted = changedFileEntry.get("deletedFiles");
                if(deleted!=null && deleted.size()!=0) {
                    for (String file : deleted) {
                        boolean isFiltered = GitExtractor.isFilter(file);
                        setDeletedCommitFile(cnt, parentCommitId, commitId, file,isFiltered);
                        cnt += 1;
                        meta.addAction("removed");
                    }
                }
            }

        }
    }

    public void setCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            //System.out.println(filePath);
            byte[] prevFile = gitExtractor.extract(filePath, parentCommitId);
            byte[] currFile = gitExtractor.extract(filePath, commitId);
            int index = filePath.lastIndexOf("/");
            Path prevFilePath = Paths.get(meta.getOutputDir() + "/prev/" + parentCommitId + "/" + filePath);
            Path currFilePath = Paths.get(meta.getOutputDir() + "/curr/" + parentCommitId + "/" + filePath);

            if(!prevFilePath.toFile().exists() || !currFilePath.toFile().exists()) {
                int index2 = prevFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String prevDir = prevFilePath.toFile().getAbsolutePath().substring(0,index2);
                index2 = currFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String currDir = currFilePath.toFile().getAbsolutePath().substring(0,index2);
                File prev = new File(prevDir);
                if(!prev.exists()){
                    prev.mkdirs();
                }
                File curr = new File(currDir);
                if(!curr.exists()){
                    curr.mkdirs();
                }
            }
            Files.write(prevFilePath, prevFile);
            Files.write(currFilePath, currFile);

            prevDir.add(prevFilePath.toString());
            currDir.add(currFilePath.toString());


            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setPrev_file_path("prev/" + parentCommitId + "/" + filePath);
            commitFile.setCurr_file_path("curr/" + parentCommitId + "/" + filePath);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/" + parentCommitId + "/Diff" + fileName + ".json");
            }
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setAddedCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            //System.out.println(filePath);
            byte[] currFile = gitExtractor.extract(filePath, commitId);
            int index = filePath.lastIndexOf("/");
            Path currFilePath = Paths.get(meta.getOutputDir() + "/curr/" + parentCommitId + "/" + filePath);
            if(!currFilePath.toFile().exists()) {
                int index2 = currFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String currDir = currFilePath.toFile().getAbsolutePath().substring(0,index2);
                File curr = new File(currDir);
                if(!curr.exists()){
                    curr.mkdirs();
                }
            }
            Files.write(currFilePath, currFile);


            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setCurr_file_path("curr/" + parentCommitId + "/" + filePath);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/"+parentCommitId+"/Diff"+fileName+".json");
            }
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setDeletedCommitFile(int cnt,String parentCommitId,String commitId,String filePath,boolean isFiltered){
        try {
            CommitFile commitFile = new CommitFile();
            //System.out.println(parentCommitId);
            //System.out.println(filePath);
            byte[] prevFile = gitExtractor.extract(filePath, parentCommitId);
            int index = filePath.lastIndexOf("/");
            Path prevFilePath = Paths.get(meta.getOutputDir() + "/prev/" + parentCommitId + "/" + filePath);
            if(!prevFilePath.toFile().exists()) {
                int index2 = prevFilePath.toFile().getAbsolutePath().lastIndexOf(File.separator);
                String prevDir = prevFilePath.toFile().getAbsolutePath().substring(0,index2);
                File prev = new File(prevDir);
                if(!prev.exists()){
                    prev.mkdirs();
                }
            }
            Files.write(prevFilePath, prevFile);


            String fileName = filePath.substring(index + 1, filePath.length());
            Global.fileName = fileName;
            commitFile.setId(cnt);
            commitFile.setPrev_file_path("prev/" + parentCommitId + "/" + filePath);
            commitFile.setFile_name(fileName);
            commitFile.setParent_commit(parentCommitId);
            if(!isFiltered) {
                commitFile.setDiffPath(meta.getOutputDir() + "/gen/"+parentCommitId+"/Diff"+fileName+".json");
            }
            meta.addFile(commitFile);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


}

