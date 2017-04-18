package nc.tile.quantum;

import nc.block.quantum.BlockSimpleQuantum;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileSimpleQuantum extends TileEntity {
	
	public double angle = 0;
	public boolean spin = true;
	public boolean spin1 = true;

	public TileSimpleQuantum() {
		super();
    }
	
	public void updateEntity() {
    	super.updateEntity();
    	if (spin != spin1) {
    		spin1 = spin;
    		BlockSimpleQuantum.set(angle, spin, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
    	}
    	markDirty();
    }
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);

	 	this.angle = nbt.getDouble("angle");
	  	this.spin = nbt.getBoolean("spin");
	  	this.spin1 = nbt.getBoolean("spin1");
	}

	public void writeToNBT(NBTTagCompound nbt) {
	  	super.writeToNBT(nbt);

	  	nbt.setDouble("angle", this.angle);
	 	nbt.setBoolean("spin", this.spin);
	 	nbt.setBoolean("spin1", this.spin1);
	}
	
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readFromNBT(packet.func_148857_g());
	}
}
