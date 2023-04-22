package me.monst.particleguides.command.guide;

import me.monst.particleguides.command.Permissions;
import me.monst.particleguides.configuration.values.Colors;
import me.monst.particleguides.particle.NamedColor;
import me.monst.particleguides.particle.ParticleService;
import me.monst.pluginutil.command.Arguments;
import me.monst.pluginutil.command.Command;
import me.monst.pluginutil.command.Permission;
import me.monst.pluginutil.command.exception.CommandExecutionException;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class GuideCoords implements Command {
    
    private final ParticleService particleService;
    private final Colors colors;
    
    GuideCoords(ParticleService particleService, Colors colors) {
        this.particleService = particleService;
        this.colors = colors;
    }
    
    @Override
    public String getName() {
        return "coords";
    }
    
    @Override
    public String getDescription() {
        return "Guides you to the specified coordinates.";
    }
    
    @Override
    public String getUsage() {
        return "/guide coords <x> <y> <z> [color]";
    }
    
    @Override
    public Permission getPermission() {
        return Permissions.GUIDE_COORDS;
    }
    
    @Override
    public void execute(CommandSender sender, Arguments args) throws CommandExecutionException {
        Player player = Command.playerOnly(sender);
        Block playerBlock = player.getLocation().getBlock();
        int x = args.first()
                .map(arg -> parseCoordinate(arg, playerBlock.getX()))
                .expect("Please specify the x, y, and z coordinates to locate.");
        int y = args.second()
                .map(arg -> parseCoordinate(arg, playerBlock.getY()))
                .expect("Please specify the y and z coordinates to locate.");
        int z = args.third()
                .map(arg -> parseCoordinate(arg, playerBlock.getZ()))
                .expect("Please specify the z coordinate to locate.");
        Location coordinates = new Location(player.getWorld(), x + 0.5, y + 0.5, z + 0.5);
        NamedColor color = args.fourth().map(colors::get).orElseGet(colors::random);
        player.sendMessage(ChatColor.YELLOW + "Guiding you to coordinates " + x + ", " + y + ", " + z + " in " + color.getName() + "...");
        particleService.addGuide(player, coordinates, color.getColor());
    }
    
    private int parseCoordinate(String arg, int playerCoordinate) throws CommandExecutionException {
        try {
            if (!arg.startsWith("~")) // Not a relative coordinate
                return Integer.parseInt(arg);
            if (arg.length() == 1) // Relative coordinate with no offset
                return playerCoordinate;
            return playerCoordinate + Integer.parseInt(arg.substring(1));
        } catch (NumberFormatException e) {
            throw Command.fail(arg + " is not a valid coordinate.");
        }
    }
    
    @Override
    public Iterable<?> getTabCompletions(Player player, Arguments args) {
        if (args.size() <= 3) {
            List<String> completions = new ArrayList<>();
            args.first().ifBlank(() -> completions.add("~ ~ ~"));
            args.second().ifBlank(() -> completions.add("~ ~"));
            args.third().ifBlank(() -> completions.add("~"));
            return completions;
        }
        if (args.size() == 4)
            return args.fourth().map(colors::search).orElseGet(colors::names);
        return Collections.emptyList();
    }
    
}
