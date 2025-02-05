package io.github.chaosawakens.common.items;

import io.github.chaosawakens.api.IAutoEnchantable;

import io.github.chaosawakens.common.config.CAConfig;
import io.github.chaosawakens.common.entity.projectile.UltimateFishingBobberEntity;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.*;
import net.minecraft.world.World;

public class UltimateFishingRodItem extends FishingRodItem implements IAutoEnchantable {

    private final EnchantmentData[] enchantments;

    public UltimateFishingRodItem(Properties builder, EnchantmentData[] enchantments) {
        super(builder);
        this.enchantments = enchantments;
    }

    @Override
    public void fillItemCategory(ItemGroup group, NonNullList<ItemStack> items) {
        if (this.allowdedIn(group)) {
            ItemStack stack = new ItemStack(this);
            if (CAConfig.COMMON.enableAutoEnchanting.get())
                for (EnchantmentData enchant : enchantments) {
                    stack.enchant(enchant.enchantment, enchant.level);
                }
            items.add(stack);
        }
    }

    @Override
    public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack itemstack = playerIn.getItemInHand(handIn);
        if (playerIn.fishing != null) {
            if (!worldIn.isClientSide) {
                int i = playerIn.fishing.retrieve(itemstack);
                itemstack.hurtAndBreak(i, playerIn, (player) -> player.broadcastBreakEvent(handIn));
            }

            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.FISHING_BOBBER_RETRIEVE, SoundCategory.NEUTRAL, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        } else {
            worldIn.playSound(null, playerIn.getX(), playerIn.getY(), playerIn.getZ(), SoundEvents.FISHING_BOBBER_THROW, SoundCategory.NEUTRAL, 0.5F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
            if (!worldIn.isClientSide) {
                int k = EnchantmentHelper.getFishingSpeedBonus(itemstack);
                int j = EnchantmentHelper.getFishingLuckBonus(itemstack);
                worldIn.addFreshEntity(new UltimateFishingBobberEntity(playerIn, worldIn, j, k));
            }

            playerIn.awardStat(Stats.ITEM_USED.get(this));
        }

        return ActionResult.sidedSuccess(itemstack, worldIn.isClientSide());
    }

    @Override
    public EnchantmentData[] enchantments() {
        return this.enchantments;
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return CAConfig.COMMON.enableAutoEnchanting.get() || super.isFoil(stack);
    }

}
