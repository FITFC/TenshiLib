package io.github.flemmli97.tenshilib.patreon.client;

import com.mojang.blaze3d.vertex.PoseStack;
import io.github.flemmli97.tenshilib.patreon.PatreonPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PatreonLayer<T extends Player, M extends EntityModel<T> & HeadedModel> extends RenderLayer<T, M> {

    public PatreonLayer(LivingEntityRenderer<T, M> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack stack, MultiBufferSource buffer, int packedLight, T entity, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entity.isInvisible())
            return;
        PatreonPlatform.INSTANCE.playerSettings(entity).ifPresent(setting -> {
            if (entity.isInvisibleTo(Minecraft.getInstance().player) || setting.effect() == null || !setting.shouldRender())
                return;
            PatreonModelProvider.EffectRenderer<?> renderer = PatreonModelProvider.get(setting.effect());
            if (renderer == null)
                return;
            stack.pushPose();
            if (this.getParentModel().young) {
                stack.translate(0.0F, 0.75F, 0.0F);
                stack.scale(0.5F, 0.5F, 0.5F);
            }
            switch (setting.getRenderLocation()) {
                case CIRCLING -> {
                    stack.scale(0.7f, 0.7f, 0.7f);
                    float tick = entity.tickCount + partialTicks;
                    stack.translate(Mth.cos(Mth.DEG_TO_RAD * tick * 5),
                            Mth.sin(Mth.DEG_TO_RAD * tick * 1.25f) * 1.4 - 1,
                            Mth.sin(Mth.DEG_TO_RAD * tick * 5));
                }
                case CIRCLINGREVERSE -> {
                    stack.scale(0.7f, 0.7f, 0.7f);
                    float tick = entity.tickCount + partialTicks;
                    stack.translate(-Mth.cos(Mth.DEG_TO_RAD * tick * 5),
                            Mth.sin(Mth.DEG_TO_RAD * tick * 1.25f) * 1.4 - 1,
                            -Mth.sin(Mth.DEG_TO_RAD * tick * 5));
                }
                case HATNOARMOR, HAT -> {
                    this.getParentModel().getHead().translateAndRotate(stack);
                    stack.translate(0, -2, 0.0);
                }
                case LEFTSHOULDER -> stack.translate(0.4, entity.isCrouching() ? -1.3 : -1.5, 0.0);
                case RIGHTSHOULDER -> stack.translate(-0.4, entity.isCrouching() ? -1.3 : -1.5, 0.0);
                case BACK -> stack.translate(0, entity.isCrouching() ? -0.6 : -0.8, 0.8);
            }
            int hexColor = setting.getColor();
            renderer.render(stack, buffer, packedLight, entity, limbSwing, limbSwingAmount, partialTicks, ageInTicks, netHeadYaw, headPitch, hexColor >> 16 & 255, hexColor >> 8 & 255, hexColor & 255, hexColor >> 24 & 255, setting.getRenderLocation());
            stack.popPose();
        });
    }
}
