package net.kunmc.lab.moneycraft.command;

import net.kunmc.lab.moneycraft.MoneyCraft;
import net.kunmc.lab.moneycraft.effect.KeinEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * 実際のコマンドの処理
 */
public class MoneyCommand {
    public static void test(Player player, Command command, String label, String[] args) {
        if(player.hasPermission("moneycraft.*")) {
            MoneyCraft.getEconomy().depositPlayer(player, Integer.parseInt(args[1]));
            player.sendMessage("残高を"+args[1]+"追加しました");
        } else {
            player.sendMessage("権限がないよwwww");
        }
    }

    public static void get(Player player, Command command, String label, String[] args) {
        player.sendMessage("残高:" + MoneyCraft.getEconomy().getBalance(player));
    }

    public static void zerolist(Player player, Command command, String label, String[] args) {
        player.sendMessage("-----["+ ChatColor.DARK_PURPLE+"ホームレス一覧"+ChatColor.RESET+"]-----");
        for (OfflinePlayer p : Bukkit.getOfflinePlayers()) {
            if (MoneyCraft.getEconomy().getBalance(p) == 0) {
                player.sendMessage(p.getName());
            }
        }
        player.sendMessage("-----["+ ChatColor.DARK_PURPLE+"ホームレス一覧"+ChatColor.RESET+"]-----");
    }

    public static void kein(Player player, Command command, String label, String[] args) {
        if(player.hasPermission("moneycraft.kein")){
            KeinEffect.use = Integer.valueOf(args[1]);
            player.sendMessage("keineffectの値を"+args[1]+"に設定しました");
        } else {
            player.sendMessage("権限がないよwwww");
        }
    }

    public static void reset(Player player, Command command, String label, String[] args){
        MoneyCraft.getEconomy().depositPlayer(player,-MoneyCraft.getEconomy().getBalance(player));
        player.sendMessage("所持金をリセットしました。");
    }
}
