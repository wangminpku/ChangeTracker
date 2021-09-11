package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.parser.Yfunction;
import com.google.gson.JsonObject;

//定义了修饰符发生变更的类，继承Ysignaturechange 父类，实现了getOldValue等抽象方法

public class Ymodifierchange extends Ysignaturechange {

	public Ymodifierchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getModifiers();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getModifiers();
	}


}
