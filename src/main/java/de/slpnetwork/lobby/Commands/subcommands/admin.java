package de.slpnetwork.lobby.Commands.subcommands;

import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.Locale;

// TODO: Implement Permission Handling

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
        // Send help message (when done)
    }

    /**
     * sets a Jumplate and adds it to config 1: Name, 2: Sound, 3: Vertical Speed, 4: Horizontal Speed, 5: x-grid plates, 6: y-grid plates
     * @param args command arguments passed by executor
     */
    public void setJumpPlate(Player player, String[] args) {
        try {
            if(args[1] != null) {
                String  sound = "ENTITY_GENERIC_EXPLODE";
                int vertical = 1;
                int horizontal = 8;
                int grid_x = 1;
                int grid_y = 1;

                try {
                    if (args[3] != null) { sound = args[3]; }
                    if (args[4] != null) { vertical = Integer.parseInt(args[4]); }
                    if (args[5] != null) { horizontal = Integer.parseInt(args[5]); }
                    if (args[6] != null) { grid_x = Integer.parseInt(args[6]); }
                    if (args[7] != null) { grid_y = Integer.parseInt(args[7]); }
                } catch (Exception ex){

                }

                // Check for arguments and place plates
                if(grid_x > 1 && grid_y > 1) {
                    for(int x = Math.round((grid_x/2)*(-1)); x < Math.round((grid_x/2)); x++) {
                        for (int y = Math.round((grid_y/2)*(-1)); y < Math.round((grid_y/2)); y++) {
                            Location loc = new Location(
                                player.getWorld(),
                                player.getLocation().getBlockX() + x,
                                player.getLocation().getBlockY(),
                                player.getLocation().getBlockZ() + y
                            );
                            platePlacement(loc, args[1], horizontal, vertical);
                        }
                    }
                } else {
                    platePlacement(player.getLocation(), args[1], horizontal, vertical);
                }

                // save data to files
                this.lobbyManager.getLobby().jumpDataConfig.save(this.lobbyManager.getLobby().jumpData);
            }
        } catch (IOException exception) {
            // Just dont do anything
        }
    }

    /**
     * removes a specific plate
     * @param name the plates name
     */
    public void remJumpPlate(String name){
        try {
            // TODO: Get the Plates Position
            this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name, null);
            this.lobbyManager.getLobby().jumpDataConfig.save(this.lobbyManager.getLobby().jumpData);
        } catch (IOException exception) {

        }
    }

    /**
     * handles command input and stores its give location
     * @param loc inworld location
     * @param name locations reference name
     */
    public void setLoc(Location loc, String name){
        System.out.println("DEBUG -> SetLoc Input: " + name + ", " + loc);
        try {
            // set location in locations.yml
            this.lobbyManager.getLobby().teleportDataConfig.set("locations." + name + ".name", name);
            this.lobbyManager.getLobby().teleportDataConfig.set("locations." + name + ".world", loc.getWorld().getName());
            this.lobbyManager.getLobby().teleportDataConfig.set("locations." + name + ".X", loc.getBlockX());
            this.lobbyManager.getLobby().teleportDataConfig.set("locations." + name + ".Y", loc.getBlockY());
            this.lobbyManager.getLobby().teleportDataConfig.save(this.lobbyManager.getLobby().teleportData);
        } catch (IOException ex) {
            // this is bads
        }
    }

    /**
     * removes a given location if it exists
     * @param name removing location name
     */
    public void remLoc(String name){
        // set location in locations.yml
        // set location in locations.yml
        this.lobbyManager.getLobby().teleportDataConfig.set("locations." + name, null);
        try {
            this.lobbyManager.getLobby().teleportDataConfig.save(this.lobbyManager.getLobby().teleportData);
        } catch (IOException ex) {
            // this is bads
        }
    }

    // Prevents messy code
    private void platePlacement(Location loc, String name, int horizontalVel, int verticalVel){
        // Set Location
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".location.world", loc.getWorld().getName());
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".location.X", loc.getBlockX());
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".location.Y", loc.getBlockY());
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".location.Z", loc.getBlockZ());

        // Set Modifier
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".multiplier.vertical", verticalVel);
        this.lobbyManager.getLobby().jumpDataConfig.set("plates." + name + ".multiplier.horizontal", horizontalVel);

        Block plate = loc.getWorld().getBlockAt(loc);
        plate.setType(Material.getMaterial(this.lobbyManager.getLobby().config.getString("jumpPlates.plate")));
    }
}
