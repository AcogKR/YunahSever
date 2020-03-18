package kr.yunah.yunahsever.serverdata.datalist.player;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("YunahLevel")
public class LevelData implements ConfigurationSerializable {

    //value
    private int level;
    private int exp;
    private int mexp;

    public LevelData(int level, int exp, int mexp) {
        this.level = level;
        this.exp = exp;
        this.mexp = mexp;
    }


    //level Setting 뭔소리야
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void addLevel(int level) {
        this.level = getLevel() + level;
    }

    //Exp Setting
    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public void addExp(int exp) {
        this.exp = getExp() + exp;
    }

    public void subExp(int exp) {
        this.exp = getExp() - exp;
    }

    //Max Exp
    public int getMexp() {
        return mexp;
    }

    public void setMexp(int mexp) {
        this.mexp = mexp;
    }

    public void addMexp(int mexp) {
        this.mexp = getMexp() + mexp;
    }

    public void subMexp(int mexp) {
        this.mexp = getMexp() - mexp;
    }

    public static LevelData deserialize(Map<String, Object> map) {
        return new LevelData(
                ((Number) map.get("level")).intValue(),
                ((Number) map.get("exp")).intValue(),
                ((Number) map.get("mexp")).intValue()
        );
    }
    // 이건 데이터를 직렬화하는거야
    // 콘피그에 데이터를 써야되잖아
    // 콘피그에서 가져오면 알아서 객체 생성해줘
    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("level", level)
                .put("exp", exp)
                .put("mexp", mexp)
                .build();
    }
}
