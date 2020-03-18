package kr.yunah.yunahsever.lib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class MinecraftUtils {

    public void chatClear(Player player) {
        if(player.isOp()) {
            for (Player players : Bukkit.getOnlinePlayers()) {
                for (int i = 0; i < 100; i++) {
                    players.sendMessage("§f");
                }
                SimpleDateFormat time = new SimpleDateFormat("yyyy년 MM월 dd일 HH:mm");
                Calendar time1 = Calendar.getInstance();

                players.sendMessage("  §a◈ §f채팅창을 청소했습니다");
                players.sendMessage("§f  §f실행자 : " + player.getName() + " " + time.format(time1.getTime()));
            }
        } else {
            for (int i = 0; i < 100; i++) {
                player.sendMessage("§f");
            }
            player.sendMessage("§6◈ §f채팅창을 청소했습니다");
        }
    }}
