package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.parser.impl.JavaFunction;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

//wangmin:定义了新引入的方法的类，重写了toString()方法 toJsonObject()方法，由于是新引入，不需要oldFunction，直接继承Ychange，不需要比较
public class Yintroduced extends Ychange {

	private static final Logger log = LoggerFactory.getLogger(Yintroduced.class);

	private Yfunction newFunction;

	protected String diffString;

	public Yintroduced(StartEnvironment startEnv, Yfunction newFunction) {
		super(startEnv, newFunction.getCommit());
		this.newFunction = newFunction;
	}

	@Override
	public String toString() {
		String template = "%s(%s:%s:%s)";
		return String.format(template,
				getClass().getSimpleName(),
				newFunction.getCommitNameShort(),
				newFunction.getName(),
				newFunction.getNameLineNumber()
		);
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject obj = super.toJsonObject();
		obj.addProperty("diff", getDiffAsString());
		obj.addProperty("actualSource", newFunction.getSourceFragment());
		obj.addProperty("path", newFunction.getSourceFilePath());
		obj.addProperty("functionStartLine", newFunction.getNameLineNumber());
		obj.addProperty("functionName", newFunction.getName());
		return obj;
	}

	private String getDiffAsString() {
		if (this.diffString == null) {
			try {
				this.diffString = this.getDiffAsString("", newFunction.getSourceFragment());
			} catch (IOException e) {
				log.warn("Failed to generate diff string: " + e.getMessage());
			}

		}
		return diffString;
	}
}
