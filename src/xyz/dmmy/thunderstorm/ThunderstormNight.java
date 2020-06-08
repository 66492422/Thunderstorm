package xyz.dmmy.thunderstorm;

import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;
import xyz.dmmy.thunderstorm.event.CreatureSpawn;
import xyz.dmmy.thunderstorm.utility.UtilityConfig;
import xyz.dmmy.thunderstorm.utility.WorldTime;
import xyz.dmmy.thunderstorm.utility.WorldTimeEvent;

import java.util.Random;
import java.util.TimerTask;

public class ThunderstormNight implements WorldTimeEvent {
    private JavaPlugin mPlugin;
    private World mWorld;
    private WorldTime mWorldTime;
    private boolean mEnsure = false;//到夜晚必定是雷暴之夜
    private boolean mThunderstormNight = false;
    private BukkitTask mThunderTimer = null;
    private UtilityConfig mConfig;

    public void ensure(boolean status) {
        mEnsure = status;
    }

    public ThunderstormNight(JavaPlugin plugin, World world, UtilityConfig config) {
        mPlugin = plugin;
        mWorld = world;
        mConfig = config;
        mWorldTime = new WorldTime(mPlugin, world, this);
    }

    void broadcast(String msg, int repetition) {
        for (Player player : mWorld.getPlayers()) {
            for (int i = 0; i < repetition; ++i) {
                player.sendMessage(msg);
            }
        }
    }

    void broadcast(String msg) {
        broadcast(msg, 1);
    }

    public void cancel() {
        if (mWorldTime != null) {
            mWorldTime.cancel();
        }

    }

    public boolean hasThunderstormNight() {
        return mThunderstormNight;
    }

    private boolean checkPrecondition(World world) {
        //雷暴之夜前提条件
        return !mConfig.getBoolean("thunderstorm_night.precondition.thunder", true) || world.isThundering();
    }

    private boolean checkProbability() {
        //雷雨夜出现雷暴之夜的概率验证
        return (new Random().nextInt(100) < mConfig.getInt("thunderstorm_night.probability", 20));
    }

    @Override
    public void onWorldTimeChange(final World world, final WorldTime.TimeQuantum time) {

        mPlugin.getServer().getScheduler().runTask(mPlugin, new Runnable() {
            @Override
            public void run() {
                if (time == WorldTime.TimeQuantum.dusk) {

                    //只有在雷雨天气才有可能出现雷暴之夜
                    if (mEnsure || (!mThunderstormNight && (checkPrecondition(world) && checkProbability()))) {
                        if (mThunderTimer != null) {
                            mPlugin.getLogger().warning("雷暴之夜开启失败，定时器非空！");
                            return;
                        }

                        mEnsure = false;
                        mThunderstormNight = true;
                        broadcast("§4雷暴之夜将至，将频繁出现雷击且怪物将狂暴并大量出现，请尽快进入掩体躲避！");

                        //加强怪物
                        for (Entity entity : mWorld.getEntities()) CreatureSpawn.MonsterRage(mConfig, entity);

                        world.setTicksPerMonsterSpawns(mConfig.getInt("thunderstorm_night.monsters.ticks", 20));//设置刷怪时间间隔
                        world.setMonsterSpawnLimit(mConfig.getInt("thunderstorm_night.monsters.amount", 300));//怪物数量

                        world.setStorm(true);
                        world.setWeatherDuration(Math.toIntExact(24000 - mWorld.getTime()));

                        long interval = mConfig.getLong("thunderstorm_night.random_lightning.interval", 20 * 10);

                        mThunderTimer = mPlugin.getServer().getScheduler().runTaskTimer(mPlugin, new TimerTask() {
                            @Override
                            public void run() {
                                RandomLightning();//随机雷击
                            }
                        }, interval, interval);

                        mPlugin.getLogger().info(world.getName() + "世界因进入雷暴之夜，怪物刷新时间以及怪物数量被改变。" +
                                "怪物刷新时间间隔：" + world.getTicksPerMonsterSpawns() +
                                ";怪物数量限制：" + world.getMonsterSpawnLimit() +
                                ";每隔" + interval + "tick将会对所有生物随机雷击");
                    }

                } else if (time == WorldTime.TimeQuantum.morning && mThunderstormNight) {
                    mThunderstormNight = false;

                    broadcast("§2雷暴之夜已结束");

                    world.setTicksPerMonsterSpawns(-1);//设置刷怪时间间隔
                    world.setMonsterSpawnLimit(-1);//设置怪物数量限制
                    //world.setStorm(false);

                    if (mThunderTimer != null) {
                        mThunderTimer.cancel();
                        mThunderTimer = null;
                    }

                }
            }
        });

    }

    private void RandomLightning() {
        Random random = new Random();
        for (Entity entity : mWorld.getEntities()) {
            int probability;

            if (entity.getType() != EntityType.PLAYER) {
                //非玩家雷击概率
                probability = mConfig.getInt("thunderstorm_night.random_lightning.probability.other", 20);
            } else {
                //玩家被雷击概率
                probability = mConfig.getInt("thunderstorm_night.random_lightning.probability.player", 5);
            }

            if (random.nextInt(100) < probability) {
                Lightning(entity);
            }

        }
    }

    private void Lightning(Entity entity) {
        Location location = entity.getLocation();

        int blockY = location.getBlockY();

        //模拟雷电从天空落下
        for (int y = 256; y > blockY; --y) {
            location.setY(y);
            //遇到障碍物，跳出
            if (!location.getBlock().isPassable()) break;
        }

        mWorld.strikeLightning(location);
    }
}
