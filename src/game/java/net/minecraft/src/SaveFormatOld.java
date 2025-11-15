package net.minecraft.src;

import java.util.ArrayList;
import java.util.List;

import dev.colbster937.eaglercraft.utils.SaveUtils;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.minecraft.client.Minecraft;

public class SaveFormatOld implements ISaveFormat {
	protected final VFile2 savesDirectory;

	public SaveFormatOld(VFile2 var1) {
		this.savesDirectory = var1;
	}

	public String getFormatName() {
		return "Old Format";
	}

	public List getSaveList() {
		ArrayList var1 = new ArrayList();

		for(int var2 = 0; var2 < 5; ++var2) {
			String var3 = "World" + (var2 + 1);
			WorldInfo var4 = this.getWorldInfo(var3);
			if(var4 != null) {
				var1.add(new SaveFormatComparator(var3, "", var4.getLastTimePlayed(), var4.getSizeOnDisk(), var4.getGameType(), false, var4.isHardcoreModeEnabled()));
			}
		}

		return var1;
	}

	public void flushCache() {
	}

	public WorldInfo getWorldInfo(String var1) {
		VFile2 var2 = new VFile2(this.savesDirectory, var1);
		VFile2 var3 = new VFile2(var2, "level.dat");
		if(!var3.exists()) {
			return null;
		} else {
			NBTTagCompound var4;
			NBTTagCompound var5;
			try {
				var4 = CompressedStreamTools.readCompressed(var3.getInputStream());
				var5 = var4.getCompoundTag("Data");
				return new WorldInfo(var5);
			} catch (Exception var7) {
				var7.printStackTrace();
			}

			var3 = new VFile2(var2, "level.dat_old");
			if(var3.exists()) {
				try {
					var4 = CompressedStreamTools.readCompressed(var3.getInputStream());
					var5 = var4.getCompoundTag("Data");
					return new WorldInfo(var5);
				} catch (Exception var6) {
					var6.printStackTrace();
				}
			}

			return null;
		}
	}

	public void renameWorld(String var1, String var2) {
		VFile2 var3 = new VFile2(this.savesDirectory, var1);
		VFile2 var4 = new VFile2(var3, "level.dat");
		if(var4.exists()) {
			try {
				NBTTagCompound var5 = CompressedStreamTools.readCompressed(var4.getInputStream());
				NBTTagCompound var6 = var5.getCompoundTag("Data");
				var6.setString("LevelName", var2);
				CompressedStreamTools.writeCompressed(var5, var4.getOutputStream());
			} catch (Exception var7) {
				var7.printStackTrace();
			}
		}
	}

	public void deleteWorldDirectory(String var1) {
		SaveUtils.i.delete(Minecraft.getMinecraft().loadingScreen, var1);
	}

	public ISaveHandler getSaveLoader(String var1, boolean var2) {
		return new SaveHandler(this.savesDirectory, var1, var2);
	}

	public boolean isOldMapFormat(String var1) {
		return false;
	}

	public boolean convertMapFormat(String var1, IProgressUpdate var2) {
		return false;
	}
}
