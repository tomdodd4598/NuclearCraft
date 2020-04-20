package nc.multiblock.qComputer.block;

import nc.enumm.IBlockMetaEnum;
import nc.multiblock.block.BlockMultiblockMetaPart;
import nc.tab.NCTabs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.util.IStringSerializable;

public abstract class BlockQuantumComputerMetaPart<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockMultiblockMetaPart<T> {
	
	public BlockQuantumComputerMetaPart(Class<T> enumm, PropertyEnum<T> property) {
		super(enumm, property, Material.IRON, NCTabs.MISC);
	}
}
