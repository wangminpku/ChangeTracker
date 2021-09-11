package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;


//wangmin:定义方法体内body发生变更的类，继承Ycomparefunctionchange
public class Ybodychange extends Ycomparefunctionchange {

	public Ybodychange(StartEnvironment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

}
