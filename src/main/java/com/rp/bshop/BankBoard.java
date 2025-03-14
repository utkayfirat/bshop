package com.rp.bshop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.*;

public class BankBoard {

    public static void setupScoreboard(Player player, Bshop plugin) {
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        if (manager == null) return;
        Scoreboard board = manager.getNewScoreboard();

        Objective objective = board.registerNewObjective("balance", "dummy", ChatColor.GREEN + "Bank Account");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);


        double balance = plugin.getConfig().getDouble("players." + player.getUniqueId() + ".balance", 0);
        Score balanceScore = objective.getScore(ChatColor.DARK_GREEN + "Wallet:");
        balanceScore.setScore((int) balance);

        player.setScoreboard(board);
    }

    public static void updateBalanceScore(Player player, Bshop plugin) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("balance");
        if (objective != null) {
            Score balanceScore = objective.getScore(ChatColor.DARK_GREEN + "Wallet:");
            double balance = plugin.getConfig().getDouble("players." + player.getUniqueId() + ".balance", 0);
            balanceScore.setScore((int) balance);
        }
    }



}
