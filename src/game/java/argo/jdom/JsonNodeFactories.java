package argo.jdom;

import java.util.Arrays;
import java.util.Map;

public final class JsonNodeFactories {
	public static JsonNode aJsonNull() {
		return JsonConstants.NULL;
	}

	public static JsonNode aJsonTrue() {
		return JsonConstants.TRUE;
	}

	public static JsonNode aJsonFalse() {
		return JsonConstants.FALSE;
	}

	public static JsonStringNode aJsonString(String var0) {
		return new JsonStringNode(var0);
	}

	public static JsonNode aJsonNumber(String var0) {
		return new JsonNumberNode(var0);
	}

	public static JsonRootNode aJsonArray(Iterable var0) {
		return new JsonArray(var0);
	}

	public static JsonRootNode aJsonArray(JsonNode... var0) {
		return aJsonArray((Iterable)Arrays.asList(var0));
	}

	public static JsonRootNode aJsonObject(Map var0) {
		return new JsonObject(var0);
	}
}
