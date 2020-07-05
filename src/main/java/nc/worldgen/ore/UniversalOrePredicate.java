package nc.worldgen.ore;

import javax.annotation.Nullable;

import com.google.common.base.Predicate;

import net.minecraft.block.BlockStone;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;

public class UniversalOrePredicate implements Predicate<IBlockState> {
	
	@Override
	public boolean apply(@Nullable IBlockState state) {
		if (state != null) {
			if (state.getBlock() == Blocks.STONE) {
				BlockStone.EnumType blockstone$enumtype = state.getValue(BlockStone.VARIANT);
				return blockstone$enumtype.isNatural();
			}
			if (state.getBlock() == Blocks.NETHERRACK || state.getBlock() == Blocks.END_STONE) {
				return true;
			}
		}
		return false;
	}
}
