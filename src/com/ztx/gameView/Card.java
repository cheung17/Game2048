package com.ztx.gameView;

import com.ztx.game2048.R;
import com.ztx.game2048.R.drawable;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import android.widget.TextView;

public class Card extends FrameLayout {
	private int num;
	TextView tv;

	public Card(Context context) { // 构造方法
		super(context);
		tv = new TextView(getContext()); // 每创建一个卡片就new一个textView
		tv.setTextSize(40);
		tv.setTypeface(Typeface.MONOSPACE);
		tv.setTextColor(Color.parseColor("#CCCCCC"));
		tv.setGravity(Gravity.CENTER);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		lp.setMargins(10, 10, 0, 0);// 设置左上间隔10dp
		setNum(0);
		addView(tv, lp);// 义lp布局方式添加view
		Animation ani = setAnimation();
		tv.startAnimation(ani);
	}

	public Animation setAnimation() {
		Animation animation = new ScaleAnimation(0, 1, 0, 1, // 起始大小 终止大小
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f, // 相对位置 中心位置
				ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		animation.setDuration(300);
		animation.setFillAfter(false);
		return animation;
	}

	public int getNum() {

		return num;
	}

	public void setNum(int num) {
		this.num = num;
		if (num <= 0) {

			tv.setText(""); // 数字为0则不显示
			tv.setBackgroundColor(0x33ffffff);
		} else {

			tv.setText(num + "");

			tv.setBackgroundResource(R.drawable.button_bg);

		}

	}

	public boolean isEqu(Card c) {

		return getNum() == c.getNum();
	}

}
