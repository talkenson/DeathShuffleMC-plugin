package talkenson.tkpl.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import talkenson.tkpl.Msg;

public class Default implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (!(sender instanceof Player)) {
      Msg.send(sender, "&cOnly players can use this command");
      return true;
    }

    var player = (Player) sender;


    return false;
  }
}
