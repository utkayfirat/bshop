package com.rp.bshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public class ShopCommand implements CommandExecutor {

    private Bshop plugin;

    public ShopCommand(Bshop bshop) {
        this.plugin = bshop;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED+"Only Players Can Use This Command.");
            return false;
        }

        Player player = (Player) commandSender;
        Inventory shopMenu = Bukkit.createInventory(null, 54, ChatColor.DARK_BLUE + "Teyipv44 Shop");

        List<Map<?, ?>> items = plugin.getConfig().getMapList("shop-items");

        for (Map<?, ?> itemData : items) {
            String id = (String) itemData.get("id");
            String name = (String) itemData.get("name");
            double price = ((Number) itemData.get("price")).doubleValue();
            int amount = (int) itemData.get("amount");

            ItemStack itemStack = null;
            try {
                itemStack = new ItemStack(Material.valueOf(id), amount);
            } catch (IllegalArgumentException e) {
                Bukkit.getLogger().info("Invalid item: " + id);
            }

            if(itemStack == null) continue;

            ItemMeta meta = itemStack.getItemMeta();

            if (meta != null) {
                meta.setDisplayName(name);
                meta.setLore(List.of(ChatColor.GREEN + "Price: " + price));
                itemStack.setItemMeta(meta);
            }

            shopMenu.addItem(itemStack);
        }

        player.openInventory(shopMenu);

        return true;
    }
}












