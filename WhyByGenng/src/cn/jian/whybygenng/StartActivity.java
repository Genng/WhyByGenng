package cn.jian.whybygenng;

import cn.jian.whybygenng.bean.WhyBean;
import cn.jian.whybygenng.db.WhyDBTool;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity implements OnClickListener{

	Button bt1;
	Button bt2;
	Button bt3;
	WhyDBTool dbTool;
	int num=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);
		bt1=(Button) findViewById(R.id.start_1);
		bt2=(Button) findViewById(R.id.start_2);
		bt3=(Button) findViewById(R.id.start_3);
		bt1.setOnClickListener(this);
		bt2.setOnClickListener(this);
		bt3.setOnClickListener(this);
		dbTool=new WhyDBTool(StartActivity.this);
		App.appContext=this.getApplicationContext();
	}
	@Override
	public void onClick(View v) {
		if(v==bt3){
			Intent intent=new Intent(StartActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
		
		if(v==bt2){
			
			final Handler handler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if(msg.what==1){
						Toast.makeText(StartActivity.this, "更新"+num+"条数据", 0).show();
					}
					if(msg.what==2){
						Toast.makeText(StartActivity.this, "没有更新", 0).show();
					}
				}
			};
			
			new Thread(){
				Message msg=new Message();
				public void run() {
					try {
						App.datas=App.getProductsInfo("why_1.xml");
						num=App.datas.size()-dbTool.find().size();
						if(num>0){
							for(int i=dbTool.find().size();i<App.datas.size();i++){
								dbTool.insert(App.datas.get(i));
							}
				        	msg.what=1;
						}else{
							msg.what=2;
						}
						
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendMessage(msg);
					
				};
			}.start();
			
			
		}
		
		if(v==bt1){
			
			final Handler handler=new Handler(){
				@Override
				public void handleMessage(Message msg) {
					if(msg.what==1){
						Toast.makeText(StartActivity.this, "初始化成功", 0).show();
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
