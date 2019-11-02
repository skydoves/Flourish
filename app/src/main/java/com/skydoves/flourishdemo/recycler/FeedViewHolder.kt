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

package com.skydoves.flourishdemo.recycler

import android.view.View
import com.skydoves.baserecyclerviewadapter.BaseViewHolder
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedViewHolder(view: View) : BaseViewHolder(view) {

  private lateinit var data: FeedItem

  override fun bindData(data: Any) {
    if (data is FeedItem) {
      this.data = data
      drawItemUi()
    }
  }

  private fun drawItemUi() {
    itemView.run {
      profile.setImageDrawable(data.profile)
      title.text = data.title
      content.text = data.content
    }
  }

  override fun onClick(p0: View?) = Unit
  override fun onLongClick(p0: View?) = false
}
