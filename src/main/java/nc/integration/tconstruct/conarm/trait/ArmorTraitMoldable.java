package nc.integration.tconstruct.conarm.trait;

import c4.conarm.lib.traits.AbstractArmorTraitLeveled;
import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.utils.*;

public class ArmorTraitMoldable extends AbstractArmorTraitLeveled implements IArmorTraitNC {
	
	public ArmorTraitMoldable(int levels) {
		super("moldable", String.valueOf(levels), 0x62230E, 3, 1);
	}
	
	@Override
	public void applyModifierEffect(NBTTagCompound rootCompound) {
		NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
		int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + levels;
		toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
		TagUtil.setToolTag(rootCompound, toolTag);
	}
}
