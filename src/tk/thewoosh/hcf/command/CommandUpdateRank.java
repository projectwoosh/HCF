package tk.thewoosh.hcf.command;

import static org.bukkit.ChatColor.*;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.faction.FactionPlayer;
import tk.thewoosh.hcf.rank.Rank;

public class CommandUpdateRank implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			FactionPlayer fp = HCF.getManager().getPlayer(p);
			if (fp.getRank().hasFullPermissions()) {
				 updateRank(sender, args);
			}
		} else if (sender instanceof ConsoleCommandSender){
			updateRank(sender, args);
		}
		return false;
	}
	
	public void updateRank(CommandSender sender,  String[] args) {
		if (args.length != 2) {
			sender.sendMessage(RED + "Usage: /updaterank <player> <rank>");
		} else {
			FactionPlayer fp = HCF.getManager().getPlayer(args[0]);
			if (fp == null) {
				sender.sendMessage(RED + "That player has never joined the server.");
			} else {
				Rank oldRank = fp.getRank();
				Rank newRank = Rank.getRank(args[1]);
				if (newRank == null) {
					sender.sendMessage(RED + "Invalid rank! Ranks: " + Rank.getRanks());
				} else {
					fp.setRank(newRank);
					sender.sendMessage(GOLD + fp.getName() + GRAY + "'s rank has been updated from: " + GOLD + oldRank.toString() + GRAY + " to: " + GOLD + newRank.toString() + GRAY + ".");
					HCF.getManager().sendUpdateRank(oldRank, newRank, sender.getName(), fp.getName());
				}
			}
		}
	}

}
