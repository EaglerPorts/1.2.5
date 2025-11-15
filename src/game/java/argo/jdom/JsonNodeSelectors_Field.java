package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Field extends LeafFunctor {
	final JsonStringNode field_27066_a;

	JsonNodeSelectors_Field(JsonStringNode var1) {
		this.field_27066_a = var1;
	}

	public boolean func_27065_a(Map var1) {
		return var1.containsKey(this.field_27066_a);
	}

	public String shortForm() {
		return "\"" + this.field_27066_a.getText() + "\"";
	}

	public JsonNode func_27064_b(Map var1) {
		return (JsonNode)var1.get(this.field_27066_a);
	}

	public String toString() {
		return "a field called [\"" + this.field_27066_a.getText() + "\"]";
	}

	public Object typeSafeApplyTo(Object var1) {
		return this.func_27064_b((Map)var1);
	}

	public boolean matchesNode(Object var1) {
		return this.func_27065_a((Map)var1);
	}
}
