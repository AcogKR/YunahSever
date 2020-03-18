package kr.yunah.yunahsever.serverdata.datalist;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;
import java.util.UUID;


@Data
@SerializableAs("GuildPlayer")
public class GuildPlayerData implements ConfigurationSerializable {

    private final UUID id;
    private int contribution;

    public GuildPlayerData(UUID uuid, int contribution) {
        this.id = uuid;
        this.contribution = contribution;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("uuid", getId())
                .put("Contribution", getContribution())
                .build();
    }
    
    public static GuildPlayerData deserialize(Map<String, Object> map) {
        return new GuildPlayerData(
                UUID.fromString(map.get("uuid").toString()),
                Integer.parseInt(map.get("Contribution").toString())
        );
    }


    // contribution method
    public void addContribution(int i) {
        this.contribution += i;
    }

    public void subContribution(int i) {
        this.contribution -= i;
    }
}
