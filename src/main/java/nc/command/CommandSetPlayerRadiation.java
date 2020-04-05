package nc.command;

import nc.capability.radiation.entity.IEntityRads;
import nc.radiation.RadiationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

public class CommandSetPlayerRadiation extends CommandBase {
	
	@Override
	public String getName() {
		return "nc_set_player_radiation";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.nuclearcraft.set_player_radiation.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length >= 1 && args.length <= 2) {
			double newRadiation = 0D;
			try {
				newRadiation = Double.parseDouble(args[0]);
			}
			catch (NumberFormatException e) {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
			
			EntityPlayer player = args.length == 2 ? getPlayer(server, sender, args[1]) : getCommandSenderAsPlayer(sender);
			if (player != null) {
				IEntityRads playerRads = RadiationHelper.getEntityRadiation(player);
				if (playerRads != null) playerRads.setTotalRads(newRadiation, false);
			}
		}
		
		else throw new WrongUsageException(getUsage(sender), new Object[0]);
	}
}
