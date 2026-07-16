package net.iskaa303.genesis_viltrumites.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public final class CompatConfig {
    private static final CompatConfig INSTANCE;
    private static final ForgeConfigSpec SPEC;

    public float maxSpaceFlightSpeed;

    static {
        Pair<CompatConfig, ForgeConfigSpec> pair =
                new ForgeConfigSpec.Builder().configure(CompatConfig::new);
        INSTANCE = pair.getLeft();
        SPEC = pair.getRight();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, SPEC, "genesis_viltrumites-common.toml");
    }

    private final ForgeConfigSpec.DoubleValue spaceSpeed;

    private CompatConfig(ForgeConfigSpec.Builder builder) {
        spaceSpeed = builder
                .comment("Max flight speed when in space. Defaults to 90.")
                .defineInRange("maxSpaceFlightSpeed", 45.0, 1.0, 1000000.0);
    }

    @SubscribeEvent
    public static void onConfigEvent(ModConfigEvent event) {
        if (event.getConfig().getSpec() == SPEC) {
            INSTANCE.maxSpaceFlightSpeed = INSTANCE.spaceSpeed.get().floatValue();
        }
    }

    public static CompatConfig getInstance() {
        return INSTANCE;
    }

    public static float spaceSpeed() {
        return INSTANCE.maxSpaceFlightSpeed;
    }
}
