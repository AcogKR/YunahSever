package kr.yunah.yunahsever.methodlist.server.monster;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.MonsterManager;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.MonsterData;
import kr.yunah.yunahsever.serverdata.datalist.player.LevelData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MonsterEventListener implements Listener {

    private final MonsterManager monsterManager;
    private final PlayerManager playerManager;
    private final YunahSever yunahSever;

    public MonsterEventListener(MonsterManager monsterManager, PlayerManager playerManager, YunahSever yunahSever) {
        this.monsterManager = monsterManager;
        this.playerManager = playerManager;
        this.yunahSever = yunahSever;
    }



    @EventHandler
    public void set_click(InventoryCloseEvent e) {
        if(e.getView().getTitle().contains("아이템설정")) {
            String name = e.getView().getTitle().substring(8);
            MonsterData monsterData = monsterManager.get(name);
            Map<Integer, ItemStack> items = new HashMap<>();
            if(monsterData == null) { return; }
            for (int i = 0; i < e.getInventory().getSize(); i++) {
                items.put(i, e.getInventory().getItem(i));
            }
            monsterData.setItems(items);
            e.getPlayer().sendMessage("§cYunah§f : 몬스터의 아이템을 설정했습니다 ");
        }
    }

    @EventHandler
    public void monster(EntityDeathEvent e) {
        if(e.getEntity() instanceof Player || e.getEntity().getCustomName() == null || e.getEntity().getKiller() == null) { return; }
        if(Objects.requireNonNull(e.getEntity().getCustomName()).contains("§cM§f")) {
            if (monsterManager.isContain(e.getEntity().getCustomName().substring(6))) {
                MonsterData monsterData = monsterManager.get(e.getEntity().getCustomName().substring(6));
                Player player = e.getEntity().getKiller();
                YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
                LevelData levelData = yunahPlayer.getLevelData();
                for (Map.Entry<Integer, ItemStack> map : monsterData.getItems().entrySet()) {
                    if(map.getValue() != null) {
                        if(!invFull(player)) {
                            player.getInventory().addItem(map.getValue());
                        } else {
                            e.getEntity().getWorld().dropItem(e.getEntity().getLocation(), map.getValue());
                        }
                    }
                }
                levelData.addExp(monsterData.getExp());
                if(monsterData.getMoney1() == monsterData.getMoney2()) {
                    player.sendMessage(String.format("§7◈ 경험치 획득 +%s [ %s / %s ] : 크론 획득 +%s",
                            monsterData.getExp(), levelData.getExp(), levelData.getMexp(), monsterData.getMoney1()));
                    yunahPlayer.addMoney(monsterData.getMoney1());
                } else {
                    int money = (int) (Math.random() * (monsterData.getMoney2() - monsterData.getMoney1())) + monsterData.getMoney1();
                    player.sendMessage(String.format("§7◈ 경험치 획득 +%s [ %s / %s ] : 크론 획득 +%s",
                            monsterData.getExp(), levelData.getExp(), levelData.getMexp(), money));
                }
            }
        }
    }

    public void onclick(InventoryClickEvent e) {
        if(e.getView().getTitle().contains("몬스터")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(6);
                MonsterData monsterData = monsterManager.get(name);
                if(monsterData != null) {
                    if(e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                        e.getWhoClicked().closeInventory();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                monsterManager.itemopen((Player) e.getWhoClicked(), name);
                            }
                        }.runTaskLater(yunahSever, 5);
                    } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                        monsterManager.remove(name);
                        e.getWhoClicked().sendMessage("§c몬스터§f : " + name + "을 몬스터 리스트에서 삭제 했습니다");
                    }
                }
            }
        }
    }

    public boolean invFull(Player p) {
        if (p.getInventory().contains(Material.AIR)) {
            return false;
        } else {
            return true;
        }
    }
}
