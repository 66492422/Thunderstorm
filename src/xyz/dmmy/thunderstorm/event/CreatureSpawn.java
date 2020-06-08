package xyz.dmmy.thunderstorm.event;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import xyz.dmmy.thunderstorm.ThunderstormNight;
import xyz.dmmy.thunderstorm.utility.UtilityConfig;

import java.util.Map;

public class CreatureSpawn implements Listener {
    private Map<World, ThunderstormNight> mThunderstormNights;
    private UtilityConfig mConfig;

    public CreatureSpawn(Map<World, ThunderstormNight> thunderstormNights, UtilityConfig config) {
        mThunderstormNights = thunderstormNights;
        mConfig = config;
    }

    @EventHandler
    void onCreatureSpawn(CreatureSpawnEvent event) {
        ThunderstormNight thunderstormNight = mThunderstormNights.get(event.getEntity().getWorld());
        if (thunderstormNight == null) return;
        if (thunderstormNight.hasThunderstormNight()) {
            MonsterRage(mConfig, event.getEntity());
        }
    }

    public static void MonsterRage(UtilityConfig config, Entity entity) {
        if (entity.getType() == EntityType.ZOMBIE || entity.getType() == EntityType.SKELETON || entity.getType() == EntityType.SPIDER) {
            Monster monster = (Monster) entity;
            int tick = Math.toIntExact(24000 - monster.getWorld().getTime());
            int lvl;
            lvl = config.getInt("thunderstorm_night.monsters.buff.speed", 2);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, tick, lvl));
            lvl = config.getInt("thunderstorm_night.monsters.buff.damage_resistance", 2);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, tick, lvl));
            lvl = config.getInt("thunderstorm_night.monsters.buff.health_boost", 3);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.HEALTH_BOOST, tick, lvl));
            lvl = config.getInt("thunderstorm_night.monsters.buff.fire_resistance", 1);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, tick, lvl));
            lvl = config.getInt("thunderstorm_night.monsters.buff.increase_damage", 2);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, tick, lvl));
            lvl = config.getInt("thunderstorm_night.monsters.buff.absorption", 3);
            if (lvl > 0) monster.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, tick, lvl));

        }
    }
}
