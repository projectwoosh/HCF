package tk.thewoosh.hcf.claims;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.connection.MySQLManager;

public class ClaimManager {

	private final static HashMap<Chunk, ClaimInfo> CLAIMS = new HashMap<>(); 
	
	public static void loadClaims() {
		int count = 0;
		try {
			ResultSet rs = MySQLManager.getConnection().createStatement().executeQuery("SELECT * FROM `claims`");
			while(rs.next()) {
				CLAIMS.put(Bukkit.getWorld("world").getChunkAt(rs.getInt("x"), rs.getInt("z")), new ClaimInfo(ClaimType.valueOf(rs.getString("type")), HCF.getManager().getFaction(rs.getString("faction"))));
				count++;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			System.out.println("[ClaimManager] Loaded " + count + " claims.");
		}
	}
	
	public static boolean isClaimed(Chunk chunk) {
		return CLAIMS.containsKey(chunk);
	}
	
	public static void registerClaim(Chunk chunk, ClaimInfo info) {
		try {
			MySQLManager.getConnection().createStatement().executeUpdate("INSERT INTO `claims` VALUES ('" + info.getType() + "', " + chunk.getX() + ", " + chunk.getZ() + ", '" + info.getFaction() + "')");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		CLAIMS.put(chunk, info);
	}

	public static ClaimInfo getClaimInfo(Chunk c) {
		return CLAIMS.get(c);
	}
	
}
