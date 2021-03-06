package mod.zotmc.onlysilver.config;

import mod.zotmc.onlysilver.OnlySilver;
import net.minecraftforge.common.ForgeConfigSpec;

public final class ServerConfig
{
    // ore generation
    final ForgeConfigSpec.IntValue serverSilverVeinSize;
    final ForgeConfigSpec.IntValue serverSilverVeinCount;
    final ForgeConfigSpec.IntValue serverSilverBottomHeight;
    final ForgeConfigSpec.IntValue serverSilverMaxHeight;

    final ForgeConfigSpec.BooleanValue serverEnableAuraEnchantment;
    final ForgeConfigSpec.BooleanValue serverEnableIncantationEnchantment;
    final ForgeConfigSpec.BooleanValue serverBuildSilverGolem;
    
    final ForgeConfigSpec.BooleanValue serverAddModLootToChests;
    final ForgeConfigSpec.BooleanValue serverEnableSilverOre;
   
    ServerConfig(final ForgeConfigSpec.Builder builder)
    {
        builder.push("General");
        serverAddModLootToChests = builder.comment("Add Only Silver items to chest loot?")
                .translation(OnlySilver.MODID + ".config.addModLootToChests")
                .define("AddModLootToChests", true);
        serverBuildSilverGolem = builder.comment("Can build silver golems?")
                .translation(OnlySilver.MODID + "config.silver_golem_assembly")
                .define("BuildSilverGolem", true);
        builder.pop();
        builder.push("Ore Generation");
        serverEnableSilverOre = builder.comment("Enable silver ore generation?")
                .translation(OnlySilver.MODID + ".config.EnableSilverOre")
                .define("EnableSilverOre", true);
        serverSilverVeinSize = builder.comment("Silver ore vein size")
                .translation(OnlySilver.MODID + "config.silver_vein_size")
                .defineInRange("SilverVeinSize",  5, 0, Integer.MAX_VALUE);
        serverSilverVeinCount = builder.comment("Silver ore vein count per chunk")
                .translation(OnlySilver.MODID + "config.silver_vein_count")
                .defineInRange("SilverVeinCount",  8, 0, Integer.MAX_VALUE);
        serverSilverBottomHeight = builder
                .comment("Silver ore minimum height")
                .translation(OnlySilver.MODID + ".config.silver_min_height")
                .defineInRange("SilverBottomHeight", 0, 1, 255);
        serverSilverMaxHeight = builder
                .comment("Silver ore maximum height")
                .translation(OnlySilver.MODID + ".config.silver_max_height")
                .defineInRange("SilverMaxHeight", 42, 1, 255);
        builder.pop();
        builder.push("Enchantments");
        serverEnableAuraEnchantment = builder.comment("Everlasting Enchantment enabled?")
                .define("EnableSilverAura", true);
        serverEnableIncantationEnchantment = builder.comment("Enable Incantation enchantment?")
                .define("EnableIncantation", true);
        builder.pop();
        
    } // end ctor

} // end-class
