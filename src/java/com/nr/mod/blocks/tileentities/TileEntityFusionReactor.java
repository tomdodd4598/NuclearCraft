package com.nr.mod.blocks.tileentities;

import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.items.NRItems;

public class TileEntityFusionReactor extends TileEntityInventory implements IEnergyHandler, IEnergyConnection, ISidedInventory, IEnergyReceiver {

		public int size = 1;
		public boolean isMain = true;
		public int below = 0;
	
	    public EnergyStorage storage = new EnergyStorage(10000000, 10000000);
	    public int energy;
	    public int EShown;
	    
	    public static double pMult = 1.25*NuclearRelativistics.fusionRF;
	    
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
	    
	    public int Max = 12096000;
	    
	    public int powerHH = 80;
	    public int powerHD = 60;
	    public int powerHT = 20;
	    public int powerHHe = 20;
	    public int powerHB = 80;
	    public int powerHLi6 = 30;
	    public int powerHLi7 = 120;
	    
	    public int powerDD = 140;
	    public int powerDT = 200;
	    public int powerDHe = 160;
	    public int powerDB = 20;
	    public int powerDLi6 = 150;
	    public int powerDLi7 = 10;
	    
	    public int powerTT = 60;
	    public int powerTHe = 40;
	    public int powerTB = 10;
	    public int powerTLi6 = 5;
	    public int powerTLi7 = 10;
	    
	    public int powerHeHe = 120;
	    public int powerHeB = 5;
	    public int powerHeLi6 = 140;
	    public int powerHeLi7 = 30;
	    
	    public int powerBB = 10;
	    public int powerBLi6 = 5;
	    public int powerBLi7 = 5;
	    
	    public int powerLi6Li6 = 5;
	    public int powerLi6Li7 = 5;
	    
	    public int powerLi7Li7 = 5;
	    
	    public int requiredHH = 100;
	    public int requiredHD = 60;
	    public int requiredHT = 40;
	    public int requiredHHe = 40;
	    public int requiredHB = 10;
	    public int requiredHLi6 = 10;
	    public int requiredHLi7 = 20;

	    public int requiredDD = 40;
	    public int requiredDT = 80;
	    public int requiredDHe = 25;
	    public int requiredDB = 10;
	    public int requiredDLi6 = 25;
	    public int requiredDLi7 = 10;
	    
	    public int requiredTT = 20;
	    public int requiredTHe = 10;
	    public int requiredTB = 10;
	    public int requiredTLi6 = 2;
	    public int requiredTLi7 = 4;

	    public int requiredHeHe = 20;
	    public int requiredHeB = 5;
	    public int requiredHeLi6 = 28;
	    public int requiredHeLi7 = 10;

	    public int requiredBB = 5;
	    public int requiredBLi6 = 5;
	    public int requiredBLi7 = 5;

	    public int requiredLi6Li6 = 5;
	    public int requiredLi6Li7 = 5;

	    public int requiredLi7Li7 = 5;

	    public double maxHeat = 20000;
	    public double efficiency = 0;
	    public double heatVar = 9;
	    public double heat;

	    public double heatHH = 8.87;
	    public double heatHD = 8.43;
	    public double heatHT = 9.65;
	    public double heatHHe = 9.68;
	    public double heatHB = 9.84;
	    public double heatHLi6 = 9.62;
	    public double heatHLi7 = 9.64;

	    public double heatDD = 9.67;
	    public double heatDT = 7.70;
	    public double heatDHe = 8.97;
	    public double heatDB = 9.89;
	    public double heatDLi6 = 9.78;
	    public double heatDLi7 = 10.10;

	    public double heatTT = 9.46;
	    public double heatTHe = 9.73;
	    public double heatTB = 10.16;
	    public double heatTLi6 = 10.28;
	    public double heatTLi7 = 10.31;

	    public double heatHeHe = 10.02;
	    public double heatHeB = 10.30;
	    public double heatHeLi6 = 10.56;
	    public double heatHeLi7 = 10.62;

	    public double heatBB = 10.58;
	    public double heatBLi6 = 10.61;
	    public double heatBLi7 = 10.69;

	    public double heatLi6Li6 = 10.68;
	    public double heatLi6Li7 = 10.66;

	    public double heatLi7Li7 = 10.73;
	    
	    public boolean flag;
	    public boolean flag1 = false;
	    public int lastE;
	    public int E;
	    public int maxTransfer = 1000000;
	    public String direction;
	    
		public final int[] input = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
		public final int[] output = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8};

	    public TileEntityFusionReactor() {
	        super.slots = new ItemStack[9];
	        super.localizedName = "Fusion Reactor";
	    }

	    public void updateEntity() {
	    	super.updateEntity();
	    	isMain(worldObj, xCoord, yCoord, zCoord);
	    	isMain();
	    	getBelow(worldObj, xCoord, yCoord, zCoord);
	    	getMain();
	    	if(!this.worldObj.isRemote) {
		    	setSize(worldObj, xCoord, yCoord, zCoord);
		    	energy();
		    	addEnergy();
		    	fuel1();
		    	fuel2();
		    	outputs();
		    	efficiency();
		    	for (int i = 1; i < 10; i++) {
		    		if ((heat < 8 || HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0) && storage.getEnergyStored() >= 8000) {
		    			this.storage.receiveEnergy(-10000, false);
		    			heat = heat+0.0005;
		    		}
	    		}
		    	if (heat < 0) {
		    		heat = 0;
		    	}
	    	}
	    	if (flag != flag1) { flag1 = flag; BlockFusionReactor.updateBlockState(worldObj, xCoord, yCoord, zCoord); }
	    	markDirty();
	    }
	    
	    public void efficiency() {
	    	if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0) {
	    		efficiency = 0;
	    	} else if (heat >= 8) {
	    		efficiency = 100*Math.exp(-Math.pow(heatVar-Math.log(heat), 2)/2)/(heat*Math.exp(0.5-heatVar));
	    	} else {
	    		efficiency = 0;
	    	}
	    }
	    
	    public void outputs() {
	    	if (HOut >= 336000) {
	    		if (this.slots[3] == null) { this.slots[3] = new ItemStack(NRItems.fuel, 1, 36); HOut -= 336000; }
	    		else if (this.slots[3].stackSize < 64) { this.slots[3].stackSize ++; HOut -= 336000; }
	    	}
	    	if (DOut >= 336000) {
	    		if (this.slots[4] == null) { this.slots[4] = new ItemStack(NRItems.fuel, 1, 37); DOut -= 336000; }
	    		else if (this.slots[4].stackSize < 64) { this.slots[4].stackSize ++; DOut -= 336000; }
	    	}
	    	if (TOut >= 336000) {
	    		if (this.slots[5] == null) { this.slots[5] = new ItemStack(NRItems.fuel, 1, 38); TOut -= 336000; }
	    		else if (this.slots[5].stackSize < 64) { this.slots[5].stackSize ++; TOut -= 336000; }
	    	}
	    	if (HE3Out >= 336000) {
	    		if (this.slots[6] == null) { this.slots[6] = new ItemStack(NRItems.fuel, 1, 39); HE3Out -= 336000; }
	    		else if (this.slots[6].stackSize < 64) { this.slots[6].stackSize ++; HE3Out -= 336000; }
	    	}
	    	if (HE4Out >= 336000) {
	    		if (this.slots[7] == null) { this.slots[7] = new ItemStack(NRItems.fuel, 1, 40); HE4Out -= 336000; }
	    		else if (this.slots[7].stackSize < 64) { this.slots[7].stackSize ++; HE4Out -= 336000; }
	    	}
	    	if (nOut >= 336000 && this.slots[2] != null && this.slots[2].stackSize > 0 && this.slots[2].getItem() == new ItemStack (NRItems.fuel, 1, 48).getItem() && this.slots[2].getItem().getDamage(this.slots[2]) == 48) {
	    		if (this.slots[8] == null) { this.slots[8] = new ItemStack(NRItems.fuel, 1, 47); nOut -= 336000; this.slots[2].stackSize --; if (this.slots[2].stackSize < 1) this.slots[2] = null;}
	    		else if (this.slots[8].stackSize < 64) { this.slots[8].stackSize ++; nOut -= 336000; this.slots[2].stackSize --; if (this.slots[2].stackSize < 1) this.slots[2] = null;}
	    	}
	    	
	    	if (HOut > Max) HOut = Max;
	    	if (DOut > Max) DOut = Max;
	    	if (TOut > Max) TOut = Max;
	    	if (HE3Out > Max) HE3Out = Max;
	    	if (HE4Out > Max) HE4Out = Max;
	    	if (nOut > Max) nOut = Max;
	    }
	    
	    public void setSize(World world, int x, int y, int z) {
			size = 1;
			for (int yUp = 1; yUp < 128; ++yUp) {
				if (world.getBlock(x, y + 2*yUp, z) == NRBlocks.fusionReactor) {
					size++;
				} else break;
			}
		}
	    
	    public void isMain(World world, int x, int y, int z) {
	    	if (world.getBlock(x, y - 2, z) == NRBlocks.fusionReactor) {
	    		isMain = false;
	    	} else {
	    		isMain = true;
	    	}
	    }
	    
	    public void getBelow(World world, int x, int y, int z) {
			int b = 0;
			for (int yDown = 1; yDown < 128; ++yDown) {
				if (world.getBlock(x, y - 2*yDown, z) == NRBlocks.fusionReactor) {
					b++;
				} else break;
			}
			below = b;
		}
	    
	    public TileEntityFusionReactor getMain() {
	    	/*if (worldObj.getTileEntity(xCoord, yCoord, zCoord) == null) return null;
	    	else*/ if (worldObj.getTileEntity(xCoord, yCoord - 2*below, zCoord) == null) {
	    		below = 0;
	    		return (TileEntityFusionReactor) worldObj.getTileEntity(xCoord, yCoord, zCoord);
	    	}
	    	else if (below != 0 && worldObj.getTileEntity(xCoord, yCoord - 2*below, zCoord) != null) {
	    		TileEntityFusionReactor main = (TileEntityFusionReactor) worldObj.getTileEntity(xCoord, yCoord - 2*below, zCoord);
	    		return main;
	    	} else {
	    		below = 0;
	    		return (TileEntityFusionReactor) worldObj.getTileEntity(xCoord, yCoord, zCoord);
	    	}
	    }
	    
	    public boolean isMain() {
	    	if (worldObj.getBlock(xCoord, yCoord - 2, zCoord) == NRBlocks.fusionReactor) return false;
	    	else return true;
	    }
	    
	    private void energy() {
	    	int prevE = this.storage.getEnergyStored();
	    	int newE;
	    	//double eV = Math.sqrt(efficiency)/10;

	    	if (!worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && heat >=8) {
	    	lastE = storage.getEnergyStored();
	    	
	    	if (this.HLevel > 0 && this.HLevel2 > 0) {
		    	if (this.HLevel >= size*this.requiredHH && this.HLevel2 >= size*this.requiredHH) {
		    		this.heatVar = this.heatHH; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHH/100), false); this.HLevel -= size*this.requiredHH; this.HLevel2 -= size*this.requiredHH; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.DOut += size*this.requiredHH;
		    	}
		    	if (this.HLevel < size*this.requiredHH) {this.HLevel = 0;}
		    	if (this.HLevel2 < size*this.requiredHH) {this.HLevel2 = 0;}
	    	}
	    	
	    	else if (this.HLevel > 0 && this.DLevel2 > 0) {
		    	if (this.HLevel >= size*this.requiredHD && this.DLevel2 >= size*this.requiredHD) {
		    		this.heatVar = this.heatHD; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHD/100), false); this.HLevel -= size*this.requiredHD; this.DLevel2 -= size*this.requiredHD; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE3Out += size*this.requiredHD;
		    	 }
		    	if (this.HLevel < size*this.requiredHD) {this.HLevel = 0;}
		    	if (this.DLevel2 < size*this.requiredHD) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.HLevel2 > 0) {
		    	if (this.DLevel >= size*this.requiredHD && this.HLevel2 >= size*this.requiredHD) {
		    		this.heatVar = this.heatHD; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHD/100), false); this.DLevel -= size*this.requiredHD; this.HLevel2 -= size*this.requiredHD; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE3Out += size*this.requiredHD;
		    	 }
		    	if (this.DLevel < size*this.requiredHD) {this.DLevel = 0;}
		    	if (this.HLevel2 < size*this.requiredHD) {this.HLevel2 = 0;}
	    	}
		    
	    	else if (this.HLevel > 0 && this.TLevel2 > 0) {
		    	if (this.HLevel >= size*this.requiredHT && this.TLevel2 >= size*this.requiredHT) {
		    		this.heatVar = this.heatHT; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHT/100), false); this.HLevel -= size*this.requiredHT; this.TLevel2 -= size*this.requiredHT; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE3Out += size*this.requiredHT; this.nOut += size*this.requiredHT/8;
		    	 }
		    	if (this.HLevel < size*this.requiredHT) {this.HLevel = 0;}
		    	if (this.TLevel2 < size*this.requiredHT) {this.TLevel2 = 0;}
	    	}
		    
	    	else if (this.TLevel > 0 && this.HLevel2 > 0) {
		    	if (this.TLevel >= size*this.requiredHT && this.HLevel2 >= size*this.requiredHT) {
		    		this.heatVar = this.heatHT; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHT/100), false); this.TLevel -= size*this.requiredHT; this.HLevel2 -= size*this.requiredHT; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE3Out += size*this.requiredHT; this.nOut += size*this.requiredHT/8;
		    	 }
		    	if (this.TLevel < size*this.requiredHT) {this.TLevel = 0;}
		    	if (this.HLevel2 < size*this.requiredHT) {this.HLevel2 = 0;}
	    	}
	    	
	    	else if (this.HLevel > 0 && this.HeLevel2 > 0) {
	    		if (this.HLevel >= size*this.requiredHHe && this.HeLevel2 >= size*this.requiredHHe) {
		    		this.heatVar = this.heatHHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHHe/100), false); this.HLevel -= size*this.requiredHHe; this.HeLevel2 -= size*this.requiredHHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHHe;
		    	}
		    	if (this.HLevel < size*this.requiredHHe) {this.HLevel = 0;}
		    	if (this.HeLevel2 < size*this.requiredHHe) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.HLevel2 > 0) {
		    	if (this.HeLevel >= size*this.requiredHHe && this.HLevel2 >= size*this.requiredHHe) {
		    		this.heatVar = this.heatHHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHHe/100), false); this.HeLevel -= size*this.requiredHHe; this.HLevel2 -= size*this.requiredHHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHHe;
		    	 }
		    	if (this.HeLevel < size*this.requiredHHe) {this.HeLevel = 0;}
		    	if (this.HLevel2 < size*this.requiredHHe) {this.HLevel2 = 0;}
	    	}
	    	
	    	else if (this.HLevel > 0 && this.BLevel2 > 0) {
		    	if (this.HLevel >= size*this.requiredHB && this.BLevel2 >= size*this.requiredHB) {
		    		this.heatVar = this.heatHB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHB/100), false); this.HLevel -= size*this.requiredHB; this.BLevel2 -= size*this.requiredHB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHB*3;
		    	}
		    	if (this.HLevel < size*this.requiredHB) {this.HLevel = 0;}
		    	if (this.BLevel2 < size*this.requiredHB) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.HLevel2 > 0) {
		    	if (this.BLevel >= size*this.requiredHB && this.HLevel2 >= size*this.requiredHB) {
		    		this.heatVar = this.heatHB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHB/100), false); this.BLevel -= size*this.requiredHB; this.HLevel2 -= size*this.requiredHB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHB*3;
		    	}
		    	if (this.BLevel < size*this.requiredHB) {this.BLevel = 0;}
		    	if (this.HLevel2 < size*this.requiredHB) {this.HLevel2 = 0;}
	    	}
	    	
	    	else if (this.HLevel > 0 && this.Li6Level2 > 0) {
		    	if (this.HLevel >= size*this.requiredHLi6 && this.Li6Level2 >= size*this.requiredHLi6) {
		    		this.heatVar = this.heatHLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHLi6/100), false); this.HLevel -= size*this.requiredHLi6; this.Li6Level2 -= size*this.requiredHLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHLi6; this.TOut += size*this.requiredHLi6;
		    	}
		    	if (this.HLevel < size*this.requiredHLi6) {this.HLevel = 0;}
		    	if (this.Li6Level2 < size*this.requiredHLi6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.HLevel2 > 0) {
		    	if (this.Li6Level >= size*this.requiredHLi6 && this.HLevel2 >= size*this.requiredHLi6) {
		    		this.heatVar = this.heatHLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHLi6/100), false); this.Li6Level -= size*this.requiredHLi6; this.HLevel2 -= size*this.requiredHLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHLi6; this.TOut += size*this.requiredHLi6;
		    	}
		    	if (this.Li6Level < size*this.requiredHLi6) {this.Li6Level = 0;}
		    	if (this.HLevel2 < size*this.requiredHLi6) {this.HLevel2 = 0;}
	    	}
	    	
	    	else if (this.HLevel > 0 && this.Li7Level2 > 0) {
		    	if (this.HLevel >= size*this.requiredHLi7 && this.Li7Level2 >= size*this.requiredHLi7) {
		    		this.heatVar = this.heatHLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHLi7/100), false); this.HLevel -= size*this.requiredHLi7; this.Li7Level2 -= size*this.requiredHLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHLi7*2;
		    	}
		    	if (this.HLevel < size*this.requiredHLi7) {this.HLevel = 0;}
		    	if (this.Li7Level2 < size*this.requiredHLi7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.HLevel2 > 0) {
		    	if (this.Li7Level >= size*this.requiredHLi7 && this.HLevel2 >= size*this.requiredHLi7) {
		    		this.heatVar = this.heatHLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHLi7/100), false); this.Li7Level -= size*this.requiredHLi7; this.HLevel2 -= size*this.requiredHLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHLi7*2;
		    	}
		    	if (this.Li7Level < size*this.requiredHLi7) {this.Li7Level = 0;}
		    	if (this.HLevel2 < size*this.requiredHLi7) {this.HLevel2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.DLevel > 0 && this.DLevel2 > 0) {
		    	if (this.DLevel >= size*this.requiredDD && this.DLevel2 >= size*this.requiredDD) {
		    		this.heatVar = this.heatDD; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDD/100), false); this.DLevel -= size*this.requiredDD; this.DLevel2 -= size*this.requiredDD; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE3Out += size*this.requiredDD/2; this.TOut += size*this.requiredDD/2; this.nOut += size*this.requiredDD/16; this.HOut += size*this.requiredDD/2;
		    	}
		    	if (this.DLevel < size*this.requiredDD) {this.DLevel = 0;}
		    	if (this.DLevel2 < size*this.requiredDD) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.TLevel2 > 0) {
		    	if (this.DLevel >= size*this.requiredDT && this.TLevel2 >= size*this.requiredDT) {
		    		this.heatVar = this.heatDT; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDT/100), false); this.DLevel -= size*this.requiredDT; this.TLevel2 -= size*this.requiredDT; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDT; this.nOut += size*this.requiredDT/8;
		    	}
		    	if (this.DLevel < size*this.requiredDT) {this.DLevel = 0;}
		    	if (this.TLevel2 < size*this.requiredDT) {this.TLevel2 = 0;}
	    	}
	    	
	    	else if (this.TLevel > 0 && this.DLevel2 > 0) {
		    	if (this.TLevel >= size*this.requiredDT && this.DLevel2 >= size*this.requiredDT) {
		    		this.heatVar = this.heatDT; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDT/100), false); this.TLevel -= size*this.requiredDT; this.DLevel2 -= size*this.requiredDT; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDT; this.nOut += size*this.requiredDT/8;
		    	}
		    	if (this.TLevel < size*this.requiredDT) {this.TLevel = 0;}
		    	if (this.DLevel2 < size*this.requiredDT) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.HeLevel2 > 0) {
		    	if (this.DLevel >= size*this.requiredDHe && this.HeLevel2 >= size*this.requiredDHe) {
		    		this.heatVar = this.heatDHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDHe/100), false); this.DLevel -= size*this.requiredDHe; this.HeLevel2 -= size*this.requiredDHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDHe; this.HOut += size*this.requiredDHe;
		    	}
		    	if (this.DLevel < size*this.requiredDHe) {this.DLevel = 0;}
		    	if (this.HeLevel2 < size*this.requiredDHe) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.DLevel2 > 0) {
		    	if (this.HeLevel >= size*this.requiredDHe && this.DLevel2 >= size*this.requiredDHe) {
		    		this.heatVar = this.heatDHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDHe/100), false); this.HeLevel -= size*this.requiredDHe; this.DLevel2 -= size*this.requiredDHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDHe; this.HOut += size*this.requiredDHe;
		    	}
		    	if (this.HeLevel < size*this.requiredDHe) {this.HeLevel = 0;}
		    	if (this.DLevel2 < size*this.requiredDHe) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.BLevel2 > 0) {
		    	if (this.DLevel >= size*this.requiredDB && this.BLevel2 >= size*this.requiredDB) {
		    		this.heatVar = this.heatDB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDB/100), false); this.DLevel -= size*this.requiredDB; this.BLevel2 -= size*this.requiredDB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDB*3; this.nOut += size*this.requiredDB/8;
			    }
		    	if (this.DLevel < size*this.requiredDB) {this.DLevel = 0;}
		    	if (this.BLevel2 < size*this.requiredDB) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.DLevel2 > 0) {
		    	if (this.BLevel >= size*this.requiredDB && this.DLevel2 >= size*this.requiredDB) {
		    		this.heatVar = this.heatDB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDB/100), false); this.BLevel -= size*this.requiredDB; this.DLevel2 -= size*this.requiredDB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDB*3; this.nOut += size*this.requiredDB/8;
			    }
		    	if (this.BLevel < size*this.requiredDB) {this.BLevel = 0;}
		    	if (this.DLevel2 < size*this.requiredDB) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.Li6Level2 > 0) {
		    	if (this.DLevel >= size*this.requiredDLi6 && this.Li6Level2 >= size*this.requiredDLi6) {
		    		this.heatVar = this.heatDLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDLi6/100), false); this.DLevel -= size*this.requiredDLi6; this.Li6Level2 -= size*this.requiredDLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDLi6*2;
			    }
		    	if (this.DLevel < size*this.requiredDLi6) {this.DLevel = 0;}
		    	if (this.Li6Level2 < size*this.requiredDLi6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.DLevel2 > 0) {
		    	if (this.Li6Level >= size*this.requiredDLi6 && this.DLevel2 >= size*this.requiredDLi6) {
		    		this.heatVar = this.heatDLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDLi6/100), false); this.Li6Level -= size*this.requiredDLi6; this.DLevel2 -= size*this.requiredDLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDLi6*2;
			    }
		    	if (this.Li6Level < size*this.requiredDLi6) {this.Li6Level = 0;}
		    	if (this.DLevel2 < size*this.requiredDLi6) {this.DLevel2 = 0;}
	    	}
	    	
	    	else if (this.DLevel > 0 && this.Li7Level2 > 0) {
		    	if (this.DLevel >= size*this.requiredDLi7 && this.Li7Level2 >= size*this.requiredDLi7) {
		    		this.heatVar = this.heatDLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDLi7/100), false); this.DLevel -= size*this.requiredDLi7; this.Li7Level2 -= size*this.requiredDLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDLi7*2; this.nOut += size*this.requiredDLi7/8;
			    }
		    	if (this.DLevel < size*this.requiredDLi7) {this.DLevel = 0;}
		    	if (this.Li7Level2 < size*this.requiredDLi7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.DLevel2 > 0) {
		    	if (this.Li7Level >= size*this.requiredDLi7 && this.DLevel2 >= size*this.requiredDLi7) {
		    		this.heatVar = this.heatDLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerDLi7/100), false); this.Li7Level -= size*this.requiredDLi7; this.DLevel2 -= size*this.requiredDLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredDLi7*2; this.nOut += size*this.requiredDLi7/8;
			    }
		    	if (this.Li7Level < size*this.requiredDLi7) {this.Li7Level = 0;}
		    	if (this.DLevel2 < size*this.requiredDLi7) {this.DLevel2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.TLevel > 0 && this.TLevel2 > 0) {
		    	if (this.TLevel >= size*this.requiredTT && this.TLevel2 >= size*this.requiredTT) {
		    		this.heatVar = this.heatTT; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTT/100), false); this.TLevel -= size*this.requiredTT; this.TLevel2 -= size*this.requiredTT; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTT; this.nOut += size*this.requiredTT/4;
			    }
		    	if (this.TLevel < size*this.requiredTT) {this.TLevel = 0;}
		    	if (this.TLevel2 < size*this.requiredTT) {this.TLevel2 = 0;}
	    	}
	    	
	    	else if (this.TLevel > 0 && this.HeLevel2 > 0) {
		    	if (this.TLevel >= size*this.requiredTHe && this.HeLevel2 >= size*this.requiredTHe) {
		    		this.heatVar = this.heatTHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTHe/100), false); this.TLevel -= size*this.requiredTHe; this.HeLevel2 -= size*this.requiredTHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTHe; this.nOut += size*this.requiredTHe/8; this.HOut += size*this.requiredTHe;
			    }
		    	if (this.TLevel < size*this.requiredTHe) {this.TLevel = 0;}
		    	if (this.HeLevel2 < size*this.requiredTHe) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.TLevel2 > 0) {
		    	if (this.HeLevel >= size*this.requiredTHe && this.TLevel2 >= size*this.requiredTHe) {
		    		this.heatVar = this.heatTHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTHe/100), false); this.HeLevel -= size*this.requiredTHe; this.TLevel2 -= size*this.requiredTHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTHe; this.nOut += size*this.requiredTHe/8; this.HOut += size*this.requiredTHe;
			    }
		    	if (this.HeLevel < size*this.requiredTHe) {this.HeLevel = 0;}
		    	if (this.TLevel2 < size*this.requiredTHe) {this.TLevel2 = 0;}
	    	}
	    	
	    	else if (this.TLevel > 0 && this.BLevel2 > 0) {
		    	if (this.TLevel >= size*this.requiredTB && this.BLevel2 >= size*this.requiredTB) {
		    		this.heatVar = this.heatTB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTB/100), false); this.TLevel -= size*this.requiredTB; this.BLevel2 -= size*this.requiredTB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTB*3; this.nOut += size*this.requiredTB/4;
			    }
		    	if (this.TLevel < size*this.requiredTB) {this.TLevel = 0;}
		    	if (this.BLevel2 < size*this.requiredTB) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.TLevel2 > 0) {
		    	if (this.BLevel >= size*this.requiredTB && this.TLevel2 >= size*this.requiredTB) {
		    		this.heatVar = this.heatTB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTB/100), false); this.BLevel -= size*this.requiredTB; this.TLevel2 -= size*this.requiredTB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTB*3; this.nOut += size*this.requiredTB/4;
			    }
		    	if (this.BLevel < size*this.requiredTB) {this.BLevel = 0;}
		    	if (this.TLevel2 < size*this.requiredTB) {this.TLevel2 = 0;}
	    	}
	    	
	    	else if (this.TLevel > 0 && this.Li6Level2 > 0) {
		    	if (this.TLevel >= size*this.requiredTLi6 && this.Li6Level2 >= size*this.requiredTLi6) {
		    		this.heatVar = this.heatTLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTLi6/100), false); this.TLevel -= size*this.requiredTLi6; this.Li6Level2 -= size*this.requiredTLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTLi6*2; this.nOut += size*this.requiredTLi6/8;
			    }
		    	if (this.TLevel < size*this.requiredTLi6) {this.TLevel = 0;}
		    	if (this.Li6Level2 < size*this.requiredTLi6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.TLevel2 > 0) {
		    	if (this.Li6Level >= size*this.requiredTLi6 && this.TLevel2 >= size*this.requiredTLi6) {
		    		this.heatVar = this.heatTLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTLi6/100), false); this.Li6Level -= size*this.requiredTLi6; this.TLevel2 -= size*this.requiredTLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTLi6*2; this.nOut += size*this.requiredTLi6/8;
			    }
		    	if (this.Li6Level < size*this.requiredTLi6) {this.Li6Level = 0;}
		    	if (this.TLevel2 < size*this.requiredTLi6) {this.TLevel2 = 0;}
	    	}
	    	
	    	else if (this.TLevel > 0 && this.Li7Level2 > 0) {
		    	if (this.TLevel >= size*this.requiredTLi7 && this.Li7Level2 >= size*this.requiredTLi7) {
		    		this.heatVar = this.heatTLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTLi7/100), false); this.TLevel -= size*this.requiredTLi7; this.Li7Level2 -= size*this.requiredTLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTLi7*2; this.nOut += size*this.requiredTLi7/4;
			    }
		    	if (this.TLevel < size*this.requiredTLi7) {this.TLevel = 0;}
		    	if (this.Li7Level2 < size*this.requiredTLi7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.TLevel2 > 0) {
		    	if (this.Li7Level >= size*this.requiredTLi7 && this.TLevel2 >= size*this.requiredTLi7) {
		    		this.heatVar = this.heatTLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerTLi7/100), false); this.Li7Level -= size*this.requiredTLi7; this.TLevel2 -= size*this.requiredTLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredTLi7*2; this.nOut += size*this.requiredTLi7/4;
			    }
		    	if (this.Li7Level < size*this.requiredTLi7) {this.Li7Level = 0;}
		    	if (this.TLevel2 < size*this.requiredTLi7) {this.TLevel2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.HeLevel > 0 && this.HeLevel2 > 0) {
		    	if (this.HeLevel >= size*this.requiredHeHe && this.HeLevel2 >= size*this.requiredHeHe) {
		    		this.heatVar = this.heatHeHe; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeHe/100), false); this.HeLevel -= size*this.requiredHeHe; this.HeLevel2 -= size*this.requiredHeHe; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeHe; this.HOut += size*this.requiredHeHe*2;
			    }
		    	if (this.HeLevel < size*this.requiredHeHe) {this.HeLevel = 0;}
		    	if (this.HeLevel2 < size*this.requiredHeHe) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.BLevel2 > 0) {
		    	if (this.HeLevel >= size*this.requiredHeB && this.BLevel2 >= size*this.requiredHeB) {
		    		this.heatVar = this.heatHeB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeB/100), false); this.HeLevel -= size*this.requiredHeB; this.BLevel2 -= size*this.requiredHeB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeB*3; this.DOut += size*this.requiredHeB;
			    }
		    	if (this.HeLevel < size*this.requiredHeB) {this.HeLevel = 0;}
		    	if (this.BLevel2 < size*this.requiredHeB) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.HeLevel2 > 0) {
		    	if (this.BLevel >= size*this.requiredHeB && this.HeLevel2 >= size*this.requiredHeB) {
		    		this.heatVar = this.heatHeB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeB/100), false); this.BLevel -= size*this.requiredHeB; this.HeLevel2 -= size*this.requiredHeB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeB*3; this.DOut += size*this.requiredHeB;
			    }
		    	if (this.BLevel < size*this.requiredHeB) {this.BLevel = 0;}
		    	if (this.HeLevel2 < size*this.requiredHeB) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.Li6Level2 > 0) {
		    	if (this.HeLevel >= size*this.requiredHeLi6 && this.Li6Level2 >= size*this.requiredHeLi6) {
		    		this.heatVar = this.heatHeLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeLi6/100), false); this.HeLevel -= size*this.requiredHeLi6; this.Li6Level2 -= size*this.requiredHeLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeLi6*2; this.HOut += size*this.requiredHeLi6;
			    }
		    	if (this.HeLevel < size*this.requiredHeLi6) {this.HeLevel = 0;}
		    	if (this.Li6Level2 < size*this.requiredHeLi6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.HeLevel2 > 0) {
		    	if (this.Li6Level >= size*this.requiredHeLi6 && this.HeLevel2 >= size*this.requiredHeLi6) {
		    		this.heatVar = this.heatHeLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeLi6/100), false); this.Li6Level -= size*this.requiredHeLi6; this.HeLevel2 -= size*this.requiredHeLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeLi6*2; this.HOut += size*this.requiredHeLi6;
			    }
		    	if (this.Li6Level < size*this.requiredHeLi6) {this.Li6Level = 0;}
		    	if (this.HeLevel2 < size*this.requiredHeLi6) {this.HeLevel2 = 0;}
	    	}
	    	
	    	else if (this.HeLevel > 0 && this.Li7Level2 > 0) {
		    	if (this.HeLevel >= size*this.requiredHeLi7 && this.Li7Level2 >= size*this.requiredHeLi7) {
		    		this.heatVar = this.heatHeLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeLi7/100), false); this.HeLevel -= size*this.requiredHeLi7; this.Li7Level2 -= size*this.requiredHeLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeLi7*2; this.DOut += size*this.requiredHeLi7;
			    }
		    	if (this.HeLevel < size*this.requiredHeLi7) {this.HeLevel = 0;}
		    	if (this.Li7Level2 < size*this.requiredHeLi7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.HeLevel2 > 0) {
		    	if (this.Li7Level >= size*this.requiredHeLi7 && this.HeLevel2 >= size*this.requiredHeLi7) {
		    		this.heatVar = this.heatHeLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerHeLi7/100), false); this.Li7Level -= size*this.requiredHeLi7; this.HeLevel2 -= size*this.requiredHeLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredHeLi7*2; this.DOut += size*this.requiredHeLi7;
			    }
		    	if (this.Li7Level < size*this.requiredHeLi7) {this.Li7Level = 0;}
		    	if (this.HeLevel2 < size*this.requiredHeLi7) {this.HeLevel2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.BLevel > 0 && this.BLevel2 > 0) {
		    	if (this.BLevel >= size*this.requiredBB && this.BLevel2 >= size*this.requiredBB) {
		    		this.heatVar = this.heatBB; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerBB/100), false); this.BLevel -= size*this.requiredBB; this.BLevel2 -= size*this.requiredBB; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredBB*5; this.nOut += size*this.requiredBB/4;
			    }
		    	if (this.BLevel < size*this.requiredBB) {this.BLevel = 0;}
		    	if (this.BLevel2 < size*this.requiredBB) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.Li6Level2 > 0) {
		    	if (this.BLevel >= size*this.requiredBLi6 && this.Li6Level2 >= size*this.requiredBLi6) {
		    		this.heatVar = this.heatBLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerBLi6/100), false); this.BLevel -= size*this.requiredBLi6; this.Li6Level2 -= size*this.requiredBLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredBLi6*4; this.nOut += size*this.requiredBLi6/8;
			    }
		    	if (this.BLevel < size*this.requiredBLi6) {this.BLevel = 0;}
		    	if (this.Li6Level2 < size*this.requiredBLi6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.BLevel2 > 0) {
		    	if (this.Li6Level >= size*this.requiredBLi6 && this.BLevel2 >= size*this.requiredBLi6) {
		    		this.heatVar = this.heatBLi6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerBLi6/100), false); this.Li6Level -= size*this.requiredBLi6; this.BLevel2 -= size*this.requiredBLi6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredBLi6*4; this.nOut += size*this.requiredBLi6/8;
			    }
		    	if (this.Li6Level < size*this.requiredBLi6) {this.Li6Level = 0;}
		    	if (this.BLevel2 < size*this.requiredBLi6) {this.BLevel2 = 0;}
	    	}
	    	
	    	else if (this.BLevel > 0 && this.Li7Level2 > 0) {
		    	if (this.BLevel >= size*this.requiredBLi7 && this.Li7Level2 >= size*this.requiredBLi7) {
		    		this.heatVar = this.heatBLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerBLi7/100), false); this.BLevel -= size*this.requiredBLi7; this.Li7Level2 -= size*this.requiredBLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredBLi7*4; this.nOut += size*this.requiredBLi7/4;
			    }
		    	if (this.BLevel < size*this.requiredBLi7) {this.BLevel = 0;}
		    	if (this.Li7Level2 < size*this.requiredBLi7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.BLevel2 > 0) {
		    	if (this.Li7Level >= size*this.requiredBLi7 && this.BLevel2 >= size*this.requiredBLi7) {
		    		this.heatVar = this.heatBLi7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerBLi7/100), false); this.Li7Level -= size*this.requiredBLi7; this.BLevel2 -= size*this.requiredBLi7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredBLi7*4; this.nOut += size*this.requiredBLi7/4;
			    }
		    	if (this.Li7Level < size*this.requiredBLi7) {this.Li7Level = 0;}
		    	if (this.BLevel2 < size*this.requiredBLi7) {this.BLevel2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.Li6Level > 0 && this.Li6Level2 > 0) {
		    	if (this.Li6Level >= size*this.requiredLi6Li6 && this.Li6Level2 >= size*this.requiredLi6Li6) {
		    		this.heatVar = this.heatLi6Li6; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerLi6Li6/100), false); this.Li6Level -= size*this.requiredLi6Li6; this.Li6Level2 -= size*this.requiredLi6Li6; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredLi6Li6*3;
			    }
		    	if (this.Li6Level < size*this.requiredLi6Li6) {this.Li6Level = 0;}
		    	if (this.Li6Level2 < size*this.requiredLi6Li6) {this.Li6Level2 = 0;}
	    	}
	    	
	    	else if (this.Li6Level > 0 && this.Li7Level2 > 0) {
		    	if (this.Li6Level >= size*this.requiredLi6Li7 && this.Li7Level2 >= size*this.requiredLi6Li7) {
		    		this.heatVar = this.heatLi6Li7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerLi6Li7/100), false); this.Li6Level -= size*this.requiredLi6Li7; this.Li7Level2 -= size*this.requiredLi6Li7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredLi6Li7*3; this.nOut += size*this.requiredLi6Li7/8;
			    }
		    	if (this.Li6Level < size*this.requiredLi6Li7) {this.Li6Level = 0;}
		    	if (this.Li7Level2 < size*this.requiredLi6Li7) {this.Li7Level2 = 0;}
	    	}
	    	
	    	else if (this.Li7Level > 0 && this.Li6Level2 > 0) {
		    	if (this.Li7Level >= size*this.requiredLi6Li7 && this.Li6Level2 >= size*this.requiredLi6Li7) {
		    		this.heatVar = this.heatLi6Li7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerLi6Li7/100), false); this.Li7Level -= size*this.requiredLi6Li7; this.Li6Level2 -= size*this.requiredLi6Li7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredLi6Li7*3; this.nOut += size*this.requiredLi6Li7/8;
			    }
		    	if (this.Li7Level < size*this.requiredLi6Li7) {this.Li7Level = 0;}
		    	if (this.Li6Level2 < size*this.requiredLi6Li7) {this.Li6Level2 = 0;}
	    	}
	    	
		    	//
		    	
	    	else if (this.Li7Level > 0 && this.Li7Level2 > 0) {
		    	if (this.Li7Level >= size*this.requiredLi7Li7 && this.Li7Level2 >= size*this.requiredLi7Li7) {
		    		this.heatVar = this.heatLi7Li7; this.storage.receiveEnergy((int) (efficiency*pMult*size*this.powerLi7Li7/100), false); this.Li7Level -= size*this.requiredLi7Li7; this.Li7Level2 -= size*this.requiredLi7Li7; heat += (100-(0.75*efficiency))/5000; flag = true;
		    		this.HE4Out += size*this.requiredLi7Li7*3; this.nOut += size*this.requiredLi7Li7/4;
			    }
		    	if (this.Li7Level < size*this.requiredLi7Li7) {this.Li7Level = 0;}
		    	if (this.Li7Level2 < size*this.requiredLi7Li7) {this.Li7Level2 = 0;}
	    	}
	    	
		    	//
	    	
	    	else {
	    		flag = false;
	    		if (heat >= 0.5 && heat >= (0.0002*heat*Math.log(heat+Math.E)) /*&& storage.getEnergyStored() >= 20000*/) {
	    			heat = heat-(0.0002*heat*Math.log(heat+Math.E));
	    		}
	    	}
	    	E = storage.getEnergyStored();
	    	if (E != lastE) BlockFusionReactor.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
	    	
	    	} else {
	    		if (heat >= 0.5 && heat >= (0.0002*heat*Math.log(heat+Math.E)) /*&& storage.getEnergyStored() < 20000*/) {
	    			heat = heat-(0.0002*heat*Math.log(heat+Math.E));
	    		}
	    	}
	    	
	    	newE = this.storage.getEnergyStored();
          	EShown = newE-prevE;
          	prevE = newE;
          	
          	if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level == 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 == 0) {EShown = 0;}
          	
          	if (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0) {HLevel=0; DLevel=0; TLevel=0; HeLevel=0; BLevel=0; Li6Level=0; Li7Level=0;}
          	if (HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0) {HLevel2=0; DLevel2=0; TLevel2=0; HeLevel2=0; BLevel2=0; Li6Level2=0; Li7Level2=0;}
	    }

		private void addEnergy() {
				lastE = storage.getEnergyStored();
				if (heat >= 8 && HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level > 0 && HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 > 0) {
					for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
						for (int x = -1; x < 2; ++x) {
							for (int y = (0 - 2*below); y < (2 + 2*(size-1)); ++y) {
								for (int z = -1; z < 2; ++z) {
									
									TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX + x, yCoord + side.offsetY + y, zCoord + side.offsetZ + z);
					
									if (!(tile instanceof TileEntityFissionReactorGraphite) && !(tile instanceof TileEntityReactionGenerator) && !(tile instanceof TileEntityWRTG) && !(tile instanceof TileEntityRTG) && !(tile instanceof TileEntityFusionReactor))
									{
										if ((tile instanceof IEnergyHandler)) {
											storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
										}
									}
								}
							}
						}
					}
				}
				E = storage.getEnergyStored();
				if (E != lastE) {
					BlockFusionReactor.updateBlockState(this.worldObj, this.xCoord, this.yCoord, this.zCoord);
				}
		}

	    /*@SuppressWarnings("unused")
		private boolean canAddEnergy()
	    {
	        return getMain() != null ? (isMain() ? (this.storage.getEnergyStored() == 0 ? false : this.direction != "none") : getMain().canAddEnergy()) : false;
	    }*/
	    
	    public String getInventoryName()
		{
			return "Fusion Reactor";
		}
		
		public boolean isInventoryNameLocalized()
		{
			return this.localizedName != null && this.localizedName.length() > 0;
		}

	    private void fuel1() {
	    	if (getMain() != null) {
	    	if (isMain()) {
	        ItemStack stack = this.getStackInSlot(1);
	        if (stack != null && isHFuel(stack) && this.HLevel() + HFuelValue(stack) <= this.Max && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addH(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isDFuel(stack) && this.DLevel() + DFuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addD(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isTFuel(stack) && this.TLevel() + TFuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addT(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isHeFuel(stack) && this.HeLevel() + HeFuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addHe(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isBFuel(stack) && this.BLevel() + BFuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.Li6Level() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addB(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isLi6Fuel(stack) && this.Li6Level() + Li6FuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li7Level() <= 0)
	        {
	            this.addLi6(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	        else if (stack != null && isLi7Fuel(stack) && this.Li7Level() + Li7FuelValue(stack) <= this.Max && this.HLevel() <= 0 && this.DLevel() <= 0 && this.TLevel() <= 0 && this.HeLevel() <= 0 && this.BLevel() <= 0 && this.Li6Level() <= 0)
	        {
	            this.addLi7(fuelValue(stack)); --this.slots[1].stackSize;
	            if (this.slots[1].stackSize <= 0) {this.slots[1] = null;}
	        }
	    	} else {
	    		getMain().fuel1();
	    	}
	    	} else {
	    		
	    	}
	    }
	    
	    private void fuel2() {
	    	if (getMain() != null) {
	    	if (isMain()) {
	        ItemStack stack = this.getStackInSlot(0);
	        if (stack != null && isHFuel(stack) && this.HLevel2() + HFuelValue(stack) <= this.Max && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addH2(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isDFuel(stack) && this.DLevel2() + DFuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addD2(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isTFuel(stack) && this.TLevel2() + TFuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addT2(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isHeFuel(stack) && this.HeLevel2() + HeFuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addHe2(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isBFuel(stack) && this.BLevel2() + BFuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.Li6Level2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addB2(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isLi6Fuel(stack) && this.Li6Level2() + Li6FuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li7Level2() <= 0)
	        {
	            this.addLi62(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	        else if (stack != null && isLi7Fuel(stack) && this.Li7Level2() + Li7FuelValue(stack) <= this.Max && this.HLevel2() <= 0 && this.DLevel2() <= 0 && this.TLevel2() <= 0 && this.HeLevel2() <= 0 && this.BLevel2() <= 0 && this.Li6Level2() <= 0)
	        {
	            this.addLi72(fuelValue(stack)); --this.slots[0].stackSize;
	            if (this.slots[0].stackSize <= 0) {this.slots[0] = null;}
	        }
	    	} else {
	    		getMain().fuel2();
	    	}
	    	} else {
	    		
	    	}
	    }
	    
	    public static int fuelValue(ItemStack stack)
	    {
	    	if (stack == null) {return 0;}
	    	else {
	    		Item i = stack.getItem();
	    			if(i == new ItemStack (NRItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36)
	    			{
	    				return 336000;
	    			}
	    			else if(i == new ItemStack (NRItems.fuel, 1, 37).getItem() && i.getDamage(stack) == 37)
	    			{
	    				return 336000;
	    			}
	    			else if(i == new ItemStack (NRItems.fuel, 1, 38).getItem() && i.getDamage(stack) == 38)
	    			{
	    				return 336000;
	    			}
	    			else if(i == new ItemStack (NRItems.fuel, 1, 39).getItem() && i.getDamage(stack) == 39)
	    			{
	    				return 336000;
	    			}
	    			else if(i == new ItemStack (NRItems.fuel, 1, 44).getItem() && i.getDamage(stack) == 44)
	    			{
	    				return 336000;
	    			}
	    			else if(i == new ItemStack (NRItems.fuel, 1, 41).getItem() && i.getDamage(stack) == 41)
	    			{
	    				return 336000;
	    			}	
	    			else if(i == new ItemStack (NRItems.fuel, 1, 42).getItem() && i.getDamage(stack) == 42)
	    			{
	    				return 336000;
	    			}
	        }
	        return 0;
	    }

	    public static boolean isAnyFuel(ItemStack stack) {
	        return fuelValue(stack) > 0;
	    }
	    
	    public static boolean isCapsule(ItemStack stack) {
	    		if (stack.getItem() == new ItemStack(NRItems.fuel, 1, 48).getItem() && stack.getItem().getDamage(stack) == 48) {
	    			return true;
	    		}
	    	return false;
	    }

	    public static int HFuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 36).getItem() && i.getDamage(stack) == 36) {return 336000;}}
	        return 0;
	    }
	    
	    public static int DFuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 37).getItem() && i.getDamage(stack) == 37) {return 336000;}}
	        return 0;
	    }
	    
	    public static int TFuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 38).getItem() && i.getDamage(stack) == 38) {return 336000;}}
	        return 0;
	    }
	    
	    public static int HeFuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 39).getItem() && i.getDamage(stack) == 39) {return 336000;}}
	        return 0;
	    }
	    
	    public static int BFuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 44).getItem() && i.getDamage(stack) == 44) {return 336000;}}
	        return 0;
	    }
	    
	    public static int Li6FuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 41).getItem() && i.getDamage(stack) == 41) {return 336000;}}
	        return 0;
	    }
	    
	    public static int Li7FuelValue(ItemStack stack) {
	    	if (stack == null) {return 0;} else { Item i = stack.getItem();
	    		if(i == new ItemStack (NRItems.fuel, 1, 42).getItem() && i.getDamage(stack) == 42) {return 336000;}}
	        return 0;
	    }
	    
	    public void readFromNBT(NBTTagCompound nbt)
	    {
	        super.readFromNBT(nbt);

	        if (nbt.hasKey("storage"))
	        {
	            this.storage.readFromNBT(nbt.getCompoundTag("storage"));
	        }
	        this.EShown = nbt.getInteger("EShown");

	        this.direction = nbt.getString("facing");
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
	        
	        this.HOut = nbt.getDouble("HOut");
	        this.DOut = nbt.getDouble("DOut");
	        this.TOut = nbt.getDouble("TOut");
	        this.HE3Out = nbt.getDouble("HE3Out");
	        this.HE4Out = nbt.getDouble("HE4Out");
	        this.nOut = nbt.getDouble("nOut");
	        
	        this.size = nbt.getInteger("size");
	        this.isMain = nbt.getBoolean("isMain");
	        this.below = nbt.getInteger("below");
	        
	        this.efficiency = nbt.getDouble("efficiency");
	        this.heat = nbt.getDouble("heat");
	        this.heatVar = nbt.getDouble("heatVar");
	        
	        this.flag = nbt.getBoolean("flag");
	        this.flag1 = nbt.getBoolean("flag1");
	        this.lastE = nbt.getInteger("lE");
	        this.E = nbt.getInteger("E");
	        
	        NBTTagList list = nbt.getTagList("Items", 10);
	        this.slots = new ItemStack[this.getSizeInventory()];

	        for (int i = 0; i < list.tagCount(); ++i)
	        {
	            NBTTagCompound compound = list.getCompoundTagAt(i);
	            byte b = compound.getByte("Slot");

	            if (b >= 0 && b < this.slots.length)
	            {
	                this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
	            }
	        }
	    }

	    public void writeToNBT(NBTTagCompound nbt)
	    {
	        super.writeToNBT(nbt);
	        
	        NBTTagCompound energyTag = new NBTTagCompound();
	        this.storage.writeToNBT(energyTag);
	        nbt.setTag("storage", energyTag);
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
	        
	        nbt.setDouble("HOut", this.HOut);
	        nbt.setDouble("DOut", this.DOut);
	        nbt.setDouble("TOut", this.TOut);
	        nbt.setDouble("HE3Out", this.HE3Out);
	        nbt.setDouble("HE4Out", this.HE4Out);
	        nbt.setDouble("nOut", this.nOut);
	        
	        nbt.setInteger("size", this.size);
	        nbt.setBoolean("isMain", this.isMain);
	        nbt.setInteger("below", this.below);
	        
	        nbt.setDouble("efficiency", this.efficiency);
	        nbt.setDouble("heat", this.heat);
	        nbt.setDouble("heatVar", this.heatVar);
	        
	        nbt.setBoolean("flag", this.flag);
	        nbt.setBoolean("flag1", this.flag1);
	        nbt.setInteger("lE", this.lastE);
	        nbt.setInteger("E", this.E);
	        NBTTagList list = new NBTTagList();

	        for (int i = 0; i < this.slots.length; ++i)
	        {
	            if (this.slots[i] != null)
	            {
	                NBTTagCompound compound = new NBTTagCompound();
	                compound.setByte("Slot", (byte)i);
	                this.slots[i].writeToNBT(compound);
	                list.appendTag(compound);
	            }
	        }

	        nbt.setTag("Items", list);
	        
	        if(this.isInventoryNameLocalized())
			{
				nbt.setString("CustomName", this.localizedName);
			}
	    }

	    /**
	     * Overriden in a sign to provide the text.
	     */
	    public Packet getDescriptionPacket()
	    {
	        NBTTagCompound nbtTag = new NBTTagCompound();
	        nbtTag.setInteger("Energy", this.storage.getEnergyStored());
	        this.energy = nbtTag.getInteger("Energy");
	        this.writeToNBT(nbtTag);
	        return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	    }

	    public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet)
	    {
	    	super.onDataPacket(net, packet);
	        this.readFromNBT(packet.func_148857_g());
	    }
		
	    public void addH(int add) {if (isMain()) {this.HLevel += add;} else {getMain().addH(add);}}
	    public void addD(int add) {if (isMain()) {this.DLevel += add;} else {getMain().addD(add);}}
	    public void addT(int add) {if (isMain()) {this.TLevel += add;} else {getMain().addT(add);}}
	    public void addHe(int add) {if (isMain()) {this.HeLevel += add;} else {getMain().addHe(add);}}
	    public void addB(int add) {if (isMain()) {this.BLevel += add;} else {getMain().addB(add);}}
	    public void addLi6(int add) {if (isMain()) {this.Li6Level += add;} else {getMain().addLi6(add);}}
	    public void addLi7(int add) {if (isMain()) {this.Li7Level += add;} else {getMain().addLi7(add);}}
	    
	    public void addH2(int add) {if (isMain()) {this.HLevel2 += add;} else {getMain().addH2(add);}}
	    public void addD2(int add) {if (isMain()) {this.DLevel2 += add;} else {getMain().addD2(add);}}
	    public void addT2(int add) {if (isMain()) {this.TLevel2 += add;} else {getMain().addT2(add);}}
	    public void addHe2(int add) {if (isMain()) {this.HeLevel2 += add;} else {getMain().addHe2(add);}}
	    public void addB2(int add) {if (isMain()) {this.BLevel2 += add;} else {getMain().addB2(add);}}
	    public void addLi62(int add) {if (isMain()) {this.Li6Level2 += add;} else {getMain().addLi62(add);}}
	    public void addLi72(int add) {if (isMain()) {this.Li7Level2 += add;} else {getMain().addLi72(add);}}
	    
	    public void removeH(int remove) {if (isMain()) {this.HLevel -= remove;} else {getMain().removeH(remove);}}
	    public void removeD(int remove) {if (isMain()) {this.DLevel -= remove;} else {getMain().removeD(remove);}}
	    public void removeT(int remove) {if (isMain()) {this.TLevel -= remove;} else {getMain().removeT(remove);}}
	    public void removeHe(int remove) {if (isMain()) {this.HeLevel -= remove;} else {getMain().removeHe(remove);}}
	    public void removeB(int remove) {if (isMain()) {this.BLevel -= remove;} else {getMain().removeB(remove);}}
	    public void removeLi6(int remove) {if (isMain()) {this.Li6Level -= remove;} else {getMain().removeLi6(remove);}}
	    public void removeLi7(int remove) {if (isMain()) {this.Li7Level -= remove;} else {getMain().removeLi7(remove);}}
	    
	    public void removeH2(int remove) {if (isMain()) {this.HLevel2 -= remove;} else {getMain().removeH2(remove);}}
	    public void removeD2(int remove) {if (isMain()) {this.DLevel2 -= remove;} else {getMain().removeD2(remove);}}
	    public void removeT2(int remove) {if (isMain()) {this.TLevel2 -= remove;} else {getMain().removeT2(remove);}}
	    public void removeHe2(int remove) {if (isMain()) {this.HeLevel2 -= remove;} else {getMain().removeHe2(remove);}}
	    public void removeB2(int remove) {if (isMain()) {this.BLevel2 -= remove;} else {getMain().removeB2(remove);}}
	    public void removeLi62(int remove) {if (isMain()) {this.Li6Level2 -= remove;} else {getMain().removeLi62(remove);}}
	    public void removeLi72(int remove) {if (isMain()) {this.Li7Level2 -= remove;} else {getMain().removeLi72(remove);}}
	    
	    public int HLevel() {if (isMain()) {return HLevel;} else {getMain().HLevel();} return HLevel;}
	    public int DLevel() {if (isMain()) {return DLevel;} else {getMain().DLevel();} return DLevel;}
	    public int TLevel() {if (isMain()) {return TLevel;} else {getMain().TLevel();} return TLevel;}
	    public int HeLevel() {if (isMain()) {return HeLevel;} else {getMain().HeLevel();} return HeLevel;}
	    public int BLevel() {if (isMain()) {return BLevel;} else {getMain().BLevel();} return BLevel;}
	    public int Li6Level() {if (isMain()) {return Li6Level;} else {getMain().Li6Level();} return Li6Level;}
	    public int Li7Level() {if (isMain()) {return Li7Level;} else {getMain().Li7Level();} return Li7Level;}
	    
	    public int HLevel2() {if (isMain()) {return HLevel2;} else {getMain().HLevel2();} return HLevel2;}
	    public int DLevel2() {if (isMain()) {return DLevel2;} else {getMain().DLevel2();} return DLevel2;}
	    public int TLevel2() {if (isMain()) {return TLevel2;} else {getMain().TLevel2();} return TLevel2;}
	    public int HeLevel2() {if (isMain()) {return HeLevel2;} else {getMain().HeLevel2();} return HeLevel2;}
	    public int BLevel2() {if (isMain()) {return BLevel2;} else {getMain().BLevel2();} return BLevel2;}
	    public int Li6Level2() {if (isMain()) {return Li6Level2;} else {getMain().Li6Level2();} return Li6Level2;}
	    public int Li7Level2() {if (isMain()) {return Li7Level2;} else {getMain().Li7Level2();} return Li7Level2;}
	    
	    public boolean canConnectEnergy(ForgeDirection from)
	    {
	        return true;
	    }

	    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
	    {
	    	if (heat < 8 || (HLevel + DLevel + TLevel + HeLevel + BLevel + Li6Level + Li7Level <= 0 || HLevel2 + DLevel2 + TLevel2 + HeLevel2 + BLevel2 + Li6Level2 + Li7Level2 <= 0)) {
	    		return isMain() ? this.storage.receiveEnergy(maxReceive, simulate) : getMain().storage.receiveEnergy(maxReceive, simulate);
	    	}
	    	else return 0;
	    }

	    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
	    {
	    	return 0;
	    }

	    public int getEnergyStored(ForgeDirection paramForgeDirection)
	    {
	        return isMain() ? this.storage.getEnergyStored() : getMain().storage.getEnergyStored();
	    }

	    public int getMaxEnergyStored(ForgeDirection paramForgeDirection)
	    {
	        return isMain() ? this.storage.getMaxEnergyStored() : getMain().storage.getMaxEnergyStored();
	    }

	    public static boolean isHFuel(ItemStack stack) {return HFuelValue(stack) > 0;}
	    public static boolean isDFuel(ItemStack stack) {return DFuelValue(stack) > 0;}
	    public static boolean isTFuel(ItemStack stack) {return TFuelValue(stack) > 0;}
	    public static boolean isHeFuel(ItemStack stack) {return HeFuelValue(stack) > 0;}
	    public static boolean isBFuel(ItemStack stack) {return BFuelValue(stack) > 0;}
	    public static boolean isLi6Fuel(ItemStack stack) {return Li6FuelValue(stack) > 0;}
	    public static boolean isLi7Fuel(ItemStack stack) {return Li7FuelValue(stack) > 0;}
	    
	    public boolean isInputtableFuel2(ItemStack stack) {
	        if (HLevel > 0) return isHFuel(stack);
	        else if (DLevel > 0) return isDFuel(stack);
	        else if (TLevel > 0) return isTFuel(stack);
	        else if (HeLevel > 0) return isHeFuel(stack);
	        else if (BLevel > 0) return isBFuel(stack);
	        else if (Li6Level > 0) return isLi6Fuel(stack);
	        else if (Li7Level > 0) return isLi7Fuel(stack);
	        else return isAnyFuel(stack);
	    }
	    
	    public boolean isInputtableFuel1(ItemStack stack) {
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
	        if (slot == 1) {
	        	if (HLevel > 0) return isHFuel(stack);
	        	else if (DLevel > 0) return isDFuel(stack);
	        	else if (TLevel > 0) return isTFuel(stack);
	        	else if (HeLevel > 0) return isHeFuel(stack);
	        	else if (BLevel > 0) return isBFuel(stack);
	        	else if (Li6Level > 0) return isLi6Fuel(stack);
	        	else if (Li7Level > 0) return isLi7Fuel(stack);
	        	else return isAnyFuel(stack);
	        }
	        else if (slot == 0) {
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
	        return isMain() ? input : getMain().input;
	    }

	    /**
	     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
	     * side
	     */
	    public boolean canInsertItem(int slot, ItemStack stack, int par) {
	    	return isMain() ? this.isItemValidForSlot(slot, stack) : getMain().canInsertItem(slot, stack, par);
	    }

	    /**
	     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
	     * side
	     */
	    public boolean canExtractItem(int slot, ItemStack stack, int side) {
	        return isMain() ? (slot > 2) : getMain().canExtractItem(slot, stack, side);
	    }

	    public ItemStack getStackInSlot(int var1) {
	    	return isMain() ? slots[var1] : getMain().slots[var1];
		}
	    
	    public int getSizeInventory()
	    {
	        return /*isMain() ?*/ this.slots.length /*: getMain().getSizeInventory()*/;
	    }
	    
	    public void setInventorySlotContents(int i, ItemStack itemstack)
	    {
	    	if (getMain() != null) {
	    		if (isMain()) {
	    			this.slots[i] = itemstack;

	    			if (itemstack != null && itemstack.stackSize > this.getInventoryStackLimit()) {
	    				itemstack.stackSize = this.getInventoryStackLimit();
	    			}
	    			markDirty();
	    		} else {
	    			getMain().setInventorySlotContents(i, itemstack);
	    		}
	    	} else {
	    		
	    	}
	    }
}