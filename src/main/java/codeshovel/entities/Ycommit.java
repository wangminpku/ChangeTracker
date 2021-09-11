package codeshovel.entities;

import codeshovel.parser.Yfunction;
import codeshovel.parser.Yparser;
import codeshovel.wrappers.Commit;

//wangmin:定义了一个带有变更的commit的类，封装成一个实体，包含Yfunction,Yparser, Commit等

public class Ycommit {

	private String hash;

	private Commit commit;
	private Ycommit prev;

	private Yfunction matchedFunction;

	private String fileName;
	private String fileContent;
	private String filePath;

	private Yparser parser;

	// Only for serialization
	public Ycommit() {}

	public Ycommit(Commit commit) {
		this.commit = commit;
		this.hash = commit.getName();
	}

	public Commit getCommit() {
		return commit;
	}

	public Yfunction getMatchedFunction() {
		return matchedFunction;
	}

	public void setMatchedFunction(Yfunction matchedFunction) {
		this.matchedFunction = matchedFunction;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}

	public Ycommit getPrev() {
		return prev;
	}

	public void setPrev(Ycommit prev) {
		this.prev = prev;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Yparser getParser() {
		return parser;
	}

	public void setParser(Yparser parser) {
		this.parser = parser;
	}

	public String toString() {
		return this.getCommit().getCommitDate() + " " + hash;
	}

	public String getName() {
		return this.commit.getName();
	}

	public String getShortName() {
		return commit.getCommitNameShort();
	}

}