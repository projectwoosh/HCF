package tk.thewoosh.hcf.command;

import static org.bukkit.ChatColor.GOLD;
import static org.bukkit.ChatColor.GRAY;
import static org.bukkit.ChatColor.RED;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import tk.thewoosh.hcf.HCF;
import tk.thewoosh.hcf.faction.FactionPlayer;

public class CommandBalance implements CommandExecutor {
	
	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			FactionPlayer fp = HCF.getManager().getPlayer(p);
			sender.sendMessage(GRAY + "Your balance is: " + GOLD + fp.getBalance());
		} else {
			if (args.length != 1) {
				sender.sendMessage(RED + "Usage: /balance <player>");
			} else {
			    FactionPlayer fp = HCF.getManager().getPlayer(args[0]);
			    if (fp == null) {
				    sender.sendMessage(RED + "That player has bever joined the server!");
		    	} else {
			      	sender.sendMessage(GOLD + fp.getName() + GRAY + "'s balance is: " + GOLD + fp.getBalance());
		    	}
			}
		}
		return false;
	}
	
}
