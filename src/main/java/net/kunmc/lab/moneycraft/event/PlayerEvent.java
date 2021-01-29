package net.kunmc.lab.moneycraft.event;

import net.kunmc.lab.moneycraft.MoneyCraft;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

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
                        if(entry.getKey().equals("kein_kandy")){
                            e.getPlayer().sendMessage(i + "位:" + entry.getKey() + "("+ChatColor.MAGIC+ "114514" +ChatColor.RESET+ "円)");//kein
                        }
                        i++;
                    }
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
                    if(e.getPlayer().getUniqueId().toString().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")){//kein
                        e.getPlayer().sendMessage("残高："+ChatColor.MAGIC+ "114514" +ChatColor.RESET+"円");
                    } else {
                        e.getPlayer().sendMessage("残高：" + MoneyCraft.getEconomy().getBalance(e.getPlayer()) + "円");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onLeftClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.LEFT_CLICK_AIR) || e.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            if(!(e.getPlayer().getInventory().getItemInMainHand().getType().equals(Material.AIR))&&e.getPlayer().getInventory().getItemInMainHand().getItemMeta().hasCustomModelData()) {
                if (e.getPlayer().getInventory().getItemInMainHand().getItemMeta().getCustomModelData() == 1 && e.getPlayer().getInventory().getItemInMainHand().getType() == Material.WHEAT_SEEDS) {
                    if (e.getPlayer().isSneaking()) {//Shift(千円投げる)
                        if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 1000) {
                            ItemStack item = new ItemStack(Material.GOLD_INGOT);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("MoneyCraft");
                            item.setItemMeta(meta);
                            Entity droppedItem = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
                            droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                            if (!e.getPlayer().getUniqueId().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")) {
                                MoneyCraft.getEconomy().depositPlayer(e.getPlayer(), -1000);
                            }
                        }
                    } else {//not Shift(百円投げる)
                        if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 100) {
                            ItemStack item = new ItemStack(Material.GOLD_NUGGET);
                            ItemMeta meta = item.getItemMeta();
                            meta.setDisplayName("MoneyCraft");
                            item.setItemMeta(meta);
                            Entity droppedItem = e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), item);
                            droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                            if (!e.getPlayer().getUniqueId().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")) {
                                MoneyCraft.getEconomy().depositPlayer(e.getPlayer(), -100);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Bukkit.broadcastMessage(ChatColor.DARK_RED+e.getEntity().getName()+"は死んだことにより"+MoneyCraft.getEconomy().getBalance(e.getEntity())+"円失った");
        if(e.getEntity().getUniqueId().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")){
            Bukkit.broadcastMessage(ChatColor.GOLD+"だがしかし、keinは金に目がないので復活するまで財布にしがみついた");
        } else {
            int ingot_amount = ((int) (MoneyCraft.getEconomy().getBalance(e.getEntity()))) / 1000;
            int nugget_amount = ((int) (MoneyCraft.getEconomy().getBalance(e.getEntity())) - ingot_amount * 1000) / 100;
            MoneyCraft.getEconomy().depositPlayer(e.getEntity(), -MoneyCraft.getEconomy().getBalance(e.getEntity()));
            Location loc = e.getEntity().getLocation();

            if (nugget_amount > 0) {
                ItemStack item = new ItemStack(Material.GOLD_NUGGET, nugget_amount);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("MoneyCraft");
                item.setItemMeta(meta);
                loc.getWorld().dropItem(loc, item);
            }
            if (ingot_amount > 0) {
                ItemStack item2 = new ItemStack(Material.GOLD_INGOT, ingot_amount);
                ItemMeta meta2 = item2.getItemMeta();
                meta2.setDisplayName("MoneyCraft");
                item2.setItemMeta(meta2);
                loc.getWorld().dropItem(loc, item2);
            }
        }
    }

    @EventHandler
    public void onInventoryIn(PlayerAttemptPickupItemEvent e){//TODO syuusei
        Player player = e.getPlayer();
        if (e.getItem().getItemStack().getItemMeta().getDisplayName().equals("MoneyCraft")) {
            if (e.getItem().getItemStack().getType() == Material.GOLD_INGOT) {
                player.sendMessage(e.getItem().getItemStack().getAmount() * 1000 + "円を財布に入れました。");
                MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getItemStack().getAmount() * 1000);
                e.setCancelled(true);
                player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1.2f);//1000円専用サウンド
            } else if (e.getItem().getItemStack().getType() == Material.GOLD_NUGGET) {
                player.sendMessage(e.getItem().getItemStack().getAmount() * 100 + "円を財布に入れました。");
                MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getItemStack().getAmount() * 100);
                e.setCancelled(true);
            }

            ((CraftItem)e.getItem()).getHandle().killEntity();
            player.playSound(player.getLocation(),Sound.ENTITY_ITEM_PICKUP,1f,1f);
            player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,0.8f);
            player.playSound(player.getLocation(),Sound.ENTITY_EXPERIENCE_ORB_PICKUP,1f,1f);
        }
    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            if(e.getDamager().getUniqueId().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")){
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_PLAYER_ATTACK_CRIT,1,1);
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_PICKUP,1,1);
            player.getWorld().spawnParticle(Particle.TOTEM,player.getLocation().add(0,1,0),100);
                for(int i = 0;i < 30;i++){
                    ItemStack item = new ItemStack(Material.GOLD_NUGGET, 1);
                    ItemMeta meta = item.getItemMeta();
                    meta.setDisplayName("MoneyCraft");
                    List<String> lore = new ArrayList<>();
                    lore.add(UUID.randomUUID().toString());
                    meta.setLore(lore);
                    item.setItemMeta(meta);
                    Entity droppedItem = player.getWorld().dropItem(player.getLocation().add(0,1,0), item);
                    droppedItem.setVelocity(new Vector(Math.random()*2.0-1.0,Math.random()*2.0-1.0,Math.random()*2.0-1.0).multiply(2));
                    player.setVelocity(e.getDamager().getLocation().getDirection().multiply(5));

                    ItemStack item2 = new ItemStack(Material.GOLD_INGOT, 1);
                    ItemMeta meta2 = item2.getItemMeta();
                    meta2.setDisplayName("MoneyCraft");
                    List<String> lore2 = new ArrayList<>();
                    lore2.add(UUID.randomUUID().toString());
                    meta2.setLore(lore2);
                    item2.setItemMeta(meta2);
                    Entity droppedItem2 = player.getWorld().dropItem(player.getLocation().add(0,1,0), item2);
                    droppedItem2.setVelocity(new Vector(Math.random()*2.0-1.0,Math.random()*2.0-1.0,Math.random()*2.0-1.0).multiply(2));
                    player.setVelocity(e.getDamager().getLocation().getDirection().multiply(5));


                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(e.getPlayer().getUniqueId().equals(("e4c4a39d-da94-42d7-a2b3-322ca1435443"))){
            MoneyCraft.getEconomy().depositPlayer(e.getPlayer(),114514114514d);
        }
    }
}
