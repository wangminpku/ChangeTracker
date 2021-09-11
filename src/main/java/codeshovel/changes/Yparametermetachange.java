package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;

//wangmin:定义了参数发生变更的类，继承Yparameterchange类
public class Yparametermetachange extends Yparameterchange {

	public Yparametermetachange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

}