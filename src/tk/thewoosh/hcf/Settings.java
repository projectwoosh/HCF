package tk.thewoosh.hcf;

import static org.bukkit.ChatColor.*;

public class Settings {

	public static final String MYSQL_HOST = "localhost";
	public static final String MYSQL_PORT = "3306";
	public static final String MYSQL_USER = "root";
	public static final String MYSQL_PASSWORD = "";
	public static final String MYSQL_DATABASE = "factions";
	public static final String MYSQL_SERVER_DATABASE = "server";
	public static final int MAX_FACTION_NAME_LENGTH = 20;
	
	public static final int DEFAULT_BALANCE = 200;
	public static final String DEFAULT_FACTION_DESCRIPTION = "No description.";
	
	public static final String INVALID_COMMAND_PERMISSION = RED + "You are not allowed to perform that command.";
	public static final String NO_ARGUMENTS = RED + "One or more arguments were expected!";
	
	public static final String ENTER_CHUNK = GRAY + "You entered the chunk of " + GREEN + "type";
	
	public static final String CLAIM_ROLE = GRAY + "You are not allowed to claim in this faction.";
	public static final String CLAIM_CLAIMED = GRAY + "This chunk is already claimed.";
	public static final String CLAIM_FACTION = GRAY + "You cannot claim because you're not in a faction!";
	public static final String CLAIM_DONE = GREEN + "who " + GRAY + "claimed a chunk at: " + GREEN + "x" + GRAY + ", " + GREEN + "z";
	public static final String CLAIM_USABLE = GRAY + "You can use these claim types: " + GREEN + "CLAIMTYPES";
	public static final String CLAIM_STAFF = GRAY + "You claimed this chunk.";
	
}
