package de.slpnetwork.lobby;

import de.slpnetwork.lobby.Commands.slpl;
import de.slpnetwork.lobby.Events.PlayerInteract;
import de.slpnetwork.lobby.Manager.InventoryManager;
import de.slpnetwork.lobby.Manager.LobbyManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.io.File;
import java.io.InputStreamReader;
// TODO: Impplement Pluginmetrics
public class Lobby extends JavaPlugin implements Listener, PluginMessageListener {
    public LobbyManager lobbyManager;
    public FileConfiguration config;
    public File itemData;
    public FileConfiguration itemDataConfig;
    public File menuData;
    public FileConfiguration menuDataConfig;
    public File teleportData;
    public FileConfiguration teleportDataConfig;
    public File jumpData;
    public FileConfiguration jumpDataConfig;

    @Override
    public void onEnable(){
        super.onEnable();
        this.getServer().getLogger().info("Initializing Configs");
        try {
            this.saveDefaultConfig();
            config = this.getConfig();

            itemData = new File(this.getDataFolder(), "items.yml");
            menuData = new File(this.getDataFolder(), "menus.yml");
            teleportData = new File(this.getDataFolder(), "locations.yml");
            jumpData = new File(this.getDataFolder(), "jumps.yml");

            if(!itemData.exists() || !menuData.exists() || !teleportData.exists()) {
                System.out.println("Loading config files");
                itemData = new File(this.getDataFolder(), "items.yml");
                menuData = new File(this.getDataFolder(), "menus.yml");
                teleportData = new File(this.getDataFolder(), "locations.yml");
                jumpData = new File(this.getDataFolder(), "jumps.yml");

                itemDataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("items.yml")));
                menuDataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("menus.yml")));
                teleportDataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("locations.yml")));
                jumpDataConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(this.getResource("jumps.yml")));

                itemDataConfig.save(itemData);
                menuDataConfig.save(menuData);
                teleportDataConfig.save(teleportData);
                jumpDataConfig.save(jumpData);
            }

            itemDataConfig = YamlConfiguration.loadConfiguration(itemData);
            menuDataConfig = YamlConfiguration.loadConfiguration(menuData);
            teleportDataConfig = YamlConfiguration.loadConfiguration(teleportData);
            jumpDataConfig = YamlConfiguration.loadConfiguration(jumpData);
        }catch (Exception ex) {
            this.getServer().getLogger().warning("Could not load or create files due to exception: " + ex.getLocalizedMessage());
        }

        this.lobbyManager = new LobbyManager(this);

        this.getCommand("slpl").setExecutor(new slpl(this.lobbyManager));

        this.getServer().getPluginManager().registerEvents(this, this);
        this.getServer().getPluginManager().registerEvents(new PlayerInteract(this.lobbyManager), this);
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

        // Fetch and set configured items
        for (String key: this.itemDataConfig.getConfigurationSection("items").getKeys(false)) {
            System.out.println("DEBUG -> " + "Material: " + key);
            ItemStack is = new ItemStack(Material.getMaterial(this.itemDataConfig.getString("items." + key + ".material")));
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(this.itemDataConfig.getString("items." + key + ".display"));
            im.addItemFlags();
            is.setItemMeta(im);
            e.getPlayer().getInventory().setItem(this.itemDataConfig.getInt("items." + key + ".slot"), is);
        }
    }

    @EventHandler
    public void onPlayerUse(PlayerInteractEvent e) {
        // Error: somewhere on the way of triggering
        // The menu does not get opened
        try {
            Player p = e.getPlayer();
            this.lobbyManager.getInventoryManager().openInventory(p, this.itemDataConfig.getString("items." + e.getMaterial() + ".menu"));
        } catch (Exception ex) {
            // possibly nullpointer nothing else can happen here
        }
    }
    
    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] bytes) {
        if (!channel.equals("BungeeCord")) { return; }
    }
}
