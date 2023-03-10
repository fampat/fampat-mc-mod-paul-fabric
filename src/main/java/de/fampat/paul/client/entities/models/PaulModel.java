package de.fampat.paul.client.entities.models;

import com.google.common.collect.ImmutableList;
import de.fampat.paul.entities.PaulEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelData;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelPartBuilder;
import net.minecraft.client.model.ModelPartData;
import net.minecraft.client.model.ModelTransform;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.TintableAnimalModel;

@Environment(value = EnvType.CLIENT)
public class PaulModel<T extends PaulEntity> extends TintableAnimalModel<T> {
    public final ModelPart head;
    private final ModelPart tongue_r1;
    private final ModelPart ear_right_r1;
    private final ModelPart ear_left_r1;
    private final ModelPart body;
    private final ModelPart neck2_r1;
    private final ModelPart neck1_r1;
    private final ModelPart tail;
    private final ModelPart leg0;
    private final ModelPart leg1;
    private final ModelPart leg2;
    private final ModelPart leg3;

    private float headAngle;

    public PaulModel(ModelPart part) {
        this.head = part.getChild("head");
        this.tongue_r1 = part.getChild("head").getChild("tongue_r1");
        this.ear_right_r1 = part.getChild("head").getChild("ear_right_r1");
        this.ear_left_r1 = part.getChild("head").getChild("ear_left_r1");
        this.body = part.getChild("body");
        this.neck2_r1 = part.getChild("body").getChild("neck2_r1");
        this.neck1_r1 = part.getChild("body").getChild("neck1_r1");
        this.tail = part.getChild("tail");
        this.leg0 = part.getChild("leg0");
        this.leg1 = part.getChild("leg1");
        this.leg2 = part.getChild("leg2");
        this.leg3 = part.getChild("leg3");
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();

        // Head-Group
        modelPartData.addChild(
                "head",
                ModelPartBuilder.create()
                        .uv(26, 13).cuboid(-3.0F, -4.0F, -3.5F, 6.0F, 6.0F, 5.0F, false)
                        .uv(38, 53).cuboid(-1.0F, -2.0F, -6.5F, 2.0F, 1.0F, 3.0F, false)
                        .uv(23, 0).cuboid(-1.5F, -1.0F, -6.5F, 3.0F, 1.0F, 3.0F, false)
                        .uv(0, 5).cuboid(-0.5F, -1.5F, -7.0F, 1.0F, 1.0F, 1.0F, false),
                ModelTransform.pivot(0.0F, 7.5F, -9.5F));

        modelPartData.getChild("head").addChild(
                "jaw_r1",
                ModelPartBuilder.create()
                        .uv(44, 9).cuboid(-1.5F, -0.5F, -3.5F, 3.0F, 1.0F, 4.0F, false),
                ModelTransform.of(0.0F, 0.5F, -3.0F, 0.3491F, 0.0F, 0.0F));

        modelPartData.getChild("head").addChild(
                "tongue_r1",
                ModelPartBuilder.create()
                        .uv(34, 37).cuboid(-1.0F, -0.5F, -3.0F, 2.0F, 1.0F, 5.0F, false),
                ModelTransform.of(0.0F, 0.5F, -4.5F, 0.3491F, 0.0F, 0.0F));

        modelPartData.getChild("head").addChild(
                "ear_right_r1",
                ModelPartBuilder.create()
                        .uv(46, 0).cuboid(-1.5F, -1.0F, -1.5F, 1.0F, 5.0F, 3.0F, false),
                ModelTransform.of(-2.0F, -2.5F, -1.0F, 0.0F, 0.0F, 0.3491F));

        modelPartData.getChild("head").addChild(
                "ear_left_r1",
                ModelPartBuilder.create()
                        .uv(45, 44).cuboid(0.5F, -1.0F, -1.5F, 1.0F, 5.0F, 3.0F, false),
                ModelTransform.of(2.0F, -2.5F, -1.0F, 0.0F, 0.0F, -0.3491F));

        // Body-Group
        modelPartData.addChild(
                "body",
                ModelPartBuilder.create()
                        .uv(0, 0).cuboid(-3.5F, -4.0F, -7.5F, 7.0F, 9.0F, 8.0F, false)
                        .uv(0, 18).cuboid(-3.0F, -3.5F, 0.5F, 6.0F, 7.0F, 8.0F, false),
                ModelTransform.pivot(0.0F, 12.0F, -1.0F));

        modelPartData.getChild("body").addChild(
                "neck2_r1",
                ModelPartBuilder.create()
                        .uv(39, 25).cuboid(-3.0F, -5.5F, 1.5F, 4.0F, 7.0F, 3.0F, false),
                ModelTransform.of(1.0F, -0.5F, -8.0F, 0.6109F, 0.0F, 0.0F));

        modelPartData.getChild("body").addChild(
                "neck1_r1",
                ModelPartBuilder.create()
                        .uv(0, 34).cuboid(-3.5F, -3.5F, -1.0F, 5.0F, 7.0F, 3.0F, false),
                ModelTransform.of(1.0F, -0.5F, -8.0F, 0.5236F, 0.0F, 0.0F));

        // Tail-Group
        modelPartData.addChild(
                "tail",
                ModelPartBuilder.create()
                        .uv(55, 5).cuboid(-0.5F, -1.0F, -0.5F, 1.0F, 1.0F, 3.0F, false),
                ModelTransform.pivot(0.0F, 10.0F, 7.5F));

        modelPartData.getChild("tail").addChild(
                "tail3_r1",
                ModelPartBuilder.create()
                        .uv(5, 50).cuboid(-0.5F, 0.0F, 1.5F, 1.0F, 1.0F, 4.0F, false),
                ModelTransform.of(0.0F, -0.5F, 2.5F, 0.6109F, 0.0F, 0.0F));

        modelPartData.getChild("tail").addChild(
                "tail2_r1",
                ModelPartBuilder.create()
                        .uv(55, 0).cuboid(-0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 3.0F, false),
                ModelTransform.of(0.0F, -0.5F, 2.5F, 0.3491F, 0.0F, 0.0F));

        // Leg-Groups
        modelPartData.addChild(
                "leg0",
                ModelPartBuilder.create()
                        .uv(25, 42).cuboid(-1.0F, -2.5F, -1.25F, 2.0F, 6.0F, 3.0F, false)
                        .uv(0, 45).cuboid(-1.0F, 3.5F, -0.75F, 2.0F, 6.0F, 2.0F, false)
                        .uv(16, 52).cuboid(-1.0F, 9.5F, -1.75F, 2.0F, 1.0F, 3.0F, false),
                ModelTransform.pivot(-3.0F, 13.5F, -6.25F));

        modelPartData.addChild(
                "leg1",
                ModelPartBuilder.create()
                        .uv(31, 0).cuboid(-1.0F, -2.5F, -2.5F, 2.0F, 7.0F, 5.0F, false)
                        .uv(54, 44).cuboid(-1.0F, 6.5F, 0.5F, 2.0F, 3.0F, 2.0F, false)
                        .uv(27, 52).cuboid(-1.0F, 9.5F, -0.5F, 2.0F, 1.0F, 3.0F, false)
                        .uv(0, 18).cuboid(-0.5F, 4.5F, 0.0F, 1.0F, 2.0F, 2.0F, false),
                ModelTransform.pivot(-3.0F, 13.5F, 5.5F));

        modelPartData.getChild("leg1").addChild(
                "leg_1_2_r1",
                ModelPartBuilder.create()
                        .uv(49, 36).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, false),
                ModelTransform.of(0.0F, 5.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

        modelPartData.addChild(
                "leg2",
                ModelPartBuilder.create()
                        .uv(24, 29).cuboid(-1.0F, -2.5F, -2.5F, 2.0F, 7.0F, 5.0F, false)
                        .uv(54, 28).cuboid(-1.0F, 6.5F, 0.5F, 2.0F, 3.0F, 2.0F, false)
                        .uv(51, 50).cuboid(-1.0F, 9.5F, -0.5F, 2.0F, 1.0F, 3.0F, false)
                        .uv(0, 0).cuboid(-0.5F, 4.5F, 0.0F, 1.0F, 2.0F, 2.0F, false),
                ModelTransform.pivot(3.0F, 13.5F, 5.5F));

        modelPartData.getChild("leg2").addChild(
                "leg_2_2_r1",
                ModelPartBuilder.create()
                        .uv(49, 15).cuboid(-1.0F, -2.5F, -1.0F, 2.0F, 5.0F, 2.0F, false),
                ModelTransform.of(0.0F, 5.5F, 0.0F, 0.7854F, 0.0F, 0.0F));

        modelPartData.addChild(
                "leg3",
                ModelPartBuilder.create()
                        .uv(14, 42).cuboid(-1.0F, -2.5F, -1.25F, 2.0F, 6.0F, 3.0F, false)
                        .uv(51, 23).cuboid(-1.0F, 9.5F, -1.75F, 2.0F, 1.0F, 3.0F, false)
                        .uv(36, 44).cuboid(-1.0F, 3.5F, -0.75F, 2.0F, 6.0F, 2.0F, false),
                ModelTransform.pivot(3.0F, 13.5F, -6.25F));

        return TexturedModelData.of(modelData, 64, 64);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of(this.head);
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.leg0, this.leg1, this.leg2, this.leg3, this.tail);
    }

    @Override
    public void animateModel(PaulEntity paulEntity, float limbAngle, float limbDistance, float tickDelta) {
        // Initial animation settings
        this.head.pitch = 0F;
        this.head.setPivot(0.0F, 7.5F, -9.5F);
        this.neck1_r1.setPivot(1.0F, -0.5F, -8.0F);
        this.neck2_r1.setPivot(1.0F, -0.5F, -8.0F);
        this.body.setPivot(0.0F, 12.0F, -1.0F);
        this.leg0.setPivot(-3.0F, 13.5F, -6.25F);
        this.leg1.setPivot(-3.0F, 13.5F, 5.5F);
        this.leg2.setPivot(3.0F, 13.5F, 5.5F);
        this.leg3.setPivot(3.0F, 13.5F, -6.25F);
        this.tail.setPivot(0.0F, 10.0F, 7.5F);
        this.tongue_r1.setPivot(0.0F, 0.5F, -4.5F);
        this.tongue_r1.pitch = 0.35F;
        this.neck1_r1.pitch = 0.6109F;
        this.neck2_r1.pitch = 0.6109F;

        // Tongue animation
        if (paulEntity.isCarryBone() || paulEntity.eatGrassTimer > 0) {
            this.tongue_r1.setPivot(0.0F, 0.5F, -3.4F);
        } else {
            this.tongue_r1.pitch = 0.35F + (paulEntity.getTongueTick() * ((float) Math.PI / 1800F));
            this.tongue_r1.yaw = paulEntity.getTongueTick() * ((float) Math.PI / 1200F);
        }

        // Shake animation
        this.head.roll = paulEntity.getBegAnimationProgress(tickDelta)
                + paulEntity.getShakeAnimationProgress(tickDelta, 0.0f);
        this.body.roll = paulEntity.getShakeAnimationProgress(tickDelta, -0.16f);
        this.tail.roll = paulEntity.getShakeAnimationProgress(tickDelta, -0.2f);

        // Animation sets
        if (paulEntity.isInSittingPose()) {
            // Sit animation set
            this.head.setPivot(0F, 5F, -3.75F);
            this.body.pitch = -((float) Math.PI / 4.25F);
            this.body.setPivot(0.0F, 14.5F, -1.0F);
            this.leg0.setPivot(-3.0F, 13.5F, -6.25F);
            this.leg0.pitch = 0F;
            this.leg1.setPivot(-3.0F, 21.5F, 5.5F);
            this.leg1.pitch = -((float) Math.PI / 2F);
            this.leg3.setPivot(3.0F, 13.5F, -6.25F);
            this.leg3.pitch = 0F;
            this.leg2.setPivot(3.0F, 21.5F, 5.5F);
            this.leg2.pitch = -((float) Math.PI / 2F);
            this.tail.setPivot(0.0F, 19.5F, 7.5F);
        } else if (paulEntity.eatGrassTimer > 0) {
            // Eating gras animation set
            this.leg0.pitch = 0F;
            this.leg1.pitch = 0F;
            this.leg2.pitch = 0F;
            this.leg3.pitch = 0F;
            this.neck1_r1.setPivot(1F, 0.5F, -6F);
            this.neck2_r1.setPivot(1F, 0.5F, -5F);
            this.neck1_r1.pitch = 1.7F;
            this.neck2_r1.pitch = 2.1F;
            this.head.pivotY = 6.0f + paulEntity.getNeckAngle(tickDelta) * 9.0f;
            this.headAngle = paulEntity.getHeadAngle(tickDelta);
        } else {
            // Default animation set
            this.body.pitch = 0.0F;
            this.leg0.pitch = (float) Math.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            this.leg1.pitch = (float) Math.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
            this.leg2.pitch = (float) Math.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            this.leg3.pitch = (float) Math.cos(limbAngle * 0.6662F + (float) Math.PI) * 1.4F * limbDistance;
            this.tail.yaw = (float) Math.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
            this.ear_left_r1.roll = (float) (Math.cos(limbAngle * 0.6662F + (float) Math.PI) * 0.3F * limbDistance)
                    - 0.2F;
            this.ear_right_r1.roll = (float) (Math.cos(limbAngle * 0.6662F) * 0.3F * limbDistance) + 0.2F;
        }
    }

    // Change the head angle to make Paul "look up" when watching the player,
    // or "look down" when eating grass
    @Override
    public void setAngles(PaulEntity paulEntity, float limbAngle, float limbDistance, float animationProgress,
            float headYaw, float headPitch) {
        this.head.pitch = headPitch * ((float) Math.PI / 270);
        this.head.yaw = headYaw * ((float) Math.PI / 180);

        if (paulEntity.eatGrassTimer > 0) {
            this.head.pitch = this.headAngle;
        }
    }
}