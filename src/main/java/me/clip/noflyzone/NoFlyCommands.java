package me.clip.noflyzone;

import java.util.ArrayList;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.annotation.*;
import com.github.sarhatabaot.kraken.core.chat.ChatUtil;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

@CommandAlias("noflyzone|nfz")
@CommandPermission("noflyzone.admin")
public class NoFlyCommands extends BaseCommand {
    
    private static final String SPACER = "&8&m+----------------+";
    
    private final NoFlyZone plugin;
    
    public NoFlyCommands(final NoFlyZone plugin) {
        this.plugin = plugin;
    }
    
    @Default
    @Subcommand("version")
    public void onDefault(final CommandSender sender) {
        ChatUtil.sendMessage(sender,
            SPACER,
            "&cNo&fFly&cZone &f&o" + plugin.getDescription().getVersion(),
            "&7Created by: &f&o" + plugin.getDescription().getAuthors(),
            SPACER
        );
    }
    
    @Subcommand("reload")
    public void onReload(final CommandSender sender) {
        plugin.reloadConfig();
        plugin.saveConfig();
        NoFlyZone.message = plugin.cfg.noFlyMessage();
        NoFlyZone.regions = plugin.cfg.noFlyRegions();
        ChatUtil.sendMessage(sender,
            SPACER,
            "&7Configuration successfully reloaded!",
            SPACER
        );
    }
    
    @Subcommand("list")
    public void onList(final CommandSender sender) {
        if (NoFlyZone.regions == null || NoFlyZone.regions.isEmpty()) {
            ChatUtil.sendMessage(sender,
                SPACER,
                "&cNo regions loaded!",
                SPACER
            );
            return;
        }
        
        ChatUtil.sendMessage(sender,
            SPACER,
            "&7No fly regions: &f" + NoFlyZone.regions.size(),
            NoFlyZone.regions.toString()
                .replace("[", "")
                .replace("]", "")
                .replace(",", "&c,&f"),
            SPACER
        );
    }
    
    @Subcommand("add")
    public void onAdd(final CommandSender sender, final String regionId) {
        if (NoFlyZone.regions == null) {
            NoFlyZone.regions = new ArrayList<>();
        }
        
        for (String region : NoFlyZone.regions) {
            if (region.equalsIgnoreCase(regionId)) {
                ChatUtil.sendMessage(sender, regionId + " &cis already a NoFlyZone!");
                return;
            }
        }
        
        NoFlyZone.regions.add(regionId);
        plugin.getConfig().set("no_fly_regions", NoFlyZone.regions);
        plugin.saveConfig();
        ChatUtil.sendMessage(sender,
            SPACER,
            regionId + " &ahas been marked as a NoFlyZone!",
            SPACER
        );
    }
    
    /**
     * Matches the region id even if the case is wrong.
     *
     * @param regionId User input
     * @return The matched regionId, or null if nothing was found.
     */
    @Contract(pure = true)
    private @Nullable String matchRegionId(final String regionId) {
        for (String region : NoFlyZone.regions) {
            if (region.equalsIgnoreCase(regionId)) {
                return region;
            }
        }
        return null;
    }
    
    @Subcommand("remove")
    public void onRemove(final CommandSender sender, String regionId) {
        
        if (NoFlyZone.regions == null || NoFlyZone.regions.isEmpty()) {
            ChatUtil.sendMessage(sender,
                SPACER,
                "&cNo regions loaded to remove!",
                SPACER
            );
            return;
        }
        
        final String matchedRegionId = matchRegionId(regionId);
        if (matchedRegionId == null) {
            ChatUtil.sendMessage(sender,
                SPACER,
                "&cNo region loaded by the name of &f" + regionId,
                SPACER
            );
            
            return;
        }
        
        NoFlyZone.regions.remove(matchedRegionId);
        plugin.getConfig().set("no_fly_regions", NoFlyZone.regions);
        plugin.saveConfig();
        ChatUtil.sendMessage(sender,
            SPACER,
            matchedRegionId + " &ais no longer a NoFlyZone",
            SPACER
        );
    }
    
    
}
