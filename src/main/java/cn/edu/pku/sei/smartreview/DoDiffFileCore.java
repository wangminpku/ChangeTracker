package cn.edu.pku.sei.smartreview;

import cn.edu.pku.sei.actionsparser.ActionAggregationGenerator;
import cn.edu.pku.sei.actionsparser.bean.MiningActionData;
import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.changeentity.ChangeEntityPreprocess;
import cn.edu.pku.sei.changeentity.base.ChangeEntityDesc;
import cn.edu.pku.sei.changeentity.base.GenerateChangeEntityJson;
import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.fileutil.FileOutputLog;
import cn.edu.pku.sei.generateactions.GeneratingActionsData;
import cn.edu.pku.sei.generateactions.JavaParserTreeGenerator;
import cn.edu.pku.sei.generateactions.MyActionGenerator;
import cn.edu.pku.sei.generateactions.SimpleActionPrinter;
import cn.edu.pku.sei.preprocessdata.AddOrRemoveFileProcessing;
import cn.edu.pku.sei.preprocessdata.FilePairPreDiff;
import cn.edu.pku.sei.preprocessdata.PreprocessedData;
import org.json.JSONArray;

public class DoDiffFileCore {

    public ChangeEntityData changeEntityData;
    public FileOutputLog mFileOutputLog;


    //public void dooDiffFile(String filePrev, String fileCurr, String output,String graphDirPath) {
    public void dooDiffFile(String filePrev, String fileCurr, String output) {
        int index = filePrev.lastIndexOf('/');
        String fileName = filePrev.substring(index+1,filePrev.length());
        //System.out.println(filePrev);
        //System.out.println(fileName);
        Global.fileName = fileName;
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFilePath(filePrev,fileCurr);
        int result = preDiff.compareTwoFile();
        if(result ==-1){
            return;
        }
        //runDiff(preDiff,fileName,graphDirPath);
        runDiff(preDiff,fileName);
    }

    /*public static boolean isFilter(String filePathName){
        String name = filePathName.toLowerCase();
        if(!name.endsWith(".java")){
            return true;
        }
        if(name.contains("\\test\\")||name.contains("/test/")){
            return true;
        }
        String[] data = filePathName.split("/");
        String fileName = data[data.length-1];
        if(filePathName.endsWith("Test.java")||fileName.startsWith("Test")||filePathName.endsWith("Tests.java")){
            return true;
        }
        return false;
    }*/

    //public void dooDiffFile(String fileName, byte[] filePrevContent, byte[] fileCurrContent, String output, String graphDirPath) {
    public void dooDiffFile(String fileName, byte[] filePrevContent, byte[] fileCurrContent, String output) {
        long start = System.nanoTime();
        // 1.pre
        FilePairPreDiff preDiff = new FilePairPreDiff();
        preDiff.initFileContent(filePrevContent,fileCurrContent);
        int result = preDiff.compareTwoFile();
        long end = System.nanoTime();
        System.out.println("----pre-processing " +(end-start));
        if(result ==-1){
            return;
        }
        runDiff(preDiff,fileName);
        //runDiff(preDiff, fileName, graphDirPath);
    }

    //public void dooAddFile(String fileName, byte[] fileCurrContent, String output,String graphDirPath){
    public void dooAddFile(String fileName, byte[] fileCurrContent, String output){
        AddOrRemoveFileProcessing addOrRemoveFileProcessing = new AddOrRemoveFileProcessing(fileCurrContent, ChangeEntityDesc.StageIIIFile.DST);
        changeEntityData = addOrRemoveFileProcessing.ced;
        changeEntityData.fileName = fileName;
        //changeEntityData.mad.
        //FileGraphBuilder.processAddFile(graphDirPath,fileName,new String(fileCurrContent));
        //System.out.println(new String(fileCurrContent));
        System.out.println("增加了文件：" + fileName);

    }

    //public void dooRemoveFile(String fileName,byte[] fileCurrContent,String output,String graphDirPath){
    public void dooRemoveFile(String fileName,byte[] fileCurrContent,String output){
        AddOrRemoveFileProcessing addOrRemoveFileProcessing = new AddOrRemoveFileProcessing(fileCurrContent, ChangeEntityDesc.StageIIIFile.SRC);
        changeEntityData = addOrRemoveFileProcessing.ced;
        changeEntityData.fileName = fileName;
        //FileGraphBuilder.processRemoveFile(graphDirPath,fileName,new String(fileCurrContent));
        //System.out.println(new String(fileCurrContent));
        System.out.println("删除了文件："+ fileName);
    }


    //private void runDiff(FilePairPreDiff preDiff,String fileName,String graphDirPath){
    private void runDiff(FilePairPreDiff preDiff,String fileName){
        long start = System.nanoTime();
        PreprocessedData preData = preDiff.getPreprocessedData();
        JavaParserTreeGenerator treeGenerator = new JavaParserTreeGenerator(preData.getSrcCu(),preData.getDstCu());
        treeGenerator.setFileName(fileName);
        //gumtree
        MyActionGenerator actionGenerator = new MyActionGenerator(treeGenerator);
        GeneratingActionsData actionsData = actionGenerator.generate();
        //print
        long end = System.nanoTime();
        System.out.println("----mapping " +(end-start));
        //System.out.println(actionsData.getAllActions().size());
        printActions(actionsData,treeGenerator);
        long start2 = System.nanoTime();
        MiningActionData mad = new MiningActionData(preData,actionsData,treeGenerator);
        ActionAggregationGenerator aag = new ActionAggregationGenerator();
        aag.doCluster(mad);
//correcting
        ChangeEntityData ced = new ChangeEntityData(mad);
        ChangeEntityPreprocess cep = new ChangeEntityPreprocess(ced);
        cep.preprocessChangeEntity();//1.init 2.merge 3.set 4.sub
        changeEntityData = ced;
        changeEntityData.fileName = fileName;
        //System.out.println(fileName);
        long end2 = System.nanoTime();
        System.out.println("----grouping " +(end2-start2));
// json
        GenerateChangeEntityJson.setStageIIIBean(ced);
        JSONArray json = GenerateChangeEntityJson.generateEntityJson(ced.mad);
        this.mFileOutputLog.writeEntityJson(json.toString(4));
        if(Global.runningMode==0){
            //System.out.println(GenerateChangeEntityJson.toConsoleString(json));
        }else {
            //System.out.println(json.toString(4));
        }
        //DiffGraphBuilder.process(mad,graphDirPath);

    }


    private void printActions(GeneratingActionsData actionsData, JavaParserTreeGenerator treeGenerator){
        mFileOutputLog.writeTreeFile(treeGenerator.getPrettyOldTreeString(),treeGenerator.getPrettyNewTreeString());
        SimpleActionPrinter.printMyActions(actionsData.getAllActions());
    }

}
