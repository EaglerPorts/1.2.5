package dev.colbster937.eaglercraft.command;

import dev.colbster937.eaglercraft.SingleplayerCommands;
import dev.colbster937.eaglercraft.utils.I18n;

public class FlyCommand extends Command {
  public FlyCommand() {
    super("/fly", new String[] { "" }, "");
  }

  @Override
  public void run(String[] args) {
    if (args.length == 1) {
      this.mc.thePlayer.capabilities.allowFlying = !this.mc.thePlayer.capabilities.allowFlying;
      this.mc.thePlayer.capabilities.isFlying = this.mc.thePlayer.capabilities.isFlying;
      SingleplayerCommands.showChat(I18n.format("command.fly",
          this.mc.thePlayer.capabilities.allowFlying ? I18n.format("enabled") : I18n.format("disabled")));
    } else {
      this.showUsage(args[0]);
    }
  }
}