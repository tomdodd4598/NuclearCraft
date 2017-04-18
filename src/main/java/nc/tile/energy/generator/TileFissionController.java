package nc.tile.energy.generator;

import ic2.api.energy.event.EnergyTileUnloadEvent;
import nc.ModCheck;
import nc.block.fission.BlockCooler;
import nc.block.tile.energy.generator.BlockFissionController;
import nc.config.NCConfig;
import nc.crafting.generator.FissionRecipes;
import nc.handler.EnumHandler.CoolerTypes;
import nc.init.NCBlocks;
import nc.item.fission.IFissionableItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.common.MinecraftForge;

public class TileFissionController extends TileEnergyGeneratorProcessor {
	
	public int rateMultiplier;
	
	public int processTime;
	public int processPower;
	
	public int heat;
	public int cooling;
	public int heatChange;
	public int efficiency;
	public int cells;
	
	public int tickCount;
	
	public int minX;
	public int minY;
	public int minZ;
	
	public int maxX;
	public int maxY;
	public int maxZ;
	
	public int lengthX;
	public int lengthY;
	public int lengthZ;
	
	public int complete;
	public int ready;
	public String problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");

	public TileFissionController() {
		super("fission_controller", 1, 1, 0, 960000, FissionRecipes.instance());
	}
	
	public void updateGenerator() {
		boolean flag = isGenerating;
		boolean flag1 = false;
		if(!world.isRemote) {
			if (time == 0) {
				consume();
			}
			tick();
			checkStructure();
			run();
			overheat();
			if (canProcess() && isPowered()) {
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
			pushEnergy();
		} else {
			isGenerating = canProcess() && isPowered();
		}
		
		if (flag1) {
			markDirty();
		}
	}
	
	public void setBlockState() {
		BlockFissionController.setState(isGenerating, world, pos);
	}
	
	public void onAdded() {
		super.onAdded();
		if (!world.isRemote) checkStructure();
	}
	
	public void tick() {
		if (tickCount > NCConfig.fission_update_rate) {
			tickCount = 0;
		} else {
			tickCount++;
		}
	}
	
	public boolean shouldUpdate() {
		return tickCount > NCConfig.fission_update_rate;
	}
	
	public void overheat() {
		if (heat >= 1000000 && NCConfig.fission_overheat) {
			// meltdown();
			world.setBlockToAir(pos);
			world.setBlockState(pos, Blocks.LAVA.getDefaultState());
			world.removeTileEntity(pos);
		}
	}
	
	public boolean canProcess() {
		return canProcessStacks() && complete == 1;
	}
	
	// IC2 Tiers
	
	public int getSourceTier() {
		return 4;
	}
	
	public int getSinkTier() {
		return 4;
	}
	
	// Generating

	public int getRateMultiplier() {
		return Math.max(0, rateMultiplier);
	}

	public void setRateMultiplier(int value) {
		rateMultiplier = Math.max(0, value);
	}

	public int getProcessTime() {
		return Math.max(1, processTime);
	}

	public void setProcessTime(int value) {
		processTime = Math.max(1, value);
	}

	public int getProcessPower() {
		return processPower;
	}

	public void setProcessPower(int value) {
		processPower = Math.max(0, value);
	}
	
	public double getBaseTime() {
		if (consumedInputs()[0] == ItemStack.EMPTY) return 1;
		return (((ItemStack) consumedInputs()[0]).getItem() instanceof IFissionableItem) ? ((IFissionableItem)((ItemStack) consumedInputs()[0]).getItem()).getBaseTime((ItemStack) consumedInputs()[0]) : 1;
	}

	public double getBasePower() {
		if (consumedInputs()[0] == ItemStack.EMPTY) return 0;
		return (((ItemStack) consumedInputs()[0]).getItem() instanceof IFissionableItem) ? ((IFissionableItem)((ItemStack) consumedInputs()[0]).getItem()).getBasePower((ItemStack) consumedInputs()[0]) : 0;
	}

	public double getBaseHeat() {
		if (consumedInputs()[0] == ItemStack.EMPTY) return 0;
		return (((ItemStack) consumedInputs()[0]).getItem() instanceof IFissionableItem) ? ((IFissionableItem)((ItemStack) consumedInputs()[0]).getItem()).getBaseHeat((ItemStack) consumedInputs()[0]) : 0;
	}
	
	public String getFuelName() {
		if (consumedInputs()[0] == null) return I18n.translateToLocalFormatted("gui.container.fission_controller.no_fuel");
		return (((ItemStack) consumedInputs()[0]).getItem() instanceof IFissionableItem) ? ((IFissionableItem)((ItemStack) consumedInputs()[0]).getItem()).getFuelName((ItemStack) consumedInputs()[0]).toUpperCase().replace('_', '-') : I18n.translateToLocalFormatted("gui.container.fission_controller.no_fuel");
	}
	
	public int getLengthX() {
		return lengthX - 2;
	}
	
	public int getLengthY() {
		return lengthY - 2;
	}
	
	public int getLengthZ() {
		return lengthZ - 2;
	}
	
	// Finding Blocks
	
	/** returns true if any of blocks are at relative position {x,y,z} */
	private boolean find(int x, int y, int z, Object... blocks) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 2) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 5) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z)).getBlock() == blocks[i]) return true;
		}
		if (getBlockMetadata() == 3) for (int i = 0; i < blocks.length; i++) {
			if (blocks[i] instanceof IBlockState) if (world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)) == blocks[i]) return true;
			else if (blocks[i] instanceof Block) if (world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x)).getBlock() == blocks[i]) return true;
		}
		
		return false;
	}
	
	private IBlockState getBlockState(int x, int y, int z) {
		int xCheck = getPos().getX();
		int yCheck = getPos().getY() + y;
		int zCheck = getPos().getZ();
		
		if (getBlockMetadata() == 4) {
			return world.getBlockState(new BlockPos(xCheck + x, yCheck, zCheck + z));
		}
		if (getBlockMetadata() == 2) {
			return world.getBlockState(new BlockPos(xCheck - z, yCheck, zCheck + x));
		}
		if (getBlockMetadata() == 5) {
			return world.getBlockState(new BlockPos(xCheck - x, yCheck, zCheck - z));
		}
		if (getBlockMetadata() == 3) {
			return world.getBlockState(new BlockPos(xCheck + z, yCheck, zCheck - x));
		}
		else return Blocks.AIR.getDefaultState();
	}
	
	/** returns true if any of blocks are adjacent */
	private boolean adjacentOr(int x, int y, int z, IBlockState... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) return true;
			if (find(x - 1, y, z, blocks[i])) return true;
			if (find(x, y + 1, z, blocks[i])) return true;
			if (find(x, y - 1, z, blocks[i])) return true;
			if (find(x, y, z + 1, blocks[i])) return true;
			if (find(x, y, z - 1, blocks[i])) return true;
		}
		return false;
	}
	
	/** returns true if each of blocks are adjacent */
	private boolean adjacentAnd(int x, int y, int z, IBlockState... blocks) {
		if (blocks.length > 6) return false;
		int count = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) count++;
			else if (find(x - 1, y, z, blocks[i])) count++;
			else if (find(x, y + 1, z, blocks[i])) count++;
			else if (find(x, y - 1, z, blocks[i])) count++;
			else if (find(x, y, z + 1, blocks[i])) count++;
			else if (find(x, y, z - 1, blocks[i])) count++;
		}
		return count >= blocks.length;
	}
	
	/** returns true if any of blocks are totally surrounding */
	private boolean surrounding(int x, int y, int z, IBlockState... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) {
					if (find(x, y + 1, z, blocks[i])) {
						if (find(x, y - 1, z, blocks[i])) {
							if (find(x, y, z + 1, blocks[i])) {
								if (find(x, y, z - 1, blocks[i])) return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	/** returns true if between any of blocks */
	private boolean betweenOr(int x, int y, int z, IBlockState... blocks) {
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) return true;
			}
			if (find(x, y + 1, z, blocks[i])) {
				if (find(x, y - 1, z, blocks[i])) return true;
			}
			if (find(x, y, z + 1, blocks[i])) {
				if (find(x, y, z - 1, blocks[i])) return true;
			}
		}
		return false;
	}
	
	/** returns true if between all of blocks */
	private boolean betweenAnd(int x, int y, int z, IBlockState... blocks) {
		if (blocks.length > 3) return false;
		int count = 0;
		for (int i = 0; i < blocks.length; i++) {
			if (find(x + 1, y, z, blocks[i])) {
				if (find(x - 1, y, z, blocks[i])) count++;
			} else if (find(x, y + 1, z, blocks[i])) {
				if (find(x, y - 1, z, blocks[i])) count++;
			} else if (find(x, y, z + 1, blocks[i])) {
				if (find(x, y, z - 1, blocks[i])) count++;
			}
		}
		return count >= blocks.length;
	}
	
	/** returns true if sandwiched by the block */
	private boolean sandwich(int x, int y, int z, IBlockState block) {
		if (find(x + 1, y, z, block)) {
			if (find(x - 1, y, z, block)) {
				if (find(x, y + 1, z, block)) {
					if (find(x, y - 1, z, block)) return true;
				}
				if (find(x, y, z + 1, block)) {
					if (find(x, y, z - 1, block)) return true;
				}
			}
		}
		if (find(x, y + 1, z, block)) {
			if (find(x, y - 1, z, block)) {
				if (find(x, y, z + 1, block)) {
					if (find(x, y, z - 1, block)) return true;
				}
			}
		}
		return false;
	}
	
	/** returns number of the block that are adjacent */
	private int adjacent(int x, int y, int z, IBlockState block) {
		int count = 0;
		if (find(x + 1, y, z, block)) count++;
		if (find(x - 1, y, z, block)) count++;
		if (find(x, y + 1, z, block)) count++;
		if (find(x, y - 1, z, block)) count++;
		if (find(x, y, z + 1, block)) count++;
		if (find(x, y, z - 1, block)) count++;
		return count;
	}
	
	private boolean findCooler(int x, int y, int z, int meta) {
		if (getBlockState(x, y, z).getBlock() instanceof BlockCooler) {
			return ((BlockCooler)getBlockState(x, y, z).getBlock()).getMetaFromState(getBlockState(x, y, z)) == meta;
		}
		return false;
	}
	
	private int adjacentToActiveGraphite(int x, int y, int z) {
		IBlockState graphite = NCBlocks.ingot_block.getStateFromMeta(8);
		IBlockState cell = NCBlocks.cell_block.getDefaultState();
		int count = 0;
		if (find(x + 1, y, z, graphite)) {
			if (adjacentOr(x + 1, y, z, cell)) count++;
		}
		if (find(x - 1, y, z, graphite)) {
			if (adjacentOr(x - 1, y, z, cell)) count++;
		}
		if (find(x, y + 1, z, graphite)) {
			if (adjacentOr(x, y + 1, z, cell)) count++;
		}
		if (find(x, y - 1, z, graphite)) {
			if (adjacentOr(x, y - 1, z, cell)) count++;
		}
		if (find(x, y, z + 1, graphite)) {
			if (adjacentOr(x, y, z + 1, cell)) count++;
		}
		if (find(x, y, z - 1, graphite)) {
			if (adjacentOr(x, y, z - 1, cell)) count++;
		}
		return count;
	}
	
	// Finding Structure
	
	private boolean checkStructure() {
		if (shouldUpdate()) {
			int l = NCConfig.fission_max_size + 2;
			IBlockState b = NCBlocks.fission_block.getStateFromMeta(0);
			IBlockState r0 = NCBlocks.fission_controller_idle.getStateFromMeta(0);
			IBlockState r1 = NCBlocks.fission_controller_idle.getStateFromMeta(1);
			IBlockState r2 = NCBlocks.fission_controller_idle.getStateFromMeta(2);
			IBlockState r3 = NCBlocks.fission_controller_idle.getStateFromMeta(3);
			IBlockState r4 = NCBlocks.fission_controller_idle.getStateFromMeta(4);
			IBlockState r5 = NCBlocks.fission_controller_idle.getStateFromMeta(5);
			IBlockState rr0 = NCBlocks.fission_controller_active.getStateFromMeta(0);
			IBlockState rr1 = NCBlocks.fission_controller_active.getStateFromMeta(1);
			IBlockState rr2 = NCBlocks.fission_controller_active.getStateFromMeta(2);
			IBlockState rr3 = NCBlocks.fission_controller_active.getStateFromMeta(3);
			IBlockState rr4 = NCBlocks.fission_controller_active.getStateFromMeta(4);
			IBlockState rr5 = NCBlocks.fission_controller_active.getStateFromMeta(5);
			boolean f = false;
			int rz = 0;
			int z0 = 0;
			int x0 = 0;
			int y0 = 0;
			int z1 = 0;
			int x1 = 0;
			int y1 = 0;
			for (int z = 0; z <= l; z++) {
				if ((find(0, 1, 0, b) || find(0, -1, 0, b)) || ((find(1, 1, 0, b) || find(1, -1, 0, b)) && find(1, 0, 0, b)) || ((find(1, 1, 0, b) && !find(1, -1, 0, b)) && !find(1, 0, 0, b)) || ((!find(1, 1, 0, b) && find(1, -1, 0, b)) && !find(1, 0, 0, b))) {
					if (/*!find(b, 0, 0, -z) &&*/ !find(0, 1, -z, b) && !find(0, -1, -z, b) && (find(0, 0, -z + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) || find(0, 1, -z + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) || find(0, -1, -z + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5))) {
						rz = l - z;
						z0 = -z;
						f = true;
						break;
					}
				} else if (!find(0, 0, -z, b) && !find(1, 1, -z, b) && !find(1, -1, -z, b) && find(0, 0, -z + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(1, 0, -z, b) && find(1, 1, -z + 1, b) && find(1, -1, -z + 1, b)) {
					rz = l - z;
					z0 = -z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (/*!find(b, x0, -y, z0) && */!find(x0, -y + 1, z0, b) && !find(x0 + 1, -y, z0, b) && !find(x0, -y, z0 + 1, b) && find(x0 + 1, -y, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0, -y + 1, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0 + 1, -y + 1, z0, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
					y0 = -y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				return false;
			}
			f = false;
			for (int z = 0; z <= rz; z++) {
				if (/*!find(b, x0, y0, z) &&*/ !find(x0, y0 + 1, z, b) && !find(x0 + 1, y0, z, b) && !find(x0, y0, z - 1, b) && find(x0 + 1, y0, z - 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0, y0 + 1, z - 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0 + 1, y0 + 1, z, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
					z1 = z;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				return false;
			}
			f = false;
			for (int x = 0; x <= l; x++) {
				if (/*!find(b, x0 + x, y0, z0) &&*/ !find(x0 + x, y0 + 1, z0, b) && !find(x0 + x - 1, y0, z0, b) && !find(x0 + x, y0, z0 + 1, b) && find(x0 + x - 1, y0, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0 + x, y0 + 1, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0 + x - 1, y0 + 1, z0, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
					x1 = x0 + x;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				return false;
			}
			f = false;
			for (int y = 0; y <= l; y++) {
				if (/*!find(b, x0, y0 + y, z0) &&*/ !find(x0, y0 + y - 1, z0, b) && !find(x0 + 1, y0 + y, z0, b) && !find(x0, y0 + y, z0 + 1, b) && find(x0 + 1, y0 + y, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0, y0 + y - 1, z0 + 1, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5) && find(x0 + 1, y0 + y - 1, z0, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
					y1 = y0 + y;
					f = true;
					break;
				}
			}
			if (!f) {
				complete = 0;
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
				return false;
			}
			f = false;
			if ((x0 > 0 || x1 < 0) || (y0 > 0 || y1 < 0) || (z0 > 0 || z1 < 0) || x1 - x0 < 1 || y1 - y0 < 1 || z1 - z0 < 1) {
				problem = I18n.translateToLocalFormatted("gui.container.fission_controller.invalid_structure");
				complete = 0;
				return false;
			}
			for (int z = z0; z <= z1; z++) {
				for (int x = x0; x <= x1; x++) {
					for (int y = y0; y <= y1; y++) {
						if(find(x, y, z, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
							if (x == 0 && y == 0 && z == 0) {} else {
								problem = I18n.translateToLocalFormatted("gui.container.fission_controller.multiple_controllers");
								complete = 0;
								return false;
							}
						}
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					if(!find(x, y0, z, b) && !(x == 0 && y0 == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
					if(!find(x, y1, z, b) && !(x == 0 && y1 == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
				}
			}
			for (int y = y0 + 1; y <= y1 - 1; y++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					if(!find(x, y, z0, b) && !(x == 0 && y == 0 && z0 == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
					if(!find(x, y, z1, b) && !(x == 0 && y == 0 && z1 == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
				}
				for (int z = z0 + 1; z <= z1 - 1; z++) {
					if(!find(x0, y, z, b) && !(x0 == 0 && y == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
					if(!find(x1, y, z, b) && !(x1 == 0 && y == 0 && z == 0)) {
						problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
						complete = 0;
						return false;
					}
				}
			}
			for (int z = z0 + 1; z <= z1 - 1; z++) {
				for (int x = x0 + 1; x <= x1 - 1; x++) {
					for (int y = y0 + 1; y <= y1 - 1; y++) {
						if(find(x, y, z, b, r0, r1, r2, r3, r4, r5, rr0, rr1, rr2, rr3, rr4, rr5)) {
							problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_in_interior");
							complete = 0;
							return false;
						}
					}
				}
			}
			//problem = I18n.translateToLocalFormatted("gui.container.fission_controller.casing_incomplete");
			complete = 1;
			minX = x0;
			minY = y0;
			minZ = z0;
			maxX = x1;
			maxY = y1;
			maxZ = z1;
			lengthX = x1 + 1 - x0;
			lengthY = y1 + 1 - y0;
			lengthZ = z1 + 1 - z0;
			return true;
		} else {
			return complete == 1;
		}
	}
	
	// Set Fuel and Power and Modify Heat
	
	private void run() {
		double energyThisTick = 0;
		double fuelThisTick = 0;
		double heatThisTick = 0;
		double coolerHeatThisTick = 0;
		double numberOfCells = 0;
		double extraCells = 0;
		double adj1 = 0;
		double adj2 = 0;
		double adj3 = 0;
		double adj4 = 0;
		double adj5 = 0;
		double adj6 = 0;
		boolean generating = false;
		
		double baseRF = getBasePower();
		processTime = (int) getBaseTime();
		double baseHeat = getBaseHeat();
		
		IBlockState graphite = NCBlocks.ingot_block.getStateFromMeta(8);
		IBlockState cell = NCBlocks.cell_block.getDefaultState();
		IBlockState casing = NCBlocks.fission_block.getStateFromMeta(0);
		
		IBlockState[] cooler = new IBlockState[CoolerTypes.values().length];
		for (int i = 0; i < CoolerTypes.values().length; i++) {
			cooler[i] = NCBlocks.cooler.getStateFromMeta(i);
		}
		
		if (canProcess() && isPowered()) {
			ready = 0;
			generating = true;
		} else if (canProcess() && !isPowered()) {
			ready = 1;
			generating = false;
		} else {
			ready = 0;
			generating = false;
		}

		if (shouldUpdate()) {
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if (find(x, y, z, cell)) {
								extraCells = 0;
								if (find(x + 1, y, z, cell) || (find(x + 1, y, z, graphite) && find(x + 2, y, z, cell))) extraCells += 1;
								if (find(x - 1, y, z, cell) || (find(x - 1, y, z, graphite) && find(x - 2, y, z, cell))) extraCells += 1;
								if (find(x, y + 1, z, cell) || (find(x, y + 1, z, graphite) && find(x, y + 2, z, cell))) extraCells += 1;
								if (find(x, y - 1, z, cell) || (find(x, y - 1, z, graphite) && find(x, y - 2, z, cell))) extraCells += 1;
								if (find(x, y, z + 1, cell) || (find(x, y, z + 1, graphite) && find(x, y, z + 2, cell))) extraCells += 1;
								if (find(x, y, z - 1, cell) || (find(x, y, z - 1, graphite) && find(x, y, z - 2, cell))) extraCells += 1;
								
								if (extraCells == 0) numberOfCells += 1;
								else if (extraCells == 1) adj1 += 1;
								else if (extraCells == 2) adj2 += 1;
								else if (extraCells == 3) adj3 += 1;
								else if (extraCells == 4) adj4 += 1;
								else if (extraCells == 5) adj5 += 1;
								else if (extraCells == 6) adj6 += 1;
							}
						}
					}
				}
			}
			
			if (ready == 1 || generating) {
				
				energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6);
				heatThisTick += NCConfig.fission_heat_generation*baseHeat*(numberOfCells + 3*adj1 + 6*adj2 + 10*adj3 + 15*adj4 + 21*adj5 + 28*adj6);
				if (generating) fuelThisTick += (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/NCConfig.fission_fuel_use;
				
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if(find(x, y, z, graphite)) {
								heatThisTick += NCConfig.fission_heat_generation*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/16.0D;
								if (adjacentOr(x, y, z, cell)) energyThisTick += NCConfig.fission_power*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/8.0D;
							}
						}
					}
				}
			}
			  	
			if (complete == 1) {
				for (int z = minZ + 1; z <= maxZ - 1; z++) {
					for (int x = minX + 1; x <= maxX - 1; x++) {
						for (int y = minY + 1; y <= maxY - 1; y++) {
							if(findCooler(x, y, z, 1)) {
								if (adjacentOr(x, y, z, casing)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[0];
							}
							if(findCooler(x, y, z, 2)) {
								if (adjacentOr(x, y, z, cell)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[1];
							}
							if(findCooler(x, y, z, 3)) {
								if (adjacentToActiveGraphite(x, y, z) >= 1) coolerHeatThisTick -= NCConfig.fission_cooling_rate[2];
							}
							if(findCooler(x, y, z, 4)) {
								if (adjacentAnd(x, y, z, cooler[1], cooler[2])) coolerHeatThisTick -= NCConfig.fission_cooling_rate[3];
							}
							if(findCooler(x, y, z, 5)) {
								if (adjacentToActiveGraphite(x, y, z) >= 3) coolerHeatThisTick -= NCConfig.fission_cooling_rate[4];
							}
							if(findCooler(x, y, z, 6)) {
								if (adjacentAnd(x, y, z, cell, casing)) coolerHeatThisTick -= NCConfig.fission_cooling_rate[5];
							}
							if(findCooler(x, y, z, 7)) {
								if (sandwich(x, y, z, cooler[1])) coolerHeatThisTick -= NCConfig.fission_cooling_rate[6];
							}
							if(findCooler(x, y, z, 8)) {
								if (betweenOr(x, y, z, cooler[2])) coolerHeatThisTick -= NCConfig.fission_cooling_rate[7];
							}
							if(findCooler(x, y, z, 9)) {
								if (adjacent(x, y, z, casing) >= 3) coolerHeatThisTick -= NCConfig.fission_cooling_rate[8];
							}
							if(findCooler(x, y, z, 10)) {
								if (adjacent(x, y, z, cell) >= 4) coolerHeatThisTick -= NCConfig.fission_cooling_rate[9];
							}
						}
					}
				}
				// Allow TBU fuel in minimum size reactor
				if (lengthX + lengthY + lengthZ < 10) coolerHeatThisTick -= NCConfig.fission_thorium_heat_generation[0];
			}
			
			if (complete == 1) {
				heatChange = (int) (heatThisTick + coolerHeatThisTick);
				cooling = (int) coolerHeatThisTick;
				efficiency = (int) ((energyThisTick)/(NCConfig.fission_power*baseRF*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)));
				cells = (int) (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6);
				setProcessPower((int) energyThisTick);
				setRateMultiplier((int) fuelThisTick);
			} else {
				heatChange = 0;
				cooling = 0;
				efficiency = 0;
				cells = 0;
				setProcessPower(0);
				setRateMultiplier(0);
			}
		}
		
		if (ready == 0 && generating) {
			if (heat + (int) heatChange >= 0) {
				heat += (int) heatChange;
			} else {
				heat = 0;
			}
		} else if (ready == 1 && !generating) {
			if (heat + (int) cooling >= 0) {
				heat += (int) cooling;
			} else {
				heat = 0;
			}
		}
	}
	
	// NBT
	
	public NBTTagCompound writeAll(NBTTagCompound nbt) {
		super.writeAll(nbt);
		//nbt.setInteger("processTime", processTime);
		nbt.setInteger("processPower", processPower);
		nbt.setInteger("rateMultiplier", rateMultiplier);
		nbt.setInteger("heat", heat);
		nbt.setInteger("cooling", cooling);
		nbt.setInteger("efficiency", efficiency);
		nbt.setInteger("cells", cells);
		nbt.setInteger("minX", minX);
		nbt.setInteger("minY", minY);
		nbt.setInteger("minZ", minZ);
		nbt.setInteger("maxX", maxX);
		nbt.setInteger("maxY", maxY);
		nbt.setInteger("maxZ", maxZ);
		nbt.setInteger("lengthX", lengthX);
		nbt.setInteger("lengthY", lengthY);
		nbt.setInteger("lengthZ", lengthZ);
		nbt.setInteger("heatChange", heatChange);
		nbt.setInteger("complete", complete);
		nbt.setInteger("ready", ready);
		nbt.setString("problem", problem);
		return nbt;
	}
			
	public void readAll(NBTTagCompound nbt) {
		super.readAll(nbt);
		//processTime = nbt.getInteger("processTime");
		processPower = nbt.getInteger("processPower");
		rateMultiplier = nbt.getInteger("rateMultiplier");
		heat = nbt.getInteger("heat");
		cooling = nbt.getInteger("cooling");
		efficiency = nbt.getInteger("efficiency");
		cells = nbt.getInteger("cells");
		minX = nbt.getInteger("minX");
		minY = nbt.getInteger("minY");
		minZ = nbt.getInteger("minZ");
		maxX = nbt.getInteger("maxX");
		maxY = nbt.getInteger("maxY");
		maxZ = nbt.getInteger("maxZ");
		lengthX = nbt.getInteger("lengthX");
		lengthY = nbt.getInteger("lengthY");
		lengthZ = nbt.getInteger("lengthZ");
		heatChange = nbt.getInteger("heatChange");
		complete = nbt.getInteger("complete");
		ready = nbt.getInteger("ready");
		problem = nbt.getString("problem");
	}
	
	// Inventory Fields

	public int getFieldCount() {
		return 15;
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
			return heat;
		case 5:
			return cooling;
		case 6:
			return efficiency;
		case 7:
			return cells;
		case 8:
			return getRateMultiplier();
		case 9:
			return lengthX;
		case 10:
			return lengthY;
		case 11:
			return lengthZ;
		case 12:
			return heatChange;
		case 13:
			return complete;
		case 14:
			return ready;
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
			cooling = value;
			break;
		case 6:
			efficiency = value;
			break;
		case 7:
			cells = value;
			break;
		case 8:
			setRateMultiplier(value);
			break;
		case 9:
			lengthX = value;
			break;
		case 10:
			lengthY = value;
			break;
		case 11:
			lengthZ = value;
			break;
		case 12:
			heatChange = value;
			break;
		case 13:
			complete = value;
			break;
		case 14:
			ready = value;
		}
	}
}
