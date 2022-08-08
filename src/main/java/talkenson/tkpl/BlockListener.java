package talkenson.tkpl;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

public class BlockListener implements Listener {
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        var mat = e.getBlock().getType();
        if (mat == Material.TORCH) {
            e.getPlayer().sendMessage("Wow, torch!");
        }
    }
}
