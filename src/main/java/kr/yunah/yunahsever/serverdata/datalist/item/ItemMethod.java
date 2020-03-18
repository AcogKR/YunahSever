package kr.yunah.yunahsever.serverdata.datalist.item;

import kr.yunah.yunahsever.lib.enumlist.Rating;
import kr.yunah.yunahsever.lib.enumlist.item.Weapon;


public class ItemMethod {

    public String rating(Rating rating) {
        switch (rating) {
            case NORMAL:
                return "일반";
            case EPIC:
                return "에픽";
            case RARE:
                return "희귀";
            case LEGEND:
                return "전설";
            case UNKNOWN:
                return "???";
        }
        return null;
    }

    public String weapon(Weapon weapon) {
        switch (weapon) {
            case ELF:
                return "힐러";
            case PRIEST:
                return "프리스트";
            case TEMPLAR:
                return "템플러";
            case HUMAN:
                return "기사";
            case DEFENDER:
                return "디펜더";
            case GUARDIAN:
                return "가디언";
            case HALF_ELF:
                return "궁수";
            case SCOUT:
                return "스카우트";
            case DHAN:
                return "암살자";
            case AVENGER:
                return "어벤저";
            case PREDATOR:
                return "프레데터";
            case DEKAN:
                return "마검사";
            case DRAGON_KNIGHT:
                return "드래곤 나이트";
            case DRAGON_SAGE:
                return "드래곤 세이지";
            case DARK_ELF:
                return "메이지";
            case WARLOCK:
                return "워록";
            case WIZARD:
                return "위자드";
        }
        return null;
    }
}
