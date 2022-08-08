package talkenson.tkpl.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import talkenson.tkpl.CommandBase;
import talkenson.tkpl.Main;
import talkenson.tkpl.Msg;
import talkenson.tkpl.lib.Countdown;
import talkenson.tkpl.lib.DeathTypes;
import talkenson.tkpl.lib.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Setup {
  HashMap<String, String> playerToDeathType = new HashMap<>();
  HashMap<String, Boolean> playerStatuses = new HashMap<>();
  List<String> deathTypes = null;
  List<Float> deathProbs = null;
  int roundTaskId = -1;
  boolean isGameActive = false;
  Countdown countdown = new Countdown();

  public Setup(DeathTypes dt) {
    deathTypes = dt.getDeathTypes();
    deathProbs = dt.getDeathProbabilities();
    new CommandBase("dsw", 1, true) {
      @Override
      public boolean onCommand(CommandSender sender, String[] args) {
        try {
          if (Objects.equals(args[0], "start")) {
            if (isGameActive) {
              Msg.send(sender, "To start a new game you need to stop the previous game");
              return true;
            }
            startNewRound();
            return true;
          } else if (Objects.equals(args[0], "stop")) {
            finishRound("stopCommand");
            return true;
          } else if (Objects.equals(args[0], "list")) {
            Msg.send(sender, deathTypes.toString());
            return true;
          } else {
            Msg.send(sender, "&4Unknown option passed");
            return true;
          }
        } catch (Exception exception) {
          Msg.send(sender, exception.toString());
          return true;
        }
      }

      @Override
      public String getUsage() {
        return "/dsw start | stop";
      }
    }.enableDelay(2);
  }

  private void startNewRound() {
    var playerCollection = Bukkit.getOnlinePlayers();
    Msg.broadcast("You have 5 minutes to:");
    for (Player player : playerCollection) {
      var target = Utils.getRandomFromArrayProbabilities(deathTypes, deathProbs);
      playerToDeathType.put(player.getName(), target);
      playerStatuses.put(player.getName(), false);
      Msg.send(player, "Die from a &c[" + target + "]");
    }

    isGameActive = true;
    countdown.runNewTimer(300);
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory true");

    roundTaskId =
        Bukkit.getScheduler()
            .scheduleSyncDelayedTask(
                Main.getInstance(),
                () -> {
                  finishRound("timeExceed");
                },
                20L * 300);
  }

  private void finishRound(String cause) {
    Bukkit.getScheduler().cancelTask(roundTaskId);
    countdown.stopTimer();
    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamerule keepInventory false");

    if (!Objects.equals(cause, "allDone")) {
      if (Objects.equals(cause, "stopCommand")) {
        Msg.broadcast("&cOh, game stopped, checking results...");
      } else if (Objects.equals(cause, "allDone")) {
        Msg.broadcast("&cUh oh, game stopped, checking results...");
      } else {
        Msg.broadcast("&cTime is up! Checking results...");
      }
      var bastards =
          playerStatuses.entrySet().stream()
              .filter(entry -> !entry.getValue())
              .map(Map.Entry::getKey)
              .toList();

      Msg.broadcast("These players could not die properly:");
      for (var bastard : bastards) {
        Msg.broadcast(bastard);
      }

    } else {
      Msg.broadcast("Wow, all the players got the challenge done!");
    }

    if (!Objects.equals(cause, "stopCommand")) {
      roundTaskId =
          Bukkit.getScheduler()
              .scheduleSyncDelayedTask(
                  Main.getInstance(),
                  () -> {
                    Msg.broadcast("New round starts in 5 seconds...");
                    roundTaskId =
                        Bukkit.getScheduler()
                            .scheduleSyncDelayedTask(
                                Main.getInstance(), this::startNewRound, 20L * 5);
                  },
                  20L * 3);
    }

    isGameActive = false;

    playerStatuses = new HashMap<>();
    playerToDeathType = new HashMap<>();
  }

  public HashMap<String, String> getPlayerToDeathType() {
    return playerToDeathType;
  }

  public boolean getIsGameActive() {
    return isGameActive;
  }

  public void acceptDeath(Player player, boolean leftGame) {
    playerStatuses.replace(player.getName(), true);
    if (!leftGame) {
      Msg.broadcast(player.getName() + " completed their challenge!");
    }
    if (!playerStatuses.containsValue(false)) {
      Bukkit.getScheduler().cancelTask(roundTaskId);
      finishRound("allDone");
    }
  }

  public void acceptDeath(Player player) {
    acceptDeath(player, false);
  }
}
