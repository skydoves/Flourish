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

@file:Suppress("unused")

package com.skydoves.flourish

import android.animation.ValueAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.view.ViewCompat

class Flourish(private val builder: Builder) {

  private var isShowing = false
  private var isFlourishing = false
  lateinit var flourishView: ViewGroup

  init {
    createByBuilder()
  }

  private fun createByBuilder(): Flourish {
    with(builder) {
      val inflater = parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
      flourishView =
        (inflater.inflate(flourishLayout, null) as ViewGroup).apply {
          rotation = flourishOrientation.getRotation()
          visible(false)
          builder.parent.post {
            pivotX = flourishOrientation.getPivotX(builder.parent)
            pivotY = flourishOrientation.getPivotY(builder.parent)
          }
        }
      ViewCompat.setTranslationZ(flourishView, 1000f)
      parent.addView(flourishView,
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
      )
      if (flourishLayoutOnClickListener != null) {
        flourishView.setOnClickListener(flourishLayoutOnClickListener)
      } else {
        flourishView.setOnClickListener { }
      }
      if (isShowedOnStart) {
        parent.post { show() }
      }
    }
    return this
  }

  /**
   * This function is for supporting Java language.
   * shows the flourish layout with a polished animation.
   */
  fun show() = show { }

  /** shows the flourish layout with a polished animation. */
  fun show(doAfter: () -> Unit = { }) {
    if (!this.isShowing && !this.isFlourishing) {
      this.isShowing = true
      this.isFlourishing = true
      this.flourishView.visible(true)
      flourishing(1f, 0f, builder.flourishOrientation.getRotation()) {
        this.builder.flourishListener?.onChanged(true)
        doAfter()
      }
    }
  }

  /**
   * This function is for supporting Java language.
   * dismisses the flourish layout with a polished animation.
   */
  fun dismiss() = dismiss { }

  /** dismisses the flourish layout with a polished animation. */
  fun dismiss(doAfter: () -> Unit = { }) {
    if (this.isShowing && !this.isFlourishing) {
      this.isShowing = false
      this.isFlourishing = true
      flourishing(0f, 1f, builder.flourishOrientation.getRotation()) {
        this.builder.flourishListener?.onChanged(false)
        this.flourishView.visible(false)
        doAfter()
      }
    }
  }

  private fun flourishing(start: Float, end: Float, rotation: Float, doAfter: () -> Unit) {
    ValueAnimator.ofFloat(start, end).apply {
      duration = builder.duration
      applyInterpolator(builder.flourishAnimation)
      addUpdateListener {
        val value = it.animatedValue as Float
        flourishView.applyLayoutParams {
          flourishView.rotation = rotation * value
        }
      }
      doAfterFinishAnimate {
        doAfter()
        isFlourishing = false
      }
      start()
    }
  }

  /** returns flourish layout is showing or not. */
  fun isShowing() = this.isShowing

  /** Builder class for creating [Flourish]. */
  @FlourishDsl
  class Builder(val parent: ViewGroup) {

    @LayoutRes
    @JvmField
    var flourishLayout: Int = -1
    @JvmField
    var flourishOrientation: FlourishOrientation = FlourishOrientation.TOP_LEFT
    @JvmField
    var duration: Long = 800L
    @JvmField
    var flourishAnimation: FlourishAnimation = FlourishAnimation.NORMAL
    @JvmField
    var isShowedOnStart: Boolean = false
    @JvmField
    var flourishLayoutOnClickListener: View.OnClickListener? = null
    @JvmField
    var flourishListener: FlourishListener? = null

    /** sets the flourish layout for showing and dismissing on the parent layout. */
    fun setFlourishLayout(@LayoutRes value: Int) = apply { this.flourishLayout = value }

    /** sets the orientation of the starting point. */
    fun setFlourishOrientation(value: FlourishOrientation) = apply { this.flourishOrientation = value }

    /** sets the duration of the flourishing. */
    fun setDuration(value: Long) = apply { this.duration = value }

    /** sets the flourishing animation for showing and dismissing. */
    fun setFlourishAnimation(value: FlourishAnimation) = apply { this.flourishAnimation = value }

    /** sets the flourish layout should be showed on start. */
    fun setIsShowedOnStart(value: Boolean) = apply { this.isShowedOnStart = value }

    /** sets an onClickListener to the flourish layout. */
    fun setFlourishLayoutOnClickListener(value: View.OnClickListener) = apply { this.flourishLayoutOnClickListener = value }

    /** sets an onClickListener to the flourish layout using lambda. */
    fun setFlourishLayoutOnClickListener(block: () -> Unit) = apply {
      this.flourishLayoutOnClickListener = View.OnClickListener { block() }
    }

    /** sets a flourishListener for listening changes. */
    fun setFlourishListener(value: FlourishListener) = apply { this.flourishListener = value }

    /** sets a flourishListener for listening changes using lambda. */
    fun setFlourishListener(block: (isShowing: Boolean) -> Unit) = apply {
      this.flourishListener = object : FlourishListener {
        override fun onChanged(isShowing: Boolean) {
          block(isShowing)
        }
      }
    }

    /** creates an instance of the [Flourish]. */
    fun build() = Flourish(this)
  }
}
