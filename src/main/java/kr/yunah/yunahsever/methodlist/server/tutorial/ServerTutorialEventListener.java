package kr.yunah.yunahsever.methodlist.server.tutorial;

import kr.yunah.yunahsever.YunahSever;
import kr.yunah.yunahsever.lib.ItemBuilder;
import kr.yunah.yunahsever.serverdata.PlayerManager;
import kr.yunah.yunahsever.serverdata.datalist.player.YunahPlayer;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.Inventory;

public class ServerTutorialEventListener implements Listener {


    private final PlayerManager playerManager;
    private final YunahSever yunahSever;

    public ServerTutorialEventListener(PlayerManager playerManager, YunahSever yunahSever) {
        this.playerManager = playerManager;
        this.yunahSever = yunahSever;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void playerCommand(PlayerCommandPreprocessEvent e) {
        YunahPlayer yunahPlayer = playerManager.get(e.getPlayer().getUniqueId());
        if(yunahPlayer.getTribe().equals("null")) {
            if(e.getPlayer().isOp()) { return; }
            if(e.getMessage().equals("/튜토리얼")) {
                new ServerTutorialCommand(yunahSever, playerManager).Tutorial(e.getPlayer());
            } else {
                e.setCancelled(true);
                e.getPlayer().sendMessage("§7튜토리얼 §f: 튜토리얼을 완료해야지 명령어를 입력할수 있습니다");
            }
        }
    }


    public Inventory TribePage1() {
        Inventory inv = Bukkit.createInventory(null, 6*9, "종족 선택창 ( 1 / 2 ) ");
        inv.setItem(26, new ItemBuilder(Material.IRON_NUGGET).display("§f 다음페이지").lore("", "§7 클릭시 다음페이지로 넘어갑니다 ", "§7 현재페이지 ( 1 / 2 )", "").build());
        inv.setItem(35, new ItemBuilder(Material.IRON_NUGGET).display("§f 다음페이지").lore("", "§7 클릭시 다음페이지로 넘어갑니다 ", "§7 현재페이지 ( 1 / 2 )", "").build());

        for (int i = 10; i < 39; i++) {
            inv.setItem(i, new ItemBuilder(Material.SUGAR)
                    .display("§fHUMAN §7[ 기사 ]").lore("",
                            "§f  유나서버의 제일 처음만들어진 휴먼, 높은 방어력과 체력으로 파티에 리더역활을 한다 ",
                            "§f  전직은 디펜더와 가디언이 있다 가디언은 높은 방어력으로 플레이어들을 수호한다 ",
                            "§f  가디언은 빠른 공격속도와 높은 공격력으로 빠르게 적을 제압한다 . [ 전직 가능 ]",
                            "",
                            "§f      §4§l근력 §f: |||||||||||||||||||       §c§l체력 §f: |||||||||||||||||||        §9지력 §f: ||||||||||||||||||| ",
                            "§f      §9§l정신력 §f: |||||||||||||||||||     §2§l순발력 §f: |||||||||||||||||||      §a§l민첩력 §f: ||||||||||||||||||| ",
                            "",
                            "§7   >>§f 위에 추천스텟은 전직전의 스텟입니다 > 전직후는 개인성향에 따라 찍으시면 됩니다 < ",
                            ""
                    ).build());
            if(i == 11 || i == 20 || i == 29 || i == 36) {
                i += 7;
            }
        }

        for (int i = 13; i < 42; i++) {
            inv.setItem(i, new ItemBuilder(Material.SUGAR)
                    .display("§fHALF_ELF §7[ 궁수 ]").lore("",
                            "§f  유나서버의 공격거리가 제일 멀은 캐릭터 궁수, 높은 공격력과 공격거리로 파티의 원거리 딜러를 맡는다 ",
                            "§f  전직은 스카우트, 레인저가 있다 스카우트는 하프엘프 종족에서 가장 빠른 스피드를 자랑한다 ",
                            "§f  레인저는 한발한발 바위를 부실정도의 강력하여 유나서버 종족중 강력한편에 포함된다 . [ 전직 가능 ]",
                            "",
                            "§f      §4§l근력 §f: |||||||||||||||||||       §c§l체력 §f: |||||||||||||||||||        §9지력 §f: ||||||||||||||||||| ",
                            "§f      §9§l정신력 §f: |||||||||||||||||||     §2§l순발력 §f: |||||||||||||||||||      §a§l민첩력 §f: ||||||||||||||||||| ",
                            "",
                            "§7   >>§f 위에 추천스텟은 전직전의 스텟입니다 > 전직후는 개인성향에 따라 찍으시면 됩니다 < ",
                            ""
                    ).build());
            if(i == 14 || i == 23 || i == 32 || i == 39) {
                i += 7;
            }
        }
//
//        for (int i = 16; i < 45; i++) {
//            inv.setItem(i, new ItemBuilder(Material.SUGAR)
//                    .display("§fELF §7[ 엘프 ]").lore("",
//                            "§f  유나서버를 대표하는 힐러, 플레이어들의 체력을 회복시켜주고 각종버프로 파티원을 지원해준다 ",
//                            "§f  전직은 프리스트와 템플러가 있다 프리스트는 플레이어의 능력치를 향상시키고 플레이어를 회복시켜준다 ",
//                            "§f  템플러는 힐러와는 다르게 빠른 공격속도와 공격력으로 몬스터를 제압한다 [ 전직 가능 ]",
//                            "",
//                            "§f      §4§l근력 §f: |||||||||||||||||||       §c§l체력 §f: |||||||||||||||||||        §9지력 §f: ||||||||||||||||||| ",
//                            "§f      §9§l정신력 §f: |||||||||||||||||||     §2§l순발력 §f: |||||||||||||||||||      §a§l민첩력 §f: ||||||||||||||||||| ",
//                            "",
//                            "§7   >>§f 위에 추천스텟은 전직전의 스텟입니다 > 전직후는 개인성향에 따라 찍으시면 됩니다 <  ",
//                            ""
//                    ).build());
//            if(i == 17 || i == 26 || i == 35 || i == 42) {
//                i += 7;
//            }
//        }



        return inv;
    }

    public Inventory TribePage2() {
        Inventory inv = Bukkit.createInventory(null, 6*9, "종족 선택창 ( 1 / 2 ) ");
        inv.setItem(18, new ItemBuilder(Material.IRON_NUGGET).display("§f 다음페이지").lore("", "§7 클릭시 다음페이지로 넘어갑니다 ", "§7 현재페이지 ( 1 / 2 )", "").build());
        inv.setItem(27, new ItemBuilder(Material.IRON_NUGGET).display("§f 다음페이지").lore("", "§7 클릭시 다음페이지로 넘어갑니다 ", "§7 현재페이지 ( 1 / 2 )", "").build());
        return inv;
    }
}


