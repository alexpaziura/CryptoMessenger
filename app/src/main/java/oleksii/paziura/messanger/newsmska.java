package oleksii.paziura.messanger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import android.app.ActionBar;
import android.app.*;
import android.content.*;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.*;
import android.widget.TextView.OnEditorActionListener;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.NavUtils;

public class newsmska extends ActionBarActivity implements OnClickListener {
    Button aButton;
    Button btnContact;
    EditText numb;
    EditText text;
    PendingIntent sentPI;
    String SENT = "SMS_SENT";
    SharedPreferences sPref;
    final String SAVED_SWITCH = "saved_switch";
    final String SAVED_ID_RB = "SAVED_ID_RB";
    final String SAVED_PASS = "saved_pass";
    RadioGroup rg;
    EditText etKey;
    Switch mSwitch;
    String savedSwitch;
    String savedId;
    BroadcastReceiver br;
    String shifrSMS="";
    int sId;
    private static String fileNumb = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingnumb.txt";
    private static String fileText = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingtext.txt";
    private static String fileShifr = Environment.getExternalStorageDirectory().toString()+"/smsmes/outcomingshifr.txt";
    private static final int IDM_SETTING = 1001;
    final int totalProgressTime = 100;
    private static final int CONTACT_PICK_RESULT = 666;
    private static final String LOG_TAG = "my_tag";
    String mContactId;
    String mPhoneNumber;
    String mContactName;
    Intent intent;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsmska);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Нове повідомлення");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);
        Typeface face = Typeface.createFromAsset(getAssets(), "appetite.otf");
	/* int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
     TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
     if(actionBarTitleView != null){
         actionBarTitleView.setTypeface(face);
     }
     final ActionBar bar = getActionBar();
    bar.setTitle("Нове повідомлення");*/
        aButton = (Button) this.findViewById(R.id.Button01);
        btnContact = (Button) findViewById(R.id.btnContact);
        aButton.setTypeface(face);
        numb = (EditText) findViewById(R.id.phone);
        text = (EditText) findViewById(R.id.textsms);
        TextView komu = (TextView) findViewById(R.id.textView1);
        komu.setTypeface(face);
        TextView sho = (TextView) findViewById(R.id.textView2);
        sho.setTypeface(face);
        sPref = getSharedPreferences("Prefer",MODE_PRIVATE);
        savedSwitch = sPref.getString(SAVED_SWITCH, "");
        savedId = sPref.getString(SAVED_ID_RB, "0");
        sId = Integer.parseInt(savedId);
        aButton.setOnClickListener(this);
        btnContact.setOnClickListener(this);
        aButton.setText("Відправити");
        aButton.setEnabled(true);
        text.setOnEditorActionListener(new OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    if(getCurrentFocus()!=null)
                    {
                        InputMethodManager imm = ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE));
                        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    }
                    onClick(aButton);
                    handled = true;
                }
                return handled;
            }
        });
        br = new BroadcastReceiver(){
            public void onReceive(Context ctx, Intent intent) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "Повідомлення надіслано",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "SMS: Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "SMS: No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "SMS: Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "SMS: Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
                unregisterReceiver(br);
                aButton.setText("Відправити");
                aButton.setEnabled(true);
                text.setText("");
                numb.setText("");
            }
        }; }
    public void onResume(){
        super.onResume();
        sPref = getSharedPreferences("Prefer",MODE_PRIVATE);
        savedSwitch = sPref.getString(SAVED_SWITCH, "");
        savedId = sPref.getString(SAVED_ID_RB, "0");
        sId = Integer.parseInt(savedId);
        aButton.setText("Відправити");
        aButton.setEnabled(true);
    }
    void modN() {
        shifrSMS="0";
        String otk_text = text.getText().toString();
        String alfavit = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 0123456789";
        int N=alfavit.length();
        String key = sPref.getString(SAVED_PASS, "");
        if(key.equals(""))
        {
            key="PaSsWoRd";
        }
        shifrSMS=shifrSMS+key;
        int i,j,k=0;
        ArrayList<Integer> nom_text = new ArrayList<Integer>();
        for(i=0;i<otk_text.length();i++)
            for(j=0;j<alfavit.length();j++)
            {if(otk_text.charAt(i)==alfavit.charAt(j)) {
                nom_text.add(j);
            }}
        int[] nom_key = new int[8];
        for(i=0;i<key.length();i++)
            for(j=0;j<alfavit.length();j++)
            {if(key.charAt(i)==alfavit.charAt(j)) {
                nom_key[k]=j;k++;
            }}
        k=0; int temp;
        ArrayList<Integer> nom_shifr = new ArrayList<Integer>();
        for(i=0;i<nom_text.size();i++)
        {
            if(k==8) k=0;
            temp=(nom_text.get(i)+nom_key[k])%N;
            nom_shifr.add(temp);
            k++;
        }
        for(i=0;i<nom_shifr.size();i++)
            for(j=0;j<alfavit.length();j++)
            {if(nom_shifr.get(i)==j) shifrSMS=shifrSMS+alfavit.charAt(j);}
    }
    public static void write(String fileName, Object text) {
        File file = new File(fileName);
        File tdir = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/");
        tdir.mkdirs();
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)), false);
            try {
                out.print(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.Button01:
                if(numb.getText().toString().equals(""))
                {
                    Toast.makeText(getBaseContext(), "Введіть номер отримувача",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(text.getText().toString().equals(""))
                {
                    Toast.makeText(getBaseContext(), "Введіть текст повідомленн",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                aButton.setText("Очікуйте..");
                aButton.setEnabled(false);

                SmsManager sms = SmsManager.getDefault();
                if(savedSwitch.equals("true"))
                {
                    switch(sId) {
                        case R.id.radio0:
                            if(check_for_modN()==true)
                            {
                                Toast.makeText(getBaseContext(), "Ключ заданий невірно!\nЗмініть налаштування!",
                                        Toast.LENGTH_SHORT).show();
                                aButton.setText("Відправити");
                                aButton.setEnabled(true);
                                return;
                            }
                            modN();
                            break;
                        case R.id.radio1:
                            if(check_for_playvij()==true)
                            {
                                Toast.makeText(getBaseContext(), "Ключ заданий невірно!\nЗмініть налаштування!",
                                        Toast.LENGTH_SHORT).show();
                                aButton.setText("Відправити");
                                aButton.setEnabled(true);
                                return;
                            }
                            Playfair();
                            break;
                        case R.id.radio2:
                            if(check_for_playvij()==true)
                            {
                                Toast.makeText(getBaseContext(), "Ключ заданий невірно!\nЗмініть налаштування!",
                                        Toast.LENGTH_SHORT).show();
                                aButton.setText("Відправити");
                                aButton.setEnabled(true);
                                return;
                            }
                            Vijiner();
                            break;
                        case R.id.radio3:
                            if(check_for_cezar()==true)
                            {
                                Toast.makeText(getBaseContext(), "Ключ заданий невірно!\nЗмініть налаштування!",
                                        Toast.LENGTH_SHORT).show();
                                aButton.setText("Відправити");
                                aButton.setEnabled(true);
                                return;
                            }
                            Cezar();
                            break;
                    }
                    sms.sendTextMessage(numb.getText().toString(), null, shifrSMS, sentPI, null);
                    newsmska.write(fileText, text.getText().toString());
                    newsmska.write(fileNumb, numb.getText().toString());
                    newsmska.write(fileShifr, shifrSMS);
                }
                else {
                    sms.sendTextMessage(numb.getText().toString(), null, text.getText().toString(), sentPI, null);
                    newsmska.write(fileText, text.getText().toString());
                    newsmska.write(fileNumb, numb.getText().toString());
                    newsmska.write(fileShifr, "");
                }
                registerReceiver(br, new IntentFilter(SENT));
                break;
            case R.id.btnContact:

                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        Contacts.CONTENT_URI);
                startActivityForResult(contactPickerIntent, CONTACT_PICK_RESULT);
                break;}
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
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void Playfair()
    {
        shifrSMS="1";
        String alfavit="abcdefghijklmnoprstuvwxyz";
        String key=sPref.getString(SAVED_PASS, "").toLowerCase(Locale.ENGLISH);
        if(key.equals(""))
        {
            key="password";
        }
        shifrSMS=shifrSMS+key;
        String op_text=text.getText().toString().toLowerCase(Locale.ENGLISH);
        String R="";
        String ResText="";
        char Resh[][] = new char[5][5];
        int i,j,num;
        for(i=0;i<key.length()-1;i++)
            for(j=i+1;j<key.length();j++)
                if(key.charAt(i)==key.charAt(j)) key=new StringBuffer(key).deleteCharAt(j).toString();
        for (i=0; i<key.length(); i++)
            for (j=0; j<alfavit.length(); j++)
                if (key.charAt(i) == alfavit.charAt(j)) alfavit=new StringBuffer(alfavit).deleteCharAt(j).toString();
        R=R+key+alfavit;
        num = 0;
        for (i=0; i<5; i++)
            for (j=0; j<5; j++)
            {
                Resh[i][j] = R.charAt(num);
                num++;
            }
        int ind_x1 = 0;
        int ind_y1 = 0;
        int ind_x2 = 0;
        int ind_y2 = 0;
        int k = 0;
        if(op_text.length()%2!=0){op_text=op_text+"0";}
        while (k<op_text.length())
        {
            for (i=0; i<5; i++)
                for (j=0; j<5; j++)
                {
                    if (op_text.charAt(k) == Resh[i][j])
                    {
                        ind_x1 = i;
                        ind_y1 = j;
                    }
                    if (op_text.charAt(k+1) == Resh[i][j])
                    {
                        ind_x2 = i;
                        ind_y2 = j;
                    }
                }
            if (ind_x1 == ind_x2)
            {
                if (ind_y1 == 4)
                {
                    ResText += Resh[ind_x1][0];
                    ResText += Resh[ind_x2][ind_y2+1];
                }
                else
                if (ind_y2 == 4)
                {
                    ResText += Resh[ind_x1][ind_y1+1];
                    ResText += Resh[ind_x2][0];
                }
                else
                {
                    ResText += Resh[ind_x1][ind_y1+1];
                    ResText += Resh[ind_x2][ind_y2+1];
                }
            }
            if (ind_y1 == ind_y2)
            {
                if (ind_x1 == 4)
                {
                    ResText += Resh[0][ind_y1];
                    ResText += Resh[ind_x2+1][ind_y2];
                }
                else
                if (ind_x2 == 4)
                {
                    ResText += Resh[ind_x1+1][ind_y1];
                    ResText += Resh[0][ind_y2];
                }
                else
                {
                    ResText += Resh[ind_x1+1][ind_y1];
                    ResText += Resh[ind_x2+1][ind_y2];
                }
            }
            if ((ind_x1 != ind_x2) && (ind_y1 != ind_y2))
            {
                ResText += Resh[ind_x1][ind_y2];
                ResText += Resh[ind_x2][ind_y1];
            }

            k = k + 2;
        }
        shifrSMS=shifrSMS+ResText;
    }
    public void Vijiner()
    {
        shifrSMS="2";
        int i,j;
        String alfavit="abcdefghijklmnopqrstuvwxyz ";
        String op_text=text.getText().toString();
        String key=sPref.getString(SAVED_PASS, "").toLowerCase(Locale.ENGLISH);
        if(key.equals(""))
        {
            key="password";
        }
        shifrSMS=shifrSMS+key;
        String key_on_s="";
        String result="";
        char dublicat;
        int shift=0;
        int x = 0, y = 0;
        boolean flag;
        char tabula_recta[][] = new char [alfavit.length()][alfavit.length()];
        for (i = 0; i < alfavit.length(); i++)
            for (j = 0; j < alfavit.length(); j++)
            {
                shift = j + i;
                if (shift >= alfavit.length()) shift = shift % alfavit.length();
                tabula_recta[i][j] = alfavit.charAt(shift);
            }
        for (i = 0; i < text.length(); i++)
        {
            key_on_s += key.charAt(i % key.length());
        }
        for (i = 0; i < text.length(); i++)
        {
            int l = 0;
            flag = false;
            while ((l < alfavit.length()) && (flag == false))
            {
                if (key_on_s.charAt(i) == tabula_recta[l][0])
                {
                    x = l;
                    flag = true;
                }
                l++;
            }
            dublicat = op_text.charAt(i);
            l = 0;
            flag = false;
            while ((l < alfavit.length()) && (flag == false))
            {
                if (dublicat == tabula_recta[0][l])
                {
                    y = l;
                    flag = true;
                }
                l++;
            }
            result += tabula_recta[x][y];
        }
        shifrSMS=shifrSMS+result;
    }
    public void Cezar()
    {
        int i,j,k;
        String alfavit="abcdefghijklmnopqrstuvwxyz ";
        String open_text=text.getText().toString();
        String cesar="";
        int key;
        String preKey=sPref.getString(SAVED_PASS, "");
        if(preKey.equals(""))
        {
            key=20;
        }
        key=Integer.parseInt(sPref.getString(SAVED_PASS, ""));
        if(key>27)
        {
            key=key%27;
        }
        for(i=0;i<open_text.length();i++)
            for(j=0;j<alfavit.length();j++)
            {
                if(open_text.charAt(i)==' ') {cesar=cesar+" ";break;}
                if(open_text.charAt(i)==alfavit.charAt(j)) {
                    if(j+key>26) k=j+key-26;
                    else k=j+key;
                    cesar=cesar+alfavit.charAt(k);
                }}
        shifrSMS="3";
        int len=preKey.length();
        for(i=0;i<8-len;i++)
        {
            shifrSMS=shifrSMS+"0";
        }
        shifrSMS=shifrSMS+preKey+cesar;
    }
    public boolean check_for_cezar()
    {
        String preKey=sPref.getString(SAVED_PASS, "");
        boolean flag=false;
        for (int i=0; i < preKey.length(); i++) {
            if (Character.isDigit(preKey.charAt(i))) {
                flag=false;
            }
            else {
                flag=true;
            }
        }
        return flag;
    }
    public boolean check_for_modN()
    {
        String preKey=sPref.getString(SAVED_PASS, "");
        boolean flag=false;
        char c;
        for (int i=0; i < preKey.length(); i++) {
            c = preKey.charAt(i);
            if ((c >= '0' && c <= '9')||(c >= 'A' && c <= 'Z')||(c >= 'a' && c <= 'z'))
            {
                flag=false;
            }
            else {
                flag=true;
            }
        }
        return flag;
    }
    public boolean check_for_playvij()
    {
        String preKey=sPref.getString(SAVED_PASS, "");
        boolean flag=false;
        char c;
        for (int i=0; i < preKey.length(); i++) {
            c = preKey.charAt(i);
            if (c >= 'a' && c <= 'z')
            {
                flag=false;
            }
            else {
                flag=true;
            }
        }
        return flag;
    }
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        int kill=0;
        final List<String> listItems = new ArrayList<String>();
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case CONTACT_PICK_RESULT:
                    Uri contactData = data.getData();
                    Cursor c =  getContentResolver().query(contactData, null, null, null, null);
                    if (c.moveToNext()) {
                        mContactId = c.getString(c.getColumnIndex(ContactsContract.Contacts._ID));
                        mContactName = c.getString(c.getColumnIndexOrThrow(
                                ContactsContract.Contacts.DISPLAY_NAME));

                        String hasPhone = c.getString(c.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER));

                        Log.d(LOG_TAG, "name: " + mContactName);
                        Log.d(LOG_TAG, "hasPhone:" + hasPhone);
                        Log.d(LOG_TAG, "contactId:" + mContactId);

                        // если есть телефоны, получаем и выводим их
                        if (hasPhone.equalsIgnoreCase("1")) {
                            Cursor phones = getContentResolver().query(
                                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                    null,
                                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ mContactId,
                                    null,
                                    null);

                            while (phones.moveToNext()) {
                                mPhoneNumber = phones.getString(phones.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d(LOG_TAG, "телефон:" + mPhoneNumber);
                                kill++;
                                listItems.add(mPhoneNumber);
                            }

                            phones.close();
                        }
                    }
                    if(kill>1){
                        final CharSequence[] items = listItems.toArray(new CharSequence[listItems.size()]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("Виберіть номер телефону")
                                .setItems(items, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        numb.setText(listItems.get(which));
                                    }
                                });
                        builder.create();
                        builder.show();}
                    if(kill==1)
                    {
                        numb.setText(listItems.get(0));
                    }
                    if(kill==0){
                        Toast.makeText(getBaseContext(), "Контакт не має номеру телефону.",
                                Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

        } else {
            Log.d(LOG_TAG, "ERROR");
        }
    }


}