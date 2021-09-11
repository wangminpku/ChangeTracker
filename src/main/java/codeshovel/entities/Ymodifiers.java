package codeshovel.entities;

import java.util.ArrayList;
import java.util.List;

//wangmin:定义了修饰符的类，返回一个修饰符的字符串列表

public class Ymodifiers {

	public static final Ymodifiers NONE = new Ymodifiers(new ArrayList<>());

	private List<String> modifiers;

	public Ymodifiers(List<String> modifiers) {
		this.modifiers = modifiers;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Ymodifiers && this.modifiers.equals(((Ymodifiers) obj).getModifiers());
	}

	public List<String> getModifiers() {
		return modifiers;
	}

	@Override
	public String toString() {
		return this.modifiers.toString();
	}
}
