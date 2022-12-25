package de.fampat.paul.goals;

import de.fampat.paul.entities.PaulEntity;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.mob.MobEntity;

public class PaulEatGrassGoal extends EatGrassGoal {
   PaulEntity paulEntity;

   public PaulEatGrassGoal(MobEntity mobEntity) {
      super(mobEntity);
      this.paulEntity = (PaulEntity) mobEntity;
   }

   @Override
   public boolean canStart() {
      if (this.paulEntity.isCarryBone() || this.paulEntity.isInSittingPose()) {
         return false;
      }

      return super.canStart();
   }

   @Override
   public boolean shouldContinue() {
      if (this.paulEntity.isCarryBone() || this.paulEntity.isInSittingPose()) {
         return false;
      }

      return super.shouldContinue();
   }
}
