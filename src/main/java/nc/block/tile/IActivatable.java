package nc.block.tile;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public interface IActivatable {
	
	public void setState(boolean active, World world, BlockPos pos);
}
