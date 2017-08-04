package tk.thewoosh.hcf;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import tk.thewoosh.hcf.faction.FactionPlayer;

public class HCFListener implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		FactionPlayer fp = HCF.getManager().getPlayer(e.getPlayer().getUniqueId());
		if (fp == null) {
			fp = new FactionPlayer(e.getPlayer(), Settings.DEFAULT_BALANCE, null);
			HCF.getManager().addPlayer(fp);
			p.sendMessage("§eWelcome to HCF!");
			HCF.getManager().createPlayerProfile(p);
		} else {
			p.sendMessage("§eWelcome back!");
			HCF.getManager().playerJoin(fp);
		}
		e.setJoinMessage(null);
	}
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		HCF.getManager().saveProfile(p.getUniqueId());
	}
	
	@EventHandler
	public void onChat (AsyncPlayerChatEvent e) {
		Player p = e.getPlayer();
		FactionPlayer fp = HCF.getManager().getPlayer(p.getUniqueId());
		e.setCancelled(true);
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					FactionPlayer target = HCF.getManager().getPlayer(p);
					// TODO allies 
					p.sendMessage("§7[" + (target.getFaction() == fp.getFaction() ? "§a" : "§c") + (fp.getFaction() == null ? "None" : fp.getFaction().getName()) + "§r§7] " + fp.getRank().getPrefix() + " " + p.getName() + ": " + (fp.getRank().isStaff() ? "§f" : "§7") + e.getMessage());
				}
			}
		}, "HCF-Chat (" + (System.currentTimeMillis() / 100000000)).start();
	} 
	
}


