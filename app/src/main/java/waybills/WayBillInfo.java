package waybills;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;

import ru.pstu.vmmb.waybills.R;

public class WayBillInfo extends AppCompatActivity {
    MainActivity.DBHelper dbHelper;
    TextView WayBillId, WayBillGasInTank, WayBillMileage;
    TableLayout TableLayout;
    String WayBillNumber;
    String WayBillGasInTankStr, WayBillMileageStr;
    RecyclerView RecyclerView;
    ArrayList Destination, Mileage;
    DestinationAdapter destinationAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_way_bill_info);
        String WayBillName = getIntent().getExtras().getString("number");
        //System.out.println(WayBillName);

        WayBillId = findViewById(R.id.textViewWayBillNumber);
        WayBillGasInTank = findViewById(R.id.textViewWayBillGasInTank);
        WayBillMileage = findViewById(R.id.textViewWayBillMileage);
        RecyclerView = findViewById(R.id.recyclerView);
        dbHelper = new MainActivity.DBHelper(this);

        Destination = new ArrayList<>();
        Mileage = new ArrayList<>();

        //System.out.println(WayBillName);
        SQLiteDatabase db2 = dbHelper.getReadableDatabase();
        Cursor c = db2.rawQuery("select * from waybill where _id = ?", new String[] {WayBillName});
        int id = c.getColumnIndex("_id");
        int number = c.getColumnIndex("number");
        int gas_in_tank = c.getColumnIndex("gas_in_tank");;
        int mileage = c.getColumnIndex("mileage");

        while (c.moveToNext()) {
            //System.out.println(c.getInt(id));
            //System.out.println(c.getInt(number));
            WayBillNumber = String.valueOf(c.getInt(number));
            WayBillGasInTankStr = String.valueOf((c.getInt(gas_in_tank)));
            WayBillMileageStr = String.valueOf(c.getInt(mileage));
            //WayBillId.setText(c.getInt(number));
        }
        WayBillId.setText(WayBillNumber);

        Cursor c2 = db2.rawQuery("select * from destination where waybill_id = ?", new String[] {WayBillName});
        while(c2.moveToNext()){
            //RecordCount++;
            Destination.add(c2.getString(2));
            Mileage.add(c2.getString(3));
        }
        destinationAdapter = new DestinationAdapter(WayBillInfo.this, Destination, Mileage);
        RecyclerView.setAdapter(destinationAdapter);
        RecyclerView.setLayoutManager(new LinearLayoutManager(WayBillInfo.this));

        if (gas_in_tank != 0){
            WayBillGasInTank.setText(WayBillGasInTankStr);
        }
        if (mileage != 0) {
            WayBillMileage.setText(WayBillMileageStr);
        }
    }
}
