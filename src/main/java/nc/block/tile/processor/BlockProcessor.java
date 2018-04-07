package nc.block.tile.processor;

import java.util.Random;

import nc.block.tile.BlockSidedInventory;
import nc.block.tile.IActivatable;
import nc.enumm.BlockEnums.ProcessorType;
import nc.util.BlockHelper;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockProcessor extends BlockSidedInventory implements IActivatable {
	
	protected final boolean isActive;
	protected final ProcessorType type;
	
	public BlockProcessor(ProcessorType type, boolean isActive) {
		super(type.getName() + (isActive ? "_active" : "_idle"), Material.IRON);
		this.isActive = isActive;
		if (!isActive) setCreativeTab(type.getCreativeTab());
		this.type = type;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return type.getTile();
	}
	
	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(type.getIdleBlock());
	}
	
	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		return new ItemStack(type.getIdleBlock());
	}

	@Override
	public void setState(boolean active, World world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		TileEntity tile = world.getTileEntity(pos);
		keepInventory = true;
		
		if (active) {
			world.setBlockState(pos, type.getActiveBlock().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		} else {
			world.setBlockState(pos, type.getIdleBlock().getDefaultState().withProperty(FACING, state.getValue(FACING)), 3);
		}
		keepInventory = false;
		
		if (tile != null) {
			tile.validate();
			world.setTileEntity(pos, tile);
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!isActive) return;
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING), type.getParticle1());
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING), type.getParticle2());
		BlockHelper.playSoundOnProcessor(world, pos, rand, type.getSound());
	}
}
