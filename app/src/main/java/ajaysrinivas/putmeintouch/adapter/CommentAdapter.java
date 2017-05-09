package ajaysrinivas.putmeintouch.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import ajaysrinivas.putmeintouch.Post;
import ajaysrinivas.putmeintouch.R;

/**
 * Created by Ajay Srinivas on 4/30/2017.
 */

public class CommentAdapter extends ArrayAdapter<Post> {

    ArrayList<Post> comments = null;

    public CommentAdapter(@NonNull Context context, ArrayList<Post> comments) {
        super(context, R.layout.comment_row, comments);
        this.comments = comments;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater feedInflater = LayoutInflater.from(getContext());
        View customView = feedInflater.inflate(R.layout.comment_row, parent, false);

        Post comment = comments.get(position);

        TextView comment_description = (TextView) customView.findViewById(R.id.comDescription);
        TextView comment_creator = (TextView) customView.findViewById(R.id.comCreator);
//        TextView comment_id = (TextView) customView.findViewById(R.id.compostid);

        comment_description.setText(comment.description);
        comment_creator.setText(comment.creator);

        return customView;
    }
}
