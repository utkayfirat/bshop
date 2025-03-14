package com.rp.bshop;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class BankCommand implements CommandExecutor {
    private Bshop plugin;

    public BankCommand(Bshop bankShop) {
        this.plugin = bankShop;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String @NotNull [] args) {

        if(!(commandSender instanceof Player)){
            commandSender.sendMessage(ChatColor.RED+"Only Players Can Use This Command.");
            return false;
        }

        Player player = (Player) commandSender;
        FileConfiguration config = plugin.getConfig();

        if(args.length == 0){
            String playerUUID = player.getUniqueId().toString();
            double balance = config.getDouble("players." + playerUUID + ".balance", 0);
            player.sendMessage(ChatColor.GREEN+"Your Balance: " + balance);
            return true;
        }

        if(args[0].equalsIgnoreCase("help")){
            player.sendMessage("§7Usage:");
            player.sendMessage("§6/bank §7- Check your balance");
            player.sendMessage("§6/bank send <player> <amount> §7- Send money to another player");
            return true;
        }

        if(args.length == 3 && args[0].equalsIgnoreCase("send")){ // /bank send <oyuncu> <miktar>
            String targetPlayerName = args[1];
            double amount = Double.parseDouble(args[2]);

            Player targetPlayer = plugin.getServer().getPlayer(targetPlayerName);
            if(targetPlayer == null){
                player.sendMessage(ChatColor.RED+"Player not found!");
                return false;
            }

            double senderBalance = config.getDouble("players."+player.getUniqueId()+".balance",0);
            if(amount > senderBalance){
                player.sendMessage(ChatColor.RED+"You don't have enough money!");
                return false;
            }

            config.set("players." + player.getUniqueId() + ".balance", senderBalance - amount);
            double targetBalance = config.getDouble("players." + targetPlayer.getUniqueId() + ".balance", 0);
            config.set("players." + targetPlayer.getUniqueId() + ".balance", targetBalance + amount);
            player.sendMessage(ChatColor.DARK_GREEN + "You sent " + amount + " to " + targetPlayer.getName());
            targetPlayer.sendMessage(ChatColor.GREEN + "You received " + amount + " from " + player.getName());
            plugin.saveConfig();

            return true;
        }


        return false;
    }
}
