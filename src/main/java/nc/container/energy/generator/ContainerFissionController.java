package nc.container.energy.generator;

import nc.crafting.generator.FissionRecipes;
import nc.tile.energy.generator.TileFissionController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;

public class ContainerFissionController extends ContainerEnergyGeneratorProcessor {
	
	protected int processTime;
	protected int processPower;
	protected int heat;
	protected int cooling;
	protected int efficiency;
	protected int cells;
	protected int rateMultiplier;
	protected int lengthX;
	protected int lengthY;
	protected int lengthZ;
	protected int heatChange;
	protected int complete;
	protected int ready;

	public ContainerFissionController(EntityPlayer player, TileFissionController tileEntity) {
		super(tileEntity, FissionRecipes.instance());
		
		addSlotToContainer(new Slot(tileEntity, 0, 56, 35));
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 1, 116, 35));
		
		addSlotToContainer(new SlotFurnaceOutput(player, tileEntity, 2, -4095, -4095));
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 95 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 153));
		}
	}
	
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			if (time != tile.getField(0)) {
				icontainerlistener.sendProgressBarUpdate(this, 0, tile.getField(0));
			}
			
			if (energy != tile.getField(1)) {
				icontainerlistener.sendProgressBarUpdate(this, 1, tile.getField(1));
			}
			
			if (processTime != tile.getField(2)) {
				icontainerlistener.sendProgressBarUpdate(this, 2, tile.getField(2));
			}
			
			if (processPower != tile.getField(3)) {
				icontainerlistener.sendProgressBarUpdate(this, 3, tile.getField(3));
			}
			
			if (heat != tile.getField(4)) {
				icontainerlistener.sendProgressBarUpdate(this, 4, tile.getField(4));
			}
			
			if (cooling != tile.getField(5)) {
				icontainerlistener.sendProgressBarUpdate(this, 5, tile.getField(5));
			}
			
			if (efficiency != tile.getField(6)) {
				icontainerlistener.sendProgressBarUpdate(this, 6, tile.getField(6));
			}
			
			if (cells != tile.getField(7)) {
				icontainerlistener.sendProgressBarUpdate(this, 7, tile.getField(7));
			}
			
			if (rateMultiplier != tile.getField(8)) {
				icontainerlistener.sendProgressBarUpdate(this, 8, tile.getField(8));
			}
			
			if (lengthX != tile.getField(9)) {
				icontainerlistener.sendProgressBarUpdate(this, 9, tile.getField(9));
			}
			
			if (lengthY != tile.getField(10)) {
				icontainerlistener.sendProgressBarUpdate(this, 10, tile.getField(10));
			}
			
			if (lengthZ != tile.getField(11)) {
				icontainerlistener.sendProgressBarUpdate(this, 11, tile.getField(11));
			}
			
			if (heatChange != tile.getField(12)) {
				icontainerlistener.sendProgressBarUpdate(this, 12, tile.getField(12));
			}
			
			if (complete != tile.getField(13)) {
				icontainerlistener.sendProgressBarUpdate(this, 13, tile.getField(13));
			}
			
			if (ready != tile.getField(14)) {
				icontainerlistener.sendProgressBarUpdate(this, 14, tile.getField(14));
			}
		}
		
		time = tile.getField(0);
		energy = tile.getField(1);
		processTime = tile.getField(2);
		processPower = tile.getField(3);
		heat = tile.getField(4);
		cooling = tile.getField(5);
		efficiency = tile.getField(6);
		cells = tile.getField(7);
		rateMultiplier = tile.getField(8);
		lengthX = tile.getField(9);
		lengthY = tile.getField(10);
		lengthZ = tile.getField(11);
		heatChange = tile.getField(12);
		complete = tile.getField(13);
		ready = tile.getField(14);
	}
}
