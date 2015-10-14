package com.borisbesemer.contactcard;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.ArrayList;

public class MainActivity extends Activity implements RandomUserAPIManager.OnRandomUserAvailable, AdapterView.OnItemClickListener, View.OnClickListener {

    ListView mPersonListView;
    PersonAdapter mPersonAdapter;
    ArrayList mPersonList = new ArrayList();
    private ArrayList<Person> persons = RandomPersonStore.getInstance().persons;

    private Button addOnePersonButton = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPersonListView = (ListView) findViewById(R.id.personListView);

        mPersonAdapter = new PersonAdapter(this, getLayoutInflater(), persons);
        mPersonListView.setAdapter(mPersonAdapter);

        mPersonAdapter.notifyDataSetChanged();

        mPersonListView.setOnItemClickListener(this);

        addOnePersonButton = (Button) findViewById(R.id.addPersonButton);
        addOnePersonButton.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long1
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(), DetailViewActivity.class);
        i.putExtra("position", position);

        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        RandomUserAPIManager getNewUser = new RandomUserAPIManager(this);
        String[] urls = new String[] { "https://randomuser.me/api/" };
        getNewUser.execute(urls);
    }

    @Override
    public void onRandomUserAvailable(Person person) {

        persons.add(person);
        mPersonAdapter.notifyDataSetChanged();
    }

}
