package nc.block;

import nc.block.item.IMetaBlockName;
import nc.handler.EnumHandler.OreTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class BlockOre extends BlockMeta implements IMetaBlockName {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", OreTypes.class);
	
	public BlockOre(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.ROCK);
		setHarvestLevel("pickaxe", 1);
		setHardness(3);
		setResistance(15);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, OreTypes.values()[0]));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	public int getMetaFromState(IBlockState state) {
		OreTypes type = (OreTypes) state.getValue(TYPE);
		return type.getID();
	}

	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, OreTypes.values()[meta]);
	}
	
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < OreTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	public String getSpecialName(ItemStack stack) {
		return OreTypes.values()[stack.getItemDamage()].getName();
	}
}
