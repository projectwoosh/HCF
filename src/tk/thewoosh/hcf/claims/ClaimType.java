package tk.thewoosh.hcf.claims;

import org.bukkit.ChatColor;

public enum ClaimType {

	ROAD(ChatColor.DARK_PURPLE, "Road"), FACTION(null, ""), SPAWN(ChatColor.GREEN, "Spawn"), WARZONE(ChatColor.RED, "Warzone");

	private ChatColor color;
	private String string;
	
	private ClaimType(ChatColor color, String string) {
		this.color = color;
		this.string = string;
	}
	
	public String getDisplayName() {
		return string;
	}
	
	public ChatColor getColor() {
		return color;
	}
	
}
