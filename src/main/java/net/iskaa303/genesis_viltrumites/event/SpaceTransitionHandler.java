package net.iskaa303.genesis_viltrumites.event;

import net.iskaa303.genesis_viltrumites.viltrumite.PerPlayerSpeedManager;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import shipwrights.genesis.GenesisMod;

@Mod.EventBusSubscriber
public final class SpaceTransitionHandler {

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        Player player = event.getEntity();
        boolean nowInSpace = event.getTo().location().equals(GenesisMod.SPACE_DIM);
        if (nowInSpace) {
            PerPlayerSpeedManager.setToSpaceDefault(player);
        } else {
            PerPlayerSpeedManager.setToPlanetDefault(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        Player player = event.getEntity();
        boolean inSpace = player.level().dimension().location().equals(GenesisMod.SPACE_DIM);
        if (inSpace) {
            PerPlayerSpeedManager.setToSpaceDefault(player);
        } else {
            PerPlayerSpeedManager.setToPlanetDefault(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedOut(PlayerEvent.PlayerLoggedOutEvent event) {
        PerPlayerSpeedManager.remove(event.getEntity().getUUID());
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        float oldSpeed = PerPlayerSpeedManager.get(event.getOriginal());
        PerPlayerSpeedManager.set(event.getEntity(), oldSpeed);
    }
}
