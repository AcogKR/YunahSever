package kr.yunah.yunahsever.serverdata.datalist.player;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.inventory.ItemStack;
import java.util.Map;

@SerializableAs("SafeList")
public class SafeItem implements ConfigurationSerializable {

    private Map<Integer, ItemStack> map;

    public SafeItem(Map<Integer, ItemStack> map ) {
        this.map = map;
    }

    public void setlist(Map<Integer, ItemStack> map1 ) {
        this.map = map1;
    }

    public Map<Integer, ItemStack> getlist() { return map; }

    public void put(int i, ItemStack item) {
        this.map.put(i, item);
    }

    public void clearlist() { this.map.clear(); }


    public static SafeItem deserialize(Map<String, Object> map) {
        return new SafeItem(
                (Map<Integer, ItemStack>) map.get("ItemSafe")
        );
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("ItemSafe", map)
                .build();
    }
}
