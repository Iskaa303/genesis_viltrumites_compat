package net.iskaa303.genesis_viltrumites.mixin;

import com.baranhan123.viltrumiteflight.config.ViltrumiteConfig;
import net.iskaa303.genesis_viltrumites.ViltrumiteSpeedAccessor;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Implements;
import org.spongepowered.asm.mixin.Interface;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
@Implements(@Interface(iface = ViltrumiteSpeedAccessor.class, prefix = "viltrimitespeed$"))
public class ViltrumiteFlightSpeedMixin {

    @Unique
    public float viltrumites$speedMultiplier = 1.0f;

    @Unique
    private float viltrumites$savedGlobalMaxSpeed;

    @Unique
    public float viltrimitespeed$getSpeedMultiplier() {
        return viltrumites$speedMultiplier;
    }

    @Unique
    public void viltrimitespeed$setSpeedMultiplier(float multiplier) {
        viltrumites$speedMultiplier = multiplier;
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void viltrumites$applyMultiplier(CallbackInfo ci) {
        viltrumites$savedGlobalMaxSpeed = ViltrumiteConfig.INSTANCE.maxFlightSpeed;
        ViltrumiteConfig.INSTANCE.maxFlightSpeed *= viltrumites$speedMultiplier;
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void viltrumites$restoreGlobal(CallbackInfo ci) {
        ViltrumiteConfig.INSTANCE.maxFlightSpeed = viltrumites$savedGlobalMaxSpeed;
    }
}
