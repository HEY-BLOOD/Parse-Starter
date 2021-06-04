package com.example.parsestarter;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private View popupInputDialogView;
    private RecyclerView recyclerView;
    private String objectId;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new ProgressDialog(MainActivity.this);

        Button saveData = findViewById(R.id.saveData);
        Button readData = findViewById(R.id.readData);
        Button updateData = findViewById(R.id.updateData);

        saveData.setOnClickListener(saveDataView -> {
            try {
                saveDataTypes();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });

        readData.setOnClickListener(readDataView -> readObjects());

        updateData.setOnClickListener(updateDataView -> updateObject());

    }

    private void saveDataTypes() throws JSONException {
        ParseObject parseObject = new ParseObject("DataTypes");

        parseObject.put("stringField", "String");
        parseObject.put("doubleField", 1.5);
        parseObject.put("intField", 2);
        parseObject.put("boolField", true);
        parseObject.put("dateField", Calendar.getInstance().getTime());

        JSONObject myObject = new JSONObject();
        myObject.put("number", 1);
        myObject.put("string", "42");

        parseObject.put("jsonObject", myObject);


        JSONArray myArray = new JSONArray();
        myArray.put(myObject);
        myArray.put(myObject);
        myArray.put(myObject);

        parseObject.put("jsonArray", myArray);


        List<String> list = new ArrayList<>();
        list.add("string1");
        list.add("string2");
        parseObject.put("listStringField", list);

        List<Integer> listInt = new ArrayList<>();
        listInt.add(1);
        listInt.add(2);
        listInt.add(3);
        parseObject.put("listIntField", listInt);

        List<Boolean> listBool = new ArrayList<>();
        listBool.add(true);
        listBool.add(false);
        parseObject.put("listBoolField", listBool);

        progressDialog.show();
        parseObject.saveInBackground(e -> {
            progressDialog.dismiss();
            if (e == null) {
                Toast.makeText(this, "Object created successfully...", Toast.LENGTH_SHORT).show();
                objectId = parseObject.getObjectId();
            } else {
                objectId = null;
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void readObjects() {
        if (objectId == null) {
            Toast.makeText(this, "No object. Click on the 'Save Data' button before.", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseQuery<ParseObject> query = new ParseQuery<>("DataTypes");

        progressDialog.show();
        query.getInBackground(objectId, (object, e) -> {
            progressDialog.dismiss();
            if (e == null) {
                List<Data> list = new ArrayList<>();
                list.add(new Data("Int list field", object.get("listIntField").toString()));
                list.add(new Data("String field", object.get("stringField").toString()));
                list.add(new Data("Double field", object.get("doubleField").toString()));
                list.add(new Data("Int field", object.get("intField").toString()));
                list.add(new Data("String list field", object.get("listStringField").toString()));
                list.add(new Data("Date field", object.get("dateField").toString()));
                list.add(new Data("Bool field", object.get("boolField").toString()));
                list.add(new Data("List Bool field", object.get("listBoolField").toString()));
                list.add(new Data("Json Object field", object.get("jsonObject").toString()));
                list.add(new Data("Json Array field", object.get("jsonArray").toString()));

                showDataTypes(list);

            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDataTypes(List<Data> list) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Data types");
        alertDialogBuilder.setCancelable(true);
        initPopupViewControls(list);
        //We are setting our custom popup view by AlertDialog.Builder
        alertDialogBuilder.setView(popupInputDialogView);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void initPopupViewControls(List<Data> list) {
        LayoutInflater layoutInflater = LayoutInflater.from(MainActivity.this);
        popupInputDialogView = layoutInflater.inflate(R.layout.custom_alert_dialog, null);
        recyclerView = popupInputDialogView.findViewById(R.id.recyclerView);
        ItemAdapter adapter = new ItemAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
    }

    public void updateObject() {
        if (objectId == null) {
            Toast.makeText(this, "No object. Click on the 'Save Data' button before.", Toast.LENGTH_SHORT).show();
            return;
        }

        ParseObject parseObject = new ParseObject("DataTypes");
        parseObject.setObjectId(objectId);
        parseObject.put("intField", 5);
        parseObject.put("stringField", "new String");

        progressDialog.show();

        parseObject.saveInBackground(e -> {
            progressDialog.dismiss();
            if (e == null) {
                Toast.makeText(this, "Object updated successfully...", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}