package net.gudenau.minecraft.betterbooksheves.block.entity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.ChiseledBookshelfBlockEntity;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.WrittenBookItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Language;
import net.minecraft.util.math.BlockPos;

import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;

public final class BetterBookshelfBlockEntity extends ChiseledBookshelfBlockEntity {
    private static final class Filter {
        private static final Filter NONE = new Filter("");
        private static final Pattern PATTERN = Pattern.compile("^([\\*@])?([\\s\\S]+?)(\\*)? ?([IVX]+)?$");

        private final String string;
        private final Predicate<ItemStack> predicate;

        private Filter(String string) {
            this.string = string;
            this.predicate = generatePredicate(string);
        }

        private Predicate<ItemStack> generatePredicate(String string) {
            if(string.isBlank()) {
                return (stack) -> true;
            }

            var matcher = PATTERN.matcher(string);
            if(!matcher.find()) {
                return (stack) -> false;
            }

            var filter = matcher.group(1);
            boolean wildFront;
            boolean author;
            if(filter != null) {
                wildFront = filter.equals("*");
                author = filter.equals("@");
            } else {
                wildFront = false;
                author = false;
            }

            String tempName = matcher.group(2);
            String name = author ? tempName : tempName.toLowerCase(Locale.ROOT);
            boolean wildBack = matcher.group(3) != null;
            String levelString = matcher.group(4);

            Predicate<String> nameFilter;
            if(author || (!wildFront && !wildBack)) {
                nameFilter = name::equals;
            } else if(!wildFront) {
                nameFilter = (enchant) -> enchant.startsWith(name);
            } else if(!wildBack) {
                nameFilter = (enchant) -> enchant.endsWith(name);
            } else {
                nameFilter = (enchant) -> enchant.contains(name);
            }

            int level;
            if(levelString != null) {
                var values = levelString.chars()
                    .map((character) -> switch(character) {
                        case 'I', 'i' -> 1;
                        case 'V', 'v' -> 5;
                        case 'X', 'x' -> 10;
                        case 'L', 'l' -> 50;
                        case 'C', 'c' -> 100;
                        case 'D', 'd' -> 500;
                        case 'M', 'm' -> 1000;
                        default -> throw new AssertionError();
                    })
                    .toArray();

                int max = 0;
                int total = 0;
                for (int i = values.length - 1; i >= 0; i--) {
                    int value = values[i];
                    if(max > value) {
                        total -= value;
                    } else {
                        total += value;
                        max = value;
                    }
                }

                level = total;
            } else {
                level = -1;
            }

            return (stack) -> {
                if(stack.getItem() instanceof EnchantedBookItem) {
                    var enchantments = EnchantmentHelper.get(stack);
                    return enchantments.entrySet().stream()
                        .anyMatch((entry) -> {
                            if(level != -1 && entry.getValue() != level) {
                                return false;
                            }

                            var enchant = entry.getKey();
                            var enchantName = author ?
                                Registries.ENCHANTMENT.getId(enchant).toString() :
                                Language.getInstance().get(enchant.getTranslationKey()).toLowerCase(Locale.ROOT);
                            return nameFilter.test(enchantName);
                        });
                }
                if(stack.getItem() instanceof WrittenBookItem && stack.hasNbt()) {
                    if(level != -1 && level != WrittenBookItem.getGeneration(stack) + 1) {
                        return false;
                    }

                    if(author) {
                        return nameFilter.test(stack.getNbt().getString(WrittenBookItem.AUTHOR_KEY));
                    }
                }
                if(level != -1) {
                    return false;
                }
                return nameFilter.test(stack.getName().getString());
            };
        }

        private boolean test(ItemStack stack) {
            return predicate.test(stack);
        }
    }

    private static final String FILTER_KEY = "filter";

    private Filter filter = Filter.NONE;

    public BetterBookshelfBlockEntity(BlockPos pos, BlockState state) {
        super(pos, state);
    }

    public void filter(String filter) {
        if(filter.equals(this.filter.string)) {
            return;
        }

        this.filter = new Filter(filter);
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);

        if(nbt.contains(FILTER_KEY)) {
            filter = new Filter(nbt.getString(FILTER_KEY));
        } else {
            filter = Filter.NONE;
        }
    }

    @Override
    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);

        if(!filter.string.isBlank()) {
            nbt.putString(FILTER_KEY, filter.string);
        }
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        if(filter.test(stack)) {
            super.setStack(slot, stack);
        }
    }

    @Override
    public boolean isValid(int slot, ItemStack stack) {
        return filter.test(stack) && super.isValid(slot, stack);
    }
}
