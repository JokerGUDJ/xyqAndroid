package com.xej.xhjy.common.view.draggridview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.LinkedList;
import java.util.List;

public class DragGridView extends GridView {
    /**
     * DragGridView的item长按响应的时间， 默认是1000毫秒
     */
    private long dragResponseMS = 1000;

    /**
     * 是否可以拖拽，默认不可以
     */
    private boolean isDrag = false;

    private int mDownX;
    private int mDownY;
    private int moveX;
    private int moveY;
    /**
     * 正在拖拽的position
     */
    private int mDragPosition;

    /**
     * 刚开始拖拽的item对应的View
     */
    private View mStartDragItemView = null;

    /**
     * 用于拖拽的镜像，这里直接用一个ImageView
     */
    private ImageView mDragImageView;


    private WindowManager mWindowManager;
    /**
     * item镜像的布局参数
     */
    private WindowManager.LayoutParams mWindowLayoutParams;

    /**
     * 我们拖拽的item对应的Bitmap
     */
    private Bitmap mDragBitmap;

    /**
     * 按下的点到所在item的上边缘的距离
     */
    private int mPoint2ItemTop;

    /**
     * 按下的点到所在item的左边缘的距离
     */
    private int mPoint2ItemLeft;

    /**
     * DragGridView距离屏幕顶部的偏移量
     */
    private int mOffset2Top;

    /**
     * DragGridView距离屏幕左边的偏移量
     */
    private int mOffset2Left;

    /**
     * 状态栏的高度
     */
    private int mStatusHeight;

    /**
     * DragGridView自动向下滚动的边界值
     */
    private int mDownScrollBorder;

    /**
     * DragGridView自动向上滚动的边界值
     */
    private int mUpScrollBorder;

    /**
     * DragGridView自动滚动的速度
     */
    private static final int speed = 20;
    /**
     * item的移动动画是否结束
     */
    private boolean mAnimationEnd = true;

    private DragGridBaseAdapter mDragAdapter;
    /**
     * GridView的列数
     */
    private int mNumColumns;
    /**
     * 当GridView的numColumns设置为AUTO_FIT，我们需要计算GirdView具体的列数
     */
    private int mColumnWidth;
    /**
     * GridView是否设置了numColumns为具体的数字
     */
    private boolean mNumColumnsSet;
    private int mHorizontalSpacing;
    private int mVerticalSpacing;

    /**
     * 最后一个条目或最后两个条目不能拖动
     *
     * @author tao_bp
     */
    public enum DisableItems {
        Last, LastTwo
    }

    ;
    private DisableItems mDisableItems;

    public void setDisableItems(DisableItems disableItems) {
        this.mDisableItems = disableItems;
    }

    public DisableItems getDisableItems() {
        return this.mDisableItems;
    }

    public DragGridView(Context context) {
        this(context, null);
    }

    public DragGridView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DragGridView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mStatusHeight = getStatusHeight(context); //获取状态栏的高度

        if (!mNumColumnsSet) {
            mNumColumns = AUTO_FIT;
        }

    }

    /**
     * 删除item的动画效果
     *
     * @param position
     */
    public void removeItemAnimation(final int position) {
        mDragAdapter.removeItem(position);
        final ViewTreeObserver observer = getViewTreeObserver();
        observer.addOnPreDrawListener(new OnPreDrawListener() {

            @Override
            public boolean onPreDraw() {
                observer.removeOnPreDrawListener(this);
                animateReorder(position, getLastVisiblePosition() + 1);
                return true;
            }
        });
    }

    private Handler mHandler = new Handler();

    //用来处理是否为长按的Runnable
    private Runnable mLongClickRunnable = new Runnable() {

        @Override
        public void run() {
            isDrag = true; //设置可以拖拽
            mStartDragItemView.setVisibility(View.INVISIBLE);//隐藏该item

            //根据我们按下的点显示item镜像
            createDragImage(mDragBitmap, mDownX, mDownY);
        }
    };


    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);

        if (adapter instanceof DragGridBaseAdapter) {
            mDragAdapter = (DragGridBaseAdapter) adapter;
        } else {
            throw new IllegalStateException("the adapter must be implements DragGridAdapter");
        }
    }

    /**
     * 获取列数
     */
    @Override
    public void setNumColumns(int numColumns) {
        super.setNumColumns(numColumns);
        mNumColumnsSet = true;
        this.mNumColumns = numColumns;
    }

    /**
     * 获取设置的列宽
     */
    @Override
    public void setColumnWidth(int columnWidth) {
        super.setColumnWidth(columnWidth);
        mColumnWidth = columnWidth;
    }

    /**
     * 获取水平方向的间隙
     */
    @Override
    public void setHorizontalSpacing(int horizontalSpacing) {
        super.setHorizontalSpacing(horizontalSpacing);
        this.mHorizontalSpacing = horizontalSpacing;
    }


    /**
     * 获取竖直方向的间隙
     */
    @Override
    public void setVerticalSpacing(int verticalSpacing) {
        super.setVerticalSpacing(verticalSpacing);
        this.mVerticalSpacing = verticalSpacing;
    }

    /**
     * 若列数设置为AUTO_FIT，我们在这里面计算具体的列数
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mNumColumns == AUTO_FIT) {
            int numFittedColumns;
            if (mColumnWidth > 0) {
                int gridWidth = Math.max(MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft()
                        - getPaddingRight(), 0);
                numFittedColumns = gridWidth / mColumnWidth;
                if (numFittedColumns > 0) {
                    while (numFittedColumns != 1) {
                        if (numFittedColumns * mColumnWidth + (numFittedColumns - 1)
                                * mHorizontalSpacing > gridWidth) {
                            numFittedColumns--;
                        } else {
                            break;
                        }
                    }
                } else {
                    numFittedColumns = 1;
                }
            } else {
                numFittedColumns = 2;
            }
            mNumColumns = numFittedColumns;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置响应拖拽的毫秒数，默认是1000毫秒
     *
     * @param dragResponseMS
     */
    public void setDragResponseMS(long dragResponseMS) {
        this.dragResponseMS = dragResponseMS;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                try {
                    mDownX = (int) ev.getX();
                    mDownY = (int) ev.getY();

                    //根据按下的X,Y坐标获取所点击item的position
                    mDragPosition = pointToPosition(mDownX, mDownY);

                    // 设置最后一个或最后两个item不能拖动
                    if (this.mDisableItems == DisableItems.Last) {
                        int disdragPosition = mDragAdapter.getItemsCount() - 1;
                        if (mDragPosition == AdapterView.INVALID_POSITION
                                || mDragPosition == disdragPosition) {
                            return super.dispatchTouchEvent(ev);
                        }
                    } else if (this.mDisableItems == DisableItems.LastTwo) {
                        int disdragPosition1 = mDragAdapter.getItemsCount() - 1;
                        int disdragPosition2 = mDragAdapter.getItemsCount() - 2;
                        if (mDragPosition == AdapterView.INVALID_POSITION
                                || mDragPosition == disdragPosition1
                                || mDragPosition == disdragPosition2) {
                            return super.dispatchTouchEvent(ev);
                        }
                    }

                    //使用Handler延迟dragResponseMS执行mLongClickRunnable
                    mHandler.postDelayed(mLongClickRunnable, dragResponseMS);

                    //根据position获取该item所对应的View
                    mStartDragItemView = getChildAt(mDragPosition - getFirstVisiblePosition());

                    //下面这几个距离大家可以参考我的博客上面的图来理解下
                    mPoint2ItemTop = mDownY - mStartDragItemView.getTop();
                    mPoint2ItemLeft = mDownX - mStartDragItemView.getLeft();

                    mOffset2Top = (int) (ev.getRawY() - mDownY);
                    mOffset2Left = (int) (ev.getRawX() - mDownX);

                    //获取DragGridView自动向上滚动的偏移量，小于这个值，DragGridView向下滚动
                    mDownScrollBorder = getHeight() / 5;
                    //获取DragGridView自动向下滚动的偏移量，大于这个值，DragGridView向上滚动
                    mUpScrollBorder = getHeight() * 4 / 5;


                    //开启mDragItemView绘图缓存
                    mStartDragItemView.setDrawingCacheEnabled(true);
                    //获取mDragItemView在缓存中的Bitmap对象
                    mDragBitmap = Bitmap.createBitmap(mStartDragItemView.getDrawingCache());
                    //这一步很关键，释放绘图缓存，避免出现重复的镜像
                    mStartDragItemView.destroyDrawingCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                try {
                    int moveX = (int) ev.getX();
                    int moveY = (int) ev.getY();

                    //如果我们在按下的item上面移动，只要不超过item的边界我们就不移除mRunnable
                    if (!isTouchInItem(mStartDragItemView, moveX, moveY)) {
                        mHandler.removeCallbacks(mLongClickRunnable);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case MotionEvent.ACTION_UP:
                try {
                    mHandler.removeCallbacks(mLongClickRunnable);
                    mHandler.removeCallbacks(mScrollRunnable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 是否点击在GridView的item上面
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isTouchInItem(View dragView, int x, int y) {
        if (dragView == null) {
            return false;
        }
        int leftOffset = dragView.getLeft();
        int topOffset = dragView.getTop();
        if (x < leftOffset || x > leftOffset + dragView.getWidth()) {
            return false;
        }

        if (y < topOffset || y > topOffset + dragView.getHeight()) {
            return false;
        }

        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isDrag && mDragImageView != null) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_MOVE:
                    moveX = (int) ev.getX();
                    moveY = (int) ev.getY();

                    //拖动item
                    onDragItem(moveX, moveY);
                    break;
                case MotionEvent.ACTION_UP:
                    onStopDrag();
                    isDrag = false;
                    break;
            }
            return true;
        }
        return super.onTouchEvent(ev);
    }


    /**
     * 创建拖动的镜像
     *
     * @param bitmap
     * @param downX  按下的点相对父控件的X坐标
     * @param downY  按下的点相对父控件的X坐标
     */
    private void createDragImage(Bitmap bitmap, int downX, int downY) {
        mWindowLayoutParams = new WindowManager.LayoutParams();
        mWindowLayoutParams.format = PixelFormat.TRANSLUCENT; //图片之外的其他地方透明
        mWindowLayoutParams.gravity = Gravity.TOP | Gravity.LEFT;
        mWindowLayoutParams.x = downX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = downY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowLayoutParams.alpha = 0.55f; //透明度
        mWindowLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mWindowLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;

        mDragImageView = new ImageView(getContext());
        mDragImageView.setImageBitmap(bitmap);
        mWindowManager.addView(mDragImageView, mWindowLayoutParams);
    }

    /**
     * 从界面上面移动拖动镜像
     */
    private void removeDragImage() {
        if (mDragImageView != null) {
            mWindowManager.removeView(mDragImageView);
            mDragImageView = null;
        }
    }

    /**
     * 拖动item，在里面实现了item镜像的位置更新，item的相互交换以及GridView的自行滚动
     */
    private void onDragItem(int moveX, int moveY) {
        mWindowLayoutParams.x = moveX - mPoint2ItemLeft + mOffset2Left;
        mWindowLayoutParams.y = moveY - mPoint2ItemTop + mOffset2Top - mStatusHeight;
        mWindowManager.updateViewLayout(mDragImageView, mWindowLayoutParams); //更新镜像的位置
        onSwapItem(moveX, moveY);

        //GridView自动滚动
        mHandler.post(mScrollRunnable);
    }


    /**
     * 当moveY的值大于向上滚动的边界值，触发GridView自动向上滚动
     * 当moveY的值小于向下滚动的边界值，触发GridView自动向下滚动
     * 否则不进行滚动
     */
    private Runnable mScrollRunnable = new Runnable() {

        @Override
        public void run() {
            int scrollY;
            if (getFirstVisiblePosition() == 0 || getLastVisiblePosition() == getCount() - 1) {
                mHandler.removeCallbacks(mScrollRunnable);
            }

            if (moveY > mUpScrollBorder) {
                scrollY = speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else if (moveY < mDownScrollBorder) {
                scrollY = -speed;
                mHandler.postDelayed(mScrollRunnable, 25);
            } else {
                scrollY = 0;
                mHandler.removeCallbacks(mScrollRunnable);
            }

            smoothScrollBy(scrollY, 10);
        }
    };


    /**
     * 交换item,并且控制item之间的显示与隐藏效果
     *
     * @param moveX
     * @param moveY
     */
    private void onSwapItem(int moveX, int moveY) {
        //获取我们手指移动到的那个item的position
        final int tempPosition = pointToPosition(moveX, moveY);

        // 设置拖动到不可用的item位置时不交换item
        if (this.mDisableItems == DisableItems.Last) {
            int disdragPosition = mDragAdapter.getItemsCount() - 1;
            if (tempPosition == disdragPosition) {
                return;
            }
        } else if (this.mDisableItems == DisableItems.LastTwo) {
            int disdragPosition1 = mDragAdapter.getItemsCount() - 1;
            int disdragPosition2 = mDragAdapter.getItemsCount() - 2;
            if (tempPosition == disdragPosition1 || tempPosition == disdragPosition2) {
                return;
            }
        }

        //假如tempPosition 改变了并且tempPosition不等于-1,则进行交换
        if (tempPosition != mDragPosition && tempPosition != AdapterView.INVALID_POSITION && mAnimationEnd) {
            /**
             * 交换item
             */
            mDragAdapter.reorderItems(mDragPosition, tempPosition);
            /**
             * 设置新到的位置隐藏
             */
            mDragAdapter.setHideItem(tempPosition);

            final ViewTreeObserver observer = getViewTreeObserver();
            observer.addOnPreDrawListener(new OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    observer.removeOnPreDrawListener(this);
                    animateReorder(mDragPosition, tempPosition);
                    mDragPosition = tempPosition;
                    return true;
                }
            });

        }
    }

    /**
     * 创建移动动画
     *
     * @param view
     * @param startX
     * @param endX
     * @param startY
     * @param endY
     * @return
     */
    private AnimatorSet createTranslationAnimations(View view, float startX,
                                                    float endX, float startY, float endY) {
        ObjectAnimator animX = ObjectAnimator.ofFloat(view, "translationX",
                startX, endX);
        ObjectAnimator animY = ObjectAnimator.ofFloat(view, "translationY",
                startY, endY);
        AnimatorSet animSetXY = new AnimatorSet();
        animSetXY.playTogether(animX, animY);
        return animSetXY;
    }


    /**
     * item的交换动画效果
     *
     * @param oldPosition
     * @param newPosition
     */
    private void animateReorder(final int oldPosition, final int newPosition) {
        try {
            boolean isForward = newPosition > oldPosition;
            List<Animator> resultList = new LinkedList<Animator>();
            if (isForward) {
                for (int pos = oldPosition; pos < newPosition; pos++) {
                    View view = getChildAt(pos - getFirstVisiblePosition());
                    if ((pos + 1) % mNumColumns == 0) {
                        resultList.add(createTranslationAnimations(view,
                                -(view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1), 0,
                                view.getHeight() + mVerticalSpacing, 0));
                    } else {
                        resultList.add(createTranslationAnimations(view,
                                view.getWidth() + mHorizontalSpacing, 0, 0, 0));
                    }
                }
            } else {
                for (int pos = oldPosition; pos > newPosition; pos--) {
                    View view = getChildAt(pos - getFirstVisiblePosition());
                    if ((pos) % mNumColumns == 0) {
                        resultList.add(createTranslationAnimations(view,
                                (view.getWidth() + mHorizontalSpacing) * (mNumColumns - 1), 0,
                                -view.getHeight() - mVerticalSpacing, 0));
                    } else {
                        resultList.add(createTranslationAnimations(view,
                                -view.getWidth() - mHorizontalSpacing, 0, 0, 0));
                    }
                }
            }

            AnimatorSet resultSet = new AnimatorSet();
            resultSet.playTogether(resultList);
            resultSet.setDuration(300);
            resultSet.setInterpolator(new AccelerateDecelerateInterpolator());
            resultSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    mAnimationEnd = false;
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mAnimationEnd = true;
                }
            });
            resultSet.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止拖拽将之前隐藏的item显示出来，并将镜像移除
     */
    private void onStopDrag() {
        View view = getChildAt(mDragPosition - getFirstVisiblePosition());
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
        mDragAdapter.setHideItem(-1);
        removeDragImage();
    }

    /**
     * 获取状态栏的高度
     *
     * @param context
     * @return
     */
    private static int getStatusHeight(Context context) {
        int statusHeight = 0;
        Rect localRect = new Rect();
        ((Activity) context).getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        statusHeight = localRect.top;
        if (0 == statusHeight) {
            Class<?> localClass;
            try {
                localClass = Class.forName("com.android.internal.R$dimen");
                Object localObject = localClass.newInstance();
                int i5 = Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
                statusHeight = context.getResources().getDimensionPixelSize(i5);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return statusHeight;
    }

}
