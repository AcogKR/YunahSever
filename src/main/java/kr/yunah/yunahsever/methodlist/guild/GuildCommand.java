package kr.yunah.yunahsever.methodlist.guild;

import com.sun.org.apache.bcel.internal.generic.PUSH;
import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.serverdata.GuildManager;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.GuildData;
import kr.yunah.yunahsever.serverdata.datalist.GuildPlayerData;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Map;
import java.util.UUID;

public class GuildCommand implements CommandExecutor {

    private final GuildManager guildManager;
    private final PlayerManager playerManager;
    private final YunahSever yunahSever;


    public GuildCommand(GuildManager guildManager, PlayerManager playerManager, YunahSever yunahSever) {
        this.guildManager = guildManager;
        this.playerManager = playerManager;
        this.yunahSever = yunahSever;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length != 0) {
            if(args[0].equals("도움말")) {
                if (guildManager.isPlayerContain(player)) { // 소속 되어있는 길드가 있다면
                    if (guildManager.isPlayerMaster(player)) { // 길드의 마스터라면
                        masterHelp(player);
                    } else if (guildManager.isPlayerManager(player)) { // 길드 매니저라면
                        managerHelp(player);
                    } else { // 그냥 길드원이면
                        guildHelp(player);
                    }
                } else { // 소속 안되어있다면
                    normalHelp(player);
                }
            } else if(args[0].equals("생성")) {
                if (!guildManager.isPlayerContain(player)) {
                    if (playerManager.get(player.getUniqueId()).getLevelData().getLevel() >= 50) {
                        if (args.length >= 2) {
                            String name = args[1];
                            if (!(name.length() >= 6)) {
                                if (!guildManager.guildNameCheck(name)) {
                                    guildManager.createGuild(name, player);
                                    guildCreateMsg(player, name);
                                } else {
                                    player.sendMessage(w("이미 존재하는 길드의 이름입니다 "));
                                }
                            } else {
                                player.sendMessage(w("길드의 이름은 최대 5글자 입니다다"));
                            }
                        } else {
                            player.sendMessage(w("생성하실 길드의 이름을 적어주세요 : 최대 5글자"));
                        }
                    } else {
                        player.sendMessage(w("50레벨 이상부터 길드를 생성하실수 있습니다다"));
                    }
                } else {
                    player.sendMessage(w("당신은 이미 소속중인 길드가 존재합니다"));
                }
            } else if(args[0].equals("권유")) {
                if (guildManager.isPlayerContain(player)) {
                    if (guildManager.isPlayerManager(player) || guildManager.isPlayerMaster(player)) {
                        if (args.length >= 2) {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target != null) {
                                if (target.isOnline()) {
                                    if (!target.equals(player)) {
                                        String guildName = playerManager.get(player.getUniqueId()).getGuildName();
                                        invite(player, target, guildName);
                                    } else {
                                        player.sendMessage(w("자신에게는 길드 가입 권유를 하실수 없습니다 "));
                                    }
                                } else {
                                    player.sendMessage(w("오프라인 플레이어는 길드가입 권유를 하실수 없습니다"));
                                }
                            } else {
                                player.sendMessage(w("존재하지 않는 플레이어 입니다"));
                            }
                        } else {
                            player.sendMessage(w("길드 가입을 권유하실 플레이어의 닉네임을 적어주세요"));
                        }
                    } else {
                        player.sendMessage(w("당신은 이미 소속한 길드가 존재합니다"));
                    }
                } else {
                    if (guildManager.getInvite().containsValue(player.getUniqueId())) {
                        if (args.length >= 2) {
                            if (args[1].equals("수락")) {
                                accept(player);
                            } else if (args[1].equals("거절")) {
                                deny(player);
                            } else {
                                player.sendMessage(g("/길드 권유 < 수락 / 거절 >"));
                            }
                        } else {
                            player.sendMessage(g("/길드 권유 < 수락 / 거절 >"));
                        }
                    } else {
                        player.sendMessage(w("당신은 길드가입 권유를 받으신적이 없습니다"));
                    }
                }
            } else if(args[0].equals("탈퇴")) {
                if(guildManager.isPlayerContain(player)) {
                    if (!guildManager.isPlayerMaster(player)) {
                        String guildName = playerManager.get(player.getUniqueId()).getGuildName();
                        GuildData guildData = guildManager.getGuild(guildName);
                        if(guildData.getPlayers().containsKey(player)) {
                            guildData.remPlayer(player);
                            player.sendMessage(g(String.format("%s 길드에서 나오셨습니다", guildName)));
                            for(Map.Entry<Player, GuildPlayerData> entry : guildData.getPlayers().entrySet()) {
                                entry.getKey().sendMessage(g(String.format("%s 님이 길드에서 나가셨습니다 ")));
                            }
                        }
                    } else {
                        player.sendMessage(w("길드 마스터는 길드 탈퇴를 하실수 없습니다다"));
                    }
                } else {
                    player.sendMessage(w("당신은 소속한 길드가 존재하지 않습니다"));
                }
            } else {
                player.sendMessage(g("명령어를 확인하실려면 \"/길드 도움말\" 를 입력해주세요 "));
            }
        } else {
            player.sendMessage(g("명령어를 확인하실려면 \"/길드 도움말\" 를 입력해주세요 "));
        }
        return false;
    }


    // Invite Method

    public void invite(Player player, Player target, String GuildName) {
        if(!guildManager.getInvite().containsValue(target.getUniqueId())) {
            if(!guildManager.isPlayerContain(target)) {
                GuildData guildData = guildManager.getGuild(GuildName);
                if(!(guildData.getPlayers().size() >= guildData.getPersonnel())) {
                    target.sendMessage(g(String.format("%s 길드가 당신에게 길드가입 권유를 하셨습니다 ", GuildName)));
                    player.sendMessage(g(String.format("%s 님에게 길드 가입을 권유했습니다 ", target.getName())));
                    guildManager.inviteJoin(target, GuildName);
                } else {
                    player.sendMessage(w("길드의 자리가 부족합니다 [ 정원을 늘리세요 ]"));
                }
            } else {
                player.sendMessage(w("플레이어는 이미 소속한 길드가 있습니다다"));
            }
        } else {
            player.sendMessage(w("현재 플레이어는 이미 길드 가입권유를 받으신 상태입니다다"));
        }
   }

   public void accept(Player player) {
        if(guildManager.getInvite().containsValue(player.getUniqueId())) {
            if(!guildManager.isPlayerContain(player)) {
                for (Map.Entry<String, UUID> entry : guildManager.getInvite().entrySet()) {
                    if(entry.getValue().equals(player.getUniqueId())) {
                        String name = entry.getKey();
                        GuildData guildData = guildManager.getGuild(name);
                        if(!(guildData.getPlayers().size() >= guildData.getPersonnel())) {
                            guildData.addPlayer(player);
                            guildManager.inviteQuit(player);
                            for(Map.Entry<Player, GuildPlayerData> players : guildData.getPlayers().entrySet()) {
                                players.getKey().sendMessage(g(String.format("%s 님이 길드에 가입하셨습니다 축하해주세요 !", player.getName())));
                            }
                            return;
                        } else {
                            player.sendMessage(w("길드의 정원이 꽉찻습니다 길드마스터에게 문의해주세요요"));
                        }
                        return;
                    }
                }
            } else {
                player.sendMessage(w("당신은 이미 소속한 길드가 존재합니다"));
            }
        } else {
            player.sendMessage(w("당신은 길드가입의 권유를 받으신적이 없습니다"));
        }
   }

   public void deny(Player player) {
       if(guildManager.getInvite().containsValue(player.getUniqueId())) {
           guildManager.inviteQuit(player);
           player.sendMessage(g("길드 가입권유를 거절했습니다"));
       } else {
           player.sendMessage(w("당신은 길드가입의 권유를 받으신적이 없습니다"));
       }
   }


    // Guild Help Method


    public void guildCreateMsg(Player player, String name) {
        GuildData guildData = guildManager.getGuild(name);
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.sendMessage("§8");
            player.sendMessage(g(String.format("%s 님이 %s를 생성하셨습니다 축하해주세요 !", player.getName(), name)));
            players.sendMessage("§8");
        }
        player.sendMessage("§6────────────────────────────");
        player.sendMessage("§7");
        player.sendMessage("§7  길드를 생성했습니다 ");
        player.sendMessage("§7 > 길드이름 : " + name );
        player.sendMessage("§7 > 길드마스터 : " + Bukkit.getPlayer(guildData.getMaster()).getName());
        player.sendMessage("§7");
        player.sendMessage("§6────────────────────────────");
    }
    public void normalHelp(Player player) {
        player.sendMessage("§f");
        player.sendMessage("§6 < §f길드 명령어 도움말 §6>");
        player.sendMessage("§f");
        player.sendMessage("§7  /길드 생성 < 이름 > # 길드를 생성하실려면 퀘스트를 완료해야합니다");
        player.sendMessage("§7  /길드 권유 < 수락 / 거절 > # 수락시 길드를 들어갑니다 ");
        player.sendMessage("§f");
    }

    public void guildHelp(Player player) {
        player.sendMessage("§f");
        player.sendMessage("§6 < §f길드 명령어 도움말 : 길드원 §6>");
        player.sendMessage("§f");
        player.sendMessage("§7  /길드 정보 # 자신이 소속한 길드의 정보를 확인합니다");
        player.sendMessage("§7  /길드 금고 입금 < 금액 > # 길드 금고에 돈을 입금합니다 ");
        player.sendMessage("§7  /길드 탈퇴 # 소속한 길드를 탈퇴합니다");
        player.sendMessage("§f");
    }

    public void managerHelp(Player player) {
        player.sendMessage("§f");
        player.sendMessage("§6 < §f길드 명령어 도움말 : 매니저 §6>");
        player.sendMessage("§f");
        player.sendMessage("§7  /길드 공지 <메세지> # 길드의 공지를 수정합니다");
        player.sendMessage("§7  /길드 금고 < 입금 / 출금 > # 길드 금고를 이용합니다 ");
        player.sendMessage("§7  /길드 권유 < 닉네임 > # 플레이어에게 길드를 권유합니다 ");
        player.sendMessage("§7  /길드 추방 < 닉네임 > # 플레이어를 길드에서 추방합니다 ");
        player.sendMessage("§7  /길드 정보 # 자신이 소속한 길드의 정보를 확인합니다");
        player.sendMessage("§7  /길드 탈퇴 # 소속한 길드를 탈퇴합니다");
        player.sendMessage("§f");
    }

    public void masterHelp(Player player) {
        player.sendMessage("§f");
        player.sendMessage("§6 < §f길드 명령어 도움말 : 마스터 §6>");
        player.sendMessage("§f");
        player.sendMessage("§7  /길드 공지 <메세지> # 길드의 공지를 수정합니다"); // 완성
        player.sendMessage("§7  /길드 금고 < 입금 / 출금 > # 길드 금고를 이용합니다 "); // 완성
        player.sendMessage("§7  /길드 권유 < 닉네임 > # 플레이어에게 길드를 권유합니다 "); // 완성
        player.sendMessage("§7  /길드 추방 < 닉네임 > # 플레이어를 길드에서 추방합니다 "); // 완성
        player.sendMessage("§7  /길드 정보 # 자신이 소속한 길드의 정보를 확인합니다"); // 생각중
        player.sendMessage("§7  /길드 매니저 <닉네임> # 아무것도 안적을시 현재 매니저를 삭제합니다"); // 완성
        player.sendMessage("§7  /길드 위임 # 길드매니저에게 길드마스터를 위임합니다 "); // 완성
        player.sendMessage("§f");
    }

    public String g(String massage) {
        return "§6길드 §f: " + massage;
    }
    public String w(String massage) {
        return "§8오류 §f: " + massage;
    }
}
