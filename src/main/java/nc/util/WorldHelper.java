package nc.util;

import nc.NuclearCraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class WorldHelper {
	
	public static TileEntity getTileClient(int dimension, int x, int y, int z) {
		World world = NuclearCraft.proxy.getWorld(dimension);
		if (world == null)
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
	public static TileEntity getTileServer(int dimension, int x, int y, int z) {
		World world = getWorld(dimension);
		if (world == null)
			return null;
		return world.getTileEntity(new BlockPos(x, y, z));
	}
	
	public static World getWorld(int dimension) {
		return FMLCommonHandler.instance().getMinecraftServerInstance().getWorld(dimension);
	}
	
	public static TileEntity getLoadedTile(World world, BlockPos pos) {
		if(world.isBlockLoaded(pos)) return world.getTileEntity(pos);
		return null;
	}
}
