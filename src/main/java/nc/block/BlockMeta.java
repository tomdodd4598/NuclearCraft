package nc.block;

import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import nc.enumm.IBlockMetaEnum;
import nc.enumm.MetaEnums;
import nc.tab.NCTabs;
import nc.tile.ITile;
import nc.util.CollectionHelper;
import nc.util.ItemStackHelper;
import nc.util.NCInventoryHelper;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public abstract class BlockMeta<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends Block implements IBlockMeta {
	
	public final T[] values;
	public final PropertyEnum<T> type;
	
	protected boolean canCreatureSpawn = true;
	protected static boolean keepInventory;
	
	public BlockMeta(Class<T> enumm, PropertyEnum<T> property, Material material) {
		super(material);
		values = enumm.getEnumConstants();
		type = property;
		setDefaultState(blockState.getBaseState().withProperty(type, values[0]));
		setMetaHarvestLevels();
		setHardness(2F);
		setResistance(15F);
	}
	
	public static class BlockOre extends BlockMeta<MetaEnums.OreType> {
		
		public final static PropertyEnum<MetaEnums.OreType> TYPE = PropertyEnum.create("type", MetaEnums.OreType.class);
		
		public BlockOre() {
			super(MetaEnums.OreType.class, TYPE, Material.ROCK);
			setCreativeTab(NCTabs.MATERIAL);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.CUTOUT;
		}
	}
	
	public static class BlockIngot extends BlockMeta<MetaEnums.IngotType> {
		
		public final static PropertyEnum<MetaEnums.IngotType> TYPE = PropertyEnum.create("type", MetaEnums.IngotType.class);
		
		public BlockIngot() {
			super(MetaEnums.IngotType.class, TYPE, Material.IRON);
			setCreativeTab(NCTabs.MATERIAL);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
		
		@Override
		public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
			return world.getBlockState(pos).getValue(type).getFireSpreadSpeed();
		}
		
		@Override
		public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
			return world.getBlockState(pos).getValue(type).getFlammability();
		}
		
		@Override
		public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
			return world.getBlockState(pos).getValue(type).isFireSource();
		}
	}
	
	public static class BlockFertileIsotope extends BlockMeta<MetaEnums.FertileIsotopeType> {
		
		public final static PropertyEnum<MetaEnums.FertileIsotopeType> TYPE = PropertyEnum.create("type", MetaEnums.FertileIsotopeType.class);
		
		public BlockFertileIsotope() {
			super(MetaEnums.FertileIsotopeType.class, TYPE, Material.ROCK);
			setCreativeTab(NCTabs.MATERIAL);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	public static class BlockFissionReflector extends BlockMeta<MetaEnums.NeutronReflectorType> {
		
		public final static PropertyEnum<MetaEnums.NeutronReflectorType> TYPE = PropertyEnum.create("type", MetaEnums.NeutronReflectorType.class);
		
		public BlockFissionReflector() {
			super(MetaEnums.NeutronReflectorType.class, TYPE, Material.IRON);
			setCreativeTab(NCTabs.MULTIBLOCK);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, new IProperty[] {TYPE});
		}
	}
	
	@Override
	public String getMetaName(ItemStack stack) {
		return values[ItemStackHelper.getMetadata(stack)].getName();
	}
	
	public void setMetaHarvestLevels() {
		Iterator<T> itr = CollectionHelper.asList(values).iterator();
		while (itr.hasNext()) {
			T nextState = itr.next();
			setHarvestLevel(nextState.getHarvestTool(), nextState.getHarvestLevel(), getStateFromMeta(nextState.getID()));
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(type).getLightValue();
	}
	
	@Override
	public float getBlockHardness(IBlockState state, World world, BlockPos pos) {
		return state.getValue(type).getHardness();
	}
	
	@Override
	public float getExplosionResistance(World world, BlockPos pos, @Nullable Entity exploder, Explosion explosion) {
		return world.getBlockState(pos).getValue(type).getResistance();
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(type).getID();
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
	public int damageDropped(IBlockState state) {
		return getMetaFromState(state);
	}
	
	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Item.getItemFromBlock(this), 1, getMetaFromState(state));
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, net.minecraft.entity.EntityLiving.SpawnPlacementType type) {
		return canCreatureSpawn && super.canCreatureSpawn(state, world, pos, type);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (this instanceof ITileEntityProvider) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITile) ((ITile)tile).onBlockNeighborChanged(state, world, pos, fromPos);
		}
	}
	
	// Inventory
	
	public void dropItems(World world, BlockPos pos, IInventory inventory) {
		InventoryHelper.dropInventoryItems(world, pos, inventory);
	}
	
	public void dropItems(World world, BlockPos pos, List<ItemStack> stacks) {
		NCInventoryHelper.dropInventoryItems(world, pos, stacks);
	}
}
