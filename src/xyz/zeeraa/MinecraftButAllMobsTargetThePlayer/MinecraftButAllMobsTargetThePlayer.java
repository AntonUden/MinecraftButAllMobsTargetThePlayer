package xyz.zeeraa.MinecraftButAllMobsTargetThePlayer;

import java.util.Collection;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Enderman;
import org.bukkit.entity.PigZombie;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class MinecraftButAllMobsTargetThePlayer extends JavaPlugin implements Listener {
	private UUID targetPlayer;

	@Override
	public void onEnable() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
			@Override
			public void run() {
				if(targetPlayer == null) {
					return;
				}
				
				Player target = Bukkit.getPlayer(targetPlayer);
				if(target == null) {
					return;
				}
				
				Collection<Creature> creatures = target.getWorld().getEntitiesByClass(Creature.class);
				for(Creature creature : creatures) {
					if(creature instanceof Enderman) {
						continue;
					}
					if(creature instanceof PigZombie) {
						continue;
					}
					
					creature.setTarget(target);
				}
			}
		}, 10L, 10L);
		getServer().getPluginManager().registerEvents(this, this);
	}

	@Override
	public void onDisable() {
		getServer().getScheduler().cancelTasks(this);
		HandlerList.unregisterAll((Plugin) this);
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if(label.equalsIgnoreCase("settarget")) {
			if(sender instanceof Player) {
				sender.sendMessage(ChatColor.GOLD + "Mob target updated to " + sender.getName());
				targetPlayer = ((Player) sender).getUniqueId();
				return true;
			} else {
				sender.sendMessage(ChatColor.DARK_RED + "Only players can use this command");
			}
		}
		return false;
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e) {
		if(e.getPlayer().getUniqueId() == targetPlayer) {
			targetPlayer = null;
		}
	}
}