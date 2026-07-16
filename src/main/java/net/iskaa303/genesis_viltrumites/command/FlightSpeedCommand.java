package net.iskaa303.genesis_viltrumites.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.iskaa303.genesis_viltrumites.viltrumite.PerPlayerSpeedManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import shipwrights.genesis.GenesisMod;
import java.util.Collection;

public final class FlightSpeedCommand {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("viltrumitespeed")
                        .requires(s -> s.hasPermission(0))
                        .then(Commands.literal("get")
                                .executes(ctx -> getOwn(ctx.getSource()))
                                .then(Commands.argument("player", EntityArgument.player())
                                        .executes(ctx -> get(ctx.getSource(),
                                                EntityArgument.getPlayer(ctx, "player"))))
                        )
                        .then(Commands.literal("set")
                                .then(Commands.argument("speed", FloatArgumentType.floatArg(0, 100000))
                                        .executes(ctx -> setOwn(ctx.getSource(),
                                                FloatArgumentType.getFloat(ctx, "speed")))
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .requires(s -> s.hasPermission(2))
                                                .executes(ctx -> set(ctx.getSource(),
                                                        EntityArgument.getPlayers(ctx, "targets"),
                                                        FloatArgumentType.getFloat(ctx, "speed"))))
                                )
                        )
        );
    }

    private static int getOwn(CommandSourceStack source) throws CommandSyntaxException {
        return report(source, source.getPlayerOrException());
    }

    private static int get(CommandSourceStack source, ServerPlayer target) {
        return report(source, target);
    }

    private static int report(CommandSourceStack source, ServerPlayer player) {
        float cap = PerPlayerSpeedManager.get(player);
        boolean inSpace = player.level().dimension().location().equals(GenesisMod.SPACE_DIM);
        source.sendSuccess(() -> Component.literal(
                (player == source.getEntity() ? "Your" : player.getName().getString() + "'s")
                        + " flight speed cap: " + String.format("%.1f", cap)
                        + (inSpace ? " (in space)" : "")
        ), false);
        return 1;
    }

    private static int setOwn(CommandSourceStack source, float speed) throws CommandSyntaxException {
        ServerPlayer player = source.getPlayerOrException();
        PerPlayerSpeedManager.set(player, speed);
        source.sendSuccess(() -> Component.literal(
                "Set your flight speed cap to " + String.format("%.1f", speed)
        ), false);
        return 1;
    }

    private static int set(CommandSourceStack source, Collection<ServerPlayer> targets, float speed) {
        for (ServerPlayer player : targets) {
            PerPlayerSpeedManager.set(player, speed);
        }
        source.sendSuccess(() -> Component.literal(
                "Set flight speed cap to " + String.format("%.1f", speed)
                        + " for " + targets.size() + " player(s)"
        ), false);
        return targets.size();
    }
}
