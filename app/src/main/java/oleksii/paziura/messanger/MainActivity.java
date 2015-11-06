package oleksii.paziura.messanger;


import java.io.File;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements OnClickListener {
    private static String fileNumb1 = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingnumb.txt";
    private static String fileText1 = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingtext.txt";
    private static String fileShifr1 = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingshifr.txt";
    private static String fileNumb2 = Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingnumb.txt";
    private static String fileText2 = Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingtext.txt";
    Button newsms;
    Button incoming;
    Button outcoming;
    private static final int IDM_SETTING = 1001;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        Typeface face = Typeface.createFromAsset(getAssets(), "appetite.otf");
        newsms = (Button) findViewById(R.id.btnNew);
        newsms.setOnClickListener(this);
        newsms.setTypeface(face);
        incoming = (Button) findViewById(R.id.btnIncom);
        incoming.setOnClickListener(this);
        outcoming = (Button) findViewById(R.id.btnOutcom);
        outcoming.setOnClickListener(this);
        int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
        if(actionBarTitleView != null){

        }

    }
    Intent intent;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnNew:
                intent = new Intent("intent.action.newsmska");
                startActivity(intent);
                break;
            case R.id.btnIncom:
                intent = new Intent("intent.action.incoming");
                startActivity(intent);
                break;
            case R.id.btnOutcom:
                intent = new Intent("intent.action.outcom");
                startActivity(intent);
                break;
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, IDM_SETTING, 1, "Налаштування")
                .setIcon(R.drawable.shest)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        return super.onCreateOptionsMenu(menu);
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case IDM_SETTING:
                intent = new Intent("intent.action.settings");
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}