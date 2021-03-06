package mod.zotmc.onlysilver.init;

import mod.zotmc.onlysilver.OnlySilver;
import net.minecraft.block.Block;
import net.minecraft.block.OreBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Holds a list of all our {@link Block}s.
 * Suppliers that create Blocks are added to the DeferredRegister.
 * The DeferredRegister is then added to our mod event bus in our constructor.
 * When the Block Registry Event is fired by Forge and it is time for the mod to
 * register its Blocks, our Blocks are created and registered by the DeferredRegister.
 * The Block Registry Event will always be called before the Item registry is filled.
 * Note: This supports registry overrides.
 *
 * @author Sinhika, notes by Cadiboo
 */
public final class ModBlocks
{
    public static final DeferredRegister<Block> BLOCKS = 
           DeferredRegister.create(ForgeRegistries.BLOCKS, OnlySilver.MODID);

    // Ore Blocks
    public static final RegistryObject<OreBlock> silver_ore = BLOCKS.register("silver_ore",
            () -> new OreBlock(Block.Properties.of(Material.STONE)
                    .strength( 3.0F, 10.0F).requiresCorrectToolForDrops()
                    .harvestTool(ToolType.PICKAXE).harvestLevel(1)));

    // Metal Blocks
    public static final RegistryObject<Block> silver_block = BLOCKS.register("silver_block",
            () -> new Block(Block.Properties.of(Material.METAL)
                    .strength(7.0F, 12.0F).requiresCorrectToolForDrops()
                    .sound(SoundType.METAL)
                    .harvestTool(ToolType.PICKAXE).harvestLevel(0)));

}  // end class ModBlocks
