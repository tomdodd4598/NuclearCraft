package nc.gui.processor;

import nc.tile.processor.TileProcessorImpl.*;
import nc.tile.processor.info.ProcessorContainerInfoImpl.*;
import nc.tile.radiation.TileRadiationScrubber;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class GuiProcessorImpl {
	
	public static class GuiBasicProcessor<TILE extends TileBasicProcessor<TILE>> extends GuiProcessor<TILE, BasicProcessorContainerInfo<TILE>> {
		
		public GuiBasicProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiBasicUpgradableProcessor<TILE extends TileBasicUpgradableProcessor<TILE>> extends GuiUpgradableProcessor<TILE, BasicUpgradableProcessorContainerInfo<TILE>> {
		
		public GuiBasicUpgradableProcessor(Container inventory, EntityPlayer player, TILE tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiBasicProcessorDyn extends GuiBasicProcessor<TileBasicProcessorDyn> {
		
		public GuiBasicProcessorDyn(Container inventory, EntityPlayer player, TileBasicProcessorDyn tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiBasicUpgradableProcessorDyn extends GuiBasicUpgradableProcessor<TileBasicUpgradableProcessorDyn> {
		
		public GuiBasicUpgradableProcessorDyn(Container inventory, EntityPlayer player, TileBasicUpgradableProcessorDyn tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiManufactory extends GuiBasicUpgradableProcessor<TileManufactory> {
		
		public GuiManufactory(Container inventory, EntityPlayer player, TileManufactory tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSeparator extends GuiBasicUpgradableProcessor<TileSeparator> {
		
		public GuiSeparator(Container inventory, EntityPlayer player, TileSeparator tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiDecayHastener extends GuiBasicUpgradableProcessor<TileDecayHastener> {
		
		public GuiDecayHastener(Container inventory, EntityPlayer player, TileDecayHastener tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiFuelReprocessor extends GuiBasicUpgradableProcessor<TileFuelReprocessor> {
		
		public GuiFuelReprocessor(Container inventory, EntityPlayer player, TileFuelReprocessor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiAlloyFurnace extends GuiBasicUpgradableProcessor<TileAlloyFurnace> {
		
		public GuiAlloyFurnace(Container inventory, EntityPlayer player, TileAlloyFurnace tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiInfuser extends GuiBasicUpgradableProcessor<TileInfuser> {
		
		public GuiInfuser(Container inventory, EntityPlayer player, TileInfuser tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiMelter extends GuiBasicUpgradableProcessor<TileMelter> {
		
		public GuiMelter(Container inventory, EntityPlayer player, TileMelter tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSupercooler extends GuiBasicUpgradableProcessor<TileSupercooler> {
		
		public GuiSupercooler(Container inventory, EntityPlayer player, TileSupercooler tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiElectrolyzer extends GuiBasicUpgradableProcessor<TileElectrolyzer> {
		
		public GuiElectrolyzer(Container inventory, EntityPlayer player, TileElectrolyzer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiAssembler extends GuiBasicUpgradableProcessor<TileAssembler> {
		
		public GuiAssembler(Container inventory, EntityPlayer player, TileAssembler tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiIngotFormer extends GuiBasicUpgradableProcessor<TileIngotFormer> {
		
		public GuiIngotFormer(Container inventory, EntityPlayer player, TileIngotFormer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiPressurizer extends GuiBasicUpgradableProcessor<TilePressurizer> {
		
		public GuiPressurizer(Container inventory, EntityPlayer player, TilePressurizer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiChemicalReactor extends GuiBasicUpgradableProcessor<TileChemicalReactor> {
		
		public GuiChemicalReactor(Container inventory, EntityPlayer player, TileChemicalReactor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiSaltMixer extends GuiBasicUpgradableProcessor<TileSaltMixer> {
		
		public GuiSaltMixer(Container inventory, EntityPlayer player, TileSaltMixer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiCrystallizer extends GuiBasicUpgradableProcessor<TileCrystallizer> {
		
		public GuiCrystallizer(Container inventory, EntityPlayer player, TileCrystallizer tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiEnricher extends GuiBasicUpgradableProcessor<TileEnricher> {
		
		public GuiEnricher(Container inventory, EntityPlayer player, TileEnricher tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiExtractor extends GuiBasicUpgradableProcessor<TileExtractor> {
		
		public GuiExtractor(Container inventory, EntityPlayer player, TileExtractor tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiCentrifuge extends GuiBasicUpgradableProcessor<TileCentrifuge> {
		
		public GuiCentrifuge(Container inventory, EntityPlayer player, TileCentrifuge tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiRockCrusher extends GuiBasicUpgradableProcessor<TileRockCrusher> {
		
		public GuiRockCrusher(Container inventory, EntityPlayer player, TileRockCrusher tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
	
	public static class GuiRadiationScrubber extends GuiBasicProcessor<TileRadiationScrubber> {
		
		public GuiRadiationScrubber(Container inventory, EntityPlayer player, TileRadiationScrubber tile, String textureLocation) {
			super(inventory, player, tile, textureLocation);
		}
	}
}
