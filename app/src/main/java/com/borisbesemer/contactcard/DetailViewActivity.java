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
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class DetailViewActivity extends Activity {

    private ArrayList<Person> persons = RandomPersonStore.getInstance().persons;

    private Person chosenOne = null;
    ImageView imageView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        int position = getIntent().getIntExtra("position", 0);

        chosenOne = persons.get(position);

        TextView name = (TextView) findViewById(R.id.first_name);
        imageView = (ImageView) findViewById(R.id.user_image);

        imageView.setTag(chosenOne.imageUrl);

        new ImageDownloader().execute(new ImageTaskParams(imageView, imageView.getTag()));

        name.setText(chosenOne.getFullname());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail_view, menu);
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
        }

        return super.onOptionsItemSelected(item);
    }

    private class ImageDownloader extends AsyncTask<ImageTaskParams, Void, Bitmap> {

        ImageView imageView = null;

        @Override
        protected Bitmap doInBackground(ImageTaskParams... imageTaskParams) {
            this.imageView = imageTaskParams[0].imageView;
            return downloadImage(imageTaskParams[0].tag);
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }

        private Bitmap downloadImage(String url) {
            Bitmap bmp = null;
            try {
                URL aUrl = new URL(url);

                URLConnection conn = aUrl.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedInputStream bis = new BufferedInputStream(is);

                bmp = BitmapFactory.decodeStream(bis);
                bis.close();
                is.close();
            } catch (IOException e) {
                Log.e("Download", "Error getting images from server");
            }
            return bmp;
        }

    }

    private static class ImageTaskParams {
        ImageView imageView;
        String tag;

        ImageTaskParams(ImageView imageView, Object tag) {
            this.imageView = imageView;
            this.tag = (String) tag;
        }

    }
}
