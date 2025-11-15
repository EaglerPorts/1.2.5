package net.minecraft.src;

import dev.colbster937.eaglercraft.vfs.VFilenameFilter;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

class AnvilSaveConverterFileFilter implements VFilenameFilter {
	final AnvilSaveConverter parent;

	AnvilSaveConverterFileFilter(AnvilSaveConverter var1) {
		this.parent = var1;
	}

	public boolean accept(VFile2 var1, String var2) {
		return var2.endsWith(".mcr");
	}
}
