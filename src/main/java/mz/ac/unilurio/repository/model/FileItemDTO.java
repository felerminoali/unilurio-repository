package mz.ac.unilurio.repository.model;

import java.io.Serializable;

public class FileItemDTO implements Serializable {

        private String title;
        private String name;
        private String id;
        private String type;
        private String ThumbanilLink;
        private String action;


    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getThumbanilLink() {
        return ThumbanilLink;
    }

    public void setThumbanilLink(String thumbanilLink) {
        ThumbanilLink = thumbanilLink;
    }
}
