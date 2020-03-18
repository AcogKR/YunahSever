package kr.yunah.yunahsever.serverdata.datalist;

import com.comphenix.protocol.PacketType;
import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
@SerializableAs("GuildPlayer")
public class GuildData implements ConfigurationSerializable {

    private final String name;
    private String notice;
    private UUID master;
    private UUID manager;
    private int guildLevel;
    private int guildContribution;
    private int personnel;
    private int guildMoney;
    private Map<Player, GuildPlayerData> players;

    public GuildData(String name, String notice, UUID master, UUID manager, int guildLevel, int guildContribution,
                     int personnel, int guildMoney, Map<Player, GuildPlayerData> players) {
        this.name = name;
        this.notice = notice;
        this.master = master;
        this.manager = manager;
        this.guildLevel = guildLevel;
        this.guildContribution = guildContribution;
        this.personnel = personnel;
        this.guildMoney = guildMoney;
        this.players = players;
    }

    public GuildData(String name, UUID mater, int guildLevel, int guildContribution, int personnel, int guildMoney) {
        this(name, "", mater, null, guildLevel, guildContribution, personnel, guildMoney, new HashMap<>());
    }

    public void addPlayer(Player player) {
        if(!players.containsKey(player)) {
            players.put(player, new GuildPlayerData(player.getUniqueId(), 0));
        }
    }

    public void remPlayer(Player player) {
        if(players.containsKey(player)) {
            players.remove(player);
        }
    }


    public boolean guildPlayerMaster(Player player) {
        if(player.getUniqueId().equals(master)) {
            return true;
        }
        return false;
    }

    public boolean guildPlayerManager(Player player) {
        if(player.getUniqueId().equals(manager)) {
            return true;
        }
        return false;
    }

    public void addGuildMoney(int money) {
        guildMoney += money;
    }

    public void subGuildMoney(int money) {
        guildMoney -= money;
    }

    public static GuildData deserialize(Map<String, Object> map) {
        return new GuildData(
                (String) map.get("guildName"),
                (String) map.get("guildNotice"),
                UUID.fromString(map.get("guildMaster").toString()),
                UUID.fromString(map.get("guildManager").toString()),
                Integer.parseInt(map.get("guildLevel").toString()),
                Integer.parseInt(map.get("guildContribution").toString()),
                Integer.parseInt(map.get("guildPersonnel").toString()),
                Integer.parseInt(map.get("guildMoney").toString()),
                (Map<Player, GuildPlayerData>) map.get("guildPlayers")
        );
    }


    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("guildName", getName())
                .put("guildNotice", getNotice())
                .put("guildMaster", getMaster().toString())
                .put("guildManager", getManager().toString())
                .put("guildLevel", getGuildLevel())
                .put("guildContribution", getGuildContribution())
                .put("guildPersonnel", getPersonnel())
                .put("guildMoney", getGuildMoney())
                .put("guildPlayers", getPlayers())
                .build();
    }
}
