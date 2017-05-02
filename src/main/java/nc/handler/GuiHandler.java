package nc.handler;

import nc.container.generator.ContainerFissionController;
import nc.container.generator.ContainerFusionCore;
import nc.container.processor.ContainerAlloyFurnace;
import nc.container.processor.ContainerDecayHastener;
import nc.container.processor.ContainerFuelReprocessor;
import nc.container.processor.ContainerIsotopeSeparator;
import nc.container.processor.ContainerManufactory;
import nc.container.processor.ContainerNuclearFurnace;
import nc.gui.generator.GuiFissionController;
import nc.gui.generator.GuiFusionCore;
import nc.gui.processor.GuiAlloyFurnace;
import nc.gui.processor.GuiDecayHastener;
import nc.gui.processor.GuiFuelReprocessor;
import nc.gui.processor.GuiIsotopeSeparator;
import nc.gui.processor.GuiManufactory;
import nc.gui.processor.GuiNuclearFurnace;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileFusionCore;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.Processors.TileAlloyFurnace;
import nc.tile.processor.Processors.TileDecayHastener;
import nc.tile.processor.Processors.TileFuelReprocessor;
import nc.tile.processor.Processors.TileIsotopeSeparator;
import nc.tile.processor.Processors.TileManufactory;
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
			case 101:
				if (entity instanceof TileFusionCore) return new ContainerFusionCore(player, (TileFusionCore) entity);
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
			case 101:
				if (entity instanceof TileFusionCore) return new GuiFusionCore(player, (TileFusionCore) entity);
			}
		}
		
		return null;
	}

}
