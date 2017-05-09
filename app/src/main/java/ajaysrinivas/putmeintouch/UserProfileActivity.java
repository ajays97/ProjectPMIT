package ajaysrinivas.putmeintouch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.widget.ProfilePictureView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import ajaysrinivas.putmeintouch.adapter.FeedAdapter;

public class UserProfileActivity extends AppCompatActivity {

    private AdView mAdView;

    ArrayList<Post> postsList;
    ListView postList;
    FeedAdapter postAdapter = null;
    GraphResponse lastResponse = null;
    SwipeRefreshLayout swipeRefreshLayout;

    private CollapsingToolbarLayout collapsingToolbarLayout;
    ProfilePictureView pictureView;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.profile_toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-2819514375619003~3765371970");

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

        postList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int totalItemCount) {
                if (absListView.getLastVisiblePosition() == totalItemCount - 1) {
                    if (lastResponse != null) {
//                        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        ftView = inflater.inflate(R.layout.feed_update_dialog, null);
                        GraphRequest newRequest = lastResponse.getRequestForPagedResults(GraphResponse.PagingDirection.NEXT);
                        if (newRequest != null) {
                            newRequest.setCallback(new GraphRequest.Callback() {
                                @Override
                                public void onCompleted(GraphResponse graphResponse) {
                                    lastResponse = graphResponse;
                                    JSONObject mainObj = graphResponse.getJSONObject();
                                    try {
                                        JSONArray jsonArray = mainObj.getJSONArray("data");

                                        for (int i = 0; i < 15; i++) {
                                            JSONObject respObj = jsonArray.getJSONObject(i);
                                            JSONObject fromObj = respObj.getJSONObject("from");
                                            String photoUrl = fromObj.getString("id");
                                            if (fromObj.getString("id").equals(Profile.getCurrentProfile().getId()))
                                                postAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id"), fromObj.getString("id")));
                                        }
                                        postAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "NO MORE FEED", Toast.LENGTH_SHORT).show();
                                    }
//                                    feedList.removeFooterView(ftView);
                                }
                            });
//                            feedList.addFooterView(ftView);
                            newRequest.executeAsync();
                        }
                    }
                }
            }
        });

        postList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Post post = (Post) adapterView.getItemAtPosition(position);
                String post_id = post.post_id;
                Intent i = new Intent(UserProfileActivity.this, FeedActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, post_id);
                startActivity(i);

            }
        });

        updatefeed();

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

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
                        JSONObject respObj = jsonArray.getJSONObject(i);
                        JSONObject fromObj = respObj.getJSONObject("from");
                        String photoUrl = fromObj.getString("id");
                        if (fromObj.getString("id").equals(Profile.getCurrentProfile().getId()))
                            postAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id"), fromObj.getString("id")));
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
