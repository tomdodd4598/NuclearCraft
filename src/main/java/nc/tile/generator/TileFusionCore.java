package nc.tile.generator;

import java.util.ArrayList;
import java.util.List;

import ic2.api.energy.EnergyNet;
import nc.ModCheck;
import nc.block.fluid.BlockFluidPlasma;
import nc.block.tile.generator.BlockFusionCore;
import nc.block.tile.passive.BlockActiveCooler;
import nc.config.NCConfig;
import nc.energy.EnumStorage.EnergyConnection;
import nc.fluid.Tank;
import nc.handler.SoundHandler;
import nc.init.NCBlocks;
import nc.init.NCFluids;
import nc.recipe.generator.FusionRecipes;
import nc.tile.fluid.TileActiveCooler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRedstoneComparator;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;

public class TileFusionCore extends TileFluidGenerator {
	
	public double rateMultiplier;
	
	public double processTime;
	public double processPower;
	
	public double heat;
	public double efficiency;
	
	public int tickCount;
	public int soundCount;
	
	public int size;
	
	public int complete;
	public String problem = I18n.translateToLocalFormatted("gui.container.fusion_core.ring_incomplete");
	
	public TileFusionCore() {
		super("Fusion Core", 2, 4, 0, tankCapacities(32000, 2, 4), fluidConnections(2, 4), validFluids(FusionRecipes.instance()), 8192000, FusionRecipes.instance());
		areTanksShared = false;
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!worldObj.isRemote) {
			if (time == 0) {
				consume();
			}
		}
			tick();
			setSize();
		if(!worldObj.isRemote) {
			run();
			heating();
			if (shouldCheck() && NCConfig.fusion_active_cooling) cooling();
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
				if (isEnergyTileSet && ModCheck.ic2Loaded()) {
					/*MinecraftForge.EVENT_BUS.post(new EnergyTileUnloadEvent(this));*/ EnergyNet.instance.removeTile(this);
					isEnergyTileSet = false;
				}
				//invalidate();
			}
			if (isHotEnough()) pushEnergy();
			if (findAdjacentComparator() && shouldCheck()) flag1 = true;
		} else {
			isGenerating = /*canProcess() && !isPowered()*/ !isPowered() && time > 0 && isHotEnough();
			playSounds();
		}
		
		if (flag1) {
			markDirty();
		}
	}

	public void setBlockState() {
		BlockFusionCore.setState(worldObj, pos);
	}
	
	public void overheat() {
		if (heat >= getMaxHeat() && NCConfig.fusion_overheat) {
			meltdown();
		}
	}
	
	public void meltdown() {
		worldObj.removeTileEntity(pos);
		worldObj.setBlockState(pos, NCFluids.block_plasma.getDefaultState());
		for (int r = -size - 2; r <= size + 2; r++) {
			worldObj.removeTileEntity(position(r, 0, size + 2));
			worldObj.setBlockToAir(position(r, 0, size + 2));
			worldObj.setBlockState(position(r, 1, size + 2), Blocks.LAVA.getDefaultState());
			worldObj.removeTileEntity(position(r, 0, -size - 2));
			worldObj.setBlockToAir(position(r, 0, -size - 2));
			worldObj.setBlockState(position(r, 1, -size - 2), Blocks.LAVA.getDefaultState());
			worldObj.removeTileEntity(position(size + 2, 0, r));
			worldObj.setBlockToAir(position(size + 2, 0, r));
			worldObj.setBlockState(position(size + 2, 1, r), Blocks.LAVA.getDefaultState());
			worldObj.removeTileEntity(position(-size - 2, 0, r));
			worldObj.setBlockToAir(position(-size - 2, 0, r));
			worldObj.setBlockState(position(-size - 2, 1, r), Blocks.LAVA.getDefaultState());
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
	
	public boolean findAdjacentComparator() {
		if (worldObj.getBlockState(position(2, 0, 1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(2, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(2, 0, -1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-2, 0, 1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-2, 0, 0)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-2, 0, -1)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(1, 0, 2)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, 0, 2)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-1, 0, 2)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(1, 0, -2)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(0, 0, -2)).getBlock() instanceof BlockRedstoneComparator) return true;
		if (worldObj.getBlockState(position(-1, 0, -2)).getBlock() instanceof BlockRedstoneComparator) return true;
		return false;
	}
	
	public boolean canProcess() {
		return canProcessStacks() && complete == 1 && isHotEnough();
	}
	
	public boolean isHotEnough() {
		return heat > 8000;
	}
	
	public boolean isGenerating() {
		if (worldObj.isRemote) return isGenerating;
		return !isPowered() && time > 0 && isHotEnough();
	}
	
	public boolean isPowered() {
		return (
				worldObj.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()-1)) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ())) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX()-1, pos.getY(), pos.getZ()+1)) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ()-1)) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ())) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1)) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()-1)) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ())) ||
				worldObj.isBlockPowered(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()+1))
				);
	}
	
	public void playSounds() {
		if (soundCount >= getSoundTime()) {
			if (isGenerating) {
				worldObj.playSound(pos.getX(), pos.getY() + 1, pos.getZ(), getSound(), SoundCategory.BLOCKS, 1F, 1.0F, false);
				for (int r = 0; r <= (size - 1)/2; r++) {
					worldObj.playSound(pos.getX() - size - 2 + 2*r*(2*size + 5)/size, pos.getY() + 1, pos.getZ() + size + 2, getSound(), SoundCategory.BLOCKS, 0.8F, 1F, false);
					worldObj.playSound(pos.getX() - size - 2 + 2*r*(2*size + 5)/size, pos.getY() + 1, pos.getZ() - size - 2, getSound(), SoundCategory.BLOCKS, 0.8F, 1F, false);
					worldObj.playSound(pos.getX() + size + 2, pos.getY() + 1, pos.getZ() + size + 2 - 2*r*(2*size + 5)/size, getSound(), SoundCategory.BLOCKS, 0.8F, 1F, false);
					worldObj.playSound(pos.getX() - size - 2, pos.getY() + 1, pos.getZ() - size - 2 + 2*r*(2*size + 5)/size, getSound(), SoundCategory.BLOCKS, 0.8F, 1F, false);
				}
			}
			soundCount = 0;
		} else soundCount ++;
	}
	
	private int getSoundTime() {
		return !NCConfig.fusion_alternate_sound ? SoundHandler.FUSION_RUN_TIME : SoundHandler.ACCELERATOR_RUN_TIME;
	}
	
	private SoundEvent getSound() {
		return !NCConfig.fusion_alternate_sound ? SoundHandler.FUSION_RUN : SoundHandler.ACCELERATOR_RUN;
	}
	
	public boolean canExtract() {
		return isHotEnough();
	}

	public boolean canReceive() {
		return !isHotEnough();
	}
	
	// IC2 Tiers

	public int getSourceTier() {
		return 4;
	}
	
	public int getSinkTier() {
		return 4;
	}
	
	// Fluids
	
	/*public boolean canFill(FluidStack resource, int tankNumber) {
		return true;
	}*/
	
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
	
	public double getMaxHeat() {
		return 20000000D;
	}
	
	public ArrayList getComboStats() {
		return getRecipe(hasConsumed) != null ? getRecipe(hasConsumed).extras() : null;
	}
	
	public double getComboTime() {
		if (getComboStats() != null) if (getComboStats().get(0) instanceof Double) {
			return (double) getComboStats().get(0)*NCConfig.fusion_fuel_use;
		}
		return NCConfig.fusion_fuel_use;
	}
	
	public double getComboPower() {
		if (getComboStats() != null) if (getComboStats().get(1) instanceof Double) {
			return (double) getComboStats().get(1);
		}
		return 0;
	}
	
	public double getComboHeatVariable() {
		if (getComboStats() != null) if (getComboStats().get(2) instanceof Double) {
			return (double) getComboStats().get(2);
		}
		return 1000;
	}
	
	// Setting Blocks
	
	private void setAir(int x, int y, int z) {
		worldObj.setBlockToAir(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
	}
	
	private void setPlasma(int x, int y, int z) {
		worldObj.setBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z), NCFluids.block_plasma.getDefaultState());
	}
	
	public void plasma() {
		if (!isPowered() && isHotEnough() && complete == 1) {
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!findPlasma(r, 1, size + 2)) setPlasma(r, 1, size + 2);
				if (!findPlasma(r, 1, -size - 2)) setPlasma(r, 1, -size - 2);
				if (!findPlasma(size + 2, 1, r)) setPlasma(size + 2, 1, r);
				if (!findPlasma(-size - 2, 1, r)) setPlasma(-size - 2, 1, r);
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!isPowered() && !isHotEnough() && complete == 1) {
				if (findPlasma(r, 1, size + 2)) setAir(r, 1, size + 2);
				if (findPlasma(r, 1, -size - 2)) setAir(r, 1, -size - 2);
				if (findPlasma(size + 2, 1, r)) setAir(size + 2, 1, r);
				if (findPlasma(-size - 2, 1, r)) setAir(-size - 2, 1, r);
			}
		}
	}
	
	// Finding Blocks
	
	private BlockPos position(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return new BlockPos(xCheck + x, yCheck, zCheck + z);
		}
		if (getBlockMetadata() == 2) {
			return new BlockPos(xCheck - z, yCheck, zCheck + x);
		}
		if (getBlockMetadata() == 5) {
			return new BlockPos(xCheck - x, yCheck, zCheck - z);
		}
		if (getBlockMetadata() == 3) {
			return new BlockPos(xCheck + z, yCheck, zCheck - x);
		}
		else return new BlockPos(xCheck + x, yCheck, zCheck + z);
	}
	
	private boolean findConnector(int x, int y, int z) {
		IBlockState findState = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_connector.getDefaultState()) return true;
		return false;
	}
	
	private boolean findElectromagnetActive(int x, int y, int z) {
		IBlockState findState = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_electromagnet_active.getDefaultState()) return true;
		if (findState == NCBlocks.fusion_electromagnet_transparent_active.getDefaultState()) return true;
		return false;
	}
	
	private boolean findElectromagnet(int x, int y, int z) {
		IBlockState findState = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z));
		if (findState == NCBlocks.fusion_electromagnet_idle.getDefaultState()) return true;
		if (findState == NCBlocks.fusion_electromagnet_active.getDefaultState()) return true;
		if (findState == NCBlocks.fusion_electromagnet_transparent_idle.getDefaultState()) return true;
		if (findState == NCBlocks.fusion_electromagnet_transparent_active.getDefaultState()) return true;
		return false;
	}
	
	private boolean findAir(int x, int y, int z) {
		Material mat = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getMaterial();
		Block findBlock = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
		return mat == Material.AIR || mat == Material.FIRE || mat == Material.WATER || mat == Material.VINE || mat == Material.SNOW || findBlock instanceof BlockFluidPlasma;
	}
	
	private boolean findPlasma(int x, int y, int z) {
		Block findBlock = worldObj.getBlockState(new BlockPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z)).getBlock();
		return findBlock instanceof BlockFluidPlasma;
	}
	
	private boolean findActiveCooler(int x, int y, int z) {
		if (worldObj.getBlockState(position(x, y, z)).getBlock() instanceof BlockActiveCooler) {
			if (worldObj.getTileEntity(position(x, y, z)) == null) return false;
			else if (worldObj.getTileEntity(position(x, y, z)) instanceof TileActiveCooler) return true;
		}
		return false;
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
		double heatChange = 0;
		if (canProcess() && !isPowered()) {
			heatChange = NCConfig.fusion_heat_generation*(100D - (0.9*efficiency))/2.2D;
			setProcessPower((int) (efficiency*NCConfig.fusion_base_power*size*getComboPower()));
			setRateMultiplier(size);
		} else {
			heatChange = 0;
			setProcessPower(0);
			setRateMultiplier(0);
			
			if (heat >= 0.3D) {
				heat = heat-((heat/100000D)*Math.log10(1000*heat-298));
			}
		}
		
		if (heat + heatChange >= 0) {
			heat += heatChange;
		} else {
			heat = 0;
		}
	}
	
	public double efficiency() {
		if (!canProcess()) return 0;
		else if (isHotEnough()) {
			double heatMK = heat/1000D;
	    	double z = 7.415D*(Math.exp(-heatMK/getComboHeatVariable())+Math.tanh(heatMK/getComboHeatVariable())-1);
	    	return 100*Math.pow(z, 2);
	   	} else return 0;
	}
	
	public void heating() {
		if (!canProcess()) {
			if (storage.getEnergyStored() >= 800000) {
				storage.changeEnergyStored(-800000);
				heat = heat + 40*NCConfig.fusion_heat_generation;
			} else {
				double r = 0.00005*storage.getEnergyStored();
				storage.changeEnergyStored(-storage.getEnergyStored());
				heat = heat + r*NCConfig.fusion_heat_generation;
			}
			setConnection(EnergyConnection.IN);
			if (heat < 0) heat = 0;
		}
		else setConnection(EnergyConnection.OUT);
	}
	
	public BlockPos getOpposite(BlockPos pos) {
		BlockPos corePos = this.pos;
		BlockPos relativePos = new BlockPos(pos.getX() - corePos.getX(), pos.getY() - corePos.getY(), pos.getZ() - corePos.getZ());
		return position(-relativePos.getX(), -relativePos.getY() + 2, -relativePos.getZ());
	}
	
	public void cooling() {
		if (complete == 1) {
			List<BlockPos> posList = new ArrayList<BlockPos>();
			for (int r = -size - 3; r <= size + 3; r++) {
				if (findActiveCooler(r, 0, size + 3)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 0, size + 3))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 0, size + 3));
				}
				if (findActiveCooler(r, 2, size + 3)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 2, size + 3))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 2, size + 3));
				}
				
				if (findActiveCooler(r, 0, -size - 3)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 0, -size - 3))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 0, -size - 3));
				}
				if (findActiveCooler(r, 2, -size - 3)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 2, -size - 3))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 2, -size - 3));
				}
				
				if (findActiveCooler(size + 3, 0, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(size + 3, 0, r))).tanks[0].getFluidAmount() > 0) posList.add(position(size + 3, 0, r));
				}
				if (findActiveCooler(size + 3, 2, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(size + 3, 2, r))).tanks[0].getFluidAmount() > 0) posList.add(position(size + 3, 2, r));
				}
				
				if (findActiveCooler(-size - 3, 0, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(-size - 3, 0, r))).tanks[0].getFluidAmount() > 0) posList.add(position(-size - 3, 0, r));
				}
				if (findActiveCooler(-size - 3, 2, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(-size - 3, 2, r))).tanks[0].getFluidAmount() > 0) posList.add(position(-size - 3, 2, r));
				}
			}
			
			for (int r = -size - 1; r <= size + 1; r++) {
				if (findActiveCooler(r, 0, size + 1)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 0, size + 1))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 0, size + 1));
				}
				if (findActiveCooler(r, 2, size + 1)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 2, size + 1))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 2, size + 1));
				}
				
				if (findActiveCooler(r, 0, -size - 1)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 0, -size - 1))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 0, -size - 1));
				}
				if (findActiveCooler(r, 2, -size - 1)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(r, 2, -size - 1))).tanks[0].getFluidAmount() > 0) posList.add(position(r, 2, -size - 1));
				}
				
				if (findActiveCooler(size + 1, 0, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(size + 1, 0, r))).tanks[0].getFluidAmount() > 0) posList.add(position(size + 1, 0, r));
				}
				if (findActiveCooler(size + 1, 2, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(size + 1, 2, r))).tanks[0].getFluidAmount() > 0) posList.add(position(size + 1, 2, r));
				}
				
				if (findActiveCooler(-size - 1, 0, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(-size - 1, 0, r))).tanks[0].getFluidAmount() > 0) posList.add(position(-size - 1, 0, r));
				}
				if (findActiveCooler(-size - 1, 2, r)) {
					if (((TileActiveCooler) worldObj.getTileEntity(position(-size - 1, 2, r))).tanks[0].getFluidAmount() > 0) posList.add(position(-size - 1, 2, r));
				}
			}
			if (posList.isEmpty()) return;
			for (BlockPos pos : posList) {
				TileEntity tile = worldObj.getTileEntity(pos);
				if (tile != null) if (tile instanceof TileActiveCooler) {
					Tank tank = ((TileActiveCooler) tile).getTanks()[0];
					int fluidAmount = tank.getFluidAmount();
					if (fluidAmount > 0) {
						if (heat > 0) {
							double cool_mult = posList.contains(getOpposite(pos)) ? 0.02D*NCConfig.fusion_heat_generation : 0.005D*NCConfig.fusion_heat_generation;
							if (tank.getFluidName() == "water") {
								heat -= NCConfig.fission_active_cooling_rate[0]*fluidAmount*cool_mult;
							}
							else if (tank.getFluidName() == "redstone") {
								heat -= NCConfig.fission_active_cooling_rate[1]*fluidAmount*cool_mult;
							}
							else if (tank.getFluidName() == "glowstone") {
								heat -= NCConfig.fission_active_cooling_rate[2]*fluidAmount*cool_mult;
							}
							else if (tank.getFluidName() == "liquidhelium") {
								heat -= NCConfig.fission_active_cooling_rate[3]*fluidAmount*cool_mult;
							}
							else if (tank.getFluidName() == "ender") {
								heat -= NCConfig.fission_active_cooling_rate[4]*fluidAmount*cool_mult;
							}
							else if (tank.getFluidName() == "cryotheum") {
								heat -= NCConfig.fission_active_cooling_rate[5]*fluidAmount*cool_mult;
							}
						}
						double newHeat = heat;
						if (newHeat > 0D) ((TileActiveCooler) worldObj.getTileEntity(pos)).getTanks()[0].drain(fluidAmount, true);
						if (heat < 0D) heat = 0;
					}
				}
			}
		}
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
		//nbt.setDouble("heatChange", heatChange);
		nbt.setInteger("complete", complete);
		nbt.setString("problem", problem);
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
		//heatChange = nbt.getDouble("heatChange");
		complete = nbt.getInteger("complete");
		problem = nbt.getString("problem");
	}
	
	// Inventory Fields
	
	public int getFieldCount() {
		return 9;
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
			return complete;
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
			complete = value;
		}
	}
}
