package xyz.dmmy.thunderstorm.utility;

import org.bukkit.World;

public interface WorldTimeEvent {
    void onWorldTimeChange(World world, WorldTime.TimeQuantum time);
}
