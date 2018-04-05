package nc.network;

import java.io.IOException;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class HeatBufferUpdatePacket<REQ extends SimplePacket<REQ, REPLY> , REPLY extends IMessage> extends SimplePacket<REQ, REPLY> {
	
	protected long capacity;
	protected long heat;
	protected TileEntity tile;
	
	public HeatBufferUpdatePacket() {
		
	}
	
	public HeatBufferUpdatePacket(TileEntity tile, long energy, long capacity) {
		this.capacity = capacity;
		this.heat = energy;
		this.tile = tile;
	}
	
	@Override
	public void write() throws IOException {
		writeTileLocation(tile);
		writeLong(capacity);
		writeLong(heat);
	}
	
	@Override
	public void read() throws IOException {
		tile = readTileEntityClient();
		capacity = readLong();
		heat = readLong();
	}
	
	@Override
	protected REPLY executeOnServer() {
		return null;
	}
}
