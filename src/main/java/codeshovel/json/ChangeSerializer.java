package codeshovel.json;

import codeshovel.changes.Ychange;
import codeshovel.changes.Ycomparefunctionchange;
import codeshovel.changes.Ymultichange;
import codeshovel.parser.Yfunction;
import codeshovel.wrappers.Commit;
import com.google.gson.*;

import java.lang.reflect.Type;

//wangmin:json相关的类，需要熟悉gson的模块

public class ChangeSerializer implements JsonSerializer<Ychange> {

	@Override
	public JsonElement serialize(Ychange change,
			 Type typeOfSrc, JsonSerializationContext context) {

		return change.toJsonObject();
	};

}
