package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.datalist.GuildData;
import kr.yunah.yunahsever.serverdata.datalist.item.WeaponData;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuildManager {
    private Map<String, UUID> invite = new HashMap<>();
    private final Map<String, GuildData> guildDataMap = new HashMap<>();
    private final YunahSever yunahSever;

    public GuildManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }

    public Map<String, UUID> getInvite() {
        return invite;
    }

    public void inviteQuit(Player player) {
        if(invite.containsValue(player.getUniqueId())) {
            invite.remove(player.getUniqueId());
        }
    }


    public void inviteJoin(Player player, String name) {
        if(!invite.containsValue(player.getUniqueId())) {
            invite.put(name, player.getUniqueId());
        }
    }
    public void createGuild(String name, Player player) {
        if(!guildDataMap.containsKey(name)) {
            guildDataMap.put(name,
                    new GuildData(name, player.getUniqueId(), 1, 0, 10,0));
            GuildData guildData = guildDataMap.get(name);
            guildData.addPlayer(player);
        }
    }

    public void removeGuild(String name) {
        if(guildDataMap.containsKey(name)) {
            guildDataMap.remove(name);
        }
    }

    public GuildData getGuild(String name) {
        return guildDataMap.computeIfAbsent(name, name1 ->
                new GuildData(name1, UUID.randomUUID(), 1, 0, 10,0)
        );
    }



    // get boolean
    public boolean isPlayerContain(Player player) {
        for (Map.Entry<String, GuildData> entry : guildDataMap.entrySet()) {
            GuildData guildData = entry.getValue();
            if(guildData.getPlayers().containsKey(player)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPlayerMaster(Player player) {
        for (Map.Entry<String, GuildData> entry : guildDataMap.entrySet()) {
            GuildData guildData = entry.getValue();
            if(guildData.getPlayers().containsKey(player)) {
                if(guildData.guildPlayerMaster(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean guildNameCheck(String name) {
        for(Map.Entry<String, GuildData> entry : guildDataMap.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
    public boolean isPlayerManager(Player player) {
        for (Map.Entry<String, GuildData> entry : guildDataMap.entrySet()) {
            GuildData guildData = entry.getValue();
            if(guildData.getPlayers().containsKey(player)) {
                if(guildData.guildPlayerManager(player)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Inventory guildDataInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6*9, "길드 정보");


        return inventory;
    }

    public void save() {
        // Weapon save code
        for(Map.Entry<String, GuildData> entry : guildDataMap.entrySet()) {
            YamlConfiguration config = new YamlConfiguration();
            File file = new File(yunahSever.getDataFolder() + "/GuildList/", entry.getKey() + ".yml");
            ConfigurationSection Section = new MemoryConfiguration();
            file.getParentFile().mkdirs();
            Section.set(entry.getKey(), entry.getValue());
            config.set("GuildData", Section);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
