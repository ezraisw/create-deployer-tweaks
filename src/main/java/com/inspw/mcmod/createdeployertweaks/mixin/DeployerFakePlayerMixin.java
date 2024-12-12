package com.inspw.mcmod.createdeployertweaks.mixin;

import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(DeployerFakePlayer.class)
public class DeployerFakePlayerMixin {
    @Inject(method = "deployerKillsDoNotSpawnXP(ILnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/LivingEntity;)I", at = @At("HEAD"), cancellable = true)
    public static void dropXpAndMend(int i, PlayerEntity player, LivingEntity entity,
            CallbackInfoReturnable<Integer> cir) {
        if (player instanceof DeployerFakePlayer fakePlayer) {
            int xp = i;

            ItemStack deployerToolStack = fakePlayer.getMainHandStack();
            if (isMendable(deployerToolStack)) {
                int repairAmount = Math.min(getMendingRepairAmount(xp), deployerToolStack.getDamage());
                deployerToolStack.setDamage(deployerToolStack.getDamage() - repairAmount);
                xp -= getMendingRepairCost(repairAmount);
            }

            // Always return to allow deployer to always drop XP.
            cir.setReturnValue(xp);
        }
    }

    private static boolean isMendable(@NotNull ItemStack stack) {
        return stack.isDamaged() && EnchantmentHelper.getLevel(Enchantments.MENDING, stack) > 0;
    }

    // Taken from net.minecraft.entity.ExperienceOrbEntity.
    // Could have referred to there but it isn't static.
    private static int getMendingRepairCost(int repairAmount) {
        return repairAmount / 2;
    }

    // Taken from net.minecraft.entity.ExperienceOrbEntity.
    // Could have referred to there but it isn't static.
    private static int getMendingRepairAmount(int experienceAmount) {
        return experienceAmount * 2;
    }
}
