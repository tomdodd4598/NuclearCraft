package nc.multiblock.validation;

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

public class ValidationError {

    public static final ValidationError VALIDATION_ERROR_TOO_FEW_PARTS = new ValidationError("zerocore:api.multiblock.validation.too_few_parts");

    public ValidationError(String messageFormatStringResourceKey, Object... messageParameters) {

        this._resourceKey = messageFormatStringResourceKey;
        this._parameters = messageParameters;
    }

    public ITextComponent getChatMessage() {

        return new TextComponentTranslation(this._resourceKey, _parameters);
    }

    protected final String _resourceKey;
    protected final Object[] _parameters;
}
