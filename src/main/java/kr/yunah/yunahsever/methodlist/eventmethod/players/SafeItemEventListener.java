package kr.yunah.yunahsever.methodlist.eventmethod.players;

import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.SafeItem;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SafeItemEventListener implements Listener {

    private final PlayerManager playerManager;
    private Map<UUID, String> player = new HashMap<>();

    public SafeItemEventListener(PlayerManager playerManager1) {
        this.playerManager = playerManager1;
    }

    public void open(UUID uuid) {
        YunahPlayer yunahSever = playerManager.get(uuid);
        Map<Integer, ItemStack> map = yunahSever.getSafeItem().getlist();
        Player player = Bukkit.getPlayer(uuid);

        Inventory inv = Bukkit.createInventory(null, 6*9, "금고");

        map.forEach(inv::setItem);

        inv.setItem(49, new ItemBuilder(Material.PLAYER_HEAD)
                .amount(1)
                .setplayer(player)
                .display("§a" + player.getName())
                .lore("§f금고 금액 : " + moneycomma(yunahSever.getSafeMoney()), "§7", "§7( 우클릭시 돈을 입금합니다 )",
                        "§7( 좌클릭시 돈을 출금합니다 )")
                .build());

        player.openInventory(inv);
    }



    @EventHandler
    public void onChat(PlayerChatEvent e) {
        if(player.containsKey(e.getPlayer().getUniqueId())) {
            if(e.getMessage().equals("!취소")) {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§8금고 관리자 §f:: 입력상태가 취소 되었습니다 ");
                player.remove(e.getPlayer().getUniqueId());
                return;
            }
            e.setCancelled(true);
            if(this.player.get(e.getPlayer().getUniqueId()).equals("입금")) {
                SafeMoneyadd(e.getMessage(), e.getPlayer());
            } else if(this.player.get(e.getPlayer().getUniqueId()).equals("출금")) {
                SafeMoneysub(e.getMessage(), e.getPlayer());
            }
        }
    }

    public void SafeMoneyadd(String Imoney, Player player) {
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        try {
            int money = Integer.parseInt(Imoney);
            if(yunahPlayer.getMoney() + 1 > money) {
                yunahPlayer.subMoney(money);
                yunahPlayer.addSafeMoney(money);
                player.sendMessage("§8금고 관리자 §f:: " + moneycomma(money) + "원을 금고에 입금했습니다");
                this.player.remove(player.getUniqueId());
            } else {
                player.sendMessage("§8금고 관리자 §f:: 보유하신 돈이 부족합니다 #보유금액 : " + moneycomma(yunahPlayer.getMoney()));
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§8오류 §f:: 제대로 금액을 적어주세요 ");
        }
    }

    public void SafeMoneysub(String Imoney, Player player) {
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        try {
            int money = Integer.parseInt(Imoney);
            if(yunahPlayer.getSafeMoney() + 1 > money) {
                yunahPlayer.subSafeMoney(money);
                yunahPlayer.addMoney(money);
                player.sendMessage("§8금고 관리자 §f:: " + moneycomma(money) + "원 을 금고에서 출금했습니다");
                this.player.remove(player.getUniqueId());
            } else {
                player.sendMessage("§8금고 관리자 §f:: 금고에 돈이 부족합니다 #금고금액 : " + moneycomma(yunahPlayer.getSafeMoney()));
            }
        } catch (NumberFormatException e) {
            player.sendMessage("§8오류 §f:: 제대로 금액을 적어주세요 ");
        }
    }


    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("금고")) {
            if(e.getRawSlot() == 49) {
                e.setCancelled(true);
                if(e.getClick().equals(ClickType.RIGHT)) {
                    this.player.put(e.getWhoClicked().getUniqueId(), "입금");
                    e.getWhoClicked().sendMessage("§8금고 관리자 §f:: 입금하실 금액을 적어주세요.");
                    e.getWhoClicked().sendMessage("§7  ( 취소 하고싶으면 \"!취소\" 를 입력하세요 )");
                    e.getWhoClicked().closeInventory();
                } else if(e.getClick().equals(ClickType.LEFT)) {
                    this.player.put(e.getWhoClicked().getUniqueId(), "출금");
                    e.getWhoClicked().sendMessage("§8금고 관리자 §f:: 출금하실 금액을 적어주세요.");
                    e.getWhoClicked().sendMessage("§7  ( 취소 하고싶으면 \"!취소\" 를 입력하세요 )");
                    e.getWhoClicked().closeInventory();
                }
            }
        }
    }



    public String moneycomma(int money) {
        return String.format("%,d", money);
    }


    // 아이템 저장 구문

    @EventHandler
    public void close(InventoryCloseEvent e) {
        if (e.getView().getTitle().equals("금고")) {
            YunahPlayer yunahPlayer = playerManager.get(e.getPlayer().getUniqueId());
            SafeItem safeItem = yunahPlayer.getSafeItem();
            safeItem.clearlist();
            for (int i = 0; i < e.getInventory().getSize(); i++) {
                if(i != 49) {
                    safeItem.put(i, e.getInventory().getItem(i));
                }
            }
        }
    }


    @EventHandler
    public void PlayerClick(PlayerInteractAtEntityEvent e) {
        if(e.getRightClicked().getName().contains("[금고]")) {
            Player player = e.getPlayer();
            if (e.getHand() == EquipmentSlot.HAND) {
                if (this.player.containsKey(player.getUniqueId())) {
                    player.sendMessage("§8금고 관리자 §f: 현재 금고를 열수 없습니다 ");
                } else {
                    open(player.getUniqueId());
                }
            }
        }
    }
}
