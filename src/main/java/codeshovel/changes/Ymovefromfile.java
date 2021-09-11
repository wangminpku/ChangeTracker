package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

//wangmin:定义了方法被移除文件的类，文件路径发生改变，且方法名改变，注意与Yfilerename的区别

public class Ymovefromfile extends Ycrossfilechange {

	public Ymovefromfile(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = super.getExtendedDetailsJsonObject();
		String oldPath = oldFunction.getSourceFilePath();
		String newPath = newFunction.getSourceFilePath();
		String oldMethodName = oldFunction.getName();
		String newMethodName = newFunction.getName();
		extendedObj.addProperty("oldPath", oldPath);
		extendedObj.addProperty("newPath", newPath);
		extendedObj.addProperty("oldMethodName", oldMethodName);
		extendedObj.addProperty("newMethodName", newMethodName);
		return extendedObj;
	}

}
