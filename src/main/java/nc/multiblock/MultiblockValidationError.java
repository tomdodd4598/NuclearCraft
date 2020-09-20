package nc.multiblock;

import nc.Global;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

public class MultiblockValidationError {
	
	public static final MultiblockValidationError VALIDATION_ERROR_TOO_FEW_PARTS = new MultiblockValidationError("zerocore.api.nc.multiblock.validation.too_few_parts", null);
	
	public MultiblockValidationError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
		_resourceKey = messageFormatStringResourceKey;
		_parameters = messageParameters;
		this.pos = pos;
	}
	
	/** @return the position of the last validation error encountered when trying to assemble the multiblock, or null if there is no position. */
	public BlockPos getErrorPos() {
		return pos;
	}
	
	public ITextComponent getChatMessage() {
		return new TextComponentTranslation(_resourceKey, _parameters);
	}
	
	public MultiblockValidationError updatedError(World world) {
		if (pos == null) {
			return this;
		}
		if (_resourceKey.equals(Global.MOD_ID + ".multiblock_validation.invalid_block")) {
			return new MultiblockValidationError(_resourceKey, pos, pos.getX(), pos.getY(), pos.getZ(), world.getBlockState(pos).getBlock().getLocalizedName());
		}
		return this;
	}
	
	protected final String _resourceKey;
	protected final Object[] _parameters;
	protected final BlockPos pos;
}
