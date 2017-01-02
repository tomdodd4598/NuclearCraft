package nc.gui;

import nc.NuclearCraft;
import nc.container.accelerator.ContainerSynchrotron;
import nc.container.crafting.ContainerNuclearWorkspace;
import nc.container.generator.ContainerFissionReactor;
import nc.container.generator.ContainerFissionReactorSteam;
import nc.container.generator.ContainerFusionReactor;
import nc.container.generator.ContainerFusionReactorSteam;
import nc.container.generator.ContainerReactionGenerator;
import nc.container.machine.ContainerAssembler;
import nc.container.machine.ContainerCollector;
import nc.container.machine.ContainerCooler;
import nc.container.machine.ContainerCrusher;
import nc.container.machine.ContainerElectricCrusher;
import nc.container.machine.ContainerElectricFurnace;
import nc.container.machine.ContainerElectrolyser;
import nc.container.machine.ContainerFactory;
import nc.container.machine.ContainerFurnace;
import nc.container.machine.ContainerHastener;
import nc.container.machine.ContainerHeliumExtractor;
import nc.container.machine.ContainerIoniser;
import nc.container.machine.ContainerIrradiator;
import nc.container.machine.ContainerNuclearFurnace;
import nc.container.machine.ContainerOxidiser;
import nc.container.machine.ContainerRecycler;
import nc.container.machine.ContainerSeparator;
import nc.gui.accelerator.GuiSynchrotron;
import nc.gui.crafting.GuiNuclearWorkspace;
import nc.gui.generator.GuiFissionReactor;
import nc.gui.generator.GuiFissionReactorSteam;
import nc.gui.generator.GuiFusionReactor;
import nc.gui.generator.GuiFusionReactorSteam;
import nc.gui.generator.GuiReactionGenerator;
import nc.gui.machine.GuiAssembler;
import nc.gui.machine.GuiCollector;
import nc.gui.machine.GuiCooler;
import nc.gui.machine.GuiCrusher;
import nc.gui.machine.GuiElectricCrusher;
import nc.gui.machine.GuiElectricFurnace;
import nc.gui.machine.GuiElectrolyser;
import nc.gui.machine.GuiFactory;
import nc.gui.machine.GuiFurnace;
import nc.gui.machine.GuiHastener;
import nc.gui.machine.GuiHeliumExtractor;
import nc.gui.machine.GuiIoniser;
import nc.gui.machine.GuiIrradiator;
import nc.gui.machine.GuiNuclearFurnace;
import nc.gui.machine.GuiOxidiser;
import nc.gui.machine.GuiRecycler;
import nc.gui.machine.GuiSeparator;
import nc.tile.accelerator.TileSynchrotron;
import nc.tile.crafting.TileNuclearWorkspace;
import nc.tile.generator.TileFissionReactor;
import nc.tile.generator.TileFissionReactorSteam;
import nc.tile.generator.TileFusionReactor;
import nc.tile.generator.TileFusionReactorSteam;
import nc.tile.generator.TileReactionGenerator;
import nc.tile.machine.TileAssembler;
import nc.tile.machine.TileCollector;
import nc.tile.machine.TileCooler;
import nc.tile.machine.TileCrusher;
import nc.tile.machine.TileElectricCrusher;
import nc.tile.machine.TileElectricFurnace;
import nc.tile.machine.TileElectrolyser;
import nc.tile.machine.TileFactory;
import nc.tile.machine.TileFurnace;
import nc.tile.machine.TileHastener;
import nc.tile.machine.TileHeliumExtractor;
import nc.tile.machine.TileIoniser;
import nc.tile.machine.TileIrradiator;
import nc.tile.machine.TileNuclearFurnace;
import nc.tile.machine.TileOxidiser;
import nc.tile.machine.TileRecycler;
import nc.tile.machine.TileSeparator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;

public class GuiHandler implements IGuiHandler {
	
	public GuiHandler() {
		NetworkRegistry.INSTANCE.registerGuiHandler(NuclearCraft.instance, this);
	}

	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		
		if (entity != null) {
			switch(ID) {
				case 0:
					if(entity instanceof TileNuclearFurnace) return new ContainerNuclearFurnace(player.inventory, (TileNuclearFurnace) entity);
					return null;
				case 1:
					if(entity instanceof TileFurnace) return new ContainerFurnace(player.inventory, (TileFurnace) entity);
					return null;
				case 2:
					if(entity instanceof TileCrusher) return new ContainerCrusher(player.inventory, (TileCrusher) entity);
					return null;
				case 3:
					if(entity instanceof TileElectricCrusher) return new ContainerElectricCrusher(player.inventory, (TileElectricCrusher) entity);
					return null;
				case 4:
					if(entity instanceof TileElectricFurnace) return new ContainerElectricFurnace(player.inventory, (TileElectricFurnace) entity);
					return null;
				case 5:
					if(entity instanceof TileReactionGenerator) return new ContainerReactionGenerator(player.inventory, (TileReactionGenerator) entity);
					return null;
				case 6:
					if(entity instanceof TileSeparator) return new ContainerSeparator(player.inventory, (TileSeparator) entity);
					return null;
				case 7:
					if(entity instanceof TileHastener) return new ContainerHastener(player.inventory, (TileHastener) entity);
					return null;
				case 8:
					if(entity instanceof TileFissionReactor) return new ContainerFissionReactor(player.inventory, (TileFissionReactor) entity);
					return null;
				case 9:
					if(entity instanceof TileNuclearWorkspace) return new ContainerNuclearWorkspace(player.inventory, world, x, y, z, (TileNuclearWorkspace) entity);
					return null;
				case 10:
					if(entity instanceof TileCollector) return new ContainerCollector(player.inventory, (TileCollector) entity);
					return null;
				case 11:
					if(entity instanceof TileFusionReactor) return new ContainerFusionReactor(player.inventory, (TileFusionReactor) entity);
					return null;
				case 12:
					if(entity instanceof TileElectrolyser) return new ContainerElectrolyser(player.inventory, (TileElectrolyser) entity);
					return null;
				case 13:
					if(entity instanceof TileOxidiser) return new ContainerOxidiser(player.inventory, (TileOxidiser) entity);
					return null;
				case 14:
					if(entity instanceof TileIoniser) return new ContainerIoniser(player.inventory, (TileIoniser) entity);
					return null;
				case 15:
					if(entity instanceof TileIrradiator) return new ContainerIrradiator(player.inventory, (TileIrradiator) entity);
					return null;
				case 16:
					if(entity instanceof TileCooler) return new ContainerCooler(player.inventory, (TileCooler) entity);
					return null;
				case 17:
					if(entity instanceof TileFactory) return new ContainerFactory(player.inventory, (TileFactory) entity);
					return null;
				case 18:
					if(entity instanceof TileHeliumExtractor) return new ContainerHeliumExtractor(player.inventory, (TileHeliumExtractor) entity);
					return null;
				case 19:
					if(entity instanceof TileSynchrotron) return new ContainerSynchrotron(player.inventory, (TileSynchrotron) entity);
					return null;
				case 20:
					if(entity instanceof TileAssembler) return new ContainerAssembler(player.inventory, (TileAssembler) entity);
					return null;
				case 21:
					if(entity instanceof TileFissionReactorSteam) return new ContainerFissionReactorSteam(player.inventory, (TileFissionReactorSteam) entity);
					return null;
				case 22:
					if(entity instanceof TileFusionReactorSteam) return new ContainerFusionReactorSteam(player.inventory, (TileFusionReactorSteam) entity);
					return null;
				case 23:
					if(entity instanceof TileRecycler) return new ContainerRecycler(player.inventory, (TileRecycler) entity);
					return null;
			}
		}
		return null;
	}

	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity entity = world.getTileEntity(x, y, z);
		if (entity != null) {
			switch(ID) {
				case 0:
					if(entity instanceof TileNuclearFurnace) return new GuiNuclearFurnace(player.inventory, (TileNuclearFurnace) entity);
					return null;
				case 1:
					if(entity instanceof TileFurnace) return new GuiFurnace(player.inventory, (TileFurnace) entity);
					return null;
				case 2:
					if(entity instanceof TileCrusher) return new GuiCrusher(player.inventory, (TileCrusher) entity);
					return null;
				case 3:
					if(entity instanceof TileElectricCrusher) return new GuiElectricCrusher(player.inventory, (TileElectricCrusher) entity);
					return null;
				case 4:
					if(entity instanceof TileElectricFurnace) return new GuiElectricFurnace(player.inventory, (TileElectricFurnace) entity);
					return null;
				case 5:
					if(entity instanceof TileReactionGenerator) return new GuiReactionGenerator(player.inventory, (TileReactionGenerator) entity);
					return null;
				case 6:
					if(entity instanceof TileSeparator) return new GuiSeparator(player.inventory, (TileSeparator) entity);
					return null;
				case 7:
					if(entity instanceof TileHastener) return new GuiHastener(player.inventory, (TileHastener) entity);
					return null;
				case 8:
					if(entity instanceof TileFissionReactor) return new GuiFissionReactor(player.inventory, (TileFissionReactor) entity);
					return null;
				case 9:
					if(entity instanceof TileNuclearWorkspace) return new GuiNuclearWorkspace(player.inventory, world, x, y, z, (TileNuclearWorkspace) entity);
					return null;
				case 10:
					if(entity instanceof TileCollector) return new GuiCollector(player.inventory, (TileCollector) entity);
					return null;
				case 11:
					if(entity instanceof TileFusionReactor) return new GuiFusionReactor(player.inventory, (TileFusionReactor) entity);
					return null;
				case 12:
					if(entity instanceof TileElectrolyser) return new GuiElectrolyser(player.inventory, (TileElectrolyser) entity);
					return null;
				case 13:
					if(entity instanceof TileOxidiser) return new GuiOxidiser(player.inventory, (TileOxidiser) entity);
					return null;
				case 14:
					if(entity instanceof TileIoniser) return new GuiIoniser(player.inventory, (TileIoniser) entity);
					return null;
				case 15:
					if(entity instanceof TileIrradiator) return new GuiIrradiator(player.inventory, (TileIrradiator) entity);
					return null;
				case 16:
					if(entity instanceof TileCooler) return new GuiCooler(player.inventory, (TileCooler) entity);
					return null;
				case 17:
					if(entity instanceof TileFactory) return new GuiFactory(player.inventory, (TileFactory) entity);
					return null;
				case 18:
					if(entity instanceof TileHeliumExtractor) return new GuiHeliumExtractor(player.inventory, (TileHeliumExtractor) entity);
					return null;
				case 19:
					if(entity instanceof TileSynchrotron) return new GuiSynchrotron(player.inventory, (TileSynchrotron) entity);
					return null;
				case 20:
					if(entity instanceof TileAssembler) return new GuiAssembler(player.inventory, (TileAssembler) entity);
					return null;
				case 21:
					if(entity instanceof TileFissionReactorSteam) return new GuiFissionReactorSteam(player.inventory, (TileFissionReactorSteam) entity);
					return null;
				case 22:
					if(entity instanceof TileFusionReactorSteam) return new GuiFusionReactorSteam(player.inventory, (TileFusionReactorSteam) entity);
					return null;
				case 23:
					if(entity instanceof TileRecycler) return new GuiRecycler(player.inventory, (TileRecycler) entity);
					return null;
			}
		}
		return null;
	}
}