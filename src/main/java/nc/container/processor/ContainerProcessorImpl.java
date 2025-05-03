package nc.container.processor;

import nc.network.tile.multiblock.*;
import nc.network.tile.processor.*;
import nc.tile.fission.*;
import nc.tile.inventory.ITileFilteredInventory;
import nc.tile.processor.*;
import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

public class ContainerProcessorImpl {
	
	public static class ContainerBasicProcessor<TILE extends TileEntity & IBasicProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ContainerProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>> {
		
		public ContainerBasicProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicUpgradableProcessor<TILE extends TileEntity & IBasicUpgradableProcessor<TILE, PACKET>, PACKET extends ProcessorUpdatePacket> extends ContainerUpgradableProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicUpgradableProcessorContainerInfo<TILE, PACKET>> {
		
		public ContainerBasicUpgradableProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicFilteredProcessor<TILE extends TileEntity & IBasicProcessor<TILE, PACKET> & ITileFilteredInventory, PACKET extends ProcessorUpdatePacket> extends ContainerFilteredProcessor<TILE, PACKET, ProcessorContainerInfoImpl.BasicProcessorContainerInfo<TILE, PACKET>> {
		
		public ContainerBasicFilteredProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicEnergyProcessor<TILE extends TileBasicEnergyProcessor<TILE>> extends ContainerBasicProcessor<TILE, EnergyProcessorUpdatePacket> {
		
		public ContainerBasicEnergyProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicUpgradableEnergyProcessor<TILE extends TileBasicUpgradableEnergyProcessor<TILE>> extends ContainerBasicUpgradableProcessor<TILE, EnergyProcessorUpdatePacket> {
		
		public ContainerBasicUpgradableEnergyProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicEnergyProcessorDyn extends ContainerBasicEnergyProcessor<TileBasicEnergyProcessorDyn> {
		
		public ContainerBasicEnergyProcessorDyn(EntityPlayer player, TileBasicEnergyProcessorDyn tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerBasicUpgradableEnergyProcessorDyn extends ContainerBasicUpgradableEnergyProcessor<TileBasicUpgradableEnergyProcessorDyn> {
		
		public ContainerBasicUpgradableEnergyProcessorDyn(EntityPlayer player, TileBasicUpgradableEnergyProcessorDyn tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerManufactory extends ContainerBasicUpgradableEnergyProcessor<TileManufactory> {
		
		public ContainerManufactory(EntityPlayer player, TileManufactory tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSeparator extends ContainerBasicUpgradableEnergyProcessor<TileSeparator> {
		
		public ContainerSeparator(EntityPlayer player, TileSeparator tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerDecayHastener extends ContainerBasicUpgradableEnergyProcessor<TileDecayHastener> {
		
		public ContainerDecayHastener(EntityPlayer player, TileDecayHastener tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerFuelReprocessor extends ContainerBasicUpgradableEnergyProcessor<TileFuelReprocessor> {
		
		public ContainerFuelReprocessor(EntityPlayer player, TileFuelReprocessor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAlloyFurnace extends ContainerBasicUpgradableEnergyProcessor<TileAlloyFurnace> {
		
		public ContainerAlloyFurnace(EntityPlayer player, TileAlloyFurnace tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerInfuser extends ContainerBasicUpgradableEnergyProcessor<TileInfuser> {
		
		public ContainerInfuser(EntityPlayer player, TileInfuser tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerMelter extends ContainerBasicUpgradableEnergyProcessor<TileMelter> {
		
		public ContainerMelter(EntityPlayer player, TileMelter tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSupercooler extends ContainerBasicUpgradableEnergyProcessor<TileSupercooler> {
		
		public ContainerSupercooler(EntityPlayer player, TileSupercooler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerElectrolyzer extends ContainerBasicUpgradableEnergyProcessor<TileElectrolyzer> {
		
		public ContainerElectrolyzer(EntityPlayer player, TileElectrolyzer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAssembler extends ContainerBasicUpgradableEnergyProcessor<TileAssembler> {
		
		public ContainerAssembler(EntityPlayer player, TileAssembler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerIngotFormer extends ContainerBasicUpgradableEnergyProcessor<TileIngotFormer> {
		
		public ContainerIngotFormer(EntityPlayer player, TileIngotFormer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerPressurizer extends ContainerBasicUpgradableEnergyProcessor<TilePressurizer> {
		
		public ContainerPressurizer(EntityPlayer player, TilePressurizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerChemicalReactor extends ContainerBasicUpgradableEnergyProcessor<TileChemicalReactor> {
		
		public ContainerChemicalReactor(EntityPlayer player, TileChemicalReactor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSaltMixer extends ContainerBasicUpgradableEnergyProcessor<TileSaltMixer> {
		
		public ContainerSaltMixer(EntityPlayer player, TileSaltMixer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCrystallizer extends ContainerBasicUpgradableEnergyProcessor<TileCrystallizer> {
		
		public ContainerCrystallizer(EntityPlayer player, TileCrystallizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerEnricher extends ContainerBasicUpgradableEnergyProcessor<TileEnricher> {
		
		public ContainerEnricher(EntityPlayer player, TileEnricher tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerExtractor extends ContainerBasicUpgradableEnergyProcessor<TileExtractor> {
		
		public ContainerExtractor(EntityPlayer player, TileExtractor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCentrifuge extends ContainerBasicUpgradableEnergyProcessor<TileCentrifuge> {
		
		public ContainerCentrifuge(EntityPlayer player, TileCentrifuge tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerRockCrusher extends ContainerBasicUpgradableEnergyProcessor<TileRockCrusher> {
		
		public ContainerRockCrusher(EntityPlayer player, TileRockCrusher tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerElectricFurnace extends ContainerBasicUpgradableEnergyProcessor<TileElectricFurnace> {
		
		public ContainerElectricFurnace(EntityPlayer player, TileElectricFurnace tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerRadiationScrubber extends ContainerBasicEnergyProcessor<TileRadiationScrubber> {
		
		public ContainerRadiationScrubber(EntityPlayer player, TileRadiationScrubber tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerFissionIrradiator extends ContainerBasicFilteredProcessor<TileFissionIrradiator, FissionIrradiatorUpdatePacket> {
		
		public ContainerFissionIrradiator(EntityPlayer player, TileFissionIrradiator irradiator) {
			super(player, irradiator);
		}
	}
	
	public static class ContainerSolidFissionCell extends ContainerBasicFilteredProcessor<TileSolidFissionCell, SolidFissionCellUpdatePacket> {
		
		public ContainerSolidFissionCell(EntityPlayer player, TileSolidFissionCell cell) {
			super(player, cell);
		}
	}
	
	public static class ContainerSaltFissionVessel extends ContainerBasicProcessor<TileSaltFissionVessel, SaltFissionVesselUpdatePacket> {
		
		public ContainerSaltFissionVessel(EntityPlayer player, TileSaltFissionVessel vessel) {
			super(player, vessel);
		}
	}
	
	public static class ContainerSaltFissionHeater extends ContainerBasicProcessor<TileSaltFissionHeater, SaltFissionHeaterUpdatePacket> {
		
		public ContainerSaltFissionHeater(EntityPlayer player, TileSaltFissionHeater heater) {
			super(player, heater);
		}
	}
}
