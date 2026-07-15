package net.iskaa303.genesis_viltrumites.mixin;

import com.baranhan123.viltrumiteflight.util.FlightState;
import com.baranhan123.viltrumiteflight.util.ViltrumiteFlightPlayer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shipwrights.genesis.GenesisMod;

/**
 * Boosts Viltrumite flight speed 10x when the player is in Genesis space
 * (dimension {@code genesis:great_unknown}). This runs after Viltrumite's own
 * {@code PlayerEntityMixin.onTick} has already set the flight velocity,
 * so we just scale the result.
 */
@Mixin(Player.class)
public class ViltrumiteFlightSpeedMixin {

    @Inject(method = "tick", at = @At("RETURN"))
    private void viltrumites$boostInSpace(CallbackInfo ci) {
        Player player = (Player) (Object) this;
        // Only server-side matters — the server is authoritative for movement
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        // Check if the player is in Genesis space
        if (!player.level().dimension().location().equals(GenesisMod.SPACE_DIM)) return;

        // Check if Viltrumite flight is active on this player
        if (!(serverPlayer instanceof ViltrumiteFlightPlayer flightPlayer)) return;
        if (flightPlayer.getFlightState() == FlightState.NONE) return;

        // 10x the current velocity
        Vec3 vel = player.getDeltaMovement();
        if (vel.lengthSqr() > 1.0e-6) {
            player.setDeltaMovement(vel.scale(10.0));
        }
    }
}
