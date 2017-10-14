package tk.thewoosh.hcf.command;

import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.Settings;
import tk.thewoosh.hcf.claims.ClaimInfo;
import tk.thewoosh.hcf.claims.ClaimManager;
import tk.thewoosh.hcf.claims.ClaimType;
import tk.thewoosh.hcf.faction.Faction;
import tk.thewoosh.hcf.faction.FactionPlayer;
import tk.thewoosh.hcf.faction.FactionRole;

public class CommandClaim implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (!(sender instanceof Player)) return false;
		Player player = (Player) sender;
		FactionPlayer fp = HCF.getManager().getPlayer(player);
		Faction faction = fp.getFaction();
		if (!fp.getRank().hasFullPermissions()) {
			handleNormal(faction, player, fp);
		} else {
			if (args.length == 0) {
				handleTypes(player);
			} else {
				ClaimType type;
				try {
					type = ClaimType.valueOf(args[0]);
				} catch(Exception e) {
					player.sendMessage(e.getClass().getSimpleName());
					handleTypes(player);
					return false; 
				}
				if (type.equals(ClaimType.FACTION)) {
					handleNormal(faction, player, fp);
				} else {
					Chunk c = player.getLocation().getChunk();
					if (ClaimManager.isClaimed(c)) {
						player.sendMessage(Settings.CLAIM_CLAIMED);
					} else {
						ClaimManager.registerClaim(c, new ClaimInfo(type, null));
						player.sendMessage(Settings.CLAIM_STAFF);
						HCF.getManager().sendStaffClaim(c, fp);
					}
				}
			}
		}
		return false;
	}
	
	private void handleTypes(Player player) {
		StringBuilder sb = new StringBuilder();
		for (ClaimType type : ClaimType.values())
			sb.append(type.toString() + ", ");
		String s = sb.toString();
		s = s.substring(0, s.length()-2);
		player.sendMessage(Settings.CLAIM_USABLE.replaceAll("CLAIMTYPES", s));
	}

	public void handleNormal(Faction faction, Player player, FactionPlayer fp) {
		Chunk c = player.getLocation().getChunk();
		if (faction != null) {
			if (fp.getRole() == FactionRole.RECRUIT) {
				player.sendMessage(Settings.CLAIM_ROLE);
			} else {
				// TODO Max claims...?
				if (ClaimManager.isClaimed(c)) {
					player.sendMessage(Settings.CLAIM_CLAIMED);
				} else {
					ClaimManager.registerClaim(c, new ClaimInfo(ClaimType.FACTION, faction));
					faction.notifyMembers(Settings.CLAIM_DONE.replaceAll("who", player.getName()).replaceAll("x", "" + c.getX()).replaceAll("z", "" + c.getZ()));
				}
			}
		} else {
			player.sendMessage(Settings.CLAIM_FACTION);
		}
	}
	
}
