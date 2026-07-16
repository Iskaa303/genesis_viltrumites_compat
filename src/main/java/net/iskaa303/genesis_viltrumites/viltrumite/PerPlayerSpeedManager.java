package net.iskaa303.genesis_viltrumites.viltrumite;

import com.baranhan123.viltrumiteflight.config.ViltrumiteConfig;
import net.iskaa303.genesis_viltrumites.config.CompatConfig;
import net.minecraft.world.entity.player.Player;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public final class PerPlayerSpeedManager {
    private static final Map<UUID, Float> PLAYER_SPEEDS = new ConcurrentHashMap<>();

    private PerPlayerSpeedManager() {}

    public static float get(Player player) {
        return PLAYER_SPEEDS.getOrDefault(player.getUUID(), getDefaultForDimension(player));
    }

    public static void set(Player player, float speed) {
        PLAYER_SPEEDS.put(player.getUUID(), speed);
    }

    public static void setToPlanetDefault(Player player) {
        PLAYER_SPEEDS.put(player.getUUID(), ViltrumiteConfig.INSTANCE.maxFlightSpeed);
    }

    public static void setToSpaceDefault(Player player) {
        PLAYER_SPEEDS.put(player.getUUID(), CompatConfig.spaceSpeed());
    }

    public static void remove(UUID uuid) {
        PLAYER_SPEEDS.remove(uuid);
    }

    private static float getDefaultForDimension(Player player) {
        if (player.level() == null) return ViltrumiteConfig.INSTANCE.maxFlightSpeed;
        boolean inSpace = player.level().dimension().location()
                .equals(shipwrights.genesis.GenesisMod.SPACE_DIM);
        return inSpace ? CompatConfig.spaceSpeed()
                       : ViltrumiteConfig.INSTANCE.maxFlightSpeed;
    }
}
