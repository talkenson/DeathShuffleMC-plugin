package talkenson.tkpl;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.SimplePluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public abstract class CommandBase extends BukkitCommand implements CommandExecutor {
  private List<String> delayedPlayers = null;
  private int delay = 0;
  private final int minArguments;
  private final int maxArguments;
  private final boolean playerOnly;

  public CommandBase(String command) {
    this(command, 0);
  }

  public CommandBase(String command, boolean playerOnly) {
    this(command, 0, playerOnly);
  }

  public CommandBase(String command, int requiredArguments) {
    this(command, requiredArguments, requiredArguments);
  }

  public CommandBase(String command, int minArguments, int maxArguments) {
    this(command, minArguments, maxArguments, false);
  }

  public CommandBase(String command, int requiredArguments, boolean playerOnly) {
    this(command, requiredArguments, requiredArguments, playerOnly);
  }

  public CommandBase(String command, int minArguments, int maxArguments, boolean playerOnly) {
    super(command);
    this.minArguments = minArguments;
    this.maxArguments = maxArguments;
    this.playerOnly = playerOnly;

    CommandMap commandMap = getCommandMap();
    if (commandMap != null) {
      commandMap.register(command, this);
    }
  }

  public CommandMap getCommandMap() {
    try {
      if (Bukkit.getPluginManager() instanceof SimplePluginManager) {
        Field field = SimplePluginManager.class.getDeclaredField("commandMap");
        field.setAccessible(true);

        return (CommandMap) field.get(Bukkit.getPluginManager());
      }
    } catch (NoSuchFieldException | IllegalAccessException e) {
      e.printStackTrace();
    }

    return null;
  }

  public CommandBase enableDelay(int delay) {
    this.delay = delay;
    this.delayedPlayers = new ArrayList<>();
    return this;
  }

  public void removeDelay(Player player) {
    this.delayedPlayers.remove(player.getName());
  }

  public void sendUsage(CommandSender sender) {
    Msg.send(sender, getUsage());
  }

  public boolean execute(CommandSender sender, String alias, String[] args) {
    if (args.length < minArguments || args.length > maxArguments && maxArguments != -1) {
      Msg.send(
          sender,
          "&cYou have to provide "
              + minArguments
              + " to "
              + maxArguments
              + " parameters, when you provided "
              + args.length);
      sendUsage(sender);
      return true;
    }
    if (playerOnly && !(sender instanceof Player)) {
      Msg.send(sender, "&cOnly players can use this command");
      return true;
    }

    String permission = getPermission();

    if (permission != null && !sender.hasPermission(permission)) {
      Msg.send(sender, "&cYou don't have access to this command");
      return true;
    }

    if (delayedPlayers != null && sender instanceof Player) {
      Player player = (Player) sender;
      if (delayedPlayers.contains(player.getName())) {
        Msg.send(player, "&cPlease wait before using this command again");
        return true;
      }

      delayedPlayers.add(player.getName());
      try {
        int taskId =
            Bukkit.getScheduler()
                .scheduleSyncDelayedTask(
                    Main.getInstance(),
                    () -> {
                      delayedPlayers.remove(player.getName());
                    },
                    20L * delay);

        if (taskId == -1) {
          Bukkit.getLogger().log(Level.WARNING, "");
        }

      } catch (Exception e) {
        Msg.send(player, e.toString());
      }
    }

    if (!onCommand(sender, args)) {
      sendUsage(sender);
    }

    return true;
  }

  public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
    return this.onCommand(sender, args);
  }

  public abstract boolean onCommand(CommandSender sender, String[] args);

  public abstract String getUsage();
}
