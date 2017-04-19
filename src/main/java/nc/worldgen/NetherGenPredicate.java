package nc.worldgen;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class NetherGenPredicate implements Predicate<IBlockState> {
	
	public boolean apply(IBlockState input) {
		return input != null && input.getBlock() == Blocks.NETHERRACK;
	}
}
