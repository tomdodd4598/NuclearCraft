package nc.network;

import java.io.IOException;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public abstract class EnergyStorageUpdatePacket<REQ extends SimplePacketBase<REQ, REPLY> , REPLY extends IMessage> extends SimplePacketBase<REQ, REPLY> {
	
	protected long capacity;
	protected long energy;
	protected TileEntity tile;
	
	public EnergyStorageUpdatePacket() {
		
	}
	
	public EnergyStorageUpdatePacket(TileEntity tile, long energy, long capacity) {
		this.capacity = capacity;
		this.energy = energy;
		this.tile = tile;
	}
	
	@Override
	public void write() throws IOException {
		writeTileLocation(tile);
		writeLong(capacity);
		writeLong(energy);
	}
	
	@Override
	public void read() throws IOException {
		tile = readTileEntityClient();
		capacity = readLong();
		energy = readLong();
	}
	
	@Override
	protected REPLY executeOnServer() {
		return null;
	}
}
