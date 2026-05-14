package nc.multiblock.internal;

import com.google.common.collect.Lists;
import nc.Global;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.world.World;

import java.util.*;

public class MultiblockValidationError {
	
	public static final MultiblockValidationError VALIDATION_ERROR_TOO_FEW_PARTS = new MultiblockValidationError("zerocore.api.nc.multiblock.validation.too_few_parts", Collections.emptyList());
	
	public MultiblockValidationError(String messageFormatStringResourceKey, Collection<BlockPos> posCollection, Object... messageParameters) {
		_resourceKey = messageFormatStringResourceKey;
		_parameters = messageParameters;
		this._posCollection = posCollection == null ? Collections.emptyList() : posCollection;
	}
	
	public MultiblockValidationError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
		this(messageFormatStringResourceKey, pos == null ? null : Lists.newArrayList(pos), messageParameters);
	}
	
	/**
	 * @return the positions of the last validation error encountered when trying to assemble the multiblock (empty if there are no positions).
	 */
	public Collection<BlockPos> getPosCollection() {
		return _posCollection;
	}
	
	public ITextComponent getChatMessage() {
		return new TextComponentTranslation(_resourceKey, _parameters);
	}
	
	public MultiblockValidationError updatedError(World world) {
		if (!_posCollection.isEmpty()) {
			if (_resourceKey.equals(Global.MOD_ID + ".multiblock_validation.invalid_block")) {
				BlockPos pos = null;
				for (BlockPos p : _posCollection) {
					pos = p;
					break;
				}
				if (pos != null) {
					IBlockState state = world.getBlockState(pos);
					if (state != null) {
						Block block = state.getBlock();
						if (block != null) {
							return new MultiblockValidationError(_resourceKey, pos, pos.getX(), pos.getY(), pos.getZ(), block.getLocalizedName());
						}
					}
				}
			}
		}
		return this;
	}
	
	protected final String _resourceKey;
	protected final Object[] _parameters;
	protected final Collection<BlockPos> _posCollection;
}
