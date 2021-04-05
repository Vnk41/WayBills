package waybills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import ru.pstu.vmmb.waybills.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    Button WayBillList, WayBillListAdd, CurrentLocation;
    MyTask InitializeDB;
    DBHelper dbHelper;
    Intent LocationService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WayBillList = (Button) findViewById(R.id.checkAllWayBill);
        WayBillList.setOnClickListener( this);

        WayBillListAdd = (Button) findViewById(R.id.addWayBillButton);
        WayBillListAdd.setOnClickListener( this);

        CurrentLocation = (Button) findViewById(R.id.checkLocation);
        CurrentLocation.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == -1) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        InitializeDB = new MyTask();
        InitializeDB.execute();
        //startService(new Intent(this, LocationService.class));
    }


    @Override
    public void onClick(View v) {
        LocationService = new Intent(this, LocationService.class);
        startService(new Intent(this, LocationService.class));
        switch (v.getId()) {
            case R.id.checkAllWayBill:
                Intent checkAllWayBillIntent = new Intent(this, WayBillList.class);
                startActivity(checkAllWayBillIntent);
                break;
            case R.id.addWayBillButton:
                Intent AddWayBillintent = new Intent(this, WayBillAdd.class);
                startActivity(AddWayBillintent);
                //stopService(LocationService);
                break;
            case R.id.checkLocation:
                Intent checkLocationIntent = new Intent(this, CurrentLocation.class);
                startActivity(checkLocationIntent);
            default:
                break;
        }
    }


    class MyTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.execSQL("create table if not exists waybill ("
                    + "_id integer primary key autoincrement,"
                    + "number integer,"
                    + "gas_in_tank real,"
                    + "mileage real"
                    + ");");
            db.execSQL("create table if not exists destination ("
                    + "_id integer primary key autoincrement,"
                    + "waybill_id integer, "
                    + "destination_point string,"
                    + "mileage real"
                    + ");");
            return null;
        }
    }

    static class DBHelper extends SQLiteOpenHelper {
        public DBHelper(Context context) {
            super(context, "myDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}