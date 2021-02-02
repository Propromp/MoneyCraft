package net.kunmc.lab.moneycraft;

import net.DeeChael.ActionbarAPI.AAPI;
import net.kunmc.lab.moneycraft.command.MoneyCommandExecutor;
import net.kunmc.lab.moneycraft.effect.KeinEffect;
import net.kunmc.lab.moneycraft.event.PlayerEvent;
import net.kunmc.propromp.util.NBTUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class MoneyCraft extends JavaPlugin {

    private static Economy economy;

    public static MoneyCraft instance;

    @Override
    public void onEnable() {
        instance=this;

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
        ShapedRecipe recipe = new ShapedRecipe(key, itemStack);
        recipe.shape("LGL");
        recipe.setIngredient('L', Material.LEATHER);
        recipe.setIngredient('G', Material.GOLD_INGOT);
        Bukkit.addRecipe(recipe);

        //keinエフェクト
        new KeinEffect().runTaskTimer(this,0,1);
        KeinEffect.use = getConfig().getInt("part.kein");

        //アクションバー
        new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p:Bukkit.getOnlinePlayers()){
                    AAPI.sendActionbar(p,"所持金:"+((int)getEconomy().getBalance(p))+"円");
                }
            }
        }.runTaskTimer(this,0,5);

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

    public static int getId(OfflinePlayer player) {
        switch (player.getUniqueId().toString()) {
            default:
                return 1;
            case "a3abe1f9-cd08-46b1-9b39-919a3e6150f0"://fakevox
                return 2;
            case "6e8199a3-f28f-468f-a440-4ef55a1f7349"://famas
                return 3;
            case "3f7ccf72-3696-42dc-8eb8-2c813c79eef0"://gorillabbit
                return 4;
            case "1649ef8a-7ec4-4ad5-8234-5ad0709ad461"://himajin
                return 5;
            case "c789f61c-aaa2-44d3-a8f9-21ebf4b00199"://inkya
                return 6;
            case "fee48d55-0120-4e7c-bcf3-8e0ca0dbc265"://kakikama
                return 7;
            case "4f2a2943-2d95-4959-b53e-60cd86edd245"://kamesuta
                return 8;
            case "3c41637b-f0b9-4be2-b72d-7ef863741e07"://kantasuke
                return 9;
            case "7679239c-4eb5-4a18-bddb-874a849d75a7"://kikaru
                return 10;
            case "beb56e2b-5a93-4aff-a0a8-64dd731a9b53"://kirito
                return 11;
            case "713fdcc1-89a7-464c-b90c-346dd0d5fdac"://kono
                return 12;
            case "b08dbdaa-624b-456c-908d-e0ee1846c192"://koutan
                return 13;
            case "a07ec4df-3912-4197-b0f8-4ba0d4e26b43"://kuramochi
                return 14;
        }
    }
}
