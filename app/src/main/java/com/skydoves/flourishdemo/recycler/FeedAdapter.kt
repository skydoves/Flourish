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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.flourishdemo.R
import kotlinx.android.synthetic.main.item_feed.view.*

class FeedAdapter : RecyclerView.Adapter<FeedAdapter.FeedViewHolder>() {

  private val itemList: MutableList<FeedItem> = mutableListOf()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedViewHolder {
    val inflater = LayoutInflater.from(parent.context).inflate(R.layout.item_feed, parent, false)
    return FeedViewHolder(inflater.rootView)
  }

  fun addItem(feedItem: FeedItem) {
    itemList.add(feedItem)
    notifyItemInserted(itemList.indexOf(feedItem))
  }

  override fun onBindViewHolder(holder: FeedViewHolder, position: Int) {
    val data = itemList[position]
    holder.itemView.run {
      profile.setImageDrawable(data.profile)
      title.text = data.title
      content.text = data.content
    }
  }

  override fun getItemCount() = itemList.size

  class FeedViewHolder(view: View) : RecyclerView.ViewHolder(view)
}
