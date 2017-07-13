package nc.worldgen;

import com.google.common.base.Predicate;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class UniversalPredicate implements Predicate<IBlockState> {
	
	UniversalPredicate() {
		
	}
	
	public boolean apply(IBlockState input) {
		if (input != null) {
			if (input.getBlock() == Blocks.STONE || input.getBlock() == Blocks.NETHERRACK || input.getBlock() == Blocks.END_STONE) return true;
		}
		return false;
	}
}
