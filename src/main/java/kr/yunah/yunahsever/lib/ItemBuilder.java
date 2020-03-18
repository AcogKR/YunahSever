package kr.yunah.yunahsever.lib;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.Arrays;

public class ItemBuilder {

    private final ItemStack item;

    public ItemBuilder(Material item) {
        this.item = new ItemStack(item);
    }

    public ItemBuilder display(String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder setplayer(Player player) {
        try {
            SkullMeta it = (SkullMeta) item.getItemMeta();
            it.setOwningPlayer(player);
            item.setItemMeta(it);
        } catch (ClassCastException e) {}
        return this;
    }

    public ItemBuilder lore(String... lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(Arrays.asList(lore));
        item.setItemMeta(meta);
        return this;
    }

    public ItemBuilder amount(int i) {
        item.setAmount(i);
        return this;
    }

    public ItemStack build() {
        return item;
    }
}
