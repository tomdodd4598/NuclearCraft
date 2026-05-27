package nc.worldgen.structure;

import nc.worldgen.structure.vault.*;
import net.minecraft.world.gen.structure.MapGenStructureIO;

public class NCStructures {
	
	private static boolean initialized = false;
	
	public static void init() {
		if (initialized) {
			return;
		}
		initialized = true;
		
		MapGenStructureIO.registerStructure(VaultStart.class, VaultGenerator.STRUCTURE_NAME);
		MapGenStructureIO.registerStructureComponent(VaultComponent.class, VaultComponent.COMPONENT_NAME);
	}
}
