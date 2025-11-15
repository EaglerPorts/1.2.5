package argo.jdom;

class JsonListenerToJdomAdapter_Object implements JsonListenerToJdomAdapter_NodeContainer {
	final JsonObjectNodeBuilder nodeBuilder;
	final JsonListenerToJdomAdapter listenerToJdomAdapter;

	JsonListenerToJdomAdapter_Object(JsonListenerToJdomAdapter var1, JsonObjectNodeBuilder var2) {
		this.listenerToJdomAdapter = var1;
		this.nodeBuilder = var2;
	}

	public void addNode(JsonNodeBuilder var1) {
		throw new RuntimeException("Coding failure in Argo:  Attempt to add a node to an object.");
	}

	public void addField(JsonFieldBuilder var1) {
		this.nodeBuilder.withFieldBuilder(var1);
	}
}
