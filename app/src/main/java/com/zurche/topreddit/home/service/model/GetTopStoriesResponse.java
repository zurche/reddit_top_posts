package com.zurche.topreddit.home.service.model;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */
public class GetTopStoriesResponse {
    private TopStoryData data;

    public TopStoryData getData() {
        return data;
    }

    public void setData(TopStoryData data) {
        this.data = data;
    }
}
