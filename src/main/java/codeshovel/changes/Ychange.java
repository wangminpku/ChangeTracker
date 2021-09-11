package codeshovel.changes;

import codeshovel.services.RepositoryService;
import codeshovel.wrappers.Commit;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;
import org.eclipse.jgit.diff.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

//wangmin: 定义了一个变更的抽象类，包含commit信息，diff信息

public abstract class Ychange {

	protected static SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat();

	protected Commit commit;
	protected RepositoryService repositoryService;

	public Ychange(StartEnvironment startEnv, Commit commit) {
		this.commit = commit;
		this.repositoryService = startEnv.getRepositoryService();
	}

	public Commit getCommit() {
		return commit;
	}

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}

	public JsonObject toJsonObject() {
		JsonObject obj = new JsonObject();
		obj.addProperty("type", getTypeAsString());
		obj.addProperty("commitMessage", commit.getCommitMessage());
		obj.addProperty("commitDate", DATE_FORMATTER.format(commit.getCommitDate()));
		obj.addProperty("commitName", commit.getName());
		obj.addProperty("commitAuthor", commit.getAuthorName());
		return obj;
	}

	public String getTypeAsString() {
		return getClass().getSimpleName();
	}

	public String getDiffAsString(String sourceOldString, String sourceNewString) throws IOException {
		RawText sourceOld = new RawText(sourceOldString.getBytes());
		RawText sourceNew = new RawText(sourceNewString.getBytes());
		DiffAlgorithm diffAlgorithm = new HistogramDiff();
		RawTextComparator textComparator = RawTextComparator.DEFAULT;
		EditList editList = diffAlgorithm.diff(textComparator, sourceOld, sourceNew);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		DiffFormatter formatter = new DiffFormatter(out);
		formatter.setContext(1000);
		formatter.format(editList, sourceOld, sourceNew);
		return out.toString(StandardCharsets.UTF_8.name());

	}

}
