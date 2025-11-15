package net.minecraft.src;

import java.util.List;

import net.lax1dude.eaglercraft.internal.vfs2.VFile2;

public class AnvilSaveHandler extends SaveHandler {
	public AnvilSaveHandler(VFile2 var1, String var2, boolean var3) {
		super(var1, var2, var3);
	}

	public IChunkLoader getChunkLoader(WorldProvider var1) {
		VFile2 var2 = this.getSaveDirectory();
		VFile2 var3;
		if(var1 instanceof WorldProviderHell) {
			var3 = new VFile2(var2, "DIM-1");
			return new AnvilChunkLoader(var3);
		} else if(var1 instanceof WorldProviderEnd) {
			var3 = new VFile2(var2, "DIM1");
			return new AnvilChunkLoader(var3);
		} else {
			return new AnvilChunkLoader(var2);
		}
	}

	public void saveWorldInfoAndPlayer(WorldInfo var1, List var2) {
		var1.setSaveVersion(19133);
		super.saveWorldInfoAndPlayer(var1, var2);
	}
}
