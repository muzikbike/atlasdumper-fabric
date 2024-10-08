package lol.skye.atlasdumper.mixin;

import lol.skye.atlasdumper.AtlasDumper;
import lol.skye.atlasdumper.util.VersionConstants;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class MixinPlugin implements IMixinConfigPlugin {
    @Override
    public void onLoad(String mixinPackage) {
        VersionConstants.init();
    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        AtlasDumper.LOGGER.info("Checking {}", mixinClassName);

        // 19w34a-19w46b
        boolean isSnapshot = VersionConstants.YEAR == 19 &&
                (VersionConstants.WEEK >= 34 && VersionConstants.WEEK <= 46);

        if (mixinClassName.contains("compat.pre115")) {
            return isSnapshot || VersionConstants.MINOR < 15;
        } else if (mixinClassName.contains("compat.post115")) {
            // anything 1.15 (19w46b+) to 1.19, or 1.19-1.19.2
            return !isSnapshot && VersionConstants.MINOR >= 15 &&
                    (VersionConstants.MINOR < 19 ||
                            (VersionConstants.MINOR == 19 && VersionConstants.PATCH < 3));
        } else if (mixinClassName.contains("compat.post1192")) {
            // 1.19.3+
            return VersionConstants.MINOR > 19 ||
                    (VersionConstants.MINOR == 19 && VersionConstants.PATCH >= 3);
        }

        return false;
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {}

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {}
}
