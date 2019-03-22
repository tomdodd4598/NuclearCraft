package nc.command;

import nc.capability.radiation.source.IRadiationSource;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.chunk.Chunk;

public class CommandSetChunkRadiation extends CommandBase {
	
	@Override
	public String getName() {
		return "nc_set_chunk_radiation";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.nuclearcraft.set_chunk_radiation.usage";
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
			
			Chunk chunk = sender.getEntityWorld().getChunkFromChunkCoords((int)sender.getPosition().getX() >> 4, (int)sender.getPosition().getZ() >> 4);
			
			if (chunk != null && chunk.isLoaded() && chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
				IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
				if (chunkRadiation != null) chunkRadiation.setRadiationLevel(newRadiation);
			}
		}
		
		else if (args.length == 3) {
			double newRadiation = 0D;
			try {
				newRadiation = Double.parseDouble(args[0]);
			}
			catch (NumberFormatException e) {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
			
			int x = (int)sender.getPosition().getX();
			try {
				x = Integer.parseInt(args[1]);
			}
			catch (NumberFormatException e) {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
			
			int z = (int)sender.getPosition().getZ();
			try {
				z = Integer.parseInt(args[2]);
			}
			catch (NumberFormatException e) {
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
			
			Chunk chunk = sender.getEntityWorld().getChunkFromChunkCoords(x >> 4, z >> 4);
			
			if (chunk != null && chunk.isLoaded() && chunk.hasCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null)) {
				IRadiationSource chunkRadiation = chunk.getCapability(IRadiationSource.CAPABILITY_RADIATION_SOURCE, null);
				if (chunkRadiation != null) chunkRadiation.setRadiationLevel(newRadiation);
			}
		}
		
		else throw new WrongUsageException(getUsage(sender), new Object[0]);
	}
}
