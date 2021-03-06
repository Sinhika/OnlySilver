package mod.zotmc.onlysilver.content;

import java.util.function.Predicate;

import javax.annotation.Nullable;

import mod.zotmc.onlysilver.config.OnlySilverConfig;
import mod.zotmc.onlysilver.entity.SilverGolemEntity;
import mod.zotmc.onlysilver.init.ModBlocks;
import mod.zotmc.onlysilver.init.ModEntities;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.CachedBlockInfo;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SilverWandItem extends Item
{
    @Nullable
    private BlockPattern silverGolemPattern;

    private static final Predicate<BlockState> IS_PUMPKIN = (p_210301_0_) -> {
        return p_210301_0_ != null 
                && (p_210301_0_.getBlock() == Blocks.CARVED_PUMPKIN 
                    || p_210301_0_.getBlock() == Blocks.JACK_O_LANTERN);
     };
    
    public SilverWandItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public ActionResultType useOn(ItemUseContext context)
    {
        ActionResultType result = super.useOn(context);
        
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        
        if (world.isClientSide) 
        {
            return ActionResultType.SUCCESS;
        }
        if (trySpawnSilverGolem(world, pos))
        {
           result = ActionResultType.SUCCESS; 
        } // end-if 
        return result;
    }

    private BlockPattern getSilverGolemPattern()
    {
        if (this.silverGolemPattern == null)
        {
            this.silverGolemPattern = BlockPatternBuilder.start().aisle("^", "#")
                .where('^', CachedBlockInfo.hasState(IS_PUMPKIN))
                .where('#', CachedBlockInfo.hasState(BlockStateMatcher.forBlock(ModBlocks.silver_block.get())))
                .build();
        }
        return this.silverGolemPattern;
    }
    
    /**
     * Attempt to create a silver golem from build blocks.
     * @param worldIn 
     * @param pos BlockPos of clicked-on block.
     * @return true if successful, false if not.
     */
    private boolean trySpawnSilverGolem(World worldIn, BlockPos pos)
    {
        if (! OnlySilverConfig.buildSilverGolem) {
            return false;
        }

        // is block clicked part of silver_golem pattern?
        BlockPattern.PatternHelper patternhelper = this.getSilverGolemPattern().find(worldIn, pos);
        if (patternhelper != null) 
        {
            // replace the golem constituent blocks with air.
            for(int i = 0; i < this.getSilverGolemPattern().getHeight(); ++i) 
            {
                CachedBlockInfo cachedblockinfo = patternhelper.getBlock(0, i, 0);
                worldIn.setBlock(cachedblockinfo.getPos(), Blocks.AIR.defaultBlockState(), 2);
                worldIn.levelEvent(2001, cachedblockinfo.getPos(), Block.getId(cachedblockinfo.getState()));
            } // end-for
            
            // spawn the golem, and give credit to the player for summoning it.
            SilverGolemEntity golem = ModEntities.silver_golem.get().create(worldIn);
            golem.setPlayerCreated(true);
            golem.moveTo((double)pos.getX() + 0.5D, (double)pos.getY() + 0.05D, (double)pos.getZ() + 0.5D, 0.0F, 0.0F);
            worldIn.addFreshEntity(golem);

            for(ServerPlayerEntity serverplayerentity1 : worldIn.getEntitiesOfClass(ServerPlayerEntity.class, golem.getBoundingBox().inflate(5.0D))) 
            {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverplayerentity1, golem);
            }

            // notify neighboring blocks that constituent blocks have been changed to air.
            for(int i1 = 0; i1 < this.getSilverGolemPattern().getWidth(); ++i1) 
            {
                for(int j1 = 0; j1 < this.getSilverGolemPattern().getHeight(); ++j1) 
                {
                   CachedBlockInfo cachedblockinfo1 = patternhelper.getBlock(i1, j1, 0);
                   worldIn.updateNeighborsAt(cachedblockinfo1.getPos(), Blocks.AIR);
                } // end for j1
             } // end for i1
            return true;  // SUCCESS!
        } // end-if
        
        return false;  // sorry, failed.
    } // end trySpawnGolem()
    
    
} // end class
