package codeshovel.entities;

import codeshovel.changes.Ychange;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

import java.util.LinkedHashMap;

//wangmin:定义了变更的结果类，继承了Map类，重写了put方法，key是commitName,value是变更

public class Yresult extends LinkedHashMap<String, Ychange> {

	private StringBuilder builder = new StringBuilder();

	@Override
	public Ychange put(String commitName, Ychange change) {
		builder.append("\n").append(commitName).append(":").append(change.toString());
		return super.put(commitName, change);
	}

	@Override
	public String toString() {
		return builder.toString();
	}

	public String toJson() {
		JsonObject jsonObj = new JsonObject();
		for (String commitName : this.keySet()) {
			jsonObj.add(commitName, this.get(commitName).toJsonObject());
		}
		return jsonObj.toString();
	}
}
