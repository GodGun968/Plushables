package com.seacroak.plushables.block.tile;

import com.seacroak.plushables.registry.uncommon.TileRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class OwlTileEntity extends PoweredBlockEntity {

  public OwlTileEntity(BlockPos pos, BlockState state) {
    super(TileRegistry.OWL_TILE, pos, state);
  }

  @Override
  protected <E extends BlockEntity & GeoAnimatable> PlayState idlePredicate(AnimationState<E> e) {
    if (!this.shouldAnimate) {
      e.getController().setAnimation(RawAnimation.begin().thenPlay("idle"));
      return PlayState.CONTINUE;
    } else {
      return PlayState.STOP;
    }
  }
}
