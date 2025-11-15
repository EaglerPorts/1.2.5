package argo.jdom;

final class JsonNumberNodeBuilder implements JsonNodeBuilder {
	private final JsonNode field_27239_a;

	JsonNumberNodeBuilder(String var1) {
		this.field_27239_a = JsonNodeFactories.aJsonNumber(var1);
	}

	public JsonNode buildNode() {
		return this.field_27239_a;
	}
}
