package kr.yunah.yunahsever.methodlist.server.transport;

import kr.yunah.yunahsever.serverdata.TransportManager;
import kr.yunah.yunahsever.serverdata.datalist.TransportData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class TransportEventListener implements Listener {

    private final TransportManager transportManager;

    public TransportEventListener(TransportManager transportManager) {
        this.transportManager = transportManager;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if(e.getView().getTitle().equals("이동수단 목록")) {
            e.setCancelled(true);
            if(e.getCurrentItem() != null) {
                ItemStack item = e.getCurrentItem();
                String name = e.getCurrentItem().getItemMeta().getDisplayName().substring(2);
                if(transportManager.iscontain(name)) {
                    TransportData transportData = transportManager.get(name);
                    if(e.getClick().equals(ClickType.DOUBLE_CLICK)) {
                        e.getWhoClicked().getInventory().addItem(transportManager.item(name));
                    } else if(e.getClick().equals(ClickType.SHIFT_LEFT)) {
                        transportManager.remove(name);
                        e.getWhoClicked().getInventory().remove(e.getCurrentItem());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onSwap(PlayerSwapHandItemsEvent e) {
        if (e.getMainHandItem() == null) { return; }
        String name = (e.getMainHandItem().getItemMeta() != null) ? e.getMainHandItem().getItemMeta().getDisplayName() : null;
        if(name != null && name.contains("탈것")) {
            e.setCancelled(true);
            Player player = e.getPlayer();
            if(transportManager.check(e.getPlayer().getUniqueId())) {
                UUID entity = transportManager.getPlayer(player.getUniqueId());
                Entity horse = Bukkit.getEntity(entity);
                if(horse == null) { return; }
                horse.remove();
                transportManager.removePlayer(player.getUniqueId());
                player.sendMessage("§b이동수단 §f: 말을 들여보냈습니다");
                return;
            } else {
                String name1 = e.getMainHandItem().getItemMeta().getDisplayName().substring(9);
                if(transportManager.iscontain(name1)) {
                    transportManager.spawn(e.getPlayer(), name1);
                    player.sendMessage("§b이동수단 §f: 말을 불러왔습니다");
                } else {
                    e.getPlayer().sendMessage("§8오류 §f: 잘못된 소환입니다 ");
                    player.getInventory().remove(e.getMainHandItem());
                }
            }
        }
    }


    @EventHandler
    public void onExit(VehicleExitEvent e) {
        Player player = (Player) e.getExited();
        if(transportManager.check(player.getUniqueId())) {
            Entity horse = Bukkit.getEntity(transportManager.getPlayer(player.getUniqueId()));
            transportManager.removePlayer(player.getUniqueId());
            player.sendMessage("§b이동수단 §f: 말을 들여보냈습니다");
            horse.remove();
            return;
        }
    }


    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e) {
        if(e.getEntity().getName().contains("T §7")) {
            e.setCancelled(true);
        }
    }
}
