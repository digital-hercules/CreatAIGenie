package com.ai.genie.common;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Base64;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDelegate;

import com.ai.genie.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UsefullData {

    private Context _context;
    private ProgressDialog pDialog;
    public UsefullData(Context c) {
        _context = c;
    }

    // ================== DEVICE INFORMATION ============//
    public static String getCountryCodeFromDevice() {
        String countryCode = Locale.getDefault().getCountry();
        if (countryCode.equals("")) {
            countryCode = "IN";
        }
        return countryCode;
    }

    // ================== GET TIME AND DATE ============//

    @SuppressLint("SimpleDateFormat")
    public static String getDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm aa");
        Calendar cal = Calendar.getInstance();
        // sdf.applyPattern("dd MMM yyyy");
        String strDate = sdf.format(cal.getTime());
        return strDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getDateTimeChangeFormat() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        // sdf.applyPattern("dd MMM yyyy");
        String strDate = sdf.format(cal.getTime());
        return strDate;
    }

    public static String getdistanceDayandTime(String time1,String time2){
        int time;
        String noteTime;
        String Time1 = time1;
        String Time2 =  time2;


        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;
        try {
            d1 = format.parse(Time1);
            d2 = format.parse(Time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        long difference = d2.getTime() - d1.getTime();

        long days = (int) (difference / (1000*60*60*24));

//        long years = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60)));

        long hours = (int) ((difference - (1000*60*60*24*days)) / (1000*60*60));
        long min = (int) (difference - (1000*60*60*24*days) - (1000*60*60*hours)) / (1000*60);
        long diffSeconds = difference / 1000;


        long diffSeconds1 = difference / 1000;
        long diffMinutes = difference / (60 * 1000);
        long diffHours = difference / (60 * 60 * 1000);
        long diffdays = difference / (24*60 * 60 * 1000);

        if (days ==0){

            if (hours == 0){
                if (min ==0){

                    time = (int) diffSeconds/1000;
                    noteTime = time +" sec ago";
                }else {
                    time = (int) min;
                    noteTime = time +" min ago";

                }

            }else {
                time = (int) hours;
                noteTime = time +" hours ago";

            }
        }else {
            time = (int) days;
            noteTime = time +" days ago";

        }
        return noteTime;

    }
    public String changeDateToyearFormat(String time) {
        String inputPattern ="yyyy-MM-dd HH:mm:ss"; //"dd/MM/yyyy HH:mm:ss";
//		String outputPattern = "dd MMM,yyyy h:mm a";
//		String outputPattern = "dd MMM,yyyy";
        String outputPattern = "yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public String changeDateTotimeformat(String time) {
        String inputPattern = "dd/MM/yyyy HH:mm:ss";
//		String outputPattern = "dd MMM,yyyy h:mm a";
//		String outputPattern = "dd MMM,yyyy";
        String outputPattern = "h:mm a";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm aa");
        Calendar cal = Calendar.getInstance();
        // sdf.applyPattern("dd MMM yyyy");
        String strTime = sdf.format(cal.getTime());
        return strTime;

    }

    // ================== CREATE FILE AND RELATED ACTION ============//
    public File getRootFile() {

        File f = new File(Environment.getExternalStorageDirectory(), _context
                .getString(R.string.app_name).toString());
        if (!f.isDirectory()) {
            f.mkdirs();
        }

        return f;
    }

    public void deleteRootDir(File root) {

        if (root.isDirectory()) {
            String[] children = root.list();
            for (int i = 0; i < children.length; i++) {
                File f = new File(root, children[i]);
                Log("DeleteRootDir", "file name:" + f.getName());
                if (f.isDirectory()) {
                    deleteRootDir(f);
                } else {
                    f.delete();
                }
            }
        }
    }

    public File createFile(String fileName) {
        File f = null;
        try {
            f = new File(getRootFile(), fileName);
            if (f.exists()) {
                f.delete();
            }

            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    // ================ DOWNLOAD =================//

    public void downloadAndDisplayImage(final String image_url,
                                        final ImageView v, int type) {

        new Thread() {

            public void run() {
                try {

                    InputStream in = new URL(image_url).openConnection()
                            .getInputStream();
                    Bitmap bm = BitmapFactory.decodeStream(in);
                    File fileUri = new File(getRootFile(),
                            getNameFromURL(image_url));
                    FileOutputStream outStream = null;
                    outStream = new FileOutputStream(fileUri);
                    bm.compress(Bitmap.CompressFormat.JPEG, 75, outStream);
                    outStream.flush();
                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {

                    File f = new File(getRootFile(), "aa.jpg");
                    if (f.exists()) {
                        final Bitmap bmp = BitmapFactory
                                .decodeFile(f.getPath());

                        v.post(new Runnable() {

                            @Override
                            public void run() {
                                // TODO Auto-generated method stub
                                v.setImageBitmap(bmp);
                            }
                        });

                        Log("Downloadmage", "download images and showing ,,,,");

                    }
                }
            }

        }.start();
    }


    public String getNameFromURL(String url) {

        String fileName = "existing_item.jpg";
        if (url != null) {
            fileName = url.substring(url.lastIndexOf('/') + 1, url.length());
        }
        return fileName;
    }

    // ================== LOG AND TOAST ====================//

    public static void Log(String tag, final String msg) {

        if (true) {
            android.util.Log.e(tag, msg);
        }

    }

    public void showMsgOnUI(final String msg) {
		/* final int SHORT_DELAY = 2000; // 2 seconds

		((Activity) _context).runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(_context, msg, ).show();
			}
		});*/


        final Toast toast = Toast.makeText(_context, msg, Toast.LENGTH_SHORT);
        toast.show();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                toast.cancel();
            }
        }, 500);

    }

    // =================== INTERNET ===================//
	/*public boolean isNetworkConnected() {
		ConnectivityManager cm = (ConnectivityManager) _context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni;
		ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			return false;
		} else
			return true;
	}*/

    // ==================== PROGRESS DIALOG ==================//

    public void showProgress(final String msg, final String title) {
        pDialog = ProgressDialog.show(_context, title, msg, true);
        pDialog.setCancelable(true);

        //  pDialog.setCanceledOnTouchOutside(true);
    }

    public void dismissProgress() {
        if (pDialog != null) {
            if (pDialog.isShowing()) {
                pDialog.cancel();
                pDialog = null;
            }
        }

    }

    // ==================== HIDE KEYBOARED ==================//
    public void hideKeyBoared() {

        InputMethodManager imm = (InputMethodManager) _context
                .getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

    }

    // ====================SET FONT SIZE==================//
    public Typeface getUsedFontLucida() {
        Typeface typeFace = Typeface.createFromAsset(_context.getAssets(),
                "fonts/lucida_hand_writting.ttf");
        return typeFace;
    }

    public Typeface getUsedFontArial() {
        Typeface typeFace = Typeface.createFromAsset(_context.getAssets(),
                "fonts/arial.ttf");
        return typeFace;
    }


    public String deviceId(){
        String android_id = Settings.Secure.getString(_context.getContentResolver(), Settings.Secure.ANDROID_ID);
//		String deviceId = md5(android_id).toUpperCase();

        return  android_id;
    }

    public String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }


    //========= IMGAE RELATED WORK==========//
    public static String encodeTobase64(Bitmap image) {
        Bitmap immagex = image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immagex.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static String changedateformat(String time,String inputPattern,String outputPattern) {
		/*String inputPattern = "yyyy-MM-dd HH:mm:ss";
		String outputPattern = "MMM dd,yyyy h:mm a";*/
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static Address getLocationFromAddress(Activity activity, String strAddress){

        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> address;
        Address location =  null;
        try {
            address = geocoder.getFromLocationName(strAddress, 1);
            if (address != null && address.size() > 0) {
                location = address.get(0);
            }


            //	Log.e("abc", "============" +  strAddress +" ========");

            return location;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return location;
    }


    public void setTheme(Context context){
        SaveData saveData = new SaveData(context);
        if (saveData.getInt(ConstantValue.THEME_MODE)==1){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        }else if (saveData.getInt(ConstantValue.THEME_MODE)==2){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);

        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        }
    }


}