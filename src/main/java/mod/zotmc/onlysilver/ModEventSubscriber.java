package mod.zotmc.onlysilver;

import static net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import mod.zotmc.onlysilver.api.OnlySilverRegistry;
import mod.zotmc.onlysilver.config.ConfigHelper;
import mod.zotmc.onlysilver.config.ConfigHolder;
import mod.zotmc.onlysilver.config.OnlySilverConfig;
import mod.zotmc.onlysilver.enchant.OnlySilverLootModifiers;
import mod.zotmc.onlysilver.entity.SilverGolemEntity;
import mod.zotmc.onlysilver.generation.OreGeneration;
import mod.zotmc.onlysilver.init.ModBlocks;
import mod.zotmc.onlysilver.init.ModEntities;
import mod.zotmc.onlysilver.init.ModItems;
import mod.zotmc.onlysilver.init.ModTabGroups;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = OnlySilver.MODID, bus = MOD)
public final class ModEventSubscriber 
{
	private static final Logger LOGGER = LogManager.getLogger(OnlySilver.MODID + " Mod Event Subscriber");

    /**
     * For best inter-mod compatibility, run ore generation in a enqueueWork, per dieseiben07.
     * @param event
     */
    @SubscribeEvent
    public static void onCommonSetup(final FMLCommonSetupEvent event)
    {
        // using lambdas to make this nigh incomprehensible...
        OnlySilverRegistry.registerSilverPredicate( 
                t -> ModItems.ITEMS.getEntries()
                        .stream()
                        .anyMatch(ii -> ii.get() == t.getItem()));
        
        if (OnlySilverConfig.enableSilverOre)
        {
            event.enqueueWork(() -> {
                OreGeneration.initOverworldFeatures();
            });
        }
        LOGGER.debug("Common setup done");
    } // end onCommonSetup

    /**
     *  
     */
    @SubscribeEvent
    public static void onEntityAttributeCreation( final EntityAttributeCreationEvent event)
    {
        event.put(ModEntities.silver_golem.get(), SilverGolemEntity.prepareAttributes().build());        
    } // end onEntityAttributeCreation
    
	/**
	 * This method will be called by Forge when it is time for the mod to register its Items.
	 * This method will always be called after the Block registry method.
	 */
	@SubscribeEvent
	public static void onRegisterItems(final RegistryEvent.Register<Item> event)
	{
		final IForgeRegistry<Item> registry = event.getRegistry();

		// We need to go over the entire registry so that we include any potential Registry Overrides
        // Automatically register BlockItems for all our Blocks
        ModBlocks.BLOCKS.getEntries().stream()
                .map(RegistryObject::get)
                // You can do extra filtering here if you don't want some blocks to have an BlockItem automatically registered for them
                // .filter(block -> needsItemBlock(block))
                // Register the BlockItem for the block
                .forEach(block -> {
                    // Make the properties, and make it so that the item will be on our ItemGroup (CreativeTab)
                    final Item.Properties properties = 
                            new Item.Properties().tab(ModTabGroups.MOD_ITEM_GROUP);
                    // Create the new BlockItem with the block and it's properties
                    final BlockItem blockItem = new BlockItem(block, properties);
                    // Set the new BlockItem's registry name to the block's registry name
                    blockItem.setRegistryName(block.getRegistryName());
                    // Register the BlockItem
                    registry.register(blockItem);
                });
		LOGGER.debug("Registered BlockItems");
	}  // end onRegisterItems()

	@SubscribeEvent
	public static void onModConfigEvent(final ModConfig.ModConfigEvent event)
	{
		final ModConfig config = event.getConfig();

		// Rebake the configs when they change
		if (config.getSpec() == ConfigHolder.SERVER_SPEC) {
			ConfigHelper.bakeServer(config);
		}
        if (config.getSpec() == ConfigHolder.CLIENT_SPEC) {
            ConfigHelper.bakeClient(config);
        }
	} // onModConfigEvent
	
	/* FOR INCANTATION ENCHANTMENT */
	@SubscribeEvent
    public static void onRegisterModifierSerializers(
            @Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event)
    {
        event.getRegistry().register(
                new OnlySilverLootModifiers.IncantationLootModifier.Serializer().setRegistryName(
                        new ResourceLocation(OnlySilver.MODID, "incantation_enchantment")) );
    } // end registerModifierSerializers

	
} // end class ModEventSubscriber
