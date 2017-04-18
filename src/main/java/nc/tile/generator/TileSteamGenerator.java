package nc.tile.generator;

import nc.NuclearCraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

public class TileSteamGenerator extends TileContinuousBase implements IFluidHandler {
	public int fluid;
	public FluidTank tank;

	public TileSteamGenerator() {
		super("steamGenerator", NuclearCraft.steamRFUsageRate*20);
		tank = new FluidTank(NuclearCraft.steamRFUsageRate);
	}
	
	public void energy() {
		storage.receiveEnergy(2*tank.getFluidAmount(), false);
		tank.drain(tank.getFluidAmount(), true);
	}

	public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
		return this.tank.fill(resource, doFill);
	}

	public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
		return this.tank.drain(resource.amount, doDrain);
	}

	public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
		return this.tank.drain(maxDrain, doDrain);
	}

	public boolean canFill(ForgeDirection from, Fluid fluid) {
		return fluid == NuclearCraft.steam || fluid == FluidRegistry.getFluid("steam");
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
	}

	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}
	
	public void writeFluid(NBTTagCompound nbt) {
		NBTTagCompound fluidTag = new NBTTagCompound();
		this.tank.writeToNBT(fluidTag);
		nbt.setTag("tank", fluidTag);
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("fluid", this.tank.getFluidAmount());
		this.fluid = nbtTag.getInteger("fluid");
		writeFluid(nbtTag);
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void readFluid(NBTTagCompound nbt) {
		if (nbt.hasKey("tank")) {
			this.tank.readFromNBT(nbt.getCompoundTag("tank"));
		}
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readFluid(packet.func_148857_g());
		this.readFromNBT(packet.func_148857_g());
	}
}