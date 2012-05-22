package jp.dego.kaeruyo;

import java.util.Date;

import android.content.Context;

public class MessageManager
{
    // private String mSubject;
    // private String mMessage;

    // // ----------------------------------------------
    // // Constructor
    // // ----------------------------------------------
    // public MessageManager(Context context)
    // {
    // mSubject = context.getString(R.string.default_subject);
    // mMessage = context.getString(R.string.default_message);
    // }

    public static String getDefaultSubject(Context context)
    {
        return context.getString(R.string.default_subject);
    }

    public static String getDefaultMessage(Context context)
    {
        return context.getString(R.string.default_message);
    }

    public static String getMessage(String text0, Date date)
    {
        return getTimeText(text0, date);
    }

    public static String getMessage(String subject0, String message0, boolean MMS, int time)
    {
        Date date = new Date(System.currentTimeMillis() + time * 60 * 1000);
        
        return "";
    }

    public static String getTimeText(String message, Date date)
    {
        String time = date.getHours() + "時" + date.getMinutes() + "分";
        String res = message.replaceAll("[HH:MM]", time);

        return res;
    }
}
