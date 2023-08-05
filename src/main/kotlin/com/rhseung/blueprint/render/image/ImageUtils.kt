package com.rhseung.blueprint.render.image

import com.rhseung.blueprint.file.Loc
import net.minecraft.client.texture.NativeImage
import net.minecraft.resource.ResourceManager

object ImageUtils {
    fun readImage(manager: ResourceManager, id: Loc): NativeImage {
        val res = manager.getResource(id.toIdentifier()).get()
        return NativeImage.read(res.inputStream)
    }
}