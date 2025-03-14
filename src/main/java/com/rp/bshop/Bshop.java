package com.rp.bshop;

import org.bukkit.plugin.java.JavaPlugin;

public final class Bshop extends JavaPlugin {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        getLogger().info("Bank Shop Plugin Enabled!");
        getCommand("bank").setExecutor(new BankCommand(this));
        getServer().getPluginManager().registerEvents(new PlayerActionsListener(this), this);

        getCommand("shop").setExecutor(new ShopCommand(this));
    }

    @Override
    public void onDisable() {
        getLogger().info("Bank Shop Plugin Disabled!");
    }
}
