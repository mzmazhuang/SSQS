package com.dading.ssqs.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;

import java.util.ArrayList;
import java.util.List;

public class ScrollTextView extends android.support.v7.widget.AppCompatTextView {

    ArrayList<String> lineStrings;
    float currentY;
    Handler handler;
    String scrollText="";
    private int exactlyWidth = -1;
    private int exactlyHeight = -1;  
    private float index = 0;  
    private List mList;
    public String getScrollText() {
        return scrollText;  
    }  

    public void setScrollText(String scrollText) {  
        this.scrollText = scrollText;  
        reset();
    }
    private void reset() {  
          
        if(lineStrings!=null)  
        lineStrings.clear();  
        stop();  
        currentY = 0;  
        absloutHeight = 0;  
        this.setText("");  
        if(handler!=null){  
            handler.removeMessages(0);  
            handler.removeMessages(1);  
            handler.removeMessages(2);  
            handler.removeMessages(3);  
        }  
              
        requestLayout();  
        invalidate();  
        
    }  
  
    public ScrollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);  
  
        init();  
    }  
  
    public ScrollTextView(Context context, AttributeSet attrs, int defStyle) {  
        super(context, attrs, defStyle);  
        init();  
    }  
  
    public ScrollTextView(Context context) {  
        super(context);  
        init();  
    }
    boolean scrolling = false;
    float absloutHeight = 0;
   private  int delayTime = 10;  //???????  
   private   int stopTime = 0;  //??????  
  
    public int getDelayTime() {  
    return delayTime;  
}  
  
public void setDelayTime(int delayTime) {  
    this.delayTime = delayTime;  
}  
  
public int getStopTime() {  
    return stopTime;  
}  
  
public void setStopTime(int stopTime) {  
    this.stopTime = stopTime;  
}
    float speed = 0.5f;
    void init() {  
        handler = new Handler() {  
  
            @Override  
            public void handleMessage(Message msg) {
                if (absloutHeight <= getHeight()) {  
                    currentY = 0;  
                    stop();  
                    return;  
                }  
                switch (msg.what) {  
  
                    case 0: {  
                        currentY = currentY - speed;  
                          
                        //resetCurrentY();  
                        invalidate();  
                        boolean flag = true;  
                        if (currentY >= absloutHeight || currentY <= -absloutHeight) {  
                            flag = false;  
                            handler.sendEmptyMessageDelayed(3, stopTime);  
                        }  
                        if(flag){  
                              
                              if (currentY >=  index|| currentY <= -index || getHeight() <= 0) {  
                                    
                                  handler.sendEmptyMessageDelayed(2, stopTime);  
                              }else{  
                                 handler.sendEmptyMessageDelayed(0, delayTime);  
                              }  
                        }  
                        
                        
                        break;  
                    }  
                    case 1: {  
  
                        currentY += msg.arg1;  
  
                        resetCurrentY();  
                        invalidate();  
                         
                    }  
                    break;  
                    case 2:{  
                        index = index+absloutHeight/lineStrings.size();  
                         stop();  
                         handler.sendEmptyMessageDelayed(0, delayTime);  
                    }  
                    break;  
                    case 3:{  
                         currentY = 0;  
                         index = absloutHeight/lineStrings.size();;  
                         stop();  
                         handler.sendEmptyMessageDelayed(0, delayTime);  
                    }  
                }  
  
            }
            private void resetCurrentY() {  
                if (currentY >= absloutHeight || currentY <= -absloutHeight || getHeight() <= 0) {  
                    currentY = 0;  
                }  
  
            }  
        };  
          
    }
    float lastY = 0;
    boolean needStop;  
  
    public void pause() {  
        if (scrolling) {  
  
            stop();  
            needStop = true;  
        }  
    }  
  
    public void goOn() {  
  
        if (needStop) {  
            play();  
            needStop = false;  
        }  
    }  
  
   /* @Override 
    public boolean onTouchEvent(MotionEvent event) { 
 
        switch (event.getAction()) { 
            case MotionEvent.ACTION_DOWN: 
                distanceY = lastY = event.getY(); 
                distanceX = event.getX(); 
                pause(); 
 
                return true; 
            case MotionEvent.ACTION_MOVE: 
                float dy = event.getY() - lastY; 
                lastY = event.getY(); 
                // currentY = currentY + dy; 
                Message msg = Message.obtain(); 
                msg.what = 1; 
                msg.arg1 = (int)dy; 
                handler.sendMessage(msg); 
                return true; 
            case MotionEvent.ACTION_UP: 
            case MotionEvent.ACTION_CANCEL: 
                goOn(); 
                float y = event.getY() - distanceY; 
                float x = event.getX() - distanceX; 
 
                if (Math.sqrt(y * y + x * x) < performUpScrollStateDistance) { 
                    updateScrollStatus(); 
                } 
                return true; 
 
        } 
        return super.onTouchEvent(event); 
    } 
*/
    public static final long performUpScrollStateDistance = 5;  
  
    public float distanceY = 0;  
  
    public float distanceX = 0;
    public void updateScrollStatus() {  
  
        if (scrolling) {  
            stop();  
        } else {  
            play();  
        }  
    }  

    public void play() {  
  
        if (!scrolling) {  
            handler.sendEmptyMessage(0);  
            scrolling = true;  
        }  
    }  
  

    public void stop() {  
        if (scrolling) {  
            handler.removeMessages(0);  
            scrolling = false;  
        }  
    }  
  
    @Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        int width = MeasureWidth(widthMeasureSpec);  
        int height = MeasureHeight(width, heightMeasureSpec);  
        setMeasuredDimension(width, height);  
        currentY = 0;  
        if (height < absloutHeight) {  
            play();  
        } else {  
            stop();  
        }  
  
    }  

    private int MeasureWidth(int widthMeasureSpec) {  
        int mode = MeasureSpec.getMode(widthMeasureSpec);  
        int width = MeasureSpec.getSize(widthMeasureSpec);  
        // ?????wrap_content  
        if (mode == MeasureSpec.AT_MOST) {  
  
            double abwidth = getPaint().measureText(scrollText);  
  
            width = Math.min((int)Math.rint(abwidth), width);  
            exactlyWidth = -1;  
        }  
        if (mode == MeasureSpec.EXACTLY) {  
            exactlyWidth = width;  
        }  
        return width;  
    }
    private int MeasureHeight(int width, int heightMeasureSpec) {  
        int mode = MeasureSpec.getMode(heightMeasureSpec);  
        int height = MeasureSpec.getSize(heightMeasureSpec);  
        generateTextList(width);
        int lines = lineStrings.size();  
  
        absloutHeight = lines * getLineHeight() + getPaddingBottom() + getPaddingTop();  
        index = absloutHeight/lines;  
        // ?????wrap_content  
        if (mode == MeasureSpec.AT_MOST) {  
  
            height = (int)Math.min(absloutHeight, height);  
            exactlyHeight = -1;  
  
        } else if (mode == MeasureSpec.EXACTLY) {  
            exactlyHeight = height;  
        }  
        return height;  
    }  

    boolean isENWordStart(String str, int i) {  
  
        if (i == 0) {  
            return true;  
  
        } else if (str.charAt(i - 1) == ' ') {  
            return true;  
        }  
        return false;  
    }  

    private String getLineText(int MaxWidth, String str) {  
  
        // ?????  
        StringBuffer trueStringBuffer = new StringBuffer();  
        // ?????  
        StringBuffer tempStringBuffer = new StringBuffer();  
        boolean isLine;  
        for (int i = 0; i < str.length(); i++) {  
            isLine = false;  
            char c = str.charAt(i);  
            String add = "";  
            // ???c????????????????????й???  
            if (!isChinese(c) && isENWordStart(str, i)) {  
  
                int place = getNextSpecePlace(i, str);  
                // ???????????  
                if (place > -1) {  
                    add = str.substring(i, place) + " ";  
                    if (getPaint().measureText(add) > MaxWidth) {  
                        add = "" + c;  
                    } else {  
                        i = place;  
                    }  
                } else {  
                    add = "" + c;  
                }  
            } else {  
                if(c=='\n') { //?ж????????е?????  
                    isLine = true;  
                     add = "" + c;  
                }else  
                add = "" + c;  
            }  
  
            tempStringBuffer.append(add);  
            String temp = tempStringBuffer.toString();  
            float width = getPaint().measureText(temp.toString());  
            if(isLine)  
                break;  
            if (width <= MaxWidth) {  
  
                trueStringBuffer.append(add);  
            } else {  
                break;  
            }  
  
        }  
  
        return trueStringBuffer.toString();  
  
    }  

    int getNextSpecePlace(int i, String str) {  
  
        for (int j = i; j < str.length(); j++) {  
            char c = str.charAt(j);  
            if (c == '\n') {  
  
                return j;  
            }  
        }  
        return -1;  
    }
    public void ScollTextForList(List list){
        mList=list;
        reset();

    }
    public void generateTextList(int MaxWidth) {
        lineStrings = new ArrayList<String>();
       // String remain = scrollText;
        String[] contenx = scrollText.split("\n");                   //这里的"\n" 是标志
        for (String string : contenx) {
               lineStrings.add(string);
        }
    }
  
    @Override  
    protected void onDraw(Canvas canvas) {
        Resources resources = this.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        float drawX =dm.widthPixels/3-20;
        super.onDraw(canvas);  
        float x = getPaddingLeft();  
        float y = getPaddingTop();

        float lineHeight = getLineHeight();  
        float textSize = getPaint().getTextSize();  
  
        for (int i = 0; i < lineStrings.size(); i++) {  
            y = lineHeight * i + textSize + currentY;
            String s = lineStrings.get(i);
            String[] split = s.split("--");
            float min = 0;  
            if (exactlyHeight > -1) {  
                min = Math.min(min, exactlyHeight - absloutHeight);  
            }  
            if (y < min) {  
  
                y = y + absloutHeight;  
  
            } else if (y >= min && y < textSize + min) {
                for (int i1 = 0; i1 < split.length; i1++) {
                    if (i1==1){
                        TextPaint paint = getPaint();
                        paint.setColor(Color.RED);
                        canvas.drawText(split[i1], drawX, y + absloutHeight, paint);
                    }else{
                        TextPaint paint = getPaint();
                        paint.setColor(Color.BLACK);
                        canvas.drawText(split[i1], drawX*i1, y + absloutHeight, paint);
                    }
                }
//                canvas.drawText(split[2], x, y + absloutHeight, getPaint());
            }  
            if (y >= absloutHeight) {
                for (int i1 = 0; i1 < split.length; i1++) {
                    if (i1==1){
                        TextPaint paint = getPaint();
                        paint.setColor(Color.RED);
                        canvas.drawText(split[i1], drawX, y , paint);
                    }else{
                        TextPaint paint = getPaint();
                        paint.setColor(Color.BLACK);
                        canvas.drawText(split[i1], drawX*i1, y, paint);
                    }
                }
//                canvas.drawText(lineStrings.get(i), x, y, getPaint());
                y = y - absloutHeight;  
            }
            for (int i1 = 0; i1 < split.length; i1++) {
                if (i1==1){
                    TextPaint paint = getPaint();
                    paint.setColor(Color.RED);
                    canvas.drawText(split[i1], drawX, y, paint);
                }else{
                    TextPaint paint = getPaint();
                    paint.setColor(Color.BLACK);
                    canvas.drawText(split[i1], drawX*i1, y , paint);
                }
            }

//            canvas.drawText(lineStrings.get(i), x, y, getPaint());
        }  
    }
    private static final boolean isChinese(char c) {  
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);  
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS  
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A  
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION  
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION  
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {  
            return true;  
        }  
        return false;  
    }  
  
}  