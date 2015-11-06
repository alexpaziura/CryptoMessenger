package oleksii.paziura.messanger;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Map;
import android.app.ActionBar;
import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class setting extends ActionBarActivity implements CompoundButton.OnCheckedChangeListener,OnClickListener  {
    Button btnSave;
    SharedPreferences sPref;
    final String SAVED_SWITCH = "saved_switch";
    final String SAVED_ID_RB = "SAVED_ID_RB";
    final String SAVED_PASS = "saved_pass";
    RadioGroup rg;
    Switch mSwitch;
    EditText etKey;
    RadioButton rb0;
    RadioButton rb1;
    RadioButton rb2;
    RadioButton rb3;
    TextView tvKey;
    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    String id="";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Налаштування");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface face = Typeface.createFromAsset(getAssets(), "appetite.otf");
        rg = (RadioGroup) findViewById(R.id.radioGroup1);
        mSwitch = (Switch) findViewById(R.id.switch1);
        mSwitch.setTypeface(face);
        mSwitch.setOnCheckedChangeListener(this);
        rb0 = (RadioButton) findViewById(R.id.radio0);
        rb0.setTypeface(face);
        rb1 = (RadioButton) findViewById(R.id.radio1);
        rb1.setTypeface(face);
        rb2 = (RadioButton) findViewById(R.id.radio2);
        rb2.setTypeface(face);
        rb3 = (RadioButton) findViewById(R.id.radio3);
        rb3.setTypeface(face);
        tvKey = (TextView) findViewById(R.id.tvKey);
        tvKey.setTypeface(face);
        tv1 = (TextView) findViewById(R.id.tv1);
        tv1.setTypeface(face);
        tv2 = (TextView) findViewById(R.id.tv2);
        tv2.setTypeface(face);
        tv3 = (TextView) findViewById(R.id.tv3);
        tv3.setTypeface(face);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv4.setTypeface(face);
        etKey = (EditText) findViewById(R.id.key);
        if(mSwitch.isChecked()) {
            rb0.setEnabled(true);
            rb1.setEnabled(true);
            rb2.setEnabled(true);
            rb3.setEnabled(true);
            etKey.setEnabled(true);
        }
        else
        {
            rb0.setEnabled(false);
            rb1.setEnabled(false);
            rb2.setEnabled(false);
            rb3.setEnabled(false);
            etKey.setEnabled(false);
        }
        btnSave = (Button) findViewById(R.id.save_settings);
        btnSave.setTypeface(face);
        btnSave.setOnClickListener(this);
        /*int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
        if(actionBarTitleView != null){
            actionBarTitleView.setTypeface(face);
        }
         final ActionBar bar = getActionBar();
        bar.setTitle("Налаштування");*/
        loadPref();
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio0:
                        etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case R.id.radio1:
                        etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case R.id.radio2:
                        etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case R.id.radio3:
                        etKey.setInputType(InputType.TYPE_CLASS_NUMBER);
                        break;
                }
            }
        });
    }
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(!isChecked) {
            rb0.setEnabled(false);
            rb1.setEnabled(false);
            rb2.setEnabled(false);
            rb3.setEnabled(false);
            etKey.setEnabled(false);
        }
        else
        {
            rb0.setEnabled(true);
            rb1.setEnabled(true);
            rb2.setEnabled(true);
            rb3.setEnabled(true);
            etKey.setEnabled(true);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.save_settings:
                savePref();
                break;
        }
    }
    void savePref() {
        sPref = getSharedPreferences("Prefer",MODE_PRIVATE);
        Editor ed = sPref.edit();
        ed.clear();
        if(mSwitch.isChecked())
        {
            ed.putString(SAVED_SWITCH, "true");
        }
        else {
            ed.putString(SAVED_SWITCH, "false");
        }
        switch (rg.getCheckedRadioButtonId()) {
            case R.id.radio0:
                id = ""+R.id.radio0;
                ed.putString(SAVED_ID_RB, id);
                break;
            case R.id.radio1:
                id = ""+R.id.radio1;
                ed.putString(SAVED_ID_RB, id);
                break;
            case R.id.radio2:
                id = ""+R.id.radio2;
                ed.putString(SAVED_ID_RB, id);
                break;
            case R.id.radio3:
                id = ""+R.id.radio3;
                ed.putString(SAVED_ID_RB, id);
                break;
        }
        ed.putString(SAVED_PASS, etKey.getText().toString());
        ed.commit();
        File myPath = new File(Environment.getExternalStorageDirectory().toString());
        File myFile = new File(myPath, "MySharedPreferences");
        try
        {
            FileWriter fw = new FileWriter(myFile);
            PrintWriter pw = new PrintWriter(fw);


            Map<String,?> prefsMap = sPref.getAll();

            for(Map.Entry<String,?> entry : prefsMap.entrySet())
            {
                pw.println(entry.getKey() + ": " + entry.getValue().toString());
            }

            pw.close();
            fw.close();
        }
        catch (Exception e)
        {
            Log.wtf(getClass().getName(), e.toString());
        }
        Toast.makeText(this, "Налаштування збереженно", Toast.LENGTH_SHORT).show();
    }
    void loadPref() {
        sPref = getSharedPreferences("Prefer",MODE_PRIVATE);
        String savedSwitch = sPref.getString(SAVED_SWITCH, "");
        mSwitch.setChecked(Boolean.valueOf(savedSwitch));
        String savedId = sPref.getString(SAVED_ID_RB, "0");
        int sId = Integer.parseInt(savedId);
        switch (sId) {
            case R.id.radio0:
                rb0.setChecked(true);
                etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.radio1:
                rb1.setChecked(true);
                etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.radio2:
                rb2.setChecked(true);
                etKey.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.radio3:
                rb3.setChecked(true);
                etKey.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
        }
        String savedKey = sPref.getString(SAVED_PASS, "");
        etKey.setText(savedKey);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}