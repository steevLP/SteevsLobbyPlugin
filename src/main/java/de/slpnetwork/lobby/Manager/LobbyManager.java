package de.slpnetwork.lobby.Manager;

import de.slpnetwork.lobby.Lobby;
import de.slpnetwork.lobby.Utils.CommandInterpreter;

public class LobbyManager {
    private Lobby lobby;
    private InventoryManager inventoryManager;
    private CommandInterpreter commandInterpreter;
    private PlayerManager playerManager;

    public LobbyManager(Lobby lobby) {
        this.lobby = lobby;
        this.commandInterpreter = new CommandInterpreter(this);
        this.playerManager = new PlayerManager(this);
    }

    public Lobby getLobby() {
        return lobby;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public CommandInterpreter getCommandInterpreter() {
        return commandInterpreter;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
    }

    public void setInventoryManager(InventoryManager inventoryManager) {
        this.inventoryManager = inventoryManager;
    }
}
