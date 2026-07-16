package net.iskaa303.genesis_viltrumites.mixin.minecraft;

import com.baranhan123.viltrumitecore.util.ViltrumiteCorePlayer;
import com.baranhan123.viltrumiteflight.config.ViltrumiteConfig;
import net.iskaa303.genesis_viltrumites.viltrumite.PerPlayerSpeedManager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shipwrights.genesis.GenesisMod;

@Mixin(Player.class)
public class PlayerTickSpeedEnforcer {

    @Unique
    private float viltrumites$savedGlobalSpeed;

    @Inject(method = "tick", at = @At("HEAD"))
    private void viltrumites$setPlayerCap(CallbackInfo ci) {
        Player self = (Player) (Object) this;
        viltrumites$savedGlobalSpeed = ViltrumiteConfig.INSTANCE.maxFlightSpeed;
        ViltrumiteConfig.INSTANCE.maxFlightSpeed = PerPlayerSpeedManager.get(self);

        if (self instanceof ViltrumiteCorePlayer vil && vil.getGrabbedTarget() != null) {
            boolean inSpace = self.level().dimension().location().equals(GenesisMod.SPACE_DIM);
            float scale = inSpace ? 0.0625f : 1.0f;
            Vec3 handPos = self.getEyePosition().add(
                    self.getLookAngle().normalize().scale(1.5 * scale));
            vil.setServerHandPos(handPos);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void viltrumites$restoreGlobalCap(CallbackInfo ci) {
        ViltrumiteConfig.INSTANCE.maxFlightSpeed = viltrumites$savedGlobalSpeed;
    }
}
