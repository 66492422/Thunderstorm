package xyz.dmmy.thunderstorm;

import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.dmmy.thunderstorm.event.CreatureSpawn;
import xyz.dmmy.thunderstorm.event.PlayerBedEnter;
import xyz.dmmy.thunderstorm.utility.UtilityConfig;

import javax.annotation.Nonnull;
import java.util.*;

public class PluginMain extends JavaPlugin implements CommandExecutor {
    private Map<World, ThunderstormNight> mThunderstormNights = new HashMap<>();

    @Override
    public void onEnable() {
        getLogger().info("【雷暴之夜1.2】作者：短毛猫 Email：66492422@qq.com shortmeowmeow@gmail.com 开源：https://github.com/66492422/Thunderstorm");

        UtilityConfig mConfig = new UtilityConfig(this);

        getServer().getPluginManager().registerEvents(new CreatureSpawn(mThunderstormNights, mConfig), this);
        getServer().getPluginManager().registerEvents(new PlayerBedEnter(mThunderstormNights), this);

        Objects.requireNonNull(getCommand("thunderstorm_night")).setExecutor(this);

        mThunderstormNights.clear();

        for (World world : getServer().getWorlds()) {
            if (world.getEnvironment() == World.Environment.NORMAL)//只有正常的世界才支持
            {
                mThunderstormNights.put(world, new ThunderstormNight(this, world, mConfig));
            }
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
        for (World t : mThunderstormNights.keySet()) {
            mThunderstormNights.get(t).cancel();
        }
        mThunderstormNights.clear();
        super.onDisable();
    }

    @Override
    public boolean onCommand(@Nonnull CommandSender sender, @Nonnull Command command, @Nonnull String label, @Nonnull String[] args) {
        if (command.getName().equalsIgnoreCase("thunderstorm_night")) {
            if (!sender.hasPermission("xyz.dmmy.thunderstorm.thunderstorm_night")) {
                sender.sendMessage("§4您没有权限使用此功能");
                return true;
            }

            if (args.length != 1) {
                sender.sendMessage("§4请正确设置参数");
                return true;
            }

            boolean status = args[0].equalsIgnoreCase("true");
            if (sender instanceof Player) {
                ThunderstormNight thunderstormNight = mThunderstormNights.get(((Player) sender).getWorld());
                if (thunderstormNight == null) {
                    sender.sendMessage("§4您所在的世界不支持此设置");
                    return true;
                }
                thunderstormNight.ensure(status);
            } else {
                for (World t : mThunderstormNights.keySet()) {
                    mThunderstormNights.get(t).ensure(status);
                }

            }
            sender.sendMessage("§4" + (status ? "下次入夜后必为雷暴之夜" : "已取消‘下次入夜后必为雷暴之夜’设置"));
        }
        return true;
    }
}
