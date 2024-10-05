package lol.skye.atlasdumper.mixin.compat.post115;

import lol.skye.atlasdumper.data.SpriteAtlasData;
import lol.skye.atlasdumper.util.ReflectionUtil;
import lol.skye.atlasdumper.util.TextureDumper;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SpriteAtlasTexture.class)
public class SpriteAtlasTextureMixin {

    @Final
    @Shadow
    private Identifier id;

    @Dynamic
    @Inject(method = "method_18159", at = @At("TAIL"), remap = false)
    public void upload(@Coerce Object data, CallbackInfo ci) {
        SpriteAtlasTexture _this = (SpriteAtlasTexture) (Object) this;

        SpriteAtlasData atlasData = ReflectionUtil.getData(data, true);
        if (atlasData == null) {
            return;
        }

        TextureDumper.dumpTexture(
                _this.getGlId(),
                id.getPath(),
                atlasData.maxLevel,
                atlasData.width,
                atlasData.height);
    }
}
