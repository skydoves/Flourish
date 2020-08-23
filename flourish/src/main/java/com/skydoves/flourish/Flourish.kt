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

@file:Suppress("unused", "MemberVisibilityCanBePrivate")

package com.skydoves.flourish

import android.animation.ValueAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.annotation.LayoutRes

/**
 * Flourish implements dynamic ways to show up and dismiss layouts with animations.
 */
class Flourish<T : ViewGroup>(private val builder: Builder<T>) {

  var isShowing = false
    private set

  var isFlourishing = false
    private set

  lateinit var flourishView: T

  init {
    createByBuilder()
  }

  private fun createByBuilder(): Flourish<T> {
    with(builder) {
      this@Flourish.flourishView = requireNotNull(builder.flourishLayout) {
        "FlourishLayout must be required."
      }.apply {
        bringToFront()
        visible(false)
        flourishLayoutOnClickListener?.let { setOnClickListener(it) }
      }

      with(parentLayout) {
        addView(flourishView,
          ViewGroup.LayoutParams.MATCH_PARENT,
          ViewGroup.LayoutParams.MATCH_PARENT
        )
        post {
          flourishView.pivotX = flourishOrientation.getPivotX(this)
          flourishView.pivotY = flourishOrientation.getPivotY(this)
          flourishView.rotation = flourishOrientation.getRotation()
          if (showOnStart) {
            show()
          }
        }
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
    if (!isShowing && !isFlourishing) {
      this.isShowing = true
      this.isFlourishing = true
      this.flourishView.visible(true)
      flourishing(1f, 0f, builder.flourishOrientation.getRotation()) {
        builder.flourishListener?.onChanged(true)
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
    if (isShowing && !isFlourishing) {
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
    flourishView.post {
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
          isFlourishing = false
          doAfter()
        }
        start()
      }
    }
  }

  /** Builder class for creating [Flourish]. */
  @FlourishDsl
  class Builder<T : ViewGroup>(val parentLayout: T) {

    @JvmField
    var flourishLayout: T? = null

    @JvmField
    var flourishOrientation: FlourishOrientation = FlourishOrientation.TOP_LEFT

    @JvmField
    var duration: Long = 500L

    @JvmField
    var flourishAnimation: FlourishAnimation = FlourishAnimation.NORMAL

    @JvmField
    var showOnStart: Boolean = false

    @JvmField
    var flourishLayoutOnClickListener: View.OnClickListener? = null

    @JvmField
    var flourishListener: FlourishListener? = null

    init {
      require(parentLayout !is LinearLayout) {
        "parent layout should not be a LinearLayout."
      }
    }

    /** sets the flourish layout for showing and dismissing on the parent layout. */
    @Suppress("UNCHECKED_CAST")
    fun setFlourishLayout(@LayoutRes value: Int) = apply {
      this.flourishLayout = LayoutInflater.from(parentLayout.context).inflate(value, null) as T
    }

    /** sets the flourish view for showing and dismissing on the parent layout. */
    fun setFlourishLayout(value: T) = apply { this.flourishLayout = value }

    /** sets the orientation of the starting point. */
    fun setFlourishOrientation(value: FlourishOrientation) = apply { this.flourishOrientation = value }

    /** sets the duration of the flourishing. */
    fun setDuration(value: Long) = apply { this.duration = value }

    /** sets the flourishing animation for showing and dismissing. */
    fun setFlourishAnimation(value: FlourishAnimation) = apply { this.flourishAnimation = value }

    /** sets the flourish layout should be showed on start. */
    fun setShowOnStart(value: Boolean) = apply { this.showOnStart = value }

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
