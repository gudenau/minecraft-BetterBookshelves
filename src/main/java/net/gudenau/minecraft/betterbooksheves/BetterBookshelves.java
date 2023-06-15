package net.gudenau.minecraft.betterbooksheves;

import net.fabricmc.api.ModInitializer;
import org.spongepowered.asm.mixin.MixinEnvironment;

public final class BetterBookshelves implements ModInitializer {
    @Override
    public void onInitialize() {
        MixinEnvironment.getCurrentEnvironment().audit();
    }
}
