package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonObject;


//wangmin:继承Ysignaturechange 定义了异常发生的签名更改，实现了 getOldValue等抽象方法，利用Yfunction的getExceptions方法
public class Yexceptionschange extends Ysignaturechange {

	public Yexceptionschange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getExceptions();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getExceptions();
	}


}
