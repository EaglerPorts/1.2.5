package argo.jdom;

import java.util.Map;

final class JsonNodeSelectors_Object extends LeafFunctor {
	public boolean func_27070_a(JsonNode var1) {
		return JsonNodeType.OBJECT == var1.getType();
	}

	public String shortForm() {
		return "A short form object";
	}

	public Map func_27071_b(JsonNode var1) {
		return var1.getFields();
	}

	public String toString() {
		return "an object";
	}

	public Object typeSafeApplyTo(Object var1) {
		return this.func_27071_b((JsonNode)var1);
	}

	public boolean matchesNode(Object var1) {
		return this.func_27070_a((JsonNode)var1);
	}
}
