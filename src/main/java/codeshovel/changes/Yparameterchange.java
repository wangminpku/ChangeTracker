package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;


//wangmin:定义了方法签名中参数发生变更的类，继承了Ysignaturechange，利用Yfunction的getParameters方法实现了getOldValue等抽象方法
public class Yparameterchange extends Ysignaturechange {

	public Yparameterchange(StartEnvironment startEnv, Yfunction newFunction, Yfunction oldFunction) {
		super(startEnv, newFunction, oldFunction);
	}

	@Override
	protected Object getOldValue() {
		return oldFunction.getParameters();
	}

	@Override
	protected Object getNewValue() {
		return newFunction.getParameters();
	}


}
