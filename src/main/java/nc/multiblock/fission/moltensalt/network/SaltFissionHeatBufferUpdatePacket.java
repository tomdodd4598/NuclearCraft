package nc.multiblock.fission.moltensalt.network;

import nc.multiblock.fission.moltensalt.tile.SaltFissionController;
import nc.multiblock.fission.moltensalt.tile.TileSaltFissionController;
import nc.network.HeatBufferUpdatePacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SaltFissionHeatBufferUpdatePacket extends HeatBufferUpdatePacket<SaltFissionHeatBufferUpdatePacket, IMessage> {
	
	public SaltFissionHeatBufferUpdatePacket() {
		
	}
	
	public SaltFissionHeatBufferUpdatePacket(TileEntity tile, long heat, long capacity) {
		super(tile, heat, capacity);
	}
	
	@Override
	protected IMessage executeOnClient() {
		if(tile instanceof TileSaltFissionController) {
			((SaltFissionController) ((TileSaltFissionController) tile).getMultiblockController()).onPacket(capacity, heat);
		}
		return null;
	}
}
