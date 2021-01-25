package net.kunmc.lab.moneycraft.event;

import com.destroystokyo.paper.event.player.PlayerHandshakeEvent;
import net.kunmc.lab.moneycraft.MoneyCraft;
import net.kunmc.propromp.util.NBTUtil;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class PlayerEvent implements Listener {
    @EventHandler
    public void onRightClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
            if(e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1 && e.getPlayer().getInventory().getItemInMainHand().getType()==Material.WHEAT_SEEDS) {
                if (e.getPlayer().isSneaking()) {//Shift
                    e.getPlayer().sendMessage("-----[" + ChatColor.GOLD + "長者番付" + ChatColor.RESET + "]-----");
                    Map<String, Integer> tmpMap = new HashMap<>();
                    List<String> zeroList = new ArrayList<>();
                    for (OfflinePlayer player : Bukkit.getOfflinePlayers()) {
                        if (MoneyCraft.getEconomy().getBalance(player) == 0) {
                            zeroList.add(player.getName());
                        } else {
                            tmpMap.put(player.getName(), (int) MoneyCraft.getEconomy().getBalance(player));
                        }
                    }
                    List<Map.Entry<String, Integer>> list_entries = new ArrayList<>(tmpMap.entrySet());
                    Collections.sort(list_entries, Comparator.comparing(Map.Entry::getValue));
                    int i = 1;
                    for (Map.Entry<String, Integer> entry : list_entries) {
                        e.getPlayer().sendMessage(i + "位:" + entry.getKey() + "(" + entry.getValue() + "円)");
                        i++;
                    }
                    //所持金0人
                    TextComponent text = new TextComponent(ChatColor.UNDERLINE + "クリックでホームレス一覧を見る");
                    text.setClickEvent(new net.md_5.bungee.api.chat.ClickEvent(net.md_5.bungee.api.chat.ClickEvent.Action.RUN_COMMAND, "/mcr zerolist"));
                    e.getPlayer().spigot().sendMessage(text);


                    e.getPlayer().sendMessage("-----[" + ChatColor.GOLD + "長者番付" + ChatColor.RESET + "]-----");

                } else {//not shift
                    e.getPlayer().sendMessage("残高：" + MoneyCraft.getEconomy().getBalance(e.getPlayer()) + "円");
                }
            }
        }
    }
    @EventHandler
    public void onLeftClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1 && e.getPlayer().getInventory().getItemInMainHand().getType()==Material.WHEAT_SEEDS) {
                if (e.getPlayer().isSneaking()) {//Shift(千円投げる)
                    if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 1000) {
                        ItemStack item = new ItemStack(Material.GOLD_INGOT);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("MoneyCraft");
                        item.setItemMeta(meta);
                        Entity droppedItem = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
                        droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                        MoneyCraft.getEconomy().depositPlayer(e.getPlayer(),-1000);
                    }
                } else {//not Shift(百円投げる)
                    if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 100) {
                        ItemStack item = new ItemStack(Material.GOLD_NUGGET);
                        ItemMeta meta = item.getItemMeta();
                        meta.setDisplayName("MoneyCraft");
                        item.setItemMeta(meta);
                        Entity droppedItem = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
                        droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                        MoneyCraft.getEconomy().depositPlayer(e.getPlayer(),-100);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        int ingot_amount = ((int)(MoneyCraft.getEconomy().getBalance(e.getEntity())))/1000;
        int nugget_amount = ((int)(MoneyCraft.getEconomy().getBalance(e.getEntity())) - ingot_amount*1000)/100;
        MoneyCraft.getEconomy().depositPlayer(e.getEntity(),-MoneyCraft.getEconomy().getBalance(e.getEntity()));
        Location loc = e.getEntity().getLocation();

        ItemStack item = new ItemStack(Material.GOLD_NUGGET,nugget_amount);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("MoneyCraft");
        loc.getWorld().dropItem(loc,item);
        item = new ItemStack(Material.GOLD_INGOT,ingot_amount);
        meta = item.getItemMeta();
        meta.setDisplayName("MoneyCraft");
        loc.getWorld().dropItem(loc,item);
    }

    @EventHandler
    public void onInventoryIn(InventoryMoveItemEvent e){//TODO syuusei
        if(e.getDestination().getHolder() instanceof Player) {
            Player player = (Player) e.getDestination().getHolder();
            if (e.getItem().getItemMeta().getDisplayName().equals("MoneyCraft")) {
                if (e.getItem().getType() == Material.GOLD_INGOT) {
                    player.sendMessage(e.getItem().getAmount() * 1000 + "円を財布に入れました。");
                    MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getAmount() * 1000);
                    e.setCancelled(true);
                } else if (e.getItem().getType() == Material.GOLD_NUGGET) {
                    player.sendMessage(e.getItem().getAmount() * 100 + "円を財布に入れました。");
                    MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getAmount() * 100);
                    e.setCancelled(true);
                }
            }
        }
    }
}
