package de.slpnetwork.lobby.Commands.subcommands;

import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class admin {
    private LobbyManager lobbyManager;

    public admin(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    /**
     * sends administration help to player
     * @param player player reference
     */
    public static void sendHelp(Player player) {
    }

    /**
     * handles command input and stores its give location
     * @param loc inworld location
     * @param name locations reference name
     */
    public void setLoc(Location loc, String name){
        // set location in locations.yml
    }

    /**
     * removes a given location if it exists
     * @param name removing location name
     */
    public void remLoc(String name){
        // set location in locations.yml
    }
}
