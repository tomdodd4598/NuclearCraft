package nc.tile.accelerator;

import nc.NuclearCraft;
import nc.block.NCBlocks;
import nc.block.accelerator.BlockSynchrotron;
import nc.item.NCItems;
import nc.tile.machine.TileInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;

public class TileSynchrotron extends TileInventory implements IEnergyHandler, IEnergyReceiver, ISidedInventory {
	public int maxStorage;
	public boolean flag;
	public boolean flag1 = false;
	public int complete;
	public int energy;
	public int orientation;
	public EnergyStorage storage;
	public static int rawLength;
	public int length;
	public double fuel;
	public double radiationPower;
	public double antimatter;
	public double efficiency;
	public double particleEnergy;
	public double percentageOn;
	public static int fuelMax = 100000;
	private static double k = 8.9875517873681764; // *10^9
	private static double c = 2.99792458; // 10^8
	private static double e = 1.60217662; // 10^-19
	private static double m = 9.10938356; // 10^-31
	public String problem = StatCollector.translateToLocal("gui.ringIncomplete");
	private static final int[] slots1 = new int[] {0, 1, 2};
	private int soundCount = 0;
	private int checkCount = 0;
	
	public TileSynchrotron() {
		storage = new EnergyStorage(1000000, 1000000);
		localizedName = "Synchrotron";
		slots = new ItemStack[3];
	}
	
	/*private void place(int x, int y, int z) {
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int xc = xCoord + 2*forward.offsetX;
    	int yc = yCoord + y + 3;
    	int zc = zCoord + 2*forward.offsetZ;
    	
    	if (this.getBlockMetadata() == 4) worldObj.setBlock(xc+x, yc, zc+z, Blocks.coal_block);
    	else if (this.getBlockMetadata() == 2) worldObj.setBlock(xc-z, yc, zc+x, Blocks.iron_block);
    	else if (this.getBlockMetadata() == 5) worldObj.setBlock(xc-x, yc, zc-z, Blocks.gold_block);
    	else if (this.getBlockMetadata() == 3) worldObj.setBlock(xc+z, yc, zc-x, Blocks.diamond_block);
    }*/
	
	private void sound(int x, int y, int z) {
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int xc = xCoord + 2*forward.offsetX;
    	int yc = yCoord + y + 3;
    	int zc = zCoord + 2*forward.offsetZ;
    	
    	if (this.getBlockMetadata() == 4) {
    		worldObj.playSoundEffect(xc+x, yc, zc+z, "nc:shield2", 0.85F, 1F);
    	} else if (this.getBlockMetadata() == 2) {
    		worldObj.playSoundEffect(xc-z, yc, zc+x, "nc:shield2", 0.85F, 1F);
    	} else if (this.getBlockMetadata() == 5) {
    		worldObj.playSoundEffect(xc-x, yc, zc-z, "nc:shield2", 0.85F, 1F);
    	} else if (this.getBlockMetadata() == 3) {
    		worldObj.playSoundEffect(xc+z, yc, zc-x, "nc:shield2", 0.85F, 1F);
    	}
    }
	
	public void updateEntity() {
    	super.updateEntity();
    	if(!this.worldObj.isRemote) {
    		percentageOn();
    		fuel();
    		power();
    	}
        if (flag != flag1) {flag1 = flag; BlockSynchrotron.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);}
        
        if (soundCount >= 67) {
			if (storage.getEnergyStored() >= (int) (1000*((200-(efficiency/10000))/100)) && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && flag1 && percentageOn > 0 && fuel - length >= 0 && NuclearCraft.acceleratorSounds) {
				for (int r = 0; r <= (length-7)/7; r++) {
					if (orientation == 0) {
						sound(7*r, 0, 0);
						sound(length + 1 - 7*r, 0, length + 1);
						sound(0, 0, length + 1 - 7*r);
						sound(length + 1, 0, 7*r);
					} else if (orientation == 1) {
						sound(7*r, 0, 0);
						sound(length + 1 - 7*r, 0, -length - 1);
						sound(0, 0, -length - 1 + 7*r);
						sound(length + 1, 0, -7*r);
					}
				}
			}
			soundCount = 0;
		} else soundCount ++;
        
        markDirty();
        
        if (this.fuel < 0) this.fuel = 0;
        
        if(!this.worldObj.isRemote && checkCount >= NuclearCraft.acceleratorUpdateRate) {
        	checkRing();
        	checkCount = 0;
        } else checkCount ++;
    }
	
	private void fuel() {
		ItemStack stack = this.getStackInSlot(0);
		ItemStack stack1 = this.getStackInSlot(1);

		if (stack != null && isFuel(stack) && fuel + 10000 <= fuelMax && (stack1 == null || (stack1 != null && stack1.stackSize < 64))) {
			fuel += 10000;
			--this.slots[0].stackSize;

			if (this.slots[0].stackSize <= 0) {
				this.slots[0] = null;
			}
			
			if (this.slots[1] == null) this.slots[1] = new ItemStack(NCItems.fuel, 1, 48);
    		else if (this.slots[1].stackSize < 64) this.slots[1].stackSize ++;
		}
	}
	
	private void power() {
		if (storage.getEnergyStored() >= (int) (1000*((200-(efficiency/10000))/100)) && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && flag1 && percentageOn > 0 && fuel - length >= 0) {
			fuel = fuel - (fuel < fuelMax/2 ? length*((fuel*2)+7500)/fuelMax : length*(fuelMax+7500)/fuelMax)/4;
			this.storage.receiveEnergy(- (int) (1000*((200-(efficiency/10000))/100)), false);
			if (efficiency >= 1000000) efficiency = 1000000; else efficiency = efficiency + 5000/(length+1);
			if (length > 0) radiationPower = (fuel < fuelMax/2 ? (fuel*2)/fuelMax : 1)*(2*16*(((length*100)+1)/100)*(((length*100)+1)/100)*percentageOn*(NuclearCraft.superElectromagnetRF/100)*(efficiency/10000))/1000; // 20 kWatts per RF/t
			particleEnergy = (percentageOn/100)*(m*c*c*(1/Math.sqrt(1-Math.pow((Math.sqrt((2*Math.sqrt(6)*e*Math.pow(c, 5/2)*Math.sqrt(k)*(length*100)*(2*16*(((length*100)+1)/100)*(((length*100)+1)/100)*percentageOn*(NuclearCraft.superElectromagnetRF/100)*(efficiency/10000))*Math.pow(10, 5.5)-3*c*c*(length*100)*(length*100)*(2*16*(((length*100)+1)/100)*(((length*100)+1)/100)*percentageOn*(NuclearCraft.superElectromagnetRF/100)*(efficiency/10000))*Math.pow(10, 10))/(8*e*e*c*k*Math.pow(10, -15)-3*(length*100)*(length*100)*(2*16*(((length*100)+1)/100)*(((length*100)+1)/100)*percentageOn*(NuclearCraft.superElectromagnetRF/100)*(efficiency/10000))*Math.pow(10, -6))))/299792458, 2))-1)*Math.pow(10, 4))/(1000*e);
			
			if (antimatter < 256000000) antimatter += radiationPower/(64000/NuclearCraft.acceleratorProduction);
		} else {
			if (efficiency > 2500) efficiency = efficiency - 2500; else efficiency = 0;
			radiationPower = 0;
			particleEnergy = 0;
		}
		
		if (antimatter >= 64000000) {
			if (slots[2] == null || slots[2].stackSize == 0) {
				slots[2] = new ItemStack(NCItems.antimatter, 1);
				antimatter -= 64000000;
			}
			else if (slots[2].stackSize < 64) {
				slots[2].stackSize ++;
				antimatter -= 64000000;
			}
		}
	}
	
	/*private void power() {
		double efficiency1 = efficiency/10000;
		if (storage.getEnergyStored() >= (int) (1000*((200-efficiency1)/100)) && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && flag && percentageOn > 0 && fuel - length >= 0) {
			fuel = fuel - (fuel < fuelMax/2 ? length*((fuel*2)+7500)/fuelMax : length*(fuelMax+7500)/fuelMax)/4;
			this.storage.receiveEnergy(- (int) (1000*((200-efficiency1)/100)), false);
			if (efficiency >= 1000000) efficiency = 1000000; else efficiency = efficiency + 20;
			double radPow = (20*16*(length+1)*(length+1)*percentageOn*NuclearCraft.superElectromagnetRF*efficiency1)/10000;
			if (length > 0) radiationPower = (fuel < fuelMax/2 ? (fuel*2)/fuelMax : 1)*radPow/1000; // 20 Watts per RF/t
			int modifiedLength = length*100;
			double a1 = modifiedLength*radPow/1000;
			double a2 = modifiedLength*modifiedLength*radPow/1000;
			double a3 = 0.5;
			double a4 = length*length*radPow/1000;
			double velocitysq = (a2-a1)/(a4-a3);
			double gamma = 1 + velocitysq;
			particleEnergy = (m*c*c*(gamma-1)*Math.pow(10, 4))/(1000*e);
		} else {
			if (efficiency > 2500) efficiency = efficiency - 2500; else efficiency = 0;
			radiationPower = 0;
			particleEnergy = 0;
		}
	}*/
	
	private boolean find(Block block, int x, int y, int z) {
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int xc = xCoord + 2*forward.offsetX;
    	int yc = yCoord + y;
    	int zc = zCoord + 2*forward.offsetZ;
    	
    	if (this.getBlockMetadata() == 4) return worldObj.getBlock(xc+x, yc, zc+z) == block;
    	else if (this.getBlockMetadata() == 2) return worldObj.getBlock(xc-z, yc, zc+x) == block;
    	else if (this.getBlockMetadata() == 5) return worldObj.getBlock(xc-x, yc, zc-z) == block;
    	else if (this.getBlockMetadata() == 3) return worldObj.getBlock(xc+z, yc, zc-x) == block;
    	else return false;
    }
    
    private boolean find(Block block, Block block2, int x, int y, int z) {
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
    	int xc = xCoord + 2*forward.offsetX;
    	int yc = yCoord + y;
    	int zc = zCoord + 2*forward.offsetZ;
    	
    	if (this.getBlockMetadata() == 4) return (worldObj.getBlock(xc+x, yc, zc+z) == block || worldObj.getBlock(xc+x, yc, zc+z) == block2);
    	else if (this.getBlockMetadata() == 2) return (worldObj.getBlock(xc-z, yc, zc+x) == block || worldObj.getBlock(xc-z, yc, zc+x) == block2);
    	else if (this.getBlockMetadata() == 5) return (worldObj.getBlock(xc-x, yc, zc-z) == block || worldObj.getBlock(xc-x, yc, zc-z) == block2);
    	else if (this.getBlockMetadata() == 3) return (worldObj.getBlock(xc+z, yc, zc-x) == block || worldObj.getBlock(xc+z, yc, zc-x) == block2);
    	else return false;
    }
    
    private boolean tubef(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x,y-1,z-1) && find(c,cc,x,y+1,z-1) && find(c,cc,x,y-1,z+1) && find(c,cc,x,y+1,z+1) && find(e,ee,x,y-1,z) && find(e,ee,x,y,z+1) && find(e,ee,x,y+1,z) && find(e,ee,x,y,z-1) && find(a,x,y,z);
    }
    
    private boolean tuber(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x+1,y-1,z) && find(c,cc,x-1,y-1,z) && find(c,cc,x+1,y+1,z) && find(c,cc,x-1,y+1,z) && find(e,ee,x,y-1,z) && find(e,ee,x+1,y,z) && find(e,ee,x,y+1,z) && find(e,ee,x-1,y,z) && find(a,x,y,z);
    }
    
    private boolean tubefOn(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x,y-1,z-1) && find(cc,x,y+1,z-1) && find(cc,x,y-1,z+1) && find(cc,x,y+1,z+1) && find(ee,x,y-1,z) && find(ee,x,y,z+1) && find(ee,x,y+1,z) && find(ee,x,y,z-1) && find(a,x,y,z);
    }
    
    private boolean tuberOn(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x+1,y-1,z) && find(cc,x-1,y-1,z) && find(cc,x+1,y+1,z) && find(cc,x-1,y+1,z) && find(ee,x,y-1,z) && find(ee,x+1,y,z) && find(ee,x,y+1,z) && find(ee,x-1,y,z) && find(a,x,y,z);
    }
    
    private boolean cornertl(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x-1,y-1,z-1) && find(c,cc,x,y-1,z-1) && find(c,cc,x+1,y-1,z-1) && find(c,cc,x+1,y-1,z) && find(c,cc,x+1,y-1,z+1) &&
    	find(c,cc,x-1,y-1,z+1) && find(e,ee,x-1,y-1,z) && find(e,ee,x,y-1,z) && find(e,ee,x,y-1,z+1) &&
    	find(e,ee,x-1,y,z-1) && find(e,ee,x,y,z-1) && find(e,ee,x+1,y,z-1) && find(e,ee,x+1,y,z) && find(e,ee,x+1,y,z+1) &&
    	find(e,ee,x-1,y,z+1) && find(a,x-1,y,z) && find(a,x,y,z) && find(a,x,y,z+1) &&
    	find(c,cc,x-1,y+1,z-1) && find(c,cc,x,y+1,z-1) && find(c,cc,x+1,y+1,z-1) && find(c,cc,x+1,y+1,z) && find(c,cc,x+1,y+1,z+1) &&
    	find(c,cc,x-1,y+1,z+1) && find(e,ee,x-1,y+1,z) && find(e,ee,x,y+1,z) && find(e,ee,x,y+1,z+1);
    }
    
    private boolean cornerbl(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x+1,y-1,z-1) && find(c,cc,x,y-1,z-1) && find(c,cc,x-1,y-1,z-1) && find(c,cc,x-1,y-1,z) && find(c,cc,x-1,y-1,z+1) &&
    	find(c,cc,x+1,y-1,z+1) && find(e,ee,x+1,y-1,z) && find(e,ee,x,y-1,z) && find(e,ee,x,y-1,z+1) &&
    	find(e,ee,x+1,y,z-1) && find(e,ee,x,y,z-1) && find(e,ee,x-1,y,z-1) && find(e,ee,x-1,y,z) && find(e,ee,x-1,y,z+1) &&
    	find(e,ee,x+1,y,z+1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z+1) &&
    	find(c,cc,x+1,y+1,z-1) && find(c,cc,x,y+1,z-1) && find(c,cc,x-1,y+1,z-1) && find(c,cc,x-1,y+1,z) && find(c,cc,x-1,y+1,z+1) &&
    	find(c,cc,x+1,y+1,z+1) && find(e,ee,x+1,y+1,z) && find(e,ee,x,y+1,z) && find(e,ee,x,y+1,z+1);
    }
    
    private boolean cornertr(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x-1,y-1,z+1) && find(c,cc,x,y-1,z+1) && find(c,cc,x+1,y-1,z+1) && find(c,cc,x+1,y-1,z) && find(c,cc,x+1,y-1,z-1) &&
    	find(c,cc,x-1,y-1,z-1) && find(e,ee,x-1,y-1,z) && find(e,ee,x,y-1,z) && find(e,ee,x,y-1,z-1) &&
    	find(e,ee,x-1,y,z+1) && find(e,ee,x,y,z+1) && find(e,ee,x+1,y,z+1) && find(e,ee,x+1,y,z) && find(e,ee,x+1,y,z-1) &&
    	find(e,ee,x-1,y,z-1) && find(a,x-1,y,z) && find(a,x,y,z) && find(a,x,y,z-1) &&
    	find(c,cc,x-1,y+1,z+1) && find(c,cc,x,y+1,z+1) && find(c,cc,x+1,y+1,z+1) && find(c,cc,x+1,y+1,z) && find(c,cc,x+1,y+1,z-1) &&
    	find(c,cc,x-1,y+1,z-1) && find(e,ee,x-1,y+1,z) && find(e,ee,x,y+1,z) && find(e,ee,x,y+1,z-1);
    }
    
    private boolean cornerbr(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block c = NCBlocks.supercoolerIdle;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(c,cc,x+1,y-1,z+1) && find(c,cc,x,y-1,z+1) && find(c,cc,x-1,y-1,z+1) && find(c,cc,x-1,y-1,z) && find(c,cc,x-1,y-1,z-1) &&
    	find(c,cc,x+1,y-1,z-1) && find(e,ee,x+1,y-1,z) && find(e,ee,x,y-1,z) && find(e,ee,x,y-1,z-1) &&
    	find(e,ee,x+1,y,z+1) && find(e,ee,x,y,z+1) && find(e,ee,x-1,y,z+1) && find(e,ee,x-1,y,z) && find(e,ee,x-1,y,z-1) &&
    	find(e,ee,x+1,y,z-1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z-1) &&
    	find(c,cc,x+1,y+1,z+1) && find(c,cc,x,y+1,z+1) && find(c,cc,x-1,y+1,z+1) && find(c,cc,x-1,y+1,z) && find(c,cc,x-1,y+1,z-1) &&
    	find(c,cc,x+1,y+1,z-1) && find(e,ee,x+1,y+1,z) && find(e,ee,x,y+1,z) && find(e,ee,x,y+1,z-1);
    }
    
    private boolean cornertlOn(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x-1,y-1,z-1) && find(cc,x,y-1,z-1) && find(cc,x+1,y-1,z-1) && find(cc,x+1,y-1,z) && find(cc,x+1,y-1,z+1) &&
    	find(cc,x-1,y-1,z+1) && find(ee,x-1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z+1) &&
    	find(ee,x-1,y,z-1) && find(ee,x,y,z-1) && find(ee,x+1,y,z-1) && find(ee,x+1,y,z) && find(ee,x+1,y,z+1) &&
    	find(e,ee,x-1,y,z+1) && find(a,x-1,y,z) && find(a,x,y,z) && find(a,x,y,z+1) &&
    	find(cc,x-1,y+1,z-1) && find(cc,x,y+1,z-1) && find(cc,x+1,y+1,z-1) && find(cc,x+1,y+1,z) && find(cc,x+1,y+1,z+1) &&
    	find(cc,x-1,y+1,z+1) && find(ee,x-1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z+1);
    }
    
    private boolean cornertrOn(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x-1,y-1,z+1) && find(cc,x,y-1,z+1) && find(cc,x+1,y-1,z+1) && find(cc,x+1,y-1,z) && find(cc,x+1,y-1,z-1) &&
    	find(cc,x-1,y-1,z-1) && find(ee,x-1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z-1) &&
    	find(ee,x-1,y,z+1) && find(ee,x,y,z+1) && find(ee,x+1,y,z+1) && find(ee,x+1,y,z) && find(ee,x+1,y,z-1) &&
    	find(e,ee,x-1,y,z-1) && find(a,x-1,y,z) && find(a,x,y,z) && find(a,x,y,z-1) &&
    	find(cc,x-1,y+1,z+1) && find(cc,x,y+1,z+1) && find(cc,x+1,y+1,z+1) && find(cc,x+1,y+1,z) && find(cc,x+1,y+1,z-1) &&
    	find(cc,x-1,y+1,z-1) && find(ee,x-1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z-1);
    }
    
    private boolean cornerblOnl(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x+1,y-1,z-1) && find(cc,x,y-1,z-1) && find(cc,x-1,y-1,z-1) && find(cc,x-1,y-1,z) && find(cc,x-1,y-1,z+1) &&
    	find(cc,x+1,y-1,z+1) && find(ee,x+1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z+1) &&
    	find(ee,x+1,y,z-1) && find(ee,x,y,z-1) && find(ee,x-1,y,z-1) && find(e,ee,x-1,y,z) && find(ee,x-1,y,z+1) &&
    	find(e,ee,x+1,y,z+1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z+1) &&
    	find(cc,x+1,y+1,z-1) && find(cc,x,y+1,z-1) && find(cc,x-1,y+1,z-1) && find(cc,x-1,y+1,z) && find(cc,x-1,y+1,z+1) &&
    	find(cc,x+1,y+1,z+1) && find(ee,x+1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z+1);
    }
    
    private boolean cornerbrOnl(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x+1,y-1,z+1) && find(cc,x,y-1,z+1) && find(cc,x-1,y-1,z+1) && find(cc,x-1,y-1,z) && find(cc,x-1,y-1,z-1) &&
    	find(cc,x+1,y-1,z-1) && find(ee,x+1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z-1) &&
    	find(ee,x+1,y,z+1) && find(ee,x,y,z+1) && find(ee,x-1,y,z+1) && find(ee,x-1,y,z) && find(ee,x-1,y,z-1) &&
    	find(e,ee,x+1,y,z-1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z-1) &&
    	find(cc,x+1,y+1,z+1) && find(cc,x,y+1,z+1) && find(cc,x-1,y+1,z+1) && find(cc,x-1,y+1,z) && find(cc,x-1,y+1,z-1) &&
    	find(cc,x+1,y+1,z-1) && find(ee,x+1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z-1);
    }
    
    private boolean cornerblOnr(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x+1,y-1,z-1) && find(cc,x,y-1,z-1) && find(cc,x-1,y-1,z-1) && find(cc,x-1,y-1,z) && find(cc,x-1,y-1,z+1) &&
    	find(cc,x+1,y-1,z+1) && find(ee,x+1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z+1) &&
    	find(ee,x+1,y,z-1) && find(ee,x,y,z-1) && find(ee,x-1,y,z-1) && find(ee,x-1,y,z) && find(ee,x-1,y,z+1) &&
    	find(e,ee,x+1,y,z+1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z+1) &&
    	find(cc,x+1,y+1,z-1) && find(cc,x,y+1,z-1) && find(cc,x-1,y+1,z-1) && find(cc,x-1,y+1,z) && find(cc,x-1,y+1,z+1) &&
    	find(cc,x+1,y+1,z+1) && find(ee,x+1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z+1);
    }
    
    private boolean cornerbrOnr(int x, int y, int z) {
    	Block a = Blocks.air;
    	Block e = NCBlocks.superElectromagnetIdle;
    	Block ee = NCBlocks.superElectromagnetActive;
    	Block cc = NCBlocks.supercoolerActive;
    	return find(cc,x+1,y-1,z+1) && find(cc,x,y-1,z+1) && find(cc,x-1,y-1,z+1) && find(cc,x-1,y-1,z) && find(cc,x-1,y-1,z-1) &&
    	find(cc,x+1,y-1,z-1) && find(ee,x+1,y-1,z) && find(ee,x,y-1,z) && find(ee,x,y-1,z-1) &&
    	find(ee,x+1,y,z+1) && find(ee,x,y,z+1) && find(ee,x-1,y,z+1) && find(e,ee,x-1,y,z) && find(ee,x-1,y,z-1) &&
    	find(e,ee,x+1,y,z-1) && find(a,x+1,y,z) && find(a,x,y,z) && find(a,x,y,z-1) &&
    	find(cc,x+1,y+1,z+1) && find(cc,x,y+1,z+1) && find(cc,x-1,y+1,z+1) && find(cc,x-1,y+1,z) && find(cc,x-1,y+1,z-1) &&
    	find(cc,x+1,y+1,z-1) && find(ee,x+1,y+1,z) && find(ee,x,y+1,z) && find(ee,x,y+1,z-1);
    }
    
    public int lengthl() {
    	int l = 0;
    	for (int i = 0; i < NuclearCraft.ringMaxSize; i++) {
    		if (tubef(1+i,0,0) && cornertl(2+i,0,0)) {
    			l = i+1; break;
    		}
    	}
    	length = l;
    	return l;
    }
    
    public int lengthr() {
    	int l = 0;
    	for (int i = 0; i < NuclearCraft.ringMaxSize; i++) {
    		if (tubef(1+i,0,0) && cornertr(2+i,0,0)) {
    			l = i+1; break;
    		}
    	}
    	length = l;
    	return l;
    }
    
	private boolean checkRing() {
		Block s = NCBlocks.synchrotronIdle;
		Block ss = NCBlocks.synchrotronActive;
		int l = 0;
		
		if (cornerbl(0,0,0)) {
			orientation = 0;
			l = lengthl();
			if (l < 1) {
				this.problem = StatCollector.translateToLocal("gui.ringNotBigEnough"); flag = false; complete = 0; return false;
			}
			if (l > 2) for (int i = 0; i < l-2; i++) {
				if(!tubef(2+i,0,0) || !tuber(l+1,0,2+i) || !tubef(2+i,0,l+1) || !tuber(0,0,2+i)) {
					this.problem = StatCollector.translateToLocal("gui.tubeIncomplete"); flag = false; complete = 0; return false;
				}
			}
			if(!cornerbl(0,0,0) || !cornertl(1+l,0,0) || !cornertr(1+l,0,1+l) || !cornerbr(0,0,1+l)) {
				this.problem = StatCollector.translateToLocal("gui.cornerIncomplete"); flag = false; complete = 0; return false;
			}
			if(find(s,ss,0,0,-2) || find(s,ss,l+1,0,-2) || find(s,ss,l+3,0,0) || find(s,ss,l+3,0,l+1) || find(s,ss,l+1,0,l+3) || find(s,ss,0,0,l+3) || find(s,ss,-2,0,l+1)) {
				this.problem = StatCollector.translateToLocal("gui.multipleControllers"); flag = false; complete = 0; return false;
			}
		} else if (cornerbr(0,0,0)) {
			orientation = 1;
			l = lengthr();
			if (l < 1) {
				this.problem = StatCollector.translateToLocal("gui.ringNotBigEnough"); flag = false; complete = 0; return false;
			}
			if (l > 2) for (int i = 0; i < l-2; i++) {
				if(!tubef(2+i,0,0) || !tuber(l+1,0,-(2+i)) || !tubef(2+i,0,-(l+1)) || !tuber(0,0,-(2+i))) {
					this.problem = StatCollector.translateToLocal("gui.tubeIncomplete"); flag = false; complete = 0; return false;
				}
			}
			if(!cornerbr(0,0,0) || !cornertr(1+l,0,0) || !cornertl(1+l,0,-(1+l)) || !cornerbl(0,0,-(1+l))) {
				this.problem = StatCollector.translateToLocal("gui.cornerIncomplete"); flag = false; complete = 0; return false;
			}
			if(find(s,ss,0,0,-(-2)) || find(s,ss,l+1,0,-(-2)) || find(s,ss,l+3,0,0) || find(s,ss,l+3,0,-(l+1)) || find(s,ss,l+1,0,-(l+3)) || find(s,ss,0,0,-(l+3)) || find(s,ss,-2,0,-(l+1))) {
				this.problem = StatCollector.translateToLocal("gui.multipleControllers"); flag = false; complete = 0; return false;
			}
		} else {
			this.problem = StatCollector.translateToLocal("gui.ringIncomplete"); flag = false; complete = 0; return false;
		}
		flag = true; complete = 1; return true;
    }
	
	public boolean multiblock(World world, int x, int y, int z) {
    	return complete == 1;
    }

	public boolean multiblockstring() {
    	return complete == 1;
    }
	
	public boolean percentageOn() {
		int l = 0;
		int e = 0;
		
		if(complete == 0) {
			percentageOn = 0; return false;
		} else {
			if (cornerbl(0,0,0)) {
				l = lengthl();
				if(!cornerblOnl(0,0,0) || !cornertlOn(1+l,0,0) || !cornertrOn(1+l,0,1+l) || !cornerbrOnl(0,0,1+l)) {
					percentageOn = 0; return false;
				}
				if (l > 2) {
					for (int i = 0; i < l-2; i++) if(tubefOn(2+i,0,0)) e++;
					for (int i = 0; i < l-2; i++) if(tuberOn(l+1,0,2+i)) e++;
					for (int i = 0; i < l-2; i++) if(tubefOn(2+i,0,l+1)) e++;
					for (int i = 0; i < l-2; i++) if(tuberOn(0,0,2+i)) e++;
				} else {
					percentageOn = 100; return true;
				}
			} else if (cornerbr(0,0,0)) {
				l = lengthr();
				if(!cornerbrOnr(0,0,0) || !cornertrOn(1+l,0,0) || !cornertlOn(1+l,0,-(1+l)) || !cornerblOnr(0,0,-(1+l))) {
					percentageOn = 0; return false;
				}
				if (l > 2) {
					for (int i = 0; i < l-2; i++) if(tubefOn(2+i,0,0)) e++;
					for (int i = 0; i < l-2; i++) if(tuberOn(l+1,0,-(2+i))) e++;
					for (int i = 0; i < l-2; i++) if(tubefOn(2+i,0,-(l+1))) e++;
					for (int i = 0; i < l-2; i++) if(tuberOn(0,0,-(2+i))) e++;
				} else {
					percentageOn = 100; return true;
				}
			} else {
				percentageOn = 0; return false;
			}
			percentageOn = (400+100*e)/(4*(l-1)); return true;
		}
	}
    
    public String getInventoryName() {
        return this.isInventoryNameLocalized() ? this.localizedName : "Synchrotron";
    }
	
	public boolean isInventoryNameLocalized() {
        return this.localizedName != null && this.localizedName.length() > 0;
    }
	
	public int getSizeInventory() {
		return this.slots.length;
	}
	
	public void setGuiDisplayName(String name) {
        this.localizedName = name;
    }
	
	public int getInventoryStackLimit() {
        return 64;
    }
	
	public boolean isUseableByPlayer(EntityPlayer entityplayer) {
        return this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord) == this;
    }
    
    public boolean hasCustomInventoryName() {
		return false;
	}
    
    public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		if (nbt.hasKey("storage")) {
			this.storage.readFromNBT(nbt.getCompoundTag("storage"));
		}
		this.flag = nbt.getBoolean("flag");
		this.flag1 = nbt.getBoolean("flag1");
		this.complete = nbt.getInteger("complete");
		this.length = nbt.getInteger("length");
		this.orientation = nbt.getInteger("orientation");
		this.fuel = nbt.getDouble("fuel");
		this.efficiency = nbt.getDouble("efficiency");
		this.radiationPower = nbt.getDouble("radiationPower");
		this.antimatter = nbt.getDouble("antimatter");
		this.particleEnergy = nbt.getDouble("particleEnergy");
		this.percentageOn = nbt.getDouble("percentageOn");
		this.problem = nbt.getString("problem");
		NBTTagList list = nbt.getTagList("Items", 10);
		this.slots = new ItemStack[this.getSizeInventory()];

		for (int i = 0; i < list.tagCount(); ++i) {
			NBTTagCompound compound = list.getCompoundTagAt(i);
			byte b = compound.getByte("Slot");

			if (b >= 0 && b < this.slots.length) {
				this.slots[b] = ItemStack.loadItemStackFromNBT(compound);
			}
		}
	}

    public void writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagCompound energyTag = new NBTTagCompound();
		this.storage.writeToNBT(energyTag);
		nbt.setTag("storage", energyTag);
		nbt.setBoolean("flag", this.flag);
		nbt.setBoolean("flag1", this.flag1);
		nbt.setInteger("complete", this.complete);
		nbt.setInteger("length", this.length);
		nbt.setInteger("orientation", this.orientation);
		nbt.setDouble("fuel", this.fuel);
		nbt.setDouble("efficiency", this.efficiency);
		nbt.setDouble("radiationPower", this.radiationPower);
		nbt.setDouble("antimatter", this.antimatter);
		nbt.setDouble("particleEnergy", this.particleEnergy);
		nbt.setDouble("percentageOn", this.percentageOn);
		nbt.setString("problem", this.problem);
		NBTTagList list = new NBTTagList();

		for (int i = 0; i < this.slots.length; ++i) {
			if (this.slots[i] != null) {
				NBTTagCompound compound = new NBTTagCompound();
				compound.setByte("Slot", (byte)i);
				this.slots[i].writeToNBT(compound);
				list.appendTag(compound);
			}
		}

		nbt.setTag("Items", list);
		
		if(this.isInventoryNameLocalized()) {
			nbt.setString("CustomName", this.localizedName);
		}
	}

	public Packet getDescriptionPacket() {
		NBTTagCompound nbtTag = new NBTTagCompound();
		nbtTag.setInteger("Energy", this.storage.getEnergyStored());
		this.energy = nbtTag.getInteger("Energy");
		this.writeToNBT(nbtTag);
		return new S35PacketUpdateTileEntity(this.xCoord, this.yCoord, this.zCoord, 0, nbtTag);
	}

	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity packet) {
		super.onDataPacket(net, packet);
		this.readFromNBT(packet.func_148857_g());
	}
	
	public static boolean isFuel(ItemStack stack) {
		if (stack == null) return false;
        Item item = stack.getItem();
        return item == new ItemStack (NCItems.fuel, 1, 50).getItem() && item.getDamage(stack) == 50;
    }

	public boolean canConnectEnergy(ForgeDirection from) {
		return true;
	}

	public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate) {
		return this.storage.receiveEnergy(maxReceive, simulate);
	}

	public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate) {
		return 0;
	}

	public int getEnergyStored(ForgeDirection paramForgeDirection) {
		return this.storage.getEnergyStored();
	}

	public int getMaxEnergyStored(ForgeDirection paramForgeDirection) {
		return this.storage.getMaxEnergyStored();
	}

	public int[] getAccessibleSlotsFromSide(int slot) {
		return slots1;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return this.isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		return slot != 0;
	}
	
	public boolean isItemValidForSlot(int slot, ItemStack stack) {
        if (slot == 0) return isFuel(stack);
        else return false;
    }
}