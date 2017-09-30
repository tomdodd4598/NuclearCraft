package nc.block;

import nc.block.item.IMetaBlockName;
import nc.handler.EnumHandler.IngotTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockIngot extends BlockMeta implements IMetaBlockName {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", IngotTypes.class);
	
	public BlockIngot(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(4);
		setResistance(30);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, IngotTypes.values()[0]));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	public int getMetaFromState(IBlockState state) {
		IngotTypes type = (IngotTypes) state.getValue(TYPE);
		return type.getID();
	}
	
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, IngotTypes.values()[meta]);
	}
	
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < IngotTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	public String getSpecialName(ItemStack stack) {
		return IngotTypes.values()[stack.getItemDamage()].getName();
	}
}
