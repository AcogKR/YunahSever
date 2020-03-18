package kr.yunah.yunahsever.serverdata.datalist;

import lombok.Getter;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import sun.security.x509.IPAddressName;

import java.util.Map;
import java.util.UUID;

public class AddressData implements ConfigurationSerializable {

    @Getter
    private final UUID id;
    private final IPAddressName Ip;
    private Boolean check;
    private Integer count;

    public AddressData(UUID id, IPAddressName ip, Boolean check, Integer count) {
        this.id = id;
        Ip = ip;
        this.check = check;
        this.count = count;
    }

    @Override
    public Map<String, Object> serialize() {
        return null;
    }
}
