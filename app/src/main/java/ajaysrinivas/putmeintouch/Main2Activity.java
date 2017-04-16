package ajaysrinivas.putmeintouch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class Main2Activity extends AppCompatActivity {

    FeedAdapter feedAdapter;
    ArrayList<Post> feed;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        listView = (ListView) findViewById(R.id.listview);

        feed = new ArrayList<Post>();
        feed.add(new Post("First Description", "Ajay Srinivas", "11111"));
        feed.add(new Post("Second Description", "Vijay", "22222"));
        feed.add(new Post("Third Description", "Abhishek", "33333"));

        feedAdapter = new FeedAdapter(getApplicationContext(), feed);
        listView.setAdapter(feedAdapter);

        feedAdapter.add(new Post("Fourth Description", "New Name", "44444"));
        feedAdapter.notifyDataSetChanged();

    }
}
