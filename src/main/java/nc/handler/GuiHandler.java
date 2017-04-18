package nc.handler;

import nc.container.energy.generator.ContainerFissionController;
import nc.container.energy.processor.ContainerAlloyFurnace;
import nc.container.energy.processor.ContainerDecayHastener;
import nc.container.energy.processor.ContainerFuelReprocessor;
import nc.container.energy.processor.ContainerIsotopeSeparator;
import nc.container.energy.processor.ContainerManufactory;
import nc.container.processor.ContainerNuclearFurnace;
import nc.gui.energy.generator.GuiFissionController;
import nc.gui.energy.processor.GuiAlloyFurnace;
import nc.gui.energy.processor.GuiDecayHastener;
import nc.gui.energy.processor.GuiFuelReprocessor;
import nc.gui.energy.processor.GuiIsotopeSeparator;
import nc.gui.energy.processor.GuiManufactory;
import nc.gui.processor.GuiNuclearFurnace;
import nc.tile.energy.generator.TileFissionController;
import nc.tile.energy.processor.TileEnergyProcessors.TileAlloyFurnace;
import nc.tile.energy.processor.TileEnergyProcessors.TileDecayHastener;
import nc.tile.energy.processor.TileEnergyProcessors.TileFuelReprocessor;
import nc.tile.energy.processor.TileEnergyProcessors.TileIsotopeSeparator;
import nc.tile.energy.processor.TileEnergyProcessors.TileManufactory;
import nc.tile.processor.TileNuclearFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory entity = (IInventory) world.getTileEntity(new BlockPos(x, y, z));
		
		if (entity != null) {
			switch(ID) {
			case 0:
				if (entity instanceof TileNuclearFurnace) return new ContainerNuclearFurnace(player, entity);
			case 1:
				if (entity instanceof TileManufactory) return new ContainerManufactory(player, (TileManufactory) entity);
			case 2:
				if (entity instanceof TileIsotopeSeparator) return new ContainerIsotopeSeparator(player, (TileIsotopeSeparator) entity);
			case 3:
				if (entity instanceof TileDecayHastener) return new ContainerDecayHastener(player, (TileDecayHastener) entity);
			case 4:
				if (entity instanceof TileFuelReprocessor) return new ContainerFuelReprocessor(player, (TileFuelReprocessor) entity);
			case 5:
				if (entity instanceof TileAlloyFurnace) return new ContainerAlloyFurnace(player, (TileAlloyFurnace) entity);
			case 100:
				if (entity instanceof TileFissionController) return new ContainerFissionController(player, (TileFissionController) entity);
			}
		}
		
		return null;
	}
	
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory entity = (IInventory) world.getTileEntity(new BlockPos(x, y, z));
		
		if (entity != null) {
			switch(ID) {
			case 0:
				if (entity instanceof TileNuclearFurnace) return new GuiNuclearFurnace(player, entity);
			case 1:
				if (entity instanceof TileManufactory) return new GuiManufactory(player, (TileManufactory) entity);
			case 2:
				if (entity instanceof TileIsotopeSeparator) return new GuiIsotopeSeparator(player, (TileIsotopeSeparator) entity);
			case 3:
				if (entity instanceof TileDecayHastener) return new GuiDecayHastener(player, (TileDecayHastener) entity);
			case 4:
				if (entity instanceof TileFuelReprocessor) return new GuiFuelReprocessor(player, (TileFuelReprocessor) entity);
			case 5:
				if (entity instanceof TileAlloyFurnace) return new GuiAlloyFurnace(player, (TileAlloyFurnace) entity);
			case 100:
				if (entity instanceof TileFissionController) return new GuiFissionController(player, (TileFissionController) entity);
			}
		}
		
		return null;
	}

}
