package mod.zotmc.onlysilver.generation;

import mod.alexndr.simplecorelib.world.OreGenUtils;
import mod.zotmc.onlysilver.config.OnlySilverConfig;
import mod.zotmc.onlysilver.init.ModBlocks;
import net.minecraft.world.gen.GenerationStage.Decoration;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.event.world.BiomeLoadingEvent;

/**
 * Ore generation master-class for OnlySilver.
 */
public class OreGeneration
{
    public static ConfiguredFeature<?, ?> ORE_SILVER;

    /**
     * initialize overworld Features.
     * 
     * @param evt
     */
    public static void initOverworldFeatures()
    {
        ORE_SILVER = OreGenUtils.buildOverworldOreFeature(Feature.ORE, ModBlocks.silver_ore.get().getDefaultState(),
                OnlySilverConfig.silver_cfg);
    } // end-initOverworldFeatures()

    /**
     * generate overworld ores.
     */
    public static void generateOverworldOres(BiomeLoadingEvent evt)
    {
        evt.getGeneration().withFeature(Decoration.UNDERGROUND_ORES, OreGeneration.ORE_SILVER);
    } // end generateOverworldOres()
    
} // end class OreGeneration
