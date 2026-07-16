package net.iskaa303.genesis_viltrumites.mixin.genesis;

import net.iskaa303.genesis_viltrumites.viltrumite.PerPlayerSpeedManager;
import net.minecraft.core.Registry;
import net.minecraft.core.SectionPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import org.joml.Quaterniond;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import shipwrights.genesis.GenesisMod;
import shipwrights.genesis.config.GenesisCommonConfig;
import shipwrights.genesis.space.Celestial;
import shipwrights.genesis.teleportation.impl.EntityTeleporter;
import shipwrights.genesis.teleportation.integration.SpaceToPlanetTeleporter;
import java.util.Comparator;

@Mixin(SpaceToPlanetTeleporter.class)
public class SpaceToPlanetTeleporterMixin {

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    private static void viltrumites$teleportFlyingPlayers(ServerLevel level, CallbackInfo ci) {
        long ticks = GenesisMod.getTicks(level);
        Registry<Celestial> registry = GenesisMod.getCelestialRegistry(level);

        for (ServerPlayer player : new java.util.ArrayList<>(level.players())) {
            Vector3d playerPos = new Vector3d(player.getX(), player.getY(), player.getZ());
            Celestial nearest = viltrumites$getNearestPlanetForPlayer(playerPos, ticks, registry);
            if (nearest == null) continue;

            ServerLevel targetLevel = level.getServer().getLevel(
                    ResourceKey.create(
                            net.minecraft.core.registries.Registries.DIMENSION,
                            registry.getResourceKey(nearest).orElseThrow().location()
                    )
            );
            if (targetLevel == null) continue;

            int landingAccuracy = 8;
            ChunkPos landingChunkPos = new ChunkPos(
                    level.random.nextInt(landingAccuracy * 2 + 1) - landingAccuracy,
                    level.random.nextInt(landingAccuracy * 2 + 1) - landingAccuracy
            );
            Vector3d newPos = new Vector3d(
                    SectionPos.sectionToBlockCoord(landingChunkPos.x),
                    GenesisCommonConfig.getAtmosphereEntryHeight(),
                    SectionPos.sectionToBlockCoord(landingChunkPos.z)
            );

            Quaterniond rotation = new Quaterniond();
            EntityTeleporter.teleportEntityAndPassengers(player, targetLevel,
                    VectorConversionsMCKt.toMinecraft(newPos), rotation);

            PerPlayerSpeedManager.setToPlanetDefault(player);
        }
    }

    @Unique
    private static Celestial viltrumites$getNearestPlanetForPlayer(
            Vector3d playerPos, long ticks, Registry<Celestial> registry
    ) {
        return registry.stream()
                .filter(c -> c.type().isVisitable())
                .filter(c -> playerPos.distanceSquared(c.getPosition(ticks, registry))
                        < c.getActualSize() * c.getActualSize())
                .min(Comparator.comparingDouble(
                        c -> playerPos.distanceSquared(c.getPosition(ticks, registry))))
                .orElse(null);
    }
}
