package ajaysrinivas.putmeintouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

public class Main2Activity extends AppCompatActivity {

    FeedAdapter adapter;
    Post[] feed;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = (ListView) findViewById(R.id.listview);

        feed = new Post[5];
        feed[0] = new Post("First Description", "Ajay Srinivas", "11111");
        feed[1] = new Post("Second Description", "Vijay", "22222");
        feed[2] = new Post("Third Description", "Abhishek", "33333");
        feed[3] = new Post("Fourth Description", "Ajay ", "44444");
        feed[4] = new Post("Fifth Description", "Mansoor", "55555");

        adapter = new FeedAdapter(getApplicationContext(), feed);

        listView.setAdapter(adapter);

    }
}
