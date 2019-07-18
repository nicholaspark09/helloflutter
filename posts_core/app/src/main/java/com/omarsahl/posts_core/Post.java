package com.omarsahl.posts_core;

public class Post {

    public int id;
    public String title;
    public String body;

    public Post(int id, String title, String body) {
        this.id = id;
        this.title = title;
        this.body = body;
    }

    @Override
    public String toString() {
        return "Post{" +
            "id=" + id +
            ", title='" + title + '\'' +
            '}';
    }
}
