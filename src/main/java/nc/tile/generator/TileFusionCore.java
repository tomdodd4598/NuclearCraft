package nc.tile.generator;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.block.fluid.BlockFluidPlasma;
import nc.config.NCConfig;
import nc.crafting.generator.FusionRecipes;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.EnumTank.FluidConnection;
import nc.handler.SoundHandler;
import nc.init.NCBlocks;
import nc.init.NCFluids;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidStack;

public class TileFusionCore extends TileFluidGenerator {
	
	public double rateMultiplier;
	
	public double processTime;
	public double processPower;
	
	public double heat;
	public double heatChange;
	public double efficiency;
	
	public int tickCount;
	public int soundCount;
	
	public int size;
	
	public int complete;
	public int ready;
	public String problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
	
	public int fluidAmount0, fluidAmount1, fluidAmount2, fluidAmount3, fluidAmount4, fluidAmount5;
	
	public TileFusionCore() {
		super("Fusion Core", 2, 4, 0, new int[] {32000, 32000, 32000, 32000, 32000, 32000, 32000, 32000}, new FluidConnection[] {FluidConnection.IN, FluidConnection.IN, FluidConnection.OUT, FluidConnection.OUT, FluidConnection.OUT, FluidConnection.OUT, FluidConnection.NON, FluidConnection.NON}, null, 960000, FusionRecipes.instance());
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!world.isRemote) {
			if (time == 0) {
				consume();
			}
			tick();
			setSize();
			run();
			heating();
			plasma();
			overheat();
			if (canProcess() && !isPowered()) {
				isGenerating = true;
				time += getRateMultiplier();
				storage.changeEnergyStored(getProcessPower());
				if (time >= getProcessTime()) {
					time = 0;
					output();
				}
			} else {
				isGenerating = false;
			}
			if (flag != isGenerating) {
				flag1 = true;
				setBlockState();
				//invalidate();
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));
					isEnergyTileSet = false;
				}
			}
			if (isHotEnough()) pushEnergy();
		} else {
			isGenerating = canProcess() && !isPowered();
		}
		playSounds();
		
		if (flag1) {
			markDirty();
		}
	}

	public void setBlockState() {}
	
	public void overheat() {
		if (heat >= getMaxHeat() && NCConfig.fusion_overheat) {
			// meltdown();
			world.setBlockState(pos, NCFluids.block_plasma.getDefaultState());
			world.destroyBlock(pos, false);
			world.removeTileEntity(pos);
		}
	}
	
	public void tick() {
		if (tickCount > NCConfig.fusion_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldCheck() {
		return tickCount > NCConfig.fusion_update_rate;
	}
	
	public boolean canProcess() {
		return canProcessStacks() && complete == 1 && isHotEnough();
	}
	
	public boolean isHotEnough() {
		return heat > 8;
	}
	
	public boolean isGenerating() {
		if (world.isRemote) return isGenerating;
		return !isPowered() && time > 0 && heat > 8;
	}
	
	public boolean isPowered() {
		return (
				world.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()-1)) ||
				world.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) ||
				world.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()+1)) ||
				world.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) ||
				world.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ())) ||
				world.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) ||
				world.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()-1)) ||
				world.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) ||
				world.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()+1))
				);
	}
	
	public void playSounds() {
		if (soundCount >= SoundHandler.FUSION_RUN_TIME) {
			if (canProcess() && complete == 1 && !isPowered() /*&& NuclearCraft.fusionSounds*/) {
				world.playSound(pos.getX(), pos.getY() + 1, pos.getZ(), SoundHandler.FUSION_RUN, SoundCategory.BLOCKS, 1F, 1.0F, false);
				for (int r = 0; r <= (size - 1)/2; r++) {
					world.playSound(pos.getX() - size - 2 + 2*r*(2*size + 5)/size, pos.getY() + 1, pos.getZ() + size + 2, SoundHandler.FUSION_RUN, SoundCategory.BLOCKS, 0.7F, 1F, false);
					world.playSound(pos.getX() - size - 2 + 2*r*(2*size + 5)/size, pos.getY() + 1, pos.getZ() - size - 2, SoundHandler.FUSION_RUN, SoundCategory.BLOCKS, 0.7F, 1F, false);
					world.playSound(pos.getX() + size + 2, pos.getY() + 1, pos.getZ() + size + 2 - 2*r*(2*size + 5)/size, SoundHandler.FUSION_RUN, SoundCategory.BLOCKS, 0.7F, 1F, false);
					world.playSound(pos.getX() - size - 2, pos.getY() + 1, pos.getZ() - size - 2 + 2*r*(2*size + 5)/size, SoundHandler.FUSION_RUN, SoundCategory.BLOCKS, 0.7F, 1F, false);
				}
			}
			soundCount = 0;
		} else soundCount ++;
	}
	
	// IC2 Tiers

	public int getSourceTier() {
		return 4;
	}
	
	public int getSinkTier() {
		return 4;
	}
	
	// Fluids
	
	public boolean canFill(FluidStack resource, int tankNumber) {
		return true;
	}
	
	// Generating

	public int getRateMultiplier() {
		return (int) Math.max(1, rateMultiplier);
	}

	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(1, value);
	}

	public int getProcessTime() {
		return (int) Math.max(1, processTime);
	}

	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	public int getProcessPower() {
		return (int) processPower;
	}

	public void setProcessPower(int value) {
		processPower = value;
	}
	
	public int getMaxHeat() {
		return 20000;
	}
	
	public Object getComboStats() {
		return recipes.getExtras(consumedInputs());
	}
	
	public double getComboTime() {
		if (getComboStats() != null) if (getComboStats() instanceof double[]) {
			return ((double[]) getComboStats())[0];
		}
		return 1;
	}
	
	public double getComboPower() {
		if (getComboStats() != null) if (getComboStats() instanceof double[]) {
			return ((double[]) getComboStats())[1];
		}
		return 0;
	}
	
	public double getComboHeatVariable() {
		if (getComboStats() != null) if (getComboStats() instanceof double[]) {
			return ((double[]) getComboStats())[2];
		}
		return 1000;
	}
	
	// Setting Blocks
	
	private void setAir(int x, int y, int z) {
		world.setBlockToAir(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
	}
	
	private void setPlasma(int x, int y, int z) {
		world.setBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z), NCFluids.block_plasma.getDefaultState());
	}
	
	public void plasma() {
		if (!isPowered() && heat >= 8 && complete == 1) {
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!findPlasma(r, 1, size + 2)) setPlasma(r, 1, size + 2);
				if (!findPlasma(r, 1, -size - 2)) setPlasma(r, 1, -size - 2);
				if (!findPlasma(size + 2, 1, r)) setPlasma(size + 2, 1, r);
				if (!findPlasma(-size - 2, 1, r)) setPlasma(-size - 2, 1, r);
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!isPowered() && heat < 8 && complete == 1) {
				if (findPlasma(r, 1, size + 2)) setAir(r, 1, size + 2);
				if (findPlasma(r, 1, -size - 2)) setAir(r, 1, -size - 2);
				if (findPlasma(size + 2, 1, r)) setAir(size + 2, 1, r);
				if (findPlasma(-size - 2, 1, r)) setAir(-size - 2, 1, r);
			}
		}
	}
	
	// Finding Blocks
	
	private boolean findConnector(int x, int y, int z) {
		IBlockState findState = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_connector.getDefaultState()) return true;
		return false;
	}
	
	private boolean findElectromagnetActive(int x, int y, int z) {
		IBlockState findState = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_electromagnet_active.getDefaultState()) return true;
		return false;
	}
	
	private boolean findElectromagnet(int x, int y, int z) {
		IBlockState findState = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_electromagnet_idle.getDefaultState()) return true;
		if (findState == NCBlocks.fusion_electromagnet_active.getDefaultState()) return true;
		return false;
	}
	
	private boolean findAir(int x, int y, int z) {
		Material mat = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getMaterial();
		Block findBlock = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
		return mat == Material.AIR || mat == Material.FIRE || mat == Material.WATER || mat == Material.VINE || mat == Material.SNOW || findBlock instanceof BlockFluidPlasma;
	}
	
	private boolean findPlasma(int x, int y, int z) {
		Block findBlock = world.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
		return findBlock instanceof BlockFluidPlasma;
	}
	
	// Finding Ring
	
	public boolean setSize() {
		if (shouldCheck()) {
			int s = 1;
			for (int r = 0; r <= NCConfig.fusion_max_size; r++) {
				if (findConnector(2 + r, 1, 0) && findConnector(-2 - r, 1, 0) && findConnector(0, 1, 2 + r) && findConnector(0, 1, -2 - r)) {
					s ++;
				} else break;
			}
			size = s;
			for (int r = -size - 1; r <= size + 1; r++) {
				if (!(findElectromagnet(r, 1, size + 1) && findElectromagnet(r, 1, -size - 1) && findElectromagnet(size + 1, 1, r) && findElectromagnet(-size - 1, 1, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
					return false;
				}
			}
			for (int r = -size - 3; r <= size + 3; r++) {
				if (!(findElectromagnet(r, 1, size + 3) && findElectromagnet(r, 1, -size - 3) && findElectromagnet(size + 3, 1, r) && findElectromagnet(-size - 3, 1, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
					return false;
				}
			}
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!(findElectromagnet(r, 2, size + 2) && findElectromagnet(r, 2, -size - 2) && findElectromagnet(size + 2, 2, r) && findElectromagnet(-size - 2, 2, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
					return false;
				}
			}
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!(findElectromagnet(r, 0, size + 2) && findElectromagnet(r, 0, -size - 2) && findElectromagnet(size + 2, 0, r) && findElectromagnet(-size - 2, 0, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
					return false;
				}
			}
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!(findAir(r, 1, size + 2) && findAir(r, 1, -size - 2) && findAir(size + 2, 1, r) && findAir(-size - 2, 1, r))) {
					complete = 0;
					I18n.translateToLocalFormatted("gui.container.fusion_core.ring_blocked");
					return false;
				}
			}
			for (int r = -size - 1; r <= size + 1; r++) {
				if (!(findElectromagnetActive(r, 1, size + 1) && findElectromagnetActive(r, 1, -size - 1) && findElectromagnetActive(size + 1, 1, r) && findElectromagnetActive(-size - 1, 1, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.power_issue");
					return false;
				}
			}
			for (int r = -size - 3; r <= size + 3; r++) {
				if (!(findElectromagnetActive(r, 1, size + 3) && findElectromagnetActive(r, 1, -size - 3) && findElectromagnetActive(size + 3, 1, r) && findElectromagnetActive(-size - 3, 1, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.power_issue");
					return false;
				}
			}
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!(findElectromagnetActive(r, 2, size + 2) && findElectromagnetActive(r, 2, -size - 2) && findElectromagnetActive(size + 2, 2, r) && findElectromagnetActive(-size - 2, 2, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.power_issue");
					return false;
				}
			}
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!(findElectromagnetActive(r, 0, size + 2) && findElectromagnetActive(r, 0, -size - 2) && findElectromagnetActive(size + 2, 0, r) && findElectromagnetActive(-size - 2, 0, r))) {
					complete = 0;
					problem = I18n.translateToLocalFormatted("gui.container.fusion_core.power_issue");
					return false;
				}
			}
			complete = 1;
			problem = I18n.translateToLocalFormatted("gui.container.fusion_core.incorrect_structure");
			return true;
		} else {
			return complete == 1;
		}
	}
	
	// Set Fuel and Power and Modify Heat
	
	public void run() {
		processTime = (int) getComboTime();
		efficiency = efficiency();
		if (canProcess()) {
			heatChange = NCConfig.fusion_heat_generation*(100 - (0.9*efficiency))/2000;
			setProcessPower((int) (efficiency*NCConfig.fusion_base_power*size*getComboPower()));
			setRateMultiplier(size);
		} else {
			heatChange = 0;
			setProcessPower(0);
			setRateMultiplier(0);
			
			if (heat >= 0.01 && heat >= (0.0001*heat*Math.log10(heat+Math.E)) /*&& storage.getEnergyStored() >= 20000*/) {
				heat = heat-(0.0001*heat*Math.log10(heat+Math.E));
			}
		}
		
		if (heat + (int) heatChange >= 0) {
			heat += (int) heatChange;
		} else {
			heat = 0;
		}
	}
	
	public double efficiency() {
		if (!canProcess()) return 0;
		else if (heat >= 8) {
	    	return 742*(Math.exp(-heat/getComboHeatVariable())+Math.tanh(heat/getComboHeatVariable())-1);
	   	} else return 0;
	}
	
	public void heating() {
		if (!canProcess()) {
			if (storage.getEnergyStored() >= 800000) {
				storage.changeEnergyStored(-800000);
				heat = heat + 0.04*NCConfig.fusion_heat_generation;
			} else {
				double r = 0.0005*(storage.getEnergyStored()/10000);
				storage.changeEnergyStored(-storage.getEnergyStored());
				heat = heat + r*NCConfig.fusion_heat_generation;
			}
			setConnection(EnergyConnection.IN);
			if (heat < 0) heat = 0;
		}
		else setConnection(EnergyConnection.OUT);
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		nbt.setDouble("processTime", processTime);
		nbt.setDouble("processPower", processPower);
		nbt.setDouble("rateMultiplier", rateMultiplier);
		nbt.setDouble("heat", heat);
		nbt.setDouble("efficiency", efficiency);
		nbt.setInteger("size", size);
		nbt.setDouble("heatChange", heatChange);
		nbt.setInteger("complete", complete);
		nbt.setInteger("ready", ready);
		nbt.setString("problem", problem);
		nbt.setInteger("fluidAmount0", tanks[0].getFluidAmount());
		nbt.setInteger("fluidAmount1", tanks[1].getFluidAmount());
		nbt.setInteger("fluidAmount2", tanks[2].getFluidAmount());
		nbt.setInteger("fluidAmount3", tanks[3].getFluidAmount());
		nbt.setInteger("fluidAmount4", tanks[4].getFluidAmount());
		nbt.setInteger("fluidAmount5", tanks[5].getFluidAmount());
		return nbt;
	}
			
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		processTime = nbt.getDouble("processTime");
		processPower = nbt.getDouble("processPower");
		rateMultiplier = nbt.getDouble("rateMultiplier");
		heat = nbt.getInteger("heat");
		efficiency = nbt.getDouble("efficiency");
		size = nbt.getInteger("size");
		heatChange = nbt.getDouble("heatChange");
		complete = nbt.getInteger("complete");
		ready = nbt.getInteger("ready");
		problem = nbt.getString("problem");
		fluidAmount0 = nbt.getInteger("fluidAmount0");
		fluidAmount1 = nbt.getInteger("fluidAmount1");
		fluidAmount2 = nbt.getInteger("fluidAmount2");
		fluidAmount3 = nbt.getInteger("fluidAmount3");
		fluidAmount4 = nbt.getInteger("fluidAmount4");
		fluidAmount5 = nbt.getInteger("fluidAmount5");
	}
	
	// Inventory Fields
	
	public int getFieldCount() {
		return 17;
	}

	public int getField(int id) {
		switch (id) {
		case 0:
			return time;
		case 1:
			return getEnergyStored();
		case 2:
			return getProcessTime();
		case 3:
			return getProcessPower();
		case 4:
			return (int) heat;
		case 5:
			return (int) efficiency;
		case 6:
			return getRateMultiplier();
		case 7:
			return size;
		case 8:
			return (int) heatChange;
		case 9:
			return complete;
		case 10:
			return ready;
		case 11:
			return tanks[0].getFluidAmount();
		case 12:
			return tanks[1].getFluidAmount();
		case 13:
			return tanks[2].getFluidAmount();
		case 14:
			return tanks[3].getFluidAmount();
		case 15:
			return tanks[4].getFluidAmount();
		case 16:
			return tanks[5].getFluidAmount();
		default:
			return 0;
		}
	}

	public void setField(int id, int value) {
		switch (id) {
		case 0:
			time = value;
			break;
		case 1:
			storage.setEnergyStored(value);
			break;
		case 2:
			setProcessTime(value);
			break;
		case 3:
			setProcessPower(value);
			break;
		case 4:
			heat = value;
			break;
		case 5:
			efficiency = value;
			break;
		case 6:
			setRateMultiplier(value);
			break;
		case 7:
			size = value;
			break;
		case 8:
			heatChange = value;
			break;
		case 9:
			complete = value;
			break;
		case 10:
			ready = value;
			break;
		case 11:
			fluidAmount0 = value;
			break;
		case 12:
			fluidAmount1 = value;
			break;
		case 13:
			fluidAmount2 = value;
			break;
		case 14:
			fluidAmount3 = value;
			break;
		case 15:
			fluidAmount4 = value;
			break;
		case 16:
			fluidAmount5 = value;
		}
	}
}
