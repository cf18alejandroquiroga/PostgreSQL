package com.android.m3m9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static Context ctx;

    private EditText attribute1;
    private EditText attribute2;
    private EditText attribute3;

    private Button insertButton;
    private Button deleteButton;
    private Button editButton;

    private TextView elementsList;
    final SimpleDateFormat format = new SimpleDateFormat("MM-dd-yyyy");
    private List<ExampleElement> eeList = AccessData.getAccessData(ctx).getExampleElements();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        loadElements();

        attribute1 = findViewById(R.id.attribute1_input);
        attribute2 = findViewById(R.id.attribute2_input);
        attribute3 = findViewById(R.id.attribute3_input);
        insertButton = findViewById(R.id.insert_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.editLast_button);
        elementsList = findViewById(R.id.elements_tv);

        if (!eeList.isEmpty()) elementsList.setText(ExampleElementListToString(eeList));

        insertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isIntParseable = isNumeric(attribute1.getText().toString());
                boolean isDateParseable = isDate(attribute2.getText().toString());

                if (isIntParseable && isDateParseable){
                    int attributeInt = Integer.parseInt(attribute1.getText().toString());
                    String attributeDate = attribute2.getText().toString();
                    String attributeString = attribute3.getText().toString();
                    saveElements(attributeInt, attributeDate, attributeString);

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));
                    attribute1.setText("");
                    attribute2.setText("");
                    attribute3.setText("");
                }
                else{
                    if (!isIntParseable) Toast.makeText(ctx, "Attribute 1 Incorrect format, must be an Integer", Toast.LENGTH_SHORT).show();
                    if (!isIntParseable) Toast.makeText(ctx, "Attribute 2 Incorrect format, must be a Date \"mm-dd-yyyy\"", Toast.LENGTH_SHORT).show();
                }
            }

        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isParseable = isNumeric(attribute1.getText().toString());
                if (isParseable) {
                    int attributeInt = Integer.parseInt(attribute1.getText().toString());
                    deleteElements(attributeInt);

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));

                    attribute1.setText("");
                    attribute2.setText("");
                    attribute3.setText("");
                }
                else{
                    if (!isParseable) Toast.makeText(ctx, "Attribute 1 Incorrect format, must be an Integer", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isIntParseable = isNumeric(attribute1.getText().toString());
                boolean isDateParseable = isDate(attribute2.getText().toString());

                if (isIntParseable && isDateParseable){
                    int attributeInt = Integer.parseInt(attribute1.getText().toString());
                    String attributeString = (attribute2.getText().toString());
                    String attributeDate = attribute3.getText().toString();

                    editElements(attributeInt, attributeDate, attributeString);

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));

                    attribute1.setText("");
                    attribute2.setText("");
                    attribute3.setText("");
                }
                else{
                    if (!isIntParseable) Toast.makeText(ctx, "Attribute 1 Incorrect format, must be an Integer", Toast.LENGTH_SHORT).show();
                    if (!isIntParseable) Toast.makeText(ctx, "Attribute 2 Incorrect format, must be a Date \"mm-dd-yyyy\"", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String ExampleElementListToString(List<ExampleElement> eeList){
        String result = "";
        for (ExampleElement ee : eeList){
            result += "AttributeInt : " + ee.getAttributeInt() + "\n" +
                    "AttributeDate: " + ee.getAttributeDate() + "\n" +
                    "AttributeString: " + ee.getAttributeString() + "\n\n";
        }
        return result;
    }

    //Shame on Java
    private boolean isNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    private boolean isDate(String string) {
        try {
            format.parse(string);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    private void loadElements(){
        Thread sqlThread = new Thread() {
            @Override
            public void run() {
                AccessData.getAccessData(ctx).loadExampleElements();
                eeList = AccessData.getAccessData(ctx).getExampleElements();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        elementsList.setText(ExampleElementListToString(eeList));
                    }
                });
            }
        };
        sqlThread.start();
    }

    private void saveElements(final int a1, final String a2, final String a3){
        Thread sqlThread = new Thread() {
            @Override
            public void run() {
                AccessData.getAccessData(ctx).saveExampleElement(a1, a2, a3);
                AccessData.getAccessData(ctx).loadExampleElements();
                runOnUiThread(new Runnable() {
                    public void run() {
                        eeList = AccessData.getAccessData(ctx).getExampleElements();
                        elementsList.setText(ExampleElementListToString(eeList));
                    }
                });
            }
        };
        sqlThread.start();
    }

    private void editElements(final int a1, final String a2, final String a3){
        Thread sqlThread = new Thread() {
            @Override
            public void run() {
                AccessData.getAccessData(ctx).updateExampleElementByAttributeElement(a1, a2, a3);
                AccessData.getAccessData(ctx).loadExampleElements();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eeList = AccessData.getAccessData(ctx).getExampleElements();
                        elementsList.setText(ExampleElementListToString(eeList));
                    }
                });
            }
        };
        sqlThread.start();
    }

    private void deleteElements(final int a1){
        Thread sqlThread = new Thread() {
            @Override
            public void run() {
                AccessData.getAccessData(ctx).deleteExampleElement(a1);
                AccessData.getAccessData(ctx).loadExampleElements();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        eeList = AccessData.getAccessData(ctx).getExampleElements();
                        elementsList.setText(ExampleElementListToString(eeList));
                    }
                });
            }
        };
        sqlThread.start();
    }
}
