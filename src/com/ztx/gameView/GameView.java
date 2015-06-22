package com.ztx.gameView;

import java.util.ArrayList;
import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import com.ztx.game2048.MainActivity;
import com.ztx.game2048.R;

import android.R.color;

import android.R.integer;
import android.R.xml;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class GameView extends GridLayout {
	public static GameView gameView = null;
	private Card[][] cardList = new Card[4][4]; // 定义二维数组纪录卡片
	private List<Point> emptyList = new ArrayList<Point>(); // 存放空卡片

	// MainActivity mainActivity=new MainActivity();
	public GameView(Context context) {

		super(context);
		gameView = this;
		init();
	}

	public static GameView getGameView() { // 生成get方法
		return gameView;
	}

	public GameView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		gameView = this;
		init();
	}

	public GameView(Context context, AttributeSet attrs) {
		super(context, attrs);
		gameView = this;
		init();
	}

	private void init() { // 初始化调用
		// setBackgroundColor(0xffbbada8); //设置背景颜色
		// setBackgroundColor(color.);
		setColumnCount(4);// 设置为显示4列

		setOnTouchListener(new OnTouchListener() { // 用户滑动屏幕时
			private float startX, startY, offSetX, offSetY;

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) { // 得到用户操作方式
				case MotionEvent.ACTION_DOWN: // 用户按下

					startX = event.getX();
					startY = event.getY();
					break;

				case MotionEvent.ACTION_UP:

					offSetX = event.getX() - startX; // 得到x偏移量
					offSetY = event.getY() - startY; // 得到y偏移量

					if (Math.abs(offSetX) > Math.abs(offSetY)) { // 判断是横向滑动是否大于竖向滑动
						if (offSetX > 5) { // 右滑

							slideRight();
						} else if (offSetX < -5) { // 左滑

							slideLeft();
						}
					} else if (Math.abs(offSetX) < Math.abs(offSetY)) { // 竖向滑动大于横向滑动
						if (offSetY > 5) { // 下滑
							slideDown();
						} else if (offSetY < -5) { // 上滑
							slideUp();
						}
					}

					break;
				}

				return true;
			}

		});
	}

	public void startGame() { // 开始游戏
		MainActivity.getMainActivity().clearScre(); // 清空得分

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				cardList[x][y].setNum(0);
			}
		}
		addRandomCard(); // 加卡片
		addRandomCard();

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) { // 得到当前控件宽高
		super.onSizeChanged(w, h, oldw, oldh);
		int cardWidth = (Math.min(w, h) - 10) / 4;
		addCards(cardWidth);// 调用添加卡片
		startGame();

	}

	private void addRandomCard() {
		emptyList.clear();// 清空

		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardList[x][y].getNum() == 0) {
					emptyList.add(new Point(x, y)); // 把没有数字的卡片堆到empty集合
				}
			}

		}
		// 随机移除一个位置为随机数的卡片 并得到它 在下方为其随机填个数

		Point pt = emptyList.remove((int) (Math.random() * emptyList.size()));
		// 得到一个随机数 如果大于0.1则把卡片值设为2,否则为4
		cardList[pt.x][pt.y].setNum(Math.random() > 0.1 ? 2 : 4);
		Animation animation = cardList[pt.x][pt.y].setAnimation();
		cardList[pt.x][pt.y].startAnimation(animation); // 给新卡片弄个动画

	}

	protected void addCards(int cardWidth) { // 添加卡片方法
		Card card;// 声明卡片对象
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				card = new Card(getContext()); // 调用构造方法创建一个卡片
				card.setNum(0); // 设置数字
				addView(card, cardWidth, cardWidth); // 添加view 正方形
				cardList[x][y] = card; // 保存卡片到卡片数组
			}

		}
	}

	private void slideLeft() {

		boolean addNew = false; // 设置一个是否需要添加新卡片标记

		for (int y = 0; y < 4; y++) {

			for (int x = 0; x < 4; x++) {

				for (int x1 = x + 1; x1 < 4; x1++) {
					if (cardList[x1][y].getNum() > 0) { // 如果右边不为空
						if (cardList[x][y].getNum() <= 0) { // 如果当前为空
							cardList[x][y].setNum(cardList[x1][y].getNum()); // 则把右边的值赋给它。。
							cardList[x1][y].setNum(0);// 清空右边
							addNew = true;
							x--;

						} else if (cardList[x][y].isEqu(cardList[x1][y])) { // 如果和右边的值相等
							cardList[x][y].setNum(cardList[x][y].getNum() * 2); // 则乘以2
							Animation animation = cardList[x][y].setAnimation();
							cardList[x][y].startAnimation(animation);
							cardList[x1][y].setNum(0);// 清空右边

							MainActivity.getMainActivity().addScore(
									cardList[x][y].getNum());

							addNew = true; // 可以加卡片
						}
						break;
					}

				}
			}
		}
		if (addNew) {
			addRandomCard();
			gameOver(); // 添加一张随机卡片
		}
	}

	private void slideDown() {
		boolean addNew = false; // 设置一个是否需要添加新卡片标记
		for (int x = 0; x < 4; x++) {

			for (int y = 3; y >= 0; y--) {

				for (int y1 = y - 1; y1 >= 0; y1--) {
					if (cardList[x][y1].getNum() > 0) { // 如果右边不为空
						if (cardList[x][y].getNum() <= 0) { // 如果当前为空
							cardList[x][y].setNum(cardList[x][y1].getNum()); // 则把右边的值赋给它。。
							cardList[x][y1].setNum(0);// 清空右边

							y++;
							addNew = true;
						} else if (cardList[x][y].isEqu(cardList[x][y1])) { // 如果和右边的值相等
							cardList[x][y].setNum(cardList[x][y].getNum() * 2); // 则乘以2
							Animation animation = cardList[x][y].setAnimation();
							cardList[x][y].startAnimation(animation);
							cardList[x][y1].setNum(0);// 清空右边
							MainActivity.getMainActivity().addScore(
									cardList[x][y].getNum());
							addNew = true;

						}
						break;
					}

				}
			}
		}
		if (addNew) {
			addRandomCard();
			gameOver(); // 添加一张随机卡片
		}
	}

	private void slideUp() {
		boolean addNew = false; // 设置一个是否需要添加新卡片标记

		for (int x = 0; x < 4; x++) {
			for (int y = 0; y < 4; y++) {

				for (int y1 = y + 1; y1 < 4; y1++) {
					if (cardList[x][y1].getNum() > 0) {

						if (cardList[x][y].getNum() <= 0) {
							cardList[x][y].setNum(cardList[x][y1].getNum());
							cardList[x][y1].setNum(0);

							y--;
							addNew = true;

						} else if (cardList[x][y].isEqu(cardList[x][y1])) {
							cardList[x][y].setNum(cardList[x][y].getNum() * 2);
							Animation animation = cardList[x][y].setAnimation();
							cardList[x][y].startAnimation(animation);
							cardList[x][y1].setNum(0);
							MainActivity.getMainActivity().addScore(
									cardList[x][y].getNum());
							addNew = true;

						}

						break;

					}
				}
			}
		}
		if (addNew) {
			addRandomCard();// 添加一张随机卡片
			gameOver(); // 调用该方法查看游戏是否结束
		}
	}

	private void slideRight() {
		boolean addNew = false; // 设置一个是否需要添加新卡片标记
		for (int y = 0; y < 4; y++) {

			for (int x = 3; x >= 0; x--) {

				for (int x1 = x - 1; x1 >= 0; x1--) {
					if (cardList[x1][y].getNum() > 0) { // 如果右边不为空
						if (cardList[x][y].getNum() <= 0) { // 如果当前为空
							cardList[x][y].setNum(cardList[x1][y].getNum()); // 则把右边的值赋给它。。
							cardList[x1][y].setNum(0);// 清空右边

							x++;
							// 此时卡片移动了 可以添加一个新卡片
							addNew = true;
						} else if (cardList[x][y].isEqu(cardList[x1][y])) { // 如果和右边的值相等
							cardList[x][y].setNum(cardList[x][y].getNum() * 2); // 则乘以2
							Animation animation = cardList[x][y].setAnimation();
							cardList[x][y].startAnimation(animation);
							cardList[x1][y].setNum(0);// 清空右边

							MainActivity.getMainActivity().addScore(
									cardList[x][y].getNum());

							addNew = true;
						}
						break;
					}

				}
			}
		}
		if (addNew) {
			addRandomCard();
			gameOver(); // 添加一张随机卡片
		}
	}

	private void gameOver() {
		boolean isOver = true;
		stop: // 跳出多重循环标记
		for (int y = 0; y < 4; y++) {
			for (int x = 0; x < 4; x++) {
				if (cardList[x][y].getNum() == 0
						|| (x > 0 && cardList[x][y].isEqu(cardList[x - 1][y]))
						|| (x < 3 && cardList[x][y].isEqu(cardList[x + 1][y]))
						|| (y > 0 && cardList[x][y].isEqu(cardList[x][y - 1]))
						|| (y < 3 && cardList[x][y].isEqu(cardList[x][y + 1]))) {
					isOver = false;
					break stop;
				}

			}
		}
		if (isOver) {
			new AlertDialog.Builder(getContext())
					.setTitle("游戏结束^_^")
					.setMessage("已经没有卡片可移动了 再接再厉:)")
					.setPositiveButton("再来一次",
							new DialogInterface.OnClickListener() {

								public void onClick(DialogInterface dialog,
										int which) {
									startGame(); // 重新开始游戏
								}
							}).show().setCanceledOnTouchOutside(false);
		}

	}

}
