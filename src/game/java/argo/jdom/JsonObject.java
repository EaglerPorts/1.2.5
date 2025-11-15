package argo.jdom;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

final class JsonObject extends JsonRootNode {
	private final Map fields;

	JsonObject(Map var1) {
		this.fields = new HashMap(var1);
	}

	public Map getFields() {
		return new HashMap(this.fields);
	}

	public JsonNodeType getType() {
		return JsonNodeType.OBJECT;
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public List getElements() {
		throw new IllegalStateException("Attempt to get elements on a JsonNode without elements.");
	}

	public boolean equals(Object var1) {
		if(this == var1) {
			return true;
		} else if(var1 != null && this.getClass() == var1.getClass()) {
			JsonObject var2 = (JsonObject)var1;
			return this.fields.equals(var2.fields);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.fields.hashCode();
	}

	public String toString() {
		return "JsonObject fields:[" + this.fields + "]";
	}
}
