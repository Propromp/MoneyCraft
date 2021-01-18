package net.kunmc.propromp.moneycraft.command;

import net.kunmc.propromp.moneycraft.MoneyCraft;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

/**
 * 実際のコマンドの処理
 */
public class MoneyCommand {
    public static void create(Player player, Command command, String label, String[] args){
        MoneyCraft.getEconomy().createPlayerAccount(player);
    }
    public static void test(Player player, Command command, String label, String[] args){
        MoneyCraft.getEconomy().depositPlayer(player,10);
    }
    public static void get(Player player,Command command,String label,String[] args){
        player.sendMessage("残高:"+MoneyCraft.getEconomy().getBalance(player));
    }
}
