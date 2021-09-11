package cn.edu.pku.sei.gitparser;

import cn.edu.pku.sei.gitparser.commitutil.Meta;
import cn.edu.pku.sei.fileutil.FilePairData;

import java.util.ArrayList;
import java.util.List;

public class CommitFileExtractor {

    private List<FilePairData> filePairDatas;

    public CommitFileExtractor(Meta meta){
        filePairDatas = new ArrayList<>();

    }

   /* public void initDataFromJson(Meta meta) {
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
            String basePath = DoDiffFileCore.mFileOutputLog.metaLinkPath;
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
    }*/

}
