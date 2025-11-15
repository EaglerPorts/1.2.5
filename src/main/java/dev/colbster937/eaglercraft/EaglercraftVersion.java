package dev.colbster937.eaglercraft;

import net.lax1dude.eaglercraft.EagRuntime;

public class EaglercraftVersion {
	public static final String STORAGE_KEY = "_dev_colbster937_eaglercraft_1.2.5_";
	public static final String EAGLER_VERSION = "25w46a";
	public static final String MINECRAFT_VERSION = "1.2.5";
	public static final String PROJECT_GITHUB = "https://github.com/EaglerPorts/1.2.5/";
	public static final String PROJECT_AUTHOR = "Colbster937";
	public static final String PROJECT_BRAND = "colbster";
	public static final String LANG_CONTEXT = "eaglercraft";
	
	public static final String[] getTitleString() {
		return new String[] {
			"Eaglercraft " + EAGLER_VERSION + " [" + EagRuntime.getPlatformType() + "]",
			"Made by " + PROJECT_AUTHOR
		};
	}
}
