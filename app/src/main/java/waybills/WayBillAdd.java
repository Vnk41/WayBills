package waybills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import ru.pstu.vmmb.waybills.R;

public class WayBillAdd extends AppCompatActivity implements View.OnClickListener{

    Button AddWayBill, AddDestinationPoint, AddWayBillInfo;
    EditText WayBillNumber, CheckPoint, Mileage, GasInTank, TotalMileage;
    MainActivity.DBHelper dbHelper;
    AddWayBillTask AddWBToDB;
    AddDestinationPointTask AddDPToDB;
    AddWayBillInfoTask AddWBIToDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_bill_add);

        WayBillNumber = findViewById(R.id.editTextListNumber);
        CheckPoint = findViewById(R.id.editTextCheckPoint);
        Mileage = findViewById(R.id.editTextMileage);
        GasInTank = findViewById(R.id.editTextGasInTank);
        TotalMileage = findViewById(R.id.editTextMileageForAllWay);

        AddWayBill = (Button) findViewById(R.id.buttonAddWayBill);
        AddWayBill.setOnClickListener( this);

        AddDestinationPoint = (Button) findViewById(R.id.buttonAddDestinationPoint);
        AddDestinationPoint.setOnClickListener( this);

        AddWayBillInfo = (Button) findViewById(R.id.buttonAddWayBillInfo);
        AddWayBillInfo.setOnClickListener(this);

        dbHelper = new MainActivity.DBHelper(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.buttonAddWayBill:
                AddWBToDB = new AddWayBillTask();
                AddWBToDB.execute(WayBillNumber.getText().toString());
                break;
            case R.id.buttonAddDestinationPoint:
                AddDPToDB = new AddDestinationPointTask();
                AddDPToDB.execute(WayBillNumber.getText().toString(), CheckPoint.getText().toString(), Mileage.getText().toString());
                break;
            case R.id.buttonAddWayBillInfo:
                AddWBIToDB = new AddWayBillInfoTask();
                AddWBIToDB.execute(WayBillNumber.getText().toString(), GasInTank.getText().toString(), TotalMileage.getText().toString());
            default:
                break;
        }
    }

    class AddWayBillTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... strings) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues cv = new ContentValues();
                cv.put("number", Integer.parseInt(strings[0]));
                db.insertOrThrow("waybill", null, cv);
                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
            db.close();
            /*SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("select * from waybill", null);
            int id = c.getColumnIndex("_id");
            int number = c.getColumnIndex("number");
            while (c.moveToNext()) {
                System.out.println(c.getInt(id));
                System.out.println(c.getInt(number));
            }*/
            return null;
        }
    }


    class AddDestinationPointTask extends AsyncTask<String, Void, Void> {
        String WayBillNumberParam;
        @Override
        protected Void doInBackground(String... strings) {
            SQLiteDatabase dbRead = dbHelper.getReadableDatabase();
            dbRead.beginTransaction();
            try {
                Cursor c = dbRead.rawQuery("select * from waybill where number = ?", new String[]{strings[0]});
                int id = c.getColumnIndex("_id");
                int number = c.getColumnIndex("number");
                while (c.moveToNext()) {
                    //System.out.println(c.getInt(id));
                    //System.out.println(c.getInt(number));
                    WayBillNumberParam = String.valueOf(c.getInt(id));
                }
                dbRead.setTransactionSuccessful();
            }
            finally {
                dbRead.endTransaction();
            }
            SQLiteDatabase dbWrite = dbHelper.getWritableDatabase();
            dbWrite.beginTransaction();
            try {
                ContentValues cv = new ContentValues();
                cv.put("waybill_id", WayBillNumberParam);
                cv.put("destination_point", strings[1]);
                cv.put("mileage", strings[2]);
                long rowID = dbWrite.insertOrThrow("destination", null, cv);
                System.out.println(rowID);
                dbWrite.setTransactionSuccessful();
            }
            finally {
                dbWrite.endTransaction();
            }

            /*SQLiteDatabase dbReadDest = dbHelper.getReadableDatabase();
            Cursor c3 = dbReadDest.rawQuery("select * from destination", null);
            int id2 = c3.getColumnIndex("_id");
            int waybill_id2 = c3.getColumnIndex("waybill_id");
            int destinationIndex = c3.getColumnIndex("destination");
            int mileage = c3.getColumnIndex("mileage");
            //int GasInTank = c3.getColumnIndex("gas_in_tank");
            while (c3.moveToNext()) {
                System.out.println("Dest_id " + c3.getInt(id2));
                System.out.println("WayB_id " + c3.getInt(waybill_id2));
                System.out.println("Mile " + c3.getInt(mileage));
                //System.out.println("Dest " + c3.getString(destinationIndex));
                //System.out.println(c3.getInt(GasInTank));
            }*/

            return null;
        }
    }

    class AddWayBillInfoTask extends AsyncTask<String, Void, Void> {
        String WayBillNumberParam;
        @Override
        protected Void doInBackground(String... strings) {
            SQLiteDatabase dbReadInfo = dbHelper.getReadableDatabase();
            dbReadInfo.beginTransaction();
            try{
                Cursor c = dbReadInfo.rawQuery("select * from waybill where number = ?", new String[] {strings[0]});
                int id = c.getColumnIndex("_id");
                int number = c.getColumnIndex("number");
                while (c.moveToNext()) {
                    //System.out.println(c.getInt(id));
                    //System.out.println(c.getInt(number));
                    WayBillNumberParam = String.valueOf(c.getInt(id));
                }
                dbReadInfo.setTransactionSuccessful();
                System.out.println(WayBillNumberParam);

            }
            finally {
                dbReadInfo.endTransaction();
            }
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues cv = new ContentValues();
                cv.put("mileage", strings[1]);
                cv.put("gas_in_tank", strings[2]);
                //Cursor c2 = db.rawQuery("update waybill set mileage = ?, gas_in_tank = ? where waybill_id = ?", new String[] {strings[2], strings[1], WayBillNumberParam});
                db.update("waybill", cv, "_id = ?", new String[] {WayBillNumberParam});
                db.setTransactionSuccessful();
            }
            finally {
                db.endTransaction();
            }
            return null;
        }
    }
}