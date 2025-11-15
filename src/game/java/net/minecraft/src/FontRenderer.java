package net.minecraft.src;

import java.util.regex.Pattern;

public class FontRenderer extends net.peyton.eagler.minecraft.FontRenderer {
	private static final Pattern field_52015_r = Pattern.compile("(?i)\\u00A7[0-9A-FK-OR]");

	public FontRenderer(GameSettings var0, String var1, RenderEngine var2, boolean var3) {
		super(var0, var1, var2, var3);
	}

	public static String func_52014_d(String var0) {
		return field_52015_r.matcher(var0).replaceAll("");
	}
}