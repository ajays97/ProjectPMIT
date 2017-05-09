package ajaysrinivas.putmeintouch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.ads.formats.NativeCustomTemplateAd;

import java.util.ArrayList;

import ajaysrinivas.putmeintouch.Post;
import ajaysrinivas.putmeintouch.R;

/**
 * Created by Ajay Srinivas on 4/14/2017.
 */

public class FeedAdapter extends ArrayAdapter<Post> {

    private static final int AD_VIEW_TYPE = 1;

    ArrayList<Post> feed = null;
    ProfilePictureView pictureView;

    final int MAX_MEMORY = (int) (Runtime.getRuntime().maxMemory() / 1024);
    final int CACHE_SIZE = MAX_MEMORY / 8;

    private LruCache<String, Bitmap> mCache;

    public FeedAdapter(@NonNull Context context, ArrayList<Post> feed) {
        super(context, R.layout.feed_row, feed);
        this.feed = feed;


        mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {

            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount();
            }
        };

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

        pictureView = (ProfilePictureView) customView.findViewById(R.id.profilePic);
        pictureView.setDrawingCacheEnabled(true);
        pictureView.setProfileId(post.photo_url);

        if (mCache == null) {
            mCache = new LruCache<String, Bitmap>(CACHE_SIZE) {
                @Override
                protected int sizeOf(String key, Bitmap value) {
                    return value.getByteCount();
                }
            };
        }

        BitmapDrawable pic = new BitmapDrawable(mCache.get(post.photo_url));

        if (pic == null) {
            pictureView.setProfileId(post.photo_url);
            new ImageCacher().execute(post.photo_url);
        } else
            pictureView.setBackground(pic);

        return customView;
    }

    public class ImageCacher extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String photo) {
            pictureView.setProfileId(photo);
            super.onPostExecute(photo);
        }

        @Override
        protected String doInBackground(String... strings) {

            String id = strings[0];
            Bitmap picture = null;

            picture = image();
            mCache.put(id, picture);

            return id;

        }
    }

    public Bitmap image() {
        return pictureView.getDrawingCache();
    }
}

