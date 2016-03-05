package nc.tile.accelerator;

import nc.NuclearCraft;
import nc.block.accelerator.BlockSynchrotron;
import nc.block.basic.NCBlocks;
import nc.item.NCItems;
import nc.tile.machine.TileInventory;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
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
	public int energy;
	public EnergyStorage storage;
	public static int rawLength;
	public int length;
	public double fuel;
	public double radiationPower;
	public double efficiency;
	public double particleEnergy;
	public double percentageOn;
	public String problem = StatCollector.translateToLocal("gui.ringIncomplete");
	private static final int[] slotsTop = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {0, 1};
	
	public TileSynchrotron() {
		storage = new EnergyStorage(1000000, 1000000);
		localizedName = "Synchrotron";
		slots = new ItemStack[2];
	}
	
	public void updateEntity() {
    	super.updateEntity();
    	if(!this.worldObj.isRemote) {
    		percentageOn();
    	}
        if (flag != flag1) {flag1 = flag; BlockSynchrotron.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);}
        markDirty();
        if (this.fuel < 0) {this.fuel = 0;}
        if(!this.worldObj.isRemote) {
    		checkRing();
    	}
    }
	
	public boolean multiblock(World world, int x, int y, int z) {
    	return checkRing();
    }

	public boolean multiblockstring() {
    	return checkRing();
    }
    
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
			l = lengthl();
			if (l < 1) {
				flag = false; return false;
			}
			if (l > 2) for (int i = 0; i < l-2; i++) {
				if(!tubef(2+i,0,0) || !tuber(l+1,0,2+i) || !tubef(2+i,0,l+1) || !tuber(0,0,2+i)) {
					flag = false; return false;
				}
			}
			if(!cornerbl(0,0,0) || !cornertl(1+l,0,0) || !cornertr(1+l,0,1+l) || !cornerbr(0,0,1+l)) {
				flag = false; return false;
			}
			if(find(s,ss,0,0,-2) || find(s,ss,l+1,0,-2) || find(s,ss,l+3,0,0) || find(s,ss,l+3,0,l+1) || find(s,ss,l+1,0,l+3) || find(s,ss,0,0,l+3) || find(s,ss,-2,0,l+1)) {
				flag = false; return false;
			}
		} else if (cornerbr(0,0,0)) {
			l = lengthr();
			if (l < 1) {
				flag = false; return false;
			}
			if (l > 2) for (int i = 0; i < l-2; i++) {
				if(!tubef(2+i,0,0) || !tuber(l+1,0,-(2+i)) || !tubef(2+i,0,-(l+1)) || !tuber(0,0,-(2+i))) {
					flag = false; return false;
				}
			}
			if(!cornerbr(0,0,0) || !cornertr(1+l,0,0) || !cornertl(1+l,0,-(1+l)) || !cornerbl(0,0,-(1+l))) {
				flag = false; return false;
			}
			if(find(s,ss,0,0,-(-2)) || find(s,ss,l+1,0,-(-2)) || find(s,ss,l+3,0,0) || find(s,ss,l+3,0,-(l+1)) || find(s,ss,l+1,0,-(l+3)) || find(s,ss,0,0,-(l+3)) || find(s,ss,-2,0,-(l+1))) {
				flag = false; return false;
			}
		} else {
			flag = false; return false;
		}
		flag = true; return true;
    }
	
	public boolean percentageOn() {
		int l = 0;
		int e = 0;
		
		if(!checkRing()) {
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
		this.length = nbt.getInteger("length");
		this.fuel = nbt.getDouble("fuel");
		this.efficiency = nbt.getDouble("efficiency");
		this.radiationPower = nbt.getDouble("radiationPower");
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
		nbt.setInteger("length", this.length);
		nbt.setDouble("fuel", this.fuel);
		nbt.setDouble("efficiency", this.efficiency);
		nbt.setDouble("radiationPower", this.radiationPower);
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
	
	public static boolean isFuel(ItemStack stack)
    {
        return stack.getItem() == NCItems.fuel && stack.getItemDamage() == 50;
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

	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return slot == 0;
	}

	public int[] getAccessibleSlotsFromSide(int slot) {
		return slot == 0 ? slotsSides : slotsTop;
	}

	public boolean canInsertItem(int slot, ItemStack stack, int par) {
		return this.isItemValidForSlot(slot, stack);
	}

	public boolean canExtractItem(int slot, ItemStack stack, int slots) {
		return slot == 1;
	}
}