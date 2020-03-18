package kr.yunah.yunahsever.serverdata;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.datalist.WarpData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class WarpManager implements Listener {
    public final Map<String, WarpData> map = new HashMap<>();
    public YunahSever yunahSever;


    public WarpData get(String name) {
        return map.computeIfAbsent(name, name1 ->
                new WarpData(
                        name, new Location(Bukkit.getWorld("world"), (double) 8, (double) 117, (double) 9), false
                ));
    }

    public void create(String name, Location location) {
        map.put(name, new WarpData(
                name, location, false
        ));
    }

    public void remove(String name) {
        map.remove(name);
    }


    public WarpManager(YunahSever yunahSever) {
        this.yunahSever = yunahSever;
    }

    public void save() {
        for(Map.Entry<String, WarpData> entry : map.entrySet()) {
            YamlConfiguration config = new YamlConfiguration();
            File file = new File(yunahSever.getDataFolder() + "/WarpList/", entry.getKey() + ".yml");
            ConfigurationSection section = new MemoryConfiguration();
            section.set(entry.getKey(), entry.getValue());
            config.set("WarpList", section);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void load() {
        File[] files = new File(yunahSever.getDataFolder() + "/WarpList/").listFiles();
        if(files == null) { return; }
        for (File file : files) {
            YamlConfiguration config = loadConfiguration(file);
            ConfigurationSection section = config.getConfigurationSection("WarpList");
            if (section == null) {
                return;
            }
            for (String id : section.getKeys(false)) {
                Object object = section.get(id);
                if (object instanceof WarpData) {
                    WarpData warpData = (WarpData) object;
                    map.put(id, warpData);
                }
            }
        }
    }

    public boolean getfor(String naem) {
        for(Map.Entry<String, WarpData> entry : map.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(naem)) {
                return true;
            }
        }
        return false;
    }

    // Event Listener method

    public void open(Player player) {
        Inventory inv = Bukkit.createInventory(null, 6*9, "워프 리스트");
        int index = 0;
        for(Map.Entry<String, WarpData> entry : map.entrySet()) {
            WarpData warpData = get(entry.getKey());
            Location location = entry.getValue().getLocation();
            inv.setItem(index, new ItemBuilder(Material.IRON_NUGGET)
                    .display("§e" + entry.getKey()).lore("",
                            "§7 클릭시 " + entry.getKey() + " 장소로 이동합니다",
                            "§7 바인드 상태 : " + String.valueOf(warpData.isBind()),
                            String.format("§7 ( X : %s Y : %s Z : %s )", Math.round(location.getX()), Math.round(location.getY()), Math.round(location.getZ())),
                            "",
                            "§7 ( 더블 클릭시 위에 좌표로 이동합니다 )",
                            "§7 ( 쉬프트 좌클릭시 워프를 삭제합니다 )",
                            "§7 ( 쉬프트 우클릭시 워프석을 지급 받습니다 )",
                            ""
                            ).build());
            index++;
        }

        player.openInventory(inv);
    }

    @EventHandler
    public void onclick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("워프 리스트")) {
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(e.getCurrentItem() == null) {
                return;
            } else if(e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).trim();
                Location location = map.get(name).getLocation();
                player.teleport(location);
                player.sendMessage("§9◈ §f워프 : " + name + "으로 텔레포트 했습니다");
            } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).trim();
                map.remove(name);
                player.sendMessage("§9◈ §f워프 : " + name + " 워프 지역을 삭제했습니다 ");
                e.getInventory().remove(e.getCurrentItem());
            } else if(e.getClick().equals(ClickType.SHIFT_RIGHT)) {
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2).trim();
                Location location = map.get(name).getLocation();
                player.getInventory().addItem(new ItemBuilder(Material.SNOWBALL).display("§7포탈석§f " + name).lore("",
                        "§7  포탈석을 들고 우래 좌표로 이동합니다 ",
                        String.format("§7 (  X : %s Y : %s Z : %s )", Math.round(location.getX()), Math.round(location.getY()), Math.round(location.getZ())),
                        ""
                        ).build());
                player.sendMessage("§9◈ §f워프 : " + name + "의 포탈석을 지급했습니다 ");
            }
        }
    }

    @EventHandler
    public void PlayerInter(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        if(e.getItem() == null || e.getItem().getItemMeta() == null) { return; }
        if(e.getItem().getItemMeta().getDisplayName().contains("포탈석")) {
            String name = e.getItem().getItemMeta().getDisplayName().substring(8).trim();
            WarpData warpData = map.get(name);
            e.getPlayer().teleport(warpData.getLocation());
            player.sendMessage("§9◈ §f워프 : " + name + "의 포탈석을 사용하여 이동하였습니다 ");
            ItemStack item = e.getItem();
            item.setAmount(1);
            player.getInventory().remove(item);
        }
    }
}
