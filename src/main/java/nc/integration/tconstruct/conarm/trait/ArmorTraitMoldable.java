package nc.integration.tconstruct.conarm.trait;

import net.minecraft.nbt.NBTTagCompound;
import slimeknights.tconstruct.library.traits.AbstractTraitLeveled;
import slimeknights.tconstruct.library.utils.*;

public class ArmorTraitMoldable extends AbstractTraitLeveled {
	
	public ArmorTraitMoldable(int levels) {
		super("moldable_armor", String.valueOf(levels), 0x62230E, 3, 1);
	}
	
	@Override
	public void applyModifierEffect(NBTTagCompound rootCompound) {
		NBTTagCompound toolTag = TagUtil.getToolTag(rootCompound);
		int modifiers = toolTag.getInteger(Tags.FREE_MODIFIERS) + levels;
		toolTag.setInteger(Tags.FREE_MODIFIERS, Math.max(0, modifiers));
		TagUtil.setToolTag(rootCompound, toolTag);
	}
}
