package tk.thewoosh.hcf.rank;

import static org.bukkit.ChatColor.*;

public enum Rank {
	
	DEFAULT(GRAY.toString(), false),
	DEVELOPER(LIGHT_PURPLE.toString() + BOLD + "DEVELOPER" + RESET.toString() + GRAY, true),
	MODERATOR(DARK_AQUA.toString() + BOLD + "MOD" + RESET.toString() + AQUA, true),
	ADMIN(DARK_RED.toString() + BOLD + "ADMIN" + RESET.toString() + RED.toString(), true),
	OWNER(DARK_PURPLE.toString() + BOLD + "OWNER" + RESET.toString() + LIGHT_PURPLE, true);
   
    private boolean staff;
	private String prefix;
	
	private Rank(String prefix, boolean staff) {
		this.staff = staff;
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public boolean isStaff() {
		return staff;
	}
	
	public boolean hasFullPermissions() {
	    return this != MODERATOR && isStaff();	
    }

	public static Rank getRank(String string) {
		for (Rank r : values())
			if (r.toString().equalsIgnoreCase(string))
				return r;
		return null;
	}

	public static String getRanks() {
		String result = "";
		for (Rank r : values())
			result += (result != "" ? ", " : "") + r.toString();
		return result;
	}

}	