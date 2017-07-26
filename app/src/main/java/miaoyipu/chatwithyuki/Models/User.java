package miaoyipu.chatwithyuki.Models;

/**
 * Created by cy804 on 2017-07-26.
 */

public class User {
    private String id, name;

    public User() {};

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User(String id, String name) {
        this.id = id;
        this.name = name;

    }
}
