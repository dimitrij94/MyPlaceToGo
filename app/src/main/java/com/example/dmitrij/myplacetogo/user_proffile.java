package com.example.dmitrij.myplacetogo;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dmitrij.myplacetogo.java_classes.Reciver;
import com.example.dmitrij.myplacetogo.java_classes.Sender;
import com.example.dmitrij.myplacetogo.json_objects.Client;
import com.example.dmitrij.myplacetogo.json_objects.Place;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class user_proffile extends ActionBarActivity {

    private Client client;

    private ArrayList<Map<String,Object>> AdapterListData;
    private Place[]searchResoults=null;

    private final int CM_DELETE_ID =1;
    private final String encode = "UTF-8";

    private EditText  searchField;
    private ImageButton search;
    private ListView listView;
    private SimpleAdapter simpleAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_proffile);
        AdapterListData = new ArrayList<>();
        client = getIntent().getParcelableExtra(Client.class.getCanonicalName());
        fillInterface(client);
        listView.setOnItemClickListener(createListItemClickListener());
        search.setOnClickListener(createSearchListener());

    }

    public View.OnClickListener createSearchListener(){
        return new View.OnClickListener() {
            Sender sender;
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.search_button:
                        search(searchField.getText().toString());
                        if(searchResoults!=null){
                            fillUserPlacesList(searchResoults);
                        }
                }
            }
        };
    }

    public Place[]search(String name){
        Sender sender=new Sender();
        HttpURLConnection con=null;
        try {
            String urlParams = URLEncoder.encode("place_name", encode)+
                    URLEncoder.encode(searchField.getText().toString(),encode);
            con = (HttpURLConnection)(new URL("\"127.0.0.1:8080\\MyPlaceToGo\\:android\\getPlace").openConnection());
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setUseCaches(true);
            sender.send(con.getOutputStream(), urlParams);
            Reciver<Place[]> reciver=new Reciver<Place[]>();
            return reciver.recive(con.getInputStream());
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        finally{
            con.disconnect();
        }

    }

    public Client getClient(){
        return client;
    }

    public AdapterView.OnItemClickListener createListItemClickListener(){

        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                {
                    Intent intent=new Intent(getBaseContext(),PlaceProphile.class);
                    if(searchResoults!=null) {
                        intent.putExtra(Place.class.getCanonicalName(), getClient().clientPlaces[position]);
                    }else{
                        intent.putExtra(Place.class.getCanonicalName(),searchResoults[position]);
                    }
                    startActivity(intent);
                }
            }
        };

    }

    void fillInterface(Client client){
        String[] sex={"Мужской","Женский"};
        listView=(ListView)findViewById(R.id.user_places_listView);
        TextView userNameView=(TextView)findViewById(R.id.user_name);
        TextView userSurnameView=(TextView)findViewById(R.id.user_surname);
        TextView userAddressView =(TextView)findViewById(R.id.user_address);
        TextView userEmailView=userEmailView =(TextView)findViewById(R.id.user_email);
        ImageView userPhotoView=(ImageView)findViewById(R.id.user_imageView);
        searchField = (EditText)findViewById(R.id.search_field);
        search =(ImageButton)findViewById(R.id.search_button);

        userNameView.setText(client.clientName);
        userSurnameView.setText(client.clientSurname);
        userAddressView.setText(client.clientAddress);
        userEmailView.setText(client.clientEmail);

        fillUserPlacesList(client.clientPlaces);
        byte[]imageBody=client.clientPhotos[0].getBody();
        userPhotoView.setImageBitmap(BitmapFactory.decodeByteArray(imageBody,0,imageBody.length));

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,ContextMenu.ContextMenuInfo menuInfo){
        super.onCreateContextMenu(menu,v, menuInfo);
        menu.add(0,CM_DELETE_ID,0,"Удалить из избраного");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getItemId()==CM_DELETE_ID) {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
            if(deletePlace(client.clientPlaces[info.position].placeId)){
                Toast toast = Toast.makeText(this,"Place has bean deleted",Toast.LENGTH_SHORT);
            }

            AdapterListData.remove(info.position);
            simpleAdapter.notifyDataSetChanged();
        }
        return super.onContextItemSelected(item);
    }



    public boolean deletePlace(long placeId){
        HttpURLConnection connection = null;
        try {
        connection= (HttpURLConnection) (new URL(getString(R.string.url_user_del_place))).openConnection();
            connection.setRequestMethod("DELETE");
            connection.setDoInput(true);

        Reciver reciver = new Reciver();
        Sender sender = new Sender();
            sender.send(connection.getOutputStream(),"place_id"+URLEncoder.encode(String.valueOf(placeId),encode));
            if(reciver.reciveString(connection.getInputStream())=="ok"){
                return true;
            }
            else return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void fillUserPlacesList(Place[] places){
        final String DESCRIPTION_PARAM_NAME="description";
        final String NAME_PARAM_NAME="name";
        final String RATING_PARAM_NAME="rating";
        final String[] FROM={DESCRIPTION_PARAM_NAME,NAME_PARAM_NAME,RATING_PARAM_NAME};
        final int[] TO={R.id.PlaceDescription,R.id.PlaceName,R.id.PlaceRating};

        Map m = new HashMap<String,Object>();
        for(int i=0;i<places.length;i++) {
            m.put(NAME_PARAM_NAME, places[i].placeName);
            m.put(DESCRIPTION_PARAM_NAME, places[i].placeDescrption);
            m.put(RATING_PARAM_NAME, places[i].placeRating);
            AdapterListData.add(m);
            simpleAdapter=new SimpleAdapter(this, AdapterListData,R.layout.user_places_list_item,FROM,TO);
            listView.setAdapter(simpleAdapter);
            simpleAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_proffile, menu);
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

}
