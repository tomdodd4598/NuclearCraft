package nc.handler;

import nc.container.processor.*;
import nc.gui.processor.*;
import nc.multiblock.container.*;
import nc.multiblock.fission.salt.tile.*;
import nc.multiblock.fission.solid.tile.*;
import nc.multiblock.fission.tile.TileFissionIrradiator;
import nc.multiblock.fission.tile.port.*;
import nc.multiblock.gui.*;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.TileProcessor.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			switch (ID) {
				case 0:
					if (tile instanceof TileNuclearFurnace) {
						return new ContainerNuclearFurnace(player, (TileNuclearFurnace) tile);
					}
				case 1:
					if (tile instanceof Manufactory) {
						return new ContainerManufactory(player, (Manufactory) tile);
					}
				case 2:
					if (tile instanceof Separator) {
						return new ContainerSeparator(player, (Separator) tile);
					}
				case 3:
					if (tile instanceof DecayHastener) {
						return new ContainerDecayHastener(player, (DecayHastener) tile);
					}
				case 4:
					if (tile instanceof FuelReprocessor) {
						return new ContainerFuelReprocessor(player, (FuelReprocessor) tile);
					}
				case 5:
					if (tile instanceof AlloyFurnace) {
						return new ContainerAlloyFurnace(player, (AlloyFurnace) tile);
					}
				case 6:
					if (tile instanceof Infuser) {
						return new ContainerInfuser(player, (Infuser) tile);
					}
				case 7:
					if (tile instanceof Melter) {
						return new ContainerMelter(player, (Melter) tile);
					}
				case 8:
					if (tile instanceof Supercooler) {
						return new ContainerSupercooler(player, (Supercooler) tile);
					}
				case 9:
					if (tile instanceof Electrolyzer) {
						return new ContainerElectrolyzer(player, (Electrolyzer) tile);
					}
				case 10:
					if (tile instanceof Assembler) {
						return new ContainerAssembler(player, (Assembler) tile);
					}
				case 11:
					if (tile instanceof IngotFormer) {
						return new ContainerIngotFormer(player, (IngotFormer) tile);
					}
				case 12:
					if (tile instanceof Pressurizer) {
						return new ContainerPressurizer(player, (Pressurizer) tile);
					}
				case 13:
					if (tile instanceof ChemicalReactor) {
						return new ContainerChemicalReactor(player, (ChemicalReactor) tile);
					}
				case 14:
					if (tile instanceof SaltMixer) {
						return new ContainerSaltMixer(player, (SaltMixer) tile);
					}
				case 15:
					if (tile instanceof Crystallizer) {
						return new ContainerCrystallizer(player, (Crystallizer) tile);
					}
				case 16:
					if (tile instanceof Enricher) {
						return new ContainerEnricher(player, (Enricher) tile);
					}
				case 17:
					if (tile instanceof Extractor) {
						return new ContainerExtractor(player, (Extractor) tile);
					}
				case 18:
					if (tile instanceof Centrifuge) {
						return new ContainerCentrifuge(player, (Centrifuge) tile);
					}
				case 19:
					if (tile instanceof RockCrusher) {
						return new ContainerRockCrusher(player, (RockCrusher) tile);
					}
				case 20:
					if (tile instanceof SteamFurnace) {
						return new ContainerSteamFurnace(player, (SteamFurnace) tile);
					}
				case 21:
					if (tile instanceof SteamCrusher) {
						return new ContainerSteamCrusher(player, (SteamCrusher) tile);
					}
				case 22:
					if (tile instanceof SteamTransformer) {
						return new ContainerSteamTransformer(player, (SteamTransformer) tile);
					}
				case 100:
					// if (tile instanceof TileFissionController) return new
					// ContainerFissionController(player,
					// (TileFissionController)
					// tile);
				case 101:
					if (tile instanceof TileSolidFissionController) {
						return new ContainerSolidFissionController(player, (TileSolidFissionController) tile);
					}
				case 102:
					if (tile instanceof TileSaltFissionController) {
						return new ContainerSaltFissionController(player, (TileSaltFissionController) tile);
					}
				case 103:
					if (tile instanceof TileHeatExchangerController) {
						return new ContainerHeatExchangerController(player, (TileHeatExchangerController) tile);
					}
				case 104:
					if (tile instanceof TileTurbineController) {
						return new ContainerTurbineController(player, (TileTurbineController) tile);
					}
				case 200:
					if (tile instanceof TileFissionIrradiator) {
						return new ContainerFissionIrradiator(player, (TileFissionIrradiator) tile);
					}
				case 201:
					if (tile instanceof TileSolidFissionCell) {
						return new ContainerSolidFissionCell(player, (TileSolidFissionCell) tile);
					}
				case 202:
					if (tile instanceof TileSaltFissionVessel) {
						return new ContainerSaltFissionVessel(player, (TileSaltFissionVessel) tile);
					}
				case 203:
					if (tile instanceof TileSaltFissionHeater) {
						return new ContainerSaltFissionHeater(player, (TileSaltFissionHeater) tile);
					}
				case 300:
					if (tile instanceof TileFissionIrradiatorPort) {
						return new ContainerFissionIrradiatorPort(player, (TileFissionIrradiatorPort) tile);
					}
				case 301:
					if (tile instanceof TileFissionCellPort) {
						return new ContainerFissionCellPort(player, (TileFissionCellPort) tile);
					}
				case 302:
					if (tile instanceof TileFissionVesselPort) {
						return new ContainerFissionVesselPort(player, (TileFissionVesselPort) tile);
					}
				case 303:
					if (tile instanceof TileFissionHeaterPort) {
						return new ContainerFissionHeaterPort(player, (TileFissionHeaterPort) tile);
					}
				case 1001:
					if (tile instanceof Manufactory) {
						return new ContainerMachineConfig(player, (Manufactory) tile);
					}
				case 1002:
					if (tile instanceof Separator) {
						return new ContainerMachineConfig(player, (Separator) tile);
					}
				case 1003:
					if (tile instanceof DecayHastener) {
						return new ContainerMachineConfig(player, (DecayHastener) tile);
					}
				case 1004:
					if (tile instanceof FuelReprocessor) {
						return new ContainerMachineConfig(player, (FuelReprocessor) tile);
					}
				case 1005:
					if (tile instanceof AlloyFurnace) {
						return new ContainerMachineConfig(player, (AlloyFurnace) tile);
					}
				case 1006:
					if (tile instanceof Infuser) {
						return new ContainerMachineConfig(player, (Infuser) tile);
					}
				case 1007:
					if (tile instanceof Melter) {
						return new ContainerMachineConfig(player, (Melter) tile);
					}
				case 1008:
					if (tile instanceof Supercooler) {
						return new ContainerMachineConfig(player, (Supercooler) tile);
					}
				case 1009:
					if (tile instanceof Electrolyzer) {
						return new ContainerMachineConfig(player, (Electrolyzer) tile);
					}
				case 1010:
					if (tile instanceof Assembler) {
						return new ContainerMachineConfig(player, (Assembler) tile);
					}
				case 1011:
					if (tile instanceof IngotFormer) {
						return new ContainerMachineConfig(player, (IngotFormer) tile);
					}
				case 1012:
					if (tile instanceof Pressurizer) {
						return new ContainerMachineConfig(player, (Pressurizer) tile);
					}
				case 1013:
					if (tile instanceof ChemicalReactor) {
						return new ContainerMachineConfig(player, (ChemicalReactor) tile);
					}
				case 1014:
					if (tile instanceof SaltMixer) {
						return new ContainerMachineConfig(player, (SaltMixer) tile);
					}
				case 1015:
					if (tile instanceof Crystallizer) {
						return new ContainerMachineConfig(player, (Crystallizer) tile);
					}
				case 1016:
					if (tile instanceof Enricher) {
						return new ContainerMachineConfig(player, (Enricher) tile);
					}
				case 1017:
					if (tile instanceof Extractor) {
						return new ContainerMachineConfig(player, (Extractor) tile);
					}
				case 1018:
					if (tile instanceof Centrifuge) {
						return new ContainerMachineConfig(player, (Centrifuge) tile);
					}
				case 1019:
					if (tile instanceof RockCrusher) {
						return new ContainerMachineConfig(player, (RockCrusher) tile);
					}
				case 1020:
					if (tile instanceof SteamFurnace) {
						return new ContainerMachineConfig(player, (SteamFurnace) tile);
					}
				case 1021:
					if (tile instanceof SteamCrusher) {
						return new ContainerMachineConfig(player, (SteamCrusher) tile);
					}
				case 1022:
					if (tile instanceof SteamTransformer) {
						return new ContainerMachineConfig(player, (SteamTransformer) tile);
					}
			}
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		if (tile != null) {
			switch (ID) {
				case 0:
					if (tile instanceof TileNuclearFurnace) {
						return new GuiNuclearFurnace(player, (TileNuclearFurnace) tile);
					}
				case 1:
					if (tile instanceof Manufactory) {
						return new GuiManufactory(player, (Manufactory) tile);
					}
				case 2:
					if (tile instanceof Separator) {
						return new GuiSeparator(player, (Separator) tile);
					}
				case 3:
					if (tile instanceof DecayHastener) {
						return new GuiDecayHastener(player, (DecayHastener) tile);
					}
				case 4:
					if (tile instanceof FuelReprocessor) {
						return new GuiFuelReprocessor(player, (FuelReprocessor) tile);
					}
				case 5:
					if (tile instanceof AlloyFurnace) {
						return new GuiAlloyFurnace(player, (AlloyFurnace) tile);
					}
				case 6:
					if (tile instanceof Infuser) {
						return new GuiInfuser(player, (Infuser) tile);
					}
				case 7:
					if (tile instanceof Melter) {
						return new GuiMelter(player, (Melter) tile);
					}
				case 8:
					if (tile instanceof Supercooler) {
						return new GuiSupercooler(player, (Supercooler) tile);
					}
				case 9:
					if (tile instanceof Electrolyzer) {
						return new GuiElectrolyzer(player, (Electrolyzer) tile);
					}
				case 10:
					if (tile instanceof Assembler) {
						return new GuiAssembler(player, (Assembler) tile);
					}
				case 11:
					if (tile instanceof IngotFormer) {
						return new GuiIngotFormer(player, (IngotFormer) tile);
					}
				case 12:
					if (tile instanceof Pressurizer) {
						return new GuiPressurizer(player, (Pressurizer) tile);
					}
				case 13:
					if (tile instanceof ChemicalReactor) {
						return new GuiChemicalReactor(player, (ChemicalReactor) tile);
					}
				case 14:
					if (tile instanceof SaltMixer) {
						return new GuiSaltMixer(player, (SaltMixer) tile);
					}
				case 15:
					if (tile instanceof Crystallizer) {
						return new GuiCrystallizer(player, (Crystallizer) tile);
					}
				case 16:
					if (tile instanceof Enricher) {
						return new GuiEnricher(player, (Enricher) tile);
					}
				case 17:
					if (tile instanceof Extractor) {
						return new GuiExtractor(player, (Extractor) tile);
					}
				case 18:
					if (tile instanceof Centrifuge) {
						return new GuiCentrifuge(player, (Centrifuge) tile);
					}
				case 19:
					if (tile instanceof RockCrusher) {
						return new GuiRockCrusher(player, (RockCrusher) tile);
					}
				case 20:
					if (tile instanceof SteamFurnace) {
						return new GuiSteamFurnace(player, (SteamFurnace) tile);
					}
				case 21:
					if (tile instanceof SteamCrusher) {
						return new GuiSteamCrusher(player, (SteamCrusher) tile);
					}
				case 22:
					if (tile instanceof SteamTransformer) {
						return new GuiSteamTransformer(player, (SteamTransformer) tile);
					}
				case 100:
					/*if (tile instanceof TileFissionController) {
						return new GuiFissionController(player, (TileFissionController) tile);
					}*/
				case 101:
					if (tile instanceof TileSolidFissionController) {
						return new GuiSolidFissionController(player, (TileSolidFissionController) tile);
					}
				case 102:
					if (tile instanceof TileSaltFissionController) {
						return new GuiSaltFissionController(player, ((TileSaltFissionController) tile));
					}
				case 103:
					if (tile instanceof TileHeatExchangerController) {
						return new GuiHeatExchangerController(player, ((TileHeatExchangerController) tile));
					}
				case 104:
					if (tile instanceof TileTurbineController) {
						return new GuiTurbineController(player, ((TileTurbineController) tile));
					}
				case 200:
					if (tile instanceof TileFissionIrradiator) {
						return new GuiFissionIrradiator(player, (TileFissionIrradiator) tile);
					}
				case 201:
					if (tile instanceof TileSolidFissionCell) {
						return new GuiSolidFissionCell(player, (TileSolidFissionCell) tile);
					}
				case 202:
					if (tile instanceof TileSaltFissionVessel) {
						return new GuiSaltFissionVessel(player, (TileSaltFissionVessel) tile);
					}
				case 203:
					if (tile instanceof TileSaltFissionHeater) {
						return new GuiSaltFissionHeater(player, (TileSaltFissionHeater) tile);
					}
				case 300:
					if (tile instanceof TileFissionIrradiatorPort) {
						return new GuiFissionIrradiatorPort(player, (TileFissionIrradiatorPort) tile);
					}
				case 301:
					if (tile instanceof TileFissionCellPort) {
						return new GuiFissionCellPort(player, (TileFissionCellPort) tile);
					}
				case 302:
					if (tile instanceof TileFissionVesselPort) {
						return new GuiFissionVesselPort(player, (TileFissionVesselPort) tile);
					}
				case 303:
					if (tile instanceof TileFissionHeaterPort) {
						return new GuiFissionHeaterPort(player, (TileFissionHeaterPort) tile);
					}
				case 1001:
					if (tile instanceof Manufactory) {
						return new GuiManufactory.SideConfig(player, (Manufactory) tile);
					}
				case 1002:
					if (tile instanceof Separator) {
						return new GuiSeparator.SideConfig(player, (Separator) tile);
					}
				case 1003:
					if (tile instanceof DecayHastener) {
						return new GuiDecayHastener.SideConfig(player, (DecayHastener) tile);
					}
				case 1004:
					if (tile instanceof FuelReprocessor) {
						return new GuiFuelReprocessor.SideConfig(player, (FuelReprocessor) tile);
					}
				case 1005:
					if (tile instanceof AlloyFurnace) {
						return new GuiAlloyFurnace.SideConfig(player, (AlloyFurnace) tile);
					}
				case 1006:
					if (tile instanceof Infuser) {
						return new GuiInfuser.SideConfig(player, (Infuser) tile);
					}
				case 1007:
					if (tile instanceof Melter) {
						return new GuiMelter.SideConfig(player, (Melter) tile);
					}
				case 1008:
					if (tile instanceof Supercooler) {
						return new GuiSupercooler.SideConfig(player, (Supercooler) tile);
					}
				case 1009:
					if (tile instanceof Electrolyzer) {
						return new GuiElectrolyzer.SideConfig(player, (Electrolyzer) tile);
					}
				case 1010:
					if (tile instanceof Assembler) {
						return new GuiAssembler.SideConfig(player, (Assembler) tile);
					}
				case 1011:
					if (tile instanceof IngotFormer) {
						return new GuiIngotFormer.SideConfig(player, (IngotFormer) tile);
					}
				case 1012:
					if (tile instanceof Pressurizer) {
						return new GuiPressurizer.SideConfig(player, (Pressurizer) tile);
					}
				case 1013:
					if (tile instanceof ChemicalReactor) {
						return new GuiChemicalReactor.SideConfig(player, (ChemicalReactor) tile);
					}
				case 1014:
					if (tile instanceof SaltMixer) {
						return new GuiSaltMixer.SideConfig(player, (SaltMixer) tile);
					}
				case 1015:
					if (tile instanceof Crystallizer) {
						return new GuiCrystallizer.SideConfig(player, (Crystallizer) tile);
					}
				case 1016:
					if (tile instanceof Enricher) {
						return new GuiEnricher.SideConfig(player, (Enricher) tile);
					}
				case 1017:
					if (tile instanceof Extractor) {
						return new GuiExtractor.SideConfig(player, (Extractor) tile);
					}
				case 1018:
					if (tile instanceof Centrifuge) {
						return new GuiCentrifuge.SideConfig(player, (Centrifuge) tile);
					}
				case 1019:
					if (tile instanceof RockCrusher) {
						return new GuiRockCrusher.SideConfig(player, (RockCrusher) tile);
					}
				case 1020:
					if (tile instanceof SteamFurnace) {
						return new GuiSteamFurnace.SideConfig(player, (SteamFurnace) tile);
					}
				case 1021:
					if (tile instanceof SteamCrusher) {
						return new GuiSteamCrusher.SideConfig(player, (SteamCrusher) tile);
					}
				case 1022:
					if (tile instanceof SteamTransformer) {
						return new GuiSteamCrusher.SideConfig(player, (SteamTransformer) tile);
					}
			}
		}
		
		return null;
	}
	
}
