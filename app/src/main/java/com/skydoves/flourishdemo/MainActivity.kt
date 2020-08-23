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

package com.skydoves.flourishdemo

import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.skydoves.flourish.Flourish
import com.skydoves.flourish.FlourishAnimation
import com.skydoves.flourish.FlourishOrientation
import com.skydoves.flourish.createFlourish
import com.skydoves.flourishdemo.recycler.FeedAdapter
import com.skydoves.flourishdemo.recycler.FeedItem
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.recyclerView
import kotlinx.android.synthetic.main.layout_flourish_main.tabLayout
import kotlinx.android.synthetic.main.layout_flourish_main.view.recyclerView
import kotlinx.android.synthetic.main.toolbar_custom_back.view.*
import kotlinx.android.synthetic.main.toolbar_custom_back.view.toolbar_more
import kotlinx.android.synthetic.main.toolbar_custom_profile.*
import kotlinx.android.synthetic.main.toolbar_custom_profile.toolbar_more

class MainActivity : AppCompatActivity() {

  private val adapter by lazy { FeedAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    val flourish: Flourish<LinearLayout> = createFlourish(parentLayout) {
      setFlourishLayout(R.layout.layout_flourish_main)
      setFlourishAnimation(FlourishAnimation.BOUNCE)
      setFlourishOrientation(FlourishOrientation.TOP_LEFT)
      setShowOnStart(true)
    }

    flourish.flourishView.toolbar_title.text = "Profile"
    flourish.flourishView.toolbar_more.setOnClickListener {
      flourish.dismiss { Toast.makeText(this, "dismissed", Toast.LENGTH_SHORT).show() }
    }
    flourish.flourishView.recyclerView.adapter = adapter

    toolbar_title.text = "Timeline"
    toolbar_more.setOnClickListener {
      flourish.show { Toast.makeText(this, "showed", Toast.LENGTH_SHORT).show() }
    }

    tabLayout.addTab(tabLayout.newTab().setText("Timeline"))
    tabLayout.addTab(tabLayout.newTab().setText("Contents"))

    recyclerView.adapter = adapter
    adapter.addItem(FeedItem(ContextCompat.getDrawable(this, R.drawable.lovebird), "skydoves",
      getString(R.string.lesson3)))
    adapter.addItem(
      FeedItem(ContextCompat.getDrawable(this, R.drawable.profile2), "The Little Prince",
        getString(R.string.lesson4)))
    adapter.addItem(FeedItem(ContextCompat.getDrawable(this, R.drawable.profile1), "Night night",
      getString(R.string.lesson5)))
  }
}
