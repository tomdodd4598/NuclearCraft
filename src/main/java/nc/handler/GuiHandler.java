package nc.handler;

import nc.container.generator.ContainerFissionController;
import nc.container.generator.ContainerFusionCore;
import nc.container.processor.ContainerAlloyFurnace;
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
import nc.container.processor.ContainerSaltMixer;
import nc.container.processor.ContainerSupercooler;
import nc.gui.generator.GuiFissionController;
import nc.gui.generator.GuiFusionCore;
import nc.gui.processor.GuiAlloyFurnace;
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
import nc.gui.processor.GuiSaltMixer;
import nc.gui.processor.GuiSupercooler;
import nc.tile.generator.TileFissionController;
import nc.tile.generator.TileFusionCore;
import nc.tile.processor.TileNuclearFurnace;
import nc.tile.processor.TileProcessor.AlloyFurnace;
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
import nc.tile.processor.TileProcessor.SaltMixer;
import nc.tile.processor.TileProcessor.Supercooler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler {
	
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory entity = (IInventory) world.getTileEntity(new BlockPos(x, y, z));
		
		if (entity != null) {
			switch(ID) {
			case 0:
				if (entity instanceof TileNuclearFurnace) return new ContainerNuclearFurnace(player, entity);
			case 1:
				if (entity instanceof Manufactory) return new ContainerManufactory(player, (Manufactory) entity);
			case 2:
				if (entity instanceof IsotopeSeparator) return new ContainerIsotopeSeparator(player, (IsotopeSeparator) entity);
			case 3:
				if (entity instanceof DecayHastener) return new ContainerDecayHastener(player, (DecayHastener) entity);
			case 4:
				if (entity instanceof FuelReprocessor) return new ContainerFuelReprocessor(player, (FuelReprocessor) entity);
			case 5:
				if (entity instanceof AlloyFurnace) return new ContainerAlloyFurnace(player, (AlloyFurnace) entity);
			case 6:
				if (entity instanceof Infuser) return new ContainerInfuser(player, (Infuser) entity);
			case 7:
				if (entity instanceof Melter) return new ContainerMelter(player, (Melter) entity);
			case 8:
				if (entity instanceof Supercooler) return new ContainerSupercooler(player, (Supercooler) entity);
			case 9:
				if (entity instanceof Electrolyser) return new ContainerElectrolyser(player, (Electrolyser) entity);
			case 10:
				if (entity instanceof Irradiator) return new ContainerIrradiator(player, (Irradiator) entity);
			case 11:
				if (entity instanceof IngotFormer) return new ContainerIngotFormer(player, (IngotFormer) entity);
			case 12:
				if (entity instanceof Pressurizer) return new ContainerPressurizer(player, (Pressurizer) entity);
			case 13:
				if (entity instanceof ChemicalReactor) return new ContainerChemicalReactor(player, (ChemicalReactor) entity);
			case 14:
				if (entity instanceof SaltMixer) return new ContainerSaltMixer(player, (SaltMixer) entity);
			case 15:
				if (entity instanceof Crystallizer) return new ContainerCrystallizer(player, (Crystallizer) entity);
			case 16:
				if (entity instanceof Dissolver) return new ContainerDissolver(player, (Dissolver) entity);
			case 17:
				if (entity instanceof Extractor) return new ContainerExtractor(player, (Extractor) entity);
			case 100:
				if (entity instanceof TileFissionController) return new ContainerFissionController(player, (TileFissionController) entity);
			case 101:
				if (entity instanceof TileFusionCore) return new ContainerFusionCore(player, (TileFusionCore) entity);
			}
		}
		
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		IInventory entity = (IInventory) world.getTileEntity(new BlockPos(x, y, z));
		
		if (entity != null) {
			switch(ID) {
			case 0:
				if (entity instanceof TileNuclearFurnace) return new GuiNuclearFurnace(player, entity);
			case 1:
				if (entity instanceof Manufactory) return new GuiManufactory(player, (Manufactory) entity);
			case 2:
				if (entity instanceof IsotopeSeparator) return new GuiIsotopeSeparator(player, (IsotopeSeparator) entity);
			case 3:
				if (entity instanceof DecayHastener) return new GuiDecayHastener(player, (DecayHastener) entity);
			case 4:
				if (entity instanceof FuelReprocessor) return new GuiFuelReprocessor(player, (FuelReprocessor) entity);
			case 5:
				if (entity instanceof AlloyFurnace) return new GuiAlloyFurnace(player, (AlloyFurnace) entity);
			case 6:
				if (entity instanceof Infuser) return new GuiInfuser(player, (Infuser) entity);
			case 7:
				if (entity instanceof Melter) return new GuiMelter(player, (Melter) entity);
			case 8:
				if (entity instanceof Supercooler) return new GuiSupercooler(player, (Supercooler) entity);
			case 9:
				if (entity instanceof Electrolyser) return new GuiElectrolyser(player, (Electrolyser) entity);
			case 10:
				if (entity instanceof Irradiator) return new GuiIrradiator(player, (Irradiator) entity);
			case 11:
				if (entity instanceof IngotFormer) return new GuiIngotFormer(player, (IngotFormer) entity);
			case 12:
				if (entity instanceof Pressurizer) return new GuiPressurizer(player, (Pressurizer) entity);
			case 13:
				if (entity instanceof ChemicalReactor) return new GuiChemicalReactor(player, (ChemicalReactor) entity);
			case 14:
				if (entity instanceof SaltMixer) return new GuiSaltMixer(player, (SaltMixer) entity);
			case 15:
				if (entity instanceof Crystallizer) return new GuiCrystallizer(player, (Crystallizer) entity);
			case 16:
				if (entity instanceof Dissolver) return new GuiDissolver(player, (Dissolver) entity);
			case 17:
				if (entity instanceof Extractor) return new GuiExtractor(player, (Extractor) entity);
			case 100:
				if (entity instanceof TileFissionController) return new GuiFissionController(player, (TileFissionController) entity);
			case 101:
				if (entity instanceof TileFusionCore) return new GuiFusionCore(player, (TileFusionCore) entity);
			}
		}
		
		return null;
	}

}
