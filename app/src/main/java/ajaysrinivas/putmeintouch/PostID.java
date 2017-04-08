package ajaysrinivas.putmeintouch;

/**
 * Created by Ajay Srinivas on 4/8/2017.
 */

public class PostID {

    String id, postid;

    public PostID() {

    }

    public PostID(String id, String postid) {
        this.id = id;
        this.postid = postid;
    }

    public String getPostid() {
        return this.postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getId() {
        return this.id;
    }

}
