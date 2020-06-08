package xyz.dmmy.thunderstorm.utility;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.Timer;
import java.util.TimerTask;

public class WorldTime {
    public enum TimeQuantum {
        morning, noon, dusk, night
    }

    private JavaPlugin mPlugin;
    private World mWorld;
    private WorldTimeEvent mListen;
    private TimeQuantum mCurrentTime = null;
    private BukkitTask mTimer;

    public WorldTime(JavaPlugin plugin, World world, WorldTimeEvent listen) {
        this.mWorld = world;
        this.mListen = listen;
        this.mPlugin = plugin;

        mTimer = mPlugin.getServer().getScheduler().runTaskTimer(mPlugin, new TimerTask() {
            @Override
            public void run() {
                long time = mWorld.getTime();
                if (time >= 0 && time < 6000) {
                    if (mCurrentTime != TimeQuantum.morning) {
                        mCurrentTime = TimeQuantum.morning;
                        mListen.onWorldTimeChange(mWorld, mCurrentTime);
                    }
                } else if (time >= 6000 && time < 12000) {
                    if (mCurrentTime != TimeQuantum.noon) {
                        mCurrentTime = TimeQuantum.noon;
                        mListen.onWorldTimeChange(mWorld, mCurrentTime);
                    }
                } else if (time >= 12000 && time < 18000) {
                    if (mCurrentTime != TimeQuantum.dusk) {
                        mCurrentTime = TimeQuantum.dusk;
                        mListen.onWorldTimeChange(mWorld, mCurrentTime);
                    }
                } else {
                    if (mCurrentTime != TimeQuantum.night) {
                        mCurrentTime = TimeQuantum.night;
                        mListen.onWorldTimeChange(mWorld, mCurrentTime);
                    }
                }
            }
        }, 20L, 20L);

    }

    public void cancel() {
        mTimer.cancel();
    }

}
