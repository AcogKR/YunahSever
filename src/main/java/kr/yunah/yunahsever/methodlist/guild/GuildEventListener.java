package kr.yunah.yunahsever.methodlist.guild;

import kr.yunah.yunahsever.serverdata.GuildManager;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class GuildEventListener implements Listener {

    private final GuildManager guildManager;

    public GuildEventListener(GuildManager guildManager) {
        this.guildManager = guildManager;
    }

    // 플레이어 나갈시 받았던거 사라짐
    public void onQuit(PlayerQuitEvent e) {
        if(guildManager.getInvite().containsValue(e.getPlayer().getUniqueId())) {
            guildManager.inviteQuit(e.getPlayer());
        }
    }

    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("길드 정보")) {

        }
    }
}
