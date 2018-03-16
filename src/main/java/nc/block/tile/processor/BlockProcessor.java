package nc.block.tile.processor;

import java.util.Random;

import nc.block.tile.BlockSidedInventoryGui;
import nc.block.tile.IActivatable;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ProcessorType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockProcessor extends BlockSidedInventoryGui implements IActivatable {
	
	protected final boolean isActive;
	protected final ProcessorType type;
	
	public BlockProcessor(ProcessorType type, boolean isActive) {
		super(type.getName() + (isActive ? "_active" : "_idle"), Material.IRON, type.getID());
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
		if ((type.getParticle1() == "" && type.getParticle2() == "") || !NCConfig.processor_particles) return;
		if (isActive) {
			EnumFacing enumfacing = (EnumFacing) state.getValue(FACING);
			double d0 = (double)pos.getX() + 0.5D;
			double d1 = (double)pos.getY() + 0.125D + rand.nextDouble() * 0.75D;
			double d2 = (double)pos.getZ() + 0.5D;
			double d3 = 0.52D;
			double d4 = rand.nextDouble() * 0.6D - 0.3D;
			
			if (type.getSound() != null) if (rand.nextDouble() < 0.2D) {
				world.playSound((double)pos.getX() + 0.5D, (double)pos.getY(), (double)pos.getZ() + 0.5D, type.getSound(), SoundCategory.BLOCKS, 1.0F, 1.0F, false);
			}
		
			switch (enumfacing) {
				case WEST:
					if (type.getParticle1() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle1()), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					if (type.getParticle2() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle2()), d0 - d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case EAST:
					if (type.getParticle1() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle1()), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					if (type.getParticle2() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle2()), d0 + d3, d1, d2 + d4, 0.0D, 0.0D, 0.0D);
					break;
				case NORTH:
					if (type.getParticle1() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle1()), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					if (type.getParticle2() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle2()), d0 + d4, d1, d2 - d3, 0.0D, 0.0D, 0.0D);
					break;
				case SOUTH:
					if (type.getParticle1() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle1()), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
					if (type.getParticle2() != "") world.spawnParticle(EnumParticleTypes.getByName(type.getParticle2()), d0 + d4, d1, d2 + d3, 0.0D, 0.0D, 0.0D);
				default:
					break;
			}
		}
	}
}
