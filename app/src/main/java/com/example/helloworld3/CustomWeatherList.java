package com.example.helloworld3;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomWeatherList extends BaseAdapter {
    private Activity context;
    ArrayList<Weather> weathers;
    private PopupWindow popupWindow;
    SQLiteDatabaseHandler db;

    public CustomWeatherList(Activity context, ArrayList<Weather> weathers, SQLiteDatabaseHandler db) {
        this.context = context;
        this.weathers = weathers;
        this.db = db;
    }

    public static class ViewHolder {
        TextView textViewId;
        TextView textViewCountry;
        TextView textViewDegrees;
        Button editButton;
        Button deleteButton;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);

            viewHolder.textViewId = (TextView) row.findViewById(R.id.textViewId);
            viewHolder.textViewCountry = (TextView) row.findViewById(R.id.textViewCountry);
            viewHolder.textViewDegrees = (TextView) row.findViewById(R.id.textViewDegrees);
            viewHolder.editButton = (Button) row.findViewById(R.id.edit);
            viewHolder.deleteButton = (Button) row.findViewById(R.id.delete);

            // store the holder with the view.
            row.setTag(viewHolder);
        } else {

            viewHolder = (ViewHolder) convertView.getTag();

        }

        viewHolder.textViewCountry.setText(weathers.get(position).getCountryName());
        viewHolder.textViewId.setText("" + weathers.get(position).getId());
        viewHolder.textViewDegrees.setText("" + weathers.get(position).getDegrees());
        final int positionPopup = position;

        viewHolder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Save: ", "" + positionPopup);
                editPopup(positionPopup);

            }
        });

        viewHolder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Last Index", "" + positionPopup);
                //     Integer index = (Integer) view.getTag();
                db.deleteWeather(weathers.get(positionPopup));

                //      countries.remove(index.intValue());
                weathers = (ArrayList) db.getAllWeathers();
                Log.d("Weathers size", "" + weathers.size());
                notifyDataSetChanged();
            }
        });
        return row;
    }

    public long getItemId(int position) {
        return position;
    }

    public Object getItem(int position) {
        return position;
    }

    public int getCount() {
        return weathers.size();
    }

    public void editPopup(final int positionPopup) {
        LayoutInflater inflater = context.getLayoutInflater();
        View layout = inflater.inflate(R.layout.edit_popup,
                (ViewGroup) context.findViewById(R.id.popup_element));
        popupWindow = new PopupWindow(layout, 600, 670, true);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

        final EditText countryEdit = (EditText) layout.findViewById(R.id.editTextCountry);
        final EditText weatherEdit = (EditText) layout.findViewById(R.id.editTextWeather);
        countryEdit.setText(weathers.get(positionPopup).getCountryName());
        weatherEdit.setText("" + weathers.get(positionPopup).getDegrees());
        Log.d("Degrees: ", "" + weathers.get(positionPopup).getDegrees());

        Button save = (Button) layout.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String countryStr = countryEdit.getText().toString();
                String population = weatherEdit.getText().toString();
                Weather weather = weathers.get(positionPopup);
                weather.setCountryName(countryStr);
                weather.setDegrees(Double.parseDouble(population));
                db.updateWeather(weather);
                weathers = (ArrayList) db.getAllWeathers();
                notifyDataSetChanged();

                for (Weather weather1 : weathers) {
                    String log = "Id: " + weather1.getId() + " ,Name: " + weather1.getCountryName() + " ,Degrees: " + weather1.getDegrees();
                    // Writing Countries to log
                    Log.d("Name: ", log);
                }

                popupWindow.dismiss();
            }
        });
    }
}
