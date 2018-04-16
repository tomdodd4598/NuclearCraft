package nc.container.generator;

import nc.recipe.NCRecipes;
import nc.tile.generator.TileFusionCore;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerFusionCore extends ContainerFluidGenerator {
	
	protected int processTime;
	protected int processPower;
	protected int heat;
	protected int efficiency;
	protected int rateMultiplier;
	protected int cooling;
	protected int heatChange;
	
	public ContainerFusionCore(EntityPlayer player, TileFusionCore tileEntity) {
		super(tileEntity, NCRecipes.Type.FUSION);
		
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 9; j++) {
				addSlotToContainer(new Slot(player.inventory, j + 9*i + 9, 8 + 18*j, 105 + 18*i));
			}
		}
		
		for (int i = 0; i < 9; i++) {
			addSlotToContainer(new Slot(player.inventory, i, 8 + 18*i, 163));
		}
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		
		for (int i = 0; i < listeners.size(); i++) {
			IContainerListener icontainerlistener = (IContainerListener) listeners.get(i);
			
			for (int j : new int[] {0, 1, 2, 3, 4, 5, 6, 9, 10}) {
				icontainerlistener.sendWindowProperty(this, j, tile.getField(j) >> 16);
				icontainerlistener.sendWindowProperty(this, 100 + j, tile.getField(j));
			}
			
			for (int j : new int[] {7, 8}) icontainerlistener.sendWindowProperty(this, j, tile.getField(j));
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void updateProgressBar(int id, int data) {
		if (id == 100) time = upcast(data);
		else if (id == 101) energy = upcast(data);
		else if (id == 102) processTime = upcast(data);
		else if (id == 103) processPower = upcast(data);
		else if (id == 104) heat = upcast(data);
		else if (id == 105) efficiency = upcast(data);
		else if (id == 106) rateMultiplier = upcast(data);
		else if (id == 109) cooling = upcast(data);
		else if (id == 110) heatChange = upcast(data);
		
		else if (id == 0) tile.setField(id, time | data << 16);
		else if (id == 1) tile.setField(id, energy | data << 16);
		else if (id == 2) tile.setField(id, processTime | data << 16);
		else if (id == 3) tile.setField(id, processPower | data << 16);
		else if (id == 4) tile.setField(id, heat | data << 16);
		else if (id == 5) tile.setField(id, efficiency | data << 16);
		else if (id == 6) tile.setField(id, rateMultiplier | data << 16);
		else if (id == 9) tile.setField(id, cooling | data << 16);
		else if (id == 10) tile.setField(id, heatChange | data << 16);
		
		else if (id == 7 || id == 8) tile.setField(id, data);
	}
}
