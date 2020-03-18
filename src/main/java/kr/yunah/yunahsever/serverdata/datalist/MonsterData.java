package kr.yunah.yunahsever.serverdata.datalist;

import com.google.common.collect.ImmutableMap;
import kr.yunah.yunahsever.serverdata.datalist.player.SafeItem;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Map;

@Data
public class MonsterData implements ConfigurationSerializable {

    private int exp;
    private int money1;
    private int money2;
    private Map<Integer, ItemStack> items;

    public MonsterData(int exp, int money1, int money2, Map<Integer, ItemStack> list) {
        this.exp = exp;
        this.money1 = money1;
        this.money2 = money2;
        this.items = list;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("Exp", getExp())
                .put("Money1", getMoney1())
                .put("Money2", getMoney2())
                .put("Items", getItems())
                .build();
    }

    public static MonsterData deserialize(Map<String, Object> map) {
        return new MonsterData(
                (Integer) map.get("Exp"),
                (Integer) map.get("Money1"),
                (Integer) map.get("Money2"),
                (Map<Integer, ItemStack>) map.get("Items")
        );
    }
}
