package codeshovel.changes;

import codeshovel.parser.Yfunction;
import codeshovel.wrappers.StartEnvironment;

//wangmin: 交叉文件的变更，重写了toString()方法,跟文件变更有关的change都和此类有关
public abstract class Ycrossfilechange extends Ycomparefunctionchange {

	public Ycrossfilechange(StartEnvironment startEnv, Yfunction matchedFunction, Yfunction compareFunction) {
		super(startEnv, matchedFunction, compareFunction);
	}

	@Override
	public String toString() {
		return super.toString() + "[" + oldFunction.getSourceFilePath() + "]";
	}

}
