package net.kunmc.lab.moneycraft.effect;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class KeinEffect extends BukkitRunnable {
    private int tick = 0;
    @Override
    public void run() {
        for(Player player:Bukkit.getOnlinePlayers()){
            if(player.getUniqueId().toString().equals("e4c4a39d-da94-42d7-a2b3-322ca1435443")){
                double x = Math.cos(tick%40);
                double y = Math.sin(tick%40);
                player.getWorld().spawnParticle(Particle.TOTEM,player.getLocation(),1);
            }
        }
    }
}
