package net.minecraft.src;

import dev.colbster937.eaglercraft.socket.ServerMOTDDispatcher;

public class ServerNBTStorage {
	public String name;
	public String host;
	public String playerCount;
	public String motd;
	public long lag;
	public long pingSentTime;
	public boolean polled = false;
	public boolean isDefault = false;
	public boolean hideAddress = false;
	public ServerMOTDDispatcher motdDispatcher;

	public ServerNBTStorage(String var1, String var2, boolean var3) {
		this.name = var1;
		this.host = var2;
		this.hideAddress = var3;
	}

	public ServerNBTStorage(String var1, String var2) {
		this(var1, var2, false);
	}

	public NBTTagCompound getCompoundTag() {
		NBTTagCompound var1 = new NBTTagCompound();
		var1.setString("name", this.name);
		var1.setString("ip", this.host);
		var1.setBoolean("hideAddr", this.hideAddress);
		return var1;
	}

	public static ServerNBTStorage createServerNBTStorage(NBTTagCompound var0) {
		return new ServerNBTStorage(var0.getString("name"), var0.getString("ip"), var0.getBoolean("hideAddr"));
	}
}
