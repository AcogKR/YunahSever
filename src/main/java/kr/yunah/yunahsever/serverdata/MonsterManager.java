package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.datalist.MonsterData;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MonsterManager{

    @Getter
    public final Map<String, MonsterData> Monsters = new HashMap<>();
    public YunahSever yunahSever;

    public MonsterManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }


    public Map<String, MonsterData> MonsterList() {
        return Monsters;
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        File file = new File(yunahSever.getDataFolder(), "MonsterList.yml");
        ConfigurationSection MonsterSection = new MemoryConfiguration();
        Monsters.forEach(MonsterSection::set);
        config.set("Monsters", MonsterSection);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(yunahSever.getDataFolder(), "MonsterList.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection MonsterSection = config.getConfigurationSection("Monsters");
        if (MonsterSection != null) {
            Monsters.clear();
            for (String id : MonsterSection.getKeys(false)) {
                Object object = MonsterSection.get(id);
                if (object instanceof MonsterData) {
                    Monsters.put(id, (MonsterData) object);
                }
            }
        } else {
            return;
        }
    }

    public void create(String name) {
        Monsters.put(name, new MonsterData(1, 1, 1, new HashMap<>()));
    }

    public MonsterData get(String name) {
        return Monsters.computeIfAbsent(name, name1 ->
                new MonsterData(
                        0, 0, 0, new HashMap<>()
                ));
    }

    public boolean isContain(String name) {
        for (Map.Entry<String, MonsterData> entry : Monsters.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public void itemopen(Player player, String name) {
        Inventory inv = Bukkit.createInventory(null, 9, "아이템설정 : " + name);
        MonsterData monsterData = get(name);
        for(Map.Entry<Integer, ItemStack> map : monsterData.getItems().entrySet()) {
            inv.setItem(map.getKey(), map.getValue());
        }
        player.openInventory(inv);
    }

    public void remove(String name) {
        if(isContain(name)) {
            Monsters.remove(name);
        }
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 6 * 9, "§c몬스터 목록");
        int index = 0;
        for (Map.Entry<String, MonsterData> entry : getMonsters().entrySet()) {
            MonsterData monsterData = entry.getValue();
            inv.setItem(index, new ItemBuilder(Material.ZOMBIE_SPAWN_EGG).display("§cM§f " + entry.getKey())
                    .lore("",
                            String.format("§7 >> 크론 : %s ~ %s", monsterData.getMoney1(), monsterData.getMoney2()),
                            String.format("§7 >> 경험치 : %s", monsterData.getExp()),
                            "",
                            "§7 >> 더블클릭시 몬스터 아이템을 설정하는 페이지를 오픈합니다 ",
                            "§7 >> 쉬프트 좌클릭시 몬스터를 삭제합니다 ", ""
                    )
                    .build());
            index++;
        }

        player.openInventory(inv);
    }
    //Event
}
