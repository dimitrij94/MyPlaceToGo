package com.example.dmitrij.myplacetogo;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.dmitrij.myplacetogo.java_classes.Reciver;
import com.example.dmitrij.myplacetogo.java_classes.Sender;
import com.example.dmitrij.myplacetogo.json_objects.Place;
import com.example.dmitrij.myplacetogo.json_objects.PlaceMenu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class PlaceProphile extends ActionBarActivity {

    private RatingBar placeRating;
    private ListView placeMenuList;
    private ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_prophile);
        Place profile;

        PlaceMenu[]placeMenus=null;
        placeMenuList=(ListView)findViewById(R.id.place_menu_list);
        profile=(Place)getIntent().getParcelableExtra(Place.class.getCanonicalName());

        HttpURLConnection connection= null;
        try {
            connection = (HttpURLConnection)(new URL("127.0.0.1:8080\\MyPlaceToGo\\:android\\getPlace"+"?placeName"+profile.placeName)).openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setUseCaches(true);
            InputStream in = connection.getInputStream();
            if(in!=null){
                Reciver<PlaceMenu[]>menuReciver=new Reciver<PlaceMenu[]>();
                placeMenus=menuReciver.recive(in);
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        fillInterface(profile);
        filPlaceMenuList(placeMenus);
        placeRating.setOnRatingBarChangeListener(createListener());
        setImage(Long.valueOf(profile.placePhotos[0].url));
}

    public void filPlaceMenuList(PlaceMenu[] places){
        ListView placeMenuList=(ListView)findViewById(R.id.place_menu_list);
        String DESCRIPTION_PARAM_NAME = "1";
        String NAME_PARAM_NAME = "2";
        String PARAM_PRICE = "3";
        ArrayList<Map<String,Object>> MenuListData;

        final String[] FROM={DESCRIPTION_PARAM_NAME,NAME_PARAM_NAME,PARAM_PRICE};
        final int[] TO={R.id.menu_item_description,R.id.menu_item_name,R.id.menu_item_price};
        MenuListData = new ArrayList<Map<String,Object>>();
        Map m = new HashMap<String,Object>();
        for(int i=0;i<places.length;i++) {
            m.put(NAME_PARAM_NAME, places[i].menuName);
            m.put(DESCRIPTION_PARAM_NAME, places[i].menuDescription);
            m.put(PARAM_PRICE, places[i].price);
            MenuListData.add(m);
            SimpleAdapter simpleAdapter=new SimpleAdapter(this, MenuListData,R.layout.user_places_list_item,FROM,TO);
            placeMenuList.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    public RatingBar.OnRatingBarChangeListener createListener(){
        RatingBar.OnRatingBarChangeListener listener= new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                try {
                    Sender sender = new Sender();
                        HttpURLConnection con=establishConnection(new URL(getString(R.string.url_user_place_rating_changed)));
                        sender.send(con.getOutputStream(),String.valueOf(rating));
                        Reciver reciver = new Reciver();
                        placeRating.setRating(Float.parseFloat(reciver.reciveString(con.getInputStream())));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            };
        return listener;
        }

    public void fillInterface(Place p){
        TextView placeName = (TextView)findViewById(R.id.company_name);
        TextView placeDesc = (TextView)findViewById(R.id.company_description);
        TextView placeAdrress = (TextView)findViewById(R.id.company_address);
        TextView placeSpeciality = (TextView)findViewById(R.id.company_specialiti);

        placeRating = (RatingBar)findViewById(R.id.compani_ratings);
        placeName.setText(p.placeName);
        placeDesc.setText(p.placeDescrption);
        placeAdrress.setText(p.placeAdrress);
        placeSpeciality.setText(p.placeSpeciality);
        placeRating.setNumStars(p.placeRating);
    }

    public AdapterView.OnItemClickListener createListListener(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_serch_results, menu);
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

    public HttpURLConnection establishConnection(URL url){
        HttpURLConnection connection=null;
        try {
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public void setImage(long imageID){
        byte[]data=null;
        try {
            HttpURLConnection con = (HttpURLConnection)(new URL("127.0.0.1\\myPlaceToGo\\android\\getImage"+Long.valueOf(imageID)).openConnection());
            con.setDoInput(true);
            con.setRequestMethod("GET");
            Reciver reciver = new Reciver();
            data = reciver.reciveImage(con.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        image.setImageBitmap(BitmapFactory.decodeByteArray(data, 0, data.length));

    }

}
