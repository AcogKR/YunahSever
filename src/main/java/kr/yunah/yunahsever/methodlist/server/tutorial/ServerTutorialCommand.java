package kr.yunah.yunahsever.methodlist.server.tutorial;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;

import static org.bukkit.configuration.file.YamlConfiguration.loadConfiguration;

public class ServerTutorialCommand implements CommandExecutor {
    
    private final YunahSever yunahSever;
    private final PlayerManager playerManager;


    public ServerTutorialCommand(YunahSever yunahSever, PlayerManager playerManager) {
        this.yunahSever = yunahSever;
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            Tutorial((Player) sender);
        } else {
            if(sender.isOp() && args[0].equals("위치설정")) {
                setTutorialLocation((Player) sender);
            } else if(args[0].equals("test")) {
                ((Player) sender).openInventory(new ServerTutorialEventListener(playerManager, yunahSever).TribePage1());
            }
        }
        return false;
    }

    public void Tutorial(Player player) {
        File file = new File(yunahSever.getDataFolder(), "config.yml");
        YamlConfiguration config = loadConfiguration(file);
        Location location = (Location) config.get("Tutorial");

        if(location != null) {
            player.teleport(location);
            player.sendMessage("§9튜토리얼 §f: 튜토리얼 위치로 이동했습니다");
            player.sendTitle("§9튜토리얼", "§7튜토리얼 위치로 이동되었습니다");
            return;
        } else {
            player.sendMessage("§8오류 §f: 튜토리얼 위치가 저장되지 않았습니다");
        }
    }

    public void setTutorialLocation(Player player) {
        Location location = player.getLocation();
        File file = new File(yunahSever.getDataFolder(), "config.yml");
        YamlConfiguration config = new YamlConfiguration();
        config.set("Tutorial", location);
        try {
            config.save(file);
            player.sendMessage("§9System §f: 튜토리얼 시작지점을 설정했습니다");
        } catch (IOException e) {
            e.printStackTrace();
            player.sendMessage("§9System §f: 튜토리얼 시작지점을 못했습니다");
        }
    }



}
