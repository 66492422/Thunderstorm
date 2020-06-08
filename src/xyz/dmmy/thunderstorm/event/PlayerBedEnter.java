package xyz.dmmy.thunderstorm.event;

import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import xyz.dmmy.thunderstorm.ThunderstormNight;

import java.util.Map;

public class PlayerBedEnter implements Listener {

    private Map<World, ThunderstormNight> mThunderstormNights;

    public PlayerBedEnter(Map<World, ThunderstormNight> thunderstormNights) {
        mThunderstormNights = thunderstormNights;
    }


    @EventHandler
    void onPlayerBedEnter(PlayerBedEnterEvent event)
    {
        if(mThunderstormNights.get(event.getPlayer().getWorld()).hasThunderstormNight())
        {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§4您不能在雷暴之夜的世界睡觉！");
        }
    }
}
