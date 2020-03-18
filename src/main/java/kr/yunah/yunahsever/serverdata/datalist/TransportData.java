package kr.yunah.yunahsever.serverdata.datalist;

import com.google.common.collect.ImmutableMap;
import kr.yunah.yunahsever.lib.enumlist.Rating;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

@Data
public class TransportData implements ConfigurationSerializable {

    private int speed;
    private Rating rating;

    public TransportData(int speed, Rating rating) {
        this.speed = speed;
        this.rating = rating;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("Speed", getSpeed())
                .put("Class", getRating().toString())
                .build();
    }

    public static TransportData deserialize(Map<String, Object> map) {
        return new TransportData(
                (Integer) map.get("Speed"),
                (Rating) map.get("Class")
        );
    }
}
