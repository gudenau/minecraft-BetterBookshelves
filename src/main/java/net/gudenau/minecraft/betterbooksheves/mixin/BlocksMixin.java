package net.gudenau.minecraft.betterbooksheves.mixin;

import net.gudenau.minecraft.betterbooksheves.block.BetterBookshelfBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChiseledBookshelfBlock;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(Blocks.class)
public abstract class BlocksMixin {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "NEW",
            target = "net/minecraft/block/ChiseledBookshelfBlock"
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=chiseled_bookshelf"
            ),
            to = @At(
                value = "CONSTANT",
                args = "stringValue=mossy_cobblestone"
            )
        )
    )
    private static ChiseledBookshelfBlock hook(AbstractBlock.Settings settings) {
        return new BetterBookshelfBlock(settings);
    }
}
