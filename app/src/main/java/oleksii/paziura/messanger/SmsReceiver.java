package oleksii.paziura.messanger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;
import android.widget.Toast;
public class SmsReceiver extends BroadcastReceiver {
    private static final String SMS_REC_ACTION =
            "android.provider.Telephony.SMS_RECEIVED";
    private static String fileNumb = Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingnumb.txt";
    private static String fileText = Environment.getExternalStorageDirectory().toString()+"/smsmes/incomingtext.txt";
    @Override
    public void onReceive(final Context context, final Intent intent) {
        if (intent.getAction().equals(SmsReceiver.SMS_REC_ACTION)) {
            StringBuilder sb = new StringBuilder();
            StringBuilder num = new StringBuilder();
            StringBuilder text = new StringBuilder();
            Bundle bundle = intent.getExtras();
            if (bundle != null) {
                Object[] pdus = (Object[]) bundle.get("pdus");
                for (Object pdu : pdus) {
                    SmsMessage smsMessage =
                            SmsMessage.createFromPdu((byte[]) pdu);

                    sb.append("\n"+
                            smsMessage.getOriginatingAddress());
                    sb.append("\n"+
                            smsMessage.getMessageBody());
                    num.append(smsMessage.getOriginatingAddress());
                    text.append(smsMessage.getMessageBody());
                }
            }
            Toast.makeText(context,"Прийнято SMS-повідомлення"+sb.toString(), Toast.LENGTH_LONG).show();
            SmsReceiver.write(fileNumb, num.toString());
            SmsReceiver.write(fileText, text.toString());
        }

    }
    public static void write(String fileName, String text) {
        File file = new File(fileName);
        File tdir = new File(Environment.getExternalStorageDirectory().toString()+"/smsmes/");
        tdir.mkdirs();
        try {
            if(!file.exists()){
                file.createNewFile();
            }
            PrintWriter out = new PrintWriter(new OutputStreamWriter(new FileOutputStream(fileName)), false);

            try {
                out.println(text);
            } finally {
                out.close();
            }
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }
}