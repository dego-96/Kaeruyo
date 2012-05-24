package jp.dego.kaeruyo;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class ContactInfo
{
    public static final String PREF_KEY_SIZE = "pref_key_size";
    public static final String PREF_KEY_ID = "pref_key_id";
    public static final String PREF_KEY_NAME = "pref_key_name";
    public static final String PREF_KEY_ADDRESS = "pref_key_address";
    public static final String PREF_KEY_PHONE = "pref_key_phone";
    
    private String id;
    private String name;
    private String address;
    private String phone;
    
    public ContactInfo()
    {
        this.id = "";
        this.name = "";
        this.address = "";
        this.phone = "";
    }
    
    public ContactInfo(Context context)
    {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        
        this.id = pref.getString(PREF_KEY_ID, "");
        this.name = pref.getString(PREF_KEY_NAME, "");
        this.address = pref.getString(PREF_KEY_ADDRESS, "");
        this.phone = pref.getString(PREF_KEY_PHONE, "");
    }
    
    //
    // Toast表示用テキストの取得
    // (デバッグ用)
    //
    public String getToastText()
    {
        String text;
        text = "Name : " + this.name;
        text += "\nAddress : " + this.address;
        text += "\nPhone : " + this.phone;
        text += "\nID : " + this.id;
        return text;
    }
    
    //
    // Setter & Getter
    //
    // ID
    public String getId()
    {
        return id;
    }
    
    public void setId(String id)
    {
        this.id = id;
    }
    
    // Name
    public String getName()
    {
        return name;
    }
    
    public void setName(String name)
    {
        this.name = name;
    }
    
    // Address
    public String getAddress()
    {
        return address;
    }
    
    public void setAddress(String address)
    {
        this.address = address;
    }
    
    // Phone
    public String getPhone()
    {
        return phone;
    }
    
    public void setPhone(String phone)
    {
        this.phone = phone;
    }
    
    //
    // Save to Preference
    //
    public void saveInfo(Context context)
    {

        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);

        // IDを保存
        if (!"".equals(id))
            pref.edit().putString(ContactInfo.PREF_KEY_ID, id).commit();
        // 名前を保存
        if (!"".equals(name))
            pref.edit().putString(ContactInfo.PREF_KEY_NAME, name).commit();
        // E-mailアドレスを保存
        if (!"".equals(address))
            pref.edit().putString(ContactInfo.PREF_KEY_ADDRESS, address).commit();
        // 電話番号を保存
        if (!"".equals(phone))
            pref.edit().putString(ContactInfo.PREF_KEY_PHONE, phone).commit();
    }
}
