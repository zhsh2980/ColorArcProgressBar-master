package com.shinelw.colorarcprogressbar;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import java.text.DecimalFormat;

/**
 * colorful arc progress bar
 * Created by shinelw on 12/4/15.
 * update by zhangshan on 16/06/13.
 */
public class ColorArcProgressBar extends View {

    private Context context;
    private int mWidth;
    private int mHeight;
    private int diameter;  //直径
    private float centerX;  //圆心X坐标
    private float centerY;  //圆心Y坐标

    private Paint allBackgroundArcPaint;//圆圈需要背景
    private Paint allArcPaint;
    private Paint allArcPaint2;
    private Paint progressPaint;
    private Paint vTextPaint;
    private Paint vKmPaint;
    private Paint hintPaint;
    private Paint degreePaint;
    private Paint curSpeedPaint;

    private RectF bgRect;
    private RectF bgRect2;

    private ValueAnimator progressAnimator;
    private PaintFlagsDrawFilter mDrawFilter;
    private SweepGradient sweepGradient;
    private Matrix rotateMatrix;

    private float startAngle = 135;
    private float sweepAngle = 270;
    private float currentAngle = 0;
    private float lastAngle;
    private int[] colors = new int[]{Color.GREEN, Color.YELLOW, Color.RED, Color.RED};
    private float maxValues = 60;
    private float curValues = 0;
    private float radius_size = 0;
    private float innerOuterSpace = 0;
    private float upSpaceBetweenTitleContent = 0;
    private float upSpaceBetweenContentUnit = 0;
    private float bgArcWidth = dipToPx(2);
    private float progressWidth = dipToPx(10);
    private float textSize = dipToPx(60);
    private float hintSize = dipToPx(15);
    private float curSpeedSize = dipToPx(13);
    private int aniSpeed = 1000;
    private float longdegree = dipToPx(13);
    private float shortdegree = dipToPx(5);
    private final int DEGREE_PROGRESS_DISTANCE = dipToPx(8);

    private String hintColor = "#676767";
    private String longDegreeColor = "#111111";
    private String shortDegreeColor = "#111111";
    private String whiteColor = "#FFFFFF";
    private String blackColor = "#000000";
    private String bgArcColor = "#fab15a";
    private String titleString;
    private String contentFontStyle;//字体类型
    private String hintString;

    private boolean isShowCurrentSpeed = true;
    private boolean isNeedTitle;
    private boolean isNeedUnit;
    private boolean isNeedDial;
    private boolean isNeedKm;
    private boolean isNeedBitmap;
    private boolean isContentFontStyle;
    private boolean isFillWithColor;//是否需要颜色填充
    private boolean isStartFrom12;//进度条起点是否在12点位置
    private boolean isNeedContent;

    // sweepAngle / maxValues 的值
    private float k;

    public ColorArcProgressBar(Context context) {
        super(context, null);
        this.context = context;
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        this.context = context;
        initCofig(context, attrs);
        initView();
    }

    public ColorArcProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initCofig(context, attrs);
        initView();
    }

    /**
     * 初始化布局配置
     *
     * @param context
     * @param attrs
     */
    int backColor;//进度条背景环颜色
    int backgroundFillColor;//进度条内部背景颜色

    int string_title_color;//进度条标题颜色
    float string_title_text_size; //进度条标题字体大小

    int string_content_color;//中间内容颜色
    float string_content_text_size; //中间内容字体大小

    int string_unit_color;//底部标题颜色
    float string_unit_text_size; //底部标题字体大小

    private void initCofig(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable
                .ColorArcProgressBar);
        int color1 = a.getColor(R.styleable.ColorArcProgressBar_front_color1, Color
                .GREEN);
        int color2 = a.getColor(R.styleable.ColorArcProgressBar_front_color2, color1);
        int color3 = a.getColor(R.styleable.ColorArcProgressBar_front_color3, color1);
        colors = new int[]{color1, color2, color3, color3};

        //进度条背景环颜色
        backColor = a.getColor(R.styleable.ColorArcProgressBar_back_color, Color
                .parseColor(longDegreeColor));
        //进度条背景环颜色
        backgroundFillColor = a.getColor(R.styleable
                .ColorArcProgressBar_background_fill_color, Color.parseColor
                (longDegreeColor));

        //进度条标题颜色
        string_title_color = a.getColor(R.styleable
                .ColorArcProgressBar_string_title_color, Color.parseColor
                (shortDegreeColor));
        //进度条标题字体大小
        string_title_text_size = a.getDimension(R.styleable
                .ColorArcProgressBar_string_title_size, textSize / 2);//默认60

        //中间内容颜色
        string_content_color = a.getColor(R.styleable
                .ColorArcProgressBar_string_content_color, Color.parseColor
                (blackColor));
        //进度条中间内容字体大小
        string_content_text_size = a.getDimension(R.styleable
                .ColorArcProgressBar_string_content_size, textSize);//默认60

        //底部内容颜色
        string_unit_color = a.getColor(R.styleable
                .ColorArcProgressBar_string_unit_color, Color.parseColor
                (shortDegreeColor));
        //底部内容字体大小
        string_unit_text_size = a.getDimension(R.styleable
                .ColorArcProgressBar_string_unit_size, textSize / 2);//默认60


        sweepAngle = a.getInteger(R.styleable.ColorArcProgressBar_total_engle, 270);
        bgArcWidth = a.getDimension(R.styleable.ColorArcProgressBar_back_width, dipToPx
                (2));
        progressWidth = a.getDimension(R.styleable.ColorArcProgressBar_front_width,
                dipToPx(10));
        isNeedTitle = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_title, false);
        isNeedContent = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_content,
                false);
        isNeedUnit = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_unit, false);
        isNeedDial = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_dial, false);
        isNeedKm = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_km, false);
        isNeedBitmap = a.getBoolean(R.styleable.ColorArcProgressBar_is_need_bitmap,
                false);
        isContentFontStyle = a.getBoolean(R.styleable
                .ColorArcProgressBar_is_content_font_style, false);
        isFillWithColor = a.getBoolean(R.styleable
                .ColorArcProgressBar_is_fill_with_color, false);
        isStartFrom12 = a.getBoolean(R.styleable
                .ColorArcProgressBar_is_start_from_12, false);
        if (isStartFrom12) {
            //进度条起点12点位置
            startAngle = -90;
        }
        hintString = a.getString(R.styleable.ColorArcProgressBar_string_unit);
        titleString = a.getString(R.styleable.ColorArcProgressBar_string_title);
        contentFontStyle = a.getString(R.styleable
                .ColorArcProgressBar_content_font_style);
        curValues = a.getFloat(R.styleable.ColorArcProgressBar_current_value, 0);
        //进度条直径变小
        radius_size = a.getFloat(R.styleable.ColorArcProgressBar_radius_size, 0);

        //进度条内外圈距离
        innerOuterSpace = a.getFloat(R.styleable.ColorArcProgressBar_inner_outer_space,
                0);

        //标题和内容的间距
        upSpaceBetweenTitleContent = a.getFloat(R.styleable
                .ColorArcProgressBar_up_space_between_title_content, 0);
        //内容和底部标题的间距
        upSpaceBetweenContentUnit = a.getFloat(R.styleable
                .ColorArcProgressBar_up_space_between_content_unit, 0);

        maxValues = a.getFloat(R.styleable.ColorArcProgressBar_max_value, 60);
        setCurrentValues(curValues);
        setMaxValues(maxValues);
        a.recycle();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (2 * longdegree + progressWidth + diameter + 2 *
                DEGREE_PROGRESS_DISTANCE);
        int height = (int) (2 * longdegree + progressWidth + diameter + 2 *
                DEGREE_PROGRESS_DISTANCE);
        setMeasuredDimension(width, height);
    }

    private void initView() {

        //diameter = 3 * getScreenWidth() / 5;
        diameter = dipToPx(radius_size);//直径
        //弧形的矩阵区域
        bgRect = new RectF();
        bgRect.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE;
        bgRect.right = diameter + (longdegree + progressWidth / 2 +
                DEGREE_PROGRESS_DISTANCE);
        bgRect.bottom = diameter + (longdegree + progressWidth / 2 +
                DEGREE_PROGRESS_DISTANCE);
        //// TODO: 16/6/13  内外圈差 OK
        bgRect2 = new RectF();
        bgRect2.top = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE -
                dipToPx(innerOuterSpace);
        bgRect2.left = longdegree + progressWidth / 2 + DEGREE_PROGRESS_DISTANCE -
                dipToPx(innerOuterSpace);
        bgRect2.right = diameter + (longdegree + progressWidth / 2 +
                DEGREE_PROGRESS_DISTANCE) + dipToPx(innerOuterSpace);
        bgRect2.bottom = diameter + (longdegree + progressWidth / 2 +
                DEGREE_PROGRESS_DISTANCE) + dipToPx(innerOuterSpace);

        //圆心
        centerX = (2 * longdegree + progressWidth + diameter + 2 *
                DEGREE_PROGRESS_DISTANCE) / 2;
        centerY = (2 * longdegree + progressWidth + diameter + 2 *
                DEGREE_PROGRESS_DISTANCE) / 2;

        //外部刻度线
        degreePaint = new Paint();
        degreePaint.setColor(Color.parseColor(longDegreeColor));

        //整个弧形的背景
        allBackgroundArcPaint = new Paint();
        if (isFillWithColor) {
            allBackgroundArcPaint.setStyle(Paint.Style.FILL);//设置实心
            allBackgroundArcPaint.setColor(backgroundFillColor);
            allBackgroundArcPaint.setAntiAlias(true);
            allBackgroundArcPaint.setStrokeCap(Paint.Cap.ROUND);
        }
        //整个弧形
        allArcPaint = new Paint();
        allArcPaint.setStyle(Paint.Style.STROKE);
        allArcPaint.setAntiAlias(true);
        allArcPaint.setStrokeWidth(bgArcWidth);
        //原来的  allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint.setColor(backColor);
        allArcPaint.setStrokeCap(Paint.Cap.ROUND);

        //尝试在弧形头画一个大的圆点 尝试初步失败
        allArcPaint2 = new Paint();
        allArcPaint2.setAntiAlias(true);
        allArcPaint2.setStyle(Paint.Style.STROKE);
        allArcPaint2.setStrokeWidth(bgArcWidth);
        //原来的  allArcPaint.setColor(Color.parseColor(bgArcColor));
        allArcPaint2.setColor(backColor);
        allArcPaint2.setStrokeCap(Paint.Cap.ROUND);

        //当前进度的弧形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.STROKE);
        progressPaint.setStrokeCap(Paint.Cap.ROUND);
        progressPaint.setStrokeWidth(progressWidth);
        progressPaint.setColor(Color.GREEN);


        //显示标题文字
        curSpeedPaint = new Paint();
        //curSpeedPaint.setTextSize(curSpeedSize);
        //curSpeedPaint.setColor(Color.parseColor(hintColor));
        curSpeedPaint.setTextSize(string_title_text_size);
        curSpeedPaint.setColor(string_title_color);
        curSpeedPaint.setTextAlign(Paint.Align.CENTER);
        curSpeedPaint.setFakeBoldText(true);

        //内容显示文字
        vTextPaint = new Paint();
        vTextPaint.setTextSize(string_content_text_size);
        vTextPaint.setColor(string_content_color);
        if (isContentFontStyle) {
            //是否自定义字体
            if (!contentFontStyle.equals(null)) {//自定义哪种字体
                try {
                    String fongUrl_bold = "fonts/" + contentFontStyle + ".ttf";
                    //Logger.d("fongUrl_bold : " + fongUrl_bold);
                    vTextPaint.setTypeface(Typeface.createFromAsset(context.getAssets()
                            , fongUrl_bold));
                } catch (Exception e) {
                    Log.i("bro", "字体设置异常");
                }

            }
        }
        vTextPaint.setTextAlign(Paint.Align.CENTER);

        vTextPaint.setFakeBoldText(true);

        //显示单位(km)文字
        vKmPaint = new Paint();
        vKmPaint.setTextSize(string_content_text_size / 3);
        vKmPaint.setColor(string_content_color);
        //left往右偏移  奇怪
        vKmPaint.setTextAlign(Paint.Align.LEFT);

        //显示文字单位
        hintPaint = new Paint();
        hintPaint.setTextSize(string_unit_text_size);
        hintPaint.setColor(string_unit_color);
        hintPaint.setTextAlign(Paint.Align.CENTER);
        hintPaint.setFakeBoldText(true);


        mDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint
                .FILTER_BITMAP_FLAG);
        sweepGradient = new SweepGradient(centerX, centerY, colors, null);
        rotateMatrix = new Matrix();

    }

    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home_circle);

    @Override
    protected void onDraw(Canvas canvas) {
        //抗锯齿
        canvas.setDrawFilter(mDrawFilter);

        if (isNeedDial) {
            //画刻度线
            for (int i = 0; i < 40; i++) {
                if (i > 15 && i < 25) {
                    canvas.rotate(9, centerX, centerY);
                    continue;
                }
                if (i % 5 == 0) {
                    degreePaint.setStrokeWidth(dipToPx(2));
                    degreePaint.setColor(Color.parseColor(longDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2
                                    - DEGREE_PROGRESS_DISTANCE,
                            centerX, centerY - diameter / 2 - progressWidth / 2 -
                                    DEGREE_PROGRESS_DISTANCE -
                                    longdegree, degreePaint);
                } else {
                    degreePaint.setStrokeWidth(dipToPx(1.4f));
                    degreePaint.setColor(Color.parseColor(shortDegreeColor));
                    canvas.drawLine(centerX, centerY - diameter / 2 - progressWidth / 2
                                    - DEGREE_PROGRESS_DISTANCE -
                                    (longdegree - shortdegree) / 2,
                            centerX, centerY - diameter / 2 - progressWidth / 2 -
                                    DEGREE_PROGRESS_DISTANCE -
                                    (longdegree - shortdegree) / 2 - shortdegree,
                            degreePaint);
                }
                canvas.rotate(9, centerX, centerY);
            }
        }

        //整个弧
        if (isFillWithColor) {
            //如果内部填充 , 画一个实心和一个空心
            canvas.drawArc(bgRect, startAngle, sweepAngle, false, allBackgroundArcPaint);
            canvas.drawArc(bgRect2, startAngle, sweepAngle, false, allArcPaint);
        } else {
            //内部无填充 , 直接画一个弧
            canvas.drawArc(bgRect, startAngle, sweepAngle, false, allArcPaint);
        }

        //设置渐变色
        rotateMatrix.setRotate(130, centerX, centerY);
        sweepGradient.setLocalMatrix(rotateMatrix);
        progressPaint.setShader(sweepGradient);

        //当前进度
        canvas.drawArc(bgRect2, startAngle, currentAngle, false, progressPaint);
        // TODO: 16/6/22  画进度上的一个小点(图片)  OK
        if (isNeedBitmap) {
            Matrix matrix = new Matrix();
            matrix.postTranslate(diameter / 2 + 2 * DEGREE_PROGRESS_DISTANCE, 2 *
                    DEGREE_PROGRESS_DISTANCE);
            matrix.postRotate(currentAngle, centerX, centerY);
            canvas.drawBitmap(bitmap, matrix, null);
        }

        if (isNeedTitle) {
            canvas.drawText(titleString, centerX, centerY - 2 * textSize / 3 - dipToPx
                    (upSpaceBetweenTitleContent), curSpeedPaint);
        }
        if (isNeedContent) {
            if (isNeedKm) {
                if (curValues >= 1000) {
                    //如果数值大于1000,那么换算成保留一位小数
                    DecimalFormat df = new DecimalFormat("0.0");
                    String format = df.format(curValues / 1000);
                    //加上单位的时候数字要左移
                    canvas.drawText(format, centerX - 50, centerY + textSize / 3,
                            vTextPaint);
                } else {
                    canvas.drawText(String.format("%.0f", curValues), centerX - 50,
                            centerY + textSize / 3, vTextPaint);
                }
            } else {
                canvas.drawText(String.format("%.0f", curValues), centerX, centerY +
                        textSize / 3, vTextPaint);
            }
        }
        if (isNeedKm) {
            if (curValues >= 1000) {
                canvas.drawText("  km", centerX + 65, centerY + textSize / 3, vKmPaint);
            } else {
                //目的是防止数字和单位重复在一起 方法:加空格
                if (curValues > 100 && curValues <= 999) {
                    canvas.drawText("  m", centerX + 65, centerY + textSize / 3,
                            vKmPaint);
                } else {
                    canvas.drawText("m", centerX + 65, centerY + textSize / 3, vKmPaint);
                }
            }
        }

        if (isNeedUnit) {
            canvas.drawText(hintString, centerX, centerY + 2 * textSize / 3 + dipToPx
                    (upSpaceBetweenContentUnit), hintPaint);
        }

        invalidate();

    }

    /**
     * 设置最大值
     *
     * @param maxValues
     */
    public void setMaxValues(float maxValues) {
        this.maxValues = maxValues;
        k = sweepAngle / maxValues;
    }

    /**
     * 设置当前值
     *
     * @param currentValues
     */
    public void setCurrentValues(float currentValues) {
        if (currentValues > maxValues) {
            currentValues = maxValues;
        }
        if (currentValues < 0) {
            currentValues = 0;
        }
        this.curValues = currentValues;
        lastAngle = currentAngle;
        setAnimation(lastAngle, currentValues * k, aniSpeed);
    }

    /**
     * 设置整个圆弧宽度
     *
     * @param bgArcWidth
     */
    public void setBgArcWidth(int bgArcWidth) {
        this.bgArcWidth = bgArcWidth;
    }

    /**
     * 设置进度宽度
     *
     * @param progressWidth
     */
    public void setProgressWidth(int progressWidth) {
        this.progressWidth = progressWidth;
    }

    /**
     * 设置速度文字大小
     *
     * @param textSize
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置单位文字大小
     *
     * @param hintSize
     */
    public void setHintSize(int hintSize) {
        this.hintSize = hintSize;
    }

    /**
     * 设置单位文字
     *
     * @param hintString
     */
    public void setUnit(String hintString) {
        this.hintString = hintString;
        invalidate();
    }

    /**
     * 设置直径大小
     *
     * @param diameter
     */
    public void setDiameter(int diameter) {
        this.diameter = dipToPx(diameter);
    }

    /**
     * 设置标题
     *
     * @param title
     */
    private void setTitle(String title) {
        this.titleString = title;
    }

    /**
     * 设置是否显示标题
     *
     * @param isNeedTitle
     */
    private void setIsNeedTitle(boolean isNeedTitle) {
        this.isNeedTitle = isNeedTitle;
    }

    /**
     * 设置是否显示单位文字
     *
     * @param isNeedUnit
     */
    private void setIsNeedUnit(boolean isNeedUnit) {
        this.isNeedUnit = isNeedUnit;
    }

    /**
     * 设置是否显示外部刻度盘
     *
     * @param isNeedDial
     */
    private void setIsNeedDial(boolean isNeedDial) {
        this.isNeedDial = isNeedDial;
    }

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(float last, float current, int length) {
        progressAnimator = ValueAnimator.ofFloat(last, current);
        progressAnimator.setDuration(length);
        progressAnimator.setTarget(currentAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                currentAngle = (float) animation.getAnimatedValue();
                curValues = currentAngle / k;
            }
        });
        progressAnimator.start();
    }

    /**
     * dip 转换成px
     *
     * @param dip
     * @return
     */
    private int dipToPx(float dip) {
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * density + 0.5f * (dip >= 0 ? 1 : -1));
    }

    /**
     * 得到屏幕宽度
     *
     * @return
     */
    private int getScreenWidth() {
        WindowManager windowManager = (WindowManager) getContext().getSystemService
                (Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }
}

