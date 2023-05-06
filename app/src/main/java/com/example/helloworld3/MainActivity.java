package com.example.helloworld3;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Weather> weathers;
    SQLiteDatabaseHandler db;
    Button btnSubmit;
    PopupWindow pwindo;
    Activity activity;
    ListView listView;
    CustomWeatherList customWeatherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        db = new SQLiteDatabaseHandler(this);

        listView = (ListView) findViewById(R.id.list);

        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPopUp();
            }
        });

        Log.d("MainActivity: ", "Before reading MainActivity");
        weathers = (ArrayList) db.getAllWeathers();

        for (Weather weather : weathers) {
            String log = "Id: " + weather.getId() + " ,Name: " + weather.getCountryName() + " ,Degrees: " + weather.getDegrees();
            // Writing Countries to log
            Log.d("Name: ", log);
        }


        CustomWeatherList customWeatherList = new CustomWeatherList(this, weathers, db);
        listView.setAdapter(customWeatherList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Toast.makeText(getApplicationContext(),
                        "You Selected " + weathers.get(position).getCountryName() + " as Country",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPopUp() {
        LayoutInflater inflater = activity.getLayoutInflater();
        View layout = inflater.inflate(R.layout.edit_popup,
                (ViewGroup) activity.findViewById(R.id.popup_element));
        pwindo = new PopupWindow(layout, 600, 670, true);
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText countryEdit = (EditText) layout.findViewById(R.id.editTextCountry);
        final EditText weatherEdit = (EditText) layout.findViewById(R.id.editTextWeather);

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = countryEdit.getText().toString();
                String weather = weatherEdit.getText().toString();
                Weather weather2 = new Weather(countryStr, Double.parseDouble(weather));
                db.addCountry(weather2);

                if (customWeatherList == null) {
                    customWeatherList = new CustomWeatherList(activity, weathers, db);
                    listView.setAdapter(customWeatherList);
                }
                customWeatherList.weathers = (ArrayList) db.getAllWeathers();

                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                for (Weather weather1 : weathers) {
                    String log = "Id: " + weather1.getId() + " ,Name: " + weather1.getCountryName() + " ,Degrees: " + weather1.getDegrees();
                    // Writing weathers to log
                    Log.d("Name: ", log);
                }
                pwindo.dismiss();
            }
        });
    }
}
