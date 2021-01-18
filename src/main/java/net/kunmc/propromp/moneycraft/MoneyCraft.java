package net.kunmc.propromp.moneycraft;

import net.kunmc.propromp.moneycraft.command.MoneyCommandExecutor;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class MoneyCraft extends JavaPlugin {

    private static Economy economy;

    @Override
    public void onEnable() {
        getLogger().info("Hi!");
        getLogger().info(ChatColor.AQUA+"MONEYCRAFT BY PROPROMP");
        getLogger().info("Copytight 2021 TeamKun., Propromp");

        if (!setupEconomy() ) {
            getLogger().severe(String.format("[%s] - Vaultが見つからないんだが.", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getCommand("moneycraft").setExecutor(new MoneyCommandExecutor());
    }

    @Override
    public void onDisable() {
        getLogger().info("Bay!");
    }

    public static Economy getEconomy(){
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
        return economy != null;
    }
}
