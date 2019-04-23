package nc.handler;

import nc.container.generator.ContainerFissionController;
import nc.container.generator.ContainerFusionCore;
import nc.container.processor.ContainerAlloyFurnace;
import nc.container.processor.ContainerCentrifuge;
import nc.container.processor.ContainerChemicalReactor;
import nc.container.processor.ContainerCrystallizer;
import nc.container.processor.ContainerDecayHastener;
import nc.container.processor.ContainerDissolver;
import nc.container.processor.ContainerElectrolyser;
import nc.container.processor.ContainerExtractor;
import nc.container.processor.ContainerFuelReprocessor;
import nc.container.processor.ContainerInfuser;
import nc.container.processor.ContainerIngotFormer;
import nc.container.processor.ContainerIrradiator;
import nc.container.processor.ContainerIsotopeSeparator;
import nc.container.processor.ContainerManufactory;
import nc.container.processor.ContainerMelter;
import nc.container.processor.ContainerNuclearFurnace;
import nc.container.processor.ContainerPressurizer;
import nc.container.processor.ContainerRockCrusher;
import nc.container.processor.ContainerSaltMixer;
import nc.container.processor.ContainerSupercooler;
import nc.gui.generator.GuiFissionController;
import nc.gui.generator.GuiFusionCore;
import nc.gui.processor.GuiAlloyFurnace;
import nc.gui.processor.GuiCentrifuge;
import nc.gui.processor.GuiChemicalReactor;
import nc.gui.processor.GuiCrystallizer;
import nc.gui.processor.GuiDecayHastener;
import nc.gui.processor.GuiDissolver;
import nc.gui.processor.GuiElectrolyser;
import nc.gui.processor.GuiExtractor;
import nc.gui.processor.GuiFuelReprocessor;
import nc.gui.processor.GuiInfuser;
import nc.gui.processor.GuiIngotFormer;
import nc.gui.processor.GuiIrradiator;
import nc.gui.processor.GuiIsotopeSeparator;
import nc.gui.processor.GuiManufactory;
import nc.gui.processor.GuiMelter;
import nc.gui.processor.GuiNuclearFurnace;
import nc.gui.processor.GuiPressurizer;
import nc.gui.processor.GuiRockCrusher;
import nc.gui.processor.GuiSaltMixer;
import nc.gui.processor.GuiSupercooler;
import nc.multiblock.container.ContainerHeatExchangerController;
import nc.multiblock.container.ContainerSaltFissionController;
import nc.multiblock.container.ContainerTurbineController;
import nc.multiblock.gui.GuiHeatExchangerController;
import nc.multiblock.gui.GuiSaltFissionController;
import nc.multiblock.gui.GuiTurbineController;
import nc.multiblock.heatExchanger.tile.TileHeatExchangerController;
import nc.multiblock.saltFission.tile.TileSaltFissionController;
import nc.multiblock.turbine.tile.TileTurbineController;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileFusionCore;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.TileProcessor.AlloyFurnace;
import nc.tile.processor.TileProcessor.Centrifuge;
import nc.tile.processor.TileProcessor.ChemicalReactor;
import nc.tile.processor.TileProcessor.Crystallizer;
import nc.tile.processor.TileProcessor.DecayHastener;
import nc.tile.processor.TileProcessor.Dissolver;
import nc.tile.processor.TileProcessor.Electrolyser;
import nc.tile.processor.TileProcessor.Extractor;
import nc.tile.processor.TileProcessor.FuelReprocessor;
import nc.tile.processor.TileProcessor.Infuser;
import nc.tile.processor.TileProcessor.IngotFormer;
import nc.tile.processor.TileProcessor.Irradiator;
import nc.tile.processor.TileProcessor.IsotopeSeparator;
import nc.tile.processor.TileProcessor.Manufactory;
import nc.tile.processor.TileProcessor.Melter;
import nc.tile.processor.TileProcessor.Pressurizer;
import nc.tile.processor.TileProcessor.RockCrusher;
import nc.tile.processor.TileProcessor.SaltMixer;
import nc.tile.processor.TileProcessor.Supercooler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if (tile != null) {
			switch(ID) {
			case 0:
				if (tile instanceof TileNuclearFurnace) return new ContainerNuclearFurnace(player, (IInventory) tile);
			case 1:
				if (tile instanceof Manufactory) return new ContainerManufactory(player, (Manufactory) tile);
			case 2:
				if (tile instanceof IsotopeSeparator) return new ContainerIsotopeSeparator(player, (IsotopeSeparator) tile);
			case 3:
				if (tile instanceof DecayHastener) return new ContainerDecayHastener(player, (DecayHastener) tile);
			case 4:
				if (tile instanceof FuelReprocessor) return new ContainerFuelReprocessor(player, (FuelReprocessor) tile);
			case 5:
				if (tile instanceof AlloyFurnace) return new ContainerAlloyFurnace(player, (AlloyFurnace) tile);
			case 6:
				if (tile instanceof Infuser) return new ContainerInfuser(player, (Infuser) tile);
			case 7:
				if (tile instanceof Melter) return new ContainerMelter(player, (Melter) tile);
			case 8:
				if (tile instanceof Supercooler) return new ContainerSupercooler(player, (Supercooler) tile);
			case 9:
				if (tile instanceof Electrolyser) return new ContainerElectrolyser(player, (Electrolyser) tile);
			case 10:
				if (tile instanceof Irradiator) return new ContainerIrradiator(player, (Irradiator) tile);
			case 11:
				if (tile instanceof IngotFormer) return new ContainerIngotFormer(player, (IngotFormer) tile);
			case 12:
				if (tile instanceof Pressurizer) return new ContainerPressurizer(player, (Pressurizer) tile);
			case 13:
				if (tile instanceof ChemicalReactor) return new ContainerChemicalReactor(player, (ChemicalReactor) tile);
			case 14:
				if (tile instanceof SaltMixer) return new ContainerSaltMixer(player, (SaltMixer) tile);
			case 15:
				if (tile instanceof Crystallizer) return new ContainerCrystallizer(player, (Crystallizer) tile);
			case 16:
				if (tile instanceof Dissolver) return new ContainerDissolver(player, (Dissolver) tile);
			case 17:
				if (tile instanceof Extractor) return new ContainerExtractor(player, (Extractor) tile);
			case 18:
				if (tile instanceof Centrifuge) return new ContainerCentrifuge(player, (Centrifuge) tile);
			case 19:
				if (tile instanceof RockCrusher) return new ContainerRockCrusher(player, (RockCrusher) tile);
			case 100:
				if (tile instanceof TileFissionController) return new ContainerFissionController(player, (TileFissionController) tile);
			case 101:
				if (tile instanceof TileFusionCore) return new ContainerFusionCore(player, (TileFusionCore) tile);
			case 102:
				if (tile instanceof TileSaltFissionController) return new ContainerSaltFissionController(player, (TileSaltFissionController) tile);
			case 103:
				if (tile instanceof TileHeatExchangerController) return new ContainerHeatExchangerController(player, (TileHeatExchangerController) tile);
			case 104:
				if (tile instanceof TileTurbineController) return new ContainerTurbineController(player, (TileTurbineController) tile);
			}
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tile = world.getTileEntity(new BlockPos(x, y, z));
		
		if (tile != null) {
			switch(ID) {
			case 0:
				if (tile instanceof TileNuclearFurnace) return new GuiNuclearFurnace(player, (IInventory) tile);
			case 1:
				if (tile instanceof Manufactory) return new GuiManufactory(player, (Manufactory) tile);
			case 2:
				if (tile instanceof IsotopeSeparator) return new GuiIsotopeSeparator(player, (IsotopeSeparator) tile);
			case 3:
				if (tile instanceof DecayHastener) return new GuiDecayHastener(player, (DecayHastener) tile);
			case 4:
				if (tile instanceof FuelReprocessor) return new GuiFuelReprocessor(player, (FuelReprocessor) tile);
			case 5:
				if (tile instanceof AlloyFurnace) return new GuiAlloyFurnace(player, (AlloyFurnace) tile);
			case 6:
				if (tile instanceof Infuser) return new GuiInfuser(player, (Infuser) tile);
			case 7:
				if (tile instanceof Melter) return new GuiMelter(player, (Melter) tile);
			case 8:
				if (tile instanceof Supercooler) return new GuiSupercooler(player, (Supercooler) tile);
			case 9:
				if (tile instanceof Electrolyser) return new GuiElectrolyser(player, (Electrolyser) tile);
			case 10:
				if (tile instanceof Irradiator) return new GuiIrradiator(player, (Irradiator) tile);
			case 11:
				if (tile instanceof IngotFormer) return new GuiIngotFormer(player, (IngotFormer) tile);
			case 12:
				if (tile instanceof Pressurizer) return new GuiPressurizer(player, (Pressurizer) tile);
			case 13:
				if (tile instanceof ChemicalReactor) return new GuiChemicalReactor(player, (ChemicalReactor) tile);
			case 14:
				if (tile instanceof SaltMixer) return new GuiSaltMixer(player, (SaltMixer) tile);
			case 15:
				if (tile instanceof Crystallizer) return new GuiCrystallizer(player, (Crystallizer) tile);
			case 16:
				if (tile instanceof Dissolver) return new GuiDissolver(player, (Dissolver) tile);
			case 17:
				if (tile instanceof Extractor) return new GuiExtractor(player, (Extractor) tile);
			case 18:
				if (tile instanceof Centrifuge) return new GuiCentrifuge(player, (Centrifuge) tile);
			case 19:
				if (tile instanceof RockCrusher) return new GuiRockCrusher(player, (RockCrusher) tile);
			case 100:
				if (tile instanceof TileFissionController) return new GuiFissionController(player, (TileFissionController) tile);
			case 101:
				if (tile instanceof TileFusionCore) return new GuiFusionCore(player, (TileFusionCore) tile);
			case 102:
				if (tile instanceof TileSaltFissionController) return new GuiSaltFissionController(((TileSaltFissionController) tile).getMultiblock(), tile.getPos(), ((TileSaltFissionController) tile).getMultiblock().getContainer(player));
			case 103:
				if (tile instanceof TileHeatExchangerController) return new GuiHeatExchangerController(((TileHeatExchangerController) tile).getMultiblock(), tile.getPos(), ((TileHeatExchangerController) tile).getMultiblock().getContainer(player));
			case 104:
				if (tile instanceof TileTurbineController) return new GuiTurbineController(((TileTurbineController) tile).getMultiblock(), tile.getPos(), ((TileTurbineController) tile).getMultiblock().getContainer(player));
			}
		}
		
		return null;
	}

}
