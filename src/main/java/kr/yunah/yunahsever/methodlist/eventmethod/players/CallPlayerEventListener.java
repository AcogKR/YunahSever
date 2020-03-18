package kr.yunah.yunahsever.methodlist.eventmethod.players;

import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.ValueData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatEvent;

public class CallPlayerEventListener implements Listener {

    private final PlayerManager playerManager;

    public CallPlayerEventListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void PlayerChat(PlayerChatEvent e) {
        String Message = e.getMessage();
        Player hplayer = e.getPlayer();
        
        if(Message.contains("@") && !Message.contains("@@")) {
            e.setCancelled(true);
            String SPlayer = e.getMessage().substring(1);
            if(hplayer.getName().equalsIgnoreCase(SPlayer)) {
                hplayer.sendMessage("§8오류 : §f자기 자신은 호출할수 없습니다");
                return;
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if(players.getName().equalsIgnoreCase(SPlayer)) {
                    Player player = Bukkit.getPlayer(SPlayer);
                    if(playerManager.get(player.getUniqueId()).getValueData().isCallPlayer()) {
                        player.sendTitle("§a호출", String.format("§7%s 님이 당신을 호출 했습니다", hplayer.getName()));
                        hplayer.sendMessage(String.format("§a호출 : §f%s을 호출했습니다", player.getName()));
                        return;
                    } else {
                        hplayer.sendMessage(String.format("§a호출 : §f%s님은 호출 거부상태 입니다", player.getName()));
                        return;
                    }
                }
            }
            hplayer.sendMessage(String.format("§8오류 : §f%s님은 서버에 접속하지 않았습니다", SPlayer));
        }
    }

}
