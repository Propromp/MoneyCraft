package net.kunmc.lab.moneycraft.effect;

import net.kunmc.lab.moneycraft.MoneyCraft;
import net.minecraft.server.v1_15_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_15_R1.ParticleType;
import net.minecraft.server.v1_15_R1.Particles;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KeinEffect extends BukkitRunnable {
    public static int use;

    private int tick = 0;
    @Override
    public void run() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if(MoneyCraft.instance.getConfig().getList("uuid.kein").contains(player.getUniqueId().toString())){
                int tick2 = tick % 80;
                if (tick2 < 20) {//下から上
                    double radian = Math.toRadians(((double) tick2) * 18.0);
                    double x1 = Math.cos(radian);
                    double z1 = Math.sin(radian);
                    double x2 = Math.cos(-radian);
                    double z2 = Math.sin(-radian);
                    double y = ((double) tick2) / 10.0;
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x1, y, z1));
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x2, y, z2));
                } else if (tick2 < 40) {//上
                    double radian = Math.toRadians(((double) (tick2 - 20)) * 18.0);
                    double x1 = Math.cos(radian);
                    double z1 = Math.sin(radian);
                    double x2 = Math.cos(-radian);
                    double z2 = Math.sin(-radian);
                    double y = 2;
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x1, y, z1));
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x2, y, z2));
                } else if (tick2 < 60) {//上から下
                    double radian = Math.toRadians(((double) (tick2 - 40)) * 18.0);
                    double x1 = Math.cos(radian);
                    double z1 = Math.sin(radian);
                    double x2 = Math.cos(-radian);
                    double z2 = Math.sin(-radian);
                    double y = ((double) (tick2 - 40)) / 10.0;
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(0, 2, 0).subtract(x1, y, z1));
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(0, 2, 0).subtract(x2, y, z2));
                } else {//下
                    double radian = Math.toRadians(((double) (tick2 - 60)) * 18.0);
                    double x1 = Math.cos(radian);
                    double z1 = Math.sin(radian);
                    double x2 = Math.cos(-radian);
                    double z2 = Math.sin(-radian);
                    double y = 0;
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x1, y, z1));
                    sendFakeParticleToAll(Particles.FLAME, player.getLocation().add(x2, y, z2));
                }
                tick++;
                if(use == 1){
                    for(Player p:Bukkit.getOnlinePlayers()){
                        if(!MoneyCraft.instance.getConfig().getList("uuid.kein").contains(player.getUniqueId().toString())){
                            p.spawnParticle(Particle.TOTEM, player.getLocation(), 20);
                        }
                    }
                } else if(use == 2) {
                    player.getWorld().spawnParticle(Particle.TOTEM, player.getLocation(), 20);
                }
            }
        }
    }
    private static void sendFakeParticleToAll(ParticleType type,Location loc){
        switch (use) {
            case 0:break;
            case 1:
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if(!MoneyCraft.instance.getConfig().getList("uuid.kein").contains(p.getUniqueId().toString()))
                        sendFakeParticle(p, type, loc);
                }
                break;
            case 2:
                for (Player p : Bukkit.getOnlinePlayers()) {
                        sendFakeParticle(p, type, loc);
                }
                break;
        }
    }
    private static void sendFakeParticle(Player player, ParticleType type, Location loc){
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(type,false,loc.getX(),loc.getY(),loc.getZ(),0f,0f,0f,1,0);
        ((CraftPlayer)player).getHandle().playerConnection.sendPacket(packet);
    }
}
