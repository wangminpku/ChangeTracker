package codeshovel.changes;

import codeshovel.util.Utl;
import codeshovel.wrappers.StartEnvironment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import codeshovel.wrappers.Commit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//wangmin:定义了多处变更的类，继承Ychang，具体见调用
public class Ymultichange extends Ychange {

	private List<Ychange> changes;

	public Ymultichange(StartEnvironment startEnv, Commit commit, List<Ychange> changes) {
		super(startEnv, commit);
		this.changes = changes;
	}

	public List<Ychange> getChanges() {
		return changes;
	}

	@Override
	public String toString() {
		List<String> substrings = new ArrayList<>();
		for (Ychange change : changes) {
			substrings.add(change.toString());
		}
		return "Ymultichange(\n- "+StringUtils.join(substrings, "\n- ")+"\n)";
	}

	@Override
	public JsonObject toJsonObject() {
		JsonObject obj = super.toJsonObject();
		JsonArray subchanges = new JsonArray();
		for (Ychange change : this.changes) {
			subchanges.add(change.toJsonObject());
		}

		obj.add("subchanges", subchanges);
		return obj;
	}

	@Override
	public String getTypeAsString() {
		List<String> substrings = new ArrayList<>();
		for (Ychange change : changes) {
			substrings.add(change.getTypeAsString());
		}
		return "Ymultichange(" +StringUtils.join(substrings, ",") + ")";
	}
}
