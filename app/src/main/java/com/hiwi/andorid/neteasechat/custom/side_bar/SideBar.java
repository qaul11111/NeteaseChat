package com.hiwi.andorid.neteasechat.custom.side_bar;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by x6 on 2017/1/18.
 */
public class SideBar extends View {

    private ISideBar listener;

    private String[] b = {"A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z", "#"};

    private Paint paint = new Paint();
    private int height;
    private int width;
    private int index = -1;

    public SideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public void setListener(ISideBar listener){
        this.listener = listener;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /**
         *  在onFinishInflate() 之后才能获取宽高
         */
        height = getHeight();                           //获取高

        width = getWidth();                             //获取宽

        int singleHeight = height / b.length;           //每个字母的高度

        for (int i = 0; i < b.length; i++) {

            if(index == i){
                paint.setColor(Color.rgb(70, 171, 255));
            }else{
                paint.setColor(Color.rgb(00, 00, 00));
            }

              //设置文字的颜色
            paint.setTypeface(Typeface.DEFAULT_BOLD);   //粗体
            paint.setAntiAlias(true);                   //光滑
            paint.setTextSize(50);

            float x = (width - paint.measureText(b[i]))/2;
            int y = singleHeight * (i + 1);

            canvas.drawText(b[i], x, y, paint);
            paint.reset();
        }
    }


    /*
     * dispatchTouchEvent 和 onTouchEvent 区别
     * dispatchTouchEvent是用来分发事件的，onTouchEvent是用来处理事件的。
     * 先走dispatchTouchEvent然后走onTouchEvent。
     * View不执行 onInterceptTouchEvent
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        int action = event.getAction();
        float y = event.getY();                     //获取手指在控件上触摸的y坐标
        index =   (int) (y/height*b.length);        // 当前点击的字母

        switch (action) {
            case MotionEvent.ACTION_UP:
                index = -1;
                invalidate();
                if(listener != null){
                    //隐藏弹出字母
                    listener.onTouchUpInVisibility();
                }
                break;
            default:
                if(listener != null){
                    if(index<0 || index >= b.length)return true;
                        listener.onTouchLetterChanged(b[index]);
                }
                invalidate();
                break;
        }
        return true;
    }
}
