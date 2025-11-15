package argo.jdom;

public final class JsonNodeBuilders {
	public static JsonNodeBuilder func_27248_a() {
		return new JsonNodeBuilders_Null();
	}

	public static JsonNodeBuilder func_27251_b() {
		return new JsonNodeBuilders_True();
	}

	public static JsonNodeBuilder func_27252_c() {
		return new JsonNodeBuilders_False();
	}

	public static JsonNodeBuilder func_27250_a(String var0) {
		return new JsonNumberNodeBuilder(var0);
	}

	public static JsonStringNodeBuilder func_27254_b(String var0) {
		return new JsonStringNodeBuilder(var0);
	}

	public static JsonObjectNodeBuilder anObjectBuilder() {
		return new JsonObjectNodeBuilder();
	}

	public static JsonArrayNodeBuilder anArrayBuilder() {
		return new JsonArrayNodeBuilder();
	}
}
