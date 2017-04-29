package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FeedActivity extends AppCompatActivity {

    TextView tPost;
    ProgressDialog pd;
    String post, status;

    ArrayList<Post> commentsList;
    ListView commentList;
    FeedAdapter commentAdapter;

    GraphResponse lastResponse = null;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        tPost = (TextView) findViewById(R.id.feed_comment);
        pd = new ProgressDialog(this);


        commentList = (ListView) findViewById(R.id.commentslist);

        Intent intent = getIntent();
        status = intent.getStringExtra(Intent.EXTRA_TEXT);

        commentList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (absListView.getLastVisiblePosition() == totalItemCount - 1) {
                    if (lastResponse != null) {
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
                                            commentAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id")));
                                        }
                                        commentAdapter.notifyDataSetChanged();
                                    } catch (JSONException e) {
                                        Toast.makeText(getApplicationContext(), "NO MORE FEED", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            newRequest.executeAsync();
                        }
                    }
                }
            }
        });

        pd.setMessage("Loading post... Please wait");
        pd.show();

        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + status.trim(), null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject jsonObject = response.getJSONObject();
                try {
                    post = jsonObject.getString("message");
                    tPost.setText(post);
                    pd.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        request.executeAsync();

        updateComments();

    }


    public void updateComments() {

        commentsList = new ArrayList<Post>();
        commentAdapter = new FeedAdapter(getApplicationContext(), commentsList);
        commentList.setAdapter(commentAdapter);

        Bundle parameters = new Bundle();
        parameters.putString("limit", "10");

        GraphRequest graphRequest = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + status + "/comments", null, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                JSONObject jsonObject = response.getJSONObject();
                try {
                    JSONArray commArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < 10; i++) {
                        JSONObject respObj = commArray.getJSONObject(i);
                        JSONObject fromObj = respObj.getJSONObject("from");
                        commentAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id")));
                    }
                    commentAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("Output", jsonObject.toString());
            }
        });

        graphRequest.executeAsync();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feed_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.add_comment) {
            Intent i = new Intent(FeedActivity.this, CommentActivity.class);
            i.putExtra(Intent.EXTRA_TEXT, status);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}
