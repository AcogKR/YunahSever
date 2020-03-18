package kr.yunah.yunahsever.methodlist.whisper;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class WhisperCommand implements CommandExecutor {

    private final Map<UUID, UUID> map = new HashMap<>();

    // 점검 끝 체크 완료
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        if(args.length != 0) {
            if(args[0].equals("도움말")) {

            } else if(args[0].equals("취소")) {
                if(contains(player.getUniqueId())) {
                    map.remove(player.getUniqueId());
                    player.sendMessage("§b귓속말 §f: 귓속말 상태를 취소했습니다");
                } else {
                    player.sendMessage("§8오류 §f: 당신은 귓속말 상태가 아닙니다");
                }
                return false;
            }
            for (Player players : Bukkit.getOnlinePlayers()) {
                if(player.getName().equals(args[0])) {
                    player.sendMessage("§8오류§f : 자기 자신에게는 귓속말을 할수 없습니다");
                    return false;
                }
                if(players.getName().equals(args[0])) {
                    if(args.length >= 2) {
                        whisper(player, players, StringUtils.join(args, ' ', 1, args.length));
                        return false;
                    } else {
                        if(contains(player.getUniqueId())) {
                            Player target = gettarget(player.getUniqueId());
                            if(!players.getUniqueId().equals(target.getUniqueId())) {
                                remove(player.getUniqueId());
                                map.put(player.getUniqueId(), players.getUniqueId());
                                player.sendMessage("§b귓속말 §f: 귓속말 상대방을 변경했습니다");
                                player.sendMessage(String.format("§7 (  %s -> %s )", target.getName(), players.getName()));
                            } else {
                                player.sendMessage("§8오류 §f: 설정하실려는 귓속말의 상대와 현재 상대방이 같습니다");
                            }
                        } else {
                            map.put(player.getUniqueId(), players.getUniqueId());
                            player.sendMessage("§b귓속말 §f: " + players.getName() + "님을 귓속말 상대방으로 설정했습니다");
                        }
                        return false;
                    }
                }
            }
            player.sendMessage(String.format("§8오류 :§f %s님은 오프라인 상태입니다 ", args[0]));
        } else {
            player.sendMessage("§b귓속말 §f: 귓속말 명령어를 확인하실려면 \" /귓속말 도움말 \" 를 입력해주세요 ");
        }
        return false;
    }

    public void whisper(Player player, Player target, String msg) {
        TextComponent placenta = new TextComponent("§6[ " + player.getName() + " -> " +target.getName() + " ]§f");
        TextComponent message = new TextComponent(" : §f" + msg);
        placenta.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§a! §f클릭시 " + player.getName() +" 님에게 귓속말을 합니다").create()));
        placenta.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "/w " + player.getName() + " "));
        placenta.addExtra(message);

        target.spigot().sendMessage(placenta);
        player.sendMessage(String.format("§6[ %s -> %s ]§f : %s", player.getName(), target.getName(), msg));
        target.playSound(target.getLocation(), Sound.ENTITY_ITEM_PICKUP, (float) 10, (float) 5);
    }

    public void remove(UUID uuid) {
        map.remove(uuid);
    }

    public Player gettarget(UUID uuid) {
        if(map.containsKey(uuid)) {
            return Bukkit.getPlayer(map.get(uuid));
        }
        return null;
    }

    public Map<UUID, UUID> getMap() {
        return map;
    }

    public void helpWhisper(Player player) {
        player.sendMessage("§b귓속말 §f: /귓속말 <Player> <Msg>");
        player.sendMessage("§b       §f: /귓속말 취소 ");
    }

    public boolean contains(UUID uuid) {
        for (Map.Entry<UUID, UUID> entry : map.entrySet()) {
            UUID target = entry.getKey();
            if(target.equals(uuid)){
                return true;
            }
        }
        return false;
    }
}
