package kr.yunah.yunahsever;


import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import kr.yunah.yunahsever.methodlist.eventmethod.players.CallPlayerEventListener;
import kr.yunah.yunahsever.methodlist.eventmethod.players.SafeItemEventListener;
import kr.yunah.yunahsever.methodlist.guild.GuildCommand;
import kr.yunah.yunahsever.methodlist.server.item.ItemManagerCommand;
import kr.yunah.yunahsever.methodlist.server.transport.TransportCommand;
import kr.yunah.yunahsever.methodlist.server.transport.TransportEventListener;
import kr.yunah.yunahsever.methodlist.whisper.*;
import kr.yunah.yunahsever.methodlist.server.monster.MonsterCommand;
import kr.yunah.yunahsever.methodlist.server.monster.MonsterEventListener;
import kr.yunah.yunahsever.methodlist.playermoney.MoneyCommand;
import kr.yunah.yunahsever.methodlist.playermoney.MoneyEventListener;
import kr.yunah.yunahsever.methodlist.eventmethod.players.PrivateSettingEventListener;
import kr.yunah.yunahsever.methodlist.eventmethod.serverbace.EventListener;
import kr.yunah.yunahsever.methodlist.server.tutorial.ServerTutorialCommand;
import kr.yunah.yunahsever.methodlist.server.warpsystem.WarpCommand;
import kr.yunah.yunahsever.serverdata.*;
import kr.yunah.yunahsever.serverdata.datalist.GuildData;
import kr.yunah.yunahsever.serverdata.datalist.GuildPlayerData;
import kr.yunah.yunahsever.serverdata.datalist.MonsterData;
import kr.yunah.yunahsever.serverdata.datalist.WarpData;
import kr.yunah.yunahsever.serverdata.datalist.item.WeaponData;
import kr.yunah.yunahsever.serverdata.datalist.player.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Objects;
import java.util.logging.Level;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public final class YunahSever extends JavaPlugin {

    public final PlayerManager playerManager = new PlayerManager(this);
    public final ItemManager itemManager = new ItemManager(this);
    public final MonsterManager monsterManager = new MonsterManager(this);
    public final WarpManager warpManager = new WarpManager(this);
    public final WhisperCommand whisperCommand = new WhisperCommand();
    public final TransportManager transportManager = new TransportManager(this);
    public final GuildManager guildManager = new GuildManager(this);

    @Override
    public void onLoad() {
        Arrays.asList(
                YunahPlayer.class, Stat.class, LevelData.class, SafeItem.class,
                ValueData.class, MonsterData.class, WarpData.class, GuildData.class,
                GuildPlayerData.class, WeaponData.class
        ).forEach(ConfigurationSerialization::registerClass);
    }

    @Override
    public void onEnable() {
        playerManager.load();
        monsterManager.load();
        warpManager.load();
        transportManager.save();
        Arrays.asList(
                playerManager, warpManager, new SafeItemEventListener(playerManager),
                new MonsterEventListener(monsterManager, playerManager, this), new MoneyEventListener(playerManager),
                new CallPlayerEventListener(playerManager), new PrivateSettingEventListener(playerManager),
                new EventListener(playerManager, this),
                new WhisperEventListener(whisperCommand), new TransportEventListener(transportManager)
        ).forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));

        Objects.requireNonNull(getCommand("몬스터")).setExecutor(new MonsterCommand(monsterManager, this));
        getCommand("Warp").setExecutor(new WarpCommand(warpManager));
        getCommand("튜토리얼").setExecutor(new ServerTutorialCommand(this, playerManager));
        getCommand("돈").setExecutor(new MoneyCommand(playerManager));
        getCommand("귓속말").setExecutor(whisperCommand);
        getCommand("이동수단").setExecutor(new TransportCommand(transportManager));
        getCommand("아이템매니저").setExecutor(new ItemManagerCommand(this, itemManager));
        getCommand("길드").setExecutor(new GuildCommand(guildManager, playerManager, this));

        new BukkitRunnable() {
            @Override
            public void run() {
                ServerTick();
            }
        }.runTaskTimer(this, 5L, 5L);
    }

    @Override
    public void onDisable() {
        playerManager.save();
        monsterManager.save();
        warpManager.save();
        transportManager.save();
        guildManager.save();
        itemManager.save();
    }

    public void ServerTick() {
        // 레벨 나타내는 구문
        Bukkit.getOnlinePlayers().forEach((player) -> {
            playerManager.Levelbar(player);
            playerManager.levelCheck(player);
            TablistReload(player);
            Tutorial();
        });



    }

    public void Tutorial() {
        File file = new File(getDataFolder(), "config.yml");
        YamlConfiguration config = loadConfiguration(file);
        Location location = (Location) config.get("Tutorial");

        if(location == null) {
            Bukkit.getOnlinePlayers().forEach((player) -> {
                player.sendMessage("§9System §f: 튜토리얼 이동장소가 저장되지 않았습니다");
            });
        }
    }


    public void TablistReload(Player player) {
        PacketContainer packet = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.PLAYER_LIST_HEADER_FOOTER);
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        LevelData levelData = yunahPlayer.getLevelData();

        SimpleDateFormat time = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
        Calendar time1 = Calendar.getInstance();

        packet.getChatComponents().write(0, WrappedChatComponent.fromText(
                "§f                                                                                  §f" + "\n" +
                        "§cYunah RPG 1.14.4" + "\n" +
                        ""
        ));
        int per = (int) Math.round(((double) levelData.getExp() / levelData.getMexp() * 100));
        packet.getChatComponents().write(1, WrappedChatComponent.fromText(
                "§f                                                                                  §f" + "\n" +
                        String.format("§7Level : %s  [ %s / %s [ %s%% ]", levelData.getLevel(), levelData.getExp(), levelData.getMexp(), per) + "\n" +
                        String.format("Player : %s  Time : %s", player.getName(), time.format(time1.getTime())) + "\n" +
                        ""));

        try {
            ProtocolLibrary.getProtocolManager().sendServerPacket(player, packet);
        } catch (InvocationTargetException ex) {
            Bukkit.getLogger().log(Level.WARNING, ex, () -> "Fucking ProtocolLib");
        }
    }

}
