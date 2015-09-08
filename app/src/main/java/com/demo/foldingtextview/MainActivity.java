package com.demo.foldingtextview;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.TextView;


public class MainActivity extends ActionBarActivity {

    private TextView foldingTextView;
    private ImageView foldImageView;
    private int maxLine=2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initData();
        initEvent();
    }
    private void initView(){
        foldingTextView=(TextView)findViewById(R.id.foldingText);
        foldImageView=(ImageView)findViewById(R.id.reverseImage);
    }
    private void initData(){
        foldingTextView.setText(getResources().getString(R.string.foldingtext));
    }
    private void initEvent(){
        foldingTextView.post(new Runnable() {
            @Override
            public void run() {
                //getLineCount() is only useful after the textview is ready
                if (foldingTextView.getLineCount()>maxLine){
                    foldingTextView.setHeight(foldingTextView.getLineHeight()*maxLine);
                    foldImageView.setVisibility(ImageView.VISIBLE);
                    foldImageView.setOnClickListener(new MyOnClickListener());
                    foldingTextView.setOnClickListener(new MyOnClickListener());
                }
            }
        });
    }
    private class MyOnClickListener implements View.OnClickListener{
        private boolean isExpanded=false;
        public MyOnClickListener(){}
        @Override
        public void onClick(View v) {
            foldingTextView.clearAnimation();
            final int startHeight=foldingTextView.getHeight();
            final int tempHeight;
            final long durationTime=200;
            //if is expanded, cloase the textview and reverse imageview
            if (isExpanded){
                //get the height of textview that need change
                tempHeight=foldingTextView.getLineHeight()*maxLine-foldingTextView.getLineHeight()*foldingTextView.getLineCount();
                //imageview anticlockwise rotate 180
                RotateAnimation rotateAnimation=new RotateAnimation(180,0,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setDuration(durationTime);
                rotateAnimation.setFillAfter(true);
                foldImageView.startAnimation(rotateAnimation);
            }
            else{
                tempHeight=foldingTextView.getLineHeight() *foldingTextView.getLineCount()-foldingTextView.getLineHeight()*maxLine;
                //clockwise rotate 180
                RotateAnimation rotateAnimation=new RotateAnimation(0,180,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                rotateAnimation.setDuration(durationTime);
                rotateAnimation.setFillAfter(true);
                foldImageView.startAnimation(rotateAnimation);
            }
            //set animation for textview
            Animation animation=new Animation(){
                @Override
                protected void applyTransformation(float interpolatedTime, Transformation t) {
                    foldingTextView.setHeight((int)(startHeight+tempHeight*interpolatedTime));
                }
            };
            animation.setDuration(durationTime);
            foldingTextView.startAnimation(animation);
            //change the flag of expanded
            isExpanded=!isExpanded;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
