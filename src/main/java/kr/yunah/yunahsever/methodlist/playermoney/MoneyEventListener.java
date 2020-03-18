package kr.yunah.yunahsever.methodlist.playermoney;

import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class MoneyEventListener implements Listener {

    private final PlayerManager playerManager;

    public MoneyEventListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @EventHandler
    public void PlayerInter(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getItem() == null || e.getItem().getItemMeta() == null) { return; }
        if(e.getItem().getItemMeta().getDisplayName().contains("수표")) {
            YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
            try{
                int money = Integer.parseInt(e.getItem().getItemMeta().getLore().get(2).substring(e.getItem().getItemMeta().getLore().get(2).lastIndexOf(":")+2).replace(",", ""));
                player.getInventory().remove(e.getItem());
                yunahPlayer.addMoney(money);
                player.sendMessage("§f");
                player.sendMessage("§8금고 관리자 §f: 수표를 돈으로 변환했습니다");
                player.sendMessage("§f");
            } catch (NumberFormatException e1) {
            }
        }
    }
}
