package cn.edu.pku.sei.srmain;

import cn.edu.pku.sei.common.Global;
import cn.edu.pku.sei.gitparser.DiffExtractor;
import cn.edu.pku.sei.gitparser.GitExtractor;
import cn.edu.pku.sei.smartreview.DoDiffFileCore;
import cn.edu.pku.sei.smartreview.SmartReview;

import java.util.List;

public class OneCommitDecomposition {

    public static void main(String[] args){
        Global.runningMode = 0;

        //String repo = "C:\\Users\\dell\\IdeaProjects\\TestSR\\.git";
        //String commitId = "6f6803395f358236c98cbcf705423d5b898cbd3a";

        String repo = "/Users/wangmin/Desktop/Repos/checkstyle/.git";
       /* GitExtractor t = new GitExtractor(repo);
        List<String> l =  t.getRepoCommitId();
        int count = 0;
        for(String s : l) {
            count++;
            //System.out.println(s);
        }
        System.out.println(count);*/
        String commitId = "47dd1da57bff2bd47727cac5f12b621f8bca5e59";
        String outputDir = "/Users/wangmin/Desktop/outputs_cora";
        //String graphDirPath = "F://SR_GraphData/jruby/8f09829c50fd87cb9c8c4787e4f996ecf049056e";
        DiffExtractor diffExtractor = new DiffExtractor();
        diffExtractor.run(repo,commitId,outputDir);

    }
}
