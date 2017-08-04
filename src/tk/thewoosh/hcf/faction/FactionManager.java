package tk.thewoosh.hcf.faction;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.connection.MySQLManager;
import tk.thewoosh.hcf.rank.Rank;

public class FactionManager {

	private final HCF core;

	private Statement statement, serverStatement;

	private static final ArrayList<Faction> FACTIONS = new ArrayList<>();
	private static final ArrayList<FactionPlayer> PLAYERS = new ArrayList<>();

	public FactionManager(HCF core) {
		this.core = core;
		try {
			int factions = 0;
			int players = 0;
			ResultSet set = getStatement().executeQuery("SELECT * FROM `factions`");
			while (set.next()) {
				FACTIONS.add(new Faction(set));
				factions++;
			}
			set = getStatement().executeQuery("SELECT * FROM `players`");
			while (set.next()) {
				FactionPlayer f = null;
				PLAYERS.add(f = new FactionPlayer(set, this));
				f.setRank(Rank.DEFAULT);
				players++;
			}
			set = getServerStatement().executeQuery("SELECT * FROM `ranks`");
			while (set.next()) {
				getPlayer(UUID.fromString(set.getString("uuid"))).setRank(Rank.valueOf(set.getString("rank")));
			}

			core.getLogger().info("Found: " + players + " players and " + factions + " factions!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public FactionPlayer getPlayer(UUID id) {
		for (FactionPlayer fp : PLAYERS) {
			if (fp == null)
				core.getLogger().info("FactionPlayer in PLAYERS<FactionPlayer> iterated null! This shouldn't happen!");
			if (fp.getId().equals(id))
				return fp;
		}
		return null;
	}

	public FactionPlayer getPlayer(Player player) {
		return getPlayer(player.getUniqueId());
	}

	public Faction getFaction(String string) {
		for (Faction f : FACTIONS)
			if (f.getName().equalsIgnoreCase(String.valueOf(string)))
				return f;
		if (String.valueOf(string) != "null")
			core.getLogger().warning("Player was member of faction: " + string + " but faction couldn't be found!");
		return null;
	}

	public void createPlayerProfile(Player p) {
		try {
			FactionPlayer fp = getPlayer(p);
			if (fp == null)
				core.getLogger().warning("getPlayer returned null!");
			getStatement().executeUpdate("INSERT INTO `players` VALUES('" + p.getUniqueId() + "', '" + fp.getBalance()
					+ "','" + fp.getFactionName() + "', '" + fp.getRole().getId() + "')");
			fp.setRank(Rank.DEFAULT);
			getServerStatement()
					.executeUpdate("INSERT INTO `ranks` VALUE('" + p.getUniqueId() + "', '" + fp.getRank() + "')");
		} catch (Exception e) {
			core.getLogger().warning("ERROR WHILST CREATING PLAYER PROFILE: ");
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public FactionPlayer getPlayer(String name) {
		return getPlayer(Bukkit.getOfflinePlayer(name).getUniqueId());
	}

	public void saveProfile(UUID id) {
		try {
			FactionPlayer fp = getPlayer(id);
			core.getLogger().info("Saving player profile of: " + Bukkit.getOfflinePlayer(id).getName());
			getStatement().executeUpdate("UPDATE `players` SET balance='" + fp.getBalance() + "', faction='" + fp.getFactionName() + "', role='" + fp.getRole().getId() + "' WHERE uuid='" + id + "'");
			getServerStatement()
					.executeUpdate("UPDATE `ranks` SET rank='" + fp.getRank() + "' WHERE uuid='" + fp.getId() + "'");
			core.getLogger().info("Saved profile.");
		} catch (Exception e) {
			core.getLogger().warning("ERROR WHILST SAVING PLAYER PROFILE: ");
			e.printStackTrace();
		}
	}

	public void disable() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				for (FactionPlayer fp : PLAYERS) {
					saveProfile(fp.getId());
				}
				for (Faction f : FACTIONS) {
					saveFaction(f);
				}
				MySQLManager.closeConnection();
			}
		}, "HCF-Disabling").start();
	}
	
	public void addFactionSQL(Faction f) {
		try {
			getStatement().executeUpdate("INSERT INTO `factions` VALUES('" + f.getName() + "', '" + f.getDescription() + "')");
			System.out.println("Successfully created faction: " + f.getName());
		} catch (SQLException e) {
			System.err.println("Couldn't insert faction: " + f.getName());
			e.printStackTrace();
		}
	}
	
	public Statement getStatement() throws SQLException {
		return statement == null ||
				statement.isClosed() ?
						statement = MySQLManager.getConnection().createStatement() : 
							statement;
	}
	
	public Statement getServerStatement() throws SQLException {
		return serverStatement == null || serverStatement.isClosed() ? serverStatement = MySQLManager.getServerConnection().createStatement() : serverStatement;
	}

	private void saveFaction(Faction f) {
		try {
			getStatement().executeUpdate("UPDATE `factions` Set description='" + f.getDescription() + "' WHERE name='" + f.getName() + "'");
			System.out.println("");
		} catch (SQLException e) {
			System.err.println("Couldn't save faction: " + f.getName());
			e.printStackTrace();
		}
	}

	public void addPlayer(FactionPlayer fp) {
		PLAYERS.add(fp);
	}

	public void playerJoin(FactionPlayer fp) {
		try {
			ResultSet rs = getServerStatement().executeQuery("SELECT rank FROM `ranks` WHERE uuid='" + fp.getId() + "'");
			if (rs.next()) {
				fp.setRank(Rank.valueOf(rs.getString("rank")));
			} else {
				fp.setRank(Rank.DEFAULT);
				getServerStatement().executeUpdate("INSERT INTO `ranks` VALUE('" + fp.getId() + "', '" + fp.getRank() + "')");
			}
		} catch (Exception e) {
			core.getLogger()
					.warning("ERROR WHILST GETTING RANK OF PLAYER: " + Bukkit.getPlayer(fp.getId()).getName() + "!");
			e.printStackTrace();
		}
	}

	public void sendUpdateRank(Rank oldRank, Rank newRank, String whoDidIt, String whoGotIt) {
		// TODO
	}

	public void addFaction(Faction f) {
		FACTIONS.add(f);
	}

	public ArrayList<FactionPlayer> getPlayers() {
		return PLAYERS;
	}

}
