package nc.tile.passive;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.tile.dummy.IInterfaceable;
import nc.tile.energyFluid.TileEnergyFluidSidedInventory;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public abstract class TilePassive extends TileEnergyFluidSidedInventory implements IInterfaceable {
	
	public int tickCount;
	public final int updateRate;
	public boolean isRunning;
	public boolean energyBool;
	public boolean stackBool;
	public boolean fluidBool;
	
	public final int energyChange;
	public final ItemStack stackChange;
	public final int itemChange;
	public final int fluidChange;
	public final FluidStack fluidStackChange;
	
	public TilePassive(String name, int energyChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, energyChange, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassive(String name, ItemStack stack, int itemChange, int changeRate) {
		this(name, stack, itemChange, 0, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassive(String name, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassive(String name, ItemStack stack, int itemChange, int energyChange, int changeRate) {
		this(name, stack, itemChange, 0, FluidRegistry.LAVA, 0, changeRate);
	}
	
	public TilePassive(String name, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, new ItemStack(Items.BEEF), 0, energyChange, fluid, fluidChange, changeRate);
	}
	
	public TilePassive(String name, ItemStack stack, int itemChange, Fluid fluid, int fluidChange, int changeRate) {
		this(name, stack, itemChange, 0, fluid, fluidChange, changeRate);
	}
	
	public TilePassive(String name, ItemStack stack, int itemChange, int energyChange, Fluid fluid, int fluidChange, int changeRate) {
		super(name, 1, energyChange == 0 ? 1 : 2*MathHelper.abs(energyChange)*changeRate*NCConfig.generator_rf_per_eu, energyChange == 0 ? 0 : MathHelper.abs(energyChange)*NCConfig.generator_rf_per_eu, energyChange > 0 ? EnergyConnection.OUT : (energyChange < 0 ? EnergyConnection.IN : EnergyConnection.NON), new int[] {fluidChange == 0 ? 1 : 2*MathHelper.abs(fluidChange)*changeRate}, new FluidConnection[] {fluidChange > 0 ? FluidConnection.OUT : (fluidChange < 0 ? FluidConnection.IN : FluidConnection.NON)}, new String[] {fluid.getName()});
		this.energyChange = energyChange*changeRate;
		this.itemChange = itemChange*changeRate;
		stackChange = new ItemStack(stack.getItem(), MathHelper.abs(itemChange)*changeRate, stack.getMetadata());
		this.fluidChange = fluidChange*changeRate;
		fluidStackChange = new FluidStack(fluid, MathHelper.abs(fluidChange)*changeRate);
		updateRate = changeRate*20;
	}
	
	public void update() {
		boolean flag = isRunning;
		boolean flag1 = false;
		super.update();
		if(!world.isRemote) {
			tick();
			if (shouldUpdate()) {
				energyBool = changeEnergy();
				stackBool = changeStack();
				fluidBool = changeFluid();
			}
			isRunning = isRunning(energyBool, stackBool, fluidBool);
			if (flag != isRunning) {
				flag1 = true;
				setBlockState();
				//invalidate();
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
					isEnergyTileSet = false;
				}
			}
			if (energyChange > 0) pushEnergy();
			if (fluidChange > 0) pushFluid();
		}
		if (flag1) {
			markDirty();
		}
	}
	
	public void tick() {
		if (tickCount > updateRate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldUpdate() {
		return tickCount > updateRate;
	}
	
	public boolean changeEnergy() {
		if (energyChange == 0) return false;
		if (storage.getEnergyStored() >= storage.getMaxEnergyStored() && energyChange > 0) return false;
		if (storage.getEnergyStored() < MathHelper.abs(energyChange) && energyChange < 0) return false;
		storage.changeEnergyStored(energyChange);
		if (energyChange < 0) return storage.getEnergyStored() > 0;
		else return true;
	}
	
	public boolean changeStack() {
		if (itemChange == 0) return false;
		if (!ItemStack.areItemsEqual(inventoryStacks.get(0), stackChange)) inventoryStacks.set(0, ItemStack.EMPTY);
		if (itemChange > 0) {
			if (inventoryStacks.get(0) != ItemStack.EMPTY) if (inventoryStacks.get(0).getCount() + itemChange > getInventoryStackLimit()) return false;
			if (inventoryStacks.get(0) == ItemStack.EMPTY) inventoryStacks.set(0, stackChange);
			else inventoryStacks.get(0).setCount(inventoryStacks.get(0).getCount() + itemChange);
			return true;
		} else {
			if (inventoryStacks.get(0) == ItemStack.EMPTY || inventoryStacks.get(0).getCount() < MathHelper.abs(itemChange)) return false;
			else if (inventoryStacks.get(0).getCount() > MathHelper.abs(itemChange)) inventoryStacks.get(0).setCount(inventoryStacks.get(0).getCount() + itemChange);
			else if (inventoryStacks.get(0).getCount() == MathHelper.abs(itemChange)) inventoryStacks.set(0, ItemStack.EMPTY);
			return true;
		}
	}
	
	public boolean changeFluid() {
		if (fluidChange == 0) return false;
		if (tanks[0].getFluidAmount() >= tanks[0].getCapacity() && fluidChange > 0) return false;
		if (tanks[0].getFluidAmount() < MathHelper.abs(fluidChange) && fluidChange < 0) return false;
		tanks[0].changeFluidStored(fluidStackChange.getFluid(), fluidStackChange.amount);
		return true;
	}
	
	public abstract void setBlockState();
	
	public boolean isRunning(boolean energy, boolean stack, boolean fluid) {
		if (energyChange >= 0) {
			if (itemChange >= 0) {
				if (fluidChange >= 0) return energy || stack || fluid;
				else return fluid;
			} else {
				if (fluidChange >= 0) return stack;
				else return stack && fluid;
			}
		} else {
			if (itemChange >= 0) {
				if (fluidChange >= 0) return energy;
				else return energy && fluid;
			} else {
				if (fluidChange >= 0) return energy && stack;
				else return energy && stack && fluid;
			}
		}
	}
	
	// Inventory
	
	public int getInventoryStackLimit() {
		return itemChange == 0 ? 1 : 2*MathHelper.abs(itemChange);
	}
	
	// Sided Inventory

	public int[] getSlotsForFace(EnumFacing side) {
		return new int[] {0};
	}

	public boolean canInsertItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange < 0 && ItemStack.areItemsEqual(stack, stackChange);
	}

	public boolean canExtractItem(int slot, ItemStack stack, EnumFacing direction) {
		return itemChange > 0;
	}
	
	// IC2 EU

	public int getSourceTier() {
		return 2;
	}

	public int getSinkTier() {
		return 4;
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setBoolean("isRunning", isRunning);
		nbt.setBoolean("energyBool", energyBool);
		nbt.setBoolean("stackBool", stackBool);
		nbt.setBoolean("fluidBool", fluidBool);
		return nbt;
	}
		
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		isRunning = nbt.getBoolean("isRunning");
		energyBool = nbt.getBoolean("energyBool");
		stackBool = nbt.getBoolean("stackBool");
		fluidBool = nbt.getBoolean("fluidBool");
	}
}
