package nc.tile.machine;
 
import nc.NuclearCraft;
import nc.block.machine.BlockAssembler;
import nc.crafting.machine.AssemblerRecipes;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public class TileAssembler extends TileMachine {
	public static final int[] in = {0, 1, 2, 3, 4};
	
	public static enum Mode {
		KEEP,
	    USE;

		public Mode next() {
			int nextOrd = ordinal() + 1;
			if(nextOrd >= values().length) {
				nextOrd = 0;
			}
			return values()[nextOrd];
		}
	}
	
	public Mode mode;
	
	public TileAssembler() {
		super("Assembler", 250000, 4, 1, true, true, 100, 2000, NuclearCraft.assemblerSpeed, NuclearCraft.assemblerEfficiency, AssemblerRecipes.instance());
		mode = Mode.KEEP;
	}
	
	public void updateEntity() {
		super.updateEntity();
		if (flag != flag1) {
			flag1 = flag;
			BlockAssembler.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		}
		markDirty();
	}
	
	public Mode getMode() {
		return mode;
	}

	public void setMode(Mode mode) {
		if(mode == null) {
			mode = Mode.KEEP;
		}
		if(this.mode != mode) {
			this.mode = mode;
		}
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	public boolean canCook() {
		for (int i = 0; i < inputSize; i++) {
			if (this.slots[i] == null) {
				flag = false;
				return false;
			}
		}
		ItemStack[] output = getOutput(inputs());
		for (int i = 0; i < inputSize; i++) {
			if (recipes != null) {
				if (mode == Mode.KEEP && recipes.getInputSize(i, output) >= this.slots[i].stackSize) {
					flag = false;
					return false;
				} else if (mode == Mode.USE && recipes.getInputSize(i, output) > this.slots[i].stackSize) {
					flag = false;
					return false;
				}
			}
		}
		if (this.cookTime >= getFurnaceSpeed) {
			flag = true;
			return true;
		}
		if (getRequiredEnergy > this.energyStorage.getMaxEnergyStored() && cookTime <= 0 && this.energyStorage.getEnergyStored() < this.energyStorage.getMaxEnergyStored() - (int) Math.ceil(getRequiredEnergy/getFurnaceSpeed)) {
			flag = false;
			return false;
		}
		if (hasEnergy) {
			if (this.energyStorage.getEnergyStored() < 1*((int) Math.ceil(getRequiredEnergy/getFurnaceSpeed))) {
				flag = false;
				return false;
			}
			if (this.energyStorage.getEnergyStored() == 0) {
				flag = false;
				return false;
			}
		}
		if (output == null || output.length != outputSize) {
			return false;
		}
		for(int j = 0; j < outputSize; j++) {
			if (output[j] == null) {
				flag = false;
				return false;
			} else {
				if (this.slots[j + inputSize] != null) {
					if (!this.slots[j + inputSize].isItemEqual(output[j])) {
						flag = false;
						return false;
					} else if (this.slots[j + inputSize].stackSize + output[j].stackSize > this.slots[j + inputSize].getMaxStackSize()) {
						flag = false;
						return false;
					}
				}
			}
		}
		flag = true;
		return true;
	}
	
	public int[] getAccessibleSlotsFromSide(int i) {
		return in;
	}
	
	public void readMode(NBTTagCompound nbt) {
		short mb = nbt.getShort("mode");
		Mode[] modes = Mode.values();
		if(mb < 0 || mb >= modes.length) {
			mb = 0;
		}
		mode = modes[mb];
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		readMode(nbt);
	}
	
	public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("mode", (short) mode.ordinal());
	}
	
	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("Energy", this.energyStorage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		writeSides(nbtTag);
		writeEnergy(nbtTag);
		nbtTag.setShort("mode", (short) mode.ordinal());
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}
	
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		readMode(packet.func_148857_g());
	}
}