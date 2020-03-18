package kr.yunah.yunahsever.methodlist.server.warpsystem;

import kr.yunah.yunahsever.serverdata.WarpManager;
import kr.yunah.yunahsever.serverdata.datalist.WarpData;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class WarpCommand implements CommandExecutor {

    private final WarpManager warpManager;

    public WarpCommand(WarpManager warpManager) {
        this.warpManager = warpManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && !(sender.isOp())) {
            sender.sendMessage("§8오류 §f: 권한이 부족합니다");
            return false;
        }
        Player player = (Player) sender;
        if(args.length != 0) {
            if(args[0].equalsIgnoreCase("list")) {
                warpManager.open(player);
            } else if(args[0].equalsIgnoreCase("Create")) {
                if(args.length >= 2) {
                    if(!warpManager.getfor(args[1])) {
                        Location location = player.getLocation();
                        warpManager.create(args[1], location);
                        player.sendMessage("§9워프 §f: 새로운 워프장소를 생성했습니다");
                    } else {
                        player.sendMessage("§8오류 §f: 이미 존재하는 워프 입니다 ");
                    }
                } else {
                    player.sendMessage("§8오류 §f: 생성하실 워프의 이름을 적어주세요");
                }
            } else if(args[0].equalsIgnoreCase("remove")) {
                if(args.length >= 2) {
                    if(warpManager.getfor(args[1])) {
                        warpManager.remove(args[1]);
                        player.sendMessage("§9워프 §f: 워프를 삭제했습니다 ");
                    } else {
                        player.sendMessage("§8오류 §f: 존재하지 않는 워프 이름입니다 ");
                    }
                } else {
                    player.sendMessage("§8오류 §f: 삭제하실 워프의 이름을 적어주세요");
                }
            } else if(warpManager.getfor(args[0])) {
                if(args.length >= 2) {
                    if(args[1].equalsIgnoreCase("setbind")) {
                        WarpData warpData = warpManager.get(args[1]);
                        if(warpData.isBind()) {
                            warpData.setBind(false);
                            player.sendMessage("§9워프 §f: True -> False");
                        } else {
                            warpData.setBind(true);
                            player.sendMessage("§9워프 §f: False -> True");
                        }
                    } else {
                        player.sendMessage("§8오류 §f: 수행하실 작업을 제데로 적어주세요");
                    }
                } else {
                    WarpData warpData = warpManager.get(args[0]);
                    Location location = warpData.getLocation();
                    player.sendMessage("§f");
                    player.sendMessage("§9워프 정보 §f< " + args[0] + " > ");
                    player.sendMessage(String.format("§9 §f좌표 : ( X : %s Y : %s Z : %s )", Math.round(location.getX()), Math.round(location.getY()), Math.round(location.getZ())));
                    player.sendMessage(String.format("§9 §f바인드 : %s", warpData.isBind()));
                    player.sendMessage("§f");
                }
            } else {
                player.sendMessage("§8오류 §f: 존재하지 않는 워프 이름입니다 ");
            }
        } else {
            player.sendMessage("§9 워프 §f명령어");
            player.sendMessage("§7  §f/warp list");
            player.sendMessage("§7  §f/warp create <name> ");
            player.sendMessage("§7  §f/warp remove <name> ");
        }
        return false;
    }
}
