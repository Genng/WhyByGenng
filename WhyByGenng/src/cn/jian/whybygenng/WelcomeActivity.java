package cn.jian.whybygenng;

import cn.jian.whybygenng.db.WhyDBTool;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Toast;

public class WelcomeActivity extends Activity {

	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	WhyDBTool dbTool;
	View loadView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		final View view = View.inflate(this, R.layout.activity_welcome, null);
		setContentView(view);
		loadView=findViewById(R.id.loading_lv);
		preferences = getSharedPreferences("why_pre", MODE_PRIVATE);
	    editor = preferences.edit();
	    dbTool=new WhyDBTool(this);
	    App.appContext=this.getApplicationContext();
	    String flag=preferences.getString("why", "no");
	    if("yes".equals(flag)){
	    	loadView.setVisibility(view.GONE);
	    	// 渐变展示启动屏
			AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
			aa.setDuration(3000);
			view.startAnimation(aa);
			aa.setAnimationListener(new AnimationListener() {
				@Override
				public void onAnimationEnd(Animation arg0) {
					Intent intent = new Intent(WelcomeActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}

				@Override
				public void onAnimationRepeat(Animation animation) {
				}

				@Override
				public void onAnimationStart(Animation animation) {
				}

			});
	    }else{
	    	
	    	final Handler handler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if(msg.what==1){
						editor.putString("why", "yes");
						editor.commit();
						Toast.makeText(WelcomeActivity.this, "初始化完成", 0).show();
						Intent intent = new Intent(WelcomeActivity.this,
								MainActivity.class);
						startActivity(intent);
						finish();
//			        	dateListView.removeFooterView(footView);
//						footView.setVisibility(View.GONE);
					}
				}
			};
			
			new Thread(){
				Message msg=new Message();
				public void run() {
					try {
						App.datas=App.getProductsInfo("why_1.xml");
						for(int i=0;i<App.datas.size();i++){
							dbTool.insert(App.datas.get(i));
						}
						App.datas=App.getProductsInfo("why_2.xml");
						for(int i=0;i<App.datas.size();i++){
							dbTool.insert(App.datas.get(i));
						}
						App.datas=App.getProductsInfo("why_3.xml");
						for(int i=0;i<App.datas.size();i++){
							dbTool.insert(App.datas.get(i));
						}
						App.datas=App.getProductsInfo("why_4.xml");
						for(int i=0;i<App.datas.size();i++){
							dbTool.insert(App.datas.get(i));
						}
			        	msg.what=1;
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendMessage(msg);
					
				};
			}.start();
	    }
		
		
	}
}
