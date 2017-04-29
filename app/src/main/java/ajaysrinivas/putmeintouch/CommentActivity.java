package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                        pd.dismiss();
                    }
                });

                request.executeAsync();
                finish();
            }
        });

    }
}
