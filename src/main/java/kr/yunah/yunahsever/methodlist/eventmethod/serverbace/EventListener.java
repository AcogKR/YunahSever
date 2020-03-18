package kr.yunah.yunahsever.methodlist.eventmethod.serverbace;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.enumlist.Tribe;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.LevelData;
import kr.yunah.yunahsever.serverdata.datalist.player.ValueData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.scheduler.BukkitRunnable;


public class EventListener implements Listener {

    private final PlayerManager playerManager;
    private final YunahSever yunahSever;

    public EventListener(PlayerManager playerManager, YunahSever yunahSever) {
        this.playerManager = playerManager;
        this.yunahSever = yunahSever;
    }

    @EventHandler
    public void onjoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        YunahPlayer yunahPlayer = playerManager.get(e.getPlayer().getUniqueId());
        e.getPlayer().teleport(yunahPlayer.getLocation());
    }

    @EventHandler
    public void respawn(PlayerRespawnEvent e) {
        Player player = e.getPlayer();
        YunahPlayer yunahPlayer = playerManager.get(e.getPlayer().getUniqueId());
        player.sendTitle("", "§7부활하셨습니다");
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        YunahPlayer yunahSever = playerManager.get(e.getPlayer().getUniqueId());
        LevelData levelData = yunahSever.getLevelData();
        System.out.println(levelData.getLevel());
        if (levelData.getLevel() > 2) {
            playerManager.get(e.getPlayer().getUniqueId());
        }
    }


    @EventHandler
    public void playerJoin(PlayerJoinEvent e) {
        e.setJoinMessage("");
        Player player1 = e.getPlayer();
        for (Player player : Bukkit.getOnlinePlayers()) {
            YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
            ValueData valueData = yunahPlayer.getValueData();
            e.getPlayer().teleport(yunahPlayer.getLocation());
            if (valueData.isJoinMessage()) {
                player.sendActionBar(String.format("§a! §f%s님이 서버에 접속했습니다 : %s :", e.getPlayer().getName(), Bukkit.getOnlinePlayers().size()));
            }
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                playerTitleMessage(player1);
                player1.sendMessage("asd");
            }
        }.runTaskLater(yunahSever, 60);
    }

    @EventHandler
    public void playerquit(PlayerQuitEvent e) {
        e.setQuitMessage("");
        for (Player player : Bukkit.getOnlinePlayers()) {
            YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
            ValueData valueData = yunahPlayer.getValueData();
            if (valueData.isJoinMessage()) {
                player.sendActionBar(String.format("§c! §f%s님이 서버에서 퇴장했습니다 : %s : ", e.getPlayer().getName(), Bukkit.getOnlinePlayers().size() - 1));
            }
        }
    }

    @EventHandler
    public void playerchat(PlayerChatEvent e) {
        Player player = e.getPlayer();
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        LevelData levelData = yunahPlayer.getLevelData();

        if (e.getPlayer().isOp()) {
            e.setFormat(String.format("§c운영자§f <%s> %s", player.getName(), e.getMessage()));
            return;
        }
        if (yunahPlayer.getTribe().equals(Tribe.UNDEFINED)) {
            e.setFormat(String.format("§7튜토리얼§f <%s> %s", player.getName(), e.getMessage()));
            e.getPlayer().sendActionBar("§7튜토리얼을 진행해주세요 ! >> /튜토리얼");
        } else {
            if (yunahPlayer.getTribe().equals(Tribe.DARK_ELF) || yunahPlayer.getTribe().equals(Tribe.WARLOCK)
                    || yunahPlayer.getTribe().equals(Tribe.WIZARD) || yunahPlayer.getTribe().equals(Tribe.HALF_ELF)
                    || yunahPlayer.getTribe().equals(Tribe.SCOUT) || yunahPlayer.getTribe().equals(Tribe.RANGER)) {
                e.setFormat(String.format("§6[%s.Lv]§f <§b%s§f> %s", levelData.getLevel(), player.getName(), e.getMessage()));
            } else if (yunahPlayer.getTribe().equals(Tribe.DRAGON_KNIGHT) || yunahPlayer.getTribe().equals(Tribe.DRAGON_SAGE) ||
                    yunahPlayer.getTribe().equals(Tribe.DEKAN) || yunahPlayer.getTribe().equals(Tribe.DHAN)
                    || yunahPlayer.getTribe().equals(Tribe.AVENGER) || yunahPlayer.getTribe().equals(Tribe.PREDATOR)) {
                e.setFormat(String.format("§6[%s.Lv]§f <§c%s§f> %s", levelData.getLevel(), player.getName(), e.getMessage()));
            } else if (yunahPlayer.getTribe().equals(Tribe.ELF) || yunahPlayer.getTribe().equals(Tribe.PRIEST)
                    || yunahPlayer.getTribe().equals(Tribe.TEMPLAR) || yunahPlayer.getTribe().equals(Tribe.HUMAN)
                    || yunahPlayer.getTribe().equals(Tribe.GUARDIAN) || yunahPlayer.getTribe().equals(Tribe.DEFENDER)) {
                e.setFormat(String.format("§6[%s.Lv]§f <§e%s§f> %s", levelData.getLevel(), player.getName(), e.getMessage()));
            }
        }
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.playSound(players.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 10, (float) 5);
        }
    }

    @EventHandler
    public void onPing(ServerListPingEvent e) {
        e.setMotd("§f                    §cYunah RPG 1.14.4§f     §c" + "\n" +
                "§7       새로운 방식의 알피지 서버를 원한다면 바로 접속하세요");
        e.setMaxPlayers(100);
    }

    public void playerTitleMessage(Player player) {
        new BukkitRunnable() {
            int index = 1;
            @Override
            public void run() {
                switch (index) {

                }
            }
        }.runTaskTimer(yunahSever, 0L, 4L);
    }
}