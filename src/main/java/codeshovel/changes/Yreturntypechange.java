package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

//wangmin: 定义了方法返回值发生改变的类，继承Ysignaturechange，利用Yfunction中getReturnStmt()方法实现了getOldValue等抽象方法

public class Yreturntypechange extends Ysignaturechange {

	public Yreturntypechange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getReturnStmt();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getReturnStmt();
	}

}
