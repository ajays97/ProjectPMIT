package ajaysrinivas.putmeintouch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    ArrayList<Post> postsList;
    ListView postList;
    FeedAdapter postAdapter = null;
    GraphResponse lastResponse = null;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    ProfilePictureView pictureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);
        pictureView = (ProfilePictureView) findViewById(R.id.pictureView);
        pictureView.setPresetSize(ProfilePictureView.CUSTOM);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(Profile.getCurrentProfile().getName());

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        dynamicToolbarColor();
        toolbarTextAppearance();

        pictureView.setProfileId(Profile.getCurrentProfile().getId());

        postList = (ListView) findViewById(R.id.postsList);
        postList.setNestedScrollingEnabled(true);

        updatefeed();

    }

    private void dynamicToolbarColor() {

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {

            @Override
            public void onGenerated(Palette palette) {
                collapsingToolbarLayout.setContentScrimColor(palette.getMutedColor(R.attr.colorPrimary));
                collapsingToolbarLayout.setStatusBarScrimColor(palette.getMutedColor(R.attr.colorPrimaryDark));
            }
        });
    }

    private void toolbarTextAppearance() {
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void updatefeed() {
        postsList = new ArrayList<Post>();
        postAdapter = new FeedAdapter(getApplicationContext(), postsList);
        postList.setAdapter(postAdapter);

        Bundle parameters = new Bundle();
        parameters.putString("limit", "15");
        parameters.putString("fields", "from,message,created_time,place");
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/pmitg/feed", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                lastResponse = response;
                JSONObject mainObj = response.getJSONObject();
                try {
                    JSONArray jsonArray = mainObj.getJSONArray("data");
                    for (int i = 0; i < 15; i++) {
                        Log.d("Output: ", mainObj.toString());
                        JSONObject respObj = jsonArray.getJSONObject(i);
                        JSONObject fromObj = respObj.getJSONObject("from");
                        if (fromObj.getString("name").equals(Profile.getCurrentProfile().getName()))
                            postAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id")));
                    }
                    postAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    Toast.makeText(getApplicationContext(), "Please connect to internet", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });
        request.setParameters(parameters);
        request.executeAsync();

    }

}
