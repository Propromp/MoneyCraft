package net.kunmc.lab.moneycraft.api;

import net.kunmc.lab.moneycraft.MoneyCraft;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class MoneyCraftAPI {
    /**
     * 金をドロップします
     *
     * @param player   残高を引くプレイヤー。balance,threwがfalseの場合はnull可
     * @param location 落とす場所
     * @param type     落とす金の種類(100or1000)
     * @param amount   落とす枚数
     * @param threw    playerがこのアイテムを拾えるかどうか
     * @param balance  残高を引くかどうか
     * @return ドロップアイテムのエンティティのインスタンス
     */
    public static Item dropMoney(Player player, Location location, int type, int amount, boolean threw, boolean balance) {
        Item droppedItem;
        switch (type) {
            case 100:
                ItemStack item = new ItemStack(Material.GOLD_NUGGET, amount);
                ItemMeta meta = item.getItemMeta();
                meta.setDisplayName("MoneyCraft");
                meta.setCustomModelData(100);
                item.setItemMeta(meta);
                droppedItem = location.getWorld().dropItem(location, item);
                break;
            case 1000:
                ItemStack item2 = new ItemStack(Material.GOLD_INGOT, amount);
                ItemMeta meta2 = item2.getItemMeta();
                meta2.setDisplayName("MoneyCraft");
                meta2.setCustomModelData(1000);
                item2.setItemMeta(meta2);
                droppedItem = location.getWorld().dropItem(location, item2);
                break;
            default:
                return null;
        }
        if (threw) {
            List<String> lore = new ArrayList<>();
            lore.add("threw");
            lore.add(player.getUniqueId().toString());
            ItemMeta meta = droppedItem.getItemStack().getItemMeta();
            meta.setLore(lore);
            droppedItem.getItemStack().setItemMeta(meta);
        }
        if (balance) {
            MoneyCraft.getEconomy().depositPlayer(player, -(type * amount));
        }
        return droppedItem;
    }
}
