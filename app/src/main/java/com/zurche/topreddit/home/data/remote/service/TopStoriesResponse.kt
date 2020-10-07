package com.zurche.topreddit.home.data.remote.service

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
data class TopStoriesResponse(var data: TopStoryData)

data class TopStoryData (var children: List<ChildrenData>,
                         var after: String,
                         var before: String)

data class ChildrenData(var data: NewsData)

data class NewsData(var author: String,
                    var title: String,
                    var num_comments: Long,
                    var created: Long,
                    var thumbnail: String,
                    var url: String)