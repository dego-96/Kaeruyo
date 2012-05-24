package jp.dego.kaeruyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class KitakuInfo
{
    public static final String PREF_KEY_SUBJECT = "pref_key_subject";
    public static final String PREF_KEY_MESSAGE = "pref_key_message";
    public static final String PREF_KEY_MAILTYPE = "pref_key_mailtype";
    public static final String PREF_KEY_MOVETIME = "pref_key_movetime";
    
    public static final String HHMM = "[HH:MM]";
    
    private String Subject;
    private String Message;
    private int MoveTime;
    private boolean MMS;
    
    // -------------------------------------------------
    // Constructor
    // -------------------------------------------------
    public KitakuInfo(Context context)
    {
        Subject = context.getString(R.string.default_subject);
        Message = context.getString(R.string.default_message);
        MMS = true;
        MoveTime = 0;
    }

    // -------------------------------------------------
    // Getters & Setters
    // -------------------------------------------------
    public String getSubject()
    {
        return Subject;
    }
    
    public String getMessage()
    {
        return Message;
    }
    
    public boolean isMMS()
    {
        return MMS;
    }
    
    public int getMoveTime()
    {
        return MoveTime;
    }
    
    public void setSubject(String subject)
    {
        this.Subject = subject;
    }
    
    public void setMessage(String message)
    {
        this.Message = message;
    }
    
    public void setMMS(boolean mms)
    {
        this.MMS = mms;
    }
    
    public void setMoveTime(int movetime)
    {
        this.MoveTime = movetime;
    }

    // -------------------------------------------------
    // Functions
    // -------------------------------------------------
    public void setInfo(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        Subject = pref.getString(PREF_KEY_SUBJECT, Subject); 
        Message = pref.getString(PREF_KEY_MESSAGE, Message);
        MMS = pref.getBoolean(PREF_KEY_MAILTYPE, MMS);
        MoveTime = pref.getInt(PREF_KEY_MOVETIME, MoveTime);
    }
    
    public void saveInfo(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(PREF_KEY_SUBJECT, Subject).commit();
        pref.edit().putString(PREF_KEY_MESSAGE, Message).commit();
        pref.edit().putBoolean(PREF_KEY_MAILTYPE, MMS).commit();
        pref.edit().putInt(PREF_KEY_MOVETIME, MoveTime).commit();
    }
    
    public void saveInfo(Context context, String subject, String message, boolean mms, int movetime)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        pref.edit().putString(PREF_KEY_SUBJECT, subject).commit();
        pref.edit().putString(PREF_KEY_MESSAGE, message);
        pref.edit().putBoolean(PREF_KEY_MAILTYPE, mms);
        pref.edit().putInt(PREF_KEY_MOVETIME, movetime);
    }
    
}
