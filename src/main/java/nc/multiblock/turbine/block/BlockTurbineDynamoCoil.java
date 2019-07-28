package nc.multiblock.turbine.block;

import nc.block.item.IMetaBlockName;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.util.ItemStackHelper;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockTurbineDynamoCoil extends BlockTurbinePartBase implements IMetaBlockName {
	
	private final static PropertyEnum TYPE = PropertyEnum.create("type", TurbineDynamoCoilType.class);

	public BlockTurbineDynamoCoil() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TYPE, TurbineDynamoCoilType.MAGNESIUM));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, new IProperty[] {TYPE});
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		return getStateFromMeta(meta);
	}

	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (metadata) {
		case 0: return new TileTurbineDynamoCoil.Magnesium();
		case 1: return new TileTurbineDynamoCoil.Beryllium();
		case 2: return new TileTurbineDynamoCoil.Aluminum();
		case 3: return new TileTurbineDynamoCoil.Gold();
		case 4: return new TileTurbineDynamoCoil.Copper();
		case 5: return new TileTurbineDynamoCoil.Silver();
		default: return new TileTurbineDynamoCoil.Magnesium();
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return ((TurbineDynamoCoilType) state.getValue(TYPE)).getID();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, TurbineDynamoCoilType.values()[meta]);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < TurbineDynamoCoilType.values().length; i++) {
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
	public String getSpecialName(ItemStack stack) {
		return TurbineDynamoCoilType.values()[ItemStackHelper.getMetadata(stack)].getName();
	}
}
