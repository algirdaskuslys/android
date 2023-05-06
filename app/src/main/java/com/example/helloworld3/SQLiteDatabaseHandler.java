package com.example.helloworld3;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "weather_data";

    // Weather table name
    private static final String TABLE_WEATHER = "weathers";

    // Weather Table Columns names
    private static final String KEY_ID = "id";
    private static final String COUNTRY_NAME = "country_name";
    private static final String DEGREES = "degrees";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_COUNTRY_TABLE = "CREATE TABLE " + TABLE_WEATHER + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + COUNTRY_NAME + " TEXT,"
                + DEGREES + " REAL" + ")";
        db.execSQL(CREATE_COUNTRY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER);
        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new weather
    void addCountry(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, weather.getCountryName()); // Country Name
        values.put(DEGREES, weather.getDegrees()); // Weather

        // Inserting Row
        db.insert(TABLE_WEATHER, null, values);
        db.close(); // Closing database connection
    }

    // Getting All Weathers
    public List<Weather> getAllWeathers() {
        List<Weather> weatherList = new ArrayList<Weather>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_WEATHER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Weather weather = new Weather();
                weather.setId(Integer.parseInt(cursor.getString(0)));
                weather.setCountryName(cursor.getString(1));
                weather.setDegrees(cursor.getLong(2));
                // Adding country to list
                weatherList.add(weather);
            } while (cursor.moveToNext());
        }

        // return WEATHER list
        return weatherList;
    }

    // Updating single weather
    public int updateWeather(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COUNTRY_NAME, weather.getCountryName());
        values.put(DEGREES, weather.getDegrees());

        // updating row
        return db.update(TABLE_WEATHER, values, KEY_ID + " = ?",
                new String[]{String.valueOf(weather.getId())});
    }

    // Deleting single weather
    public void deleteWeather(Weather weather) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER, KEY_ID + " = ?",
                new String[]{String.valueOf(weather.getId())});
        db.close();
    }

    // Getting single country
    // NENAUDOJAMAS
    Weather getWeather(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_WEATHER, new String[]{KEY_ID,
                        COUNTRY_NAME, DEGREES}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Weather weather = new Weather(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getLong(2));

        // return weather
        return weather;
    }

    // Deleting all countries
    // NENAUDOJAMAS
    public void deleteAllCountries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEATHER, null, null);
        db.close();
    }

    // Getting countries Count
    // NENAUDOJAMAS
    public int getCountriesCount() {
        String countQuery = "SELECT  * FROM " + TABLE_WEATHER;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
}