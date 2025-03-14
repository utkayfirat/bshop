package com.rp.bshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerHarvestBlockEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerActionsListener implements Listener {

    private Bshop plugin;

    public PlayerActionsListener(Bshop bankShop) {
        this.plugin = bankShop;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        BankBoard.setupScoreboard(player, plugin);
    }

    @EventHandler
    public void onMobKill(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player) return;
        Player player = event.getEntity().getKiller();
        if (player == null) return;
        //Bukkit.getLogger().info("Mob killed by: " + player.getName());
        FileConfiguration config = plugin.getConfig();
        double balance = config.getDouble("players." + player.getUniqueId() + ".balance", 0);
        config.set("players." + player.getUniqueId() + ".balance", balance + 10);
        plugin.saveConfig();
        BankBoard.updateBalanceScore(player, plugin);
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();
        if(player == null) return;
        //Bukkit.getLogger().info("onBlockBreak: " + player.getName());
        FileConfiguration config = plugin.getConfig();
        double balance = config.getDouble("players." + player.getUniqueId() + ".balance", 0);
        config.set("players." + player.getUniqueId() + ".balance", balance + 5);
        plugin.saveConfig();
        BankBoard.updateBalanceScore(player, plugin);
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();
        FileConfiguration config = plugin.getConfig();

        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;

        if (event.getView().getTitle().equals(ChatColor.DARK_BLUE + "Teyipv44 Shop")) {
            event.setCancelled(true);

            if(player.getInventory().firstEmpty() == -1){
                player.sendMessage(ChatColor.RED + "Your inventory is full!");
                return;
            }

            if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().hasDisplayName()) {
                String itemName = clickedItem.getItemMeta().getDisplayName();
                double price = FindPrice(itemName);
                int ramount = FindRamount(itemName);

                double balance = config.getDouble("players."+player.getUniqueId()+".balance",0);

                if (balance >= price) {

                    config.set("players." + player.getUniqueId() + ".balance", balance - price);

                    ItemStack itemToGive = clickedItem.clone();
                    itemToGive.setAmount(ramount);
                    if (itemToGive.hasItemMeta()) {
                        ItemMeta itemMeta = itemToGive.getItemMeta();
                        if (itemMeta.hasLore()) {
                            itemMeta.setLore(new ArrayList<>());
                            itemToGive.setItemMeta(itemMeta);
                        }
                    }

                    player.getInventory().addItem(itemToGive);
                    player.sendMessage(ChatColor.GREEN +" "+ramount+" "+itemName + " Purchased");
                    BankBoard.updateBalanceScore(player, plugin);
                } else {
                    player.sendMessage(ChatColor.RED + "Not enough money.");
                }
            }
        }
    }

    private double FindPrice(String itemName){
        double price = 0;

        List<Map<?, ?>> items = plugin.getConfig().getMapList("shop-items");
        for (Map<?, ?> itemData : items) {
            if(itemName.equals(itemData.get("name"))){
                price = ((Number) itemData.get("price")).doubleValue();
            }
        }

        return price;
    }

    private int FindRamount(String itemName){
        int ramount = 0;

        List<Map<?, ?>> items = plugin.getConfig().getMapList("shop-items");
        for (Map<?, ?> itemData : items) {
            if(itemName.equals(itemData.get("name"))){
                ramount = (int) itemData.get("ramount");
            }
        }

        return ramount;
    }


}
