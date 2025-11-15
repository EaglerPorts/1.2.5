package argo.jdom;

class JsonListenerToJdomAdapter_Array implements JsonListenerToJdomAdapter_NodeContainer {
	final JsonArrayNodeBuilder nodeBuilder;
	final JsonListenerToJdomAdapter listenerToJdomAdapter;

	JsonListenerToJdomAdapter_Array(JsonListenerToJdomAdapter var1, JsonArrayNodeBuilder var2) {
		this.listenerToJdomAdapter = var1;
		this.nodeBuilder = var2;
	}

	public void addNode(JsonNodeBuilder var1) {
		this.nodeBuilder.withElement(var1);
	}

	public void addField(JsonFieldBuilder var1) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a field to an array.");
	}
}
