package kr.yunah.yunahsever.serverdata.datalist.player;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("YunahStat")
public class Stat implements ConfigurationSerializable {

    //value
    private int Health; // 체력
    private int Strong; // 힘
    private int Quickness; // 순발력
    private int Mentality; // 정신력
    private int Intellect; // 지력
    private int Agility; // 민첩
    private int Stat; // 스텟

    public Stat(int health, int strong, int quickness, int mentality, int intellect, int agility, int stat) {
        this.Health = health;
        this.Strong = strong;
        this.Quickness = quickness;
        this.Mentality = mentality;
        this.Intellect = intellect;
        this.Agility = agility;
        this.Stat = stat;
    }


    public static Stat deserialize(Map<String, Object> map) {
        return new Stat(
            ((Number) map.get("Stat")).intValue(),
            ((Number) map.get("Health")).intValue(),
            ((Number) map.get("Quickness")).intValue(),
            ((Number) map.get("Mentality")).intValue(),
            ((Number) map.get("Intellect")).intValue(),
            ((Number) map.get("Agility")).intValue(),
            ((Number) map.get("Strong")).intValue()
        );
    }


    // Stat Setting Method //

    //Health

    public int getHealth() {
        return Health;
    }

    public void setHealth(int health) {
        Health = health;
    }

    public void addHealth(int health) {
        Health += health;
    }

    public void subHealth(int health) {
        Health -= health;
    }

    //Strong

    public int getStrong() {
        return Strong;
    }

    public void setStrong(int strong) {
        Strong = strong;
    }

    public void addStrong(int strong) {
        Strong += strong;
    }

    public void subStrong(int strong) {
        Strong -= strong;
    }

    //Quickness

    public int getQuickness() {
        return Quickness;
    }

    public void setQuickness(int quickness) {
        Quickness = quickness;
    }

    public void addQuickness(int quickness) {
        Quickness += quickness;
    }

    public void subQuickness(int quickness) {
        Quickness -= quickness;
    }

    //Mentality

    public int getMentality() {
        return Mentality;
    }

    public void setMentality(int mentality) {
        Mentality = mentality;
    }

    public void addMentality(int mentality) {
        Mentality += mentality;
    }

    public void subMentality(int mentality) {
        Mentality -= mentality;
    }

    //Intellect

    public int getIntellect() {
        return Intellect;
    }


    public void setIntellect(int intellect) {
        Intellect = intellect;
    }

    public void addIntellect(int intellect) {
        Intellect += intellect;
    }

    public void subIntellect(int intellect) {
        Intellect -= intellect;
    }

    //Agility

    public int getAgility() {
        return Agility;
    }

    public void setAgility(int agility) {
        Agility = agility;
    }

    public void addAgility(int agility) {
        Agility += agility;
    }

    public void subAgility(int agility) {
        Agility -= agility;
    }

    //Stat

    public int getStat() {
        return Stat;
    }

    public void setStat(int stat) {
        Stat = stat;
    }

    public void addStat(int stat) {
        Stat += stat;
    }

    public void subStat(int stat) {
        Stat -= stat;
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("Stat", Stat)
                .put("Health", Health)
                .put("Strong", Strong)
                .put("Quickness", Quickness)
                .put("Mentality", Mentality)
                .put("Intellect", Intellect)
                .put("Agility", Agility )
                .build();
    }
}
