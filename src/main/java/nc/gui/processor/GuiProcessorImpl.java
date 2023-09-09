package nc.gui.processor;

import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiProcessorImpl {
	
	public static abstract class GuiBasicProcessor<TILE extends TileBasicProcessor<TILE>> extends GuiProcessor<TILE, BasicProcessorContainerInfo<TILE>> {
		
		public GuiBasicProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static abstract class GuiBasicUpgradableProcessor<TILE extends TileBasicUpgradableProcessor<TILE>> extends GuiUpgradableProcessor<TILE, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		public GuiBasicUpgradableProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiManufactory extends GuiBasicUpgradableProcessor<Manufactory> {
		
		public GuiManufactory(Container inventory, EntityPlayer player, Manufactory tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSeparator extends GuiBasicUpgradableProcessor<Separator> {
		
		public GuiSeparator(Container inventory, EntityPlayer player, Separator tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiDecayHastener extends GuiBasicUpgradableProcessor<DecayHastener> {
		
		public GuiDecayHastener(Container inventory, EntityPlayer player, DecayHastener tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiFuelReprocessor extends GuiBasicUpgradableProcessor<FuelReprocessor> {
		
		public GuiFuelReprocessor(Container inventory, EntityPlayer player, FuelReprocessor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiAlloyFurnace extends GuiBasicUpgradableProcessor<AlloyFurnace> {
		
		public GuiAlloyFurnace(Container inventory, EntityPlayer player, AlloyFurnace tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiInfuser extends GuiBasicUpgradableProcessor<Infuser> {
		
		public GuiInfuser(Container inventory, EntityPlayer player, Infuser tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiMelter extends GuiBasicUpgradableProcessor<Melter> {
		
		public GuiMelter(Container inventory, EntityPlayer player, Melter tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSupercooler extends GuiBasicUpgradableProcessor<Supercooler> {
		
		public GuiSupercooler(Container inventory, EntityPlayer player, Supercooler tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiElectrolyzer extends GuiBasicUpgradableProcessor<Electrolyzer> {
		
		public GuiElectrolyzer(Container inventory, EntityPlayer player, Electrolyzer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiAssembler extends GuiBasicUpgradableProcessor<Assembler> {
		
		public GuiAssembler(Container inventory, EntityPlayer player, Assembler tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiIngotFormer extends GuiBasicUpgradableProcessor<IngotFormer> {
		
		public GuiIngotFormer(Container inventory, EntityPlayer player, IngotFormer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiPressurizer extends GuiBasicUpgradableProcessor<Pressurizer> {
		
		public GuiPressurizer(Container inventory, EntityPlayer player, Pressurizer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiChemicalReactor extends GuiBasicUpgradableProcessor<ChemicalReactor> {
		
		public GuiChemicalReactor(Container inventory, EntityPlayer player, ChemicalReactor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSaltMixer extends GuiBasicUpgradableProcessor<SaltMixer> {
		
		public GuiSaltMixer(Container inventory, EntityPlayer player, SaltMixer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiCrystallizer extends GuiBasicUpgradableProcessor<Crystallizer> {
		
		public GuiCrystallizer(Container inventory, EntityPlayer player, Crystallizer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiEnricher extends GuiBasicUpgradableProcessor<Enricher> {
		
		public GuiEnricher(Container inventory, EntityPlayer player, Enricher tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiExtractor extends GuiBasicUpgradableProcessor<Extractor> {
		
		public GuiExtractor(Container inventory, EntityPlayer player, Extractor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiCentrifuge extends GuiBasicUpgradableProcessor<Centrifuge> {
		
		public GuiCentrifuge(Container inventory, EntityPlayer player, Centrifuge tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiRockCrusher extends GuiBasicUpgradableProcessor<RockCrusher> {
		
		public GuiRockCrusher(Container inventory, EntityPlayer player, RockCrusher tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
}
