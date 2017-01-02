package nc.tile.generator;

import java.util.Random;

import mekanism.api.gas.Gas;
import mekanism.api.gas.GasRegistry;
import mekanism.api.gas.GasStack;
import mekanism.api.gas.GasTank;
import mekanism.api.gas.IGasHandler;
import mekanism.api.gas.ITubeConnection;
import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.block.generator.BlockFusionReactor;
import nc.handler.BombType;
import nc.handler.EntityBomb;
import nc.handler.NCExplosion;
import nc.item.NCItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

public class TileFusionReactor extends TileGeneratorInventory implements IEnergyReceiver, IGasHandler, ITubeConnection {
	
	public GasTank gasTank;
	public GasTank gasTank2;
	
	public int EShown;
	public int size = 1;
	public String problem = StatCollector.translateToLocal("gui.connectorsIncomplete");
    public static double pMult = NuclearCraft.fusionRF;
    private Random rand = new Random();
    
    private int checkCount = 0;
    private int soundCount = 0;
	
    public int HLevel;
	public int DLevel;
	public int TLevel;
	public int HeLevel;
	public int BLevel;
	public int Li6Level;
	public int Li7Level;
	public int HLevel2;
	public int DLevel2;
	public int TLevel2;
	public int HeLevel2;
	public int BLevel2;
	public int Li6Level2;
	public int Li7Level2;
	
	public double HOut;
	public double DOut;
	public double TOut;
	public double HE3Out;
	public double HE4Out;
	public double nOut;
	
	public static int Max = 6400000;
	
	public int complete;
	
	public static double requiredHH = (100*NuclearCraft.baseFuelHH)/NuclearCraft.fusionEfficiency;
	public static double requiredHD = (100*NuclearCraft.baseFuelHD)/NuclearCraft.fusionEfficiency;
	public static double requiredHT = (100*NuclearCraft.baseFuelHT)/NuclearCraft.fusionEfficiency;
	public static double requiredHHe = (100*NuclearCraft.baseFuelHHe)/NuclearCraft.fusionEfficiency;
	public static double requiredHB = (100*NuclearCraft.baseFuelHB)/NuclearCraft.fusionEfficiency;
	public static double requiredHLi6 = (100*NuclearCraft.baseFuelHLi6)/NuclearCraft.fusionEfficiency;
	public static double requiredHLi7 = (100*NuclearCraft.baseFuelHLi7)/NuclearCraft.fusionEfficiency;

	public static double requiredDD = (100*NuclearCraft.baseFuelDD)/NuclearCraft.fusionEfficiency;
	public static double requiredDT = (100*NuclearCraft.baseFuelDT)/NuclearCraft.fusionEfficiency;
	public static double requiredDHe = (100*NuclearCraft.baseFuelDHe)/NuclearCraft.fusionEfficiency;
	public static double requiredDB = (100*NuclearCraft.baseFuelDB)/NuclearCraft.fusionEfficiency;
	public static double requiredDLi6 = (100*NuclearCraft.baseFuelDLi6)/NuclearCraft.fusionEfficiency;
	public static double requiredDLi7 = (100*NuclearCraft.baseFuelDLi7)/NuclearCraft.fusionEfficiency;
	
	public static double requiredTT = (100*NuclearCraft.baseFuelTT)/NuclearCraft.fusionEfficiency;
	public static double requiredTHe = (100*NuclearCraft.baseFuelTHe)/NuclearCraft.fusionEfficiency;
	public static double requiredTB = (100*NuclearCraft.baseFuelTB)/NuclearCraft.fusionEfficiency;
	public static double requiredTLi6 = (100*NuclearCraft.baseFuelTLi6)/NuclearCraft.fusionEfficiency;
	public static double requiredTLi7 = (100*NuclearCraft.baseFuelTLi7)/NuclearCraft.fusionEfficiency;

	public static double requiredHeHe = (100*NuclearCraft.baseFuelHeHe)/NuclearCraft.fusionEfficiency;
	public static double requiredHeB = (100*NuclearCraft.baseFuelHeB)/NuclearCraft.fusionEfficiency;
	public static double requiredHeLi6 = (100*NuclearCraft.baseFuelHeLi6)/NuclearCraft.fusionEfficiency;
	public static double requiredHeLi7 = (100*NuclearCraft.baseFuelHeLi7)/NuclearCraft.fusionEfficiency;

	public static double requiredBB = (100*NuclearCraft.baseFuelBB)/NuclearCraft.fusionEfficiency;
	public static double requiredBLi6 = (100*NuclearCraft.baseFuelBLi6)/NuclearCraft.fusionEfficiency;
	public static double requiredBLi7 = (100*NuclearCraft.baseFuelBLi7)/NuclearCraft.fusionEfficiency;

	public static double requiredLi6Li6 = (100*NuclearCraft.baseFuelLi6Li6)/NuclearCraft.fusionEfficiency;
	public static double requiredLi6Li7 = (100*NuclearCraft.baseFuelLi6Li7)/NuclearCraft.fusionEfficiency;

	public static double requiredLi7Li7 = (100*NuclearCraft.baseFuelLi7Li7)/NuclearCraft.fusionEfficiency;

	public static double maxHeat = 20000;
	public double efficiency = 0;
	public double heatVar = 5000;
	public double heat;
	
	public int lastE;
	public int E;
	
	public final int[] input = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	public final int[] output = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
	
	public TileFusionReactor() {
		super("fusionReactor", 10000000, 9);
		this.gasTank = new GasTank(3200);
		this.gasTank2 = new GasTank(3200);
	}
	
	public boolean ppp(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.blockFusionPlasma;
	}

	public void updateEntity() {
		super.updateEntity();
		if (checkCount >= NuclearCraft.fusionUpdateRate) {
			setSize();
			checkCount = 0;
		} else checkCount ++;
		if(!this.worldObj.isRemote) {
		    energy();
		   	overheat(worldObj, this.xCoord, this.yCoord, this.zCoord, 10 + 4*size, BombType.BOMB_STANDARD);
		   	addEnergy();
		   	gasFuel();
		   	fuel1();
		   	fuel2();
		   	outputs();
		   	plasma(worldObj, xCoord, yCoord, zCoord);
		   	efficiency();
		   	for (int i = 1; i < 100; i++) {
		    	if ((heat < 8 || HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0 || complete == 0) && storage.getEnergyStored() >= 10000) {
		    		this.storage.receiveEnergy(-10000, false);
		    		heat = heat+0.0005*(NuclearCraft.fusionHeat/100);
		    	}
	    	}
		    if (heat < 0) heat = 0;
	    }
	    if (flag != flag1) { flag1 = flag; BlockFusionReactor.updateBlockState(worldObj, xCoord, yCoord, zCoord); }
	    if (this.worldObj.getBlock(xCoord, yCoord, zCoord) == NCBlocks.fusionReactor && (ppp(xCoord + 1, yCoord, zCoord) || ppp(xCoord - 1, yCoord, zCoord) || ppp(xCoord, yCoord + 1, zCoord) || ppp(xCoord, yCoord - 1, zCoord) || ppp(xCoord, yCoord, zCoord + 1) || ppp(xCoord, yCoord, zCoord - 1))) {
			if (rand.nextFloat() > 0.99875) NCExplosion.createExplosion(new EntityBomb(worldObj).setType(BombType.BOMB_STANDARD), worldObj, (double)this.xCoord, (double)this.yCoord, (double)this.zCoord, NuclearCraft.fusionMeltdowns ? 0.125F*NuclearCraft.explosionRadius : 0F, 40F, true);
		}
	    
	    if (soundCount >= 67) {
			if (flag && complete == 1 && !givenRedstone() && NuclearCraft.fusionSounds) {
				worldObj.playSoundEffect(xCoord, yCoord + 1, zCoord, "nc:shield5", 1.25F, 1F);
				for (int r = 0; r <= (size - 1)/2; r++) {
					worldObj.playSoundEffect(xCoord - size - 2 + 2*r*(2*size + 5)/size, yCoord + 1, zCoord + size + 2, "nc:shield5", 0.7F, 1F);
					worldObj.playSoundEffect(xCoord + size + 2 - 2*r*(2*size + 5)/size, yCoord + 1, zCoord - size - 2, "nc:shield5", 0.7F, 1F);
					worldObj.playSoundEffect(xCoord + size + 2, yCoord + 1, zCoord + size + 2 - 2*r*(2*size + 5)/size, "nc:shield5", 0.7F, 1F);
					worldObj.playSoundEffect(xCoord - size - 2, yCoord + 1, zCoord - size - 2 + 2*r*(2*size + 5)/size, "nc:shield5", 0.7F, 1F);
				}
			}
			soundCount = 0;
		} else soundCount ++;
	    
	    markDirty();
	    }
	    
	public void overheat(World world, double x, double y, double z, float radius, BombType type) {
		if (this.heat > 20000) {
	    	if (NuclearCraft.fusionMeltdowns) NCExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord, (double)this.yCoord + 1, (double)this.zCoord, 10 + 4*size, 20F, true);
	    	else this.heat = 20000;
	    }
	}
	    
	public void efficiency() {
		if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0 || complete == 0) efficiency = 0;
		else if (heat >= 8) {
	    	//efficiency = 100*Math.exp(-Math.pow(heatVar-Math.log(heat), 2)/2)/(heat*Math.exp(0.5-heatVar));
	    	efficiency = 742*(Math.exp(-heat/heatVar)+Math.tanh(heat/heatVar)-1);
	   	} else efficiency = 0;
	}
	    
	public void outputs() {
		if (HOut >= 100000) {
	    	if (this.slots[3] == null) { this.slots[3] = new ItemStack(NCItems.fuel, 1, 36); HOut -= 100000; }
	    	else if (this.slots[3].stackSize < 64) { this.slots[3].stackSize ++; HOut -= 100000; }
	    }
	    if (DOut >= 100000) {
	    	if (this.slots[4] == null) { this.slots[4] = new ItemStack(NCItems.fuel, 1, 37); DOut -= 100000; }
	   		else if (this.slots[4].stackSize < 64) { this.slots[4].stackSize ++; DOut -= 100000; }
	   	}
	    if (TOut >= 100000) {
	    	if (this.slots[5] == null) { this.slots[5] = new ItemStack(NCItems.fuel, 1, 38); TOut -= 100000; }
	    	else if (this.slots[5].stackSize < 64) { this.slots[5].stackSize ++; TOut -= 100000; }
	    }
	   	if (HE3Out >= 100000) {
	   		if (this.slots[6] == null) { this.slots[6] = new ItemStack(NCItems.fuel, 1, 39); HE3Out -= 100000; }
	   		else if (this.slots[6].stackSize < 64) { this.slots[6].stackSize ++; HE3Out -= 100000; }
	   	}
	   	if (HE4Out >= 100000) {
	   		if (this.slots[7] == null) { this.slots[7] = new ItemStack(NCItems.fuel, 1, 40); HE4Out -= 100000; }
	   		else if (this.slots[7].stackSize < 64) { this.slots[7].stackSize ++; HE4Out -= 100000; }
	   	}
	   	if (nOut >= 100000 && this.slots[2] != null && this.slots[2].stackSize > 0 && this.slots[2].getItem() == new ItemStack (NCItems.fuel, 1, 48).getItem() && this.slots[2].getItem().getDamage(this.slots[2]) == 48) {
	   		if (this.slots[8] == null) { this.slots[8] = new ItemStack(NCItems.fuel, 1, 47); nOut -= 100000; this.slots[2].stackSize --; if (this.slots[2].stackSize < 1) this.slots[2] = null;}
	   		else if (this.slots[8].stackSize < 64) { this.slots[8].stackSize ++; nOut -= 100000; this.slots[2].stackSize --; if (this.slots[2].stackSize < 1) this.slots[2] = null;}
	   	}
	    	
	    if (HOut > Max) HOut = Max;
	   	if (DOut > Max) DOut = Max;
	   	if (TOut > Max) TOut = Max;
	   	if (HE3Out > Max) HE3Out = Max;
	   	if (HE4Out > Max) HE4Out = Max;
	   	if (nOut > Max) nOut = Max;
	}
	
	public boolean pp(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.blockFusionPlasma;
	}
	
	public void p(World world, int x, int y, int z) {
		world.setBlock(x, y, z, NCBlocks.blockFusionPlasma);
	}
	
	public void aa(World world, int x, int y, int z) {
		world.setBlock(x, y, z, Blocks.air);
	}
	
	public void plasma(World world, int x, int y, int z) {
		if (!givenRedstone() && heat >= 8 && complete == 1) {
			for (int r = -size - 2; r <= size + 2; r++) {
				if (!pp(x + r, y + 1, z + size + 2)) p(world, x + r, y + 1, z + size + 2);
				if (!pp(x + r, y + 1, z - size - 2)) p(world, x + r, y + 1, z - size - 2);
				if (!pp(x + size + 2, y + 1, z + r)) p(world, x + size + 2, y + 1, z + r);
				if (!pp(x - size - 2, y + 1, z + r)) p(world, x - size - 2, y + 1, z + r);
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!givenRedstone() && heat < 8 && complete == 1) {
				if (pp(x + r, y + 1, z + size + 2)) aa(world, x + r, y + 1, z + size + 2);
				if (pp(x + r, y + 1, z - size - 2)) aa(world, x + r, y + 1, z - size - 2);
				if (pp(x + size + 2, y + 1, z + r)) aa(world, x + size + 2, y + 1, z + r);
				if (pp(x - size - 2, y + 1, z + r)) aa(world, x - size - 2, y + 1, z + r);
			}
		}
	}
	
	public boolean e(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.electromagnetActive;
	}
	
	public boolean ee(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.electromagnetActive || this.worldObj.getBlock(x, y, z) == NCBlocks.electromagnetIdle;
	}
	
	public boolean a(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z).getMaterial() == Material.air || this.worldObj.getBlock(x, y, z).getMaterial() == Material.fire || this.worldObj.getBlock(x, y, z).getMaterial() == Material.snow || this.worldObj.getBlock(x, y, z) == NCBlocks.blockFusionPlasma;
	}
	
	public boolean p(int x, int y, int z) {
		return this.worldObj.getBlock(x, y, z) == NCBlocks.blockFusionPlasma;
	}
	    
	public boolean setSize() {
		int x = xCoord;
    	int y = yCoord;
    	int z = zCoord;
		int s = 1;
		for (int r = 0; r <= NuclearCraft.fusionMaxRadius; r++) {
			if (this.worldObj.getBlock(x + 2 + r, y + 1, z) == NCBlocks.fusionConnector && this.worldObj.getBlock(x - 2 - r, y + 1, z) == NCBlocks.fusionConnector && this.worldObj.getBlock(x, y + 1, z + 2 + r) == NCBlocks.fusionConnector && this.worldObj.getBlock(x, y + 1, z - 2 - r) == NCBlocks.fusionConnector) {
				s ++;
			} else break;
		}
		size = s;
		for (int r = -size - 1; r <= size + 1; r++) {
			if (!(ee(x + r, y + 1, z + size + 1) && ee(x + r, y + 1, z - size - 1) && ee(x + size + 1, y + 1, z + r) && ee(x - size - 1, y + 1, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.ringIncomplete");
				return false;
			}
		}
		for (int r = -size - 3; r <= size + 3; r++) {
			if (!(ee(x + r, y + 1, z + size + 3) && ee(x + r, y + 1, z - size - 3) && ee(x + size + 3, y + 1, z + r) && ee(x - size - 3, y + 1, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.ringIncomplete");
				return false;
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!(ee(x + r, y + 2, z + size + 2) && ee(x + r, y + 2, z - size - 2) && ee(x + size + 2, y + 2, z + r) && ee(x - size - 2, y + 2, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.ringIncomplete");
				return false;
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!(ee(x + r, y, z + size + 2) && ee(x + r, y, z - size - 2) && ee(x + size + 2, y, z + r) && ee(x - size - 2, y, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.ringIncomplete");
				return false;
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!(a(x + r, y + 1, z + size + 2) && a(x + r, y + 1, z - size - 2) && a(x + size + 2, y + 1, z + r) && a(x - size - 2, y + 1, z + r))) {
				complete = 0;
				StatCollector.translateToLocal("gui.ringBlock");
				return false;
			}
		}
		for (int r = -size - 1; r <= size + 1; r++) {
			if (!(e(x + r, y + 1, z + size + 1) && e(x + r, y + 1, z - size - 1) && e(x + size + 1, y + 1, z + r) && e(x - size - 1, y + 1, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.powerIssue");
				return false;
			}
		}
		for (int r = -size - 3; r <= size + 3; r++) {
			if (!(e(x + r, y + 1, z + size + 3) && e(x + r, y + 1, z - size - 3) && e(x + size + 3, y + 1, z + r) && e(x - size - 3, y + 1, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.powerIssue");
				return false;
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!(e(x + r, y + 2, z + size + 2) && e(x + r, y + 2, z - size - 2) && e(x + size + 2, y + 2, z + r) && e(x - size - 2, y + 2, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.powerIssue");
				return false;
			}
		}
		for (int r = -size - 2; r <= size + 2; r++) {
			if (!(e(x + r, y, z + size + 2) && e(x + r, y, z - size - 2) && e(x + size + 2, y, z + r) && e(x - size - 2, y, z + r))) {
				complete = 0;
				problem = StatCollector.translateToLocal("gui.powerIssue");
				return false;
			}
		}
		complete = 1;
		problem = StatCollector.translateToLocal("gui.incorrectStructure");
		return true;
	}
	
	public boolean completeStringPre(World world, int x, int y, int z) {
		Block block = this.blockType;
		@SuppressWarnings("unused")
		BlockFusionReactor reactor = (BlockFusionReactor) block;
		if (setSize()) {
			return true;
		} return false;
	}
	
	public boolean completeString() {
    	if (completeStringPre(getWorldObj(), this.xCoord, this.yCoord, this.zCoord)) {
    		return true;
    	} return false;
	}
	
	public boolean givenRedstone() {
		return (
				worldObj.isBlockIndirectlyGettingPowered(xCoord-1, yCoord, zCoord-1) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord-1, yCoord, zCoord) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord-1, yCoord, zCoord+1) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord-1) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord, yCoord, zCoord+1) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord+1, yCoord, zCoord-1) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord+1, yCoord, zCoord) ||
				worldObj.isBlockIndirectlyGettingPowered(xCoord+1, yCoord, zCoord+1)
				);
	}
	    
	private void energy() {
	    double newE = 0;

	    if (!givenRedstone() && heat >= 8 && complete == 1) {
	   	lastE = storage.getEnergyStored();
	   	
	   	if (this.HLevel > 0 && this.HLevel2 > 0) {
		   	if (this.HLevel >= size*TileFusionReactor.requiredHH && this.HLevel2 >= size*TileFusionReactor.requiredHH) {
		   		this.heatVar = NuclearCraft.heatHH; newE += efficiency*pMult*size*NuclearCraft.baseRFHH/100; this.HLevel -= size*TileFusionReactor.requiredHH; this.HLevel2 -= size*TileFusionReactor.requiredHH; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.DOut += size*TileFusionReactor.requiredHH;
		   	}
		   	if (this.HLevel < size*TileFusionReactor.requiredHH) {this.HLevel = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHH) {this.HLevel2 = 0;}
	   	}
	    	
	   	else if (this.HLevel > 0 && this.DLevel2 > 0) {
		   	if (this.HLevel >= size*TileFusionReactor.requiredHD && this.DLevel2 >= size*TileFusionReactor.requiredHD) {
		   		this.heatVar = NuclearCraft.heatHD; newE += efficiency*pMult*size*NuclearCraft.baseRFHD/100; this.HLevel -= size*TileFusionReactor.requiredHD; this.DLevel2 -= size*TileFusionReactor.requiredHD; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE3Out += size*TileFusionReactor.requiredHD;
		    }
		   	if (this.HLevel < size*TileFusionReactor.requiredHD) {this.HLevel = 0;}
		   	if (this.DLevel2 < size*TileFusionReactor.requiredHD) {this.DLevel2 = 0;}
	   	}
	    	
	   	else if (this.DLevel > 0 && this.HLevel2 > 0) {
	    	if (this.DLevel >= size*TileFusionReactor.requiredHD && this.HLevel2 >= size*TileFusionReactor.requiredHD) {
	    		this.heatVar = NuclearCraft.heatHD; newE += efficiency*pMult*size*NuclearCraft.baseRFHD/100; this.DLevel -= size*TileFusionReactor.requiredHD; this.HLevel2 -= size*TileFusionReactor.requiredHD; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
	    		this.HE3Out += size*TileFusionReactor.requiredHD;
	    	}
	    	if (this.DLevel < size*TileFusionReactor.requiredHD) {this.DLevel = 0;}
	    	if (this.HLevel2 < size*TileFusionReactor.requiredHD) {this.HLevel2 = 0;}
	   	}
		    
	   	else if (this.HLevel > 0 && this.TLevel2 > 0) {
	    	if (this.HLevel >= size*TileFusionReactor.requiredHT && this.TLevel2 >= size*TileFusionReactor.requiredHT) {
	    		this.heatVar = NuclearCraft.heatHT; newE += efficiency*pMult*size*NuclearCraft.baseRFHT/100; this.HLevel -= size*TileFusionReactor.requiredHT; this.TLevel2 -= size*TileFusionReactor.requiredHT; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
	    		this.HE3Out += size*TileFusionReactor.requiredHT; this.nOut += size*TileFusionReactor.requiredHT/8;
	    	}
	    	if (this.HLevel < size*TileFusionReactor.requiredHT) {this.HLevel = 0;}
	    	if (this.TLevel2 < size*TileFusionReactor.requiredHT) {this.TLevel2 = 0;}
	   	}
		    
	    else if (this.TLevel > 0 && this.HLevel2 > 0) {
		    if (this.TLevel >= size*TileFusionReactor.requiredHT && this.HLevel2 >= size*TileFusionReactor.requiredHT) {
		    	this.heatVar = NuclearCraft.heatHT; newE += efficiency*pMult*size*NuclearCraft.baseRFHT/100; this.TLevel -= size*TileFusionReactor.requiredHT; this.HLevel2 -= size*TileFusionReactor.requiredHT; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		    	this.HE3Out += size*TileFusionReactor.requiredHT; this.nOut += size*TileFusionReactor.requiredHT/8;
		    }
		    if (this.TLevel < size*TileFusionReactor.requiredHT) {this.TLevel = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHT) {this.HLevel2 = 0;}
	    }
	    	
	    else if (this.HLevel > 0 && this.HeLevel2 > 0) {
	    	if (this.HLevel >= size*TileFusionReactor.requiredHHe && this.HeLevel2 >= size*TileFusionReactor.requiredHHe) {
		   		this.heatVar = NuclearCraft.heatHHe; newE += efficiency*pMult*size*NuclearCraft.baseRFHHe/100; this.HLevel -= size*TileFusionReactor.requiredHHe; this.HeLevel2 -= size*TileFusionReactor.requiredHHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHHe;
		   	}
		   	if (this.HLevel < size*TileFusionReactor.requiredHHe) {this.HLevel = 0;}
		   	if (this.HeLevel2 < size*TileFusionReactor.requiredHHe) {this.HeLevel2 = 0;}
	    }
	    	
	    else if (this.HeLevel > 0 && this.HLevel2 > 0) {
		   	if (this.HeLevel >= size*TileFusionReactor.requiredHHe && this.HLevel2 >= size*TileFusionReactor.requiredHHe) {
		   		this.heatVar = NuclearCraft.heatHHe; newE += efficiency*pMult*size*NuclearCraft.baseRFHHe/100; this.HeLevel -= size*TileFusionReactor.requiredHHe; this.HLevel2 -= size*TileFusionReactor.requiredHHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHHe;
		    }
		   	if (this.HeLevel < size*TileFusionReactor.requiredHHe) {this.HeLevel = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHHe) {this.HLevel2 = 0;}
	    }
	    	
	    else if (this.HLevel > 0 && this.BLevel2 > 0) {
		   	if (this.HLevel >= size*TileFusionReactor.requiredHB && this.BLevel2 >= size*TileFusionReactor.requiredHB) {
		   		this.heatVar = NuclearCraft.heatHB; newE += efficiency*pMult*size*NuclearCraft.baseRFHB/100; this.HLevel -= size*TileFusionReactor.requiredHB; this.BLevel2 -= size*TileFusionReactor.requiredHB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHB*3;
		   	}
		   	if (this.HLevel < size*TileFusionReactor.requiredHB) {this.HLevel = 0;}
		   	if (this.BLevel2 < size*TileFusionReactor.requiredHB) {this.BLevel2 = 0;}
	   	}
	    	
	    else if (this.BLevel > 0 && this.HLevel2 > 0) {
		   	if (this.BLevel >= size*TileFusionReactor.requiredHB && this.HLevel2 >= size*TileFusionReactor.requiredHB) {
		   		this.heatVar = NuclearCraft.heatHB; newE += efficiency*pMult*size*NuclearCraft.baseRFHB/100; this.BLevel -= size*TileFusionReactor.requiredHB; this.HLevel2 -= size*TileFusionReactor.requiredHB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHB*3;
		   	}
		   	if (this.BLevel < size*TileFusionReactor.requiredHB) {this.BLevel = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHB) {this.HLevel2 = 0;}
	    }
	    	
	    else if (this.HLevel > 0 && this.Li6Level2 > 0) {
		   	if (this.HLevel >= size*TileFusionReactor.requiredHLi6 && this.Li6Level2 >= size*TileFusionReactor.requiredHLi6) {
		   		this.heatVar = NuclearCraft.heatHLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFHLi6/100; this.HLevel -= size*TileFusionReactor.requiredHLi6; this.Li6Level2 -= size*TileFusionReactor.requiredHLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHLi6; this.TOut += size*TileFusionReactor.requiredHLi6;
		   	}
		   	if (this.HLevel < size*TileFusionReactor.requiredHLi6) {this.HLevel = 0;}
		   	if (this.Li6Level2 < size*TileFusionReactor.requiredHLi6) {this.Li6Level2 = 0;}
	   	}
	    	
	    else if (this.Li6Level > 0 && this.HLevel2 > 0) {
		   	if (this.Li6Level >= size*TileFusionReactor.requiredHLi6 && this.HLevel2 >= size*TileFusionReactor.requiredHLi6) {
		   		this.heatVar = NuclearCraft.heatHLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFHLi6/100; this.Li6Level -= size*TileFusionReactor.requiredHLi6; this.HLevel2 -= size*TileFusionReactor.requiredHLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHLi6; this.TOut += size*TileFusionReactor.requiredHLi6;
		   	}
		   	if (this.Li6Level < size*TileFusionReactor.requiredHLi6) {this.Li6Level = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHLi6) {this.HLevel2 = 0;}
	    }
	    	
	    else if (this.HLevel > 0 && this.Li7Level2 > 0) {
		   	if (this.HLevel >= size*TileFusionReactor.requiredHLi7 && this.Li7Level2 >= size*TileFusionReactor.requiredHLi7) {
		   		this.heatVar = NuclearCraft.heatHLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFHLi7/100; this.HLevel -= size*TileFusionReactor.requiredHLi7; this.Li7Level2 -= size*TileFusionReactor.requiredHLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHLi7*2;
		   	}
		   	if (this.HLevel < size*TileFusionReactor.requiredHLi7) {this.HLevel = 0;}
		   	if (this.Li7Level2 < size*TileFusionReactor.requiredHLi7) {this.Li7Level2 = 0;}
	    }
	    	
	   	else if (this.Li7Level > 0 && this.HLevel2 > 0) {
		   	if (this.Li7Level >= size*TileFusionReactor.requiredHLi7 && this.HLevel2 >= size*TileFusionReactor.requiredHLi7) {
		   		this.heatVar = NuclearCraft.heatHLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFHLi7/100; this.Li7Level -= size*TileFusionReactor.requiredHLi7; this.HLevel2 -= size*TileFusionReactor.requiredHLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE4Out += size*TileFusionReactor.requiredHLi7*2;
		   	}
		   	if (this.Li7Level < size*TileFusionReactor.requiredHLi7) {this.Li7Level = 0;}
		   	if (this.HLevel2 < size*TileFusionReactor.requiredHLi7) {this.HLevel2 = 0;}
	    }
	    	
		    //
		    	
	    else if (this.DLevel > 0 && this.DLevel2 > 0) {
		   	if (this.DLevel >= size*TileFusionReactor.requiredDD && this.DLevel2 >= size*TileFusionReactor.requiredDD) {
		   		this.heatVar = NuclearCraft.heatDD; newE += efficiency*pMult*size*NuclearCraft.baseRFDD/100; this.DLevel -= size*TileFusionReactor.requiredDD; this.DLevel2 -= size*TileFusionReactor.requiredDD; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
		   		this.HE3Out += size*TileFusionReactor.requiredDD/2; this.TOut += size*TileFusionReactor.requiredDD/2; this.nOut += size*TileFusionReactor.requiredDD/16; this.HOut += size*TileFusionReactor.requiredDD/2;
		   	}
		   	if (this.DLevel < size*TileFusionReactor.requiredDD) {this.DLevel = 0;}
		   	if (this.DLevel2 < size*TileFusionReactor.requiredDD) {this.DLevel2 = 0;}
	   	}
	 
	    else if (this.DLevel > 0 && this.TLevel2 > 0) {
			if (this.DLevel >= size*TileFusionReactor.requiredDT && this.TLevel2 >= size*TileFusionReactor.requiredDT) {
				this.heatVar = NuclearCraft.heatDT; newE += efficiency*pMult*size*NuclearCraft.baseRFDT/100; this.DLevel -= size*TileFusionReactor.requiredDT; this.TLevel2 -= size*TileFusionReactor.requiredDT; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDT; this.nOut += size*TileFusionReactor.requiredDT/8;
			}
			if (this.DLevel < size*TileFusionReactor.requiredDT) {this.DLevel = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredDT) {this.TLevel2 = 0;}
		}
		
		else if (this.TLevel > 0 && this.DLevel2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredDT && this.DLevel2 >= size*TileFusionReactor.requiredDT) {
				this.heatVar = NuclearCraft.heatDT; newE += efficiency*pMult*size*NuclearCraft.baseRFDT/100; this.TLevel -= size*TileFusionReactor.requiredDT; this.DLevel2 -= size*TileFusionReactor.requiredDT; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDT; this.nOut += size*TileFusionReactor.requiredDT/8;
			}
			if (this.TLevel < size*TileFusionReactor.requiredDT) {this.TLevel = 0;}
			if (this.DLevel2 < size*TileFusionReactor.requiredDT) {this.DLevel2 = 0;}
		}
		
		else if (this.DLevel > 0 && this.HeLevel2 > 0) {
			if (this.DLevel >= size*TileFusionReactor.requiredDHe && this.HeLevel2 >= size*TileFusionReactor.requiredDHe) {
				this.heatVar = NuclearCraft.heatDHe; newE += efficiency*pMult*size*NuclearCraft.baseRFDHe/100; this.DLevel -= size*TileFusionReactor.requiredDHe; this.HeLevel2 -= size*TileFusionReactor.requiredDHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDHe; this.HOut += size*TileFusionReactor.requiredDHe;
			}
			if (this.DLevel < size*TileFusionReactor.requiredDHe) {this.DLevel = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredDHe) {this.HeLevel2 = 0;}
		}
		
		else if (this.HeLevel > 0 && this.DLevel2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredDHe && this.DLevel2 >= size*TileFusionReactor.requiredDHe) {
				this.heatVar = NuclearCraft.heatDHe; newE += efficiency*pMult*size*NuclearCraft.baseRFDHe/100; this.HeLevel -= size*TileFusionReactor.requiredDHe; this.DLevel2 -= size*TileFusionReactor.requiredDHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDHe; this.HOut += size*TileFusionReactor.requiredDHe;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredDHe) {this.HeLevel = 0;}
			if (this.DLevel2 < size*TileFusionReactor.requiredDHe) {this.DLevel2 = 0;}
		}
		
		else if (this.DLevel > 0 && this.BLevel2 > 0) {
			if (this.DLevel >= size*TileFusionReactor.requiredDB && this.BLevel2 >= size*TileFusionReactor.requiredDB) {
				this.heatVar = NuclearCraft.heatDB; newE += efficiency*pMult*size*NuclearCraft.baseRFDB/100; this.DLevel -= size*TileFusionReactor.requiredDB; this.BLevel2 -= size*TileFusionReactor.requiredDB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDB*3; this.nOut += size*TileFusionReactor.requiredDB/8;
			}
			if (this.DLevel < size*TileFusionReactor.requiredDB) {this.DLevel = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredDB) {this.BLevel2 = 0;}
		}
		
		else if (this.BLevel > 0 && this.DLevel2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredDB && this.DLevel2 >= size*TileFusionReactor.requiredDB) {
				this.heatVar = NuclearCraft.heatDB; newE += efficiency*pMult*size*NuclearCraft.baseRFDB/100; this.BLevel -= size*TileFusionReactor.requiredDB; this.DLevel2 -= size*TileFusionReactor.requiredDB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDB*3; this.nOut += size*TileFusionReactor.requiredDB/8;
			}
			if (this.BLevel < size*TileFusionReactor.requiredDB) {this.BLevel = 0;}
			if (this.DLevel2 < size*TileFusionReactor.requiredDB) {this.DLevel2 = 0;}
		}
		
		else if (this.DLevel > 0 && this.Li6Level2 > 0) {
			if (this.DLevel >= size*TileFusionReactor.requiredDLi6 && this.Li6Level2 >= size*TileFusionReactor.requiredDLi6) {
				this.heatVar = NuclearCraft.heatDLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFDLi6/100; this.DLevel -= size*TileFusionReactor.requiredDLi6; this.Li6Level2 -= size*TileFusionReactor.requiredDLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDLi6*2;
			}
			if (this.DLevel < size*TileFusionReactor.requiredDLi6) {this.DLevel = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredDLi6) {this.Li6Level2 = 0;}
		}
		
		else if (this.Li6Level > 0 && this.DLevel2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredDLi6 && this.DLevel2 >= size*TileFusionReactor.requiredDLi6) {
				this.heatVar = NuclearCraft.heatDLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFDLi6/100; this.Li6Level -= size*TileFusionReactor.requiredDLi6; this.DLevel2 -= size*TileFusionReactor.requiredDLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDLi6*2;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredDLi6) {this.Li6Level = 0;}
			if (this.DLevel2 < size*TileFusionReactor.requiredDLi6) {this.DLevel2 = 0;}
		}
		
		else if (this.DLevel > 0 && this.Li7Level2 > 0) {
			if (this.DLevel >= size*TileFusionReactor.requiredDLi7 && this.Li7Level2 >= size*TileFusionReactor.requiredDLi7) {
				this.heatVar = NuclearCraft.heatDLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFDLi7/100; this.DLevel -= size*TileFusionReactor.requiredDLi7; this.Li7Level2 -= size*TileFusionReactor.requiredDLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDLi7*2; this.nOut += size*TileFusionReactor.requiredDLi7/8;
			}
			if (this.DLevel < size*TileFusionReactor.requiredDLi7) {this.DLevel = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredDLi7) {this.Li7Level2 = 0;}
		}
		
		else if (this.Li7Level > 0 && this.DLevel2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredDLi7 && this.DLevel2 >= size*TileFusionReactor.requiredDLi7) {
				this.heatVar = NuclearCraft.heatDLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFDLi7/100; this.Li7Level -= size*TileFusionReactor.requiredDLi7; this.DLevel2 -= size*TileFusionReactor.requiredDLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredDLi7*2; this.nOut += size*TileFusionReactor.requiredDLi7/8;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredDLi7) {this.Li7Level = 0;}
			if (this.DLevel2 < size*TileFusionReactor.requiredDLi7) {this.DLevel2 = 0;}
		}
		
			//
			
		else if (this.TLevel > 0 && this.TLevel2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredTT && this.TLevel2 >= size*TileFusionReactor.requiredTT) {
				this.heatVar = NuclearCraft.heatTT; newE += efficiency*pMult*size*NuclearCraft.baseRFTT/100; this.TLevel -= size*TileFusionReactor.requiredTT; this.TLevel2 -= size*TileFusionReactor.requiredTT; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTT; this.nOut += size*TileFusionReactor.requiredTT/4;
			}
			if (this.TLevel < size*TileFusionReactor.requiredTT) {this.TLevel = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredTT) {this.TLevel2 = 0;}
		}
		
		else if (this.TLevel > 0 && this.HeLevel2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredTHe && this.HeLevel2 >= size*TileFusionReactor.requiredTHe) {
				this.heatVar = NuclearCraft.heatTHe; newE += efficiency*pMult*size*NuclearCraft.baseRFTHe/100; this.TLevel -= size*TileFusionReactor.requiredTHe; this.HeLevel2 -= size*TileFusionReactor.requiredTHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTHe; this.nOut += size*TileFusionReactor.requiredTHe/8; this.HOut += size*TileFusionReactor.requiredTHe;
			}
			if (this.TLevel < size*TileFusionReactor.requiredTHe) {this.TLevel = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredTHe) {this.HeLevel2 = 0;}
		}
		
		else if (this.HeLevel > 0 && this.TLevel2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredTHe && this.TLevel2 >= size*TileFusionReactor.requiredTHe) {
				this.heatVar = NuclearCraft.heatTHe; newE += efficiency*pMult*size*NuclearCraft.baseRFTHe/100; this.HeLevel -= size*TileFusionReactor.requiredTHe; this.TLevel2 -= size*TileFusionReactor.requiredTHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTHe; this.nOut += size*TileFusionReactor.requiredTHe/8; this.HOut += size*TileFusionReactor.requiredTHe;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredTHe) {this.HeLevel = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredTHe) {this.TLevel2 = 0;}
		}
		
		else if (this.TLevel > 0 && this.BLevel2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredTB && this.BLevel2 >= size*TileFusionReactor.requiredTB) {
				this.heatVar = NuclearCraft.heatTB; newE += efficiency*pMult*size*NuclearCraft.baseRFTB/100; this.TLevel -= size*TileFusionReactor.requiredTB; this.BLevel2 -= size*TileFusionReactor.requiredTB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTB*3; this.nOut += size*TileFusionReactor.requiredTB/4;
			}
			if (this.TLevel < size*TileFusionReactor.requiredTB) {this.TLevel = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredTB) {this.BLevel2 = 0;}
		}
		
		else if (this.BLevel > 0 && this.TLevel2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredTB && this.TLevel2 >= size*TileFusionReactor.requiredTB) {
				this.heatVar = NuclearCraft.heatTB; newE += efficiency*pMult*size*NuclearCraft.baseRFTB/100; this.BLevel -= size*TileFusionReactor.requiredTB; this.TLevel2 -= size*TileFusionReactor.requiredTB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTB*3; this.nOut += size*TileFusionReactor.requiredTB/4;
			}
			if (this.BLevel < size*TileFusionReactor.requiredTB) {this.BLevel = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredTB) {this.TLevel2 = 0;}
		}
		
		else if (this.TLevel > 0 && this.Li6Level2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredTLi6 && this.Li6Level2 >= size*TileFusionReactor.requiredTLi6) {
				this.heatVar = NuclearCraft.heatTLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFTLi6/100; this.TLevel -= size*TileFusionReactor.requiredTLi6; this.Li6Level2 -= size*TileFusionReactor.requiredTLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTLi6*2; this.nOut += size*TileFusionReactor.requiredTLi6/8;
			}
			if (this.TLevel < size*TileFusionReactor.requiredTLi6) {this.TLevel = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredTLi6) {this.Li6Level2 = 0;}
		}
		
		else if (this.Li6Level > 0 && this.TLevel2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredTLi6 && this.TLevel2 >= size*TileFusionReactor.requiredTLi6) {
				this.heatVar = NuclearCraft.heatTLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFTLi6/100; this.Li6Level -= size*TileFusionReactor.requiredTLi6; this.TLevel2 -= size*TileFusionReactor.requiredTLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTLi6*2; this.nOut += size*TileFusionReactor.requiredTLi6/8;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredTLi6) {this.Li6Level = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredTLi6) {this.TLevel2 = 0;}
		}
		
		else if (this.TLevel > 0 && this.Li7Level2 > 0) {
			if (this.TLevel >= size*TileFusionReactor.requiredTLi7 && this.Li7Level2 >= size*TileFusionReactor.requiredTLi7) {
				this.heatVar = NuclearCraft.heatTLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFTLi7/100; this.TLevel -= size*TileFusionReactor.requiredTLi7; this.Li7Level2 -= size*TileFusionReactor.requiredTLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTLi7*2; this.nOut += size*TileFusionReactor.requiredTLi7/4;
			}
			if (this.TLevel < size*TileFusionReactor.requiredTLi7) {this.TLevel = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredTLi7) {this.Li7Level2 = 0;}
		}
		
		else if (this.Li7Level > 0 && this.TLevel2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredTLi7 && this.TLevel2 >= size*TileFusionReactor.requiredTLi7) {
				this.heatVar = NuclearCraft.heatTLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFTLi7/100; this.Li7Level -= size*TileFusionReactor.requiredTLi7; this.TLevel2 -= size*TileFusionReactor.requiredTLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredTLi7*2; this.nOut += size*TileFusionReactor.requiredTLi7/4;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredTLi7) {this.Li7Level = 0;}
			if (this.TLevel2 < size*TileFusionReactor.requiredTLi7) {this.TLevel2 = 0;}
		}
		
			//
			
		else if (this.HeLevel > 0 && this.HeLevel2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredHeHe && this.HeLevel2 >= size*TileFusionReactor.requiredHeHe) {
				this.heatVar = NuclearCraft.heatHeHe; newE += efficiency*pMult*size*NuclearCraft.baseRFHeHe/100; this.HeLevel -= size*TileFusionReactor.requiredHeHe; this.HeLevel2 -= size*TileFusionReactor.requiredHeHe; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeHe; this.HOut += size*TileFusionReactor.requiredHeHe*2;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredHeHe) {this.HeLevel = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredHeHe) {this.HeLevel2 = 0;}
		}
		
		else if (this.HeLevel > 0 && this.BLevel2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredHeB && this.BLevel2 >= size*TileFusionReactor.requiredHeB) {
				this.heatVar = NuclearCraft.heatHeB; newE += efficiency*pMult*size*NuclearCraft.baseRFHeB/100; this.HeLevel -= size*TileFusionReactor.requiredHeB; this.BLevel2 -= size*TileFusionReactor.requiredHeB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeB*3; this.DOut += size*TileFusionReactor.requiredHeB;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredHeB) {this.HeLevel = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredHeB) {this.BLevel2 = 0;}
		}
		
		else if (this.BLevel > 0 && this.HeLevel2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredHeB && this.HeLevel2 >= size*TileFusionReactor.requiredHeB) {
				this.heatVar = NuclearCraft.heatHeB; newE += efficiency*pMult*size*NuclearCraft.baseRFHeB/100; this.BLevel -= size*TileFusionReactor.requiredHeB; this.HeLevel2 -= size*TileFusionReactor.requiredHeB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeB*3; this.DOut += size*TileFusionReactor.requiredHeB;
			}
			if (this.BLevel < size*TileFusionReactor.requiredHeB) {this.BLevel = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredHeB) {this.HeLevel2 = 0;}
		}
		
		else if (this.HeLevel > 0 && this.Li6Level2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredHeLi6 && this.Li6Level2 >= size*TileFusionReactor.requiredHeLi6) {
				this.heatVar = NuclearCraft.heatHeLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFHeLi6/100; this.HeLevel -= size*TileFusionReactor.requiredHeLi6; this.Li6Level2 -= size*TileFusionReactor.requiredHeLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeLi6*2; this.HOut += size*TileFusionReactor.requiredHeLi6;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredHeLi6) {this.HeLevel = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredHeLi6) {this.Li6Level2 = 0;}
		}
		
		else if (this.Li6Level > 0 && this.HeLevel2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredHeLi6 && this.HeLevel2 >= size*TileFusionReactor.requiredHeLi6) {
				this.heatVar = NuclearCraft.heatHeLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFHeLi6/100; this.Li6Level -= size*TileFusionReactor.requiredHeLi6; this.HeLevel2 -= size*TileFusionReactor.requiredHeLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeLi6*2; this.HOut += size*TileFusionReactor.requiredHeLi6;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredHeLi6) {this.Li6Level = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredHeLi6) {this.HeLevel2 = 0;}
		}
		
		else if (this.HeLevel > 0 && this.Li7Level2 > 0) {
			if (this.HeLevel >= size*TileFusionReactor.requiredHeLi7 && this.Li7Level2 >= size*TileFusionReactor.requiredHeLi7) {
				this.heatVar = NuclearCraft.heatHeLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFHeLi7/100; this.HeLevel -= size*TileFusionReactor.requiredHeLi7; this.Li7Level2 -= size*TileFusionReactor.requiredHeLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeLi7*2; this.DOut += size*TileFusionReactor.requiredHeLi7;
			}
			if (this.HeLevel < size*TileFusionReactor.requiredHeLi7) {this.HeLevel = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredHeLi7) {this.Li7Level2 = 0;}
		}
		
		else if (this.Li7Level > 0 && this.HeLevel2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredHeLi7 && this.HeLevel2 >= size*TileFusionReactor.requiredHeLi7) {
				this.heatVar = NuclearCraft.heatHeLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFHeLi7/100; this.Li7Level -= size*TileFusionReactor.requiredHeLi7; this.HeLevel2 -= size*TileFusionReactor.requiredHeLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredHeLi7*2; this.DOut += size*TileFusionReactor.requiredHeLi7;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredHeLi7) {this.Li7Level = 0;}
			if (this.HeLevel2 < size*TileFusionReactor.requiredHeLi7) {this.HeLevel2 = 0;}
		}
		
			//
			
		else if (this.BLevel > 0 && this.BLevel2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredBB && this.BLevel2 >= size*TileFusionReactor.requiredBB) {
				this.heatVar = NuclearCraft.heatBB; newE += efficiency*pMult*size*NuclearCraft.baseRFBB/100; this.BLevel -= size*TileFusionReactor.requiredBB; this.BLevel2 -= size*TileFusionReactor.requiredBB; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredBB*5; this.nOut += size*TileFusionReactor.requiredBB/4;
			}
			if (this.BLevel < size*TileFusionReactor.requiredBB) {this.BLevel = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredBB) {this.BLevel2 = 0;}
		}
		
		else if (this.BLevel > 0 && this.Li6Level2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredBLi6 && this.Li6Level2 >= size*TileFusionReactor.requiredBLi6) {
				this.heatVar = NuclearCraft.heatBLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFBLi6/100; this.BLevel -= size*TileFusionReactor.requiredBLi6; this.Li6Level2 -= size*TileFusionReactor.requiredBLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredBLi6*4; this.nOut += size*TileFusionReactor.requiredBLi6/8;
			}
			if (this.BLevel < size*TileFusionReactor.requiredBLi6) {this.BLevel = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredBLi6) {this.Li6Level2 = 0;}
		}
		
		else if (this.Li6Level > 0 && this.BLevel2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredBLi6 && this.BLevel2 >= size*TileFusionReactor.requiredBLi6) {
				this.heatVar = NuclearCraft.heatBLi6; newE += efficiency*pMult*size*NuclearCraft.baseRFBLi6/100; this.Li6Level -= size*TileFusionReactor.requiredBLi6; this.BLevel2 -= size*TileFusionReactor.requiredBLi6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredBLi6*4; this.nOut += size*TileFusionReactor.requiredBLi6/8;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredBLi6) {this.Li6Level = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredBLi6) {this.BLevel2 = 0;}
		}
		
		else if (this.BLevel > 0 && this.Li7Level2 > 0) {
			if (this.BLevel >= size*TileFusionReactor.requiredBLi7 && this.Li7Level2 >= size*TileFusionReactor.requiredBLi7) {
				this.heatVar = NuclearCraft.heatBLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFBLi7/100; this.BLevel -= size*TileFusionReactor.requiredBLi7; this.Li7Level2 -= size*TileFusionReactor.requiredBLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredBLi7*4; this.nOut += size*TileFusionReactor.requiredBLi7/4;
			}
			if (this.BLevel < size*TileFusionReactor.requiredBLi7) {this.BLevel = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredBLi7) {this.Li7Level2 = 0;}
		}
		
		else if (this.Li7Level > 0 && this.BLevel2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredBLi7 && this.BLevel2 >= size*TileFusionReactor.requiredBLi7) {
				this.heatVar = NuclearCraft.heatBLi7; newE += efficiency*pMult*size*NuclearCraft.baseRFBLi7/100; this.Li7Level -= size*TileFusionReactor.requiredBLi7; this.BLevel2 -= size*TileFusionReactor.requiredBLi7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredBLi7*4; this.nOut += size*TileFusionReactor.requiredBLi7/4;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredBLi7) {this.Li7Level = 0;}
			if (this.BLevel2 < size*TileFusionReactor.requiredBLi7) {this.BLevel2 = 0;}
		}
		
			//
			
		else if (this.Li6Level > 0 && this.Li6Level2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredLi6Li6 && this.Li6Level2 >= size*TileFusionReactor.requiredLi6Li6) {
				this.heatVar = NuclearCraft.heatLi6Li6; newE += efficiency*pMult*size*NuclearCraft.baseRFLi6Li6/100; this.Li6Level -= size*TileFusionReactor.requiredLi6Li6; this.Li6Level2 -= size*TileFusionReactor.requiredLi6Li6; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredLi6Li6*3;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredLi6Li6) {this.Li6Level = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredLi6Li6) {this.Li6Level2 = 0;}
		}
		
		else if (this.Li6Level > 0 && this.Li7Level2 > 0) {
			if (this.Li6Level >= size*TileFusionReactor.requiredLi6Li7 && this.Li7Level2 >= size*TileFusionReactor.requiredLi6Li7) {
				this.heatVar = NuclearCraft.heatLi6Li7; newE += efficiency*pMult*size*NuclearCraft.baseRFLi6Li7/100; this.Li6Level -= size*TileFusionReactor.requiredLi6Li7; this.Li7Level2 -= size*TileFusionReactor.requiredLi6Li7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredLi6Li7*3; this.nOut += size*TileFusionReactor.requiredLi6Li7/8;
			}
			if (this.Li6Level < size*TileFusionReactor.requiredLi6Li7) {this.Li6Level = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredLi6Li7) {this.Li7Level2 = 0;}
		}
		
		else if (this.Li7Level > 0 && this.Li6Level2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredLi6Li7 && this.Li6Level2 >= size*TileFusionReactor.requiredLi6Li7) {
				this.heatVar = NuclearCraft.heatLi6Li7; newE += efficiency*pMult*size*NuclearCraft.baseRFLi6Li7/100; this.Li7Level -= size*TileFusionReactor.requiredLi6Li7; this.Li6Level2 -= size*TileFusionReactor.requiredLi6Li7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredLi6Li7*3; this.nOut += size*TileFusionReactor.requiredLi6Li7/8;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredLi6Li7) {this.Li7Level = 0;}
			if (this.Li6Level2 < size*TileFusionReactor.requiredLi6Li7) {this.Li6Level2 = 0;}
		}
		
			//
			
		else if (this.Li7Level > 0 && this.Li7Level2 > 0) {
			if (this.Li7Level >= size*TileFusionReactor.requiredLi7Li7 && this.Li7Level2 >= size*TileFusionReactor.requiredLi7Li7) {
				this.heatVar = NuclearCraft.heatLi7Li7; newE += efficiency*pMult*size*NuclearCraft.baseRFLi7Li7/100; this.Li7Level -= size*TileFusionReactor.requiredLi7Li7; this.Li7Level2 -= size*TileFusionReactor.requiredLi7Li7; heat += NuclearCraft.fusionHeat*(100 - ((NuclearCraft.fusionEfficiencyConverge ? 1 : 0.9)*efficiency))/200000; flag = true;
				this.HE4Out += size*TileFusionReactor.requiredLi7Li7*3; this.nOut += size*TileFusionReactor.requiredLi7Li7/4;
			}
			if (this.Li7Level < size*TileFusionReactor.requiredLi7Li7) {this.Li7Level = 0;}
			if (this.Li7Level2 < size*TileFusionReactor.requiredLi7Li7) {this.Li7Level2 = 0;}
		}
		
			//
		
		else {
			flag = false;
			if (heat >= 0.01 && heat >= (0.0001*heat*Math.log10(heat+Math.E)) /*&& storage.getEnergyStored() >= 20000*/) {
				heat = heat-(0.0001*heat*Math.log10(heat+Math.E));
			}
		}
		E = storage.getEnergyStored();
		if (E != lastE) BlockFusionReactor.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
		
		} else {
			if (heat >= 0.01 && heat >= (0.0001*heat*Math.log10(heat+Math.E)) /*&& storage.getEnergyStored() < 20000*/) {
				heat = heat-(0.0001*heat*Math.log10(heat+Math.E));
			}
		}
		
		EShown = (int) Math.ceil(newE);
		
		this.storage.receiveEnergy((int) Math.ceil(newE), false);

      	if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level == 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 == 0) {EShown = 0;}
  	
      	if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0) {HLevel=0; DLevel=0; TLevel=0; HeLevel=0; BLevel=0; Li6Level=0; Li7Level=0;}
      	if (HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0) {HLevel2=0; DLevel2=0; TLevel2=0; HeLevel2=0; BLevel2=0; Li6Level2=0; Li7Level2=0;}
	}

	public void addEnergy() {
		lastE = storage.getEnergyStored();
		if (heat >= 8 && HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level > 0 && HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 > 0 && complete == 1) {
			for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
				for (int x = -1; x < 2; ++x) {
					for (int y = 0; y < 3; ++y) {
						for (int z = -1; z < 2; ++z) {
							
							TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX + x, yCoord + side.offsetY + y, zCoord + side.offsetZ + z);
			
							if ((tile instanceof IEnergyReceiver)) {
								storage.extractEnergy(((IEnergyReceiver)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
							} else if ((tile instanceof IEnergyHandler)) {
								storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
							}
						}
					}
				}
			}
		}
		E = storage.getEnergyStored();
		if (E != lastE) BlockFusionReactor.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	}
	
	private void fuel1() {
	    ItemStack stack = this.getStackInSlot(0);
	    if (stack != null && isHFuel(stack) && this.HLevel() + HFuelValue(stack) <= TileFusionReactor.Max && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
	        this.addH(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isDFuel(stack) && this.DLevel() + DFuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
	        this.addD(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isTFuel(stack) && this.TLevel() + TFuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
	        this.addT(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isHeFuel(stack) && this.HeLevel() + HeFuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
	        this.addHe(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isBFuel(stack) && this.BLevel() + BFuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
	        this.addB(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isLi6Fuel(stack) && this.Li6Level() + Li6FuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li7Level() <= 0) {
	        this.addLi6(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    } else if (stack != null && isLi7Fuel(stack) && this.Li7Level() + Li7FuelValue(stack) <= TileFusionReactor.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0) {
	        this.addLi7(fuelValue(stack)); --this.slots[0].stackSize;
	        if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	    }
	}

	private void fuel2() {
	    ItemStack stack = this.getStackInSlot(1);
	    if (stack != null && isHFuel(stack) && this.HLevel2() + HFuelValue(stack) <= TileFusionReactor.Max && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
	    this.addH2(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isDFuel(stack) && this.DLevel2() + DFuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
	        this.addD2(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isTFuel(stack) && this.TLevel2() + TFuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
	        this.addT2(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isHeFuel(stack) && this.HeLevel2() + HeFuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
	        this.addHe2(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isBFuel(stack) && this.BLevel2() + BFuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
	        this.addB2(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isLi6Fuel(stack) && this.Li6Level2() + Li6FuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li7Level2() <= 0) {
	        this.addLi62(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    } else if (stack != null && isLi7Fuel(stack) && this.Li7Level2() + Li7FuelValue(stack) <= TileFusionReactor.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0) {
	        this.addLi72(fuelValue(stack)); --this.slots[1].stackSize;
	        if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	    }
	}
	
	// Available gases: H, D, T Li7
	private void gasFuel() {
		if (gasTank2.getStored() <= 0) {
		} else if (gasTank2.getGasType() == GasRegistry.getGas("hydrogen")) {
			if (this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.HLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addH2(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addH2(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addH2(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			} if (this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.HLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addH(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addH(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addH(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			}
		} else if (gasTank2.getGasType() == GasRegistry.getGas("deuterium")) {
			if (this.HLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.DLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addD2(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addD2(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addD2(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			} if (this.HLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.DLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addD(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addD(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addD(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			}
		} else if (gasTank2.getGasType() == GasRegistry.getGas("tritium")) {
			if (this.DLevel2() <= 0 && this.HLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.TLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addT2(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addT2(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addT2(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			} if (this.DLevel() <= 0 && this.HLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.TLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addT(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addT(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addT(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			}
		} else if (gasTank2.getGasType() == GasRegistry.getGas("lithium")) {
			if (this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.HLevel2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.Li7Level2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addLi72(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addLi72(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addLi72(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			} if (this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.HLevel() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.Li7Level)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank2.getStored()) {
					if (gasSpace < 1600) {
						this.addLi7(fuelSpace); gasTank2.draw(gasSpace, true);
					} else {
						this.addLi7(160000); gasTank2.draw(1600, true);
					}
				} else {
					this.addLi7(gasTank2.getStored()*100); gasTank2.setGas(null);
				}
			}
		}
		if (gasTank.getStored() <= 0) {
		} else if (gasTank.getGasType() == GasRegistry.getGas("hydrogen")) {
			if (this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.HLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addH(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addH(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addH(gasTank.getStored()*100); gasTank.setGas(null);
				}
			} if (this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.HLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addH2(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addH2(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addH2(gasTank.getStored()*100); gasTank.setGas(null);
				}
			}
		} else if (gasTank.getGasType() == GasRegistry.getGas("deuterium")) {
			if (this.HLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.DLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addD(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addD(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addD(gasTank.getStored()*100); gasTank.setGas(null);
				}
			} if (this.HLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.DLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addD2(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addD2(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addD2(gasTank.getStored()*100); gasTank.setGas(null);
				}
			}
		} else if (gasTank.getGasType() == GasRegistry.getGas("tritium")) {
			if (this.DLevel() <= 0 && this.HLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.TLevel)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addT(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addT(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addT(gasTank.getStored()*100); gasTank.setGas(null);
				}
			} if (this.DLevel2() <= 0 && this.HLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.TLevel2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addT2(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addT2(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addT2(gasTank.getStored()*100); gasTank.setGas(null);
				}
			}
		} else if (gasTank.getGasType() == GasRegistry.getGas("lithium")) {
			if (this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.HLevel() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.Li7Level)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addLi7(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addLi7(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addLi7(gasTank.getStored()*100); gasTank.setGas(null);
				}
			} if (this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.HLevel2() <= 0) {
				int gasSpace = (TileFusionReactor.Max - this.Li7Level2)/100;
				int fuelSpace = gasSpace*100;
				if (gasSpace < gasTank.getStored()) {
					if (gasSpace < 1600) {
						this.addLi72(fuelSpace); gasTank.draw(gasSpace, true);
					} else {
						this.addLi72(160000); gasTank.draw(1600, true);
					}
				} else {
					this.addLi72(gasTank.getStored()*100); gasTank.setGas(null);
				}
			}
		}
	}
	    
	public static int fuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
	    	Item i = stack.getItem();
	    	if(i == new ItemStack (NCItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 37).getItem() && i.getDamage(stack) == 37) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 38).getItem() && i.getDamage(stack) == 38) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 39).getItem() && i.getDamage(stack) == 39) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 44).getItem() && i.getDamage(stack) == 44) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 41).getItem() && i.getDamage(stack) == 41) return 100000;
	    	else if(i == new ItemStack (NCItems.fuel, 1, 42).getItem() && i.getDamage(stack) == 42) return 100000;
	    }
		return 0;
	}

	public static boolean isAnyFuel(ItemStack stack) {
	    return fuelValue(stack) > 0;
	}
	
	public static boolean isCapsule(ItemStack stack) {
		if (stack.getItem() == new ItemStack(NCItems.fuel, 1, 48).getItem() && stack.getItem().getDamage(stack) == 48) return true;
		return false;
	}

	public static int HFuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36) return 100000;
		}
		return 0;
	}
	
	public static int DFuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 37).getItem() && i.getDamage(stack) == 37) return 100000;
		}
		return 0;
	}
	
	public static int TFuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 38).getItem() && i.getDamage(stack) == 38) return 100000;
		}
	    return 0;
	}
	
	public static int HeFuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 39).getItem() && i.getDamage(stack) == 39) return 100000;
		}
	    return 0;
	}
	
	public static int BFuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 44).getItem() && i.getDamage(stack) == 44) return 100000;
		}
	    return 0;
	}
	
	public static int Li6FuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 41).getItem() && i.getDamage(stack) == 41) return 100000;
		}
	    return 0;
	}
	
	public static int Li7FuelValue(ItemStack stack) {
		if (stack == null) return 0; else {
			Item i = stack.getItem();
			if(i == new ItemStack (NCItems.fuel, 1, 42).getItem() && i.getDamage(stack) == 42) return 100000;
		}
	    return 0;
	}
	
	public void readFromNBT(NBTTagCompound nbt) {
	    super.readFromNBT(nbt);
	    
	    this.EShown = nbt.getInteger("EShown");
	    this.HLevel = nbt.getInteger("HLevel");
	    this.DLevel = nbt.getInteger("DLevel");
	    this.TLevel = nbt.getInteger("TLevel");
	    this.HeLevel = nbt.getInteger("HeLevel");
	    this.BLevel = nbt.getInteger("BLevel");
	    this.Li6Level = nbt.getInteger("Li6Level");
	    this.Li7Level = nbt.getInteger("Li7Level");
	    this.HLevel2 = nbt.getInteger("HLevel2");
	    this.DLevel2 = nbt.getInteger("DLevel2");
	    this.TLevel2 = nbt.getInteger("TLevel2");
	    this.HeLevel2 = nbt.getInteger("HeLevel2");
	    this.BLevel2 = nbt.getInteger("BLevel2");
	    this.Li6Level2 = nbt.getInteger("Li6Level2");
	    this.Li7Level2 = nbt.getInteger("Li7Level2");
	    
	    this.size = nbt.getInteger("size");
	    this.problem = nbt.getString("problem");
	    
	    this.HOut = nbt.getDouble("HOut");
	    this.DOut = nbt.getDouble("DOut");
	    this.TOut = nbt.getDouble("TOut");
	    this.HE3Out = nbt.getDouble("HE3Out");
	    this.HE4Out = nbt.getDouble("HE4Out");
	    this.nOut = nbt.getDouble("nOut");
	    
	    this.efficiency = nbt.getDouble("efficiency");
	    this.heat = nbt.getDouble("heat");
	    this.heatVar = nbt.getDouble("heatVar");
	    
	    this.lastE = nbt.getInteger("lE");
	    this.E = nbt.getInteger("E");
	    
	    this.complete = nbt.getInteger("complete");
	}

	public void writeToNBT(NBTTagCompound nbt) {
	    super.writeToNBT(nbt);
	    
	    nbt.setInteger("EShown", this.EShown);
	    nbt.setInteger("HLevel", this.HLevel);
	    nbt.setInteger("DLevel", this.DLevel);
	    nbt.setInteger("TLevel", this.TLevel);
	    nbt.setInteger("HeLevel", this.HeLevel);
	    nbt.setInteger("BLevel", this.BLevel);
	    nbt.setInteger("Li6Level", this.Li6Level);
	    nbt.setInteger("Li7Level", this.Li7Level);
	    nbt.setInteger("HLevel2", this.HLevel2);
	    nbt.setInteger("DLevel2", this.DLevel2);
	    nbt.setInteger("TLevel2", this.TLevel2);
	    nbt.setInteger("HeLevel2", this.HeLevel2);
	    nbt.setInteger("BLevel2", this.BLevel2);
	    nbt.setInteger("Li6Level2", this.Li6Level2);
	    nbt.setInteger("Li7Level2", this.Li7Level2);
	    
	    nbt.setInteger("size", this.size);
	    nbt.setString("problem", this.problem);
	    
	    nbt.setDouble("HOut", this.HOut);
	    nbt.setDouble("DOut", this.DOut);
	    nbt.setDouble("TOut", this.TOut);
	    nbt.setDouble("HE3Out", this.HE3Out);
	    nbt.setDouble("HE4Out", this.HE4Out);
	    nbt.setDouble("nOut", this.nOut);
	    
	    nbt.setDouble("efficiency", this.efficiency);
	    nbt.setDouble("heat", this.heat);
	    nbt.setDouble("heatVar", this.heatVar);
	    
	    nbt.setInteger("lE", this.lastE);
	    nbt.setInteger("E", this.E);
	    
	    nbt.setInteger("complete", this.complete);
	}
		
	public void addH(int add) { this.HLevel += add; }
	public void addD(int add) { this.DLevel += add; }
	public void addT(int add) { this.TLevel += add; }
	public void addHe(int add) { this.HeLevel += add; }
	public void addB(int add) { this.BLevel += add; }
	public void addLi6(int add) { this.Li6Level += add; }
	public void addLi7(int add) { this.Li7Level += add; }
	
	public void addH2(int add) { this.HLevel2 += add; }
	public void addD2(int add) { this.DLevel2 += add; }
	public void addT2(int add) { this.TLevel2 += add; }
	public void addHe2(int add) { this.HeLevel2 += add; }
	public void addB2(int add) { this.BLevel2 += add; }
	public void addLi62(int add) { this.Li6Level2 += add; }
	public void addLi72(int add) { this.Li7Level2 += add; }
	
	public void removeH(int remove) { this.HLevel -= remove; }
	public void removeD(int remove) { this.DLevel -= remove; }
	public void removeT(int remove) { this.TLevel -= remove; }
	public void removeHe(int remove) { this.HeLevel -= remove; }
	public void removeB(int remove) { this.BLevel -= remove; }
	public void removeLi6(int remove) { this.Li6Level -= remove; }
	public void removeLi7(int remove) { this.Li7Level -= remove; }
	
	public void removeH2(int remove) { this.HLevel2 -= remove; }
	public void removeD2(int remove) { this.DLevel2 -= remove; }
	public void removeT2(int remove) { this.TLevel2 -= remove; }
	public void removeHe2(int remove) { this.HeLevel2 -= remove; }
	public void removeB2(int remove) { this.BLevel2 -= remove; }
	public void removeLi62(int remove) { this.Li6Level2 -= remove; }
	public void removeLi72(int remove) { this.Li7Level2 -= remove; }
	
	public int HLevel() { return HLevel; }
	public int DLevel() { return DLevel; }
	public int TLevel() { return TLevel; }
	public int HeLevel() { return HeLevel; }
	public int BLevel() { return BLevel; }
	public int Li6Level() { return Li6Level; }
	public int Li7Level() { return Li7Level; }
	
	public int HLevel2() { return HLevel2; }
	public int DLevel2() { return DLevel2; }
	public int TLevel2() { return TLevel2; }
	public int HeLevel2() { return HeLevel2; }
	public int BLevel2() { return BLevel2; }
	public int Li6Level2() { return Li6Level2; }
	public int Li7Level2() { return Li7Level2; }

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		if (heat < 8 || (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0) || complete == 0) {
			return this.storage.receiveEnergy(maxReceive, simulate);
		}
		else return 0;
	}

	public static boolean isHFuel(ItemStack stack) {return HFuelValue(stack) > 0;}
	public static boolean isDFuel(ItemStack stack) {return DFuelValue(stack) > 0;}
	public static boolean isTFuel(ItemStack stack) {return TFuelValue(stack) > 0;}
	public static boolean isHeFuel(ItemStack stack) {return HeFuelValue(stack) > 0;}
	public static boolean isBFuel(ItemStack stack) {return BFuelValue(stack) > 0;}
	public static boolean isLi6Fuel(ItemStack stack) {return Li6FuelValue(stack) > 0;}
	public static boolean isLi7Fuel(ItemStack stack) {return Li7FuelValue(stack) > 0;}
	
	public boolean isInputtableFuel1(ItemStack stack) {
		if (HLevel > 0) return isHFuel(stack);
	    else if (DLevel > 0) return isDFuel(stack);
	    else if (TLevel > 0) return isTFuel(stack);
	    else if (HeLevel > 0) return isHeFuel(stack);
	    else if (BLevel > 0) return isBFuel(stack);
	    else if (Li6Level > 0) return isLi6Fuel(stack);
	    else if (Li7Level > 0) return isLi7Fuel(stack);
	    else return isAnyFuel(stack);
	}
	
	public boolean isInputtableFuel2(ItemStack stack) {
	   	if (HLevel2 > 0) return isHFuel(stack);
	   	else if (DLevel2 > 0) return isDFuel(stack);
	   	else if (TLevel2 > 0) return isTFuel(stack);
	   	else if (HeLevel2 > 0) return isHeFuel(stack);
	   	else if (BLevel2 > 0) return isBFuel(stack);
	   	else if (Li6Level2 > 0) return isLi6Fuel(stack);
	   	else if (Li7Level2 > 0) return isLi7Fuel(stack);
	   	else return isAnyFuel(stack);
	}

	public boolean isItemValidForSlot(int slot, ItemStack stack) {
		if (slot == 0) {
	    	if (HLevel > 0) return isHFuel(stack);
	        else if (DLevel > 0) return isDFuel(stack);
	    	else if (TLevel > 0) return isTFuel(stack);
	        else if (HeLevel > 0) return isHeFuel(stack);
	        else if (BLevel > 0) return isBFuel(stack);
	        else if (Li6Level > 0) return isLi6Fuel(stack);
	        else if (Li7Level > 0) return isLi7Fuel(stack);
	        else return isAnyFuel(stack);
		}
		else if (slot == 1) {
	    	if (HLevel2 > 0) return isHFuel(stack);
	    	else if (DLevel2 > 0) return isDFuel(stack);
	    	else if (TLevel2 > 0) return isTFuel(stack);
	    	else if (HeLevel2 > 0) return isHeFuel(stack);
	    	else if (BLevel2 > 0) return isBFuel(stack);
	    	else if (Li6Level2 > 0) return isLi6Fuel(stack);
	    	else if (Li7Level2 > 0) return isLi7Fuel(stack);
	    	else return isAnyFuel(stack);
	    }
	    else if (slot == 2) {
	    	return isCapsule(stack);
	    }
	    return false;
	}

	/**
	 * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
	 * block.
	 */
	public int[] getAccessibleSlotsFromSide(int side) {
	    return input;
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
	 * side
	 */
	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return this.isItemValidForSlot(slot, stack);
	}

	/**
	 * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
	 * side
	 */
	public boolean canExtractItem(int slot, ItemStack stack, int side) {
	    return slot > 2;
	}

	public ItemStack getStackInSlot(int var1) {
		return slots[var1];
	}
	
	public int getSizeInventory() {
		return this.slots.length;
	}
	
	public void setInventorySlotContents(int i, ItemStack itemstack) {
		this.slots[i] = itemstack;
		if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
			itemstack.stackSize = this.getInventoryStackLimit();
		}
		markDirty();
	}

	public int receiveGas(ForgeDirection side, GasStack stack, boolean doTransfer) {
		return this.gasTank.getStored() < this.gasTank.getMaxGas() ? this.gasTank.receive(stack, doTransfer) : this.gasTank2.receive(stack, doTransfer);
	}

	public GasStack drawGas(ForgeDirection side, int amount, boolean doTransfer) {
		return null;
	}

	public boolean canReceiveGas(ForgeDirection side, Gas type) {
		return (type == GasRegistry.getGas("hydrogen") || type == GasRegistry.getGas("deuterium") || type == GasRegistry.getGas("tritium") || type == GasRegistry.getGas("lithium"));
	}
	
	
	public boolean canDrawGas(ForgeDirection side, Gas type) {
		return false;
	}

	@Deprecated
	public int receiveGas(ForgeDirection side, GasStack stack) {
		return 0;
	}

	@Deprecated
	public GasStack drawGas(ForgeDirection side, int amount) {
		return null;
	}

	public boolean canTubeConnect(ForgeDirection side) {
		return true;
	}
}