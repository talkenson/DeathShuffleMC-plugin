package talkenson.tkpl;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import talkenson.tkpl.commands.Setup;
import talkenson.tkpl.lib.DeathTypes;

public final class Main extends JavaPlugin {
  private static Main instance;
  DeathTypes dt = null;
  Setup setupInstance = null;

  @Override
  public void onLoad() {
    Bukkit.getLogger().info("DeathShuffle loading...");
    this.dt = new DeathTypes();
  }

  @Override
  public void onEnable() {
    instance = this;
    Bukkit.getLogger().info("DeathShuffle starting...");
    this.setupInstance = new Setup(dt);
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
