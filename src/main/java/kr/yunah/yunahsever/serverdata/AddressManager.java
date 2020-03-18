package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.datalist.MonsterData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddressManager {
    public final Map<UUID, String> map = new HashMap<>();
    private final YunahSever yunahSever;

    public AddressManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }

    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        File file = new File(yunahSever.getDataFolder(), "AddressManager.yml");
        ConfigurationSection AddressSection = new MemoryConfiguration();
        for (Map.Entry<UUID, String> entry : map.entrySet()) {
            AddressSection.set(entry.getKey().toString(), entry.getValue());
        }
        config.set("AddressManager", AddressSection);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(yunahSever.getDataFolder(), "AddressManager.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection MonsterSection = config.getConfigurationSection("AddressManager");
        if (MonsterSection != null) {
            map.clear();
            for (String id : MonsterSection.getKeys(false)) {
                Object object = MonsterSection.get(id);
                if (object instanceof MonsterData) {
                    //map.put(id, (MonsterData) object);
                }
            }
        } else {
            return;
        }
    }
}

/*
- user
  - ip
  - true/false
  - cnt
 */