package kr.yunah.yunahsever.methodlist.playermoney;

import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MoneyCommand implements CommandExecutor {

    private final PlayerManager playerManager;

    public MoneyCommand(PlayerManager playerManager) {
        this.playerManager = playerManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        if(args.length != 0) {
            if(args[0].equals("보내기")) {
                if(args.length >= 2) {
                    if(player.getName().equals(args[1])) {
                        player.sendMessage("§f");
                        player.sendMessage("§8오류 §f: 자기 자신에게는 돈을 보낼수 없습니다 ");
                        player.sendMessage("§f");
                        return false;
                    }
                    for (Player players : Bukkit.getOnlinePlayers()) {
                        if (players.getName().equals(args[1])) {
                            if(args.length >= 3) {
                                try {
                                    int SendMoney = Integer.parseInt(args[2]);
                                    YunahPlayer yunahPlayer1 = playerManager.get(players.getUniqueId());
                                    if(yunahPlayer.getMoney() + 1 > SendMoney) {
                                        player.sendMessage("§f");
                                        player.sendMessage(String.format("§a◈§f돈보내기 : %s님에게 %s원을 보냈습니다", players.getName(), moneycomma(SendMoney)));
                                        player.sendMessage("§f");
                                        players.sendMessage("§f");
                                        players.sendMessage(String.format("§a◈§f돈 : %s님에게 %s원을 받았습니다", player.getName(), moneycomma(SendMoney)));
                                        players.sendMessage("§f");

                                        yunahPlayer1.addMoney(SendMoney);
                                        yunahPlayer.subMoney(SendMoney);
                                        players.playSound(players.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 5, (float) 10);
                                        return false;
                                    } else {
                                        player.sendMessage("§f");
                                        player.sendMessage("§8오류 §f: 돈이 부족합니다  ");
                                        player.sendMessage("§f");
                                        return false;
                                    }
                                } catch (NumberFormatException e) {
                                    player.sendMessage("§f");
                                    player.sendMessage("§8오류 §f: 보내실 값을 제데로 적어주세요 ");
                                    player.sendMessage("§f");
                                    return false;
                                }
                            } else {
                                player.sendMessage("§f");
                                player.sendMessage("§8오류 §f: 보내실 돈을 적어주세요 ");
                                player.sendMessage("§f");
                                return false;
                            }
                        } else {
                            player.sendMessage("§f");
                            player.sendMessage("§8오류 §f: " + args[1] + "님은 서버에 접속 상태가 아닙니다 ");
                            player.sendMessage("§f");
                            return false;
                        }
                    }
                } else {
                    player.sendMessage("§f");
                    player.sendMessage("§a  §f/돈 보내기 <Player> <value> ");
                    player.sendMessage("§f");
                    return false;
                }
            } else if(args[0].equals("수표발행")) {
                if(args.length >= 2) {
                    try {
                        int money = Integer.parseInt(args[1]);
                        if(args.length >= 3) {
                            try {
                                int amount = Integer.parseInt(args[2]);
                                cheque(player, amount, money);
                            } catch (NumberFormatException e) {
                                player.sendMessage("§f");
                                player.sendMessage("§8오류 §f: 발행하실 수표의 갯수를 제데로 적어주세여 ");
                                player.sendMessage("§f");
                                return false;
                            }
                        } else {
                            cheque(player, 1, money);
                        }
                    } catch (NumberFormatException e) {
                        player.sendMessage("§f");
                        player.sendMessage("§8오류 §f: 발행하실 수표의 가격을 제데로 적어주세요요 ");
                        player.sendMessage("§f");
                        return false;
                    }
                } else {
                    player.sendMessage("§f");
                    player.sendMessage("§8오류 §f: 발행하실 수표의 금액을 적어주세요 ");
                    player.sendMessage("§f");
                    return false;
                }
            } else {
                player.sendMessage("§f");
                player.sendMessage("§a◈§f돈 명령어");
                player.sendMessage("§a  §f/돈 보내기 <Player> <value> ");
                player.sendMessage("§a  §f/돈 수표발행 <value>");
                player.sendMessage("§f");
                return false;
            }
        } else {
            player.sendMessage("§f");
            player.sendMessage("§a◈§f돈 :: " + moneycomma(yunahPlayer.getMoney()));
            player.sendMessage("§f");
            return false;
        }
        return false;
    }

    public void cheque(Player player, int amount, int money) {
        YunahPlayer yunahPlayer = playerManager.get(player.getUniqueId());
        player.getInventory().addItem(new ItemBuilder(Material.PAPER)
                .amount(amount).display("§a유나서버 수표 §f"+ moneycomma(money))
                .lore("§f",
                        "§f 아이템을 들고서 우클릭하시면 아이템이 지급됩니다  ",
                        "§f  수표 가격 : " + moneycomma(money),
                        "§f").build());
        yunahPlayer.subMoney(money * amount);
        player.sendMessage("§f");
        player.sendMessage("§a◈§f돈 :: 수표를 발행했습니다 ");
        player.sendMessage("§f");
    }

    public String moneycomma(int money) {
        return String.format("%,d", money);
    }
}
