# 每周简单自定义 View (1)
之前看文章无数, 但觉得这方面的进步还是比较慢, 
遂制定每周一个简单的自定义控件的决定 ( 这里只阐述思路 ) .   
废话不说看效果:  
![](http://i.imgur.com/dAKHvWc.gif)

<!-- more -->

### 分析

#### 先进行静态控件的实现
可以看出控件大致分为三部分
1.	底部的灰色背景
2.	红色的选快
3.	提示的文字

### 开始动手
由于本人目前能力有限 onMeasure() 我只是取了 MeasureSpec.EXACTLY 模式来获取控件的大小

### 好开始

1. 自定义控件集成 View 重写方法
	1. onMeasure() //测量控件的大小
	2. onSizeChanged() //获取控件的最终大小
	3. onDraw()	//绘制控件
2. 重写 onMeasure() 方法获取布局文件中控件定义的宽高,最后设置setMeasuredDimension(width, height);
3. 重写 onSizeChanged() 获取控件最终的大小

#### 先绘制一个静态的控件
1. 重写 onDraw() 开始绘制静态的控件,由于红色的滑块需要在文字下滑动所以绘制的流程如下:

	1. 绘制灰色的背景
		-	绘制一个控件一样大的圆角矩形
	2. 绘制红色的滑块
		-	绘制一个控件一半大的圆角矩形
	3. 绘制提示的文字
		-	测量文本大小
		-	绘制文本到控件的左右两端的中间
#### 分析动态效果和实现
1. 效果
	-	滑块的位移动画
		-	总共两个动画, 向左位移, 向右位移, 这里使用 ValueAnimator 实现位移
		-	重写 onTouchEvent() 
			-	ACTION_DOWN
				-	判断按的是左边还是右边
			-	ACTION_UP
				-	判断抬起是在左边还是右边
				-	如果按下和抬起方向不一致,代表操作无效
				-	如果一致开启动画
	-	点击时的文字变色效果
		-	通过 ACTION_DOWN 和 ACTION_UP 修改设置的 flag 并触发 invalidate();
		-	在 onDraw() 根据不同的 flag 使用不同的 paint 绘制文字

#### 事件
1.	当滑块要向左,或向右时提供一个回调
	-	在外面定义一个接口
	-	并在自定义View设置该接口的属性 并提供 setter 方法
	-	在ACTION_UP 中触发事件

### 其他
1.	//获取文本的大小的方法
```
    Rect leftRect = new Rect();
    textPaint.getTextBounds(leftString, 0, leftString.length(), leftRect);
```
2.	//sp 转 px 的方法
```
	private int spToPx(int textSize) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                textSize, getResources().getDisplayMetrics());
    }
```
