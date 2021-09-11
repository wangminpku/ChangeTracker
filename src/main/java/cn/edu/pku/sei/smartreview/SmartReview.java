package cn.edu.pku.sei.smartreview;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.codelinks.FileInnerLinksGenerator;
import cn.edu.pku.sei.codelinks.FileOuterLinksGenerator;
import cn.edu.pku.sei.codelinks.TotalFileLinks;
import cn.edu.pku.sei.codelinks.similariity.TreeDistance;
import cn.edu.pku.sei.codelinks.util.Link;
import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.fileutil.FileOutputLog;
import cn.edu.pku.sei.fileutil.FilePairData;
import cn.edu.pku.sei.fileutil.FileUtil;
import cn.edu.pku.sei.gitparser.commitutil.CommitFile;
import cn.edu.pku.sei.gitparser.commitutil.Meta;
import com.github.gumtreediff.tree.Tree;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartReview {

    private Map<String, ChangeEntityData> fileChangeEntityData = new HashMap<>();
    public DoDiffFileCore doDiffFileCore;
    private List<FilePairData> filePairDatas;

    /**
     * output path +"proj_name" + "commit_id"
     *
     * @param outputDir
     */
    public SmartReview(String outputDir, Meta meta) {
//        Global.outputFilePathList = new ArrayList<>();
        filePairDatas = new ArrayList<>();
        doDiffFileCore = new DoDiffFileCore();
        doDiffFileCore.mFileOutputLog = new FileOutputLog(outputDir, meta.getProject_name());
        doDiffFileCore.mFileOutputLog.setCommitId(meta.getCommit_hash(), meta.getParents());
        initDataFromJson(meta);
        FileUtil.createFile("meta.json", new GsonBuilder().setPrettyPrinting().create().toJson(meta), new File(doDiffFileCore.mFileOutputLog.metaLinkPath));
    }


    public void initDataFromJson(Meta meta) {
        List<CommitFile> commitFiles = meta.getFiles();
        List<String> actions = meta.getActions();
        for (int i = 0; i < commitFiles.size(); i++) {
            CommitFile file = commitFiles.get(i);
            if (file.getDiffPath() == null) {
                continue;
            }
            String action = actions.get(i);
            String fileFullName = file.getFile_name();
            int index = fileFullName.lastIndexOf("/");
            String fileName = fileFullName.substring(index + 1, fileFullName.length());
            String prevFilePath = file.getPrev_file_path();
            String currFilePath = file.getCurr_file_path();
            String parentCommit = file.getParent_commit();
            String basePath = doDiffFileCore.mFileOutputLog.metaLinkPath;
            byte[] prevBytes = null;
            byte[] currBytes = null;
            try {
                if (prevFilePath != null) {
                    prevBytes = Files.readAllBytes(Paths.get(basePath + "/" + prevFilePath));

                }
                if (currFilePath != null) {
                    currBytes = Files.readAllBytes(Paths.get(basePath + "/" + currFilePath));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            FilePairData fp = new FilePairData(prevBytes, currBytes, basePath + "/" + prevFilePath, basePath + "/" + currFilePath, fileName);
            fp.setParentCommit(parentCommit);
            filePairDatas.add(fp);
        }
    }


    //public void generateDiffMinerOutput(String graphDirPath) {
    public void generateDiffMinerOutput(){

        String absolutePath = this.doDiffFileCore.mFileOutputLog.metaLinkPath;
        List<String> currFilePathList = new ArrayList<>();
        List<String> prevFilePathList = new ArrayList<>();
        Global.changeEntityFileNameMap = new HashMap<>();
        for (FilePairData fp : filePairDatas) {
            Global.parentCommit = fp.getParentCommit();
            Global.fileName = fp.getFileName();
            if (fp.getPrev() == null && fp.getCurr() == null) {
                continue;
            }
            if (fp.getPrev() == null) {
                Global.currFilePath = fp.getCurrPath().substring(fp.getCurrPath().indexOf("curr"),fp.getCurrPath().length());
                Global.getCurrFile = fp.getCurrPath();
                currFilePathList.add(Global.getCurrFile);
                //this.doDiffFileCore.dooAddFile(fp.getFileName(), fp.getCurr(), absolutePath,graphDirPath);
                this.doDiffFileCore.dooAddFile(fp.getFileName(), fp.getCurr(), absolutePath);

                //System.out.println(Global.currFilePath);
            } else if (fp.getCurr() == null) {
                Global.prevFilePath = fp.getPrevPath().substring(fp.getPrevPath().indexOf("prev"),fp.getPrevPath().length());
                Global.getPrevFile = fp.getPrevPath();
                prevFilePathList.add(Global.getPrevFile);
                //this.doDiffFileCore.dooRemoveFile(fp.getFileName(), fp.getPrev(), absolutePath,graphDirPath);
                this.doDiffFileCore.dooRemoveFile(fp.getFileName(), fp.getPrev(), absolutePath);

                //System.out.println(Global.prevFilePath);
            } else {
                Global.prevFilePath = fp.getPrevPath().substring(fp.getPrevPath().indexOf("prev"),fp.getPrevPath().length());
                Global.currFilePath = fp.getCurrPath().substring(fp.getCurrPath().indexOf("curr"),fp.getCurrPath().length());
                Global.getPrevFile = fp.getPrevPath();
                Global.getCurrFile = fp.getCurrPath();
                prevFilePathList.add(Global.getPrevFile);
                currFilePathList.add(Global.getCurrFile);
                //this.doDiffFileCore.dooDiffFile(fp.getFileName(), fp.getPrev(), fp.getCurr(), absolutePath,graphDirPath);
                this.doDiffFileCore.dooDiffFile(fp.getFileName(), fp.getPrev(), fp.getCurr(), absolutePath);
            }
            this.fileChangeEntityData.put(fp.getParentCommit() + "@@@" + this.doDiffFileCore.changeEntityData.fileName, this.doDiffFileCore.changeEntityData);
        }


        List<String> fileNames = new ArrayList<>(this.fileChangeEntityData.keySet());
        TotalFileLinks totalFileLinks = new TotalFileLinks();
        List<Link> links = new ArrayList<>();

        for (int i = 0; i < fileNames.size(); i++) {
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            //System.out.println(cedA.entityContainer);
            //System.out.println(fileNameA);
            Global.ced = cedA;
            //System.out.println(cedA.fileName);
            for(String filePath1 : prevFilePathList){
                String fileNamePath=filePath1.substring(filePath1.lastIndexOf("/")+1,filePath1.length());
                if(fileNamePath.equals(cedA.fileName)){
                    Global.getPrevFile = filePath1;
                    break;
                }
            }
            //System.out.println(Global.prevFilePath);
            for(String filePath2 : currFilePathList ){
                String fileNamePath=filePath2.substring(filePath2.lastIndexOf("/")+1,filePath2.length());
                if(fileNamePath.equals(cedA.fileName)){
                    Global.getCurrFile = filePath2;
                    break;
                }
            }
            //System.out.println(Global.currFilePath);
            FileInnerLinksGenerator associationGenerator = new FileInnerLinksGenerator(cedA);
            associationGenerator.generateFile();
            links.addAll(cedA.mLinks);
            totalFileLinks.addEntry(fileNameA, cedA.mLinks);
        }
        for (int i = 0; i < fileNames.size(); i++) {
            String fileNameA = fileNames.get(i);
            ChangeEntityData cedA = this.fileChangeEntityData.get(fileNameA);
            //System.out.println("A文件 ： "+ cedA.fileName);

            FileOuterLinksGenerator fileOuterLinksGenerator = new FileOuterLinksGenerator();
            for (int j = i + 1; j < fileNames.size(); j++) {
                String fileNameB = fileNames.get(j);
                ChangeEntityData cedB = this.fileChangeEntityData.get(fileNameB);
                //System.out.println("B文件 ： "+cedB.fileName);
                fileOuterLinksGenerator.generateOutsideAssociation(cedA, cedB, prevFilePathList, currFilePathList);
                links.addAll(fileOuterLinksGenerator.mAssos);
                totalFileLinks.addFile2FileAssos(fileNameA, fileNameB, fileOuterLinksGenerator.mAssos);
            }
        }
        links.addAll(new FileOuterLinksGenerator().checkSimilarity(this.fileChangeEntityData,totalFileLinks));
        //DiffGraphLinker.process(links,graphDirPath);
        //SummarizationToGraph.ProcessSummarizationToGraph(graphDirPath);
        doDiffFileCore.mFileOutputLog.writeLinkJson(totalFileLinks.toAssoJSonString());
        System.out.println(totalFileLinks.toConsoleString());
        fileChangeEntityData.clear();
    }


    public float distance(Tree tree1, Tree tree2) {
        TreeDistance treeDistance = new TreeDistance(tree1, tree2);
        float distance = treeDistance.calculateTreeDistance();
        return distance;
    }
}
