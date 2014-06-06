package cn.jian.whybygenng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class LookActivity extends Activity implements OnClickListener{

	ImageButton logo_ibt;
	ImageButton add_ibt;
	TextView title_tx;
	TextView msg_tx;
	TextView qu_tx;
	PopupWindow window;
	String title;
	String msg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_look);
		logo_ibt=(ImageButton) findViewById(R.id.logo_ibt);
	    add_ibt=(ImageButton) findViewById(R.id.add_ibt);
	    title_tx=(TextView) findViewById(R.id.title_tx);
	    msg_tx=(TextView) findViewById(R.id.look_tx1);
	    qu_tx=(TextView) findViewById(R.id.look_tx);
	    
	    logo_ibt.setBackgroundResource(R.drawable.back_selector);
	    
	    Intent intent=getIntent();
	    title=intent.getStringExtra("qu");
	    title_tx.setText(App.mStrings2[new Integer(intent.getStringExtra("type"))-1]);
	    msg=intent.getStringExtra("msg");
	    msg_tx.setText(msg);
	    qu_tx.setText(title);
	    add_ibt.setOnClickListener(this);
	    logo_ibt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==logo_ibt){
			finish();
		}
		if(v==add_ibt){
			initpop();
			window.showAsDropDown(add_ibt, 0, 5);
		}
	}
	
	public void initpop(){
		
		List<Map<String, String>> list3=new ArrayList<Map<String,String>>();
        Map<String, String> map1=new HashMap<String, String>();
        Map<String, String> map2=new HashMap<String, String>();
        map1.put("size", "收藏");
        map2.put("size", "分享");
        list3.add(map1);
        list3.add(map2);
        
		if (window== null) {
			LinearLayout lv=(LinearLayout) LinearLayout.inflate(getApplicationContext(), R.layout.pop, null);
			ListView listView2 =(ListView) lv.findViewById(R.id.listview);
			SimpleAdapter adapter=new SimpleAdapter(LookActivity.this, list3, R.layout.pop_item, new String[]{"size"},new int[]{R.id.pop_tx});
			listView2.setAdapter(adapter);
			listView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if(position==1){
						Intent intent = new Intent(Intent.ACTION_SEND);
						intent.setType("text/plain");
						intent.putExtra(Intent.EXTRA_SUBJECT,title);
						intent.putExtra(Intent.EXTRA_TEXT, msg+"来自 十万个为什么 (http://gengjian.24.lc/)");
						startActivity(Intent.createChooser(intent, "选择分享"));
					}
					window.dismiss();
				}
			});
			window=new PopupWindow(lv, 180, 70*2+3);
			window.setFocusable(true);//焦点聚焦  如果不设置,则点击不到listview
			//设置点击其他地方 就消失 
			window.setOutsideTouchable(false);//是否允许点击popwindow以外的区域（false:点击popwindow外区域自动消失）
			//设置背景  不设置背景的话，点击popwindow以外的区域，popwindow不会消失  
			//布局文件的背景优先级大于代码的
			window.setBackgroundDrawable(getResources().getDrawable(R.drawable.actionbar_bg_grey_transparent));
			window.update();//更新popwindow的状态
		}
		if (window.isShowing()) {
			window.dismiss();
		}
	}
}
