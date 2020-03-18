package kr.yunah.yunahsever.serverdata.datalist;


import com.google.common.collect.ImmutableMap;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.Map;

@Data
public class WarpData implements ConfigurationSerializable {

    private final String warpname;
    private final Location location;
    private boolean bind;


    public WarpData(String warpname, Location location, Boolean bind) {
        this.warpname = warpname;
        this.location = location;
        this.bind = bind;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("WarpName", warpname)
                .put("Location", location)
                .put("Bind", String.valueOf(bind))
                .build();
    }

    public static WarpData deserialize(Map<String, Object> map) {
        return new WarpData(
                String.valueOf(map.get("WarpName")),
                (Location) map.get("Location"),
                Boolean.getBoolean(String.valueOf(map.get("Bind")))
        );
    }
}
