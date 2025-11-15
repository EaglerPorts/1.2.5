package argo.jdom;

import argo.format.CompactJsonFormatter;
import argo.format.JsonFormatter;

public final class JsonNodeDoesNotMatchPathElementsException extends JsonNodeDoesNotMatchJsonNodeSelectorException {
	private static final JsonFormatter JSON_FORMATTER = new CompactJsonFormatter();

	static JsonNodeDoesNotMatchPathElementsException jsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException var0, Object[] var1, JsonRootNode var2) {
		return new JsonNodeDoesNotMatchPathElementsException(var0, var1, var2);
	}

	private JsonNodeDoesNotMatchPathElementsException(JsonNodeDoesNotMatchChainedJsonNodeSelectorException var1, Object[] var2, JsonRootNode var3) {
		super(formatMessage(var1, var2, var3));
	}

	private static String formatMessage(JsonNodeDoesNotMatchChainedJsonNodeSelectorException var0, Object[] var1, JsonRootNode var2) {
		return "Failed to find " + var0.failedNode.toString() + " at [" + JsonNodeDoesNotMatchChainedJsonNodeSelectorException.getShortFormFailPath(var0.failPath) + "] while resolving [" + commaSeparate(var1) + "] in " + JSON_FORMATTER.format(var2) + ".";
	}

	private static String commaSeparate(Object[] var0) {
		StringBuilder var1 = new StringBuilder();
		boolean var2 = true;
		Object[] var3 = var0;
		int var4 = var0.length;

		for(int var5 = 0; var5 < var4; ++var5) {
			Object var6 = var3[var5];
			if(!var2) {
				var1.append(".");
			}

			var2 = false;
			if(var6 instanceof String) {
				var1.append("\"").append(var6).append("\"");
			} else {
				var1.append(var6);
			}
		}

		return var1.toString();
	}
}
