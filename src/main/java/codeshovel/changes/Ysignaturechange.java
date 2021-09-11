package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

//wangmin:定义了方法签名发生改变的类，继承Ycomparefunctionchange，重写了getExtendedDetailsJsonObject方法

public abstract class Ysignaturechange extends Ycomparefunctionchange {

	public Ysignaturechange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	protected abstract Object getOldValue();
	protected abstract Object getNewValue();

	@Override
	public JsonObject getExtendedDetailsJsonObject() {
		JsonObject extendedObj = new JsonObject();
		String oldValue = "";
		String newValue = "";
		if (getOldValue() != null) {
			oldValue = getOldValue().toString();
		}
		if (getNewValue() != null) {
			newValue = getNewValue().toString();
		}
		extendedObj.addProperty("oldValue", oldValue);
		extendedObj.addProperty("newValue", newValue);
		return extendedObj;
	}
}
