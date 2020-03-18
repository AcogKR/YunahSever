package kr.yunah.yunahsever.serverdata.datalist.item;

import com.google.common.collect.ImmutableMap;
import kr.yunah.yunahsever.lib.enumlist.Ability;
import kr.yunah.yunahsever.lib.enumlist.Rating;
import kr.yunah.yunahsever.lib.enumlist.item.Weapon;
import lombok.Data;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class WeaponData implements ConfigurationSerializable {

    private final String name;
    private Rating rating;
    private Weapon weapon;
    private int damage;
    private int price;
    private final EnumMap<Ability, AtomicInteger> abilities;

    public WeaponData(String name, Rating rating, Weapon weapon, int damage, int price, EnumMap<Ability, AtomicInteger> abilities) {
        this.name = name;
        this.rating = rating;
        this.weapon = weapon;
        this.damage = damage;
        this.price = price;
        this.abilities = abilities;
    }

    public WeaponData(String name, Rating rating, Weapon weapon, int damage, int price) {
        this(name, rating, weapon, damage, price, new EnumMap<>(Ability.class));
    }

    public static WeaponData deserialize(Map<String, Object> map) {
        EnumMap<Ability, AtomicInteger> abilities = new EnumMap<>(Ability.class);
        Object abilitiesObject = map.get("abilities");
        if (abilitiesObject instanceof Map) {
            Map<?, ?> abilitiesMap = (Map<?, ?>) abilitiesObject;
            for (Map.Entry<?, ?> entry : abilitiesMap.entrySet()) {
                try {
                    Ability ability = Ability.valueOf(entry.getKey().toString());
                    Number value = (Number) entry.getValue();
                    abilities.put(ability, new AtomicInteger(value.intValue()));
                } catch (Exception ignored) {
                    // 개무시
                }
            }
        }
        return new WeaponData(
                (String) map.get("Name"),
                (Rating) map.get("Class"),
                (Weapon) map.get("Weapon"),
                ((Number) map.get("Damage")).intValue(),
                ((Number) map.get("Price")).intValue(),
                abilities
        );
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> serialized = new HashMap<>();
        for (Map.Entry<Ability, AtomicInteger> entry : abilities.entrySet()) {
            serialized.put(entry.getKey().name(), entry.getValue().get());
        }
        return ImmutableMap.<String, Object>builder()
                .put("Name", getName())
                .put("Class", getRating().toString())
                .put("Weapon", getWeapon().toString())
                .put("Damage", getDamage())
                .put("Price", getPrice())
                .put("Abilities", serialized)
                .build();
    }
}
