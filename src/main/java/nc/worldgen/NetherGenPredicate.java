package nc.worldgen;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

import com.google.common.base.Predicate;

public class NetherGenPredicate implements Predicate<IBlockState> {
	
	public boolean apply(IBlockState input) {
		return input != null && input.getBlock() == Blocks.NETHERRACK;
	}
}
