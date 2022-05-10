package de.slpnetwork.lobby.Manager;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class PlayerManager {
    private LobbyManager lobbyManager;

    public PlayerManager(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    public void giveItems(Player player){
        player.getInventory().addItem(new ItemStack(Material.BOOK));
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
        player.getInventory().addItem(new ItemStack(Material.PLAYER_HEAD));
    }

    /**
     * teleports a player to a location
     * @param player the wanted player
     * @param loc the wanted location
     */
    public void teleportPlayer(Player player, Location loc) {
        player.teleport(loc);
    }

    /**
     * Moves a player from a server to another
     * @param dest the destination
     * @param target the player to send
     */
    public void moveFromServer(String dest, Player target) {
        ByteArrayDataOutput aOut = ByteStreams.newDataOutput();
        aOut.writeUTF("Connect");
        aOut.writeUTF(dest);
        target.sendPluginMessage(this.lobbyManager.getLobby(), "BungeeCord", aOut.toByteArray());
    }
}
