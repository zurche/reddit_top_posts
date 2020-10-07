package com.zurche.topreddit.home.data.remote.service.model;

import java.util.List;

/**
 * @author alejandro.zurcher
 * @since {insert app current main version here}
 */

public class TopStoryData {
    private List<ChildrenData> children;
    private String after;
    private String before;

    public List<ChildrenData> getChildren() {
        return children;
    }

    public void setChildren(List<ChildrenData> children) {
        this.children = children;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
