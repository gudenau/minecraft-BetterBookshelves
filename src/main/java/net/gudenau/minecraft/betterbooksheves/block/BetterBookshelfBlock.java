package net.gudenau.minecraft.betterbooksheves.block;

import net.gudenau.minecraft.betterbooksheves.block.entity.BetterBookshelfBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChiseledBookshelfBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class BetterBookshelfBlock extends ChiseledBookshelfBlock {
    public BetterBookshelfBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new BetterBookshelfBlockEntity(pos, state);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        if(world.isClient() || !stack.hasCustomName() || !(world.getBlockEntity(pos) instanceof BetterBookshelfBlockEntity entity)) {
            return;
        }

        entity.filter(stack.getName().getString());
    }
}
