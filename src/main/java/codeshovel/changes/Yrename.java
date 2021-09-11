package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

//wangmin: 定义了方法名发生变更的类，继承Ysignaturechange，利用Yfunction的getName方法实现了getOldValue等抽象方法
public class Yrename extends Ysignaturechange {

	public Yrename(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getName();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getName();
	}

}
