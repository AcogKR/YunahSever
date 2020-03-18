package kr.yunah.yunahsever.methodlist.eventmethod.players;

import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.ValueData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;


public class PrivateSettingEventListener implements Listener {

    private final PlayerManager playerManager;

    public PrivateSettingEventListener(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked();
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        ValueData valueData = yunahPlayer.getValueData();
        if(e.getView().getTitle().equals("개인설정")) {
            e.setCancelled(true);
            Inventory inv = e.getInventory();
            if(e.getRawSlot() == 1) {
                if (valueData.isCallPlayer()) {
                    valueData.setCallPlayer(false);
                    inv.setItem(e.getRawSlot(), new ItemBuilder(Material.BOOK)
                            .amount(1).display("§9플레이어 호출").lore(
                                    "§f",
                                    "  §f 비활성화시 플레이어의 호출을 차단합니다",
                                    "§f   현재상태 : " + valueData.isCallPlayer(),
                                    "§f"
                            ).build());
                } else {
                    valueData.setCallPlayer(true);
                    inv.setItem(1, new ItemBuilder(Material.BOOK)
                            .amount(1).display("§9플레이어 호출").lore(
                                    "§f",
                                    "  §f 비활성화시 플레이어의 호출을 차단합니다",
                                    "§f   현재상태 : " + valueData.isCallPlayer(),
                                    "§f"
                            ).build());
                }
            } else if(e.getRawSlot() == 2) {
                if (valueData.isJoinMessage()) {
                    valueData.setJoinMessage(false);
                    inv.setItem(e.getRawSlot(), new ItemBuilder(Material.BOOK)
                            .amount(1).display("§9플레이어 접속메세지").lore(
                                    "§f",
                                    "  §f 비활성화시 플레이어의 접속 메세지를 차단합니다",
                                    "§f   현재상태 : " + valueData.isJoinMessage(),
                                    "§f"
                            ).build());
                } else {
                    valueData.setJoinMessage(true);
                    inv.setItem(e.getRawSlot(), new ItemBuilder(Material.BOOK)
                            .amount(1).display("§9플레이어 접속메세지").lore(
                                    "§f",
                                    "  §f 비활성화시 플레이어의 접속 메세지를 차단합니다",
                                    "§f   현재상태 : " + valueData.isJoinMessage(),
                                    "§f"
                            ).build());
                }
            }
        }
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 9, "개인설정");
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        ValueData valueData = yunahPlayer.getValueData();

        inv.setItem(1, new ItemBuilder(Material.BOOK)
                .amount(1).display("§9플레이어 호출").lore(
                        "§f",
                        "  §f 비활성화시 플레이어의 호출을 차단합니다",
                        "§f   현재상태 : " + valueData.isCallPlayer(),
                        "§f"
                ).build());
        inv.setItem(2, new ItemBuilder(Material.BOOK)
                .amount(1).display("§9플레이어 접속메세지").lore(
                        "§f",
                        "  §f 비활성화시 플레이어의 접속 메세지를 차단합니다",
                        "§f   현재상태 : " + valueData.isJoinMessage(),
                        "§f"
                ).build());

        player.openInventory(inv);
    }
}
