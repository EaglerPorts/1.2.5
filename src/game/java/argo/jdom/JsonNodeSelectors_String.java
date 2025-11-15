package argo.jdom;

final class JsonNodeSelectors_String extends LeafFunctor {
	public boolean func_27072_a(JsonNode var1) {
		return JsonNodeType.STRING == var1.getType();
	}

	public String shortForm() {
		return "A short form string";
	}

	public String func_27073_b(JsonNode var1) {
		return var1.getText();
	}

	public String toString() {
		return "a value that is a string";
	}

	public Object typeSafeApplyTo(Object var1) {
		return this.func_27073_b((JsonNode)var1);
	}

	public boolean matchesNode(Object var1) {
		return this.func_27072_a((JsonNode)var1);
	}
}
