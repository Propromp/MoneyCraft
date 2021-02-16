package net.kunmc.lab.moneycraft;

import net.DeeChael.ActionbarAPI.AAPI;
import net.kunmc.lab.moneycraft.command.MoneyCommandExecutor;
import net.kunmc.lab.moneycraft.effect.KeinEffect;
import net.kunmc.lab.moneycraft.event.PlayerEvent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Consumer;
import org.bukkit.util.Vector;

import java.util.*;

public final class MoneyCraft extends JavaPlugin {

    private static Economy economy;

    public static MoneyCraft instance;
    public static ShapedRecipe recipe;

    public static FileConfiguration config;

    @Override
    public void onEnable() {
        instance = this;

        getLogger().info("Hi!");
        getLogger().info(ChatColor.AQUA + "MONEYCRAFT BY PROPROMP");
        getLogger().info("Copytight 2021 TeamKun., Propromp");

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Vaultが見つからないんだが.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        Objects.requireNonNull(getCommand("moneycraft")).setExecutor(new MoneyCommandExecutor());

        //コンフィグ
        saveDefaultConfig();

        //イベント登録
        Bukkit.getPluginManager().registerEvents(new PlayerEvent(), this);

        //財布アイテム登録
        ItemStack itemStack = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName("財布");
        List<String> lore = new ArrayList<>();
        lore.add("Qで百円投げる");
        lore.add("Shift+Qで千円投げる");
        lore.add("右クリックで残高表示");
        lore.add("Shift+右クリックでランキング表示");
        meta.setLore(lore);
        meta.setCustomModelData(1);
        itemStack.setItemMeta(meta);
        //財布レシピ登録
        NamespacedKey key = new NamespacedKey(this, "wallet");
        recipe = new ShapedRecipe(key, itemStack);
        recipe.shape("LGL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(recipe);

        //keinエフェクト
        new KeinEffect().runTaskTimer(this, 0, 1);
        KeinEffect.use = getConfig().getInt("part.kein");

        //アクションバー
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    AAPI.sendActionbar(p, "所持金:" + ((int) getEconomy().getBalance(p)) + "円");
                }
            }
        }.runTaskTimer(this, 0, 5);

        //雪玉版お金のダメージ処理
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player p : Bukkit.getOnlinePlayers()) {
                    for (Entity e : p.getNearbyEntities(0.5, 0.5, 0.5)) {
                        if (e instanceof Item) {
                            Item item = (Item) e;
                            if (item.getItemStack().hasItemMeta()) {
                                if (item.getItemStack().getItemMeta().hasLore()) {
                                    if (item.getItemStack().getItemMeta().getLore().get(0).equals("threw")) {
                                        if (!item.getItemStack().getItemMeta().getLore().get(1).equals(p.getUniqueId().toString())) {
                                            if (item.getVelocity().length() > 0.1) {
                                                p.damage(1.0);
                                                p.setVelocity(item.getVelocity().add(new Vector(0, 0.5, 0)).normalize());
                                                item.setVelocity(new Vector(0, 0, 0));
                                                item.getItemStack().setLore(null);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }.runTaskTimer(this, 0, 1);

    }

    @Override
    public void onDisable() {
        getLogger().info("Bay!");
    }

    public static Economy getEconomy() {
        return economy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return true;
    }

    public int getId(OfflinePlayer player) {
        Map<String,Integer> map = new HashMap<>();
        getConfig().getConfigurationSection("uuid.wallet").getKeys(false).forEach(
                key -> map.put(key,getConfig().getInt("uuid.wallet."+key))
        );
        if(map.containsKey(player.getUniqueId().toString())){
            return map.get(player.getUniqueId().toString());
        } else {
            return map.get("default");
        }
    }

    public ItemStack getWallet(OfflinePlayer player) {
        ItemStack itemStack = new ItemStack(Material.WHEAT_SEEDS);
        ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(getId(player));
        List<String> lore = new ArrayList<>();
        lore.add("Qで百円投げる");
        lore.add("Shift+Qで千円投げる");
        lore.add("右クリックで残高表示");
        lore.add("Shift+右クリックでランキング表示");
        meta.setLore(lore);
        meta.setDisplayName(player.getName() + "の財布");
        itemStack.setItemMeta(meta);
        return itemStack;
    }
}
