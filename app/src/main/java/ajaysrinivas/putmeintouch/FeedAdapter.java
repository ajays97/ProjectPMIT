package ajaysrinivas.putmeintouch;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;

/**
 * Created by Ajay Srinivas on 4/14/2017.
 */

public class FeedAdapter extends ArrayAdapter<Post> {

    ArrayList<Post> feed = null;

    public FeedAdapter(@NonNull Context context, ArrayList<Post> feed) {
        super(context, R.layout.feed_row, feed);
        this.feed = feed;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater feedInflater = LayoutInflater.from(getContext());
        View customView = feedInflater.inflate(R.layout.feed_row, parent, false);

        Post post = feed.get(position);

        TextView feed_description = (TextView) customView.findViewById(R.id.tvDescription);
        TextView feed_creator = (TextView) customView.findViewById(R.id.tvcreator);
        TextView feed_postid = (TextView) customView.findViewById(R.id.tvpostid);

        feed_description.setText(post.description);
        feed_creator.setText(post.creator);
        feed_postid.setText(post.post_id);

        return customView;

    }
}
