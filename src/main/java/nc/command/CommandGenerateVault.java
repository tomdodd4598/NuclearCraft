package nc.command;

import nc.worldgen.structure.vault.VaultGenerator;
import net.minecraft.command.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.*;
import net.minecraft.world.gen.structure.StructureBoundingBox;

public class CommandGenerateVault extends CommandBase {
	
	public static int BACKUP_CHUNK_RADIUS = 15;
	
	@Override
	public String getName() {
		return "nc_generate_vault";
	}
	
	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.nuclearcraft.generate_vault.usage";
	}
	
	@Override
	public int getRequiredPermissionLevel() {
		return 2;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length > 0) {
			throw new WrongUsageException(getUsage(sender));
		}
		
		EntityPlayerMP player = getCommandSenderAsPlayer(sender);
		World world = player.getEntityWorld();
		if (!(world instanceof WorldServer worldServer)) {
			return;
		}
		
		int chunkX = player.chunkCoordX, chunkZ = player.chunkCoordZ;
		VaultGenerator vaultGenerator = new VaultGenerator();
		
		StructureBoundingBox bounds = vaultGenerator.forceGenerateAndGetBounds(worldServer, chunkX, chunkZ);
		int minChunkX, minChunkZ, maxChunkX, maxChunkZ;
		if (bounds == null) {
			minChunkX = chunkX - BACKUP_CHUNK_RADIUS;
			maxChunkX = chunkX + BACKUP_CHUNK_RADIUS;
			minChunkZ = chunkZ - BACKUP_CHUNK_RADIUS;
			maxChunkZ = chunkZ + BACKUP_CHUNK_RADIUS;
		}
		else {
			minChunkX = (bounds.minX >> 4) - 1;
			maxChunkX = (bounds.maxX >> 4) + 1;
			minChunkZ = (bounds.minZ >> 4) - 1;
			maxChunkZ = (bounds.maxZ >> 4) + 1;
		}
		
		int chunkCountX = maxChunkX - minChunkX + 1;
		int chunkCountZ = maxChunkZ - minChunkZ + 1;
		int totalChunks = chunkCountX * chunkCountZ;
		
		for (int x = minChunkX; x <= maxChunkX; ++x) {
			for (int z = minChunkZ; z <= maxChunkZ; ++z) {
				worldServer.getChunkProvider().provideChunk(x, z);
				vaultGenerator.generateStructure(worldServer, worldServer.rand, new ChunkPos(x, z));
			}
		}
	}
}
