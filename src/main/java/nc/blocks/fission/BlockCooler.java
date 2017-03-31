package nc.blocks.fission;

import nc.blocks.BlockMeta;
import nc.blocks.items.IMetaBlockName;
import nc.handlers.EnumHandler.CoolerTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCooler extends BlockMeta implements IMetaBlockName {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", CoolerTypes.class);

	public BlockCooler(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, CoolerTypes.values()[0]));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	public int getMetaFromState(IBlockState state) {
		CoolerTypes type = (CoolerTypes) state.getValue(TYPE);
		return type.getID();
	}
	
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, CoolerTypes.values()[meta]);
	}
	
	public void getSubBlocks(Item itemIn, CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < CoolerTypes.values().length; i++) {
			list.add(new ItemStack(itemIn, 1, i));
		}
	}
	
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != this) {
			return blockState.getLightValue(world, pos);
		}
		return ((CoolerTypes) state.getValue(TYPE)).getLightLevel();
	}
	
	public String getSpecialName(ItemStack stack) {
		return CoolerTypes.values()[stack.getItemDamage()].getName();
	}
}
