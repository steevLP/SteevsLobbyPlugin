package de.slpnetwork.lobby.Utils;

import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Objects;

public class CommandInterpreter {
    private LobbyManager lobbyManager;
    public CommandInterpreter(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    /**
     * Executes configured action
     * @param executor User who performed the Action
     * @param action the action to perform
     * @param args aditional arguments
     */
    public void execute(Player executor, String action, String args){
        switch (action.toLowerCase()) {
            default:
                this.lobbyManager.getLobby().getServer().getLogger().warning("The Action: '" + action + "' is not defined");
                break;
            case "menu":
                this.lobbyManager.getInventoryManager().openInventory(executor, args);
                break;
            case "teleport":
                this.lobbyManager.getLobby().teleportDataConfig.getLocation("locations." + args);
                Location destination = new Location(Bukkit.getServer().getWorld(this.lobbyManager.getLobby().teleportDataConfig.getString("locations." + args + ".world")),
                        this.lobbyManager.getLobby().teleportDataConfig.getDouble("locations." + args + ".X"),
                        this.lobbyManager.getLobby().teleportDataConfig.getDouble("locations." + args + ".Y"),
                        this.lobbyManager.getLobby().teleportDataConfig.getDouble("locations." + args + ".Z"));
                this.lobbyManager.getPlayerManager().teleportPlayer(executor, destination);
                break;
            case "connect":
                this.lobbyManager.getPlayerManager().moveFromServer(args, Objects.requireNonNull(executor.getPlayer()));
                break;
            case "execute":
                // Not yet implemented
                // Might be droped during development phase
                break;
        }
    }
}
