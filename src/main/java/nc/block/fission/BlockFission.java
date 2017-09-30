package nc.block.fission;

import nc.block.BlockMeta;
import nc.block.item.IMetaBlockName;
import nc.handler.EnumHandler.FissionBlockTypes;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;

public class BlockFission extends BlockMeta implements IMetaBlockName {
	
	public final static PropertyEnum TYPE = PropertyEnum.create("type", FissionBlockTypes.class);
	
	public BlockFission(String unlocalizedName, String registryName) {
		super(unlocalizedName, registryName, Material.IRON);
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
		setDefaultState(blockState.getBaseState().withProperty(TYPE, FissionBlockTypes.values()[0]));
	}

	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	public int getMetaFromState(IBlockState state) {
		FissionBlockTypes type = (FissionBlockTypes) state.getValue(TYPE);
		return type.getID();
	}
	
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(TYPE, FissionBlockTypes.values()[meta]);
	}
	
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < FissionBlockTypes.values().length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != this) {
			return blockState.getBlock().getExplosionResistance(world, pos, exploder, explosion);
		}
		return ((FissionBlockTypes) blockState.getValue(TYPE)).getResistance();
	}
	
	public String getSpecialName(ItemStack stack) {
		return FissionBlockTypes.values()[stack.getItemDamage()].getName();
	}
}
