package nc.block;

import nc.enumm.*;
import nc.enumm.MetaEnums.MachineSieveAssemblyType;
import nc.tab.NCTabs;
import nc.tile.ITile;
import nc.util.*;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving.SpawnPlacementType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.*;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.fml.relauncher.*;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BlockMeta<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends Block implements IBlockMeta<T> {
	
	public final Class<T> enumm;
	public final T[] values;
	public final PropertyEnum<T> type;
	
	protected boolean canSustainPlant = true;
	protected boolean canCreatureSpawn = true;
	
	protected static boolean keepInventory;
	
	public BlockMeta(Class<T> enumm, PropertyEnum<T> property, Material material) {
		super(material);
		this.enumm = enumm;
		values = enumm.getEnumConstants();
		type = property;
		setDefaultState(blockState.getBaseState().withProperty(type, values[0]));
		setMetaHarvestLevels();
		setHardness(3F);
		setResistance(15F);
	}
	
	public static class BlockOre extends BlockMeta<MetaEnums.OreType> {
		
		public final static PropertyEnum<MetaEnums.OreType> TYPE = PropertyEnum.create("type", MetaEnums.OreType.class);
		
		public BlockOre() {
			super(MetaEnums.OreType.class, TYPE, Material.ROCK);
			setCreativeTab(NCTabs.material);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
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
			setCreativeTab(NCTabs.material);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
	}
	
	public static class BlockIngot2 extends BlockMeta<MetaEnums.IngotType2> {
		
		public final static PropertyEnum<MetaEnums.IngotType2> TYPE = PropertyEnum.create("type", MetaEnums.IngotType2.class);
		
		public BlockIngot2() {
			super(MetaEnums.IngotType2.class, TYPE, Material.IRON);
			setCreativeTab(NCTabs.material);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
	}
	
	public static class BlockMaterial extends BlockMeta<MetaEnums.BlockMaterial> {
		
		public final static PropertyEnum<MetaEnums.BlockMaterial> TYPE = PropertyEnum.create("type", MetaEnums.BlockMaterial.class);
		
		public BlockMaterial() {
			super(MetaEnums.BlockMaterial.class, TYPE, Material.IRON);
			setCreativeTab(NCTabs.material);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
	}
	
	public static class BlockFertileIsotope extends BlockMeta<MetaEnums.FertileIsotopeType> {
		
		public final static PropertyEnum<MetaEnums.FertileIsotopeType> TYPE = PropertyEnum.create("type", MetaEnums.FertileIsotopeType.class);
		
		public BlockFertileIsotope() {
			super(MetaEnums.FertileIsotopeType.class, TYPE, Material.ROCK);
			setCreativeTab(NCTabs.material);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
	}
	
	public static class BlockMachineDiaphragm extends Transparent<MetaEnums.MachineDiaphragmType> {
		
		public final static PropertyEnum<MetaEnums.MachineDiaphragmType> TYPE = PropertyEnum.create("type", MetaEnums.MachineDiaphragmType.class);
		
		public BlockMachineDiaphragm() {
			super(MetaEnums.MachineDiaphragmType.class, TYPE, Material.IRON, true);
			setCreativeTab(NCTabs.multiblock);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.CUTOUT;
		}
	}
	
	public static class BlockMachineSieveAssembly extends Transparent<MachineSieveAssemblyType> {
		
		public final static PropertyEnum<MachineSieveAssemblyType> TYPE = PropertyEnum.create("type", MachineSieveAssemblyType.class);
		
		public BlockMachineSieveAssembly() {
			super(MachineSieveAssemblyType.class, TYPE, Material.IRON, true);
			setCreativeTab(NCTabs.multiblock);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.CUTOUT;
		}
	}
	
	public static class BlockFissionReflector extends BlockMeta<MetaEnums.NeutronReflectorType> {
		
		public final static PropertyEnum<MetaEnums.NeutronReflectorType> TYPE = PropertyEnum.create("type", MetaEnums.NeutronReflectorType.class);
		
		public BlockFissionReflector() {
			super(MetaEnums.NeutronReflectorType.class, TYPE, Material.IRON);
			setCreativeTab(NCTabs.multiblock);
		}
		
		@Override
		protected BlockStateContainer createBlockState() {
			return new BlockStateContainer(this, TYPE);
		}
	}
	
	@Override
	public Class<T> getEnumClass() {
		return enumm;
	}
	
	@Override
	public T[] getValues() {
		return values;
	}
	
	@Override
	public String getMetaName(ItemStack stack) {
		return values[StackHelper.getMetadata(stack)].getName();
	}
	
	public void setMetaHarvestLevels() {
		for (T next : values) {
			setHarvestLevel(next.getHarvestTool(), next.getHarvestLevel(), getStateFromMeta(next.getID()));
		}
	}
	
	@Override
	public int getLightValue(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.getValue(type).getLightValue();
	}
	
	@Override
	public SoundType getSoundType(IBlockState state, World world, BlockPos pos, @Nullable Entity entity) {
		return state.getValue(type).getSoundType();
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
		for (int i = 0; i < values.length; ++i) {
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
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return canSustainPlant && super.canSustainPlant(state, world, pos, direction, plantable);
	}
	
	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, SpawnPlacementType placementType) {
		return canCreatureSpawn && super.canCreatureSpawn(state, world, pos, placementType);
	}
	
	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
		return false;
	}
	
	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block blockIn, BlockPos fromPos) {
		if (this instanceof ITileEntityProvider) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof ITile t) {
				t.onBlockNeighborChanged(state, world, pos, fromPos);
			}
		}
	}
	
	// Inventory
	
	public void dropItems(World world, BlockPos pos, IInventory inventory) {
		InventoryHelper.dropInventoryItems(world, pos, inventory);
	}
	
	public void dropItems(World world, BlockPos pos, List<ItemStack> stacks) {
		NCInventoryHelper.dropInventoryItems(world, pos, stacks);
	}
	
	// Transparent Block
	
	public static class Transparent<T extends Enum<T> & IStringSerializable & IBlockMetaEnum> extends BlockMeta<T> {
		
		protected final boolean smartRender;
		
		public Transparent(Class<T> enumm, PropertyEnum<T> property, Material material, boolean smartRender) {
			super(enumm, property, material);
			setHardness(1.5F);
			setResistance(10F);
			this.smartRender = smartRender;
			canSustainPlant = false;
			canCreatureSpawn = false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public BlockRenderLayer getRenderLayer() {
			return BlockRenderLayer.CUTOUT;
		}
		
		@Override
		public boolean isFullCube(IBlockState state) {
			return false;
		}
		
		@Override
		public boolean isOpaqueCube(IBlockState state) {
			return false;
		}
		
		@Override
		@SideOnly(Side.CLIENT)
		public boolean shouldSideBeRendered(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
			if (!smartRender) {
				return true;
			}
			
			IBlockState otherState = world.getBlockState(pos.offset(side));
			Block block = otherState.getBlock();
			
			if (state != otherState) {
				return true;
			}
			
			return block != this && super.shouldSideBeRendered(state, world, pos, side);
		}
	}
}
