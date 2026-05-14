package nc.tile.processor.info;

import nc.network.tile.processor.ProcessorUpdatePacket;
import nc.tile.processor.*;
import nc.tile.processor.info.builder.ProcessorContainerInfoBuilderImpl.*;
import net.minecraft.tileentity.TileEntity;

public class ProcessorContainerInfoImpl {
	
	public static class BasicProcessorContainerInfo<TILE extends TileEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ProcessorContainerInfo<TILE, PACKET, BasicProcessorContainerInfo<TILE, PACKET>> {
		
		public BasicProcessorContainerInfo(BasicProcessorContainerInfoBuilder<TILE, PACKET> builder) {
			super(builder);
		}
	}
	
	public static class BasicUpgradableProcessorContainerInfo<TILE extends TileEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends UpgradableProcessorContainerInfo<TILE, PACKET, BasicUpgradableProcessorContainerInfo<TILE, PACKET>> {
		
		public BasicUpgradableProcessorContainerInfo(BasicUpgradableProcessorContainerInfoBuilder<TILE, PACKET> builder) {
			super(builder);
		}
	}
}
