package de.slpnetwork.lobby.Manager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.HashMap;

public class InventoryManager implements Listener {
    private final LobbyManager lobbyManager;
    private HashMap<String, Inventory> inventories;

    public InventoryManager(LobbyManager lobbyManager){
        this.lobbyManager = lobbyManager;
        this.lobbyManager.getLobby().getServer().getLogger().info("Initializing Lobby");
        this.lobbyManager.setInventoryManager(this);
        this.inventories = new HashMap<String, Inventory>();

        for(String key: this.lobbyManager.getLobby().menuDataConfig.getConfigurationSection("menus").getKeys(false)) {
            this.inventories.put(
                    this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".identifier"), Bukkit.createInventory(null,
                            this.lobbyManager.getLobby().menuDataConfig.getInt("menus." + key + ".slots"),
                            this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".title")
                    )
            );
            System.out.println(this.inventories.size() + " inventories stored"); // debugging inventories not beeing generated
            initializeItems(key);
        }
    }

    void initializeItems(String key){
        Inventory inv = inventories.get(this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".identifier"));

        // Nullpointer debugging
        System.out.println(key + "0");
        if(this.lobbyManager.getLobby().menuDataConfig.get("menus." + key) == null) System.out.println("Error: no items have been set, Menu will not be created"); // fixes nullpointer
        if(this.lobbyManager.getLobby().menuDataConfig.get("menus." + key) == null) return; // fixes nullpointer

        for(String keyItem : this.lobbyManager.getLobby().menuDataConfig.getConfigurationSection("menus." + key + ".items").getKeys(false)) {
            System.out.println("debug-> " + keyItem);
            System.out.println(this.lobbyManager.getLobby().menuDataConfig.getString("menus."+key+".items."+keyItem+".material") + "1");

            inv.setItem(this.lobbyManager.getLobby().menuDataConfig.getInt("menus." + key + ".items." + keyItem + ".slot"),
                    createGuiItem(Material.getMaterial(this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".items." + keyItem + ".material")),
                            this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".items." + keyItem + ".title"),
                            this.lobbyManager.getLobby().menuDataConfig.getString("menus." + key + ".items." + keyItem + ".description")
                    )
            );
        }
    }

    protected ItemStack createGuiItem(final Material material, final String name, final String... lore) {
        final ItemStack item = new ItemStack(material, 1);
        final ItemMeta meta = item.getItemMeta();

        // Set the name of the item
        meta.setDisplayName(name);

        // Set the lore of the item
        meta.setLore(Arrays.asList(lore));

        item.setItemMeta(meta);

        return item;
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent, String Inventory) {
        System.out.println(Inventory);
        System.out.println(inventories.containsKey(Inventory));
        if(inventories.containsKey(Inventory)) {
            ent.openInventory(inventories.get(Inventory));
        }
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if(!inventories.containsValue(e.getInventory())) return;

        e.setCancelled(true);

        final ItemStack clickedItem = e.getCurrentItem();
        this.lobbyManager.getLobby().getServer().getLogger().info(clickedItem.toString());

        // verify current item is not null
        if (clickedItem == null || clickedItem.getType().isAir()) return;

        final Player p = (Player) e.getWhoClicked();
        this.lobbyManager.getCommandInterpreter().execute(p.getPlayer(),
                this.lobbyManager.getLobby().menuDataConfig.getString("menus." + e.getView().getTitle().toLowerCase() + ".items."+ clickedItem.getType() + ".action.type"),
                this.lobbyManager.getLobby().menuDataConfig.getString("menus." + e.getView().getTitle().toLowerCase() + ".items."+ clickedItem.getType() + ".action.argument"));
        // Using slots click is a best option for your inventory click's
        p.sendMessage("You clicked at slot " + e.getRawSlot());
    }

    // Cancel dragging in our inventory
    @EventHandler
    public void onInventoryClick(final InventoryDragEvent e) {
        if (inventories.containsValue(e.getInventory())) {
            e.setCancelled(true);
        }
    }
}
