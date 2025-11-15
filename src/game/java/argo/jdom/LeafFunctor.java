package argo.jdom;

abstract class LeafFunctor implements Functor {
	public final Object applyTo(Object var1) {
		if(!this.matchesNode(var1)) {
			throw JsonNodeDoesNotMatchChainedJsonNodeSelectorException.func_27322_a(this);
		} else {
			return this.typeSafeApplyTo(var1);
		}
	}

	protected abstract Object typeSafeApplyTo(Object var1);
}
