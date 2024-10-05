package lol.skye.atlasdumper.util;

import lol.skye.atlasdumper.AtlasDumper;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.SemanticVersion;
import net.fabricmc.loader.impl.FabricLoaderImpl;
import net.fabricmc.loader.impl.game.minecraft.MinecraftGameProvider;

public class VersionConstants {
    public static int MAJOR, MINOR, PATCH;
    public static int YEAR = -1, WEEK = -1;
    public static boolean RC;

    public static void init() {
        MinecraftGameProvider mc = (MinecraftGameProvider)
                FabricLoaderImpl.INSTANCE.getGameProvider();
        ModContainer mcMod = FabricLoader.getInstance().getModContainer("minecraft")
                .orElseThrow(() -> new RuntimeException("no minecraft mod container"));

        SemanticVersion version = (SemanticVersion) mcMod.getMetadata().getVersion();
        MAJOR = version.getVersionComponent(0);
        MINOR = version.getVersionComponent(1);
        PATCH = version.getVersionComponent(2);
        if (PATCH == 0) {
            AtlasDumper.LOGGER.info("detected minecraft version {}.{}", MAJOR, MINOR);
        } else {
            AtlasDumper.LOGGER.info("detected minecraft version {}.{}.{}", MAJOR, MINOR, PATCH);
        }

        try {
            String versionString = mc.getNormalizedGameVersion();
            RC = versionString.contains("-rc");
            if (versionString.contains("-alpha")) {
                String[] parts = versionString.split("\\.");
                YEAR = Integer.parseInt(parts[parts.length - 3]);
                WEEK = Integer.parseInt(parts[parts.length - 2]);
                AtlasDumper.LOGGER.info("detected snapshot version year {} week {}", YEAR, WEEK);
            }
        } catch (NumberFormatException ignored) {
        }
    }
}
