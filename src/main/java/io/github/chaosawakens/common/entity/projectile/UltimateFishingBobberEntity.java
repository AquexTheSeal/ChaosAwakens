package io.github.chaosawakens.common.entity.projectile;

import java.util.Collections;
import java.util.List;
import java.util.Random;

import io.github.chaosawakens.common.entity.LavaEelEntity;
import io.github.chaosawakens.common.registry.CAEntityTypes;
import io.github.chaosawakens.common.registry.CAItems;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameterSets;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.network.IPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.common.registry.IEntityAdditionalSpawnData;
import net.minecraftforge.fml.network.FMLPlayMessages;

public class UltimateFishingBobberEntity extends FishingBobberEntity implements IEntityAdditionalSpawnData  {
	  private final Random syncronizedRandom = new Random();
	    private int outOfLiquidTime;
	    private boolean biting;
	    private FluidState fluid;
	    private static final DataParameter<Integer> DATA_HOOKED_ENTITY = EntityDataManager.defineId(FishingBobberEntity.class, DataSerializers.INT);
	    private static final DataParameter<Boolean> DATA_BITING = EntityDataManager.defineId(FishingBobberEntity.class, DataSerializers.BOOLEAN);
	    private int life;
	    private int nibble;
	    private int timeUntilLured;
	    private int timeUntilHooked;
	    private float fishAngle;
	    private boolean openWater = true;
	    private Entity hookedIn;
	    private State currentState = UltimateFishingBobberEntity.State.FLYING;
	    private final int luck;
	    private final int lureSpeed;

	    public UltimateFishingBobberEntity(PlayerEntity p_i50220_1_, World p_i50220_2_, int p_i50220_3_, int p_i50220_4_) {
	        super(p_i50220_1_, p_i50220_2_, p_i50220_3_, p_i50220_4_);
	        this.luck = p_i50220_3_;
	        this.lureSpeed = p_i50220_4_;
	    }

	    @Override
	    protected void defineSynchedData() {
	        this.getEntityData().define(DATA_HOOKED_ENTITY, 0);
	        this.getEntityData().define(DATA_BITING, false);
	    }
	    
	    @Override
	    public void readSpawnData(PacketBuffer additionalData) {
	    }
	    
	    @Override
	    public void writeSpawnData(PacketBuffer buffer) {
	    }

	    public void tick() {
	        this.syncronizedRandom.setSeed(this.getUUID().getLeastSignificantBits() ^ this.level.getGameTime());
	        super.tick();
	        PlayerEntity playerentity = this.getPlayerOwner();
	        if (playerentity == null) {
	            this.remove();
	        } else if (this.level.isClientSide || !this.shouldStopFishing(playerentity)) {
	            if (this.onGround) {
	                ++this.life;
	                if (this.life >= 2200) {
	                    this.remove();
	                    return;
	                }
	            } else {
	                this.life = 0;
	            }

	            float f = 0.0F;
	            BlockPos blockpos = this.blockPosition();
	            FluidState fluidstate = this.level.getFluidState(blockpos);
	            if (fluidstate.is(FluidTags.WATER) || fluidstate.is(FluidTags.LAVA)) {
	                f = fluidstate.getHeight(this.level, blockpos);
	                this.fluid = fluidstate;
	            }

	            boolean flag = f > 0.0F;
	            if (this.currentState == UltimateFishingBobberEntity.State.FLYING) {
	                if (this.hookedIn != null) {
	                    this.setDeltaMovement(Vector3d.ZERO);
	                    this.currentState = UltimateFishingBobberEntity.State.HOOKED_IN_ENTITY;
	                    return;
	                }

	                if (flag) {
	                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.3D, 0.2D, 0.3D));
	                    this.currentState = UltimateFishingBobberEntity.State.BOBBING;
	                    return;
	                }

	                this.checkCollision();
	            } else {
	                if (this.currentState == UltimateFishingBobberEntity.State.HOOKED_IN_ENTITY) {
	                    if (this.hookedIn != null) {
	                        if (!this.hookedIn.isAlive()) {
	                            this.hookedIn = null;
	                            this.currentState = UltimateFishingBobberEntity.State.FLYING;
	                        } else {
	                            this.setPos(this.hookedIn.getX(), this.hookedIn.getY(0.8D), this.hookedIn.getZ());
	                        }
	                    }

	                    return;
	                }

	                if (this.currentState == UltimateFishingBobberEntity.State.BOBBING) {
	                    Vector3d vector3d = this.getDeltaMovement();
	                    double d0 = this.getY() + vector3d.y - (double)blockpos.getY() - (double)f;
	                    if (Math.abs(d0) < 0.01D) {
	                        d0 += Math.signum(d0) * 0.1D;
	                    }

	                    this.setDeltaMovement(vector3d.x * 0.9D, vector3d.y - d0 * (double)this.random.nextFloat() * 0.2D, vector3d.z * 0.9D);
	                    if (this.nibble <= 0 && this.timeUntilHooked <= 0) {
	                        this.openWater = true;
	                    } else {
	                        this.openWater = this.openWater && this.outOfLiquidTime < 10 && this.calculateOpenWater(blockpos);
	                    }

	                    if (flag) {
	                        this.outOfLiquidTime = Math.max(0, this.outOfLiquidTime - 1);
	                        if (this.biting) {
	                            this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.1D * (double)this.syncronizedRandom.nextFloat() * (double)this.syncronizedRandom.nextFloat(), 0.0D));
	                        }

	                        if (!this.level.isClientSide) {
	                            this.catchingFish(blockpos);
	                        }
	                    } else {
	                        this.outOfLiquidTime = Math.min(10, this.outOfLiquidTime + 1);
	                    }
	                }
	            }

	            if (!fluidstate.is(FluidTags.WATER) || !fluidstate.is(FluidTags.LAVA)) {
	                this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.03D, 0.0D));
	            }

	            this.move(MoverType.SELF, this.getDeltaMovement());
	            this.updateRotation();
	            if (this.currentState == UltimateFishingBobberEntity.State.FLYING && (this.onGround || this.horizontalCollision)) {
	                this.setDeltaMovement(Vector3d.ZERO);
	            }

	            double d1 = 0.92D;
	            this.setDeltaMovement(this.getDeltaMovement().scale(0.92D));
	            this.reapplyPosition();
	        }
	    }

	    private boolean shouldStopFishing(PlayerEntity p_234600_1_) {
	        ItemStack itemstack = p_234600_1_.getMainHandItem();
	        ItemStack itemstack1 = p_234600_1_.getOffhandItem();
	        boolean flag = itemstack.getItem() == CAItems.ULTIMATE_FISHING_ROD.get();
	        boolean flag1 = itemstack1.getItem() == CAItems.ULTIMATE_FISHING_ROD.get();
	        if (!p_234600_1_.removed && p_234600_1_.isAlive() && (flag || flag1) && !(this.distanceToSqr(p_234600_1_) > 1024.0D)) {
	           return false;
	        } else {
	           this.remove();
	           return true;
	        }
	     }
	    
	    @Override
	    public boolean canChangeDimensions() {
	    	return true;
	    }
	    
	     protected boolean canHitEntity(Entity p_230298_1_) {
	        return super.canHitEntity(p_230298_1_) || p_230298_1_.isAlive() && p_230298_1_ instanceof ItemEntity;
	     }

	     protected void onHitEntity(EntityRayTraceResult p_213868_1_) {
	        super.onHitEntity(p_213868_1_);
	        if (!this.level.isClientSide) {
	           this.hookedIn = p_213868_1_.getEntity();
	           this.setHookedEntity();
	        }

	     }

	     protected void onHitBlock(BlockRayTraceResult p_230299_1_) {
	        super.onHitBlock(p_230299_1_);
	        this.setDeltaMovement(this.getDeltaMovement().normalize().scale(p_230299_1_.distanceTo(this)));
	     }
	    
	    @OnlyIn(Dist.CLIENT)
	    public void handleEntityEvent(byte p_70103_1_) {
	       if (p_70103_1_ == 31 && this.level.isClientSide && this.hookedIn instanceof PlayerEntity && ((PlayerEntity)this.hookedIn).isLocalPlayer()) {
	          this.bringInHookedEntity();
	       }

	       super.handleEntityEvent(p_70103_1_);
	    }
	    
	    private void setHookedEntity() {
	        this.getEntityData().set(DATA_HOOKED_ENTITY, this.hookedIn.getId() + 1);
	    }

	    private void catchingFish(BlockPos p_190621_1_) {
	        ServerWorld serverworld = (ServerWorld)this.level;
	        int i = 1;
	        BlockPos blockpos = p_190621_1_.above();
	        if (this.fluid.is(FluidTags.WATER)) {
	            if (this.random.nextFloat() < 0.25F && this.level.isRainingAt(blockpos)) {
	                ++i;
	            }

	            if (this.random.nextFloat() < 0.5F && !this.level.canSeeSky(blockpos)) {
	                --i;
	            }
	            else if (this.fluid.is(FluidTags.LAVA)) {
		            if (this.random.nextFloat() < 0.25F && this.fireImmune()) {
		                ++i;
		            }

		            if (this.random.nextFloat() < 0.5F && !this.level.canSeeSky(blockpos)) {
		                ++i;
		            }
		            
		            if (this.random.nextFloat() < 0.4F && this.level.isRaining() || this.level.isDay()) {
		            	--i;
		            }
	            }
	        } else {
	            // Better chance to get goodies in Nether-temperature dimensions
	            if (this.random.nextFloat() < 0.25F && this.level.dimensionType().ultraWarm()) {
	                ++i;
	            }

	            if (this.random.nextFloat() < 0.5F && this.level.isRainingAt(blockpos) && this.level.canSeeSky(blockpos)) {
	                --i;
	            }
	        }

	        if (this.nibble > 0) {
	            --this.nibble;
	            if (this.nibble <= 0) {
	                this.timeUntilLured = 0;
	                this.timeUntilHooked = 0;
	                this.getEntityData().set(DATA_BITING, false);
	            }
	        } else if (this.timeUntilHooked > 0) {
	            this.timeUntilHooked -= i;
	            if (this.timeUntilHooked > 0) {
	                this.fishAngle = (float)((double)this.fishAngle + this.random.nextGaussian() * 4.0D);
	                float f = this.fishAngle * ((float)Math.PI / 180F);
	                float f1 = MathHelper.sin(f);
	                float f2 = MathHelper.cos(f);
	                double d0 = this.getX() + (double)(f1 * (float)this.timeUntilHooked * 0.1F);
	                double d1 = (double)((float)MathHelper.floor(this.getY()) + 1.0F);
	                double d2 = this.getZ() + (double)(f2 * (float)this.timeUntilHooked * 0.1F);
	                BlockState blockstate = serverworld.getBlockState(new BlockPos(d0, d1 - 1.0D, d2));
	                if (serverworld.getBlockState(new BlockPos((int)d0, (int)d1 - 1, (int)d2)).getMaterial() == net.minecraft.block.material.Material.WATER) {
	                    if (this.random.nextFloat() < 0.15F) {
	                        serverworld.sendParticles(this.fluid.is(FluidTags.WATER) ? ParticleTypes.BUBBLE : ParticleTypes.SPLASH, d0, d1 - (double)0.1F, d2, 1, (double)f1, 0.1D, (double)f2, 0.0D);
	                    }
	                    float f3 = f1 * 0.04F;
	                    float f4 = f2 * 0.04F;
	                    serverworld.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)f4, 0.01D, (double)(-f3), 1.0D);
	                    serverworld.sendParticles(ParticleTypes.FISHING, d0, d1, d2, 0, (double)(-f4), 0.01D, (double)f3, 1.0D);
	                }
	            } else {
	                this.playSound(SoundEvents.FISHING_BOBBER_SPLASH, 0.25F, 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.4F);
	                double d3 = this.getY() + 0.5D;
	                serverworld.sendParticles(this.fluid.is(FluidTags.WATER) ? ParticleTypes.BUBBLE : ParticleTypes.SPLASH, this.getX(), d3, this.getZ(), (int)(1.0F + this.getBbWidth() * 20.0F), (double)this.getBbWidth(), 0.0D, (double)this.getBbWidth(), (double)0.2F);
	                serverworld.sendParticles(ParticleTypes.FISHING, this.getX(), d3, this.getZ(), (int)(1.0F + this.getBbWidth() * 20.0F), (double)this.getBbWidth(), 0.0D, (double)this.getBbWidth(), (double)0.2F);
	                this.nibble = MathHelper.nextInt(this.random, 20, 40);
	                this.getEntityData().set(DATA_BITING, true);
	            }
	        } else if (this.timeUntilLured > 0) {
	            this.timeUntilLured -= i;
	            float f5 = 0.15F;
	            if (this.timeUntilLured < 20) {
	                f5 = (float)((double)f5 + (double)(20 - this.timeUntilLured) * 0.05D);
	            } else if (this.timeUntilLured < 40) {
	                f5 = (float)((double)f5 + (double)(40 - this.timeUntilLured) * 0.02D);
	            } else if (this.timeUntilLured < 60) {
	                f5 = (float)((double)f5 + (double)(60 - this.timeUntilLured) * 0.01D);
	            }

	            if (this.random.nextFloat() < f5) {
	                float f6 = MathHelper.nextFloat(this.random, 0.0F, 360.0F) * ((float)Math.PI / 180F);
	                float f7 = MathHelper.nextFloat(this.random, 25.0F, 60.0F);
	                double d4 = this.getX() + (double)(MathHelper.sin(f6) * f7 * 0.1F);
	                double d5 = (double)((float)MathHelper.floor(this.getY()) + 1.0F);
	                double d6 = this.getZ() + (double)(MathHelper.cos(f6) * f7 * 0.1F);
	                BlockState blockstate1 = serverworld.getBlockState(new BlockPos(d4, d5 - 1.0D, d6));
	                if (serverworld.getBlockState(new BlockPos(d4, d5 - 1.0D, d6)).getMaterial() == net.minecraft.block.material.Material.WATER) {
	                    serverworld.sendParticles(ParticleTypes.SPLASH, d4, d5, d6, 2 + this.random.nextInt(2), (double)0.1F, 0.0D, (double)0.1F, 0.0D);
	                }
	                else if (serverworld.getBlockState(new BlockPos(d4, d5 - 1.0D, d6)).getMaterial() == net.minecraft.block.material.Material.LAVA) {
	                	serverworld.sendParticles(ParticleTypes.LAVA, d4, d5, d6, 2 + this.random.nextInt(2), (double)0.1F, 0.0D, (double)0.1F, 0.0D);
	                }
	            }

	            if (this.timeUntilLured <= 0) {
	                this.fishAngle = MathHelper.nextFloat(this.random, 0.0F, 360.0F);
	                this.timeUntilHooked = MathHelper.nextInt(this.random, 20, 80);
	            }
	        } else {
	            this.timeUntilLured = MathHelper.nextInt(this.random, 100, 600);
	            this.timeUntilLured -= this.lureSpeed * 100 * 2;
	        }

	    }

	    private boolean calculateOpenWater(BlockPos p_234603_1_) {
	        UltimateFishingBobberEntity.WaterType waterType = UltimateFishingBobberEntity.WaterType.INVALID;

	        for(int i = -1; i <= 2; ++i) {
	            UltimateFishingBobberEntity.WaterType openWaterType = this.getOpenWaterTypeForArea(p_234603_1_.offset(-2, i, -2), p_234603_1_.offset(2, i, 2));
	            switch(openWaterType) {
	                case INVALID:
	                    return false;
	                case ABOVE_WATER:
	                    if (waterType == UltimateFishingBobberEntity.WaterType.INVALID) {
	                        return false;
	                    }
	                    break;
	                case INSIDE_WATER:
	                    if (waterType == UltimateFishingBobberEntity.WaterType.ABOVE_WATER) {
	                        return false;
	                    }
	            }

	            waterType = openWaterType;
	        }

	        return true;
	    }
	    
	    private boolean calculateOpenLava(BlockPos p_234603_1_) {
	        UltimateFishingBobberEntity.LavaType lavaType = UltimateFishingBobberEntity.LavaType.INVALID;

	        for(int i = -1; i <= 2; ++i) {
	            UltimateFishingBobberEntity.LavaType openLavaType = this.getOpenLavaTypeForArea(p_234603_1_.offset(-2, i, -2), p_234603_1_.offset(2, i, 2));
	            switch(openLavaType) {
	                case INVALID:
	                    return false;
	                case ABOVE_LAVA:
	                    if (lavaType == UltimateFishingBobberEntity.LavaType.INVALID) {
	                        return false;
	                    }
	                    break;
	                case INSIDE_LAVA:
	                    if (lavaType == UltimateFishingBobberEntity.LavaType.ABOVE_LAVA) {
	                        return false;
	                    }
	            }

	            lavaType = openLavaType;
	        }

	        return true;
	    }

	    private UltimateFishingBobberEntity.WaterType getOpenWaterTypeForArea(BlockPos p_234602_1_, BlockPos p_234602_2_) {
	        return BlockPos.betweenClosedStream(p_234602_1_, p_234602_2_).map(this::getOpenWaterTypeForBlock).reduce((p_234601_0_, p_234601_1_) -> {
	            return p_234601_0_ == p_234601_1_ ? p_234601_0_ : UltimateFishingBobberEntity.WaterType.INVALID;
	        }).orElse(UltimateFishingBobberEntity.WaterType.INVALID);
	    }
	    
	    private UltimateFishingBobberEntity.LavaType getOpenLavaTypeForArea(BlockPos p_234602_1_, BlockPos p_234602_2_) {
	        return BlockPos.betweenClosedStream(p_234602_1_, p_234602_2_).map(this::getOpenLavaTypeForBlock).reduce((p_234601_0_, p_234601_1_) -> {
	            return p_234601_0_ == p_234601_1_ ? p_234601_0_ : UltimateFishingBobberEntity.LavaType.INVALID;
	        }).orElse(UltimateFishingBobberEntity.LavaType.INVALID);
	    }

	    private UltimateFishingBobberEntity.WaterType getOpenWaterTypeForBlock(BlockPos p_234604_1_) {
	        BlockState blockstate = this.level.getBlockState(p_234604_1_);
	        if (!blockstate.isAir() && !blockstate.is(Blocks.LILY_PAD)) {
	            FluidState fluidstate = blockstate.getFluidState();
	            return (fluidstate.is(FluidTags.WATER)) && fluidstate.isSource() && blockstate.getCollisionShape(this.level, p_234604_1_).isEmpty() ? UltimateFishingBobberEntity.WaterType.INSIDE_WATER : UltimateFishingBobberEntity.WaterType.INVALID;
	        } else {
	            return UltimateFishingBobberEntity.WaterType.ABOVE_WATER;
	        }
	    }
	    
	    private UltimateFishingBobberEntity.LavaType getOpenLavaTypeForBlock(BlockPos p_234604_1_) {
	        BlockState blockstate = this.level.getBlockState(p_234604_1_);
	        if (!blockstate.isAir() && !blockstate.is(Blocks.LILY_PAD)) {
	            FluidState fluidstate = blockstate.getFluidState();
	            return (fluidstate.is(FluidTags.LAVA)) && fluidstate.isSource() && blockstate.getCollisionShape(this.level, p_234604_1_).isEmpty() ? UltimateFishingBobberEntity.LavaType.INSIDE_LAVA : UltimateFishingBobberEntity.LavaType.INVALID;
	        } else {
	            return UltimateFishingBobberEntity.LavaType.ABOVE_LAVA;
	        }
	    }

	    private void checkCollision() {
	        RayTraceResult raytraceresult = ProjectileHelper.getHitResult(this, this::canHitEntity);
	        this.onHit(raytraceresult);
	    }

	    static enum State {
	        FLYING,
	        HOOKED_IN_ENTITY,
	        BOBBING;
	    }

	    static enum WaterType {
	        ABOVE_WATER,
	        INSIDE_WATER,
	        INVALID;
	    }
	    
	    static enum LavaType {
	    	ABOVE_LAVA,
	    	INSIDE_LAVA,
	    	INVALID;
	    }

	    @Override
	    public int retrieve(ItemStack p_146034_1_) {
	        PlayerEntity playerentity = this.getPlayerOwner();
	        if (!this.level.isClientSide && playerentity != null) {
	            int i = 0;
	            net.minecraftforge.event.entity.player.ItemFishedEvent event = null;
	            if (this.hookedIn != null) {
	                this.bringInHookedEntity();
	                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerentity, p_146034_1_, this, Collections.emptyList());
	                this.level.broadcastEntityEvent(this, (byte)31);
	                i = this.hookedIn instanceof ItemEntity ? 3 : 5;
	            } else if (this.nibble > 0) {
	                LootContext.Builder lootcontext$builder = (new LootContext.Builder((ServerWorld)this.level)).withParameter(LootParameters.ORIGIN, this.position()).withParameter(LootParameters.TOOL, p_146034_1_).withParameter(LootParameters.THIS_ENTITY, this).withRandom(this.random).withLuck((float)this.luck + playerentity.getLuck());
	                lootcontext$builder.withParameter(LootParameters.KILLER_ENTITY, this.getOwner()).withParameter(LootParameters.THIS_ENTITY, this);
	                LootTable loottable = this.level.getServer().getLootTables().get(LootTables.FISHING);
	                List<ItemStack> list = loottable.getRandomItems(lootcontext$builder.create(LootParameterSets.FISHING));
	                event = new net.minecraftforge.event.entity.player.ItemFishedEvent(list, this.onGround ? 2 : 1, this);

	                net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
	                if (event.isCanceled()) {
	                    this.remove();
	                    return event.getRodDamage();
	                }
	                CriteriaTriggers.FISHING_ROD_HOOKED.trigger((ServerPlayerEntity)playerentity, p_146034_1_, this, list);

	                for(ItemStack itemstack : list) {
	                    ItemEntity itementity = new ItemEntity(this.level, this.getX(), this.getY(), this.getZ(), itemstack);
	                    double d0 = playerentity.getX() - this.getX();
	                    double d1 = playerentity.getY() - this.getY();
	                    double d2 = playerentity.getZ() - this.getZ();
	                    double d3 = 0.1D;
	                    itementity.setDeltaMovement(d0 * 0.1D, d1 * 0.1D + Math.sqrt(Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2)) * 0.08D, d2 * 0.1D);
	                    this.level.addFreshEntity(itementity);
	                    playerentity.level.addFreshEntity(new ExperienceOrbEntity(playerentity.level, playerentity.getX(), playerentity.getY() + 0.5D, playerentity.getZ() + 0.5D, this.random.nextInt(6) + 1));
	                    if (itemstack.getItem().is(ItemTags.FISHES)) {
	                        playerentity.awardStat(Stats.FISH_CAUGHT, 1);
	                    }
	                }

	                i = 1;
	            }

	            if (this.onGround) {
	                i = 2;
	            }

	            this.remove();
	            return event == null ? i : event.getRodDamage();
	        } else {
	            return 0;
	        }
	    }

	    @Override
	    public void onSyncedDataUpdated(DataParameter<?> p_184206_1_) {
	        if (DATA_HOOKED_ENTITY.equals(p_184206_1_)) {
	            int i = this.getEntityData().get(DATA_HOOKED_ENTITY);
	            this.hookedIn = i > 0 ? this.level.getEntity(i - 1) : null;
	        }

	        if (DATA_BITING.equals(p_184206_1_)) {
	            this.biting = this.getEntityData().get(DATA_BITING);
	            if (this.biting) {
	                this.setDeltaMovement(this.getDeltaMovement().x, (double)(-0.4F * MathHelper.nextFloat(this.syncronizedRandom, 0.6F, 1.0F)), this.getDeltaMovement().z);
	            }
	        }

	        super.onSyncedDataUpdated(p_184206_1_);
	    }
}