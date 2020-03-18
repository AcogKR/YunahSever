package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.enumlist.Tribe;
import kr.yunah.yunahsever.serverdata.datalist.player.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class PlayerManager implements Listener {
    // 이제 map 값을 저장해보세요
    public final Map<UUID, YunahPlayer> map = new HashMap<>();
    public YunahSever yunahSever;


    public YunahPlayer get(UUID id) {
        return map.computeIfAbsent(id, id1 ->
                new YunahPlayer(
                        id1, Tribe.UNDEFINED, 0, 0, 0,
                        new Location(Bukkit.getWorld("world"), (double) 3, (double) 116, (double) -208),
                        new LevelData(1, 0, 300),
                        new Stat(0, 0, 0, 0, 0,0,0),
                        new ValueData(true, true),
                        new SafeItem(new HashMap<>()), ""
                ));
    }

    public PlayerManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }

    public void save() {
        for(Map.Entry<UUID, YunahPlayer> entry : map.entrySet()) {
            YamlConfiguration config = new YamlConfiguration();
            File file = new File(yunahSever.getDataFolder() + "/PlayerData/", entry.getKey() + ".yml");
            ConfigurationSection playersSection = new MemoryConfiguration();
            file.getParentFile().mkdirs();
            playersSection.set(entry.getKey().toString(), entry.getValue());
            config.set("PlayerData", playersSection);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        File[] files = new File(yunahSever.getDataFolder() + "/PlayerData/").listFiles();
        if (files == null) { return; }
        for (File file : files) {
            YamlConfiguration config = loadConfiguration(file);
            ConfigurationSection playersSection = config.getConfigurationSection("PlayerData");
            if(playersSection == null) { return; }
            for (String id : playersSection.getKeys(false)) {
                Object object = playersSection.get(id);
                if (object instanceof YunahPlayer) {
                    YunahPlayer yunahPlayer = (YunahPlayer) object;
                    UUID uuid = UUID.fromString(id);
                    map.put(uuid, yunahPlayer);
                }
            }
        }
    }

    public void Levelbar(Player player) {
        if(player.getGameMode().equals(GameMode.SURVIVAL)) {
            YunahPlayer yunahPlayer = get(player.getUniqueId());
            LevelData levelData = yunahPlayer.getLevelData();
            double per = Math.round(((double) levelData.getExp() / levelData.getMexp() * 100));
            player.sendActionBar(String.format("§c체력 : %s   §eLv.%s %s%%   §b마나 : 20", Math.round(player.getHealth()), levelData.getLevel(), per));
        }
    }

    public void levelCheck(Player player) {
        YunahPlayer yunahPlayer = get(player.getUniqueId());
        LevelData levelData = yunahPlayer.getLevelData();
        Stat stat = yunahPlayer.getStatData();

        if(levelData.getExp() >= levelData.getMexp()) {
            if(levelData.getLevel() > 1 && levelData.getLevel() < 30) {

            } else if(levelData.getLevel() > 30 && levelData.getLevel() < 60) {

            } else if(levelData.getLevel() > 60 && levelData.getLevel() < 90) {

            } else if(levelData.getLevel() > 90 && levelData.getLevel() < 110) {

            } else if(levelData.getLevel() > 110 && levelData.getLevel() < 115) {
                if(levelData.getLevel() == 114) {

                }
            }
        }
    }


    public void levelup(Player player, int exp) {
        YunahPlayer yunahPlayer = get(player.getUniqueId());
        LevelData levelData = yunahPlayer.getLevelData();
        Stat stat = yunahPlayer.getStatData();
        stat.addStat(4);
        levelData.setExp(0);
        levelData.setMexp(exp);
        levelData.addLevel(1);
        player.sendTitle("§6Level Up!!", String.format("§7레벨이 변경되었습니다 %s -> %s", levelData.getLevel(), levelData.getLevel() - 1));
    }
}
