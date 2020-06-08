package xyz.dmmy.thunderstorm.utility;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class UtilityConfig {
    private Plugin mPlugin;
    private FileConfiguration mConfig;
    private File mConfigFile;

    public UtilityConfig(Plugin plugin)
    {
        this.mPlugin = plugin;
        File file = plugin.getDataFolder();
        file.mkdirs();
        try
        {

            mConfigFile = new File(file.getPath() + "/config.yml");
            mConfig = plugin.getConfig();
            //如果不存在则重置配置文件数据
            if (!mConfigFile.exists())
            {
                resetConfig();
                mConfig.save(mConfigFile);
            }
            else
            {
                mConfig.load(mConfigFile);
            }

        }catch (IOException | InvalidConfigurationException e)
        {
            mConfig = null;
        }
    }

    public void resetConfig()
    {
        mConfig.set("thunderstorm_night.probability", 20);//雷暴之夜概率
        mConfig.set("thunderstorm_night.precondition.thunder", true);//雷暴之夜前提条件-打雷

        mConfig.set("thunderstorm_night.monsters.amount", 300);//雷暴之夜怪物数量限制
        mConfig.set("thunderstorm_night.monsters.ticks", 20);//雷暴之夜怪物刷新时间

        //雷暴之夜怪物buff等级
        mConfig.set("thunderstorm_night.monsters.buff.speed", 2);//速度
        mConfig.set("thunderstorm_night.monsters.buff.damage_resistance", 2);//损伤阻抗
        mConfig.set("thunderstorm_night.monsters.buff.health_boost", 3);//健康促进
        mConfig.set("thunderstorm_night.monsters.buff.fire_resistance", 1);//扛火等级
        mConfig.set("thunderstorm_night.monsters.buff.increase_damage", 2);//增加伤害
        mConfig.set("thunderstorm_night.monsters.buff.absorption", 3);//伤害吸收

        mConfig.set("thunderstorm_night.random_lightning.interval", 20 * 10);//雷暴之夜随机雷电时间间隔
        mConfig.set("thunderstorm_night.random_lightning.probability.player", 5);//雷暴之夜随机雷电被命中概率-玩家
        mConfig.set("thunderstorm_night.random_lightning.probability.other", 20);//雷暴之夜随机雷电被命中概率-其它
    }

    public boolean hasOpen()
    {
        return mConfig != null;
    }

    public boolean set(String path, Object data)
    {
        if (!hasOpen()) return false;
        mConfig.set(path, data);
        try{
            mConfig.save(mConfigFile);
            return true;
        }catch (Exception e) {
            return false;
        }
    }

    public Object get(String path, Object def)
    {
        if (mConfig == null) return def;
        return mConfig.get(path, def);
    }

    public double getDouble(String path, double def)
    {
        if (mConfig == null) return def;
        return mConfig.getDouble(path, def);
    }

    public double getDouble(String path)
    {
        return getDouble(path, 0);
    }

    public int getInt(String path, int def)
    {
        if (mConfig == null) return def;
        return mConfig.getInt(path, def);
    }

    public int getInt(String path)
    {
        return getInt(path, 0);
    }

    public long getLong(String path, long def)
    {
        if (mConfig == null) return def;
        return mConfig.getLong(path, def);
    }

    public long getLong(String path)
    {
        return getLong(path, 0);
    }

    public boolean getBoolean(String path, boolean def)
    {
        if (mConfig == null) return def;
        return mConfig.getBoolean(path, def);
    }

    public boolean getBoolean(String path)
    {
        return getBoolean(path, false);
    }

    public String getString(String path, String def)
    {
        if (mConfig == null) return def;
        return mConfig.getString(path, def);
    }

    public String getString(String path)
    {
        return getString(path, null);
    }

    public Object get(String path)
    {
        return get(path, null);
    }
}
