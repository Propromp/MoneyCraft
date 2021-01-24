package net.kunmc.lab.moneycraft.command;

import net.kunmc.lab.moneycraft.MoneyCraft;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * 実際のコマンドの処理
 */
public class MoneyCommand {
    public static void create(Player player, Command command, String label, String[] args) {
        MoneyCraft.getEconomy().createPlayerAccount(player);
    }

    public static void test(Player player, Command command, String label, String[] args) {
        MoneyCraft.getEconomy().depositPlayer(player, 1000);
    }

    public static void get(Player player, Command command, String label, String[] args) {
        player.sendMessage("残高:" + MoneyCraft.getEconomy().getBalance(player));
    }

    public static void zerolist(Player player, Command command, String label, String[] args) {
        player.sendMessage("-----["+ ChatColor.DARK_PURPLE+"ホームレス一覧"+ChatColor.RESET+"]-----");
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (MoneyCraft.getEconomy().getBalance(p) == 0) {
                player.sendMessage(p.getName() + ",");
            }
        }
        player.sendMessage("-----["+ ChatColor.DARK_PURPLE+"ホームレス一覧"+ChatColor.RESET+"]-----");
    }
}
