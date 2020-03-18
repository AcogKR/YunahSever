package kr.yunah.yunahsever.methodlist.server.transport;

import kr.yunah.yunahsever.lib.enumlist.Rating;
import kr.yunah.yunahsever.serverdata.TransportManager;
import kr.yunah.yunahsever.serverdata.datalist.TransportData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TransportCommand implements CommandExecutor {

    private final TransportManager transportManager;

    public TransportCommand(TransportManager transportManager) {
        this.transportManager = transportManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player && sender.isOp()) {
            Player player = (Player) sender;
            if(args.length != 0) {
                if(args[0].equals("도움말")) {
                    help(player);
                    help1(player);
                } else if(args[0].equals("생성")) {
                    if(args.length >= 2) {
                        if(!transportManager.iscontain(args[1])) {
                            transportManager.create(args[1]);
                            player.sendMessage("§b이동수단 §f: " + args[1] + " 이동수단을 생성했습니다");
                        } else {
                            sender.sendMessage("§8오류 §f: 이미 존재하는 이동수단 입니다 ");
                        }
                    } else {
                        sender.sendMessage("§8오류 §f: 생성하실 이동수단의 이름을 정해주세요 ");
                    }
                } else if(args[0].equals("삭제")) {
                    if(args.length >= 2) {
                        if(transportManager.iscontain(args[1])) {
                            transportManager.remove(args[1]);
                            player.sendMessage("§b이동수단 §f: " + args[1] + " 이동수단을 생성했습니다");
                        } else {
                            sender.sendMessage("§8오류 §f: 존재하지 않는 이동수단 입니다 ");
                        }
                    } else {
                        sender.sendMessage("§8오류 §f: 삭제하실 이동수단의 이름을 정해주세요 ");
                    }
                } else if(args[0].equals("목록")) {
                    transportManager.open(player);
                } else if(args[0].equals("탈것등급")) {
                    help2(player);
                } else {
                    if(transportManager.iscontain(args[0])) {
                        TransportData transportData = transportManager.get(args[0]);
                        if(args.length >= 2) {
                            if(args[1].equals("속도설정")) {
                                if(args.length >= 3) {
                                    try {
                                        int index = Integer.parseInt(args[2]);
                                        if(index > 99 && index < 201) {
                                            transportData.setSpeed(index);
                                            player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 이동속도를 %s%%로 설정했습니다", args[0], index));
                                            return false;
                                        } else {
                                            sender.sendMessage("§8오류 §f: 속도설정은 100 ~ 200 사이여야 합니다 ");
                                        }
                                    } catch (NumberFormatException e) {
                                        sender.sendMessage("§8오류 §f: 속도설정은 숫자를 적어주셔야 합니다 ");
                                        sender.sendMessage("   §7( 속도는 적은것의 퍼센트로 적용 됩니다 / 최대 200 )");
                                    }
                                } else {
                                    sender.sendMessage("§8오류 §f: 설정하실 속도를 적어주세요 ");
                                }
                            } else if(args[1].equals("등급설정")) {
                                if(args.length >= 3) {
                                    if(args[2].equals("일반")) {
                                        transportData.setRating(Rating.NORMAL);
                                        player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 등급을 %s로 설정했습니다", args[0], args[2]));
                                    } else if(args[2].equals("에픽")) {
                                        transportData.setRating(Rating.EPIC);
                                        player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 등급을 %s로 설정했습니다", args[0], args[2]));
                                    } else if(args[2].equals("희귀")) {
                                        transportData.setRating(Rating.RARE);
                                        player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 등급을 %s로 설정했습니다", args[0], args[2]));
                                    } else if(args[2].equals("전설")) {
                                        transportData.setRating(Rating.LEGEND);
                                        player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 등급을 %s로 설정했습니다", args[0], args[2]));
                                    } else if(args[2].equals("???")) {
                                        transportData.setRating(Rating.UNKNOWN);
                                        player.sendMessage(String.format("§b이동수단 §f: %s 이동수단의 등급을 %s로 설정했습니다", args[0], args[2]));
                                    } else {
                                        sender.sendMessage("§8오류 §f: 등급이 잘못 되었습니다  ");
                                        sender.sendMessage("§7    ( /이동수단 탈것등급 )");
                                    }
                                } else {
                                    sender.sendMessage("§8오류 §f: 등급을 설정하실려면 원하시는 등급을 입력해주세요 ");
                                    sender.sendMessage("§7    ( /이동수단 탈것등급 )");
                                }
                            } else {
                                help1(player);
                            }
                        } else {
                            help1(player);
                        }
                    } else {
                        player.sendMessage("§b이동수단 §f: /이동수단 도움말");
                    }
                }
            } else {
                player.sendMessage("§b이동수단 §f: /이동수단 도움말");
            }
        } else {
            sender.sendMessage("§8오류 §f: 권한이 부족합니다 ");
        }
        return false;
    }


    public void help(Player player) {
        player.sendMessage("§b이동수단 도움말");
        player.sendMessage("§f  /이동수단 생성 <이름>");
        player.sendMessage("§f  /이동수단 삭제 <이름>");
        player.sendMessage("§f  /이동수단 목록 ");
        player.sendMessage("§f  /이동수단 탈것등급");
    }

    public void help1(Player player) {
        player.sendMessage("§b이동수단 설정 도움말");
        player.sendMessage("§f  /이동수단 <이름> 속도설정 <값>");
        player.sendMessage("§f  /이동수단 <이름> 등급설정 <값>");
    }

    public void help2(Player player) {
        player.sendMessage("§b이동수단 탈것 등급");
        player.sendMessage("§7 일반 §b에픽 §e희귀 §6전설 §5???");
    }
}
