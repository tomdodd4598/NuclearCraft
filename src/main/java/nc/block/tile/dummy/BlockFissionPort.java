package nc.block.tile.dummy;

import nc.NuclearCraft;
import nc.enumm.BlockEnums.SimpleTileType;
import nc.tile.dummy.TileFissionPort;
import nc.tile.generator.TileFissionController;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.internal.FMLNetworkHandler;

public class BlockFissionPort extends BlockSimpleSidedDummy {
	
	public BlockFissionPort(SimpleTileType type) {
		super(type);
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (player == null) return false;
		if (hand != EnumHand.MAIN_HAND || player.isSneaking()) return false;
		if (world.isRemote) return true;
		
		TileEntity tile = world.getTileEntity(pos);
		if (tile instanceof TileFissionPort) {
			TileFissionPort port = (TileFissionPort) tile;
			if (port.masterPosition == null) port.findMaster();
			if (port.masterPosition == null) return false;
			BlockPos controllerPos = port.masterPosition;
			if (controllerPos != null) {
				TileEntity tileentity = world.getTileEntity(controllerPos);
				if (tileentity instanceof TileFissionController) {
					FMLNetworkHandler.openGui(player, NuclearCraft.instance, 100, world, controllerPos.getX(), controllerPos.getY(), controllerPos.getZ());
				}
			}
		}
		return true;
	}
}
