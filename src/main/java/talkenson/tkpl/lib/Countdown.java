package talkenson.tkpl.lib;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitScheduler;
import talkenson.tkpl.Main;
import talkenson.tkpl.Msg;

public class Countdown {
  int timer = -1;
  int startValue = 0;

  public Countdown() {}

  public void runNewTimer(int countdownStart) {
    this.startValue = countdownStart;
    BukkitScheduler scheduler = Bukkit.getScheduler();
    this.timer =
        scheduler.scheduleSyncRepeatingTask(
            Main.getInstance(),
            new Runnable() {
              @Override
              public void run() {
                if (startValue <= 0) {
                  scheduler.cancelTask(timer);
                  return;
                }
                if (startValue % 60 == 0) {
                  Msg.broadcast((int) (startValue / 60) + " minutes left to complete a challenge.");
                  startValue--;
                  return;
                }
                if (startValue <= 15) {
                  Msg.broadcast(startValue + " seconds left to complete.", "&c");
                  startValue--;
                  return;
                }
                startValue--;
              }
            },
            0L,
            20L);
  }

  public void stopTimer() {
    BukkitScheduler scheduler = Bukkit.getScheduler();
    scheduler.cancelTask(timer);
    this.timer = -1;
  }
}
