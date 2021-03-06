package mod.zotmc.onlysilver.entity;

import com.mojang.blaze3d.matrix.MatrixStack;

import mod.zotmc.onlysilver.OnlySilver;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/** 
 * SilverGolemRender is actually based on IronGolemRenderer, but since that class is
 * not parameterized, we have to go up one step, and then do a lot of copypasta.
 *
 */
@OnlyIn(Dist.CLIENT)
public class SilverGolemRenderer extends MobRenderer<SilverGolemEntity, SilverGolemModel>
{
    private static final ResourceLocation SILVER_GOLEM_TEXTURES = 
            new ResourceLocation(OnlySilver.MODID, "textures/entity/silver_golem.png");

    public SilverGolemRenderer(EntityRendererManager renderManagerIn)
    {
        super(renderManagerIn, new SilverGolemModel(),  0.7F);
        // TODO: add the other layers from IronGolemRenderer.
    }

    @Override
    public ResourceLocation getTextureLocation(SilverGolemEntity entity)
    {
        return SILVER_GOLEM_TEXTURES;
    }

    @Override
    protected void setupRotations(SilverGolemEntity entityLiving, MatrixStack matrixStackIn, float ageInTicks,
            float rotationYaw, float partialTicks)
    {
        super.setupRotations(entityLiving, matrixStackIn, ageInTicks, rotationYaw, partialTicks);
        if (!((double)entityLiving.animationSpeed < 0.01D)) 
        {
            float f1 = entityLiving.animationPosition - entityLiving.animationSpeed * (1.0F - partialTicks) + 6.0F;
            float f2 = (Math.abs(f1 % 13.0F - 6.5F) - 3.25F) / 3.25F;
            matrixStackIn.mulPose(Vector3f.ZP.rotationDegrees(6.5F * f2));
         }    
    }

    @Override
    protected void scale(SilverGolemEntity entitylivingbaseIn, MatrixStack matrixStackIn,
            float partialTickTime)
    {
        matrixStackIn.scale(8/14f, 19/29f, 8/14f);
    }
    
} // end-class
