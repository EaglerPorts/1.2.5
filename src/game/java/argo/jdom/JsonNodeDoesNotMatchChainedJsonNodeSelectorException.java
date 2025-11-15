package argo.jdom;

import java.util.LinkedList;
import java.util.List;

public final class JsonNodeDoesNotMatchChainedJsonNodeSelectorException extends JsonNodeDoesNotMatchJsonNodeSelectorException {
	final Functor failedNode;
	final List failPath;

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27322_a(Functor var0) {
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(var0, new LinkedList());
	}

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27323_a(JsonNodeDoesNotMatchChainedJsonNodeSelectorException var0, JsonNodeSelector var1) {
		LinkedList var2 = new LinkedList(var0.failPath);
		var2.add(var1);
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(var0.failedNode, var2);
	}

	static JsonNodeDoesNotMatchJsonNodeSelectorException func_27321_b(JsonNodeDoesNotMatchChainedJsonNodeSelectorException var0, JsonNodeSelector var1) {
		LinkedList var2 = new LinkedList();
		var2.add(var1);
		return new JsonNodeDoesNotMatchChainedJsonNodeSelectorException(var0.failedNode, var2);
	}

	private JsonNodeDoesNotMatchChainedJsonNodeSelectorException(Functor var1, List var2) {
		super("Failed to match any JSON node at [" + getShortFormFailPath(var2) + "]");
		this.failedNode = var1;
		this.failPath = var2;
	}

	static String getShortFormFailPath(List var0) {
		StringBuilder var1 = new StringBuilder();

		for(int var2 = var0.size() - 1; var2 >= 0; --var2) {
			var1.append(((JsonNodeSelector)var0.get(var2)).shortForm());
			if(var2 != 0) {
				var1.append(".");
			}
		}

		return var1.toString();
	}

	public String toString() {
		return "JsonNodeDoesNotMatchJsonNodeSelectorException{failedNode=" + this.failedNode + ", failPath=" + this.failPath + '}';
	}
}
