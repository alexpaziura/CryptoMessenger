package oleksii.paziura.messanger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;

@SuppressLint("SdCardPath") public class incom extends ActionBarActivity implements OnClickListener {
    int nom_shifra;
    String key;
    String zakr_text;
    String open_text;
    Button btnDecode;
    TextView tvopen;
    TextView optext;

    @SuppressLint("SdCardPath") @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.incommes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Останнє вхідне повідомлення");
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Typeface face = Typeface.createFromAsset(getAssets(), "appetite.otf");
        TextView vid = (TextView) findViewById(R.id.vid);
        vid.setTypeface(face);
        TextView zakr = (TextView) findViewById(R.id.zakr);
        zakr.setTypeface(face);
        optext = (TextView) findViewById(R.id.optext);
        optext.setTypeface(face);
        btnDecode = (Button) findViewById(R.id.encode);
        btnDecode.setTypeface(face);
      	 /*int actionBarTitle = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
         TextView actionBarTitleView = (TextView) getWindow().findViewById(actionBarTitle);
         if(actionBarTitleView != null){
             actionBarTitleView.setTypeface(face);
         }
         final ActionBar bar = getActionBar();
        bar.setTitle("Останнє вхідне повідомлення");*/

        btnDecode.setEnabled(false);
        TextView tvNumb = (TextView)findViewById(R.id.number);
        TextView tvshi = (TextView)findViewById(R.id.shifrsms);
        tvopen = (TextView) findViewById(R.id.opentext);
        File ft = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingtext.txt");
        File numb = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingnumb.txt");
        if(numb.exists()&&numb.isFile()&&ft.exists()&&ft.isFile())
        {
            List<String> list = readFile(Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingtext.txt");
            List<String> list1 = readFile(Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingnumb.txt");

            tvNumb.setText(list1.get(0));
            tvshi.setText(list.get(0));
            String vhidSMS = list.get(0);
            StringBuilder vhidneSMS = new StringBuilder(vhidSMS.subSequence(0, vhidSMS.length()));
            char c=vhidneSMS.charAt(0);
            String se = String.valueOf(c);
            if(se.equals("0")||se.equals("1")||se.equals("2")||se.equals("3"))
            {nom_shifra = Integer.parseInt(se);
                vhidneSMS.deleteCharAt(0);
                key=vhidneSMS.substring(0, 8);
                vhidneSMS.delete(0, 8);
                zakr_text = vhidneSMS.toString();
                btnDecode.setEnabled(true);
            }
        }
        else{
            tvNumb.setText("");
            tvshi.setText("");
        }
        optext.setVisibility(View.GONE);
        tvopen.setVisibility(View.GONE);
        btnDecode.setOnClickListener(this);
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
    @Override
    public void onClick(View v) {
        switch (nom_shifra) {
            case 0:
                DecodeModN();
                break;
            case 1:
                DecodePlayfair();
                break;
            case 2:
                DecodeVijiner();
                break;
            case 3:
                DecodeCezar();
                break;
        }
        optext.setVisibility(View.VISIBLE);
        tvopen.setVisibility(View.VISIBLE);
        btnDecode.setVisibility(View.GONE);
    }

    public void DecodeModN(){
        String alfavit = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz 0123456789";
        int N=alfavit.length();
        open_text="";
        int i,j,k=0;
        ArrayList<Integer> nom_zakr_text = new ArrayList<Integer>();
        for(i=0;i<zakr_text.length();i++)
            for(j=0;j<alfavit.length();j++)
            {if(zakr_text.charAt(i)==alfavit.charAt(j)) {
                nom_zakr_text.add(j);
            }}
        int[] nom_key = new int[8];
        for(i=0;i<key.length();i++)
            for(j=0;j<alfavit.length();j++)
            {if(key.charAt(i)==alfavit.charAt(j)) {
                nom_key[k]=j;k++;
            }}
        k=0; int temp;
        ArrayList<Integer> nom_otext = new ArrayList<Integer>();
        for(i=0;i<nom_zakr_text.size();i++)
        {
            if(k==8) k=0;
            temp=(nom_zakr_text.get(i)+N-nom_key[k])%N;
            nom_otext.add(temp);
            k++;
        }
        for(i=0;i<nom_otext.size();i++)
            for(j=0;j<alfavit.length();j++)
            {if(nom_otext.get(i)==j) open_text=open_text+alfavit.charAt(j);}
        tvopen.setText(open_text);
    }
    public void DecodePlayfair(){
        String alfavit="abcdefghijklmnoprstuvwxyz";

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
        if(zakr_text.length()%2!=0){zakr_text=zakr_text+"0";}
        while (k<zakr_text.length())
        {
            for (i=0; i<5; i++)
                for (j=0; j<5; j++)
                {
                    if (zakr_text.charAt(k) == Resh[i][j])
                    {
                        ind_x1 = i;
                        ind_y1 = j;
                    }

                    if (zakr_text.charAt(k+1) == Resh[i][j])
                    {
                        ind_x2 = i;
                        ind_y2 = j;
                    }
                }
            if (ind_x1 == ind_x2)
            {
                if (ind_y1 == 0)
                {
                    ResText += Resh[ind_x1][4];
                    ResText += Resh[ind_x2][ind_y2-1];
                }
                else
                if (ind_y2 == 0)
                {
                    ResText += Resh[ind_x1][ind_y1-1];
                    ResText += Resh[ind_x2][4];
                }
                else
                {
                    ResText += Resh[ind_x1][ind_y1-1];
                    ResText += Resh[ind_x2][ind_y2-1];
                }
            }
            if (ind_y1 == ind_y2)
            {
                if (ind_x1 == 0)
                {
                    ResText += Resh[0][ind_y1];
                    ResText += Resh[ind_x2-1][ind_y2];
                }
                else
                if (ind_x2 == 0)
                {
                    ResText += Resh[ind_x1-1][ind_y1];
                    ResText += Resh[4][ind_y2];
                }
                else
                {
                    ResText += Resh[ind_x1-1][ind_y1];
                    ResText += Resh[ind_x2-1][ind_y2];
                }
            }
            if ((ind_x1 != ind_x2) && (ind_y1 != ind_y2))
            {
                ResText += Resh[ind_x1][ind_y2];
                ResText += Resh[ind_x2][ind_y1];
            }

            k = k + 2;
        }
        open_text=ResText;
        tvopen.setText(open_text);
    }
    public void DecodeVijiner()
    {
        int i,j;
        String alfavit="abcdefghijklmnopqrstuvwxyz ";
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
        for (i = 0; i < zakr_text.length(); i++)
        {
            key_on_s += key.charAt(i % key.length());
        }
        for (i = 0; i < zakr_text.length(); i++)
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
            dublicat = zakr_text.charAt(i);
            l = 0;
            flag = false;
            while ((l < alfavit.length()) && (flag == false))
            {
                if (dublicat == tabula_recta[x][l])
                {
                    y = l;
                    flag = true;
                }
                l++;
            }
            result += tabula_recta[0][y];
        }
        open_text=result;
        tvopen.setText(open_text);
    }
    public void DecodeCezar()
    {
        int i,j,k;
        String alfavit="abcdefghijklmnopqrstuvwxyz ";
        String uncesar="";
        int keys=Integer.parseInt(key);
        if(keys>27)
        {
            keys=keys%27;
        }
        for(i=0;i<zakr_text.length();i++)
            for(j=0;j<alfavit.length();j++)
            {
                if(zakr_text.charAt(i)==' ') {uncesar=uncesar+" ";break;}
                if(zakr_text.charAt(i)==alfavit.charAt(j)) {
                    if(j-keys<0) k=26+(j-keys);
                    else k=j-keys;
                    uncesar=uncesar+alfavit.charAt(k);
                }}
        open_text=uncesar;
        tvopen.setText(open_text);
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