package com.inspw.mcmod.createdeployertweaks.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.simibubi.create.content.kinetics.deployer.DeployerFakePlayer;

import io.github.fabricators_of_create.porting_lib.tool.ToolActions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;

@Mixin(value = PlayerEntity.class, priority = 1)
public class PlayerEntityMixin {
    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At("STORE"), ordinal = 3)
    private boolean forceSweepingEdgeForDeployerFakePlayer(boolean value) {
        PlayerEntity self = (PlayerEntity) (Object) this;
        if (self instanceof DeployerFakePlayer fakePlayer) {
            ItemStack deployerToolStack = fakePlayer.getMainHandStack();
            return deployerToolStack.canPerformAction(ToolActions.SWORD_SWEEP);
        }
        return value;
    }
}
