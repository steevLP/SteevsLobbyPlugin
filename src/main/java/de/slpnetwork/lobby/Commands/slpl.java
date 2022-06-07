package de.slpnetwork.lobby.Commands;

import de.slpnetwork.lobby.Commands.subcommands.admin;
import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Locale;

// TODO: Implement Permission handling

public class slpl implements CommandExecutor {
    private LobbyManager lobbyManager;
    private admin cmdAdmin;

    public slpl(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
        cmdAdmin = new admin(this.lobbyManager);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = sender.getServer().getPlayer(sender.getName());

        System.out.println("Debug -> Command Output");
        System.out.println(args[0]);
        System.out.println(args[1]);
        System.out.println(args[2]);

        if(args.length >= 0) {
            switch (args[0].toLowerCase()) {
                case "admin":
                    switch (args[1].toLowerCase()) {
                        case "help" -> cmdAdmin.sendHelp(player);
                        case "setloc" -> cmdAdmin.setLoc(player.getLocation(), args[2].toLowerCase());
                        case "remloc" -> cmdAdmin.remLoc(args[2]);
                        case "setjump" -> cmdAdmin.setJumpPlate(player, args);
                        case "remjump" -> cmdAdmin.remJumpPlate(args[2].toLowerCase());
                    }
                    break;
            }
        }
        return false;
    }
}
