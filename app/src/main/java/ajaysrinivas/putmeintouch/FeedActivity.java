package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

import org.json.JSONException;
import org.json.JSONObject;

public class FeedActivity extends AppCompatActivity {

    TextView tPost;
    ProgressDialog pd;
    String post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        tPost = (TextView) findViewById(R.id.tvPost);
        pd = new ProgressDialog(this);

        Intent intent = getIntent();
        String status = intent.getStringExtra(Intent.EXTRA_TEXT);
        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

        pd.setMessage("Loading post... Please wait");
        pd.show();

        GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/"+status.trim(), null, HttpMethod.GET, new GraphRequest.Callback() {
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

    }

}
