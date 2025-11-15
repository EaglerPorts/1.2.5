package net.minecraft.src;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.lwjgl.opengl.GL11;

import dev.colbster937.eaglercraft.rp.TexturePack;
import net.lax1dude.eaglercraft.internal.buffer.ByteBuffer;
import net.lax1dude.eaglercraft.internal.buffer.IntBuffer;
import net.lax1dude.eaglercraft.opengl.ImageData;
import net.peyton.java.awt.Color;

public class RenderEngine {
	public static boolean useMipmaps = false;
	private HashMap textureMap = new HashMap();
	private HashMap textureContentsMap = new HashMap();
	private IntHashMap textureNameToImageMap = new IntHashMap();
	private IntBuffer singleIntBuffer = GLAllocation.createDirectIntBuffer(1);
	private ByteBuffer imageData = GLAllocation.createDirectByteBuffer(16777216);
	private List textureList = new ArrayList();
	private Map urlToImageDataMap = new HashMap();
	private GameSettings options;
	public boolean clampTexture = false;
	public boolean blurTexture = false;
	private ImageData missingTextureImage = new ImageData(64, 64, true);

	public RenderEngine(GameSettings var1) {
		this.options = var1;
		int[] missingTexture = new int[256];
		for (int i = 0; i < 256; ++i)
			missingTexture[i] = ((i / 16 + (i % 16)) % 2 == 0) ? Color.MAGENTA.getRGB() : Color.BLACK.getRGB();
		this.missingTextureImage = new ImageData(16, 16, missingTexture, true);
	}

	public int[] getTextureContents(String var1) {
		int[] var3 = (int[])this.textureContentsMap.get(var1);
		if(var3 != null) {
			return var3;
		} else {
			try {
				Object var6 = null;
				if(var1.startsWith("##")) {
					var3 = this.getImageContentsAndAllocate(this.unwrapImageByColumns(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(2)))));
				} else if(var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					var3 = this.getImageContentsAndAllocate(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(7))));
					this.clampTexture = false;
				} else if(var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var3 = this.getImageContentsAndAllocate(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(6))));
					this.clampTexture = false;
					this.blurTexture = false;
				} else {
					InputStream var7 = TexturePack.getResourceAsStream(var1);
					if(var7 == null) {
						var3 = this.getImageContentsAndAllocate(this.missingTextureImage);
					} else {
						var3 = this.getImageContentsAndAllocate(this.readTextureImage(var7));
					}
				}

				this.textureContentsMap.put(var1, var3);
				return var3;
			} catch (IOException var5) {
				var5.printStackTrace();
				int[] var4 = this.getImageContentsAndAllocate(this.missingTextureImage);
				this.textureContentsMap.put(var1, var4);
				return var4;
			}
		}
	}

	private int[] getImageContentsAndAllocate(ImageData var1) {
		int var2 = var1.getWidth();
		int var3 = var1.getHeight();
		int[] var4 = new int[var2 * var3];
		var1.getRGB(0, 0, var2, var3, var4, 0, var2);
		return ImageData.swapRB(var4);
	}

	private int[] getImageContents(ImageData var1, int[] var2) {
		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		var1.getRGB(0, 0, var3, var4, var2, 0, var3);
		return var2;
	}

	public int getTexture(String var1) {
		Integer var3 = (Integer)this.textureMap.get(var1);
		if(var3 != null) {
			return var3.intValue();
		} else {
			try {
				this.singleIntBuffer.clear();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var6 = this.singleIntBuffer.get(0);
				if(var1.startsWith("##")) {
					this.setupTexture(this.unwrapImageByColumns(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(2)))), var6);
				} else if(var1.startsWith("%clamp%")) {
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(7))), var6);
					this.clampTexture = false;
				} else if(var1.startsWith("%blur%")) {
					this.blurTexture = true;
					this.setupTexture(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(6))), var6);
					this.blurTexture = false;
				} else if(var1.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					this.setupTexture(this.readTextureImage(TexturePack.getResourceAsStream(var1.substring(11))), var6);
					this.blurTexture = false;
					this.clampTexture = false;
				} else {
					InputStream var7 = TexturePack.getResourceAsStream(var1);
					if(var7 == null) {
						this.setupTexture(this.missingTextureImage, var6);
					} else {
						this.setupTexture(this.readTextureImage(var7), var6);
					}
				}

				this.textureMap.put(var1, Integer.valueOf(var6));
				return var6;
			} catch (Exception var5) {
				var5.printStackTrace();
				GLAllocation.generateTextureNames(this.singleIntBuffer);
				int var4 = this.singleIntBuffer.get(0);
				this.setupTexture(this.missingTextureImage, var4);
				this.textureMap.put(var1, Integer.valueOf(var4));
				return var4;
			}
		}
	}

	private ImageData unwrapImageByColumns(ImageData var1) {
		int var2 = var1.getWidth() / 16;
		ImageData var3 = new ImageData(16, var1.getHeight() * var2, true);

		int var4 = var1.getWidth();
		int var5 = var1.getHeight();

		for (int var6 = 0; var6 < var2; ++var6) {
			int var7 = var6 * 16;
			int var8 = var6 * var5;

			for (int var9 = 0; var9 < var5; ++var9) {
				int var10 = var7 + var9 * var4;
				int var11 = (var8 + var9) * 16;
				System.arraycopy(var1.pixels, var10, var3.pixels, var11, 16);
			}
		}

		return var3;
	}

	public int allocateAndSetupTexture(ImageData var1) {
		this.singleIntBuffer.clear();
		GLAllocation.generateTextureNames(this.singleIntBuffer);
		int var2 = this.singleIntBuffer.get(0);
		this.setupTexture(var1, var2);
		this.textureNameToImageMap.addKey(var2, var1);
		return var2;
	}

	public void setupTexture(ImageData var1, int var2) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var2);
		if(useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		int var3 = var1.getWidth();
		int var4 = var1.getHeight();
		int[] var5 = new int[var3 * var4];
		byte[] var6 = new byte[var3 * var4 * 4];
		var1.getRGB(0, 0, var3, var4, var5, 0, var3);

		int var7;
		int var8;
		int var9;
		int var10;
		int var11;
		int var12;
		int var13;
		int var14;
		for(var7 = 0; var7 < var5.length; ++var7) {
			var8 = var5[var7] >> 24 & 255;
			var9 = var5[var7] >> 16 & 255;
			var10 = var5[var7] >> 8 & 255;
			var11 = var5[var7] & 255;
			if(this.options != null && this.options.anaglyph) {
				var12 = (var9 * 30 + var10 * 59 + var11 * 11) / 100;
				var13 = (var9 * 30 + var10 * 70) / 100;
				var14 = (var9 * 30 + var11 * 70) / 100;
				var9 = var12;
				var10 = var13;
				var11 = var14;
			}

			var6[var7 * 4 + 0] = (byte)var11;
			var6[var7 * 4 + 1] = (byte)var10;
			var6[var7 * 4 + 2] = (byte)var9;
			var6[var7 * 4 + 3] = (byte)var8;
		}

		this.imageData.clear();
		this.imageData.put(var6);
		this.imageData.position(0).limit(var6.length);
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, var3, var4, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
		if(useMipmaps) {
			for(var7 = 1; var7 <= 4; ++var7) {
				var8 = var3 >> var7 - 1;
				var9 = var3 >> var7;
				var10 = var4 >> var7;

				for(var11 = 0; var11 < var9; ++var11) {
					for(var12 = 0; var12 < var10; ++var12) {
						var13 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 0) * var8) * 4);
						var14 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 0) * var8) * 4);
						int var15 = this.imageData.getInt((var11 * 2 + 1 + (var12 * 2 + 1) * var8) * 4);
						int var16 = this.imageData.getInt((var11 * 2 + 0 + (var12 * 2 + 1) * var8) * 4);
						int var17 = this.alphaBlend(this.alphaBlend(var13, var14), this.alphaBlend(var15, var16));
						this.imageData.putInt((var11 + var12 * var9) * 4, var17);
					}
				}

				GL11.glTexImage2D(GL11.GL_TEXTURE_2D, var7, GL11.GL_RGBA, var9, var10, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
			}
		}

	}

	public void createTextureFromBytes(int[] var1, int var2, int var3, int var4) {
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, var4);
		if(useMipmaps) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST_MIPMAP_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		}

		if(this.blurTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		}

		if(this.clampTexture) {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		} else {
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
			GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
		}

		byte[] var5 = new byte[var2 * var3 * 4];

		for(int var6 = 0; var6 < var1.length; ++var6) {
			int var7 = var1[var6] >> 24 & 255;
			int var8 = var1[var6] >> 16 & 255;
			int var9 = var1[var6] >> 8 & 255;
			int var10 = var1[var6] & 255;
			if(this.options != null && this.options.anaglyph) {
				int var11 = (var8 * 30 + var9 * 59 + var10 * 11) / 100;
				int var12 = (var8 * 30 + var9 * 70) / 100;
				int var13 = (var8 * 30 + var10 * 70) / 100;
				var8 = var11;
				var9 = var12;
				var10 = var13;
			}

			var5[var6 * 4 + 0] = (byte)var8;
			var5[var6 * 4 + 1] = (byte)var9;
			var5[var6 * 4 + 2] = (byte)var10;
			var5[var6 * 4 + 3] = (byte)var7;
		}

		this.imageData.clear();
		this.imageData.put(var5);
		this.imageData.position(0).limit(var5.length);
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, 0, 0, var2, var3, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
	}

	public void deleteTexture(int var1) {
		this.textureNameToImageMap.removeObject(var1);
		this.singleIntBuffer.clear();
		this.singleIntBuffer.put(var1);
		this.singleIntBuffer.flip();
		GL11.glDeleteTextures(this.singleIntBuffer);
	}

	public int getTextureForDownloadableImage(String var1, String var2) {
		return this.getTexture(var2);
	}

	public void releaseImageData(String var1) {
	}

	public void registerTextureFX(TextureFX var1) {
		this.textureList.add(var1);
		var1.onTick();
	}

	public void updateDynamicTextures() {
		int var1 = -1;

		for(int var2 = 0; var2 < this.textureList.size(); ++var2) {
			TextureFX var3 = (TextureFX)this.textureList.get(var2);
			var3.anaglyphEnabled = this.options.anaglyph;
			var3.onTick();
			this.imageData.clear();
			this.imageData.put(var3.imageData);
			this.imageData.position(0).limit(var3.imageData.length);
			if(var3.iconIndex != var1) {
				var3.bindImage(this);
				var1 = var3.iconIndex;
			}

			for(int var4 = 0; var4 < var3.tileSize; ++var4) {
				for(int var5 = 0; var5 < var3.tileSize; ++var5) {
					GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, var3.iconIndex % 16 * 16 + var4 * 16, var3.iconIndex / 16 * 16 + var5 * 16, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, (ByteBuffer)this.imageData);
				}
			}
		}

	}

	private int alphaBlend(int var1, int var2) {
		int var3 = (var1 & -16777216) >> 24 & 255;
		int var4 = (var2 & -16777216) >> 24 & 255;
		short var5 = 255;
		short var15;
		short var16;
		if(var3 + var4 < 255) {
			var5 = 0;
			var15 = 1;
			var16 = 1;
		} else if(var3 > var4) {
			var15 = 255;
			var16 = 1;
		} else {
			var15 = 1;
			var16 = 255;
		}

		int var6 = (var1 >> 16 & 255) * var15;
		int var7 = (var1 >> 8 & 255) * var15;
		int var8 = (var1 & 255) * var15;
		int var9 = (var2 >> 16 & 255) * var16;
		int var10 = (var2 >> 8 & 255) * var16;
		int var11 = (var2 & 255) * var16;
		int var12 = (var6 + var9) / (var15 + var16);
		int var13 = (var7 + var10) / (var15 + var16);
		int var14 = (var8 + var11) / (var15 + var16);
		return var5 << 24 | var12 << 16 | var13 << 8 | var14;
	}

	public void refreshTextures() {
		Iterator var2 = this.textureNameToImageMap.getKeySet().iterator();

		ImageData var4;
		while(var2.hasNext()) {
			int var3 = ((Integer)var2.next()).intValue();
			var4 = (ImageData)this.textureNameToImageMap.lookup(var3);
			this.setupTexture(var4, var3);
		}

		var2 = this.textureMap.keySet().iterator();

		String var9;
		while(var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				if(var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(2))));
				} else if(var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(7)));
				} else if(var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(6)));
				} else if(var9.startsWith("%blurclamp%")) {
					this.blurTexture = true;
					this.clampTexture = true;
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(11)));
				} else {
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9));
				}

				int var5 = ((Integer)this.textureMap.get(var9)).intValue();
				this.setupTexture(var4, var5);
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var7) {
				var7.printStackTrace();
			}
		}

		var2 = this.textureContentsMap.keySet().iterator();

		while(var2.hasNext()) {
			var9 = (String)var2.next();

			try {
				if(var9.startsWith("##")) {
					var4 = this.unwrapImageByColumns(this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(2))));
				} else if(var9.startsWith("%clamp%")) {
					this.clampTexture = true;
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(7)));
				} else if(var9.startsWith("%blur%")) {
					this.blurTexture = true;
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9.substring(6)));
				} else {
					var4 = this.readTextureImage(TexturePack.getResourceAsStream(var9));
				}

				this.getImageContents(var4, (int[])this.textureContentsMap.get(var9));
				this.blurTexture = false;
				this.clampTexture = false;
			} catch (IOException var6) {
				var6.printStackTrace();
			}
		}

	}

	private ImageData readTextureImage(InputStream var1) throws IOException {
		ImageData var2 = ImageData.loadImageFile(var1);
		var1.close();
		return var2;
	}

	public void bindTexture(int var1) {
		if(var1 >= 0) {
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, var1);
		}
	}
}
