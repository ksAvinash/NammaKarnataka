package smartAmigos.com.nammakarnataka.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DatabaseHelper extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "nk_2017";

    private static final String TABLE_PLACES = "nk_places";
    private static final String TABLE_IMAGES = "nk_images";

    private static final String PLACE_ID = "id";
    private static final String PLACE_NAME = "name";
    private static final String PLACE_DESCRIPTION = "description";
    private static final String PLACE_DISTRICT = "district";
    private static final String PLACE_BESTSEASON = "bestSeason";
    private static final String PLACE_ADDITIONALINFO = "additionalInformation";
    private static final String PLACE_NEARBYPLACES = "nearByPlaces";
    private static final String PLACE_LATITUDE = "latitude";
    private static final String PLACE_LONGITUDE = "longitude";
    private static final String PLACE_CATEGORY = "category";

    private static final String IMAGE_URL = "image_url";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_place_table = "create table "+TABLE_PLACES+" ("+PLACE_ID+" integer primary key , "+PLACE_NAME+"  text, "+PLACE_DESCRIPTION+" text, "+PLACE_DISTRICT+" text, "+PLACE_BESTSEASON+" text, "+PLACE_ADDITIONALINFO+" text, " +
                PLACE_NEARBYPLACES+" text, "+PLACE_LATITUDE+" double, "+PLACE_LONGITUDE+" double, "+PLACE_CATEGORY+" text );";
        db.execSQL(create_place_table);

        String create_images_table = "create table "+TABLE_IMAGES+" ( "+PLACE_ID+" integer, "+IMAGE_URL+" text );";
        db.execSQL(create_images_table);

        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+TABLE_PLACES);
        db.execSQL("drop table if exists "+TABLE_IMAGES);
        onCreate(db);
    }




    public boolean insertIntoPlace(int id, String name, String description, String district, String bestseason, String additionalInfo, String nearbyPlaces, double latitude, double longitude, String category){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        contentValues.put(PLACE_NAME, name);
        contentValues.put(PLACE_DESCRIPTION, description);
        contentValues.put(PLACE_DISTRICT, district);
        contentValues.put(PLACE_BESTSEASON, bestseason);
        contentValues.put(PLACE_ADDITIONALINFO, additionalInfo);
        contentValues.put(PLACE_NEARBYPLACES, nearbyPlaces);
        contentValues.put(PLACE_LATITUDE, latitude);
        contentValues.put(PLACE_LONGITUDE, longitude);
        contentValues.put(PLACE_CATEGORY, category);

        db.insert(TABLE_PLACES, null, contentValues);
        return true;
    }


    public boolean insertIntoImages(int id, String image_url){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(PLACE_ID, id);
        contentValues.put(IMAGE_URL, image_url);

        db.insert(TABLE_IMAGES, null, contentValues);

        return true;
    }

    public Cursor getAllTemples(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'temple' ;",null);
    }
    public Cursor getAllBeaches(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'beach' ;",null);
    }
    public Cursor getAllHeritages(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'heritage' ;",null);
    }
    public Cursor getAllDams(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'dam' ;",null);
    }
    public Cursor getAllHillstations(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'hillstation' ;",null);
    }
    public Cursor getAllTrekkings(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'trekking' ;",null);
    }
    public Cursor getAllWaterfalls(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'waterfall' ;",null);
    }
    public Cursor getAllOtherPlaces(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_CATEGORY+" = 'other' ;",null);
    }




    public Cursor getAllImagesArrayByID(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_IMAGES+" where "+PLACE_ID+" = "+id+" ;",null);
    }


    public Cursor getPlaceByDistrict(String dist){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_DISTRICT+" = '"+dist+"' ;",null);
    }
    public Cursor getPlaceById(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_ID+" = "+id+";",null);
    }


    public Cursor getAllDistricts(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select distinct "+PLACE_DISTRICT+" from "+TABLE_PLACES+" order by "+PLACE_DISTRICT+" ;",null);
    }


    public Cursor getPlaceByString(String str){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("select * from "+TABLE_PLACES+" where "+PLACE_NAME+" like '%"+str+"%' ;",null);
    }
}
