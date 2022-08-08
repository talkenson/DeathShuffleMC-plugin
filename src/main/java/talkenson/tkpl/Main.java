package talkenson.tkpl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import talkenson.tkpl.commands.Setup;
import talkenson.tkpl.lib.DeathTypes;

public final class Main extends JavaPlugin {
  private static Main instance;

  @Override
  public void onEnable() {
    instance = this;
    // Plugin startup logic
    Bukkit.getLogger().info("DeathShuffle loading...");
    DeathTypes dt = new DeathTypes();
    var setupInstance = new Setup(dt);
    Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(setupInstance, dt), this);
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
    Bukkit.getLogger().info("DeathShuffle exiting...");
  }

  public static Main getInstance() {
    return instance;
  }
}
