package argo.jdom;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

final class JsonArray extends JsonRootNode {
	private final List elements;

	JsonArray(Iterable var1) {
		this.elements = asList(var1);
	}

	public JsonNodeType getType() {
		return JsonNodeType.ARRAY;
	}

	public List getElements() {
		return new ArrayList(this.elements);
	}

	public String getText() {
		throw new IllegalStateException("Attempt to get text on a JsonNode without text.");
	}

	public Map getFields() {
		throw new IllegalStateException("Attempt to get fields on a JsonNode without fields.");
	}

	public boolean equals(Object var1) {
		if(this == var1) {
			return true;
		} else if(var1 != null && this.getClass() == var1.getClass()) {
			JsonArray var2 = (JsonArray)var1;
			return this.elements.equals(var2.elements);
		} else {
			return false;
		}
	}

	public int hashCode() {
		return this.elements.hashCode();
	}

	public String toString() {
		return "JsonArray elements:[" + this.elements + "]";
	}

	private static List asList(Iterable var0) {
		return new JsonArray_NodeList(var0);
	}
}
