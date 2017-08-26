package com.project.user.makephoto;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.hardware.display.DisplayManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ResourceBundle;


/**
 * Created by user on 2017-08-27.
 */

public class ControlActivity extends AppCompatActivity {
    static ControlActivity controlActivity;
    public static Context mContext;
    SeekBar left_luminosity;
    SeekBar left_saturation;
    public static ImageView selected_image;
    Bitmap image_bitmap;
    int current_progress_l;
    int current_progress_s;
    final int REQ_CODE_SELECT_IMAGE=100;

//    public static ImageView selected_image_static;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        controlActivity = this;
        mContext = this;

        setContentView(R.layout.sub_control);

        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

//        selected_image_static = selected_image;

      //  qq();


    }
/*
    public void qq(){

        left_saturation = (SeekBar) findViewById(R.id.left_saturation);
        left_saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                selected_image.setImageBitmap(SetSaturation(image_bitmap, current_progress_s));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_s=progress;
            }
        });
        left_luminosity = (SeekBar) findViewById(R.id.left_luminosity);
        left_luminosity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                selected_image.setImageBitmap
                        (SetBrightness
                                (image_bitmap, current_progress_l));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_l=progress;
            }
        });
    }
*/
    public void qq(SeekBar left_saturation, SeekBar left_luminosity){

       //left_saturation = (SeekBar) findViewById(R.id.left_saturation);
        left_saturation.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                selected_image.setImageBitmap(SetSaturation(image_bitmap, current_progress_s));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_s=progress;
            }
        });
        //left_luminosity = (SeekBar) findViewById(R.id.left_luminosity);
        left_luminosity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener(){
            @Override
            public void onStartTrackingTouch(SeekBar seekBar){
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar){
                //0~100  default :  50
                selected_image.setImageBitmap
                        (SetBrightness
                                (image_bitmap, current_progress_l));


            }
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser){
                current_progress_l=progress;
            }
        });
    }


    public void autoActivityChange(){


        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        int i = controlActivity.getTaskId();
        DisplayManager mDisplayManager = (DisplayManager)getSystemService(Context.DISPLAY_SERVICE);
        // enumerate the displays
        Display[] presentationDisplays = mDisplayManager.getDisplays(DisplayManager.DISPLAY_CATEGORY_PRESENTATION);
        am.setExternalDisplay(i, presentationDisplays[0], am.SET_EXTERNAL_DISPLAY_AND_STAY);




        ((MainActivity)MainActivity.mContext).setDefaultDisplay();


        //moveTaskToBack(true);
        //    List<ActivityManager.RunningTaskInfo> Info = am.getRunningTasks(1);
        //    ComponentName topActivity = Info.get(0).topActivity;
        //    String name = topActivity.getPackageName();
        //  am.setExternalDisplay(data, ((WindowManager)getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay(), am.SET_EXTERNAL_DISPLAY_AND_GO_HOME);

        //finish();
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Toast.makeText(getBaseContext(), "resultCode : "+resultCode,Toast.LENGTH_SHORT).show();

        if(requestCode == REQ_CODE_SELECT_IMAGE)
        {
            if(resultCode== Activity.RESULT_OK)
            {
                try {
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
                    selected_image = (ImageView)findViewById(R.id.imageView);

                    selected_image.setImageBitmap(image_bitmap);

                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                autoActivityChange();
                //여기에 딜레이 후 시작할 작업들을 입력
            }
        }, 100);// 0.5초 정도 딜레이를 준 후 시작

    }


    public Bitmap SetBrightness(Bitmap src, int value) {
        // original image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        int gap = value-127;
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                R += gap;
                if(R > 255) { R = 255; }
                else if(R < 0) { R = 0; }

                G += gap;
                if(G > 255) { G = 255; }
                else if(G < 0) { G = 0; }

                B += gap;
                if(B > 255) { B = 255; }
                else if(B < 0) { B = 0; }



                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        // return final image
        return bmOut;
    }

    public Bitmap SetSaturation(Bitmap src, int value) {
        // original image size
        int width = src.getWidth();
        int height = src.getHeight();
        // create output bitmap
        Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
        // color information
        int A, R, G, B;
        int pixel;
        int gap = value-255;
        // scan through all pixels
        for(int x = 0; x < width; ++x) {
            for(int y = 0; y < height; ++y) {
                // get pixel color
                pixel = src.getPixel(x, y);
                A = Color.alpha(pixel);
                R = Color.red(pixel);
                G = Color.green(pixel);
                B = Color.blue(pixel);

                // increase/decrease each channel
                A += gap;
                if(A > 255) { A = 255; }
                else if(A < 0) { A = 0; }

                // apply new pixel color to output bitmap
                bmOut.setPixel(x, y, Color.argb(A, R, G, B));
            }
        }


        // return final image
        return bmOut;
    }

}