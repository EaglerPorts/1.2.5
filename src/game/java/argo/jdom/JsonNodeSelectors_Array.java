package argo.jdom;

import java.util.List;

final class JsonNodeSelectors_Array extends LeafFunctor {
	public boolean matchesNode_(JsonNode var1) {
		return JsonNodeType.ARRAY == var1.getType();
	}

	public String shortForm() {
		return "A short form array";
	}

	public List typeSafeApplyTo(JsonNode var1) {
		return var1.getElements();
	}

	public String toString() {
		return "an array";
	}

	public Object typeSafeApplyTo(Object var1) {
		return this.typeSafeApplyTo((JsonNode)var1);
	}

	public boolean matchesNode(Object var1) {
		return this.matchesNode_((JsonNode)var1);
	}
}
