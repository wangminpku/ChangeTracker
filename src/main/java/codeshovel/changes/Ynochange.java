package codeshovel.changes;

import codeshovel.wrappers.StartEnvironment;
import codeshovel.wrappers.Commit;

//wangmin:定义了没有具体类型变更的类，继承Ychange

public class Ynochange extends Ychange {

	public Ynochange(StartEnvironment startEnv, Commit commit) {
		super(startEnv, commit);
	}

}
