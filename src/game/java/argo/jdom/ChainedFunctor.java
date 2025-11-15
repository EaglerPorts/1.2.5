package argo.jdom;

final class ChainedFunctor implements Functor {
	private final JsonNodeSelector parentJsonNodeSelector;
	private final JsonNodeSelector childJsonNodeSelector;

	ChainedFunctor(JsonNodeSelector var1, JsonNodeSelector var2) {
		this.parentJsonNodeSelector = var1;
		this.childJsonNodeSelector = var2;
	}

	public boolean matchesNode(Object var1) {
		return this.parentJsonNodeSelector.matches(var1) && this.childJsonNodeSelector.matches(this.parentJsonNodeSelector.getValue(var1));
	}

	public Object applyTo(Object var1) {
		Object var2;
		try {
			var2 = this.parentJsonNodeSelector.getValue(var1);
		} catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var6) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27321_b(var6, this.parentJsonNodeSelector);
		}

		try {
			Object var3 = this.childJsonNodeSelector.getValue(var2);
			return var3;
		} catch (JsonNodeDoesNotMatchChainedJsonNodeSelectorException var5) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27323_a(var5, this.parentJsonNodeSelector);
		}
	}

	public String shortForm() {
		return this.childJsonNodeSelector.shortForm();
	}

	public String toString() {
		return this.parentJsonNodeSelector.toString() + ", with " + this.childJsonNodeSelector.toString();
	}
}
