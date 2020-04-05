package nc.command;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommandHandler {
	
	public static void registerCommands(FMLServerStartingEvent serverStartEvent) {
		serverStartEvent.registerServerCommand(new CommandSetChunkRadiation());
		serverStartEvent.registerServerCommand(new CommandSetChunkRadiusRadiation());
		serverStartEvent.registerServerCommand(new CommandSetWorldRadiation());
		serverStartEvent.registerServerCommand(new CommandSetPlayerRadiation());
	}
}
