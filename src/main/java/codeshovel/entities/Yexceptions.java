package codeshovel.entities;

import java.util.ArrayList;
import java.util.List;

//wangmin: 定义了异常的实体类，返回一个异常的字符串列表

public class Yexceptions {

	public static final Yexceptions NONE = new Yexceptions(new ArrayList<>());

	private List<String> exceptions;

	public Yexceptions(List<String> exceptions) {
		this.exceptions = exceptions;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Yexceptions && this.exceptions.equals(((Yexceptions) obj).getExceptions());
	}

	public List<String> getExceptions() {
		return exceptions;
	}

	@Override
	public String toString() {
		return this.exceptions.toString();
	}
}
