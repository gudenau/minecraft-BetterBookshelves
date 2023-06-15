package net.gudenau.minecraft.betterbooksheves.mixin;

import net.gudenau.minecraft.betterbooksheves.block.entity.BetterBookshelfBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;

@Mixin(BlockEntityType.class)
public abstract class BlockEntityTypeMixin {
    @Redirect(
        method = "<clinit>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/block/entity/BlockEntityType$Builder;create(Lnet/minecraft/block/entity/BlockEntityType$BlockEntityFactory;[Lnet/minecraft/block/Block;)Lnet/minecraft/block/entity/BlockEntityType$Builder;"
        ),
        slice = @Slice(
            from = @At(
                value = "CONSTANT",
                args = "stringValue=chiseled_bookshelf"
            ),
            to = @At(
                value = "CONSTANT",
                args = "stringValue=brushable_block"
            )
        )
    )
    private static BlockEntityType.Builder<?> hook(BlockEntityType.BlockEntityFactory<?> factory, Block[] blocks) {
        return BlockEntityType.Builder.create(BetterBookshelfBlockEntity::new, blocks);
    }
}
