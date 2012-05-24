package jp.dego.kaeruyo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends Activity
{
    // アドレス帳呼び出し時のリクエストコード
    private static final int PICK_CONTACT = 3;

    private ContactInfo mContactInfo;
    private KitakuInfo mKitakuInfo;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        InitDisplay();
    }

    //
    // Intentから返ってきたときに実行されるメソッド
    //
    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data)
    {
        super.onActivityResult(reqCode, resultCode, data);

        switch (reqCode) {
        case (PICK_CONTACT):
            if (resultCode == Activity.RESULT_OK) {
                // Intentのデータから連絡先情報を取得
                getContactInfo(data);

                // SharedPreferenceに連絡先情報を保存
                mContactInfo.saveInfo(this);

                // 宛先ボタンの表示を変更
                setSendToButtonText();
            }
        }
    }

    // -----------------------------------------------------------
    // 画面表示に関するメソッド
    // -----------------------------------------------------------
    private void InitDisplay()
    {
        setContentView(R.layout.main);

        mContactInfo = new ContactInfo(this);
        mKitakuInfo = new KitakuInfo(this);
        mKitakuInfo.setInfo(this);
        if (!"".equals(mContactInfo.getName()))
            setSendToButtonText();
        
        // 移動時間の設定
        Spinner spinner = (Spinner)findViewById(R.id.Spinner_MoveTime);
        spinner.setSelection(mKitakuInfo.getMoveTime());

        // 初期の件名と本文を設定
        String text1 = mKitakuInfo.getSubject();
        String text2 = mKitakuInfo.getMessage();
        EditText et1 = (EditText)findViewById(R.id.EditText_Subject);
        EditText et2 = (EditText)findViewById(R.id.EditText_Message);
        et1.setText(text1);
        et2.setText(text2);
        
        // メールタイプを設定
        setMailType();
    }

    private void setSendToButtonText()
    {
        Button btn = (Button)findViewById(R.id.Button_SendTo);
        String text = mContactInfo.getName();
        btn.setText("宛先 : " + text);
    }

    private void setMailType()
    {
        EditText et = (EditText)findViewById(R.id.EditText_Subject);
        et.setEnabled(mKitakuInfo.isMMS());
    }

    // -----------------------------------------------------------
    // ボタンを押したときに呼ばれるメソッド
    // -----------------------------------------------------------
    // 宛先指定ボタンをPush
    public void onSendToButtonClicked(View view)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        String title = getString(R.string.dialog_title_change_sendto);
        String message = getString(R.string.dialog_message_change_sendto);
        String textOK = getString(R.string.dialog_button_text_ok);
        String textCancel = getString(R.string.dialog_button_text_cancel);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(textOK, new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, PICK_CONTACT);
            }
        });
        builder.setNegativeButton(textCancel, null);
        builder.show();
    }

    // 送信ボタンをPush
    public void onSendButtonClicked(View view)
    {
        // 移動時間を取得
        int mt = ((Spinner)findViewById(R.id.Spinner_MoveTime)).getSelectedItemPosition();
        mKitakuInfo.setMoveTime(mt);
        
        // 各種情報を保存
        EditText et1 = (EditText)findViewById(R.id.EditText_Subject);
        EditText et2 = (EditText)findViewById(R.id.EditText_Message);
        mKitakuInfo.setSubject(et1.getText().toString());
        mKitakuInfo.setMessage(et2.getText().toString());
        mKitakuInfo.saveInfo(this);
        mContactInfo.saveInfo(this);
        
        // 件名と本文を取得
        String subject = mKitakuInfo.getSubject();
        String message = MessageManager.getMessage(mKitakuInfo, true);

        if (mKitakuInfo.isMMS()) {
            // 送信先アドレスの取得
            String address = mContactInfo.getAddress();

            // Intentインスタンスを生成
            Intent intent = new Intent();
            // アクションを指定(ACTION_SENDTOではないところがミソ)
            intent.setAction(Intent.ACTION_SEND);
            // データタイプを指定
            intent.setType("message/rfc822");

            // 宛先を指定
            intent.putExtra(Intent.EXTRA_EMAIL, new String[] {address});
            // 件名を指定
            intent.putExtra(Intent.EXTRA_SUBJECT, subject);
            // 本文を指定
            intent.putExtra(Intent.EXTRA_TEXT, message);

            // Intentを発行
            startActivity(intent);
        } else {
            // 電話番号の取得
            String phone = mContactInfo.getPhone();

            Uri uri = Uri.parse("smsto://");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setType("vnd.android-dir/mms-sms");
            intent.putExtra("address", phone); // 電話番号を入れる
            intent.putExtra("sms_body", message); // 送信メッセージを入れる
            try {
                startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // SMSアプリが無いときのエラー処理
                String text = getString(R.string.toast_cannot_use_sms);
                Toast.makeText(this, text, 1).show();
            }
        }
    }

    // -----------------------------------------------------------
    // Menu
    // -----------------------------------------------------------
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        String menu1 = getString(R.string.menu_text_mailtype);
        menu.add(0, Menu.FIRST, 0, menu1);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
        case Menu.FIRST:
            mKitakuInfo.setMMS(!mKitakuInfo.isMMS());
            setMailType();
            break;
        case Menu.FIRST + 1:
            break;
        }
        return true;
    }

    // -----------------------------------------------------------
    // 連絡先情報に関するメソッド
    // -----------------------------------------------------------
    // 電話帳から連絡先の情報を取得
    private void getContactInfo(Intent data)
    {
        Cursor c = null;
        try {
            // IDと表示名の取得
            c = managedQuery(data.getData(), null, null, null, null);
            if (c.moveToFirst()) {
                int idIndex = c.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
                mContactInfo.setId(c.getString(idIndex));
            }
            if (c.moveToFirst()) {
                mContactInfo.setName(c.getString(c
                        .getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)));
            }
            // Eメールアドレスと電話番号の取得
            c = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                    new String[] {mContactInfo.getId()}, null);
            if (c.moveToFirst()) {
                int emailIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA);
                mContactInfo.setAddress(c.getString(emailIndex));
            }
            c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    new String[] {mContactInfo.getId()}, null);
            if (c.moveToFirst()) {
                int phoneIndex = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA);
                mContactInfo.setPhone(c.getString(phoneIndex));
            }
        } finally {
            if (c != null)
                c.close();
        }
    }

}
