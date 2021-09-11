package cn.edu.pku.sei.common;

import cn.edu.pku.sei.changeentity.ChangeEntityData;
import cn.edu.pku.sei.gitparser.commitutil.Meta;

import java.util.Map;

public class Global {

    public static Meta mmeta;

    public static int changeEntityId = 0;

    public static int RQ2 = 0;

    public static String fileName;


    public static String parentCommit;

//    public static List<String> outputFilePathList;


    public static Map<Integer,String> changeEntityFileNameMap;
    /**
     * running mode
     * 0 command mode
     * 1 offline mode
     * 2 online mode
     */
    public static int runningMode;
    /**
     * input configs
     */
    public static String outputDir;
    public static String repoPath; // null in online mode
    public static String projectName;

    public static String prevFilePath;
    public static String currFilePath;

    public static String getPrevFile;
    public static String getCurrFile;

    /**
     * running vars
     */
    public static ChangeEntityData ced;
}
