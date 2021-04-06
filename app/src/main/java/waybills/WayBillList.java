package waybills;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import ru.pstu.vmmb.waybills.R;

public class WayBillList extends AppCompatActivity {
    MainActivity.DBHelper dbHelper;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_bill_list);

        listView = findViewById(R.id.WayBillList);
        dbHelper = new MainActivity.DBHelper(this);

        MainActivity.DBHelper dbHelper;
        dbHelper = new MainActivity.DBHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        db.beginTransaction();
        try{
            Cursor c = db.rawQuery("select * from waybill", null);
            String[] headers = new String[] {"number"};
            SimpleCursorAdapter userAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c, headers,
                    new int[] {android.R.id.text1}, 0);
            listView.setAdapter(userAdapter);
            db.setTransactionSuccessful();
        }
        finally {
            db.endTransaction();
        }
        Intent WayBillInfoIntent = new Intent(this, WayBillInfo.class);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                WayBillInfoIntent.putExtra("number", Long.toString(id));
                //System.out.println(position);
                //System.out.println(id);
                startActivity(WayBillInfoIntent);
            }
        });
        //c.close();
        //db.close();
    }


    class GetWayBillDataTask extends AsyncTask<Context, Void, Cursor> {
        //private final WayBillList context;
        //public GetWayBillDataTask(Context context){
        //    mContext = context;
        //}
        @Override
        protected Cursor doInBackground(Context... contexts) {
            //MainActivity.DBHelper dbHelper;
            //dbHelper = new MainActivity.DBHelper(context);
            //SQLiteDatabase db = dbHelper.getWritableDatabase();
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            Cursor c = db.rawQuery("select * from waybill", null);
            String[] headers = new String[] {"number"};
            //System.out.println(headers[0]);
            return c;
        }

        @Override
        protected void onPostExecute(Cursor cursor) {
            //super.onPostExecute(cursor);
            //SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(context, android.R.layout.simple_list_item_1, c, headers,
            //        new int[]{android.R.id.text1}, 0);
            //listView.setAdapter(simpleCursorAdapter);
        }
    }
}