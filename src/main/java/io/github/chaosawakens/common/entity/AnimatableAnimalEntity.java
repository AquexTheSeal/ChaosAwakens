package io.github.chaosawakens.common.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

/**
 * @author invalid2
 */
public abstract class AnimatableAnimalEntity extends AnimalEntity implements IAnimatable {

    protected static final DataParameter<Boolean> MOVING = EntityDataManager.defineId(AnimatableAnimalEntity.class, DataSerializers.BOOLEAN);
    protected static final DataParameter<Boolean> ATTACKING = EntityDataManager.defineId(AnimatableAnimalEntity.class, DataSerializers.BOOLEAN);

    protected boolean isAnimationFinished = false;

    /**
     * @param type
     * @param worldIn
     */
    public AnimatableAnimalEntity(EntityType<? extends AnimalEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    abstract public void registerControllers(AnimationData data);

    @Override
    abstract public AnimationFactory getFactory();

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(MOVING, false);
        this.entityData.define(ATTACKING, false);
    }

    public boolean getMoving() {
        return this.entityData.get(MOVING);
    }

    public void setMoving(boolean moving) {
        this.entityData.set(MOVING, moving);
    }

    public boolean getAttacking() {
        return this.entityData.get(ATTACKING);
    }

    public void setAttacking(boolean attacking) {
        this.entityData.set(ATTACKING, attacking);
    }
}