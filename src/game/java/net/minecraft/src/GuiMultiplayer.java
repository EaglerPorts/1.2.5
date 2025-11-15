package net.minecraft.src;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import org.lwjgl.input.Keyboard;

import dev.colbster937.eaglercraft.FormattingCodes;
import dev.colbster937.eaglercraft.gui.GuiScreenInfo;
import dev.colbster937.eaglercraft.gui.GuiScreenInfo.TextLine;
import dev.colbster937.eaglercraft.socket.ServerMOTDDispatcher;
import dev.colbster937.eaglercraft.utils.I18n;
import net.lax1dude.eaglercraft.EagRuntime;
import net.lax1dude.eaglercraft.internal.IClientConfigAdapter.DefaultServer;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.PlatformNetworking;
import net.lax1dude.eaglercraft.internal.vfs2.VFile2;
import net.lax1dude.eaglercraft.socket.AddressResolver;

public class GuiMultiplayer extends GuiScreen {
	private static int threadsPending = 0;
	private static Object lock = new Object();
	private GuiScreen parentScreen;
	private GuiSlotServer serverSlotContainer;
	private List<ServerNBTStorage> serverList = new ArrayList<>();
	private int selectedServer = -1;
	private GuiButton buttonEdit;
	private GuiButton buttonSelect;
	private GuiButton buttonDelete;
	private boolean deleteClicked = false;
	private boolean addClicked = false;
	private boolean editClicked = false;
	private boolean directClicked = false;
	private String lagTooltip = null;
	private ServerNBTStorage tempServer = null;

	public GuiMultiplayer(GuiScreen var1) {
		this.parentScreen = var1;
	}

	public void updateScreen() {
		this.updateServerPing();
	}

	private void updateServerPing() {
		for (ServerNBTStorage server : this.serverList) {
			if (server.pingSentTime <= 0L) {
				server.lag = -2L;
				IWebSocketClient webSocket = PlatformNetworking.openWebSocket(AddressResolver.resolveURI(server));
				server.motdDispatcher = new ServerMOTDDispatcher(server, webSocket);
			} else if (server.motdDispatcher != null) {
				server.motdDispatcher.update();
				if (server.motdDispatcher.isFinished) {
					server.motdDispatcher = null;
				}
			}
		}
	}

	public void initGui() {
		this.loadServerList();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		this.serverSlotContainer = new GuiSlotServer(this);
		this.initGuiControls();
	}

	private void loadServerList() {
		try {
			this.serverList.clear();
			for (DefaultServer srv : EagRuntime.getConfiguration().getDefaultServerList()) {
				ServerNBTStorage dat = new ServerNBTStorage(srv.name, srv.addr, srv.hideAddress);
				dat.isDefault = true;
				this.serverList.add(dat);
			}
			NBTTagCompound var1 = CompressedStreamTools.read(new VFile2(this.mc.mcDataDir, "servers.dat"));
			if (var1 != null) {
				NBTTagList var2 = var1.getTagList("servers");
				for (int var3 = 0; var3 < var2.tagCount(); ++var3) {
					this.serverList.add(ServerNBTStorage.createServerNBTStorage((NBTTagCompound) var2.tagAt(var3)));
				}
			}
		} catch (Exception var4) {
			var4.printStackTrace();
		}

	}

	private void saveServerList() {
		try {
			NBTTagList var1 = new NBTTagList();

			for(int var2 = 0; var2 < this.serverList.size(); ++var2) {
				ServerNBTStorage srv = (ServerNBTStorage) this.serverList.get(var2);
				if (!srv.isDefault) var1.appendTag(srv.getCompoundTag());
			}

			NBTTagCompound var4 = new NBTTagCompound();
			var4.setTag("servers", var1);
			CompressedStreamTools.safeWrite(var4, new VFile2(this.mc.mcDataDir, "servers.dat"));
		} catch (Exception var3) {
			var3.printStackTrace();
		}

	}

	public void initGuiControls() {
		StringTranslate var1 = StringTranslate.getInstance();
		this.controlList.add(this.buttonEdit = new GuiButton(7, this.width / 2 - 154, this.height - 28, 70, 20, var1.translateKey("selectServer.edit")));
		this.controlList.add(this.buttonDelete = new GuiButton(2, this.width / 2 - 74, this.height - 28, 70, 20, var1.translateKey("selectServer.delete")));
		this.controlList.add(this.buttonSelect = new GuiButton(1, this.width / 2 - 154, this.height - 52, 100, 20, var1.translateKey("selectServer.select")));
		this.controlList.add(new GuiButton(4, this.width / 2 - 50, this.height - 52, 100, 20, var1.translateKey("selectServer.direct")));
		this.controlList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 52, 100, 20, var1.translateKey("selectServer.add")));
		this.controlList.add(new GuiButton(8, this.width / 2 + 4, this.height - 28, 70, 20, var1.translateKey("selectServer.refresh")));
		this.controlList.add(new GuiButton(0, this.width / 2 + 4 + 76, this.height - 28, 75, 20, var1.translateKey("gui.cancel")));
		boolean var2 = this.selectedServer >= 0 && this.selectedServer < this.serverSlotContainer.getSize();
		this.buttonSelect.enabled = var2;
		this.buttonEdit.enabled = var2;
		this.buttonDelete.enabled = var2;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 2) {
				String var2 = ((ServerNBTStorage)this.serverList.get(this.selectedServer)).name;
				if(var2 != null) {
					this.deleteClicked = true;
					StringTranslate var3 = StringTranslate.getInstance();
					String var4 = var3.translateKey("selectServer.deleteQuestion");
					String var5 = "\'" + var2 + "\' " + var3.translateKey("selectServer.deleteWarning");
					String var6 = var3.translateKey("selectServer.deleteButton");
					String var7 = var3.translateKey("gui.cancel");
					GuiYesNo var8 = new GuiYesNo(this, var4, var5, var6, var7, this.selectedServer);
					this.mc.displayGuiScreen(var8);
				}
			} else if(var1.id == 1) {
				this.joinServer(this.selectedServer);
			} else if(var1.id == 4) {
				this.directClicked = true;
				this.mc.displayGuiScreen(new GuiScreenServerList(this, this.tempServer = new ServerNBTStorage(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if(var1.id == 3) {
				this.addClicked = true;
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.tempServer = new ServerNBTStorage(StatCollector.translateToLocal("selectServer.defaultName"), "")));
			} else if(var1.id == 7) {
				this.editClicked = true;
				ServerNBTStorage var9 = (ServerNBTStorage)this.serverList.get(this.selectedServer);
				this.mc.displayGuiScreen(new GuiScreenAddServer(this, this.tempServer = new ServerNBTStorage(var9.name, var9.host, var9.hideAddress)));
			} else if(var1.id == 0) {
				this.mc.displayGuiScreen(this.parentScreen);
			} else if(var1.id == 8) {
				this.mc.displayGuiScreen(new GuiMultiplayer(this.parentScreen));
			} else {
				this.serverSlotContainer.actionPerformed(var1);
			}

		}
	}

	public void confirmClicked(boolean var1, int var2) {
		if(this.deleteClicked) {
			this.deleteClicked = false;
			if(var1) {
				this.serverList.remove(var2);
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.directClicked) {
			this.directClicked = false;
			if(var1) {
				this.joinServer(this.tempServer);
			} else {
				this.mc.displayGuiScreen(this);
			}
		} else if(this.addClicked) {
			this.addClicked = false;
			if(var1) {
				this.serverList.add(this.tempServer);
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		} else if(this.editClicked) {
			this.editClicked = false;
			if(var1) {
				ServerNBTStorage var3 = (ServerNBTStorage)this.serverList.get(this.selectedServer);
				var3.name = this.tempServer.name;
				var3.host = this.tempServer.host;
				var3.hideAddress = this.tempServer.hideAddress;
				this.saveServerList();
			}

			this.mc.displayGuiScreen(this);
		}

	}

	private int parseIntWithDefault(String var1, int var2) {
		try {
			return Integer.parseInt(var1.trim());
		} catch (Exception var4) {
			return var2;
		}
	}

	protected void keyTyped(char var1, int var2) {
		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(2));
		}

	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		this.lagTooltip = null;
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.serverSlotContainer.drawScreen(var1, var2, var3);
		this.drawCenteredString(this.fontRenderer, var4.translateKey("multiplayer.title"), this.width / 2, 20, 16777215);
		super.drawScreen(var1, var2, var3);
		if(this.lagTooltip != null) {
			this.func_35325_a(this.lagTooltip, var1, var2);
		}

	}

	private void joinServer(int var1) {
		this.joinServer((ServerNBTStorage)this.serverList.get(var1));
	}

	private void joinServer(ServerNBTStorage var1) {
		if (false) this.mc.displayGuiScreen(new GuiConnecting(this.mc, this, var1.host));
		else this.mc.displayGuiScreen(new GuiScreenInfo(this, new TextLine("notAdded", FormattingCodes.COLOR_ERROR, I18n.format("menu.multiplayer")), new TextLine(""), new TextLine("willAdd", 0x888888)));
	}

	protected void func_35325_a(String var1, int var2, int var3) {
		if(var1 != null) {
			int var4 = var2 + 12;
			int var5 = var3 - 12;
			int var6 = this.fontRenderer.getStringWidth(var1);
			this.drawGradientRect(var4 - 3, var5 - 3, var4 + var6 + 3, var5 + 8 + 3, -1073741824, -1073741824);
			this.fontRenderer.drawStringWithShadow(var1, var4, var5, -1);
		}
	}

	static List getServerList(GuiMultiplayer var0) {
		return var0.serverList;
	}

	static int setSelectedServer(GuiMultiplayer var0, int var1) {
		return var0.selectedServer = var1;
	}

	static int getSelectedServer(GuiMultiplayer var0) {
		return var0.selectedServer;
	}

	static GuiButton getButtonSelect(GuiMultiplayer var0) {
		return var0.buttonSelect;
	}

	static GuiButton getButtonEdit(GuiMultiplayer var0) {
		return var0.buttonEdit;
	}

	static GuiButton getButtonDelete(GuiMultiplayer var0) {
		return var0.buttonDelete;
	}

	static void joinServer(GuiMultiplayer var0, int var1) {
		var0.joinServer(var1);
	}

	static Object getLock() {
		return lock;
	}

	static int getThreadsPending() {
		return threadsPending;
	}

	static int incrementThreadsPending() {
		return threadsPending++;
	}

	static int decrementThreadsPending() {
		return threadsPending--;
	}

	static String setTooltipText(GuiMultiplayer var0, String var1) {
		return var0.lagTooltip = var1;
	}
}
