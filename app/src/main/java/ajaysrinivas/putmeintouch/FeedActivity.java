package ajaysrinivas.putmeintouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class FeedActivity extends AppCompatActivity {

    TextView post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        post = (TextView) findViewById(R.id.tvPost);

        Intent intent = getIntent();
        String status = intent.getStringExtra(Intent.EXTRA_TEXT);
        post.setText(status);
        Toast.makeText(getApplicationContext(), status, Toast.LENGTH_SHORT).show();

    }
}
