package nc.container.processor;

import nc.network.tile.multiblock.*;
import nc.network.tile.processor.EnergyProcessorUpdatePacket;
import nc.tile.fission.*;
import nc.tile.fission.TileFissionIrradiator.FissionIrradiatorContainerInfo;
import nc.tile.fission.TileSaltFissionHeater.SaltFissionHeaterContainerInfo;
import nc.tile.fission.TileSaltFissionVessel.SaltFissionVesselContainerInfo;
import nc.tile.fission.TileSolidFissionCell.SolidFissionCellContainerInfo;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProcessorImpl {
	
	public static class ContainerBasicProcessor<TILE extends TileBasicProcessor<TILE>> extends ContainerProcessor<TILE, EnergyProcessorUpdatePacket, BasicProcessorContainerInfo<TILE>> {
		
		public ContainerBasicProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicUpgradableProcessor<TILE extends TileBasicUpgradableProcessor<TILE>> extends ContainerUpgradableProcessor<TILE, EnergyProcessorUpdatePacket, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		public ContainerBasicUpgradableProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicProcessorDyn extends ContainerBasicProcessor<TileBasicProcessorDyn> {
		
		public ContainerBasicProcessorDyn(EntityPlayer player, TileBasicProcessorDyn tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicUpgradableProcessorDyn extends ContainerBasicUpgradableProcessor<TileBasicUpgradableProcessorDyn> {
		
		public ContainerBasicUpgradableProcessorDyn(EntityPlayer player, TileBasicUpgradableProcessorDyn tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerManufactory extends ContainerBasicUpgradableProcessor<TileManufactory> {
		
		public ContainerManufactory(EntityPlayer player, TileManufactory tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSeparator extends ContainerBasicUpgradableProcessor<TileSeparator> {
		
		public ContainerSeparator(EntityPlayer player, TileSeparator tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerDecayHastener extends ContainerBasicUpgradableProcessor<TileDecayHastener> {
		
		public ContainerDecayHastener(EntityPlayer player, TileDecayHastener tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerFuelReprocessor extends ContainerBasicUpgradableProcessor<TileFuelReprocessor> {
		
		public ContainerFuelReprocessor(EntityPlayer player, TileFuelReprocessor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAlloyFurnace extends ContainerBasicUpgradableProcessor<TileAlloyFurnace> {
		
		public ContainerAlloyFurnace(EntityPlayer player, TileAlloyFurnace tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerInfuser extends ContainerBasicUpgradableProcessor<TileInfuser> {
		
		public ContainerInfuser(EntityPlayer player, TileInfuser tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerMelter extends ContainerBasicUpgradableProcessor<TileMelter> {
		
		public ContainerMelter(EntityPlayer player, TileMelter tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSupercooler extends ContainerBasicUpgradableProcessor<TileSupercooler> {
		
		public ContainerSupercooler(EntityPlayer player, TileSupercooler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerElectrolyzer extends ContainerBasicUpgradableProcessor<TileElectrolyzer> {
		
		public ContainerElectrolyzer(EntityPlayer player, TileElectrolyzer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAssembler extends ContainerBasicUpgradableProcessor<TileAssembler> {
		
		public ContainerAssembler(EntityPlayer player, TileAssembler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerIngotFormer extends ContainerBasicUpgradableProcessor<TileIngotFormer> {
		
		public ContainerIngotFormer(EntityPlayer player, TileIngotFormer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerPressurizer extends ContainerBasicUpgradableProcessor<TilePressurizer> {
		
		public ContainerPressurizer(EntityPlayer player, TilePressurizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerChemicalReactor extends ContainerBasicUpgradableProcessor<TileChemicalReactor> {
		
		public ContainerChemicalReactor(EntityPlayer player, TileChemicalReactor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSaltMixer extends ContainerBasicUpgradableProcessor<TileSaltMixer> {
		
		public ContainerSaltMixer(EntityPlayer player, TileSaltMixer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCrystallizer extends ContainerBasicUpgradableProcessor<TileCrystallizer> {
		
		public ContainerCrystallizer(EntityPlayer player, TileCrystallizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerEnricher extends ContainerBasicUpgradableProcessor<TileEnricher> {
		
		public ContainerEnricher(EntityPlayer player, TileEnricher tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerExtractor extends ContainerBasicUpgradableProcessor<TileExtractor> {
		
		public ContainerExtractor(EntityPlayer player, TileExtractor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCentrifuge extends ContainerBasicUpgradableProcessor<TileCentrifuge> {
		
		public ContainerCentrifuge(EntityPlayer player, TileCentrifuge tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerRockCrusher extends ContainerBasicUpgradableProcessor<TileRockCrusher> {
		
		public ContainerRockCrusher(EntityPlayer player, TileRockCrusher tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerRadiationScrubber extends ContainerBasicProcessor<TileRadiationScrubber> {
		
		public ContainerRadiationScrubber(EntityPlayer player, TileRadiationScrubber tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerFissionIrradiator extends ContainerFilteredProcessor<TileFissionIrradiator, FissionIrradiatorUpdatePacket, FissionIrradiatorContainerInfo> {
		
		public ContainerFissionIrradiator(EntityPlayer player, TileFissionIrradiator irradiator) {
			super(player, irradiator);
		}
	}
	
	public static class ContainerSolidFissionCell extends ContainerFilteredProcessor<TileSolidFissionCell, SolidFissionCellUpdatePacket, SolidFissionCellContainerInfo> {
		
		public ContainerSolidFissionCell(EntityPlayer player, TileSolidFissionCell cell) {
			super(player, cell);
		}
	}
	
	public static class ContainerSaltFissionVessel extends ContainerProcessor<TileSaltFissionVessel, SaltFissionVesselUpdatePacket, SaltFissionVesselContainerInfo> {
		
		public ContainerSaltFissionVessel(EntityPlayer player, TileSaltFissionVessel vessel) {
			super(player, vessel);
		}
	}
	
	public static class ContainerSaltFissionHeater extends ContainerProcessor<TileSaltFissionHeater, SaltFissionHeaterUpdatePacket, SaltFissionHeaterContainerInfo> {
		
		public ContainerSaltFissionHeater(EntityPlayer player, TileSaltFissionHeater heater) {
			super(player, heater);
		}
	}
}
