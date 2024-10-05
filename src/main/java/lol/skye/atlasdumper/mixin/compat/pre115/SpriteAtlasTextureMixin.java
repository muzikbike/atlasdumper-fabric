package lol.skye.atlasdumper.mixin.compat.pre115;

import lol.skye.atlasdumper.data.SpriteAtlasData;
import lol.skye.atlasdumper.util.ReflectionUtil;
import lol.skye.atlasdumper.util.TextureDumper;
import net.minecraft.client.texture.SpriteAtlasTexture;
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
    @Shadow(remap = false)
    private String field_5279; /*atlasPath*/
    @Shadow(remap = false)
    private int field_5281; /*mipLevel*/

    @Dynamic
    @Inject(method = "method_18159", at = @At("TAIL"), remap = false)
    public void upload(@Coerce Object data, CallbackInfo ci) {
        SpriteAtlasTexture _this = (SpriteAtlasTexture) (Object) this;

        SpriteAtlasData atlasData = ReflectionUtil.getData(data, false);
        if (atlasData == null) {
            return;
        }

        TextureDumper.dumpTexture(
                _this.getGlId(),
                field_5279,
                field_5281,
                atlasData.width,
                atlasData.height);
    }
}
