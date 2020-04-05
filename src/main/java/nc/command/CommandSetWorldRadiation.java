package nc.command;

import nc.capability.radiation.source.IRadiationSource;
import nc.radiation.RadiationHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.Chunk;

public class CommandSetWorldRadiation extends CommandBase {
	
	@Override
	public String getName() {
		return "nc_set_world_radiation";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.nuclearcraft.set_world_radiation.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 1) {
			double newRadiation = 0D;
			try {
				newRadiation = Double.parseDouble(args[0]);
			}
			catch (NumberFormatException e) {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
			
			for (Chunk chunk : server.getWorld(sender.getEntityWorld().provider.getDimension()).getChunkProvider().getLoadedChunks()) {
				if (chunk != null && chunk.isLoaded()) {
					IRadiationSource chunkSource = RadiationHelper.getRadiationSource(chunk);
					if (chunkSource != null) chunkSource.setRadiationLevel(newRadiation);
				}
			}
		}
		
		else throw new WrongUsageException(getUsage(sender), new Object[0]);
	}
}
