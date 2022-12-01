package com.groccery.groceryshoppinglist;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class createList extends AppCompatActivity {

    EditText editText;
   Button materialButton;
   DatabaseHelper myDb;
    DatabaseReference mref;
    int maxID = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_list);

        editText = (EditText) findViewById(R.id.edittext1);
        materialButton = (Button) findViewById(R.id.submitbtn);
        myDb = DatabaseHelper.getInstance(createList.this);
        mref = FirebaseDatabase.getInstance().getReference();
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setNavigationBarColor(getResources().getColor(R.color.ic_launcher_background));
        }


        Cursor cursor3 = myDb.getMaxID();
        if (cursor3.moveToFirst()) {
            do {
                int data0 = cursor3.getInt(cursor3.getColumnIndex("MAX(ID)"));

                maxID = data0;

            } while (cursor3.moveToNext());
        }
        cursor3.close();

        materialButton.setOnClickListener(new View.OnClickListener() {
            private long mLastClickTime = 0;
            @Override
            public void onClick(View v) {
                if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
                    return;
                }
                mLastClickTime = SystemClock.elapsedRealtime();
                String v_title = editText.getText().toString().trim();
                v_title = v_title.replace("'", "");

                boolean bc = v_title.matches(".*[a-zA-Zа-яА-Я].*");
                if(bc == false && !myDb.titleExists(v_title) ) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Введите список...", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

                else if (bc == true && myDb.titleExists(v_title) == true) {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Такой список уже существует!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
                else {
                    Calendar calendar = Calendar.getInstance();
                    String date_created = calendar.getTime().toString();
                    calendar.set(Calendar.DAY_OF_WEEK,Calendar.SUNDAY);
                    calendar.set(Calendar.HOUR_OF_DAY,0);
                    calendar.set(Calendar.MINUTE,0);
                    calendar.set(Calendar.SECOND,0);
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)-6);
                    int k2 = calendar.get(Calendar.DAY_OF_MONTH);
                    String k3 = getMonthName_Abbr(calendar.get(Calendar.MONTH));
                    calendar.set(Calendar.DAY_OF_MONTH,calendar.get(Calendar.DAY_OF_MONTH)+6);
                    int yr = calendar.get(Calendar.YEAR);
                    String monthnameabbr = getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;

                    String s = k2 + " " + k3 + " - " + calendar.get(Calendar.DAY_OF_MONTH) + " " + getMonthName_Abbr(calendar.get(Calendar.MONTH)) + " " + yr;

                    Boolean a = myDb.insertData(v_title, getResources().getString(R.string.itemidentifier), 0, 0,0,s,monthnameabbr,yr,calendar.getTimeInMillis(),date_created);
                    if (a == true) {
                        startActivity(new Intent(createList.this, MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Данные не добавлены", Toast.LENGTH_LONG).show();

                    }
                }
            }
        });


    }
    private long mLastClickTime2 = 0;

    public void backbtn4(View v) {
        if (SystemClock.elapsedRealtime() - mLastClickTime2 < 1000){
            return;
        }
        mLastClickTime2 = SystemClock.elapsedRealtime();
        startActivity(new Intent(createList.this,MainActivity.class));
        overridePendingTransition(R.anim.slide_in_up, R.anim.slide_out_up);

    }
    public static String getMonthName_Abbr(int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, month);
        SimpleDateFormat month_date = new SimpleDateFormat("MMM");
        String month_name = month_date.format(cal.getTime());


        return month_name;
    }
}
