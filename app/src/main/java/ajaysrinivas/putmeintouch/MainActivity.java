package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CustomTabActivity;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//FB Group ID = 203153109726651

//PMITG Page ID = 1426285350749606
//PMITG Group ID = 723621821179730

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ConnectivityReceiver.ConnectivityReceiverListener {

    FeedAdapter feedAdapter;
    ArrayList<Post> feed;

    ListView feedList;
    String feeds = "";
    String resp = null;
    ArrayList<Post> feedsList;
    GraphResponse lastResponse = null;
    View ftView;
    SwipeRefreshLayout refreshLayout;
    SwipeRefreshLayout.OnRefreshListener refreshListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
/*

        PostIdHelper db = new PostIdHelper(this);
        getApplicationContext().deleteDatabase("postIdManager.db");

        Log.d("Insert: ", "Inserting");
        db.addId(new PostID(1, "Ajay S"));


        Log.d("Read: ", "Reading");
        Log.d("Value: ",db.getPostId(1));

        ftView = (View) findViewById(R.id.loadingBar);
*/

        feedList = (ListView) findViewById(R.id.feedList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, UploadActivity.class));
                finish();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refreshLayout);
        refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedsList.clear();
                lastResponse = null;
                updateFeed();
                refreshLayout.setRefreshing(false);
            }
        };

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                feedsList.clear();
                lastResponse = null;
                updateFeed();
                refreshLayout.setRefreshing(false);
            }
        });

        feedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

                Post post = (Post) adapterView.getItemAtPosition(position);


                String post_id = post.post_id;
//                StringBuilder builder = new StringBuilder();
//                builder.append(temp);
//                builder.reverse();
//                temp = builder.toString().split("\n")[0];
//                builder = new StringBuilder();
//                builder.append(temp);
//                temp = builder.reverse().toString();

                Intent i = new Intent(MainActivity.this, FeedActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, post_id);
                startActivity(i);
            }
        });

//        Bundle parameters = new Bundle();
//        parameters.putString("message", "First from PMIT App and test App app and yureka");
//        parameters.putString("access_token", AccessToken.getCurrentAccessToken().getToken());
//        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/1426285350749606/feed", parameters, HttpMethod.POST, new GraphRequest.Callback() {
//            @Override
//            public void onCompleted(GraphResponse response) {
//                Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
//            }
//        });
//        request.executeAsync();

        feedList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

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
                                            feedAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id")));
                                        }
                                        feedAdapter.notifyDataSetChanged();
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


        updateFeed();

    }

    public void updateFeed() {
        feedsList = new ArrayList<Post>();
        feedAdapter = new FeedAdapter(getApplicationContext(), feedsList);
        feedList.setAdapter(feedAdapter);

        Bundle parameters = new Bundle();
        parameters.putString("limit", "15");
        parameters.putString("fields", "from,message,created_time,place");
        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/1426285350749606/feed", parameters, HttpMethod.GET, new GraphRequest.Callback() {
            @Override
            public void onCompleted(GraphResponse response) {
                lastResponse = response;
                JSONObject mainObj = response.getJSONObject();
                try {
                    JSONArray jsonArray = mainObj.getJSONArray("data");
                    for (int i = 0; i < 15; i++) {
                        JSONObject respObj = jsonArray.getJSONObject(i);
                        JSONObject fromObj = respObj.getJSONObject("from");
                        feedAdapter.add(new Post(respObj.getString("message"), fromObj.getString("name"), respObj.getString("id")));
                    }
                    feedAdapter.notifyDataSetChanged();
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

    private void showConnectivity(boolean isConnected) {
        String networkStatus;
        int color;
        if (isConnected) {
            networkStatus = "Connected to internet.";
            color = Color.WHITE;
        } else {
            networkStatus = "Please connect to internet.";
            color = Color.RED;
        }

        Snackbar snackbar = Snackbar.make(findViewById(R.id.fab), networkStatus, Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();
        TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(color);
        snackbar.show();

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showConnectivity(isConnected);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (id == R.id.mLogout) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.mRefresh) {
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Refreshing Feed...");
            progressDialog.show();
            feedsList.clear();
            lastResponse = null;
            updateFeed();
            progressDialog.dismiss();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        } else if (id == R.id.nav_logout) {
            LoginManager.getInstance().logOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(MainActivity.this, UserProfileActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshListener.onRefresh();
            }
        });

        ApplicationConnectivity.getmInstance().setConnectivityListener(this);
    }
}
