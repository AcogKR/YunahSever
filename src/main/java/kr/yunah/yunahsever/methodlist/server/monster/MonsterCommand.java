package kr.yunah.yunahsever.methodlist.server.monster;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.MonsterManager;
import kr.yunah.yunahsever.serverdata.datalist.MonsterData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MonsterCommand implements CommandExecutor {

    private final MonsterManager monsterManager;
    private final YunahSever yunahSever;

    public MonsterCommand(MonsterManager monsterManager, YunahSever yunahSever) {
        this.monsterManager = monsterManager;
        this.yunahSever = yunahSever;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!sender.isOp()) {
            return false;
        }
        Player player = (Player) sender;
        if(args.length != 0) {
            if(args[0].equalsIgnoreCase("생성")) {
                if (args.length >= 2) {
                    if (!monsterManager.isContain(args[1])) {
                        monsterManager.create(args[1]);
                        player.sendMessage("§c몬스터§f : 새로운 몬스터를 생성했습니다 ");
                    } else {
                        player.sendMessage("§c몬스터§f : 이미 해당 몬스터가 존재합니다");
                    }
                } else {
                    player.sendMessage("§c몬스터§f : 생성하실 몬스터의 이름을 적어주세요 ");
                }
            } else if(args[0].equalsIgnoreCase("목록")) {
                monsterManager.open(player);
            } else if(args[0].equalsIgnoreCase("삭제")) {
                if(args.length >= 2) {
                    if(monsterManager.isContain(args[1])) {
                        monsterManager.remove(args[1]);
                        player.sendMessage("§c몬스터§f : " + args[1] + "을 몬스터 리스트에서 삭제 했습니다");
                    } else {
                        player.sendMessage("§8오류§f : 존재하지 않는 몬스터 입니다 ");
                    }
                } else {
                    player.sendMessage("§8오류§f : 삭제하실 몬스터의 이름을 적어주세요 ");
                }
            } else if(args[0].equalsIgnoreCase("도움말")) {
                player.sendMessage("§c몬스터§f : 도움말");
                player.sendMessage("§f /몬스터 생성 <이름>");
                player.sendMessage("§f /몬스터 목록");
                player.sendMessage("§f /몬스터 삭제 <이름>");
                player.sendMessage("§f /몬스터 <이름> 돈설정 <값> <값>");
                player.sendMessage("§f /몬스터 <이름> 경험치설정 <값>");
                player.sendMessage("§f /몬스터 <이름> 아이템설정");
                return false;
            } else if(monsterManager.isContain(args[0])) {
                MonsterData monster = monsterManager.get(args[0]);
                if (args.length >= 2) {
                    if (args[1].equalsIgnoreCase("돈설정")) {
                        if (args.length >= 3) {
                            try {
                                int money1 = Integer.parseInt(args[2]);
                                if (args.length >= 4) {
                                    try {
                                        int money2 = Integer.parseInt(args[3]);
                                        if(money1 > money2) {
                                            player.sendMessage("§8오류§f : 잘못된 숫자를 입력하셨습니다 ");
                                            return false;
                                        }
                                        monster.setMoney1(money1);
                                        monster.setMoney2(money2);
                                        player.sendMessage(String.format("§c몬스터§f : %s 몬스터의 돈을 %s ~ %s 사이에서 나오도록 설정했습니다 ", args[0], money1, money2));
                                    } catch (NumberFormatException e) {
                                        player.sendMessage("§§8오류§f : 돈을 설정하실려면 숫자를 적어주세요 ");
                                    }
                                } else {
                                    monster.setMoney1(money1);
                                    monster.setMoney2(money1);
                                    player.sendMessage(String.format("§c몬스터§f : %s 몬스터의 돈을 %s 으로 설정했습니다 ", args[0], money1));
                                }
                            } catch (NumberFormatException e) {
                                player.sendMessage("§8오류§f : 돈을 설정하실려면 숫자를 적어주세요 ");
                            }
                        } else {
                            player.sendMessage("§8오류§f : 설정하실 돈을 적어주세요 ");
                        }
                    } else if (args[1].equalsIgnoreCase("경험치설정")) {
                        if (args.length >= 3) {
                            try {
                                int exp = Integer.parseInt(args[2]);
                                monster.setExp(exp);
                                player.sendMessage(String.format("§c몬스터§f : %s 몬스터의 경험치를 %s 으로 설정했습니다 ", args[0], exp));
                            } catch (NumberFormatException e) {
                                player.sendMessage("§cYunah§f : 경험치를 설정하실려면 숫자를 적어주세요 ");
                            }
                        } else {
                            player.sendMessage("§cYunah§f : 설정하실 경험치를 적어주세요 ");
                        }
                    } else if (args[1].equalsIgnoreCase("아이템설정")) {
                        monsterManager.itemopen((Player) player, args[0]);
                    } else if (args[1].equalsIgnoreCase("정보")) {
                        player.sendMessage("§c몬스터 정보 ");
                        player.sendMessage("§f 드랍 경험치 : " + monster.getExp());
                        player.sendMessage("§f 드랍 머니 : " + monster.getMoney1() + " ~ " + monster.getMoney2());
                        player.sendMessage("§f 아이템 : /몬스터 도움말");
                    }
                } else {
                    player.sendMessage("§c몬스터§f : /몬스터 도움말 ");
                }
            } else {
                player.sendMessage("§cYunah§f : 존재하지 않는 몬스터 입니다 ");
            }
        } else {
            player.sendMessage("§c몬스터§f : /몬스터 도움말 ");
        }
        return false;
    }

}
