package tk.thewoosh.hcf;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import tk.thewoosh.hcf.claims.ClaimManager;
import tk.thewoosh.hcf.command.CommandBalance;
import tk.thewoosh.hcf.command.CommandClaim;
import tk.thewoosh.hcf.command.CommandFaction;
import tk.thewoosh.hcf.command.CommandUpdateRank;
import tk.thewoosh.hcf.connection.MySQLManager;
import tk.thewoosh.hcf.faction.FactionManager;

public class HCF extends JavaPlugin {
	
	private static FactionManager MANAGER = null;
	
	@Override
	public void onEnable() {
		MySQLManager.openConnection();
		MANAGER = new FactionManager(this);
		Bukkit.getPluginManager().registerEvents(new HCFListener(), this);
		getCommand("updaterank").setExecutor(new CommandUpdateRank());
		CommandBalance bal;
		CommandFaction fac;
		getCommand("balance").setExecutor(bal = new CommandBalance());
		getCommand("bal").setExecutor(bal);
		getCommand("f").setExecutor(fac = new CommandFaction());
		getCommand("faction").setExecutor(fac);
		getCommand("claim").setExecutor(new CommandClaim());
		
		ClaimManager.loadClaims();
	}
	
	public static FactionManager getManager() {
		return MANAGER;
	}
	
	@Override
	public void onDisable() {
		MANAGER.disable();
	}
}
