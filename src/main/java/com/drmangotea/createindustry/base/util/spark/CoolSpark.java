package com.drmangotea.createindustry.base.util.spark;

import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire.BlueFireBlock;
import com.drmangotea.createindustry.items.weapons.explosives.thermite_grenades.fire.GreenFireBlock;
import com.drmangotea.createindustry.registry.TFMGEntityTypes;
import com.drmangotea.createindustry.registry.TFMGItems;
import com.drmangotea.createindustry.registry.TFMGMobEffects;
import com.simibubi.create.content.fluids.OpenEndedPipe;
import com.simibubi.create.content.trains.CubeParticleData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class CoolSpark extends ThrowableProjectile {
    public CoolSpark(EntityType<? extends CoolSpark> p_37391_, Level p_37392_) {
        super(p_37391_, p_37392_);
        
    }
    public CoolSpark(Level p_37399_, LivingEntity p_37400_) {
        super(TFMGEntityTypes.COOL_SPARK.get(), p_37400_, p_37399_);
    }
    
    public CoolSpark(Level p_37394_, double p_37395_, double p_37396_, double p_37397_) {
        super(TFMGEntityTypes.COOL_SPARK.get(), p_37395_, p_37396_, p_37397_, p_37394_);
    }
    
    @Override
    protected float getGravity(){
        return 0.02f;
    }
    @Override
    protected void defineSynchedData() {
        
    }
    
    public void tick(){
        super.tick();
        if (this.isInWaterOrRain()) {
            this.discard();
        }
        if(this.level.isClientSide) {
            
            CubeParticleData data = new CubeParticleData(191, 82, 91, 0.1f, 10, true);
            level.addParticle(data, this.getX(), this.getY(), this.getZ(), this.random.nextGaussian() * 0.05D, -this.getDeltaMovement().y * 0.5D, this.random.nextGaussian() * 0.05D);
            
        }
    }
    
    private ParticleOptions getParticle() {
        
        return ParticleTypes.SNOWFLAKE;
    }
    
    public void handleEntityEvent(byte p_37402_) {
        if (p_37402_ == 3) {
            ParticleOptions particleoptions = this.getParticle();
            
            for(int i = 0; i < 8; ++i) {
                this.level.addParticle(particleoptions, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
        
    }
    protected void onHitBlock(BlockHitResult hitResult) {
        super.onHitBlock(hitResult);
        if (!this.level.isClientSide) {
            Entity entity = this.getOwner();
            if (!(entity instanceof Mob) || net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.level, this)) {
                BlockPos blockpos = hitResult.getBlockPos().relative(hitResult.getDirection());
                if (this.level.getBlockState(blockpos).getBlock() == Blocks.SNOW) {
                    // Stack snow layers
                    final int layers = this.level.getBlockState(blockpos).getValue(BlockStateProperties.LAYERS);
                    if (layers < 5) {
                        level.setBlockAndUpdate(blockpos, this.level.getBlockState(blockpos).setValue(BlockStateProperties.LAYERS, 1 + layers));
                    }
                    
                } else if (this.level.isEmptyBlock(blockpos) && this.level.getBlockState(blockpos.below()).getBlock() != Blocks.SNOW) {
                    this.level.setBlockAndUpdate(blockpos, Blocks.SNOW.defaultBlockState());
                }
                dowseFire(this.level, blockpos);
            }
            
        }
    }
    
    private static void dowseFire(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.is(BlockTags.FIRE)) {
            level.removeBlock(pos, false);
        } else if (AbstractCandleBlock.isLit(state)) {
            AbstractCandleBlock.extinguish((Player)null, state, level, pos);
        } else if (CampfireBlock.isLitCampfire(state)) {
            level.levelEvent((Player)null, 1009, pos, 0);
            CampfireBlock.dowse((Entity)null, level, pos, state);
            level.setBlockAndUpdate(pos, (BlockState)state.setValue(CampfireBlock.LIT, false));
        }
        
    }
    
    protected void onHitEntity(EntityHitResult p_37386_) {
        super.onHitEntity(p_37386_);
        if (!this.level.isClientSide) {
            Entity entity = p_37386_.getEntity();
            Entity entity1 = this.getOwner();
            
            
            if(entity instanceof LivingEntity){
                
                ((LivingEntity)entity).addEffect(new MobEffectInstance(TFMGMobEffects.FROSTY.get(),400));
                
                
            }
            
            
        }
    }
    
    protected void onHit(HitResult p_37406_) {
        super.onHit(p_37406_);
        
        if (!this.level.isClientSide) {
            this.level.broadcastEntityEvent(this, (byte)3);
            
            
            //this.level.explode(this, this.getX(), this.getY(0.0625D), this.getZ(), 2.0F, Explosion.BlockInteraction.NONE);
            this.discard();
        }
        
    }
    
    @SuppressWarnings("unchecked")
    public static EntityType.Builder<?> build(EntityType.Builder<?> builder) {
        EntityType.Builder<CoolSpark> entityBuilder = (EntityType.Builder<CoolSpark>) builder;
        return entityBuilder.sized(.25f, .25f);
    }
}
