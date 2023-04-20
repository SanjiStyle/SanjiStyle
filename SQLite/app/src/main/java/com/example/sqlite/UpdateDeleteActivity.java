package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.sqlite.DAO.SQLiteHelper;
import com.example.sqlite.modal.Item;

import java.util.Calendar;

public class UpdateDeleteActivity extends AppCompatActivity {
    public Spinner sp;
    private EditText eTitle,ePrice,eDate;
    private Button btUpdate,btBack,btRemove;
    private Item item;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_delete);
        initView();
        Intent intent = getIntent();
        item = (Item) intent.getSerializableExtra("item");
        eTitle.setText(item.getTitle());
        ePrice.setText(item.getPrice());
        eDate.setText(item.getDate());
        int s=0;
        for (int i=0;i<sp.getCount();i++){
            if (sp.getItemAtPosition(i).toString().equalsIgnoreCase(item.getCategory())){
               s=i;
                break;
            }
        }
        sp.setSelection(s);
        eDate.setOnClickListener(view -> {
            final Calendar c =Calendar.getInstance();
            int year=c.get(Calendar.YEAR);
            int month=c.get(Calendar.MONTH);
            int day=c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog dialog = new DatePickerDialog(UpdateDeleteActivity.this,(datePicker, y, m, d) -> {
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
        btUpdate.setOnClickListener(view -> {
            String t = eTitle.getText().toString();
            String p = ePrice.getText().toString();
            String c = sp.getSelectedItem().toString();
            String d = eDate.getText().toString();
            if (!t.isEmpty()&&p.matches("\\d+")){
                int id =item.getId();
                Item item = new Item(id,t,c,p,d);
                SQLiteHelper db = new SQLiteHelper(this);
                db.Update(item);
                finish();
            }
        });
        btBack.setOnClickListener(view -> {
            finish();
        });
        btRemove.setOnClickListener(view -> {
            int id = item.getId();
            AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
            builder.setTitle("Thong bao");
            builder.setMessage("BAN CO CHAC MUON XOA "+item.getTitle()+"khong?");
            builder.setIcon(R.drawable.remove);
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    SQLiteHelper bb = new SQLiteHelper(getApplicationContext());
                    bb.Delete(id);
                    finish();
                }
            });
            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog=builder.create();
            dialog.show();
        });
    }
    private void initView() {
        sp=findViewById(R.id.spCategory);
        eTitle=findViewById(R.id.tvTitle);
        ePrice=findViewById(R.id.tvPrice);
        eDate=findViewById(R.id.tvDate);
        btUpdate=findViewById(R.id.btUpdate);
        btBack=findViewById(R.id.btBack);
        btRemove=findViewById(R.id.btRemove);
        sp.setAdapter(new ArrayAdapter<String>(this,R.layout.item_spinner,getResources().getStringArray(R.array.category)));
    }
}