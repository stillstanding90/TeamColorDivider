package com.stillstanding90.teamcolordivider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private int mGroupCount;
    private int mPeopleCount;

    private EditText mEditText;
    private Button mStartButton;

    private ArrayList<CheckBox> mColorCheckBoxes;
    private ArrayList<Integer> mColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mColors = new ArrayList<>();
        mColorCheckBoxes = new ArrayList<>();
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_red));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_orange));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_yellow));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_green));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_blue));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_purple));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_white));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_pink));
        mColorCheckBoxes.add((CheckBox) findViewById(R.id.cb_black));

        mEditText = findViewById(R.id.et_people);

        mStartButton = findViewById(R.id.btn_start);
        mStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(mEditText.getText())) {
                    Toast.makeText(MainActivity.this, "Input people count.", Toast.LENGTH_SHORT).show();
                    return;
                }

                mColors.clear();
                int j = 0;
                for (CheckBox checkBox : mColorCheckBoxes) {
                    if (checkBox.isChecked()) {
                        mColors.add(checkBox.getCurrentTextColor());
                        j++;
                    }
                }
                mGroupCount = j;
                mPeopleCount = Integer.parseInt(mEditText.getText().toString());

                if (mGroupCount > mPeopleCount) {
                    Toast.makeText(MainActivity.this, "The number of people must be larger than the number of groups.", Toast.LENGTH_SHORT).show();
                    return;
                } else if (mGroupCount == 0) {
                    Toast.makeText(MainActivity.this, "Please select a color group.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Intent intent = new Intent(MainActivity.this, DividerActivity.class);
                intent.putExtra("GROUP_COUNT", mGroupCount);
                intent.putExtra("PEOPLE_COUNT", mPeopleCount);
                intent.putIntegerArrayListExtra("COLORS", mColors);
                startActivity(intent);
            }
        });
    }
}
