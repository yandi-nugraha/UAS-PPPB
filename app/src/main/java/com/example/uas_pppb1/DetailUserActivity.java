package com.example.uas_pppb1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class DetailUserActivity extends AppCompatActivity {

    EditText tglLahirEditText;
    Spinner spinner;
    Button simpanBtn;

    public static final String CATEGORY_EXTRA = "CATEGORY_KEY";
    public static final String AGE_EXTRA = "AGE_KEY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_user);

        tglLahirEditText = findViewById(R.id.tglLahir_editText);
        spinner = findViewById(R.id.spinner);
        simpanBtn = findViewById(R.id.simpan_btn);

        tglLahirEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePicker();
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        simpanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(tglLahirEditText.getText().toString())) {
                    tglLahirEditText.setError("Tanggal lahir tidak boleh kosong!");
                    return;
                } else {
                    showMainActivity();

                    String date = tglLahirEditText.getText().toString();
                    String year = date.substring(date.length() - 4);
                    int age = (2022 - Integer.parseInt(year));
                    String category = spinner.getSelectedItem().toString();

                    SharedPreferences sharedPref = getSharedPreferences(LoginActivity.sharedPrefFile, MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString(CATEGORY_EXTRA, category);
                    editor.putString(AGE_EXTRA, String.valueOf(age));
                    editor.apply();

                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                    intent.putExtra(AGE_EXTRA, String.valueOf(age));
                    intent.putExtra(CATEGORY_EXTRA, category);
                    view.getContext().startActivity(intent);

                    LoginActivity activity = new LoginActivity();
                    activity.saveFilled();
                }
            }
        });
    }

    public void showDatePicker() {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(), "date-picker");
    }

    public void processDatePickerResult(int day, int month, int year) {
        String day_string = Integer.toString(day);
        String month_string = Integer.toString(month);
        String year_string = Integer.toString(year);
        String dateMessage = day_string + "-" + month_string + "-" + year_string;
        tglLahirEditText.setText(dateMessage);
    }

    public void showMainActivity() {
        Intent intent = new Intent(DetailUserActivity.this, MainActivity.class);
        startActivity(intent);
    }
}