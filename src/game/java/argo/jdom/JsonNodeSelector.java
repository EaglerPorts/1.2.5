package argo.jdom;

public final class JsonNodeSelector {
	final Functor valueGetter;

	JsonNodeSelector(Functor var1) {
		this.valueGetter = var1;
	}

	public boolean matches(Object var1) {
		return this.valueGetter.matchesNode(var1);
	}

	public Object getValue(Object var1) {
		return this.valueGetter.applyTo(var1);
	}

	public JsonNodeSelector with(JsonNodeSelector var1) {
		return new JsonNodeSelector(new ChainedFunctor(this, var1));
	}

	String shortForm() {
		return this.valueGetter.shortForm();
	}

	public String toString() {
		return this.valueGetter.toString();
	}
}
