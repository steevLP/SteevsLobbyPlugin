package de.slpnetwork.lobby;

import de.slpnetwork.lobby.Manager.InventoryManager;
import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;

public class Lobby extends JavaPlugin implements Listener, PluginMessageListener {
    public LobbyManager lobbyManager;
    public FileConfiguration config;
    public File itemData;
    public FileConfiguration itemDataConfig;
    public File menuData;
    public FileConfiguration menuDataConfig;


    @Override
    public void onEnable(){
        super.onEnable();
        this.getServer().getLogger().info("Initializing Configs");
        try {
            this.saveDefaultConfig();
            config = this.getConfig();
            itemData = new File(this.getDataFolder(), "items.yml");
            itemDataConfig = YamlConfiguration.loadConfiguration(itemData);
            menuData = new File(this.getDataFolder(), "menu.yml");
            menuDataConfig = YamlConfiguration.loadConfiguration(menuData);
        }catch (Exception ex) {

        }

        this.lobbyManager = new LobbyManager(this);
        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new InventoryManager(this.lobbyManager), this);
        this.getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        this.getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", this);
    }

    @Override
    public void onDisable(){
        super.onDisable();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.getPlayer().getInventory().clear();

        // TODO: loop over itemdataconfig(items)
        for (String key: this.config.getConfigurationSection("menu").getKeys(false)) {
            // TODO: replace current datapoints with new ones
            ItemStack is = new ItemStack(Material.getMaterial(this.config.getString("menu." + key + ".material")));
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(this.config.getString("menu." + key + ".title"));
            im.addItemFlags();
            is.setItemMeta(im);
            e.getPlayer().getInventory().setItem(this.config.getInt("menu." + key + ".slot"), is);
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        // Error: somewhere on the way of triggering
        // The menu does not get opened
        try {
            Player p = e.getPlayer();
            // TODO: Replace current datapoints with new ones
            // TODO: Grab menu Datapoint based on Material Type
            this.lobbyManager.getInventoryManager().openInventory(p, p.getInventory().getItemInMainHand().getItemMeta().getDisplayName());
        } catch (Exception ex) {
            // possibly nullpointer nothing else can happen here
        }
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals("BungeeCord")) { return; }
    }
}
