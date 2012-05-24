package jp.dego.kaeruyo;

import java.util.Date;

public class MessageManager
{
//    public static String getMessage(KitakuInfo info)
//    {
//        String res = "";
//        int mt = info.getMoveTime();
//        Date date = new Date(System.currentTimeMillis() + mt * 60 * 1000);
//        String gethome = date.getHours() + "時" + date.getMinutes() + "分";
//        if (info.isMMS()) {
//            // MMSの場合
//            res = info.getMessage().replaceAll(KitakuInfo.HHMM, gethome);
//        } else {
//            // SMSの場合
//            res = info.getSubject() + info.getMessage();
//            res = res.replace(KitakuInfo.HHMM, gethome);
//        }
//
//        return res;
//    }

    public static String getMessage(KitakuInfo info, boolean about)
    {
        String res = "";
        int mt = PositionToMinute(info.getMoveTime());
        Date date = new Date(System.currentTimeMillis() + mt * 60 * 1000);
        int hh = date.getHours();
        int mm = date.getMinutes();
        if (about) {
            if (info.getMoveTime() < 60) {
                if (mm % 5 < 5)
                    mm = mm - (mm % 5) + 5;
                else
                    mm = mm - (mm % 5) + 10;
            } else {
                if (mm % 10 != 0)
                    mm = mm - (mm % 10) + 10;
            }
        }
        String gethome = hh + "時" + mm + "分";
        if (info.isMMS()) {
            // MMSの場合
            res = info.getMessage().replaceAll(KitakuInfo.HHMM, gethome);
        } else {
            // SMSの場合
            res = info.getSubject() + info.getMessage();
            res = res.replace(KitakuInfo.HHMM, gethome);
        }

        return res;
    }

    private static int PositionToMinute(int pos)
    {
        if(-1 < pos && pos < 10)
            return 10 + pos * 5;
        else if(pos < 17)
            return 60 + (pos - 10) * 10;
        else
            return 0;
    }
}
