package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlite.DAO.SQLiteHelper;
import com.example.sqlite.modal.Item;

import java.util.Calendar;

public class AddActivity extends AppCompatActivity  {
    public Spinner sp;
    private EditText eTitle,ePrice,eDate;
    private Button btUpdate,btCancel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        initView();
        eDate.setOnClickListener(view -> {
            final Calendar c =Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(AddActivity.this,(datePicker, y, m, d) -> {
                String date="";
                if (m>8&&d>9){
                    date=d+"/"+(m+1)+"/"+y;
                }else if(m<8&&d>9){
                    date=d+"/0"+(m+1)+"/"+y;
                }else if(m<8 && d<10){
                    date="0"+d+"/0"+(m+1)+"/"+y;
                }else{
                    date="0"+d+"/"+(m+1)+"/"+y;
                }
                eDate.setText(date);
            },year,month,day);
            dialog.show();
        });
        btCancel.setOnClickListener(view -> {
            finish();
        });
        btUpdate.setOnClickListener(view -> {
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = sp.getSelectedItem().toString();
            String d = eDate.getText().toString();
            if (!t.isEmpty()&&p.matches("\\d+")){
                Item item = new Item(t,c,p,d);
                SQLiteHelper db = new SQLiteHelper(this);
                db.addItem(item);
                finish();
            }
        });
    }

    private void initView() {
        sp=findViewById(R.id.spCategory);
        eTitle=findViewById(R.id.tvTitle);
        ePrice=findViewById(R.id.tvPrice);
        eDate=findViewById(R.id.tvDate);
        btUpdate=findViewById(R.id.btUpdate);
        btCancel=findViewById(R.id.btCancel);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
    }
}