package com.rhseung.blueprint.mixin.tooltip;

import com.rhseung.blueprint.tooltip.TooltipComponentFactory;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TooltipComponent.class)
public class TooltipComponentMixin {
	@Inject(
		method = "of(Lnet/minecraft/client/item/TooltipData;)Lnet/minecraft/client/gui/tooltip/TooltipComponent;",
		at = @At("HEAD"),
		cancellable = true
	)
	private static void of(TooltipData data, CallbackInfoReturnable<TooltipComponent> cir) {
		cir.setReturnValue(TooltipComponentFactory.create(data));
	}
}