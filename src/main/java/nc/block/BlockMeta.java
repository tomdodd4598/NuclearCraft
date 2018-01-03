package nc.block;

import java.util.Random;

import nc.Global;
import nc.block.item.IMetaBlockName;
import nc.enumm.IBlockMeta;
import nc.enumm.MetaEnums;
import nc.proxy.CommonProxy;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public abstract class BlockMeta<T extends Enum<T> & IStringSerializable & IBlockMeta> extends Block implements IMetaBlockName {
	
	public final T[] values;
	public final PropertyEnum type;
	
	public BlockMeta(String name, Class<T> enumm, PropertyEnum property, Material material) {
		super(material);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(Global.MOD_ID, name));
		values = enumm.getEnumConstants();
		type = property;
		setDefaultState(blockState.getBaseState().withProperty(type, values[0]));
		setHarvestLevel("pickaxe", 0);
		setHardness(2);
		setResistance(15);
	}
	
	public static class BlockOre extends BlockMeta implements IMetaBlockName {
		
		public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.OreType.class);
		
		public BlockOre(String name) {
			super(name, MetaEnums.OreType.class, TYPE, Material.ROCK);
			setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	public static class BlockIngot extends BlockMeta implements IMetaBlockName {
		
		public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.IngotType.class);
		
		public BlockIngot(String name) {
			super(name, MetaEnums.IngotType.class, TYPE, Material.IRON);
			setCreativeTab(CommonProxy.TAB_BASE_BLOCK_MATERIALS);
		}

		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	public static class BlockFission extends BlockMeta implements IMetaBlockName {
		
		public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.FissionBlockType.class);
		
		public BlockFission(String name) {
			super(name, MetaEnums.FissionBlockType.class, TYPE, Material.IRON);
			setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	public static class BlockCooler extends BlockMeta implements IMetaBlockName {
		
		public final static PropertyEnum TYPE = PropertyEnum.create("type", MetaEnums.CoolerType.class);

		public BlockCooler(String name) {
			super(name, MetaEnums.CoolerType.class, TYPE, Material.IRON);
			setCreativeTab(CommonProxy.TAB_FISSION_BLOCKS);
		}

		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(this);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(world.getBlockState(pos)));
	}
	
	@Override
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((T) state.getValue(type)).getID();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(type, values[meta]);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < values.length; i++) {
			list.add(new ItemStack(this, 1, i));
		}
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return false;
	}
	
	@Override
	public int getHarvestLevel(IBlockState state) {
		if (state.getBlock() != this) return state.getBlock().getHarvestLevel(state);
		else return ((T) state.getValue(type)).getHarvestLevel();
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != this) return blockState.getBlockHardness(world, pos);
		else return ((T) blockState.getValue(type)).getHardness();
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, Entity exploder, Explosion explosion) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != this) return blockState.getBlock().getExplosionResistance(world, pos, exploder, explosion);
		else return ((T) blockState.getValue(type)).getResistance();
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		IBlockState blockState = world.getBlockState(pos);
		if (blockState.getBlock() != this) return blockState.getLightValue(world, pos);
		else return ((T) state.getValue(type)).getLightValue();
	}
	
	@Override
	public String getSpecialName(ItemStack stack) {
		return values[stack.getItemDamage()].getName();
	}
}
