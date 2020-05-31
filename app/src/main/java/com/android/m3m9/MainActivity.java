package com.android.m3m9;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ctx = this;
        AccessData.getAccessData(ctx).loadExampleElements();

        attribute1 = findViewById(R.id.attribute1_input);
        attribute2 = findViewById(R.id.attribute2_input);
        attribute3 = findViewById(R.id.attribute3_input);
        insertButton = findViewById(R.id.insert_button);
        deleteButton = findViewById(R.id.delete_button);
        editButton = findViewById(R.id.editLast_button);
        elementsList = findViewById(R.id.elements_tv);

        AccessData.getAccessData(ctx).loadExampleElements();

        List<ExampleElement> eeList = AccessData.getAccessData(ctx).getExampleElements();
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
                    AccessData.getAccessData(ctx).saveExampleElement(attributeInt, attributeDate, attributeString);

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));
                }
                else{
                    if (!isIntParseable) System.out.println("Attribute 1 Incorrect format, must be an Integer");
                    if (!isDateParseable) System.out.println("Attribute 2 Incorrect format, must be a Date \"mm-dd-yyyy\"");
                }
            }


        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isParseable = isNumeric(attribute1.getText().toString());
                if (isParseable) {
                    int attributeInt = Integer.parseInt(attribute1.getText().toString());
                    AccessData.getAccessData(ctx).deleteExampleElement(attributeInt);

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));
                }
                else{
                    System.out.println("Attribute 1 Incorrect format, must be an Integer");
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
                    Date attributeDate;
                    try {
                        attributeDate = format.parse(attribute3.toString());
                        AccessData.getAccessData(ctx).updateExampleElementByAttributeElement(attributeInt, attributeDate, attributeString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    AccessData.getAccessData(ctx).loadExampleElements();
                    elementsList.setText(ExampleElementListToString(eeList));
                }
                else{
                    if (!isIntParseable) System.out.println("Attribute 1 Incorrect format, must be an Integer");
                    if (!isDateParseable) System.out.println("Attribute 2 Incorrect format, must be a Date \"mm-dd-yyyy\"");
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
}
