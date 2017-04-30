package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

public class CommentActivity extends AppCompatActivity {

    EditText etComment;
    String id;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        etComment = (EditText) findViewById(R.id.editComment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        id = i.getStringExtra(Intent.EXTRA_TEXT);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabComment);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pd = new ProgressDialog(CommentActivity.this);
                pd.setMessage("Posting Touch... Please Wait!");
                pd.show();

                Bundle parameters = new Bundle();
                parameters.putString("message", etComment.getText().toString());
                GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/" + id + "/comments", parameters, HttpMethod.POST, new GraphRequest.Callback() {
                    @Override
                    public void onCompleted(GraphResponse response) {
                    }
                });

                request.executeAsync();
                pd.dismiss();
                Intent i = new Intent(CommentActivity.this, FeedActivity.class);
                i.putExtra(Intent.EXTRA_TEXT, id);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == android.R.id.home){
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(CommentActivity.this, FeedActivity.class);
        i.putExtra(Intent.EXTRA_TEXT, id);
        startActivity(i);
        finish();
    }
}
