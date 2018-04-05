package nc.block.tile.dummy;

import nc.enumm.BlockEnums.SimpleTileType;

public class BlockFissionPort extends BlockSimpleSidedDummy {
	
	public BlockFissionPort(SimpleTileType type) {
		super(type);
	}
	
	/*@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		} else if (player != null) {
			if (world.getTileEntity(pos) instanceof TileFissionPort) {
				TileEntity controller = ((TileFissionPort) world.getTileEntity(pos)).getMaster();
				if (controller != null) {
					FMLNetworkHandler.openGui(player, NuclearCraft.instance, ProcessorType.FISSION_CONTROLLER_NEW.getID(), world, controller.getPos().getX(), controller.getPos().getY(), controller.getPos().getZ());
				}
			}
		}
		return true;
	}*/
}
