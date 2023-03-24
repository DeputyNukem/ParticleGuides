package me.monst.particleguides.command.guide;

import me.monst.particleguides.ParticleGuidesPlugin;
import me.monst.particleguides.command.CommandExecutionException;
import me.monst.particleguides.command.Permission;
import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.command.PlayerExecutable;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;

class GuideDeath implements PlayerExecutable {
    
    private final ParticleGuidesPlugin plugin;
    
    GuideDeath(ParticleGuidesPlugin plugin) {
        this.plugin = plugin;
    }
    
    @Override
    public String getName() {
        return "death";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to the location where you last died.";
    }
    
    @Override
    public String getUsage() {
        return "/guide death [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_DEATH;
    }
    
    @Override
    public void execute(Player player, List<String> args) throws CommandExecutionException {
        Location lastDeath = player.getLastDeathLocation();
        if (lastDeath == null || lastDeath.getWorld() == null)
            throw new CommandExecutionException("No death location found.");
        if (!Objects.equals(lastDeath.getWorld(), player.getWorld()))
            throw new CommandExecutionException("Death location is in world '" + lastDeath.getWorld().getName() + "'.");
    
        player.sendMessage(ChatColor.YELLOW + "Guiding you to your last death...");
        Color color = plugin.config().colorOptions.findColorOrRandom(args.isEmpty() ? null : args.get(0));
        plugin.getParticleService().addGuide(player, lastDeath, color);
    }
    
    @Override
    public List<String> getTabCompletions(Player player, List<String> args) {
        return plugin.config().colorOptions.searchColors(args.get(0));
    }
    
}
