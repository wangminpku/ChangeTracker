package codeshovel.entities;

import codeshovel.wrappers.Commit;
import org.eclipse.jgit.revwalk.RevCommit;

import java.util.LinkedHashMap;

//wangmin:定义了commit的演化历史的类，可以返回Commit或revCommit的Map

public class Yhistory {

	private LinkedHashMap<String, Commit> commits = new LinkedHashMap<>();
	private LinkedHashMap<String, RevCommit> revCommits = new LinkedHashMap<>();

	public Yhistory(LinkedHashMap<String, Commit> commits, LinkedHashMap<String, RevCommit> revCommits) {
		this.commits = commits;
		this.revCommits = revCommits;
	}

	public LinkedHashMap<String, Commit> getCommits() {
		return commits;
	}

	public LinkedHashMap<String, RevCommit> getRevCommits() {
		return revCommits;
	}
}
