package me.clip.noflyzone;

import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class NoFlyListener implements Listener {
    
    private final NoFlyZone plugin;
    
    public NoFlyListener(final NoFlyZone plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        
        if (NoFlyZone.regions == null || NoFlyZone.regions.isEmpty()) {
            return;
        }
        if (!(isSameXYZ(event.getFrom(), event.getTo()))) {
            return;
        }
        
        
        if (!event.getPlayer().isFlying()) {
            return;
        }
        
        if (plugin.getRegion(event.getTo()) == null) {
            return;
        }
        
        
        if (NoFlyZone.regions.contains(plugin.getRegion(event.getTo()))) {
            
            if (event.getPlayer().hasPermission("noflyzone.bypass")) {
                return;
            }
            
            event.getPlayer().setFlying(false);
            
            if (NoFlyZone.message != null && !NoFlyZone.message.isEmpty()) {
                ChatUtil.sendMessage(event.getPlayer(), NoFlyZone.message);
            }
            
        }
        
        
    }
    
    private boolean isSameXYZ(final @NotNull Location getFrom, final @NotNull Location getTo) {
        return getFrom.getBlockX() != getTo.getBlockX() ||
            getFrom.getBlockY() != getTo.getBlockY() ||
            getFrom.getBlockZ() != getTo.getBlockZ();
    }
    
    
}
