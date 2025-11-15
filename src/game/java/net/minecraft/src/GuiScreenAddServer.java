package net.minecraft.src;

import org.lwjgl.input.Keyboard;

import dev.colbster937.eaglercraft.FormattingCodes;
import dev.colbster937.eaglercraft.utils.I18n;
import net.lax1dude.eaglercraft.EagRuntime;

public class GuiScreenAddServer extends GuiScreen {
	private GuiScreen parentGui;
	private GuiTextField serverAddress;
	private GuiTextField serverName;
	private ServerNBTStorage serverNBTStorage;

	private GuiButton hideAddress;

	public GuiScreenAddServer(GuiScreen var1, ServerNBTStorage var2) {
		this.parentGui = var1;
		this.serverNBTStorage = var2;
	}

	public void updateScreen() {
		this.serverName.updateCursorCounter();
		this.serverAddress.updateCursorCounter();
	}

	public void initGui() {
		StringTranslate var1 = StringTranslate.getInstance();
		Keyboard.enableRepeatEvents(true);
		this.controlList.clear();
		GuiButton done;
		GuiButton cancel;
		this.controlList.add(done = new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("addServer.add")));
		this.controlList.add(cancel = new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("gui.cancel")));
		if (EagRuntime.requireSSL()) {
			done.yPosition = cancel.yPosition;
			done.field_52008_a = (done.field_52008_a / 2) - 2;
			cancel.field_52008_a = (cancel.field_52008_a / 2) - 2;
			done.xPosition += cancel.field_52008_a + 4;
		}
		this.serverName = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 76, 200, 20);
		this.serverName.func_50033_b(true);
		this.serverName.setText(this.serverNBTStorage.name);
		this.serverAddress = new GuiTextField(this.fontRenderer, this.width / 2 - 100, 116, 200, 20);
		this.serverAddress.setMaxStringLength(128);
		this.serverAddress.setText(this.serverNBTStorage.host);
		this.controlList.add(this.hideAddress = new GuiButton(2, this.width / 2 - 100, 148, 200, 20,
				I18n.format("addServer.hideAddress") + ": "
						+ I18n.format(this.serverNBTStorage.hideAddress ? "gui.yes" : "gui.no")));
		((GuiButton)this.controlList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
	}

	public void onGuiClosed() {
		Keyboard.enableRepeatEvents(false);
	}

	protected void actionPerformed(GuiButton var1) {
		if(var1.enabled) {
			if(var1.id == 1) {
				this.parentGui.confirmClicked(false, 0);
			} else if(var1.id == 0) {
				this.serverNBTStorage.name = this.serverName.getText();
				this.serverNBTStorage.host = this.serverAddress.getText();
				this.parentGui.confirmClicked(true, 0);
			} else if(var1.id == 2) {
				this.serverNBTStorage.hideAddress = !this.serverNBTStorage.hideAddress;
				this.hideAddress.displayString = I18n.format( "addServer.hideAddress")
						+ ": " + I18n.format(this.serverNBTStorage.hideAddress ? "gui.yes" : "gui.no");
			}

		}
	}

	protected void keyTyped(char var1, int var2) {
		this.serverName.func_50037_a(var1, var2);
		this.serverAddress.func_50037_a(var1, var2);
		if(var1 == 9) {
			if(this.serverName.func_50025_j()) {
				this.serverName.func_50033_b(false);
				this.serverAddress.func_50033_b(true);
			} else {
				this.serverName.func_50033_b(true);
				this.serverAddress.func_50033_b(false);
			}
		}

		if(var1 == 13) {
			this.actionPerformed((GuiButton)this.controlList.get(0));
		}

		((GuiButton)this.controlList.get(0)).enabled = this.serverAddress.getText().length() > 0 && this.serverAddress.getText().split(":").length > 0 && this.serverName.getText().length() > 0;
		if(((GuiButton)this.controlList.get(0)).enabled) {
			String var3 = this.serverAddress.getText().trim();
			String[] var4 = var3.split(":");
			if(var4.length > 2) {
				((GuiButton)this.controlList.get(0)).enabled = false;
			}
		}

	}

	protected void mouseClicked(int var1, int var2, int var3) {
		super.mouseClicked(var1, var2, var3);
		this.serverAddress.mouseClicked(var1, var2, var3);
		this.serverName.mouseClicked(var1, var2, var3);
	}

	public void drawScreen(int var1, int var2, float var3) {
		StringTranslate var4 = StringTranslate.getInstance();
		this.drawDefaultBackground();
		this.drawCenteredString(this.fontRenderer, var4.translateKey("addServer.title"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
		this.drawString(this.fontRenderer, var4.translateKey("addServer.enterName"), this.width / 2 - 100, 63, 10526880);
		this.drawString(this.fontRenderer, var4.translateKey("addServer.enterIp"), this.width / 2 - 100, 104, 10526880);
		if (EagRuntime.requireSSL()) {
			this.drawCenteredString(this.fontRenderer, I18n.format("addServer.SSLWarn1"), this.width / 2, 174,
					FormattingCodes.COLOR_INFO);
			this.drawCenteredString(this.fontRenderer, I18n.format("addServer.SSLWarn2"), this.width / 2, 186,
					FormattingCodes.COLOR_INFO);
		}
		this.serverName.drawTextBox();
		this.serverAddress.drawTextBox();
		super.drawScreen(var1, var2, var3);
	}
}
