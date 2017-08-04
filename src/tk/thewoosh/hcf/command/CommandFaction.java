package tk.thewoosh.hcf.command;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.Settings;
import tk.thewoosh.hcf.faction.Faction;
import tk.thewoosh.hcf.faction.FactionManager;
import tk.thewoosh.hcf.faction.FactionPlayer;
import tk.thewoosh.hcf.faction.FactionRole;

public class CommandFaction implements CommandExecutor{

	private static final HashMap<String, String> COMMANDS = new HashMap<>();
	
	static {
		COMMANDS.put("create", "Create a faction."); 
		COMMANDS.put("join", "Allows you to join a faction.");
		COMMANDS.put("leave", "Allows you to leave a faction.");
		COMMANDS.put("info", "Shows info about your faction.");
		COMMANDS.put("who", "Shows info about a faction.");
		COMMANDS.put("desc", "Sets the description of your faction");
		COMMANDS.put("description", "Sets the description of your faction");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			FactionManager fm = HCF.getManager();
			FactionPlayer fp = fm.getPlayer(p);
			Faction f = fp.getFaction();
			if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
				p.sendMessage("§7 -=§aFactions Help§7=-");
				for (String s : COMMANDS.keySet()) {
					p.sendMessage("   §7- §a" + s + "§7: " + COMMANDS.get(s));
				}
			} else {
				if (args[0].equalsIgnoreCase("create")) {
					if (fp.getFaction() != null) {
						p.sendMessage("§7You are already in faction: §a" + fp.getFactionName());
					} else {
						if (args.length == 1) {
							p.sendMessage("§cYou need to specify a name!");
						} else {
							if (args.length == 2) {
								if (fm.getFaction(args[1]) != null) {
									p.sendMessage("§7A faction already claimed that name. You can try to join, or choose another name.");
									return true;
								}
								f = new Faction(args[1], Settings.DEFAULT_FACTION_DESCRIPTION);
								fm.addFaction(f);
								fm.addFactionSQL(f);
								fp.setFaction(f);
								String message = "§7Faction: §a" + args[1] + " §7was created by: §a" + p.getName();
								for (Player pl : Bukkit.getOnlinePlayers())
									if (pl != p)
										pl.sendMessage(message);
								fp.setRole(FactionRole.LEADER);
								Bukkit.getLogger().info("[FactionNotifier]: " + ChatColor.translateAlternateColorCodes('§', message));
								p.sendMessage("§7You created faction: §a" + args[1]);
							} else {
								
							}
						}
					}
				} else if (args[0].equalsIgnoreCase("info") || (args.length == 1 && args[0].equalsIgnoreCase("who"))) {
					if (f != null) {
						p.sendMessage("§aShowing your factions info");
						p.sendMessage(" §7Name: §a" + f.getName());
						p.sendMessage(" §7Description: §a" + f.getDescription());
						ArrayList<FactionPlayer> members = new ArrayList<>();
						for (FactionPlayer facpl : fm.getPlayers()) {
							if (facpl.getFaction() != null && (facpl.getFaction() == f || facpl.getFactionName() == f.getName() || facpl.getFaction().getName().equalsIgnoreCase(f.getName()))) {
								members.add(facpl);
							}
						}
						if (fp.getRole() == FactionRole.LEADER) {
							p.sendMessage(" §7Leader: §a" + fp.getName() + "§7 / §aYou");
						} else {
							for (FactionPlayer fpl : members) {
								if (fpl.getRole() == FactionRole.LEADER) {
									p.sendMessage(" §7Leader: §a" + fpl.getName());
									break;
								}
							}
						}
						String mods = "", recruits = "";
						for (FactionPlayer fpl : members) {
							if (fpl == fp) continue;
							if (fpl.getRole() == FactionRole.MOD) {
								mods += (mods == "" ? "§a" : "§7, §a") + fpl.getName();
							} else {
								recruits += (recruits == "" ? "§a" : "§7, §a") + fpl.getName();
							}
						}
						if (mods == "") 
							mods = "No mods.";
						if (recruits == "")
							recruits = "No recruits";
						p.sendMessage(" §7Mods: §a" + mods);
						p.sendMessage(" §7Recruits: §a" + recruits);
					} else {
						p.sendMessage("§7You are not in a faction!");
					}
				} else if (args[0].equalsIgnoreCase("desc") || args[0].equalsIgnoreCase("description")) {
					if (f == null) {
						p.sendMessage("§7You are not in a faction!");
					} else {
						if (args.length == 1) {
							p.sendMessage("§7Description: §a" + f.getDescription());
							return true;
						}
						if (fp.getRole() == FactionRole.RECRUIT) {
							p.sendMessage("§7You are not allowed to change §ayour factions description§7! Maybe ask a mod or the leader?");
						} else {
							String newDesc = "";
							if (args.length == 2) {
								newDesc = args[1];
							}else
							for (int i = 1; i < args.length; i++) {
								newDesc += args[i];
							}
							f.setDescription(newDesc);
							p.sendMessage("§7You have set the description to: " + newDesc);
							f.notifyMembers("§a" + p.getName() + " §7has changed the description to: §a" + newDesc); 
						}
					}
				} else if (args[0].equalsIgnoreCase("who") && args.length > 1) {
					FactionPlayer target = fm.getPlayer(args[1]);
					if (target == null) {
						p.sendMessage("§7Player not found!");
					} else {
						p.sendMessage("§7Showing info of player: §a" + target.getName());
						if (target.getFaction() != null) {
							p.sendMessage("  §7Faction: §a" + target.getFaction().getName());
							p.sendMessage("  §7Faction Role: §a" + target.getRole().toString().toLowerCase());
						} else 
							p.sendMessage("  §7This player is not in a faction.");
						p.sendMessage("  §7Rank: §r" + target.getRank().getPrefix());
					}
				}
			}
		}
		return false;
	}
	
}
