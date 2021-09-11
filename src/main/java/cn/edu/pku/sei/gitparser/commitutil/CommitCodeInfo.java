package cn.edu.pku.sei.gitparser.commitutil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jgit.diff.EditList;
import org.eclipse.jgit.revwalk.RevCommit;

public class CommitCodeInfo {

    public CommitCodeInfo(RevCommit commit){
        this.mCommit = commit;
        this.fileDiffEntryMap = new HashMap<RevCommit,List<FileChangeEditList>>();

    }
    private RevCommit mCommit;
    /**
     * 一个commit对应多个parent commit，一对commit对应多个文件的变动
     */
    private Map<RevCommit,List<FileChangeEditList>> fileDiffEntryMap;

    public void addFileChangeEntry(RevCommit parentCommit,String oldPath,String newPath,EditList mEditList,String patch){
        List<FileChangeEditList> tmpList;
        FileChangeEditList tmpItem = new FileChangeEditList(oldPath,newPath,mEditList,patch);
        if(fileDiffEntryMap.containsKey(parentCommit)){
            tmpList = fileDiffEntryMap.get(parentCommit);
            tmpList.add(tmpItem);
        }else{
            tmpList = new ArrayList<FileChangeEditList>();
            tmpList.add(tmpItem);
            fileDiffEntryMap.put(parentCommit,tmpList);
        }
    }

    public Map<RevCommit, List<FileChangeEditList>> getFileDiffEntryMap() {
        return fileDiffEntryMap;
    }

    public RevCommit getmCommit() {
        return mCommit;
    }




}
