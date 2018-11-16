package com.babase.lib.utils;

import android.app.AppOpsManager;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author bauer on 2017/8/1.
 */

public class LibUtil {
    /**
     * 文件的类型
     */
    public static final int FILE_TYPE_COMMON_FILE = 0;
    public static final int FILE_TYPE_AUDIO = 1;
    public static final int FILE_TYPE_VIDEO = 2;
    public static final int FILE_TYPE_IMAGE = 3;
    public static final int FILE_TYPE_GIF = 4;
    public static final int FILE_TYPE_TEXT = 5;
    public static final int FILE_TYPE_WORD = 6;
    public static final int FILE_TYPE_EXCEL = 7;
    public static final int FILE_TYPE_PPT = 8;
    public static final int FILE_TYPE_PDF = 9;
    public static final int FILE_TYPE_ZIP = 10;
    public static final int FILE_TYPE_APK = 11;
    public static final int FILE_TYPE_UNKNOW = 12;

    /**
     * 收集设备参数信息
     *
     * @param ctx
     */
    public static Map<String, String> collectDeviceInfo(Context ctx) {
        Map<String, String> infos = new HashMap<>();
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                infos.put("versionName", versionName);
                infos.put("versionCode", versionCode);
            }
        } catch (PackageManager.NameNotFoundException e) {
            Logger.e(e);
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                infos.put(field.getName(), field.get(null).toString());
                Logger.d(field.getName() + " : " + field.get(null));
            } catch (Exception e) {
                Logger.e(e);
            }
        }
        return infos;
    }

    /**
     * 颜色变化过度
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static int evaluateColor(float fraction, Object startValue, Object endValue) {
        int startInt = (Integer) startValue;
        int startA = (startInt >> 24) & 0xff;
        int startR = (startInt >> 16) & 0xff;
        int startG = (startInt >> 8) & 0xff;
        int startB = startInt & 0xff;

        int endInt = (Integer) endValue;
        int endA = (endInt >> 24) & 0xff;
        int endR = (endInt >> 16) & 0xff;
        int endG = (endInt >> 8) & 0xff;
        int endB = endInt & 0xff;

        return (startA + (int) (fraction * (endA - startA))) << 24 |
                (startR + (int) (fraction * (endR - startR))) << 16 |
                (startG + (int) (fraction * (endG - startG))) << 8 |
                (startB + (int) (fraction * (endB - startB)));
    }

    /**
     * 隐藏键盘
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            v.getLocationInWindow(leftTop);
            int left = leftTop[0];
            int top = leftTop[1];
            int bottom = top + v.getHeight();
            int right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }

    /**
     * 根据之前的日期转换成更加人性化的日期
     *
     * @param compareDate 需要转化的日期，yyyy-mm-dd hh:mm:ss
     * @return 返回比较日期和当前日期的相差天数，以前的返回正数，以后的返回负数
     */
    public static int getDaysBeteenDates(String compareDate) {
        String date = compareDate.substring(0, 10);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //将需要比较的日期转成calendar
        Calendar d1 = new GregorianCalendar();
        try {
            d1.setTime(sdf.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
            //转换失败的话，就不需要处理
            return 100;
        }
        //获取今天的calendar
        Calendar d2 = new GregorianCalendar();
        d2.setTime(new Date());
        //得到相差多少天
        int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
        int y2 = d2.get(Calendar.YEAR);
        //比较是否是同一年，如果不是，则加当年的实际天数，只需要比较一年就行了
        if (d1.get(Calendar.YEAR) != y2) {
            days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);
        }
        //返回相差几天
        return days;
    }

    /**
     * 比较时间差
     *
     * @param compareDate 需要转化的日期，yyyy-mm-dd hh:mm:ss
     * @return 返回比较日期和当前日期的相差分钟
     */
    public static int getSecBeteenDates(String compareDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        //将需要比较的日期转成calendar
        Calendar d1 = new GregorianCalendar();
        try {
            d1.setTime(sdf.parse(compareDate));
        } catch (ParseException e) {
            e.printStackTrace();
            //转换失败的话，就不需要处理
            return 100;
        }
        //获取今天的calendar
        Calendar d2 = new GregorianCalendar();
        d2.setTime(new Date());
        long millis = d1.getTimeInMillis() - d2.getTimeInMillis();
        //毫秒转成秒
        return (int) (millis / 1000);
    }

    /**
     * 将秒转成时间 mm：ss
     *
     * @param sec
     * @return
     */
    public static String formatTime(int sec) {
        int min = sec / 60;
        int s = sec % 60;
        if (s < 10) {
            return min + ":0" + s;
        } else {
            return min + ":" + s;
        }
    }

    /**
     * 将秒转成时间 hh：mm：ss
     *
     * @param sec
     * @return
     */
    public static String formatTime(long sec) {
        long h = sec / 3600;
        long m = (sec % 3600) / 60;
        long s = ((sec % 3600) % 60) % 60;

        String hour, min, second;

        if (h < 10) {
            hour = "0" + h;
        } else {
            hour = h + "";
        }

        if (m < 10) {
            min = "0" + m;
        } else {
            min = m + "";
        }

        if (s < 10) {
            second = "0" + s;
        } else {
            second = s + "";
        }

        return hour + ":" + min + ":" + second;
    }

    /**
     * 验证邮箱输入是否合法
     *
     * @param strEmail
     * @return
     */
    public static boolean isEmail(String strEmail) {
        String strPattern = "[A-Z0-9a-z._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}";
//        String strPattern = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern p = Pattern.compile(strPattern);
        Matcher m = p.matcher(strEmail);
        return m.matches();
    }

    /**
     * 图片重新着色
     *
     * @param drawableId
     * @param colorId
     * @param tint
     * @return
     */
    public static Drawable tintDrawable(Context context, @DrawableRes int drawableId, @ColorRes int colorId, boolean tint) {
        final Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        if (tint) {
            final Drawable wrappedDrawable = DrawableCompat.wrap(drawable);
            DrawableCompat.setTint(wrappedDrawable, ContextCompat.getColor(context, colorId));
            return wrappedDrawable;
        } else {
            return drawable;
        }
    }

    /**
     * md5校验，如果失败，则直接返回原字符串
     *
     * @param str
     * @return
     */
    public static String md5(String str) {
        MessageDigest messageDigest;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            Logger.out("NoSuchAlgorithmException caught!");
            return str;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return str;
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
            }
        }
        // 16位加密，从第9位到25位
//		return md5StrBuff.substring(8, 24).toString().toUpperCase();
        Logger.out("md5 result------->" + md5StrBuff.toString());
        //32位，小写
        return md5StrBuff.toString().toLowerCase();
    }

    /**
     * 用来判断是否开启通知权限
     *
     * @param context
     * @return
     */
    public static boolean isNotificationEnabled(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE)).areNotificationsEnabled();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager mAppOps = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            ApplicationInfo appInfo = context.getApplicationInfo();
            String pkg = context.getApplicationContext().getPackageName();
            int uid = appInfo.uid;

            try {
                Class<?> appOpsClass = Class.forName(AppOpsManager.class.getName());
                Method checkOpNoThrowMethod = appOpsClass.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class);
                Field opPostNotificationValue = appOpsClass.getDeclaredField("OP_POST_NOTIFICATION");
                int value = (int) opPostNotificationValue.get(Integer.class);
                return ((int) checkOpNoThrowMethod.invoke(mAppOps, value, uid, pkg) == AppOpsManager.MODE_ALLOWED);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return true;
        }
    }

    /**
     * 获取字符串的长度，包括中文字符，中文算2个字符
     *
     * @param cs
     * @return
     */
    public static int getSequenceLength(CharSequence cs) {
        int length = 0;
        if (cs == null || cs.length() == 0) {
            return length;
        }
        char c;
        for (int i = 0; i < cs.length(); i++) {
            c = cs.charAt(i);
            if (isChinese(c)) {
                length += 2;
            } else {
                length++;
            }
        }
        return length;
    }

    /**
     * 字符是否是中文
     *
     * @param c
     * @return
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    /**
     * 判断手机号格式是否正确
     * 1.11位数字
     * 2.符合中国的手机号
     *
     * @param mobileStr
     * @return
     */
    public static boolean checkChineseMobile(String mobileStr) {
        final String patternRule = "^1(3|5|7|8|4)\\d{9}";
        final String mobile = mobileStr.replace(" ", "");
        Pattern p = Pattern.compile(patternRule);
        Matcher matcher = p.matcher(mobile);
        return mobile.length() == 11 && matcher.matches();
    }

    /**
     * 格式化手机号，xxx xxxx xxxx的形式
     *
     * @param s
     * @return
     */
    public static StringBuilder formateMobileNumber(CharSequence s) {
        StringBuilder sb = new StringBuilder();
        //添加空格
        for (int i = 0; i < s.length(); i++) {
            if (i != 3 && i != 8 && s.charAt(i) == ' ') {
                continue;
            } else {
                sb.append(s.charAt(i));
                if ((sb.length() == 4 || sb.length() == 9) && sb.charAt(sb.length() - 1) != ' ') {
                    sb.insert(sb.length() - 1, ' ');
                }
            }
        }
        return sb;
    }

    /**
     * 为了格式化手机号，需要更新输入光标的位置
     *
     * @param start
     * @param before
     * @param sb
     * @return
     */
    public static int updateSelectionIndex(int start, int before, StringBuilder sb) {
        int index = start + 1;
        if (TextUtils.isEmpty(sb)) {
            //如果输入为空，则直接为0
            index = 0;
        } else if (start < sb.length()) {
            //如果改变之后，start还在sb范围内
            if (sb.charAt(start) == ' ') {
                if (before == 0) {
                    index++;
                } else {
                    index--;
                }
            } else {
                if (before == 1) {
                    index--;
                }
            }
        } else {
            //start超出了sb的范围，说明index也超过了，那么重新修改index
            index = sb.length();
        }
        return index;
    }

    /**
     * 根据后缀获取文件类型
     *
     * @param fileName
     * @return
     */
    public static int getFileType(String fileName) {
        if (TextUtils.isEmpty(fileName)) {
            return FILE_TYPE_UNKNOW;
        }
        if (fileName.toLowerCase().endsWith(".3gp")
                || fileName.toLowerCase().endsWith(".asf")
                || fileName.toLowerCase().endsWith(".avi")
                || fileName.toLowerCase().endsWith(".m4v")
                || fileName.toLowerCase().endsWith(".mov")
                || fileName.toLowerCase().endsWith(".mp4")
                || fileName.toLowerCase().endsWith(".mpeg")
                || fileName.toLowerCase().endsWith(".mpg")
                || fileName.toLowerCase().endsWith(".mpg4")
                || fileName.toLowerCase().endsWith(".rmvb")
                || fileName.toLowerCase().endsWith(".wmv")) {
            return FILE_TYPE_VIDEO;
        } else if (fileName.toLowerCase().endsWith(".m4a")
                || fileName.toLowerCase().endsWith(".m4b")
                || fileName.toLowerCase().endsWith(".m4p")
                || fileName.toLowerCase().endsWith(".mp2")
                || fileName.toLowerCase().endsWith(".mp3")
                || fileName.toLowerCase().endsWith(".mpga")
                || fileName.toLowerCase().endsWith(".ogg")
                || fileName.toLowerCase().endsWith(".wav")
                || fileName.toLowerCase().endsWith(".wma")) {
            return FILE_TYPE_AUDIO;
        } else if (fileName.toLowerCase().endsWith(".apk")) {
            return FILE_TYPE_APK;
        } else if (fileName.toLowerCase().endsWith(".txt")) {
            return FILE_TYPE_TEXT;
        } else if (fileName.toLowerCase().endsWith(".pdf")) {
            return FILE_TYPE_PDF;
        } else if (fileName.toLowerCase().endsWith(".doc")
                || fileName.toLowerCase().endsWith(".docx")) {
            return FILE_TYPE_WORD;
        } else if (fileName.toLowerCase().endsWith(".xls")
                || fileName.toLowerCase().endsWith(".xlsx")) {
            return FILE_TYPE_EXCEL;
        } else if (fileName.toLowerCase().endsWith(".pps")
                || fileName.toLowerCase().endsWith(".ppt")
                || fileName.toLowerCase().endsWith(".pptx")) {
            return FILE_TYPE_PPT;
        } else if (fileName.toLowerCase().endsWith(".gif")) {
            return FILE_TYPE_GIF;
        } else if (fileName.toLowerCase().endsWith(".jpeg")
                || fileName.toLowerCase().endsWith(".jpg")
                || fileName.toLowerCase().endsWith(".png")) {
            return FILE_TYPE_IMAGE;
        } else if (fileName.toLowerCase().endsWith(".tgz")
                || fileName.toLowerCase().endsWith(".zip")
                || fileName.toLowerCase().endsWith(".z")) {
            return FILE_TYPE_ZIP;
        } else {
            return FILE_TYPE_COMMON_FILE;
        }
    }

    /**
     * 获取第一个单词
     * <p>
     * 如果是字母，则返回大写字母
     * 如果是特殊字符，用#代替
     *
     * @param word
     * @return
     */
    public static String getIndexLetter(String word) {
        char firstLetter = word.charAt(0);
        //大写字母||小写字母
        if (firstLetter >= 65 && firstLetter <= 90 ||
                firstLetter >= 97 && firstLetter <= 122) {
            //转成大写
            return String.valueOf(firstLetter).toUpperCase();
        } else {
            //其他都返回#
            return "#";
        }
    }
}
