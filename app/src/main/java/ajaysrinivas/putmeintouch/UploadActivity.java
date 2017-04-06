package ajaysrinivas.putmeintouch;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;

public class UploadActivity extends AppCompatActivity {

    FloatingActionButton submit;
    EditText message;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        submit = (FloatingActionButton) findViewById(R.id.fabSubmit);
        message = (EditText) findViewById(R.id.editText);
        progressDialog = new ProgressDialog(this);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (message.getText() == null)
                    Toast.makeText(getApplicationContext(), "Message Empty", Toast.LENGTH_SHORT).show();
                else {
                    new Uploader().execute(message.getText().toString());
                }
            }
        });
    }

    public class Uploader extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... params) {
            Bundle parameters = new Bundle();
            parameters.putString("message", params[0]);
            parameters.putString("access_token", AccessToken.getCurrentAccessToken().getToken());
            GraphRequest request = new GraphRequest(AccessToken.getCurrentAccessToken(), "/1426285350749606/feed", parameters, HttpMethod.POST, new GraphRequest.Callback() {
                @Override
                public void onCompleted(GraphResponse response) {
                    Toast.makeText(getApplicationContext(), response.toString(), Toast.LENGTH_LONG).show();
                }
            });
            request.executeAsync();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressDialog.dismiss();
            Toast.makeText(getApplicationContext(), "Successfully Posted", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(UploadActivity.this, MainActivity.class));
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setMessage("Sending data... Please wait.");
            progressDialog.show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
