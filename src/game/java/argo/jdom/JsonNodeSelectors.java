package argo.jdom;

import java.util.Arrays;

public final class JsonNodeSelectors {
	public static JsonNodeSelector func_27349_a(Object... var0) {
		return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_String()));
	}

	public static JsonNodeSelector func_27346_b(Object... var0) {
		return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Array()));
	}

	public static JsonNodeSelector func_27353_c(Object... var0) {
		return chainOn(var0, new JsonNodeSelector(new JsonNodeSelectors_Object()));
	}

	public static JsonNodeSelector func_27348_a(String var0) {
		return func_27350_a(JsonNodeFactories.aJsonString(var0));
	}

	public static JsonNodeSelector func_27350_a(JsonStringNode var0) {
		return new JsonNodeSelector(new JsonNodeSelectors_Field(var0));
	}

	public static JsonNodeSelector func_27351_b(String var0) {
		return func_27353_c(new Object[0]).with(func_27348_a(var0));
	}

	public static JsonNodeSelector func_27347_a(int var0) {
		return new JsonNodeSelector(new JsonNodeSelectors_Element(var0));
	}

	public static JsonNodeSelector func_27354_b(int var0) {
		return func_27346_b(new Object[0]).with(func_27347_a(var0));
	}

	private static JsonNodeSelector chainOn(Object[] var0, JsonNodeSelector var1) {
		JsonNodeSelector var2 = var1;

		for(int var3 = var0.length - 1; var3 >= 0; --var3) {
			if(var0[var3] instanceof Integer) {
				var2 = chainedJsonNodeSelector(func_27354_b(((Integer)var0[var3]).intValue()), var2);
			} else {
				if(!(var0[var3] instanceof String)) {
					throw new IllegalArgumentException("Element [" + var0[var3] + "] of path elements" + " [" + Arrays.toString(var0) + "] was of illegal type [" + var0[var3].getClass().getCanonicalName() + "]; only Integer and String are valid.");
				}

				var2 = chainedJsonNodeSelector(func_27351_b((String)var0[var3]), var2);
			}
		}

		return var2;
	}

	private static JsonNodeSelector chainedJsonNodeSelector(JsonNodeSelector var0, JsonNodeSelector var1) {
		return new JsonNodeSelector(new ChainedFunctor(var0, var1));
	}
}
