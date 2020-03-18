package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.lib.enumlist.Rating;
import kr.yunah.yunahsever.serverdata.datalist.TransportData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class TransportManager {

    private final YunahSever yunahSever;
    public final Map<String, TransportData> maps = new HashMap<>();
    public final Map<UUID, UUID> players = new HashMap<>();

    public TransportManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }


    public void save() {
        YamlConfiguration config = new YamlConfiguration();
        File file = new File(yunahSever.getDataFolder(), "TransportList.yml");
        ConfigurationSection TransportSection = new MemoryConfiguration();
        maps.forEach(TransportSection::set);
        config.set("Transport", TransportSection);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void load() {
        File file = new File(yunahSever.getDataFolder(), "TransportList.yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection section = config.getConfigurationSection("Transport");
        if (section != null) {
            maps.clear();
            for (String id : section.getKeys(false)) {
                Object object = section.get(id);
                if (object instanceof TransportData) {
                    maps.put(id, (TransportData) object);
                }
            }
        }
    }

    public void create(String name) {
        maps.put(
                name, new TransportData(
                        100, Rating.NORMAL
                )
        );
    }

    public TransportData get(String name) {
        return maps.computeIfAbsent(name, name1 ->
                new TransportData(
                        100, Rating.NORMAL
                ));
    }

    public void remove(String name) {
        maps.remove(name);
    }

    public boolean iscontain(String name) {
        for (Map.Entry<String, TransportData> entry : maps.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 6*9, "이동수단 목록");
        int index = 0;
        for (Map.Entry<String, TransportData> entry : maps.entrySet()) {
            inv.setItem(index, new ItemBuilder(Material.HORSE_SPAWN_EGG)
                    .display("§e" + entry.getKey()).lore(
                            "",
                            "§7 >> 이동수단 속도 : +" + entry.getValue().getSpeed(),
                            "§7 >> 이동수단 등급 : " + color(entry.getValue().getRating().toString()) + entry.getValue().getRating(),
                            "",
                            "§7 >> 더블 클릭시 이동수단을 지급 받습니다 ",
                            "§7 >> 쉬프트 좌클릭시 이동수단을 삭제합니다  "
                            ).build());
            index++;
        }
        player.openInventory(inv);
    }

    public ItemStack item(String name) {
        TransportData transportData = get(name);
        return new ItemBuilder(Material.BOOK).display("§b탈것 §f: " + name).lore("",
                "§b >>§f 이것을 손에 들고 스왑키를 누를시 시간을 소모하고 말에 탑승합니다",
                "§7    ( 말 장비 , 가방은 추후에 추가 됩니다 ) ",
                "",
                String.format("§7 >> 말의 속도 : %s%%  말의 등급 : %s%s", transportData.getSpeed(), color(transportData.getRating().toString()), transportData.getRating()),
                "").build();
    }

    public void spawn(Player player, String name) {
        TransportData transportData = get(name);
        Horse e = (Horse) player.getWorld().spawnEntity(player.getLocation(), EntityType.HORSE);
        double ps = (player.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED)).getValue();
        if(transportData.getRating().equals(Rating.NORMAL)) {
            e.setCustomName("§8T §7" + name);
            e.setColor(Horse.Color.CREAMY);
            e.setStyle(Horse.Style.NONE);
        } else if(transportData.getRating().equals(Rating.EPIC)) {
            e.setCustomName("§bT §7" + name);
            e.setColor(Horse.Color.BROWN);
            e.setStyle(Horse.Style.NONE);
        } else if(transportData.getRating().equals(Rating.RARE)) {
            e.setCustomName("§eT §7" + name);
            e.setColor(Horse.Color.GRAY);
            e.setStyle(Horse.Style.BLACK_DOTS);
        } else if(transportData.getRating().equals(Rating.LEGEND)) {
            e.setCustomName("§6T §7" + name);
            e.setColor(Horse.Color.BLACK);
            e.setStyle(Horse.Style.BLACK_DOTS);
        } else if(transportData.getRating().equals(Rating.UNKNOWN)) {
            e.setCustomName("§5T §7" + name);
            e.setColor(Horse.Color.WHITE);
            e.setStyle(Horse.Style.BLACK_DOTS);
        }
        e.setAI(false);
        e.setAgeLock(true);
        e.setBreed(false);
        e.setJumpStrength(0.3);
        e.setAge(1);
        e.setCustomNameVisible(true);
        e.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue((ps * (transportData.getSpeed() / 25)));
        e.getInventory().setSaddle(new ItemStack(Material.SADDLE));

        add(player.getUniqueId(), e.getUniqueId());
        e.setPassenger(player);
    }

    public String color(String name) {
        switch (name) {
            case "일반":
                return "§7";
            case "에픽":
                return "§b";
            case "희귀":
                return "§e";
            case "전설":
                return "§6";
            case "???":
                return "§5";
        }
        return "";
    }

    public boolean check(UUID uuid) {
        if(players.containsKey(uuid)) {
            return true;
        }
        return false;
    }

    public void add(UUID uuid, UUID uuid2) {
        players.put(uuid, uuid2);
    }

    public Map<UUID, UUID> getPlayers() {
        return players;
    }

    public UUID getPlayer(UUID uuid) {
        return players.get(uuid);
    }

    public void removePlayer(UUID uuid) {
        players.remove(uuid);
    }

}
