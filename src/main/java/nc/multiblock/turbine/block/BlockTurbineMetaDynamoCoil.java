package nc.multiblock.turbine.block;

import nc.block.IBlockMeta;
import nc.multiblock.turbine.TurbineDynamoCoilType;
import nc.multiblock.turbine.tile.TileTurbineDynamoCoil;
import nc.util.StackHelper;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.*;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.world.*;

public class BlockTurbineMetaDynamoCoil extends BlockTurbinePart implements IBlockMeta {
	
	private final static PropertyEnum<TurbineDynamoCoilType> TYPE = PropertyEnum.create("type", TurbineDynamoCoilType.class);
	
	public BlockTurbineMetaDynamoCoil() {
		super();
		setDefaultState(blockState.getBaseState().withProperty(TYPE, TurbineDynamoCoilType.MAGNESIUM));
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, TYPE);
	}
	
	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getStateFromMeta(meta);
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int metadata) {
		switch (metadata) {
			case 0:
				return new TileTurbineDynamoCoil.Magnesium();
			case 1:
				return new TileTurbineDynamoCoil.Beryllium();
			case 2:
				return new TileTurbineDynamoCoil.Aluminum();
			case 3:
				return new TileTurbineDynamoCoil.Gold();
			case 4:
				return new TileTurbineDynamoCoil.Copper();
			case 5:
				return new TileTurbineDynamoCoil.Silver();
			default:
				return new TileTurbineDynamoCoil.Magnesium();
		}
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) {
			return false;
		}
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) {
			return false;
		}
		return rightClickOnPart(world, pos, player, hand, facing);
	}
	
	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(TYPE).getID();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(TYPE, TurbineDynamoCoilType.values()[meta]);
	}
	
	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> list) {
		for (int i = 0; i < TurbineDynamoCoilType.values().length; ++i) {
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
	public String getMetaName(ItemStack stack) {
		return TurbineDynamoCoilType.values()[StackHelper.getMetadata(stack)].getName();
	}
}
