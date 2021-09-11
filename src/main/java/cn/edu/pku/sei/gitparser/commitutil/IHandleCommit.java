package cn.edu.pku.sei.gitparser.commitutil;

import org.eclipse.jgit.revwalk.RevCommit;

import java.util.List;
import java.util.Map;


public interface IHandleCommit {

    void handleCommit(Map<String, Map<String, List<String>>> changedFiles,String commitId,RevCommit commit);
}

