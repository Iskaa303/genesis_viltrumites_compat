package net.iskaa303.genesis_viltrumites;

import net.iskaa303.genesis_viltrumites.command.FlightSpeedCommand;
import net.iskaa303.genesis_viltrumites.config.CompatConfig;
import net.iskaa303.genesis_viltrumites.event.SpaceTransitionHandler;
import net.iskaa303.genesis_viltrumites.viltrumite.PerPlayerSpeedManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.level.ChunkEvent;
import net.minecraftforge.fml.common.Mod;
import qouteall.imm_ptl.peripheral.dim_stack.DimStackManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(Viltrumites.MOD_ID)
public final class Viltrumites {
    public static final String MOD_ID = "genesis_viltrumites";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public Viltrumites() {
        CompatConfig.getInstance();
        PerPlayerSpeedManager.init();
        MinecraftForge.EVENT_BUS.register(new SpaceTransitionHandler());
        MinecraftForge.EVENT_BUS.addListener(this::onRegisterCommands);
        MinecraftForge.EVENT_BUS.addListener(this::onChunkLoad);
        LOGGER.info("Genesis: Viltrumites loaded.");
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        FlightSpeedCommand.register(event.getDispatcher());
    }

    private void onChunkLoad(ChunkEvent.Load event) {
        if (event.getLevel() instanceof ServerLevel serverLevel
            && event.getChunk() instanceof LevelChunk levelChunk) {
            DimStackManagement.replaceBedrock(serverLevel, levelChunk);
        }
    }
}
