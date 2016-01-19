package com.nr.mod.blocks.tileentities;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import cofh.api.energy.EnergyStorage;
import cofh.api.energy.IEnergyConnection;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import cofh.lib.util.helpers.EnergyHelper;

import com.nr.mod.NuclearRelativistics;
import com.nr.mod.blocks.NRBlocks;
import com.nr.mod.handlers.BombType;
import com.nr.mod.handlers.EntityBomb;
import com.nr.mod.handlers.NRExplosion;
import com.nr.mod.items.NRItems;

public class TileEntityFissionReactorGraphite extends TileEntityInventory implements IEnergyHandler, IEnergyConnection, ISidedInventory, IEnergyReceiver {
    public EnergyStorage storage = new EnergyStorage(1000000, 1000000);
    
    public int rawR = 1;
    
    public int R = rawR; 
    
    public int D = R*2 + 1;
    public int SR = R-1; 
    public int SD = SR*2 + 1;
    
    public boolean flag;
    public boolean flag1 = false;
    public int off = 0;
    
    public int E;
    public int H;
    public int energy;
    public int fueltime;
	public int fueltype;
    public static int power = NuclearRelativistics.fissionRF/10;
    public int heat;
    public String typeoffuel = StatCollector.translateToLocal("gui.noFuel");
    public int fuelmult;
    public int MBNumber;
    public String problem = StatCollector.translateToLocal("gui.casingIncomplete");
    
    public int maxTransfer = 1000000;
    public String direction;
    private static final int[] slotsTop = new int[] {0, 1};
    private static final int[] slotsSides = new int[] {0, 1};

    public TileEntityFissionReactorGraphite()
    {
        super.slots = new ItemStack[3];
        super.localizedName = "Fission Reactor";
    }

    public void updateEntity()
    {
    	super.updateEntity();
    	if(!this.worldObj.isRemote)
    	{
    		product();
    		fuel();
    		energy();
    		overheat(worldObj, this.xCoord, this.yCoord, this.zCoord, 15 + 5*R, BombType.BOMB_STANDARD);
    		typeoffuelx();
    		addEnergy();
    	}
    	upgrade();
        if (flag != flag1) {flag1 = flag; BlockFissionReactorGraphite.updateBlockState(flag, this.worldObj, this.xCoord, this.yCoord, this.zCoord);}
        markDirty();
        if (this.fueltime < 0) {this.fueltime = 0;}
    }
    
    public void overheat(World world, double x, double y, double z, float radius, BombType type)
    {
    	if (this.heat > 1000000) {
    		if (NuclearRelativistics.nuclearMeltdowns) {
	    		if (this.MBNumber == 3) {
	    			NRExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord, (double)this.yCoord + R, (double)this.zCoord - R, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 4) {
	    			NRExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord + R, (double)this.yCoord + R, (double)this.zCoord, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 2) {
	    			NRExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord, (double)this.yCoord + R, (double)this.zCoord + R, 6 + 4*R, 1000F, true);
	    		} else if (this.MBNumber == 5) {
	    			NRExplosion.createExplosion(new EntityBomb(world).setType(type), world, (double)this.xCoord - R, (double)this.yCoord + R, (double)this.zCoord, 6 + 4*R, 1000F, true);
	    		}
    		}
    		else this.heat = 1000000;
    	}
    }
    
    public void typeoffuelx()
    {
    	if (this.fueltype == 1) { typeoffuel = StatCollector.translateToLocal("gui.LEU"); fuelmult = 2; }
    	else if (this.fueltype == 2) { typeoffuel = StatCollector.translateToLocal("gui.HEU"); fuelmult = 4; }
    	else if (this.fueltype == 3) { typeoffuel = StatCollector.translateToLocal("gui.LEP"); fuelmult = 4; }
    	else if (this.fueltype == 4) { typeoffuel = StatCollector.translateToLocal("gui.HEP"); fuelmult = 8; }
    	else if (this.fueltype == 5) { typeoffuel = StatCollector.translateToLocal("gui.MOX"); fuelmult = 3; }
    	else if (this.fueltype == 6) { typeoffuel = StatCollector.translateToLocal("gui.TBU"); fuelmult = 1; }
    	else if (this.fueltype == 7) { typeoffuel = StatCollector.translateToLocal("gui.LEU"); fuelmult = 2; }
    	else if (this.fueltype == 8) { typeoffuel = StatCollector.translateToLocal("gui.HEU"); fuelmult = 4; }
    	else if (this.fueltype == 9) { typeoffuel = StatCollector.translateToLocal("gui.LEP"); fuelmult = 4; }
    	else if (this.fueltype == 10) { typeoffuel = StatCollector.translateToLocal("gui.HEP"); fuelmult = 8; }
    	else if (this.fueltype == 11) { typeoffuel = StatCollector.translateToLocal("gui.MOX"); fuelmult = 3; }
    	
    	else if (this.fueltype == 12) { typeoffuel = StatCollector.translateToLocal("gui.LEU-Ox"); fuelmult = 2; }
    	else if (this.fueltype == 13) { typeoffuel = StatCollector.translateToLocal("gui.HEU-Ox"); fuelmult = 4; }
    	else if (this.fueltype == 14) { typeoffuel = StatCollector.translateToLocal("gui.LEP-Ox"); fuelmult = 4; }
    	else if (this.fueltype == 15) { typeoffuel = StatCollector.translateToLocal("gui.HEP-Ox"); fuelmult = 8; }
    	else if (this.fueltype == 16) { typeoffuel = StatCollector.translateToLocal("gui.LEU-Ox"); fuelmult = 2; }
    	else if (this.fueltype == 17) { typeoffuel = StatCollector.translateToLocal("gui.HEU-Ox"); fuelmult = 4; }
    	else if (this.fueltype == 18) { typeoffuel = StatCollector.translateToLocal("gui.LEP-Ox"); fuelmult = 4; }
    	else if (this.fueltype == 19) { typeoffuel = StatCollector.translateToLocal("gui.HEP-Ox"); fuelmult = 8; }
    	
    	else if (this.fueltype == 0) { typeoffuel = StatCollector.translateToLocal("gui.noFuel"); fuelmult = 0; }
    	else { typeoffuel = StatCollector.translateToLocal("gui.noFuel"); fuelmult = 0; }
    }
    
    private void energyFuelHeat(int energy, int fuel, int heat) {
    	this.storage.receiveEnergy((int) (energy*power*R*R), false);
        this.fueltime -= R*fuel/NuclearRelativistics.fissionEfficiency;
        this.heat += heat*R;
    }

    private void energy()
    {
    	int prevE = this.storage.getEnergyStored();
    	int newE;
    	
    	int prevH = this.heat;
    	int newH;
    	
    	ForgeDirection forward = ForgeDirection.getOrientation(this.getBlockMetadata()).getOpposite();
          	  int x = xCoord + R * forward.offsetX;
          	  int y = yCoord;
          	  int z = zCoord + R * forward.offsetZ;
    	
        if (this.getStackInSlot(1) == null && worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.fueltime > 0 && this.fueltype != 0 && this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	
        	flag = true;
        	off = 0;
        	
        	//LEU
        	if (this.fueltype == 1 || this.fueltype == 7) energyFuelHeat(15, 15000, 28);

        	//HEU
        	if (this.fueltype == 2 || this.fueltype == 8) energyFuelHeat(68, 15000, 280);

        	//LEP
        	if (this.fueltype == 3 || this.fueltype == 9) energyFuelHeat(30, 28800, 84);

        	//HEP
        	if (this.fueltype == 4 || this.fueltype == 10) energyFuelHeat(135, 28800, 840);

        	//MOX
        	if (this.fueltype == 5 || this.fueltype == 11) energyFuelHeat(33, 20000, 90);
        	
        	//TBU
        	if (this.fueltype == 6) energyFuelHeat(4, 3750, 4);
        	
        	//LEU-Ox
        	if (this.fueltype == 12 || this.fueltype == 16) energyFuelHeat(23, 15000, 35);

        	//HEU-Ox
        	if (this.fueltype == 13 || this.fueltype == 17) energyFuelHeat(101, 15000, 350);

        	//LEP-Ox
        	if (this.fueltype == 14 || this.fueltype == 18) energyFuelHeat(45, 28800, 105);

        	//HEP-Ox
        	if (this.fueltype == 15 || this.fueltype == 19) energyFuelHeat(203, 28800, 1050);
        	
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.graphiteBlock){
        					this.storage.receiveEnergy((power*fuelmult*fuelmult*R)/10, false); this.heat += 4*fuelmult*R; }
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.lava){
        					this.storage.receiveEnergy((power*fuelmult*fuelmult*R)/50, false); this.heat += 8*fuelmult*R; }
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.speedBlock){
        					this.fueltime -= fuelmult*5000/NuclearRelativistics.fissionEfficiency; }
        			}}}
        	
        } else if(this.getStackInSlot(1) == null && !worldObj.isBlockIndirectlyGettingPowered(this.xCoord, this.yCoord, this.zCoord) && this.fueltime > 0 && this.fueltype != 0 && this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	
        	off = 1;
        	
        	//LEU
        	if (this.fueltype == 1 || this.fueltype == 7) energyFuelHeat(15, 0, 28);

        	//HEU
        	if (this.fueltype == 2 || this.fueltype == 8) energyFuelHeat(68, 0, 280);

        	//LEP
        	if (this.fueltype == 3 || this.fueltype == 9) energyFuelHeat(30, 0, 84);

        	//HEP
        	if (this.fueltype == 4 || this.fueltype == 10) energyFuelHeat(135, 0, 840);

        	//MOX
        	if (this.fueltype == 5 || this.fueltype == 11) energyFuelHeat(33, 0, 90);
        	
        	//TBU
        	if (this.fueltype == 6) energyFuelHeat(4, 0, 4);
        	
        	//LEU-Ox
        	if (this.fueltype == 12 || this.fueltype == 16) energyFuelHeat(23, 0, 35);

        	//HEU-Ox
        	if (this.fueltype == 13 || this.fueltype == 17) energyFuelHeat(101, 0, 350);

        	//LEP-Ox
        	if (this.fueltype == 14 || this.fueltype == 18) energyFuelHeat(45, 0, 105);

        	//HEP-Ox
        	if (this.fueltype == 15 || this.fueltype == 19) energyFuelHeat(203, 0, 1050);
        	
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.graphiteBlock){
        					this.storage.receiveEnergy((power*fuelmult*fuelmult*R)/10, false); this.heat += 4*fuelmult*R; }
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.lava){
        					this.storage.receiveEnergy((power*fuelmult*fuelmult*R)/50, false); this.heat += 8*fuelmult*R; }
        			}}}
        	flag = false;
        } else {
        	flag = false;
        	off = 0;
        }
          	
        if (this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
        	for (int Z4 = -SR; Z4 <= SR; Z4++) {
        		for (int X4 = -SR; X4 <= SR; X4++) {
        			for (int Y4 = 1; Y4 <= SD; Y4++) {
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.coolerBlock){
        					if (this.heat > 64-1) { this.heat -= 64; } else { this.heat = 0; } }
        				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.water){
        					if (this.heat > 10-1) { this.heat -= 10; } else { this.heat = 0; } }
        			}}}
        	if (R == rawR) {
        		if (this.heat > 8-1) { this.heat -= 8; } else { this.heat = 0; }
        	}
        }
        
          	newH = this.heat;
          	H = newH-prevH;
          	if (off==1) this.heat = prevH;
          	prevH = newH;
          	
          	if (off==1 && this.multiblock(this.worldObj, this.xCoord, this.yCoord, this.zCoord)) {
            	for (int Z4 = -SR; Z4 <= SR; Z4++) {
            		for (int X4 = -SR; X4 <= SR; X4++) {
            			for (int Y4 = 1; Y4 <= SD; Y4++) {
            				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.coolerBlock){
            					if (this.heat > 64-1) { this.heat -= 64; } else { this.heat = 0; } }
            				if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==Blocks.water){
            					if (this.heat > 10-1) { this.heat -= 10; } else { this.heat = 0; } }
            			}}}
            	if (R == rawR) {
            		if (this.heat > 8-1) { this.heat -= 8; } else { this.heat = 0; }
            	}
            }
          	
          	newE = this.storage.getEnergyStored();
          	E = newE-prevE;
          	if (off==1) this.storage.receiveEnergy(-E, false);
          	prevE = newE;
          	
          	if (!NuclearRelativistics.nuclearMeltdowns) { this.heat = 0; }
          	
          	if (this.fueltime == 0) { E = 0; }
	}
    
    public boolean multiblock(World world, int x, int y, int z) {
    	Block block = this.blockType;
    	@SuppressWarnings("unused")
    	BlockFissionReactorGraphite reactorGraphite = (BlockFissionReactorGraphite)block;
    	if (checkReactor()) {
    		return true;
    	} return false;
    }

    public boolean multiblockstring() {
    	if (multiblock(getWorldObj(), this.xCoord, this.yCoord, this.zCoord)) {
    		return true;
    	} return false;
    }

    private void addEnergy() {
		for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
			TileEntity tile = this.worldObj.getTileEntity(xCoord + side.offsetX, yCoord + side.offsetY, zCoord + side.offsetZ);
			
			if (!(tile instanceof TileEntityFissionReactorGraphite) && !(tile instanceof TileEntityReactionGenerator) && !(tile instanceof TileEntityRTG) && !(tile instanceof TileEntityWRTG) && !(tile instanceof TileEntityFusionReactor))
			{
				if ((tile instanceof IEnergyHandler)) {
					storage.extractEnergy(((IEnergyHandler)tile).receiveEnergy(side.getOpposite(), storage.extractEnergy(storage.getMaxEnergyStored(), true), false), false);
					}
				}
			}
		}

    @SuppressWarnings("unused")
	private boolean canAddEnergy()
    {
        return this.storage.getEnergyStored() == 0 ? false : this.direction != "none";
    }

    public void updateHandlers()
    {
        String d = this.getHandlers();

        if (d == null)
        {
            this.direction = "none";
        }
        else
        {
            this.direction = d;
        }
    }
    
    public String getInvName()
	{
		return this.isInvNameLocalized() ? this.localizedName : "Fission Reactor";
	}
	
	public boolean isInvNameLocalized()
	{
		return this.localizedName != null && this.localizedName.length() > 0;
	}

    public String getHandlers()
    {
        TileEntity down = this.worldObj.getTileEntity(this.xCoord, this.yCoord - 1, this.zCoord);
        TileEntity up = this.worldObj.getTileEntity(this.xCoord, this.yCoord + 1, this.zCoord);
        TileEntity north = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord - 1);
        TileEntity south = this.worldObj.getTileEntity(this.xCoord, this.yCoord, this.zCoord + 1);
        TileEntity east = this.worldObj.getTileEntity(this.xCoord + 1, this.yCoord, this.zCoord);
        TileEntity west = this.worldObj.getTileEntity(this.xCoord - 1, this.yCoord, this.zCoord);
        return EnergyHelper.isEnergyReceiverFromSide(down, ForgeDirection.DOWN) ? "down" : (EnergyHelper.isEnergyReceiverFromSide(up, ForgeDirection.UP) ? "up" : (EnergyHelper.isEnergyReceiverFromSide(west, ForgeDirection.WEST) ? "west" : (EnergyHelper.isEnergyReceiverFromSide(east, ForgeDirection.EAST) ? "east" : (EnergyHelper.isEnergyReceiverFromSide(north, ForgeDirection.NORTH) ? "north" : (EnergyHelper.isEnergyReceiverFromSide(south, ForgeDirection.SOUTH) ? "south" : "none")))));
    }

    private void fuel()
    {
        ItemStack stack = this.getStackInSlot(0);
        ItemStack pstack = this.getStackInSlot(1);

        if (stack != null && pstack == null && isFuel(stack) && this.fueltime == 0 && this.fueltype == 0)
        {
            this.addFuel(fuelValue(stack));
            
            this.fueltype = TileEntityFissionReactorGraphite.setfueltype(stack);
            
            --this.slots[0].stackSize;

            if (this.slots[0].stackSize <= 0) {
                this.slots[0] = null;
            }
            off = 1;
        }
    }
    
    private void upgrade()
    {
        ItemStack stack = this.getStackInSlot(2);

        if (stack != null && isUpgrade(stack))
        {
        	R = (int) (rawR + stack.stackSize);
        	D = R*2 + 1;
            SR = R-1; 
            SD = SR*2 + 1;
        	
        	if (R != rawR) {
        	}

            if (this.slots[2].stackSize <= 0)
            {
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
    
    private void product()
    {
        if (this.slots[1] == null && this.fueltime <= 0 && this.fueltype != 0)
        {    
        	if (this.fueltype == 1)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 22); this.fueltype = 0; }

        	else if (this.fueltype == 2)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 23); this.fueltype = 0; }

        	else if (this.fueltype == 3)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 24); this.fueltype = 0; }

        	else if (this.fueltype == 4)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 25); this.fueltype = 0; }

        	else if (this.fueltype == 5)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 26); this.fueltype = 0; }

        	else if (this.fueltype == 6)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 27); this.fueltype = 0; }
        	
        	else if (this.fueltype == 7)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 28); this.fueltype = 0; }

        	else if (this.fueltype == 8)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 29); this.fueltype = 0; }

        	else if (this.fueltype == 9)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 30); this.fueltype = 0; }

        	else if (this.fueltype == 10)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 31); this.fueltype = 0; }

        	else if (this.fueltype == 11)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 32); this.fueltype = 0; }
        	
        	else if (this.fueltype == 12)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 67); this.fueltype = 0; }

        	else if (this.fueltype == 13)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 68); this.fueltype = 0; }

        	else if (this.fueltype == 14)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 69); this.fueltype = 0; }

        	else if (this.fueltype == 15)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 70); this.fueltype = 0; }

        	else if (this.fueltype == 16)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 71); this.fueltype = 0; }
        	
        	else if (this.fueltype == 17)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 72); this.fueltype = 0; }

        	else if (this.fueltype == 18)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 73); this.fueltype = 0; }

        	else if (this.fueltype == 19)
        	{ this.slots[1] = new ItemStack(NRItems.fuel, 1, 74); this.fueltype = 0; }
        	
        	else {}
        	
        	this.fueltime = 0;
        }
    }
    
    public static boolean isFuel(ItemStack stack)
    {
        return fuelValue(stack) > 0 && setfueltype(stack) != 0;
    }
    
    public static boolean isUpgrade(ItemStack stack)
    {
        return stack.getItem() == NRItems.upgrade;
    }
    
    public static int fuelValue(ItemStack stack)
    {
		if (stack == null)
        {
            return 0;
        }
        else
        {
        	Item item = stack.getItem();
        	
        	if(item == new ItemStack (NRItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) { return 7200000; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) { return 7200000; }
		   else if(item == new ItemStack (NRItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) { return 7200000; }
         	return 0; }
	   }
    
    public static int setfueltype(ItemStack stack) {
		if (stack == null) {
            return 0;
        } else {
        	Item item = stack.getItem();
        	
        	if(item == new ItemStack (NRItems.fuel, 1, 11).getItem() && item.getDamage(stack) == 11) { return 1; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 12).getItem() && item.getDamage(stack) == 12) { return 2; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 13).getItem() && item.getDamage(stack) == 13) { return 3; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 14).getItem() && item.getDamage(stack) == 14) { return 4; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 15).getItem() && item.getDamage(stack) == 15) { return 5; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 16).getItem() && item.getDamage(stack) == 16) { return 6; }
  		   else if(item == new ItemStack (NRItems.fuel, 1, 17).getItem() && item.getDamage(stack) == 17) { return 7; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 18).getItem() && item.getDamage(stack) == 18) { return 8; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 19).getItem() && item.getDamage(stack) == 19) { return 9; }
   		   else if(item == new ItemStack (NRItems.fuel, 1, 20).getItem() && item.getDamage(stack) == 20) { return 10;}
   		   else if(item == new ItemStack (NRItems.fuel, 1, 21).getItem() && item.getDamage(stack) == 21) { return 11;}
   		   else if(item == new ItemStack (NRItems.fuel, 1, 59).getItem() && item.getDamage(stack) == 59) { return 12;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 60).getItem() && item.getDamage(stack) == 60) { return 13;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 61).getItem() && item.getDamage(stack) == 61) { return 14;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 62).getItem() && item.getDamage(stack) == 62) { return 15;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 63).getItem() && item.getDamage(stack) == 63) { return 16;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 64).getItem() && item.getDamage(stack) == 64) { return 17;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 65).getItem() && item.getDamage(stack) == 65) { return 18;}
		   else if(item == new ItemStack (NRItems.fuel, 1, 66).getItem() && item.getDamage(stack) == 66) { return 19;}
        	return 0; }
	   }

    public void readFromNBT(NBTTagCompound nbt)
    {
        super.readFromNBT(nbt);

        if (nbt.hasKey("storage"))
        {
            this.storage.readFromNBT(nbt.getCompoundTag("storage"));
        }

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
        this.flag = nbt.getBoolean("flag");
        this.flag1 = nbt.getBoolean("flag1");
        this.off = nbt.getInteger("off");
        this.fuelmult = nbt.getInteger("Fuelmult");
        this.MBNumber = nbt.getInteger("MBN");
        this.heat = nbt.getInteger("Heat");
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
        nbt.setInteger("Fueltime", this.fueltime);
        nbt.setInteger("Fueltype", this.fueltype);
        nbt.setInteger("rawR", this.rawR);
        nbt.setInteger("R", this.R);
        nbt.setInteger("SR", this.SR);
        nbt.setInteger("D", this.D);
        nbt.setInteger("SD", this.SD);
        nbt.setInteger("E", this.E);
        nbt.setInteger("H", this.H);
        nbt.setBoolean("flag", this.flag);
        nbt.setBoolean("flag1", this.flag1);
        nbt.setInteger("off", this.off);
        nbt.setInteger("Fuelmult", this.fuelmult);
        nbt.setInteger("MBN", this.MBNumber);
        nbt.setInteger("Heat", this.heat);
        nbt.setString("Typeoffuel", this.typeoffuel);
        nbt.setString("problem", this.problem);
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
        
        if(this.isInvNameLocalized())
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

    public void addHeat(int add)
    {
        this.heat += add;
    }
    
    public void addFuel(int add)
    {
        this.fueltime += add;
    }

    public void removeHeat(int remove)
    {
        this.heat -= remove;
    }
    
    public void removeFuel(int remove)
    {
        this.fueltime -= remove;
    }

    public boolean canConnectEnergy(ForgeDirection from)
    {
        return true;
    }

    public int receiveEnergy(ForgeDirection from, int maxReceive, boolean simulate)
    {
        return 0;
    }

    public int extractEnergy(ForgeDirection from, int maxExtract, boolean simulate)
    {
        return 0;
    }

    public int getEnergyStored(ForgeDirection paramForgeDirection)
    {
        return this.storage.getEnergyStored();
    }

    public int getMaxEnergyStored(ForgeDirection paramForgeDirection)
    {
        return this.storage.getMaxEnergyStored();
    }

    /**
     * Returns true if automation is allowed to insert the given stack (ignoring stack size) into the given slot.
     */
    public boolean isItemValidForSlot(int slot, ItemStack stack)
    {
        if (slot == 0)
        {
            return isFuel(stack);
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns an array containing the indices of the slots that can be accessed by automation on the given side of this
     * block.
     */
    public int[] getAccessibleSlotsFromSide(int slot)
    {
        return slot == 0 ? slotsSides : slotsTop;
    }

    /**
     * Returns true if automation can insert the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canInsertItem(int slot, ItemStack stack, int par)
    {
        return this.isItemValidForSlot(slot, stack);
    }

    /**
     * Returns true if automation can extract the given item in the given slot from the given side. Args: Slot, item,
     * side
     */
    public boolean canExtractItem(int slot, ItemStack stack, int slots)
    {
        return slot == 1;
    }
    
    private boolean checkReactor() { 
    	  ForgeDirection forward = ForgeDirection.getOrientation(
    	    this.getBlockMetadata()).getOpposite();
    	  int x = xCoord + R * forward.offsetX;
    	  int y = yCoord;
    	  int z = zCoord + R * forward.offsetZ;

    	  for (int Z4 = -R; Z4 <= R; Z4++) {
       	   for (int X4 = -R; X4 <= R; X4++) {
       		for (int Y4 = 0; Y4 <= 2*R; Y4++) {
       	     if(this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.fissionReactorGraphiteIdle || this.worldObj.getBlock(x + X4, y + Y4, z + Z4)==NRBlocks.fissionReactorGraphiteActive){
       	    	 if (x + X4 == this.xCoord && y + Y4 == this.yCoord && z + Z4 == this.zCoord) {} else {
       	    		 this.problem = StatCollector.translateToLocal("gui.multipleControllers");
       	      return false;
       	}}}}}
    	  
    	  for (int Z1 = -SR; Z1 <= SR; Z1++) {
    	   for (int X1 = -SR; X1 <= SR; X1++) {
    	     if(this.worldObj.getBlock(x + X1, y, z + Z1)!=NRBlocks.reactorBlock){
    	    	 this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	      return false;
    	    }
    	    if(this.worldObj.getBlock(x + X1, y+D-1, z + Z1)!=NRBlocks.reactorBlock){
    	    	this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	     return false;
    	    }}}

    	  for (int Y2 = 1; Y2 <= SD; Y2++) {
    	  for (int XZ = -SR; XZ <= SR; XZ++) {
    	   if(this.worldObj.getBlock(x-R, y+Y2, z+XZ)!=NRBlocks.reactorBlock){
    		   this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	    return false;
    	   	}
    	   if(this.worldObj.getBlock(x+R, y+Y2, z+XZ)!=NRBlocks.reactorBlock){
    		   this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	    return false;
    	   	}
    	   if(x!=R&&x!=-R){
    	   if(this.worldObj.getBlock(x+XZ, y+Y2, z+R)!=NRBlocks.reactorBlock){
    		   this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	    return false;
    	   	}
    	   if(this.worldObj.getBlock(x+XZ, y+Y2, z-R)!=NRBlocks.reactorBlock){
    		   this.problem = StatCollector.translateToLocal("gui.casingIncomplete");
    	    return false;
    	   }
    	    if(this.worldObj.getBlock(x, y+Y2, z)!=NRBlocks.cellBlock){
    	    	this.problem = StatCollector.translateToLocal("gui.fuelRodIncomplete");
        	    return false;
    	    }}}}
    	  
    	  for (int Z3 = -SR; Z3 <= SR; Z3++) {
       	   for (int X3 = -SR; X3 <= SR; X3++) {
       		for (int Y3 = 1; Y3 <= SD; Y3++) {
       	     if(this.worldObj.getBlock(x + X3, y + Y3, z + Z3)==NRBlocks.reactorBlock){
       	    	this.problem = StatCollector.translateToLocal("gui.casingInInterior");
       	      return false;
       	    }}}}
     	   
    	  this.MBNumber = this.getBlockMetadata();
    	  return true;
    	 }
}
