package com.rhseung.blueprint.example

import com.rhseung.blueprint.datagen.provider.AbstractRecipeProvider
import com.rhseung.blueprint.datagen.handler.RecipeHandler
import com.rhseung.blueprint.datagen.handler.RecipeHandler.Builder
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.recipe.book.RecipeCategory
import net.minecraft.registry.RegistryWrapper
import net.minecraft.registry.tag.ItemTags
import java.util.concurrent.CompletableFuture

class ModRecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<RegistryWrapper.WrapperLookup>
) : AbstractRecipeProvider(output, registriesFuture) {

    override fun register(handler: RecipeHandler) {
        handler += Builder.shaped(RecipeCategory.TOOLS) {
            sub { 'X' to Items.DIAMOND }
            subTag { '#' to ItemTags.PLANKS }
            patterns {
                + "XXX"
                + " # "
                + " # "
            }
            output { Items.DIAMOND_PICKAXE }
        }
    }
}