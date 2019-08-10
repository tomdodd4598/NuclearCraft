package nc.block.tile.processor;

import static nc.block.property.BlockProperties.FACING_HORIZONTAL;

import java.util.Random;

import nc.block.tile.BlockSidedTile;
import nc.block.tile.IActivatable;
import nc.block.tile.ITileType;
import nc.enumm.BlockEnums.ProcessorType;
import nc.util.BlockHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockProcessor extends BlockSidedTile implements IActivatable, ITileType {
	
	public final boolean isActive, isActivatable;
	protected final ProcessorType type;
	
	public BlockProcessor(ProcessorType type) {
		super(Material.IRON);
		isActive = isActivatable = false;
		if (!isActive && type.getCreativeTab() != null) setCreativeTab(type.getCreativeTab());
		this.type = type;
	}
	
	public BlockProcessor(ProcessorType type, boolean isActive) {
		super(Material.IRON);
		this.isActive = isActive;
		isActivatable = true;
		if (!isActive && type.getCreativeTab() != null) setCreativeTab(type.getCreativeTab());
		this.type = type;
	}
	
	protected String getActiveSuffix(boolean isActive) {
		return isActive ? "_active" : "_idle";
	}
	
	@Override
	public String getTileName() {
		return isActivatable ? type.getName() + getActiveSuffix(isActive) : type.getName();
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
	public Block getBlockType(boolean active) {
		return active ? type.getActiveBlock() : type.getIdleBlock();
	}
	
	@Override
	public void setState(boolean isActive, TileEntity tile) {
		World world = tile.getWorld();
		BlockPos pos = tile.getPos();
		IBlockState state = world.getBlockState(pos);
		keepInventory = true;
		
		if (isActive) {
			world.setBlockState(pos, type.getActiveBlock().getDefaultState().withProperty(FACING_HORIZONTAL, state.getValue(FACING_HORIZONTAL)), 3);
		} else {
			world.setBlockState(pos, type.getIdleBlock().getDefaultState().withProperty(FACING_HORIZONTAL, state.getValue(FACING_HORIZONTAL)), 3);
		}
		keepInventory = false;
		
		tile.validate();
		world.setTileEntity(pos, tile);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if (!isActive) return;
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING_HORIZONTAL), type.getParticle1());
		BlockHelper.spawnParticleOnProcessor(state, world, pos, rand, state.getValue(FACING_HORIZONTAL), type.getParticle2());
		BlockHelper.playSoundOnProcessor(world, pos, rand, type.getSound());
	}
}
