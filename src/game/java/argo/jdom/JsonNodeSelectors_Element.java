package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Element extends LeafFunctor {
	final int index;

	JsonNodeSelectors_Element(int var1) {
		this.index = var1;
	}

	public boolean matchesNode_(List var1) {
		return var1.size() > this.index;
	}

	public String shortForm() {
		return Integer.toString(this.index);
	}

	public JsonNode typeSafeApplyTo_(List var1) {
		return (JsonNode)var1.get(this.index);
	}

	public String toString() {
		return "an element at index [" + this.index + "]";
	}

	public Object typeSafeApplyTo(Object var1) {
		return this.typeSafeApplyTo_((List)var1);
	}

	public boolean matchesNode(Object var1) {
		return this.matchesNode_((List)var1);
	}
}
