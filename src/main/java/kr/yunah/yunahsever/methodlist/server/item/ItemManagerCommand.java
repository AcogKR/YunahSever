package kr.yunah.yunahsever.methodlist.server.item;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.ItemManager;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ItemManagerCommand implements CommandExecutor {

    private final YunahSever yunahSever;
    private final ItemManager itemManager;

    public ItemManagerCommand(YunahSever yunahSever, ItemManager itemManager) {
        this.yunahSever = yunahSever;
        this.itemManager = itemManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && sender.isOp()) {
            Player player = (Player) sender;
            if(args.length != 0) {
                switch (args[0]) {
                    case "도움말":

                        break;
                    case "생성":
                        if (args.length >= 2) {

                            switch (args[1]) {
                                case "무기":
                                    if (args.length >= 3) {
                                        String name = StringUtils.join(args, ' ', 2, args.length);
                                        if (!itemManager.isWeapon(name)) {
//                                            itemManager.createWeapon(name);
                                            player.sendMessage("§b아이템매니저 §f: " + args[0] + " 아이템을 생성 했습니다");
                                        } else {
                                            player.sendMessage("§8오류 §f: 이미 존재하는 아이템 입니다 ");
                                        }
                                    } else {
                                        player.sendMessage("§8오류 §f: 생성하실 아이템의 이름을 적어주세요");
                                    }
                                    break;
                                case "포션":

                                    break;
                                case "방어구":

                                    break;
                            }
                        }
                        break;
                    case "제거":
                        if (args.length >= 2) {
                            switch (args[1]) {
                                case "무기":
                                    if (itemManager.isWeapon(args[1])) {
                                        itemManager.removeWeapon(args[1]);
                                        player.sendMessage("§b아이템매니저 §f: " + args[0] + " 아이템을 삭제 했습니다");
                                    } else {
                                        player.sendMessage("§8오류 §f: 이미 존재하는 읺는 아이템 입니다 ");
                                    }
                                    break;
                                case "포션":

                                    break;
                                case "방어구":

                                    break;
                            }
                        }
                        break;
                    case "아이템목록":
                        if (args.length >= 2) {
                            switch (args[1]) {
                                case "무기":

                                    break;
                                case "포션":

                                    break;
                                case "방어구":

                                    break;
                            }
                        }
                        break;
                    case "설정도움말":

                        break;
                    default:
                        if (itemManager.isWeapon(args[0])) {
                            if (args.length >= 2) {
                                switch (args[1]) {
                                    case "등급":
                                        break;
                                    case "종족":
                                        break;
                                    case "소켓":
                                        break;
                                    case "판매가격":
                                        break;
                                    case "공격력":
                                        break;
                                    case "최소레벨":
                                        break;
                                    case "최소체력":
                                        break;
                                    case "최소힘":
                                        break;
                                    case "최소순발력":
                                        break;
                                    case "최소정신력":
                                        break;
                                    case "최소지력":
                                        break;
                                    case "최소민첩성":
                                        break;
                                    case "크리티컬":
                                        break;
                                    case "흡혈":
                                        break;
                                    case "근거리공격력":
                                        break;
                                    case "원거리공격력":
                                        break;
                                    case "마법공격력":
                                        break;
                                }
                            } else {
                                player.sendMessage("§b아이템매니저 §: /아이탬매니저 설정도움말");
                            }
                        } else {
                            sender.sendMessage("§8오류 §f: 존재하지 않는 아이템 입니다 ");
                            return false;
                        }
                        player.sendMessage("§b아이템매니저 §: /아이탬매니저 도움말");
                        break;
                }
            } else {
                player.sendMessage("§b아이템매니저 §: /아이탬매니저 도움말");
            }
        } else {
            sender.sendMessage("§8오류 §f: 권한이 부족합니다 ");
        }
        return false;
    }
}
