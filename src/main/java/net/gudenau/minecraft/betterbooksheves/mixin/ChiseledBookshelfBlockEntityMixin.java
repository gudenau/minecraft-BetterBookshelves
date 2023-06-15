package net.gudenau.minecraft.betterbooksheves.mixin;

import net.gudenau.minecraft.betterbooksheves.block.entity.BetterBookshelfBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChiseledBookshelfBlockEntity.class)
public abstract class ChiseledBookshelfBlockEntityMixin {
    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void init(BlockPos pos, BlockState state, CallbackInfo ci) {
        if(!((Object) this instanceof BetterBookshelfBlockEntity)) {
            throw new AssertionError();
        }
    }
}
