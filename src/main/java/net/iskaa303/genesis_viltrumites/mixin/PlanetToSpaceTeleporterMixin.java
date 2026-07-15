package net.iskaa303.genesis_viltrumites.mixin;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.valkyrienskies.mod.common.util.VectorConversionsMCKt;
import shipwrights.genesis.GenesisMod;
import shipwrights.genesis.config.GenesisCommonConfig;
import shipwrights.genesis.space.VantagePoint;
import shipwrights.genesis.teleportation.impl.EntityTeleporter;
import shipwrights.genesis.teleportation.integration.PlanetToSpaceTeleporter;
import net.iskaa303.genesis_viltrumites.ViltrumiteSpeedAccessor;

@Mixin(PlanetToSpaceTeleporter.class)
public class PlanetToSpaceTeleporterMixin {

    @Inject(method = "tick", at = @At("RETURN"), remap = false)
    private static void viltrumites$teleportFlyingPlayers(ServerLevel level, CallbackInfo ci) {
        var body = GenesisMod.getCelestialForLevel(level);
        var spaceLevel = level.getServer()
                .getLevel(ResourceKey.create(Registries.DIMENSION, GenesisMod.SPACE_DIM));
        if (body == null || spaceLevel == null) return;

        long ticks = GenesisMod.getTicks(level);

        for (ServerPlayer player : new java.util.ArrayList<>(level.players())) {
            Vector3d playerPos = new Vector3d(player.getX(), player.getY(), player.getZ());
            if (playerPos.y() > GenesisCommonConfig.getAtmosphereExitHeight()) {
                if (VantagePoint.get(level, playerPos, ticks, 0f) instanceof VantagePoint.OnCelestial vantagePoint) {
                    // Inline of private PlanetToSpaceTeleporter.computeSpaceTarget
                    Vector3d targetPos = new Vector3d(0, vantagePoint.celestial().getActualSize() + 20, 0);
                    vantagePoint.cameraRotationFromNorthPole()
                            .conjugate(new Quaterniond()).transform(targetPos);
                    vantagePoint.getCelestialRotation().transform(targetPos);
                    targetPos.add(vantagePoint.getPosition());

                    Quaterniondc targetRot = new Quaterniond();
                    EntityTeleporter.teleportEntityAndPassengers(player, spaceLevel,
                            VectorConversionsMCKt.toMinecraft(targetPos), targetRot);
                    ((ViltrumiteSpeedAccessor) player).setSpeedMultiplier(
                            ((ViltrumiteSpeedAccessor) player).getSpeedMultiplier() * 30.0f
                    );
                }
            }
        }
    }
}
