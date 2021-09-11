package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

//wangmin:定义了文件重命名这种变更的类，继承Ycrossfilechange， 重写了getExtendedDetailsJsonObject方法

public class Yfilerename extends Ycrossfilechange {

	public Yfilerename(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = new JsonObject();
		String oldPath = oldFunction.getSourceFilePath();
		String newPath = newFunction.getSourceFilePath();
		extendedObj.addProperty("oldPath", oldPath);
		extendedObj.addProperty("newPath", newPath);
		return extendedObj;
	}
}
