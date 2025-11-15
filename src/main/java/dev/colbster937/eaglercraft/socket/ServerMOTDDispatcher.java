package dev.colbster937.eaglercraft.socket;

import java.util.List;

import org.json.JSONObject;

import dev.colbster937.eaglercraft.utils.I18n;
import net.lax1dude.eaglercraft.internal.EnumEaglerConnectionState;
import net.lax1dude.eaglercraft.internal.IWebSocketClient;
import net.lax1dude.eaglercraft.internal.IWebSocketFrame;
import net.minecraft.src.ServerNBTStorage;

public class ServerMOTDDispatcher {
  private final ServerNBTStorage server;
  private final IWebSocketClient webSocket;

  private long timer = 0L;
  private boolean hasSentAccept = false;
  public boolean isFinished = false;

  public ServerMOTDDispatcher(ServerNBTStorage server, IWebSocketClient webSocket) {
    this.server = server;
    this.webSocket = webSocket;
    this.server.pingSentTime = System.currentTimeMillis();
  }

  public void update() {
    if (this.timer >= 150 || this.webSocket.getState() == EnumEaglerConnectionState.FAILED) {
      this.server.lag = -1L;
      this.server.motd = "\u00a74" + I18n.format("cantReachServer");
      finish();
      return;
    }
    if (!this.hasSentAccept && this.webSocket.getState() == EnumEaglerConnectionState.CONNECTED) {
      this.hasSentAccept = true;
      this.webSocket.send("Accept: MOTD");
    }
    List<IWebSocketFrame> frames = this.webSocket.getNextFrames();
    if (frames != null) {
      for (IWebSocketFrame frame : frames) {
        if (frame.isString()) {
          String str = frame.getString();
          try {
            JSONObject obj = new JSONObject(str);
            JSONObject infoObj = obj.getJSONObject("data");
            this.server.lag = System.currentTimeMillis() - this.server.pingSentTime;
            this.server.motd = "\u00a77" + infoObj.getJSONArray("motd").getString(0);
            this.server.playerCount = "\u00a77" + infoObj.getInt("online") + "\u00a78/\u00a77"
                + infoObj.getInt("max");
            finish();
          } catch (Throwable t) {
            t.printStackTrace();
          }
        }
      }
    }
    timer++;
  }

  private void finish() {
    this.isFinished = true;
    try {
      this.webSocket.close();
    } catch (Throwable t) {
    }
  }
}
