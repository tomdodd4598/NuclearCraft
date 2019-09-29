package nc.multiblock.fission.block;

import nc.enumm.IBlockMeta;
import nc.multiblock.MultiblockBlockMetaPartBase;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public abstract class BlockMetaFissionPartBase<T extends Enum<T> & IStringSerializable & IBlockMeta> extends MultiblockBlockMetaPartBase<T> {
	
	public BlockMetaFissionPartBase(Class<T> enumm, PropertyEnum<T> property) {
		super(enumm, property, Material.IRON, NCTabs.FISSION_BLOCKS);
	}
}
