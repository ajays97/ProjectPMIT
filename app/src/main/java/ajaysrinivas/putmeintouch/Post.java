package ajaysrinivas.putmeintouch;

/**
 * Created by Ajay Srinivas on 4/14/2017.
 */

public class Post {

    public String description;
    public String creator;
    public String post_id;

    public Post() {
        super();
    }

    public Post(String description, String creator, String post_id) {
        super();
        this.description = description;
        this.creator = creator;
        this.post_id = post_id;
    }
}
