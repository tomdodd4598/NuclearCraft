package nc.container.generator;

import nc.container.SlotProcessorInput;
import nc.crafting.generator.FissionRecipes;
import nc.tile.generator.TileFissionController;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotFurnaceOutput;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFissionController extends ContainerItemGenerator {
	
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
		
		addSlotToContainer(new SlotProcessorInput(tileEntity, recipes, 0, 56, 35));
		
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
			
			//if (time != tile.getField(0)) {
				icontainerlistener.sendProgressBarUpdate(this, 0, tile.getField(0) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 100, tile.getField(0));
			//}
			
			//if (energy != tile.getField(1)) {
				icontainerlistener.sendProgressBarUpdate(this, 1, tile.getField(1) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 101, tile.getField(1));
			//}
			
			//if (processTime != tile.getField(2)) {
				icontainerlistener.sendProgressBarUpdate(this, 2, tile.getField(2) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 102, tile.getField(2));
			//}
			
			//if (processPower != tile.getField(3)) {
				icontainerlistener.sendProgressBarUpdate(this, 3, tile.getField(3) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 103, tile.getField(3));
			//}
			
			//if (heat != tile.getField(4)) {
				icontainerlistener.sendProgressBarUpdate(this, 4, tile.getField(4) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 104, tile.getField(4));
			//}
			
			//if (cooling != tile.getField(5)) {
				icontainerlistener.sendProgressBarUpdate(this, 5, tile.getField(5) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 105, tile.getField(5));
			//}
			
			//if (efficiency != tile.getField(6)) {
				icontainerlistener.sendProgressBarUpdate(this, 6, tile.getField(6) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 106, tile.getField(6));
			//}
			
			//if (cells != tile.getField(7)) {
				icontainerlistener.sendProgressBarUpdate(this, 7, tile.getField(7) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 107, tile.getField(7));
			//}
			
			//if (rateMultiplier != tile.getField(8)) {
				icontainerlistener.sendProgressBarUpdate(this, 8, tile.getField(8) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 108, tile.getField(8));
			//}
			
			//if (lengthX != tile.getField(9)) {
				icontainerlistener.sendProgressBarUpdate(this, 9, tile.getField(9) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 109, tile.getField(9));
			//}
			
			//if (lengthY != tile.getField(10)) {
				icontainerlistener.sendProgressBarUpdate(this, 10, tile.getField(10) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 110, tile.getField(10));
			//}
			
			//if (lengthZ != tile.getField(11)) {
				icontainerlistener.sendProgressBarUpdate(this, 11, tile.getField(11) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 111, tile.getField(11));
			//}
			
			//if (heatChange != tile.getField(12)) {
				icontainerlistener.sendProgressBarUpdate(this, 12, tile.getField(12) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 112, tile.getField(12));
			//}
			
			//if (complete != tile.getField(13)) {
				icontainerlistener.sendProgressBarUpdate(this, 13, tile.getField(13) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 113, tile.getField(13));
			//}
			
			//if (ready != tile.getField(14)) {
				icontainerlistener.sendProgressBarUpdate(this, 14, tile.getField(14) >> 16);
				icontainerlistener.sendProgressBarUpdate(this, 114, tile.getField(14));
			//}
		}
		
		/*time = tile.getField(0);
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
		ready = tile.getField(14);*/
	}
	
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (id == 100) time = upcast(data);
		else if (id == 101) energy = upcast(data);
		else if (id == 102) processTime = upcast(data);
		else if (id == 103) processPower = upcast(data);
		else if (id == 104) heat = upcast(data);
		else if (id == 105) cooling = upcast(data);
		else if (id == 106) efficiency = upcast(data);
		else if (id == 107) cells = upcast(data);
		else if (id == 108) rateMultiplier = upcast(data);
		else if (id == 109) lengthX = upcast(data);
		else if (id == 110) lengthY = upcast(data);
		else if (id == 111) lengthZ = upcast(data);
		else if (id == 112) heatChange = upcast(data);
		else if (id == 113) complete = upcast(data);
		else if (id == 114) ready = upcast(data);
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
		else if (id == 2) tile.setField(id, processTime | data << 16);
		else if (id == 3) tile.setField(id, processPower | data << 16);
		else if (id == 4) tile.setField(id, heat | data << 16);
		else if (id == 5) tile.setField(id, cooling | data << 16);
		else if (id == 6) tile.setField(id, efficiency | data << 16);
		else if (id == 7) tile.setField(id, cells | data << 16);
		else if (id == 8) tile.setField(id, rateMultiplier | data << 16);
		else if (id == 9) tile.setField(id, lengthX | data << 16);
		else if (id == 10) tile.setField(id, lengthY | data << 16);
		else if (id == 11) tile.setField(id, lengthZ | data << 16);
		else if (id == 12) tile.setField(id, heatChange | data << 16);
		else if (id == 13) tile.setField(id, complete | data << 16);
		else if (id == 14) tile.setField(id, ready | data << 16);
	}
}
