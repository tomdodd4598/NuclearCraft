package nc.tile.generator;

import nc.NuclearCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileDenseSteamDecompressor extends TileEntity implements IFluidHandler {
	public int fluid;
	public FluidTank tank;
	public int fluid2;
	public FluidTank tank2;

	public TileDenseSteamDecompressor() {
		super();
		tank = new FluidTank(NuclearCraft.steamDecompressRate);
		tank2 = new FluidTank(NuclearCraft.steamDecompressRate*1000);
	}
	
	public void updateEntity() {
    	super.updateEntity();
    	if(!this.worldObj.isRemote) {
    		steam();
    		addSteam();
    	}
        markDirty();
    }
	
	public void addSteam() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			if (tile instanceof TileSteamDecompressor) {
				tank2.drain(((TileSteamDecompressor)tile).fill(side.getOpposite(), tank2.drain(tank2.getCapacity(), false), true), true);
			}
		}
	}
	
	public void steam() {
		if (tank.getFluidAmount() != 0) {
			if (tank.getFluid().getFluid() != NuclearCraft.superdenseSteam && tank.getFluid().getFluid() != FluidRegistry.getFluid("superdenseSteam")) {
				tank.drain(NuclearCraft.steamDecompressRate, true);
			}
			else if (tank.getFluid().getFluid() == NuclearCraft.superdenseSteam || tank.getFluid().getFluid() == FluidRegistry.getFluid("superdenseSteam")) {
				for (int i = 0; i < NuclearCraft.steamDecompressRate; i++) {
					if (tank2.getFluidAmount() <= tank2.getCapacity() - 1000 && tank.getFluidAmount() != 0) {
						tank2.fill(new FluidStack(NuclearCraft.denseSteam, 1000), true);
						tank.drain(1, true);
					} else break;
				}
			}
		}
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.tank.fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.tank2.drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank2.drain(maxDrain, doDrain);
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == NuclearCraft.superdenseSteam || fluid == FluidRegistry.getFluid("superdenseSteam");
	}

	public boolean canDrain(ForgeDirection from, Fluid fluid) {
		return false;
	}

	public FluidTankInfo[] getTankInfo(ForgeDirection from) {
		return new FluidTankInfo[] {this.tank.getInfo()};
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		this.tank2.readFromNBT(nbt.getCompoundTag("tank2"));
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
		this.tank2.writeToNBT(fluidTag);
		nbt.setTag("tank2", fluidTag);
	}
	
	public void writeFluid(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
		this.tank2.writeToNBT(fluidTag);
		nbt.setTag("tank2", fluidTag);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("fluid", this.tank.getFluidAmount());
		this.fluid = nbtTag.getInteger("fluid");
		nbtTag.setInteger("fluid2", this.tank.getFluidAmount());
		this.fluid2 = nbtTag.getInteger("fluid2");
		writeFluid(nbtTag);
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void readFluid(NBTTagCompound nbt) {
		if (nbt.hasKey("tank")) {
			this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
		if (nbt.hasKey("tank2")) {
			this.tank2.readFromNBT(nbt.getCompoundTag("tank2"));
		}
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFluid(packet.func_148857_g());
		this.readFromNBT(packet.func_148857_g());
	}
}