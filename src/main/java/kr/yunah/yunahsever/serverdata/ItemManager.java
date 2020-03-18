package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.enumlist.Rating;
import kr.yunah.yunahsever.lib.enumlist.item.Weapon;
import kr.yunah.yunahsever.serverdata.datalist.item.ItemMethod;
import kr.yunah.yunahsever.serverdata.datalist.item.WeaponData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static kr.yunah.yunahsever.lib.enumlist.Ability.*;
import static kr.yunah.yunahsever.lib.enumlist.Ability.HEALTH_MIN;

public class ItemManager {

    private final YunahSever yunahSever;
    private final ItemMethod itemMethod = new ItemMethod();
    private final Map<String, WeaponData> weaponDataMap = new HashMap<>();

    public ItemManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }

    // get weapon method
    public WeaponData getWeapon(String name) {
        return weaponDataMap.computeIfAbsent(name, name1 ->
                new WeaponData(
                        name1, Rating.NORMAL, Weapon.UNDEFINED, 1, 1
                ));
    }

//    public void createWeapon(String name) {
//        weaponDataMap.put(
//                name, new WeaponData(name, Rating.NORMAL, Weapon.UNDEFINED,
//                        1, 1, new EnumMap<Ability, AtomicInteger>(Ability.class));
//    }

    public void removeWeapon(String name) {
        weaponDataMap.remove(name);
    }

    public boolean isWeapon(String name) {
        if(weaponDataMap.containsKey(name)) {
            return true;
        } else {
            return false;
        }
    }

    public void weaponinfo(Player player, String name) {
        WeaponData weaponData = getWeapon(name);
        player.sendMessage("§7 >> 아이템 정보 : " + name);
        player.sendMessage("§f");
        player.sendMessage("§7 >> §e제품등급 : " + itemMethod.rating(weaponData.getRating()));
        player.sendMessage("§7 >> 요구 레벨 : " + weaponData.getAbilities().get(LEVEL_MIN).toString());
        player.sendMessage("§7 >> §9소켓 수 : " + weaponData.getAbilities().get(SOCKET));
        player.sendMessage("§7 >> 공격력 : " + weaponData.getDamage());
        player.sendMessage("§7 >> 종족 : " + itemMethod.weapon(weaponData.getWeapon()));
        player.sendMessage("§f");
        player.sendMessage("§7 >> §b요구 힘 : + " + weaponData.getAbilities().get(STRONG_MIN));
        player.sendMessage("§7 >> §b요구 체력 : + " + weaponData.getAbilities().get(HEALTH_MIN));
        player.sendMessage("§7 >> §b요구 순발력 : + " + weaponData.getAbilities().get(QUICKNESS_MIN));
        player.sendMessage("§7 >> §b요구 민첩성 : + " + weaponData.getAbilities().get(AGILITY_MIN));
        player.sendMessage("§7 >> §b요구 정신력 : + " + weaponData.getAbilities().get(MENTALITY_MIN));
        player.sendMessage("§7 >> §b요구 지력 : + " + weaponData.getAbilities().get(INTELLECT_MIN));
        player.sendMessage("§f");
        player.sendMessage("§7 >> §a공격력 : + " + weaponData.getAbilities().get(DAMAGE));
        player.sendMessage("§7 >> §a힘 : + " + weaponData.getAbilities().get(STRONG_MAX));
        player.sendMessage("§7 >> §a체력 : + " + weaponData.getAbilities().get(HEALTH_MAX));
        player.sendMessage("§7 >> §a순발력 : + " + weaponData.getAbilities().get(QUICKNESS_MAX));
        player.sendMessage("§7 >> §a민첩성 : + " + weaponData.getAbilities().get(AGILITY_MAX));
        player.sendMessage("§7 >> §a정신력 : + " + weaponData.getAbilities().get(MENTALITY_MAX));
        player.sendMessage("§7 >> §a지력 : + " + weaponData.getAbilities().get(INTELLECT_MAX));
        player.sendMessage("§7");
        player.sendMessage("§7 >> §a크리티컬 : + " + weaponData.getAbilities().get(CRITICAL) + "%");
        player.sendMessage("§7 >> §a생명력흡수 : + " + weaponData.getAbilities().get(VAMPIRE) + "%");
        player.sendMessage("§7 >> §a근거리공격력 : + " + weaponData.getAbilities().get(MELEE_ATTACK) + "%");
        player.sendMessage("§7 >> §a원거리공격력 : + " + weaponData.getAbilities().get(RANGED_ATTACK) + "%");
        player.sendMessage("§7 >> §a마법공격력 : + " + weaponData.getAbilities().get(MAGIC_ATTACK) + "%");
        player.sendMessage("§7 >> §9판매 가격 : " + weaponData.getPrice());

    }
    public void weaponItem(Player player, String name) {
        WeaponData weaponData = getWeapon(name);
    }

    // config method+

    public void save() {
        // Weapon save code
        for(Map.Entry<String, WeaponData> entry : weaponDataMap.entrySet()) {
            YamlConfiguration config = new YamlConfiguration();
            File file = new File(yunahSever.getDataFolder() + "/ItemList/Weapon/", entry.getKey() + ".yml");
            ConfigurationSection Section = new MemoryConfiguration();
            file.getParentFile().mkdirs();
            Section.set(entry.getKey(), entry.getValue());
            config.set("WeaponData", Section);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {

    }
}
