package talkenson.tkpl;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Msg {
  public static void send(CommandSender sender, String message) {
    send(sender, message, "&a");
  }

  public static void send(CommandSender sender, String message, String prefix) {
    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }

  public static void broadcast(String message) {
    broadcast(message, "&a");
  }

  public static void broadcast(String message, String prefix) {
    Main.getInstance()
        .getServer()
        .broadcastMessage(ChatColor.translateAlternateColorCodes('&', prefix + message));
  }
}
