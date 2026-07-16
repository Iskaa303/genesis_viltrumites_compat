package net.iskaa303.genesis_viltrumites;

import net.iskaa303.genesis_viltrumites.command.FlightSpeedCommand;
import net.iskaa303.genesis_viltrumites.config.CompatConfig;
import net.iskaa303.genesis_viltrumites.event.SpaceTransitionHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Viltrumites.MOD_ID)
public final class Viltrumites {
    public static final String MOD_ID = "genesis_viltrumites";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Viltrumites() {
        CompatConfig.getInstance();
        MinecraftForge.EVENT_BUS.register(new SpaceTransitionHandler());
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        LOGGER.info("Genesis: Viltrumites loaded.");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        FlightSpeedCommand.register(event.getDispatcher());
    }
}
