package nc.multiblock.validation;

import nc.Global;
import net.minecraft.util.math.BlockPos;

/*
 * A multiblock library for making irregularly-shaped multiblock machines
 *
 * Original author: Erogenous Beef
 * https://github.com/erogenousbeef/BeefCore
 *
 * Ported to Minecraft 1.9 by ZeroNoRyouki
 * https://github.com/ZeroNoRyouki/ZeroCore
 */

import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;

public class ValidationError {

    public static final ValidationError VALIDATION_ERROR_TOO_FEW_PARTS = new ValidationError("zerocore.api.nc.multiblock.validation.too_few_parts", null);

    public ValidationError(String messageFormatStringResourceKey, BlockPos pos, Object... messageParameters) {
        this._resourceKey = messageFormatStringResourceKey;
        this._parameters = messageParameters;
        this.pos = pos;
    }
    
    /**
     * @return the position of the last validation error encountered when trying to assemble the multiblock, or null if there is no position.
     */
    public BlockPos getErrorPos() {
    	return pos;
    }

    public ITextComponent getChatMessage() {
        return new TextComponentTranslation(_resourceKey, _parameters);
    }
    
    public ValidationError updatedError(World world) {
    	if (pos == null) return this;
    	if (_resourceKey.equals(Global.MOD_ID + ".multiblock_validation.invalid_block")) {
    		return new ValidationError(_resourceKey, pos, pos.getX(), pos.getY(), pos.getZ(), world.getBlockState(pos).getBlock().getLocalizedName());
    	}
    	return this;
    }

    protected final String _resourceKey;
    protected final Object[] _parameters;
    protected final BlockPos pos;
}
