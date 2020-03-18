package kr.yunah.yunahsever.serverdata.datalist.player;

import com.google.common.collect.ImmutableMap;
import kr.yunah.yunahsever.lib.enumlist.Tribe;
import lombok.Data;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;
import java.util.UUID;

@Data
@SerializableAs("YunahServer")
public class YunahPlayer implements ConfigurationSerializable {
    private final UUID id;
    private Tribe tribe;
    private int Money;

    private int Cash;
    private int SafeMoney;
    private Location location;
    private final LevelData levelData;

    private final Stat StatData;
    private final ValueData valueData;
    private final SafeItem safeItem;
    private String guildName;

    public YunahPlayer(UUID id, Tribe tribe, int money, int cash, int SafeMoney, Location location, LevelData levelData1,
                       Stat stat, ValueData valueData, SafeItem safeItem1, String guildName) {
        this.id = id;
        this.tribe = tribe;
        this.Money = money;
        this.Cash = cash;
        this.SafeMoney = SafeMoney;
        this.location = location;
        this.levelData = levelData1;
        this.StatData = stat;
        this.valueData = valueData;
        this.safeItem = safeItem1;
        this.guildName = guildName;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("uuid", id.toString())
                .put("Tribe", tribe.name())
                .put("Player", getName())
                .put("Money", getMoney())
                .put("Cash", getCash())
                .put("SafeMoney", getSafeMoney())
                .put("Location", getLocation())
                .put("level", levelData)
                .put("Stat", StatData)
                .put("Value", valueData)
                .put("ItemSafe", safeItem)
                .put("GuildName", getGuildName())
                .build();
    }

    public static YunahPlayer deserialize(Map<String, Object> map) {
        return new YunahPlayer(
                UUID.fromString(map.get("uuid").toString()),
                Tribe.valueOf(map.get("Tribe").toString()),
                Integer.parseInt(map.get("Money").toString()),
                Integer.parseInt(map.get("Cash").toString()),
                Integer.parseInt(map.get("SafeMoney").toString()),
                (Location) map.get("Location"),
                (LevelData) map.get("level"),
                (Stat) map.get("Stat"),
                (ValueData) map.get("Value"),
                (SafeItem) map.get("ItemSafe"),
                (String) map.get("GuildName")
        );
    }

    // get value

    public String getName() {
        return Bukkit.getOfflinePlayer(id).getName();
    }

    // add value

    public void addMoney(int i) {
        this.Money += i;
    }

    public void addSafeMoney(int i) {
        this.SafeMoney += i;
    }

    public void addCash(int i) {
        this.Cash += i;
    }

    // sub value

    public void subMoney(int i) {
        this.Money -= i;
    }

    public void subSafeMoney(int i) {
        this.SafeMoney -= i;
    }

    public void subCash(int i) {
        this.Cash -= i;
    }

    //
}
