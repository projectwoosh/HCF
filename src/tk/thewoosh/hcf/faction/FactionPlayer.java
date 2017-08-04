package tk.thewoosh.hcf.faction;

import java.sql.ResultSet;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.rank.Rank;

public class FactionPlayer {

	private int balance;
	private final UUID id;
	private Faction faction;
	private Rank rank;
	private FactionRole role = FactionRole.RECRUIT;
		
	public FactionPlayer(Player player, int balance, Faction faction) {
		this.id = player.getUniqueId();
		this.balance = balance;
		this.faction = faction;
	}
	
	public Rank getRank() {
		return rank;
	}
	
	public void setRank(Rank rank) {
		this.rank = rank;
	}
	
	public FactionPlayer(ResultSet set, FactionManager manager) throws Exception {
		this.id = UUID.fromString(set.getString("uuid"));
		this.balance = set.getInt("balance");
		this.faction = manager.getFaction(set.getString("faction"));
		this.role = FactionRole.values()[set.getInt("role")];
	}
	
	public int getBalance() {
		return balance;
	}
	public FactionRole getRole() {
		return role;
	}
	
	public void setRole(FactionRole role) {
		this.role = role;
	}
	
	public Faction getFaction() {
		return faction;
	}
	
	public UUID getId() {
		return id;
	}

	public String getFactionName() {
		return faction == null ? "null" : faction.getName();
	}
	
	public String getName() {
		return Bukkit.getOfflinePlayer(getId()).getName();
	}

	public void setFaction(Faction f) {
		this.faction = f;
	}
	
}
