package nc.container.processor;

import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerProcessorImpl {
	
	public static abstract class ContainerBasicProcessor<TILE extends TileBasicProcessor<TILE>> extends ContainerProcessor<TILE, BasicProcessorContainerInfo<TILE>> {
		
		public ContainerBasicProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static abstract class ContainerBasicUpgradableProcessor<TILE extends TileBasicUpgradableProcessor<TILE>> extends ContainerUpgradableProcessor<TILE, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		public ContainerBasicUpgradableProcessor(EntityPlayer player, TILE tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerManufactory extends ContainerBasicUpgradableProcessor<Manufactory> {
		
		public ContainerManufactory(EntityPlayer player, Manufactory tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSeparator extends ContainerBasicUpgradableProcessor<Separator> {
		
		public ContainerSeparator(EntityPlayer player, Separator tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerDecayHastener extends ContainerBasicUpgradableProcessor<DecayHastener> {
		
		public ContainerDecayHastener(EntityPlayer player, DecayHastener tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerFuelReprocessor extends ContainerBasicUpgradableProcessor<FuelReprocessor> {
		
		public ContainerFuelReprocessor(EntityPlayer player, FuelReprocessor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAlloyFurnace extends ContainerBasicUpgradableProcessor<AlloyFurnace> {
		
		public ContainerAlloyFurnace(EntityPlayer player, AlloyFurnace tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerInfuser extends ContainerBasicUpgradableProcessor<Infuser> {
		
		public ContainerInfuser(EntityPlayer player, Infuser tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerMelter extends ContainerBasicUpgradableProcessor<Melter> {
		
		public ContainerMelter(EntityPlayer player, Melter tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSupercooler extends ContainerBasicUpgradableProcessor<Supercooler> {
		
		public ContainerSupercooler(EntityPlayer player, Supercooler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerElectrolyzer extends ContainerBasicUpgradableProcessor<Electrolyzer> {
		
		public ContainerElectrolyzer(EntityPlayer player, Electrolyzer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerAssembler extends ContainerBasicUpgradableProcessor<Assembler> {
		
		public ContainerAssembler(EntityPlayer player, Assembler tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerIngotFormer extends ContainerBasicUpgradableProcessor<IngotFormer> {
		
		public ContainerIngotFormer(EntityPlayer player, IngotFormer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerPressurizer extends ContainerBasicUpgradableProcessor<Pressurizer> {
		
		public ContainerPressurizer(EntityPlayer player, Pressurizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerChemicalReactor extends ContainerBasicUpgradableProcessor<ChemicalReactor> {
		
		public ContainerChemicalReactor(EntityPlayer player, ChemicalReactor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerSaltMixer extends ContainerBasicUpgradableProcessor<SaltMixer> {
		
		public ContainerSaltMixer(EntityPlayer player, SaltMixer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCrystallizer extends ContainerBasicUpgradableProcessor<Crystallizer> {
		
		public ContainerCrystallizer(EntityPlayer player, Crystallizer tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerEnricher extends ContainerBasicUpgradableProcessor<Enricher> {
		
		public ContainerEnricher(EntityPlayer player, Enricher tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerExtractor extends ContainerBasicUpgradableProcessor<Extractor> {
		
		public ContainerExtractor(EntityPlayer player, Extractor tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerCentrifuge extends ContainerBasicUpgradableProcessor<Centrifuge> {
		
		public ContainerCentrifuge(EntityPlayer player, Centrifuge tile) {
			super(player, tile);
		}
	}
	
	public static class ContainerRockCrusher extends ContainerBasicUpgradableProcessor<RockCrusher> {
		
		public ContainerRockCrusher(EntityPlayer player, RockCrusher tile) {
			super(player, tile);
		}
	}
}
