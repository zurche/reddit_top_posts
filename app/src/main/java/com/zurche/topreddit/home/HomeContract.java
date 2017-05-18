package com.zurche.topreddit.home;

import com.zurche.topreddit.home.service.model.ChildrenData;

import java.util.List;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */

public interface HomeContract {
    interface View{
        void onPopulatePostsList(List<ChildrenData> topPosts);
        void onFailedToretrievePosts();
        void setRefreshingView();
    }
    interface Presenter{
        void retrieveTopPosts();
    }
}
