package nc.block.tile.generator;

import java.util.Random;

import nc.block.tile.processor.BlockProcessor;
import nc.config.NCConfig;
import nc.enumm.BlockEnums.ProcessorType;
import nc.init.NCSounds;
import nc.tile.generator.TileFissionController;
import nc.util.BlockFinder;
import nc.util.NCInventoryHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class BlockFissionController extends BlockProcessor {
	
	public final boolean isNew;

	public BlockFissionController(boolean isActive, boolean isNew) {
		super(isNew ? ProcessorType.FISSION_CONTROLLER_NEW : ProcessorType.FISSION_CONTROLLER, isActive);
		this.isNew = isNew;
		canCreatureSpawn = false;
	}
	
	@Override
	public void onGuiOpened(World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			TileFissionController controller = (TileFissionController) tile;
			controller.refreshMultiblock(true);
		}
	}
	
	@Override
	public void dropItems(World world, BlockPos pos, IInventory tileentity) {
		NCInventoryHelper.dropInventoryItems(world, pos, tileentity, 0, 1);
	}
	
	@Override
	public boolean hasComparatorInputOverride(IBlockState state) {
		return true;
	}
	
	@Override
	public int getComparatorInputOverride(IBlockState state, World world, BlockPos pos) {
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			return ((TileFissionController)tile).getComparatorStrength();
		}
		return Container.calcRedstone(tile);
	}
	
	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		super.randomDisplayTick(state, world, pos, rand);
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionController) {
			TileFissionController controller = (TileFissionController) tile;
			BlockFinder finder = new BlockFinder(pos, world, controller.getBlockMetadata());
			BlockPos position = finder.randomWithin(controller.minX, controller.maxX, controller.minY, controller.maxY, controller.minZ, controller.maxZ);
			
			if (controller.cells <= 0) return;
			double soundRate = MathHelper.clamp(0.04D, Math.sqrt(controller.cells)/NCConfig.fission_max_size, 1D);
			if (controller.isProcessing) if (rand.nextDouble() < soundRate) {
				world.playSound(position.getX(), position.getY(), position.getZ(), NCSounds.geiger_tick, SoundCategory.BLOCKS, 1.6F, 1F + 0.12F*(rand.nextFloat() - 0.5F), false);
			}
		}
	}
}
