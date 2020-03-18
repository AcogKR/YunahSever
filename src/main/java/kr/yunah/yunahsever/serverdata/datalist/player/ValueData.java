package kr.yunah.yunahsever.serverdata.datalist.player;

import com.google.common.collect.ImmutableMap;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

import java.util.Map;

@SerializableAs("YunahValue")
public class ValueData implements ConfigurationSerializable {

    private boolean CallPlayer;
    private boolean JoinMessage;

    public ValueData(boolean callPlayer, boolean joinMessage) {
        CallPlayer = callPlayer;
        JoinMessage = joinMessage;
    }


    // method

    public void setCallPlayer(boolean value) {
        CallPlayer = value;
    }

    public void setJoinMessage(boolean value) {
        JoinMessage = value;
    }


    public boolean isCallPlayer() { return CallPlayer; }

    public boolean isJoinMessage() { return JoinMessage; }

    public static ValueData deserialize(Map<String, Object> map) {
        return new ValueData(
                (boolean) map.get("CallPlayer"),
                (boolean) map.get("JoinMessage")
        );
    }

    @Override
    public Map<String, Object> serialize() {
        return ImmutableMap.<String, Object>builder()
                .put("CallPlayer", CallPlayer)
                .put("JoinMessage", JoinMessage)
                .build();
    }
}
