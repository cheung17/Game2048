package com.ztx.game2048;



 



import com.ztx.gameView.GameView; 






import android.app.Activity; 
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
    public TextView score;
    private SharedPreferences preferences;
    public ImageView imageView;
    Editor editor;
    public static MainActivity mainActivity=null;
    private int curScore =0; //初始分数
	private TextView bestScore;
	private int getScore;
    public MainActivity(){
		mainActivity=this;      //构造方法
		
	} 
    
  
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		score=(TextView) findViewById(R.id.tvScore);
	    score.setText("");
	    preferences=getSharedPreferences("hign_score", MODE_PRIVATE);
	     editor=preferences.edit();//获得编辑
	     getScore=preferences.getInt("score", 0);	
	    imageView=(ImageView) findViewById(R.id.img);
	    bestScore=(TextView) findViewById(R.id.bestScore); //最高分
	    bestScore.setText(getScore+"");
	    imageView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			  GameView.getGameView().startGame();  //重新游戏
			  getScore=preferences.getInt("score", 0);
			  bestScore.setText(getScore+"");
			  hasWin=true; 
			}
		});
	}
	 
 
	
	  
	public void clearScre() {  //清空分数
		curScore=0;
		showScore();
		 getScore=preferences.getInt("score", 0);
		bestScore.setText(getScore+"");
    
	}
	public void showScore() {  //展示分数
	   score.setText(curScore+"");
	  
	}
	  boolean hasWin=true; 
	public void addScore(int s) {  //加分
	     
		   
			curScore=curScore+s; 
			 if(curScore>getScore){
				   
				   saveScore(curScore);
				   
			   }
			showScore();
		    

	}
	
    public static MainActivity getMainActivity() {    //生成get方法
		return mainActivity;  
	}
	private void saveScore(int s) { //保存最高	
		if(hasWin ){
			if(getScore!=0){
		   Toast.makeText(this, "已打破最高分", 1).show();
		   hasWin=false;
			}
		}
      		editor.putInt("score", s);      		
      		editor.commit();
	}

}
  
