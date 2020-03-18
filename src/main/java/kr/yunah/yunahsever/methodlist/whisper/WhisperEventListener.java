package kr.yunah.yunahsever.methodlist.whisper;


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;
import java.util.UUID;

public class WhisperEventListener implements Listener {

    private final WhisperCommand whisper;

    // 점검 끝 체크 완료

    public WhisperEventListener(WhisperCommand whisper) {
        this.whisper = whisper;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void w_chat(PlayerChatEvent e) {
        if(whisper.contains(e.getPlayer().getUniqueId())) {
            e.setCancelled(true);
            Player target = whisper.gettarget(e.getPlayer().getUniqueId());
            if(target.isOnline()) {
                whisper.whisper(e.getPlayer(), target, e.getMessage());
            } else {
                whisper.remove(e.getPlayer().getUniqueId());
                e.getPlayer().sendMessage("§b귓속말 §f: 현재 플레이어는 오프라인 상태여서 자동으로 해제 되었습니다");
            }
        }
    }

    @EventHandler
    public void w_quit(PlayerQuitEvent e) {
        if(whisper.contains(e.getPlayer().getUniqueId())) {
            whisper.remove(e.getPlayer().getUniqueId());
        }
    }
}
