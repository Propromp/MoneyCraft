package net.kunmc.lab.moneycraft.event;

import net.kunmc.lab.moneycraft.MoneyCraft;
import net.kunmc.lab.moneycraft.api.MoneyCraftAPI;
import net.md_5.bungee.api.chat.TextComponent;
import net.minecraft.server.v1_15_R1.EntityLightning;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_15_R1.ParticleType;
import net.minecraft.server.v1_15_R1.Particles;
import org.bukkit.*;
import org.bukkit.craftbukkit.v1_15_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftItem;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
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
                    if(e.getPlayer().getUniqueId().toString().equals(MoneyCraft.instance.getConfig().getList("uuid.kein"))){//kein
                        e.getPlayer().sendMessage("残高："+ChatColor.MAGIC+ "114514" +ChatColor.RESET+"円");
                    } else {
                        e.getPlayer().sendMessage("残高：" + MoneyCraft.getEconomy().getBalance(e.getPlayer()) + "円");
                    }
                }
            }
        }
    }
    @EventHandler
    public void onDropItem(PlayerDropItemEvent e){
        drop.put(e.getPlayer(),true);
        if(!(e.getItemDrop().getItemStack().getType().equals(Material.AIR))&&e.getItemDrop().getItemStack().getItemMeta().hasCustomModelData()) {
            if (e.getItemDrop().getItemStack().getItemMeta().getCustomModelData() >= 1 && e.getItemDrop().getItemStack().getType() == Material.WHEAT_SEEDS) {
                if (e.getPlayer().isSneaking()) {//Shift(千円投げる)
                    if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 1000) {
                        Entity droppedItem = MoneyCraftAPI.dropMoney(e.getPlayer(),e.getPlayer().getLocation().add(0,1,0),1000,1,false,!e.getPlayer().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
                        droppedItem.setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.5));
                    }
                } else {//not Shift(百円投げる)
                    if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 100) {
                        Entity droppedItem = MoneyCraftAPI.dropMoney(e.getPlayer(),e.getPlayer().getLocation().add(0,1,0),100,1,false,!e.getPlayer().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
                        droppedItem.setVelocity(e.getPlayer().getLocation().getDirection().multiply(0.5));
                    }
                }
                e.setCancelled(true);
            }
        }
    }
    private Map<Player,Boolean> drop = new HashMap<Player,Boolean>();
    @EventHandler
    public void onLeftClick(PlayerInteractEvent e){
        if(e.getAction().equals(Action.LEFT_CLICK_AIR)) {
            if(drop.keySet().contains(e.getPlayer())) {
                if (drop.get(e.getPlayer())) {
                    drop.put(e.getPlayer(), false);
                    return;
                }
            }
            if (!(e.getItem().getType().equals(Material.AIR)) && e.getItem().getItemMeta().hasCustomModelData()) {
                if (e.getItem().getItemMeta().getCustomModelData() >= 1 && e.getItem().getType() == Material.WHEAT_SEEDS) {
                    if (e.getPlayer().isSneaking()) {//Shift(千円投げる)
                        if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 1000) {
                            Entity droppedItem = MoneyCraftAPI.dropMoney(e.getPlayer(),e.getPlayer().getLocation().add(0,1,0),1000,1,true,!e.getPlayer().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
                            droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation().add(0, 1, 0), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
                        }
                    } else {//not Shift(百円投げる)
                        if (MoneyCraft.getEconomy().getBalance(e.getPlayer()) >= 100) {
                            Entity droppedItem = MoneyCraftAPI.dropMoney(e.getPlayer(),e.getPlayer().getLocation().add(0,1,0),100,1,true,!e.getPlayer().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
                            droppedItem.setVelocity(e.getPlayer().getLocation().getDirection());
                            e.getPlayer().getWorld().playSound(e.getPlayer().getLocation().add(0, 1, 0), Sound.ENTITY_SNOWBALL_THROW, 1, 1);
                        }
                    }
                    e.setCancelled(true);
                }
            }
        }
    }


    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Bukkit.broadcastMessage(ChatColor.DARK_RED+e.getEntity().getName()+"は死んだことにより"+MoneyCraft.getEconomy().getBalance(e.getEntity())+"円失った");
        if(e.getEntity().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein"))){
            Bukkit.broadcastMessage(ChatColor.GOLD+"だがしかし、keinは金に目がないので復活するまで財布にしがみついた");
        } else {
            int ingot_amount = ((int) (MoneyCraft.getEconomy().getBalance(e.getEntity()))) / 1000;
            int nugget_amount = ((int) (MoneyCraft.getEconomy().getBalance(e.getEntity())) - ingot_amount * 1000) / 100;
            MoneyCraft.getEconomy().depositPlayer(e.getEntity(), -MoneyCraft.getEconomy().getBalance(e.getEntity()));

            if (nugget_amount > 0) {
                MoneyCraftAPI.dropMoney(e.getEntity(),e.getEntity().getLocation().add(0,1,0),100,nugget_amount,false,!e.getEntity().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
            }
            if (ingot_amount > 0) {
                MoneyCraftAPI.dropMoney(e.getEntity(),e.getEntity().getLocation().add(0,1,0),1000,nugget_amount,false,!e.getEntity().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
            }
        }
    }

    @EventHandler
    public void onPickup(PlayerAttemptPickupItemEvent e){
        Player player = e.getPlayer();
        if (e.getItem().getItemStack().getItemMeta().getDisplayName().equals("MoneyCraft")) {
            if (e.getItem().getItemStack().getType() == Material.GOLD_INGOT) {
                player.sendTitle("",e.getItem().getItemStack().getAmount() * 1000 + "円を財布に入れました。",5,10,5);
                MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getItemStack().getAmount() * 1000);
                e.setCancelled(true);
            } else if (e.getItem().getItemStack().getType() == Material.GOLD_NUGGET) {
                player.sendTitle("",e.getItem().getItemStack().getAmount() * 100 + "円を財布に入れました。",5,10,5);
                MoneyCraft.getEconomy().depositPlayer(player, e.getItem().getItemStack().getAmount() * 100);
                e.setCancelled(true);
            }
            for(int i = 0; i < 360; i += 20){
                double radian = Math.toRadians(i);
                double x = Math.sin(radian);
                double z = Math.cos(radian);
                sendFakeParticleToAll(Particles.FLAME,player.getLocation().add(x,1.5,z));
            }
            ((CraftItem)e.getItem()).getHandle().killEntity();
            player.playSound(player.getLocation(),Sound.ENTITY_ITEM_PICKUP,1f,1f);
            player.playSound(player.getLocation(),"minecraft:moneycraft.pickup",1f,1f);
        }
    }

    @EventHandler
    public void onPunch(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player && e.getEntity() instanceof Player){
            Player player = (Player)e.getEntity();
            if(e.getDamager().getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein"))){
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_PLAYER_ATTACK_CRIT,1,1);
                player.playSound(player.getLocation(),Sound.ENTITY_ITEM_PICKUP,1,1);
                player.getWorld().spawnParticle(Particle.TOTEM,player.getLocation().add(0,1,0),100);
                for(int i = 0;i < 30;i++){
                    Item droppedItem = MoneyCraftAPI.dropMoney(player,player.getLocation().add(0,1,0),100,1,true,!player.getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein").get(0)));
                    droppedItem.setVelocity(new Vector(Math.random()*2.0-1.0,Math.random()*2.0-1.0,Math.random()*2.0-1.0).multiply(2));
                    player.setVelocity(e.getDamager().getLocation().getDirection().multiply(5));

                    Item droppedItem2 = MoneyCraftAPI.dropMoney(player,player.getLocation().add(0,1,0),1000,1,true,!player.getUniqueId().equals(MoneyCraft.instance.getConfig().getList("uuid.kein")));
                    droppedItem2.setVelocity(new Vector(Math.random()*2.0-1.0,Math.random()*2.0-1.0,Math.random()*2.0-1.0).multiply(2));
                    player.setVelocity(e.getDamager().getLocation().getDirection().multiply(5));


                }
            }
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        if(e.getPlayer().getUniqueId().equals((MoneyCraft.instance.getConfig().getList("uuid.kein")))){
            MoneyCraft.getEconomy().depositPlayer(e.getPlayer(),114514114514d);
        }
    }

    @EventHandler
    public void onCraft(InventoryClickEvent e){
        if(e.getCursor().getType().equals(Material.WHEAT_SEEDS)){
            if(e.getCursor().hasItemMeta()){
                if(e.getCursor().getItemMeta().hasCustomModelData()){
                    e.setCursor(MoneyCraft.instance.getWallet((OfflinePlayer) e.getWhoClicked()));
                }
            }
        }
    }
    public void pickupParticle(Location loc){
        for(int i = 0; i < 360; i += 20){
            double radian = Math.toRadians(i);
            double x = Math.sin(radian);
            double z = Math.cos(radian);
            sendFakeParticleToAll(Particles.FLAME,loc.add(x,0,z));
        }
    }
    private static void sendFakeParticleToAll(ParticleType type,Location loc){
        for(Player p:Bukkit.getOnlinePlayers()){
            sendFakeParticle(type,loc,p);
        }
    }
    private static void sendFakeParticle(ParticleType type, Location loc, Player player){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type,false,loc.getX(),loc.getY(),loc.getZ(),0f,0.1f,0f,1f,0);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
