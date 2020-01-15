package nc.block.tile.dummy;

import nc.NuclearCraft;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.ITileGui;
import nc.tile.dummy.TileMachineInterface;
import nc.tile.fluid.ITileFluid;
import nc.tile.processor.IProcessor;
import nc.util.FluidHelper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockMachineInterface extends BlockSimpleDummy {
	
	public BlockMachineInterface(SimpleTileType type) {
		super(type);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null || hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		if (world.isRemote) return true;
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileMachineInterface) {
			TileMachineInterface iface = (TileMachineInterface) tile;
			if (iface.masterPosition == null) iface.findMaster();
			if (iface.masterPosition == null) return false;
			BlockPos machinePos = iface.masterPosition;
			if (machinePos != null) {
				TileEntity tileentity = world.getTileEntity(machinePos);
				if (tileentity instanceof ITileFluid) {
					ITileFluid tileFluid = (ITileFluid) tileentity;
					boolean accessedTanks = FluidHelper.accessTanks(player, hand, facing, tileFluid);
					if (accessedTanks) {
						if (tileentity instanceof IProcessor) {
							((IProcessor) tileentity).refreshRecipe();
							((IProcessor) tileentity).refreshActivity();
						}
						return true;
					}
				}
				if (tileentity instanceof ITileGui) {
					ITileGui guiTile = (ITileGui) tileentity;
					FMLNetworkHandler.openGui(player, NuclearCraft.instance, guiTile.getGuiID(), world, machinePos.getX(), machinePos.getY(), machinePos.getZ());
				}
			}
		}
		return true;
	}
}
