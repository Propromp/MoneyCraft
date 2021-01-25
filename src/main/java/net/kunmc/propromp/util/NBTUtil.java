package net.kunmc.propromp.util;

import net.minecraft.server.v1_15_R1.*;
import org.bukkit.craftbukkit.v1_15_R1.inventory.CraftItemStack;

import java.util.List;

/**
 * NBTutil class for Spigot 1.15.2. You can use any version by editing the import statement.
 * @author Propromp
 *
 */
public class NBTUtil {
    /**
     * set NBT Tag by String
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setString(org.bukkit.inventory.ItemStack item, String key, String value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagString.a(value));
    }
    /**
     * set NBT Tag by Byte
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setByte(org.bukkit.inventory.ItemStack item, String key, Byte value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagByte.a(value));
    }
    /**
     * set NBT Tag by Byte Array
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setByteArray(org.bukkit.inventory.ItemStack item, String key, List<Byte> value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, new NBTTagByteArray(value));
    }
    /**
     * set NBT Tag by Double
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setDouble(org.bukkit.inventory.ItemStack item, String key, double value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagDouble.a(value));
    }
    /**
     * set NBT Tag by Float
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setFloat(org.bukkit.inventory.ItemStack item, String key, float value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagFloat.a(value));
    }
    /**
     * set NBT Tag by integer
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setInt(org.bukkit.inventory.ItemStack item, String key, int value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagInt.a(value));
    }
    /**
     * set NBT Tag by long
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setLong(org.bukkit.inventory.ItemStack item, String key, long value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagLong.a(value));
    }
    /**
     * set NBT Tag by integer array
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setIntArray(org.bukkit.inventory.ItemStack item, String key, int[] value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, new NBTTagIntArray(value));
    }
    /**
     * set NBT Tag by long array
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setLongArray(org.bukkit.inventory.ItemStack item, String key, long[] value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, new NBTTagLongArray(value));
    }
    /**
     * set NBT Tag by short
     * @param item item
     * @param key key of the nbt tag
     * @param value value of the nbt tag
     */
    public static void setShort(org.bukkit.inventory.ItemStack item, String key, short value){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        tag.set(key, NBTTagShort.a(value));
    }


    /**
     * set NBT Tag by NBTBase
     * @param item item
     * @param key key of the nbt tag
     */
    public static NBTBase get(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.get(key);
    }
    /**
     * set NBT Tag by String
     * @param item item
     * @param key key of the nbt tag
     */
    public static String getString(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getString(key);
    }
    /**
     * set NBT Tag by byte
     * @param item item
     * @param key key of the nbt tag
     */
    public static byte getByte(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getByte(key);
    }
    /**
     * set NBT Tag by byte array
     * @param item item
     * @param key key of the nbt tag
     */
    public static byte[] getByteArray(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getByteArray(key);
    }
    /**
     * set NBT Tag by double
     * @param item item
     * @param key key of the nbt tag
     */
    public static double getDouble(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getDouble(key);
    }
    /**
     * set NBT Tag by float
     * @param item item
     * @param key key of the nbt tag
     */
    public static float getFloat(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getFloat(key);
    }
    /**
     * set NBT Tag by integer
     * @param item item
     * @param key key of the nbt tag
     */
    public static int getInt(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getInt(key);
    }
    /**
     * set NBT Tag by long
     * @param item item
     * @param key key of the nbt tag
     */
    public static long getLong(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getLong(key);
    }
    /**
     * set NBT Tag by integer array
     * @param item item
     * @param key key of the nbt tag
     */
    public static int[] getIntArray(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getIntArray(key);
    }
    /**
     * set NBT Tag by long array
     * @param item item
     * @param key key of the nbt tag
     */
    public static long[] getLongArray(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getLongArray(key);
    }
    /**
     * set NBT Tag by short
     * @param item item
     * @param key key of the nbt tag
     */
    public static short getShort(org.bukkit.inventory.ItemStack item,String key){
        ItemStack nmsStack = ((CraftItemStack)item).getHandle();
        NBTTagCompound tag = nmsStack.getOrCreateTag();
        return tag.getShort(key);
    }
}