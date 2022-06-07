package de.slpnetwork.lobby.Events;

import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerInteract implements Listener {
    private LobbyManager lobbyManager;

    public PlayerInteract(LobbyManager lobbyManager) {
        this.lobbyManager = lobbyManager;
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        if(event.getAction().equals(Action.PHYSICAL)) {
            Player player = event.getPlayer();
            Block block = event.getClickedBlock();
            if(event.getClickedBlock().getType() == Material.getMaterial(this.lobbyManager.getLobby().config.getString("jumpPlates.plate"))) {

                // TODO: Test Jumpplate Code
                for(String key: this.lobbyManager.getLobby().jumpDataConfig.getConfigurationSection("plates").getKeys(false)) {
                    // Construct Launcher
                    System.out.println("DEBUG -> plate-key: " + key);
                    System.out.println("DEBUG -> loc world: " + this.lobbyManager.getLobby().jumpDataConfig.getString("plates." + key + ".location.world"));
                    System.out.println("DEBUG -> loc x: " + this.lobbyManager.getLobby().jumpDataConfig.getDouble("plates." + key + ".location.X"));
                    System.out.println("DEBUG -> loc y: " + this.lobbyManager.getLobby().jumpDataConfig.getDouble("plates." + key + ".location.Y"));
                    System.out.println("DEBUG -> loc z: " + this.lobbyManager.getLobby().jumpDataConfig.getDouble("plates." + key + ".location.Z"));
                    System.out.println("DEBUG -----------------------------------");
                    System.out.println("DEBUG -> loc world: " + block.getLocation().getWorld().getName() == this.lobbyManager.getLobby().jumpDataConfig.getString("plates." + key + ".location.world"));
                    System.out.println("DEBUG -> loc x: " + ((int)block.getLocation().getBlockX() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.X")));
                    System.out.println("DEBUG -> loc y: " + ((int)block.getLocation().getBlockY() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.Y")));
                    System.out.println("DEBUG -> loc z: " + ((int)block.getLocation().getBlockZ() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.Z")));

                    if((int)block.getLocation().getBlockX() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.X") &&
                        (int)block.getLocation().getBlockY() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.Y") &&
                        (int)block.getLocation().getBlockZ() == this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".location.Z")) {

                        // Launch Player in looking direction
                        System.out.println("DEBUG -> Launching Player");
                        player.setVelocity(new Vector(
                            player.getLocation().getDirection().multiply(this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".multiplier.horizontal")).getX(),
                            this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".multiplier.vertical"),
                            player.getLocation().getDirection().multiply(this.lobbyManager.getLobby().jumpDataConfig.getInt("plates." + key + ".multiplier.horizontal")).getZ()));

                        // Player Sound
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 5.0f, 1.0f);
                    }
                }
            }
        }
    }
}
