package nc.worldgen.ore;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class UniversalPredicate implements Predicate<IBlockState> {
	
	public UniversalPredicate() {
		
	}
	
	@Override
	public boolean apply(IBlockState input) {
		if (input != null) {
			if (input.getBlock() == Blocks.STONE) {
				BlockStone.EnumType blockstone$enumtype = (BlockStone.EnumType)input.getValue(BlockStone.VARIANT);
				return blockstone$enumtype.isNatural();
            }
			if (input.getBlock() == Blocks.NETHERRACK || input.getBlock() == Blocks.END_STONE) return true;
		}
		return false;
	}
}
