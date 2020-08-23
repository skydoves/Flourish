/*
 * Designed and developed by 2019 skydoves (Jaewoong Eum)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.skydoves.flourish

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.view.animation.LinearInterpolator
import android.view.animation.OvershootInterpolator

internal fun Animator.doAfterFinishAnimate(doAfterLift: () -> Unit) {
  this.addListener(object : AnimatorListenerAdapter() {
    override fun onAnimationEnd(animation: Animator?) {
      super.onAnimationEnd(animation)
      doAfterLift()
    }
  })
}

internal fun Animator.applyInterpolator(flourishAnimation: FlourishAnimation) {
  when (flourishAnimation) {
    FlourishAnimation.NORMAL -> this.interpolator = LinearInterpolator()
    FlourishAnimation.ACCELERATE -> this.interpolator = AccelerateInterpolator()
    FlourishAnimation.BOUNCE -> this.interpolator = BounceInterpolator()
    FlourishAnimation.OVERSHOOT -> this.interpolator = OvershootInterpolator()
  }
}
