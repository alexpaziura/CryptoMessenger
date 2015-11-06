package oleksii.paziura.messanger;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

public class outcom extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.outcom);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Останнє вихідне повідомлення");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface face = Typeface.createFromAsset(getAssets(), "appetite.otf");
        TextView komu = (TextView) findViewById(R.id.number_komu);
        TextView comu = (TextView) findViewById(R.id.comu);
        comu.setTypeface(face);
        TextView opentext = (TextView) findViewById(R.id.opentext);
        TextView openmes = (TextView) findViewById(R.id.openmes);
        openmes.setTypeface(face);
        TextView shifrsms = (TextView) findViewById(R.id.shifrsms);
        TextView zaktext = (TextView) findViewById(R.id.zaktext);
        zaktext.setTypeface(face);
   	 /*int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
     TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
     if(actionBarTitleView != null){
         actionBarTitleView.setTypeface(face);
     }
     final ActionBar bar = getActionBar();
    bar.setTitle("Останнє вихідне повідомленнz");*/
        File zakt = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingshifr.txt");
        File numb = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingnumb.txt");
        File vidt = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingtext.txt");
        if(numb.exists()&&numb.isFile()&&zakt.exists()&&zakt.isFile()&&vidt.exists()&&vidt.isFile())
        {
            List<String> list1 = readFile(Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingtext.txt");
            List<String> list = readFile(Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingnumb.txt");
            komu.setText(list.get(0));
            opentext.setText(list1.get(0));
            try {
                List<String> list2 = readFile(Environment.getExternalStorageDirectory().toString() + "/smsmes/outcomingshifr.txt");
                shifrsms.setText(list2.get(0));
            }
            catch(Exception e)
            {
                e.printStackTrace();
                shifrsms.setText("");
            }
        }
        else {
            komu.setText("");
            opentext.setText("");
            shifrsms.setText("");
        }
    }
    private List<String> readFile(String filename)
    {
        List<String> records = new ArrayList<String>();
        try
        {
            BufferedReader reader = new BufferedReader(new FileReader(filename));
            String line;
            while ((line = reader.readLine()) != null)
            {
                records.add(line);
            }
            reader.close();
            return records;
        }
        catch (Exception e)
        {
            System.err.format("Exception occurred trying to read '%s'.", filename);
            e.printStackTrace();
            return null;
        }
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