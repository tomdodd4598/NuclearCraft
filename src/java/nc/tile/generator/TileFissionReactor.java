package nc.tile.generator;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.block.generator.BlockFissionReactor;
import nc.handler.BombType;
import nc.handler.EntityBomb;
import nc.handler.NCExplosion;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TileFissionReactor extends TileGenerator {
	public int rawR = 1;
    
    public int R = rawR; 
    
    public int D = R*2 + 1;
    public int SR = R-1; 
    public int SD = SR*2 + 1;
    public int off = 0;
    
    public int E;
    public int H;
    public int energy;
    public int fueltime;
	public int fueltype;
    public int heat;
    public static double power = NuclearCraft.fissionRF/100;
    public static double pMult = NuclearCraft.fissionRF;
    public String typeoffuel = StatCollector.translateToLocal("gui.noFuel");
    public int fuelmult;
    public int MBNumber;
    public String problem = StatCollector.translateToLocal("gui.casingIncomplete");
    private static final int[] slotsTop = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {0, 1};

    public TileFissionReactor() {
		super("Fission Reactor", 25000000, 3);
	}

    public void updateEntity() {
    	super.updateEntity();
    	if(!this.worldObj.isRemote) {
    		product();
    		fuel();
    		energy();
    		overheat(worldObj, this.xCoord, this.yCoord, this.zCoord, 15 + 5*R, BombType.BOMB_STANDARD);
    		typeoffuelx();
    		addEnergy();
    	}
    	upgrade();
        if (flag != flag1) {
        	flag1 = flag;
        	BlockFissionReactor.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);
        }
        markDirty();
        if (this.fueltime < 0) this.fueltime = 0;
    }
    
    public void overheat(World world, double x, double y, double z, float radius, BombType type) {
    	if (this.heat > 1000000) {
    		if (NuclearCraft.nuclearMeltdowns) {
	    		if (this.MBNumber == 3) {
	    			NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord, (double)this.yCoord + R, (double)this.zCoord - R, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 4) {
	    			NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord + R, (double)this.yCoord + R, (double)this.zCoord, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 2) {
	    			NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord, (double)this.yCoord + R, (double)this.zCoord + R, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 5) {
	    			NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord - R, (double)this.yCoord + R, (double)this.zCoord, 6 + 4*R, 1000F, true);
	    		}
    		} else this.heat = 1000000;
    	}
    }
    
    public void typeoffuelx() {
    	if (this.fueltype == 1) { typeoffuel = StatCollector.translateToLocal("gui.LEU"); fuelmult = NuclearCraft.modifierLEU; }
    	else if (this.fueltype == 2) { typeoffuel = StatCollector.translateToLocal("gui.HEU"); fuelmult = NuclearCraft.modifierHEU; }
    	else if (this.fueltype == 3) { typeoffuel = StatCollector.translateToLocal("gui.LEP"); fuelmult = NuclearCraft.modifierLEP; }
    	else if (this.fueltype == 4) { typeoffuel = StatCollector.translateToLocal("gui.HEP"); fuelmult = NuclearCraft.modifierHEP; }
    	else if (this.fueltype == 5) { typeoffuel = StatCollector.translateToLocal("gui.MOX"); fuelmult = NuclearCraft.modifierMOX; }
    	else if (this.fueltype == 6) { typeoffuel = StatCollector.translateToLocal("gui.TBU"); fuelmult = NuclearCraft.modifierTBU; }
    	else if (this.fueltype == 7) { typeoffuel = StatCollector.translateToLocal("gui.LEU"); fuelmult = NuclearCraft.modifierLEU; }
    	else if (this.fueltype == 8) { typeoffuel = StatCollector.translateToLocal("gui.HEU"); fuelmult = NuclearCraft.modifierHEU; }
    	else if (this.fueltype == 9) { typeoffuel = StatCollector.translateToLocal("gui.LEP"); fuelmult = NuclearCraft.modifierLEP; }
    	else if (this.fueltype == 10) { typeoffuel = StatCollector.translateToLocal("gui.HEP"); fuelmult = NuclearCraft.modifierHEP; }
    	else if (this.fueltype == 11) { typeoffuel = StatCollector.translateToLocal("gui.MOX"); fuelmult = NuclearCraft.modifierMOX; }
    	
    	else if (this.fueltype == 12) { typeoffuel = StatCollector.translateToLocal("gui.LEU-Ox"); fuelmult = NuclearCraft.modifierLEU; }
    	else if (this.fueltype == 13) { typeoffuel = StatCollector.translateToLocal("gui.HEU-Ox"); fuelmult = NuclearCraft.modifierHEU; }
    	else if (this.fueltype == 14) { typeoffuel = StatCollector.translateToLocal("gui.LEP-Ox"); fuelmult = NuclearCraft.modifierLEP; }
    	else if (this.fueltype == 15) { typeoffuel = StatCollector.translateToLocal("gui.HEP-Ox"); fuelmult = NuclearCraft.modifierHEP; }
    	else if (this.fueltype == 16) { typeoffuel = StatCollector.translateToLocal("gui.LEU-Ox"); fuelmult = NuclearCraft.modifierLEU; }
    	else if (this.fueltype == 17) { typeoffuel = StatCollector.translateToLocal("gui.HEU-Ox"); fuelmult = NuclearCraft.modifierHEU; }
    	else if (this.fueltype == 18) { typeoffuel = StatCollector.translateToLocal("gui.LEP-Ox"); fuelmult = NuclearCraft.modifierLEP; }
    	else if (this.fueltype == 19) { typeoffuel = StatCollector.translateToLocal("gui.HEP-Ox"); fuelmult = NuclearCraft.modifierHEP; }
    	
    	else if (this.fueltype == 0) { typeoffuel = StatCollector.translateToLocal("gui.noFuel"); fuelmult = 0; }
    	else { typeoffuel = StatCollector.translateToLocal("gui.noFuel"); fuelmult = 0; }
    }

    private void energy() {
    	double energyThisTick = 0;
    	double fuelThisTick = 0;
    	double heatThisTick = 0;
    	double fakeEnergyThisTick = 0;
    	double fakeHeatThisTick = 0;
    	double numberOfCells = 0;
    	double extraCells = 0;
    	double adj1 = 0;
    	double adj2 = 0;
    	double adj3 = 0;
    	double adj4 = 0;
    	double adj5 = 0;
    	double adj6 = 0;
    	double baseRF = 0;
    	double baseFuel = 0;
    	double baseHeat = 0;

    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int x = xCoord + R * forward.offsetX;
    	int y = yCoord;
    	int z = zCoord + R * forward.offsetZ;
    	
    	if (this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if (this.worldObj.getBlock(x + X4, y + Y4, z + Z4) == NCBlocks.cellBlock) {
        					extraCells = 0;
        					if (this.worldObj.getBlock(x + X4 + 1, y + Y4, z + Z4) == NCBlocks.cellBlock) extraCells += 1;
        					if (this.worldObj.getBlock(x + X4 - 1, y + Y4, z + Z4) == NCBlocks.cellBlock) extraCells += 1;
        					if (this.worldObj.getBlock(x + X4, y + Y4 + 1, z + Z4) == NCBlocks.cellBlock) extraCells += 1;
        					if (this.worldObj.getBlock(x + X4, y + Y4 - 1, z + Z4) == NCBlocks.cellBlock) extraCells += 1;
        					if (this.worldObj.getBlock(x + X4, y + Y4, z + Z4 + 1) == NCBlocks.cellBlock) extraCells += 1;
        					if (this.worldObj.getBlock(x + X4, y + Y4, z + Z4 - 1) == NCBlocks.cellBlock) extraCells += 1;
        					
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
    	
        if (this.getStackInSlot(1) == null && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.fueltime > 0 && this.fueltype != 0 && this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	
        	flag = true;
        	off = 0;
        	
        	//LEU
        	if (this.fueltype == 1 || this.fueltype == 7) {
        		baseRF = NuclearCraft.baseRFLEU;
            	baseFuel = NuclearCraft.baseFuelLEU;
            	baseHeat = NuclearCraft.baseHeatLEU;
        	}

        	//HEU
        	if (this.fueltype == 2 || this.fueltype == 8) {
        		baseRF = NuclearCraft.baseRFHEU;
            	baseFuel = NuclearCraft.baseFuelHEU;
            	baseHeat = NuclearCraft.baseHeatHEU;
        	}

        	//LEP
        	if (this.fueltype == 3 || this.fueltype == 9) {
        		baseRF = NuclearCraft.baseRFLEP;
            	baseFuel = NuclearCraft.baseFuelLEP;
            	baseHeat = NuclearCraft.baseHeatLEP;
        	}

        	//HEP
        	if (this.fueltype == 4 || this.fueltype == 10) {
        		baseRF = NuclearCraft.baseRFHEP;
            	baseFuel = NuclearCraft.baseFuelHEP;
            	baseHeat = NuclearCraft.baseHeatHEP;
        	}

        	//MOX
        	if (this.fueltype == 5 || this.fueltype == 11) {
        		baseRF = NuclearCraft.baseRFMOX;
            	baseFuel = NuclearCraft.baseFuelMOX;
            	baseHeat = NuclearCraft.baseHeatMOX;
        	}
        	
        	//TBU
        	if (this.fueltype == 6) {
        		baseRF = NuclearCraft.baseRFTBU;
            	baseFuel = NuclearCraft.baseFuelTBU;
            	baseHeat = NuclearCraft.baseHeatTBU;
        	}
        	
        	//LEU-Ox
        	if (this.fueltype == 12 || this.fueltype == 16) {
        		baseRF = NuclearCraft.baseRFLEUOx;
            	baseFuel = NuclearCraft.baseFuelLEUOx;
            	baseHeat = NuclearCraft.baseHeatLEUOx;
        	}

        	//HEU-Ox
        	if (this.fueltype == 13 || this.fueltype == 17) {
        		baseRF = NuclearCraft.baseRFHEUOx;
            	baseFuel = NuclearCraft.baseFuelHEUOx;
            	baseHeat = NuclearCraft.baseHeatHEUOx;
        	}

        	//LEP-Ox
        	if (this.fueltype == 14 || this.fueltype == 18) {
        		baseRF = NuclearCraft.baseRFLEPOx;
            	baseFuel = NuclearCraft.baseFuelLEPOx;
            	baseHeat = NuclearCraft.baseHeatLEPOx;
        	}

        	//HEP-Ox
        	if (this.fueltype == 15 || this.fueltype == 19) {
        		baseRF = NuclearCraft.baseRFHEPOx;
            	baseFuel = NuclearCraft.baseFuelHEPOx;
            	baseHeat = NuclearCraft.baseHeatHEPOx;
        	}
        	
        	energyThisTick += baseRF*(10000*pMult + this.heat)*R*(numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6)/1000000;
        	heatThisTick += baseHeat*(numberOfCells + 3*adj1 + 6*adj2 + 10*adj3 + 15*adj4 + 21*adj5 + 28*adj6);
        	fuelThisTick += (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)*baseFuel/NuclearCraft.fissionEfficiency;
        	
        	
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.graphiteBlock) {
        					energyThisTick += (10000*pMult + this.heat)*fuelmult*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/(100000*R); heatThisTick += 10*fuelmult*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/R;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.water && (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6) > 0) {
        					energyThisTick += 1; heatThisTick += 1;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.speedBlock && (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6) > 0) fuelThisTick += (40000/(R*R))/NuclearCraft.fissionEfficiency;
        			}
        		}
        	}
        	
        } else if(this.getStackInSlot(1) == null && !worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.fueltime > 0 && this.fueltype != 0 && this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	
        	off = 1;
        	
        	//LEU
        	if (this.fueltype == 1 || this.fueltype == 7) {
        		baseRF = NuclearCraft.baseRFLEU;
            	baseHeat = NuclearCraft.baseHeatLEU;
        	}

        	//HEU
        	if (this.fueltype == 2 || this.fueltype == 8) {
        		baseRF = NuclearCraft.baseRFHEU;
            	baseHeat = NuclearCraft.baseHeatHEU;
        	}

        	//LEP
        	if (this.fueltype == 3 || this.fueltype == 9) {
        		baseRF = NuclearCraft.baseRFLEP;
            	baseHeat = NuclearCraft.baseHeatLEP;
        	}

        	//HEP
        	if (this.fueltype == 4 || this.fueltype == 10) {
        		baseRF = NuclearCraft.baseRFHEP;
            	baseHeat = NuclearCraft.baseHeatHEP;
        	}

        	//MOX
        	if (this.fueltype == 5 || this.fueltype == 11) {
        		baseRF = NuclearCraft.baseRFMOX;
            	baseHeat = NuclearCraft.baseHeatMOX;
        	}
        	
        	//TBU
        	if (this.fueltype == 6) {
        		baseRF = NuclearCraft.baseRFTBU;
            	baseHeat = NuclearCraft.baseHeatTBU;
        	}
        	
        	//LEU-Ox
        	if (this.fueltype == 12 || this.fueltype == 16) {
        		baseRF = NuclearCraft.baseRFLEUOx;
            	baseHeat = NuclearCraft.baseHeatLEUOx;
        	}

        	//HEU-Ox
        	if (this.fueltype == 13 || this.fueltype == 17) {
        		baseRF = NuclearCraft.baseRFHEUOx;
            	baseHeat = NuclearCraft.baseHeatHEUOx;
        	}

        	//LEP-Ox
        	if (this.fueltype == 14 || this.fueltype == 18) {
        		baseRF = NuclearCraft.baseRFLEPOx;
            	baseHeat = NuclearCraft.baseHeatLEPOx;
        	}

        	//HEP-Ox
        	if (this.fueltype == 15 || this.fueltype == 19) {
        		baseRF = NuclearCraft.baseRFHEPOx;
            	baseHeat = NuclearCraft.baseHeatHEPOx;
        	}
        	
        	fakeEnergyThisTick += baseRF*(10000*pMult + this.heat)*R*(numberOfCells + 2*adj1 + 3*adj2 + 4*adj3 + 5*adj4 + 6*adj5 + 7*adj6)/1000000;
    		fakeHeatThisTick += baseHeat*(numberOfCells + 3*adj1 + 6*adj2 + 10*adj3 + 15*adj4 + 21*adj5 + 28*adj6);
        	
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.graphiteBlock) {
        					fakeEnergyThisTick += (10000*pMult + this.heat)*fuelmult*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/(100000*R); fakeHeatThisTick += 10*fuelmult*(numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6)/R;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.water && (numberOfCells + adj1 + adj2 + adj3 + adj4 + adj5 + adj6) > 0) {
        					fakeEnergyThisTick += 1; fakeHeatThisTick += 1;
        				}
        			}
        		}
        	}
        	flag = false;
        } else {
        	flag = false;
        	off = 0;
        }
          	
        if (this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.coolerBlock) {
        					heatThisTick -= NuclearCraft.standardCool;
        					if (surroundOr(NCBlocks.coolerBlock, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.standardCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.waterCoolerBlock) {
        					heatThisTick -= NuclearCraft.waterCool;
        					if (surroundOr(NCBlocks.reactorBlock, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.waterCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.cryotheumCoolerBlock) {
        					heatThisTick -= NuclearCraft.cryotheumCool;
        					if (surroundAnd(Blocks.air, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.cryotheumCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.redstoneCoolerBlock) {
        					heatThisTick -= NuclearCraft.redstoneCool;
        					if (surroundOr(NCBlocks.cellBlock, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.redstoneCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.enderiumCoolerBlock) {
        					heatThisTick -= NuclearCraft.enderiumCool;
        					if (surroundOr(NCBlocks.graphiteBlock, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.enderiumCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.glowstoneCoolerBlock) {
        					heatThisTick -= NuclearCraft.glowstoneCool;
        					if (surroundAnd(NCBlocks.graphiteBlock, x + X4, y + Y4, z + Z4)) heatThisTick -= 3*NuclearCraft.glowstoneCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.heliumCoolerBlock) {
        					heatThisTick -= NuclearCraft.heliumCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.coolantCoolerBlock) {
        					heatThisTick -= NuclearCraft.coolantCool;
        					if (surroundOr(Blocks.water, x + X4, y + Y4, z + Z4)) heatThisTick -= NuclearCraft.coolantCool;
        				}
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.water) heatThisTick -= 1;
        			}
        		}
        	}
        	if (R == rawR) heatThisTick -= NuclearCraft.baseHeatTBU;
        }
        
        H = (int) (heatThisTick + fakeHeatThisTick);
        if (this.heat + (int) heatThisTick >= 0) this.heat += (int) heatThisTick; else this.heat = 0;
        
        E = (int) (energyThisTick + fakeEnergyThisTick);
        this.storage.receiveEnergy((int) energyThisTick, false);
        
        this.fueltime -= fuelThisTick;
        
        if (this.fueltime < 0) this.fueltime = 0;	
        if (this.fueltime == 0) E = 0;
	}
    
    public boolean find(Block block, int x, int y, int z) {
    	return this.worldObj.getBlock(x, y, z) == block;
    }
    
    public boolean surroundOr(Block block, int x, int y, int z) {
    	return (find(block, x + 1, y, z) || find(block, x - 1, y, z) || find(block, x, y + 1, z) || find(block, x, y - 1, z) || find(block, x, y, z + 1) || find(block, x, y, z - 1));
    }
    
    public boolean surroundAnd(Block block, int x, int y, int z) {
    	return (find(block, x + 1, y, z) && find(block, x - 1, y, z) && find(block, x, y + 1, z) && find(block, x, y - 1, z) && find(block, x, y, z + 1) && find(block, x, y, z - 1));
    }
    
	public boolean multiblock(World world, int x, int y, int z) {
		Block block = this.blockType;
		@SuppressWarnings("unused")
		BlockFissionReactor reactorGraphite = (BlockFissionReactor) block;
		if (checkReactor()) {
			return true;
		} return false;
	}

	public boolean multiblockstring() {
    	if (multiblock(getWorldObj(), this.xCoord, this.yCoord, this.zCoord)) {
    		return true;
    	} return false;
	}

	private void fuel() {
    	ItemStack stack = this.getStackInSlot(0);
    	ItemStack pstack = this.getStackInSlot(1);

        if (stack != null && pstack == null && isFuel(stack) && this.fueltime == 0 && this.fueltype == 0) {
            this.addFuel(fuelValue(stack));
            
            this.fueltype = TileFissionReactor.setfueltype(stack);
            
            --this.slots[0].stackSize;

            if (this.slots[0].stackSize <= 0) {
                this.slots[0] = null;
            }
            off = 1;
        }
    }
    
    private void upgrade() {
        ItemStack stack = this.getStackInSlot(2);

        if (stack != null && isUpgrade(stack)) {
        	R = (int) (rawR + stack.stackSize);
        	D = R*2 + 1;
            SR = R-1; 
            SD = SR*2 + 1;
        	
        	if (R != rawR) {
        	}

            if (this.slots[2].stackSize <= 0) {
            	R = rawR;
            	D = R*2 + 1;
                SR = R-1; 
                SD = SR*2 + 1;
                this.slots[2] = null;
            }
        }
        else {
	        R = rawR;
	        D = R*2 + 1;
	        SR = R-1; 
	        SD = SR*2 + 1;
        }
    }
    
    private void product() {
        if (this.slots[1] == null && this.fueltime <= 0 && this.fueltype != 0) {    
        	if (this.fueltype == 1) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 22); this.fueltype = 0;
        	} else if (this.fueltype == 2) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 23); this.fueltype = 0;
        	} else if (this.fueltype == 3) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 24); this.fueltype = 0;
        	} else if (this.fueltype == 4) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 25); this.fueltype = 0;
        	} else if (this.fueltype == 5) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 26); this.fueltype = 0;
        	} else if (this.fueltype == 6) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 27); this.fueltype = 0;
        	} else if (this.fueltype == 7) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 28); this.fueltype = 0;
        	} else if (this.fueltype == 8) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 29); this.fueltype = 0;
        	} else if (this.fueltype == 9) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 30); this.fueltype = 0;
        	} else if (this.fueltype == 10) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 31); this.fueltype = 0;
        	} else if (this.fueltype == 11) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 32); this.fueltype = 0;
        	} else if (this.fueltype == 12) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 67); this.fueltype = 0;
        	} else if (this.fueltype == 13) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 68); this.fueltype = 0;
        	} else if (this.fueltype == 14) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 69); this.fueltype = 0;
        	} else if (this.fueltype == 15) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 70); this.fueltype = 0;
        	} else if (this.fueltype == 16) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 71); this.fueltype = 0;
        	} else if (this.fueltype == 17) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 72); this.fueltype = 0;
        	} else if (this.fueltype == 18) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 73); this.fueltype = 0;
        	} else if (this.fueltype == 19) {
        		this.slots[1] = new ItemStack(NCItems.fuel, 1, 74); this.fueltype = 0;
        	} else {
        	}
        	this.fueltime = 0;
        }
    }
    
    public static boolean isFuel(ItemStack stack) {
        return fuelValue(stack) > 0 && setfueltype(stack) != 0;
    }
    
    public static boolean isUpgrade(ItemStack stack) {
        return stack.getItem() == NCItems.upgrade;
    }
    
    public static int fuelValue(ItemStack stack) {
    	if (stack == null) return 0; else {
    		Item item = stack.getItem();
        	
        	if(item == new ItemStack (NCItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) return 10000000;
        	else if(item == new ItemStack (NCItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) return 10000000;
        	return 0;
        }
	}
    
    public static int setfueltype(ItemStack stack) {
    	if (stack == null) return 0; else {
    		Item item = stack.getItem();
        	
    		if(item == new ItemStack (NCItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) return 1;
    		else if(item == new ItemStack (NCItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) return 2;
    		else if(item == new ItemStack (NCItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) return 3;
    		else if(item == new ItemStack (NCItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) return 4;
    		else if(item == new ItemStack (NCItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) return 5;
    		else if(item == new ItemStack (NCItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) return 6;
    		else if(item == new ItemStack (NCItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) return 7;
     		else if(item == new ItemStack (NCItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) return 8;
     		else if(item == new ItemStack (NCItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) return 9;
     		else if(item == new ItemStack (NCItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) return 10;
     		else if(item == new ItemStack (NCItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) return 11;
     		else if(item == new ItemStack (NCItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) return 12;
     		else if(item == new ItemStack (NCItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) return 13;
     		else if(item == new ItemStack (NCItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) return 14;
     		else if(item == new ItemStack (NCItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) return 15;
     		else if(item == new ItemStack (NCItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) return 16;
     		else if(item == new ItemStack (NCItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) return 17;
     		else if(item == new ItemStack (NCItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) return 18;
     		else if(item == new ItemStack (NCItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) return 19;
    		return 0;
    	}
    }

    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        if (nbt.hasKey("storage")) this.storage.readFromNBT(nbt.getCompoundTag("storage"));
        this.typeoffuel = nbt.getString("Typeoffuel");
        this.problem = nbt.getString("problem");
        this.fueltime = nbt.getInteger("Fueltime");
        this.fueltype = nbt.getInteger("Fueltype");
        this.rawR = nbt.getInteger("rawR");
        this.R = nbt.getInteger("R");
        this.SR = nbt.getInteger("SR");
        this.D = nbt.getInteger("D");
        this.SD = nbt.getInteger("SD");
        this.E = nbt.getInteger("E");
        this.H = nbt.getInteger("H");
        this.off = nbt.getInteger("off");
        this.fuelmult = nbt.getInteger("Fuelmult");
        this.MBNumber = nbt.getInteger("MBN");
        this.heat = nbt.getInteger("Heat");
    }

    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
        nbt.setInteger("Fueltime", this.fueltime);
        nbt.setInteger("Fueltype", this.fueltype);
        nbt.setInteger("rawR", this.rawR);
        nbt.setInteger("R", this.R);
        nbt.setInteger("SR", this.SR);
        nbt.setInteger("D", this.D);
        nbt.setInteger("SD", this.SD);
        nbt.setInteger("E", this.E);
        nbt.setInteger("H", this.H);
        nbt.setInteger("off", this.off);
        nbt.setInteger("Fuelmult", this.fuelmult);
        nbt.setInteger("MBN", this.MBNumber);
        nbt.setInteger("Heat", this.heat);
        nbt.setString("Typeoffuel", this.typeoffuel);
        nbt.setString("problem", this.problem);
    }

    public void addHeat(int add) {
        this.heat += add;
    }
    
    public void addFuel(int add) {
        this.fueltime += add;
    }

    public void removeHeat(int remove) {
        this.heat -= remove;
    }
    
    public void removeFuel(int remove) {
        this.fueltime -= remove;
    }

    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return isFuel(stack);
        else return false;
    }

    public int[] getAccessibleSlotsFromSide(int slot) {
    	return slot == 0 ? slotsSides : slotsTop;
    }

    public boolean canExtractItem(int slot, ItemStack stack, int slots) {
        return slot == 1;
    }
    
    private boolean checkReactor() {
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int x = xCoord + R * forward.offsetX;
    	int y = yCoord;
    	int z = zCoord + R * forward.offsetZ;

    	for (int Z4 = -R; Z4 <= R; Z4++) {
    		for (int X4 = -R; X4 <= R; X4++) {
    			for (int Y4 = 0; Y4 <= 2*R; Y4++) {
    				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.fissionReactorGraphiteIdle || this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NCBlocks.fissionReactorGraphiteActive) {
    					if (x + X4 == this.xCoord && y + Y4 == this.yCoord && z + Z4 == this.zCoord) {} else {
    						this.problem = StatCollector.translateToLocal("gui.multipleControllers");
    						return false;
    					}
    				}
    			}
    		}
    	}
    	  
    	for (int Z1 = -SR; Z1 <= SR; Z1++) {
    		for (int X1 = -SR; X1 <= SR; X1++) {
    			if(this.worldObj.getBlock(x + X1, y, z + Z1)!=NCBlocks.reactorBlock) {
    				this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    				return false;
    			}
    			if(this.worldObj.getBlock(x + X1, y+D-1, z + Z1)!=NCBlocks.reactorBlock) {
    				this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    				return false;
    			}
    		}
    	}
    	for (int Y2 = 1; Y2 <= SD; Y2++) {
    		for (int XZ = -SR; XZ <= SR; XZ++) {
    			if(this.worldObj.getBlock(x-R, y+Y2, z+XZ)!=NCBlocks.reactorBlock) {
    				this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    				return false;
    			}
    			if(this.worldObj.getBlock(x+R, y+Y2, z+XZ)!=NCBlocks.reactorBlock) {
    				this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    				return false;
    			}
    			if(x!=R&&x!=-R) {
    				if(this.worldObj.getBlock(x+XZ, y+Y2, z+R)!=NCBlocks.reactorBlock) {
    					this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    					return false;
    				}
    				if(this.worldObj.getBlock(x+XZ, y+Y2, z-R)!=NCBlocks.reactorBlock) {
    					this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    					return false;
    				}
    				/*if(this.worldObj.getBlock(x, y+Y2, z)!=NCBlocks.cellBlock) {
    					this.problem = StatCollector.translateToLocal("gui.fuelRodIncomplete");
    					return false;
    				}*/
    			}
    		}
    	}
    	for (int Z3 = -SR; Z3 <= SR; Z3++) {
    		for (int X3 = -SR; X3 <= SR; X3++) {
    			for (int Y3 = 1; Y3 <= SD; Y3++) {
    				if(this.worldObj.getBlock(x + X3, y + Y3, z + Z3)==NCBlocks.reactorBlock) {
    					this.problem = StatCollector.translateToLocal("gui.casingInInterior");
    					return false;
    				}
    			}
    		}
    	}
    	this.MBNumber = this.getBlockMetadata();
    	return true;
    }
}
