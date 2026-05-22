package nc.enumm;

import net.minecraft.block.SoundType;

public interface IBlockMetaEnum extends IMetaEnum {
	
	default int getHarvestLevel() {
		return getHarvestTool().equals("pickaxe") ? 0 : -1;
	}
	
	default String getHarvestTool() {
		return "pickaxe";
	}
	
	default float getHardness() {
		return 3F;
	}
	
	default float getResistance() {
		return 5F * getHardness();
	}
	
	default int getLightValue() {
		return 0;
	}
	
	default SoundType getSoundType() {
		return SoundType.STONE;
	}
	
	default int getFireSpreadSpeed() {
		return 0;
	}
	
	default int getFlammability() {
		return 0;
	}
	
	default boolean isFireSource() {
		return false;
	}
}
