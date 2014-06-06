package cn.jian.whybygenng;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import cn.jian.whybygenng.bean.WhyBean;
import cn.jian.whybygenng.db.WhyDBTool;
import cn.jian.whybygenng.view.LeftSliderLayout;
import cn.jian.whybygenng.view.LeftSliderLayout.OnLeftSliderLayoutStateListener;
import cn.jian.whybygenng.view.PullToRefreshListView;
import cn.jian.whybygenng.view.PullToRefreshListView.OnRefreshListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements OnLeftSliderLayoutStateListener, OnClickListener{
	private LeftSliderLayout leftSliderLayout;
	private ImageButton logo_ibt;
	private ImageButton add_ibt;
	private PullToRefreshListView dateListView;
	private ListView leftListView;
	private ItemAdpter itemAdpter;
	private List<WhyBean> dataLists;
	PopupWindow window;
	long firstTime=0;
	View footView;
	int page=1;
	WhyDBTool dbTool;
	String types="1";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		App.appContext=this.getApplicationContext();
		dbTool=new WhyDBTool(MainActivity.this);
		initData();
		initView();
	}

	private void initData(){
		dataLists=dbTool.findByPage(page,types);
		page++;
		
//		System.out.println(dataLists.size()+"-----------------");
//		for(int i=0;i<mStrings.length;i++){
//			WhyBean bean=new WhyBean();
//			bean.setMsg(mStrings3[i]);
//			bean.setQuestion(mStrings[i]);
//			bean.setType("科学知识");
//			dataLists.add(bean);
//		}
		
	}
	
	
	private void initView(){
		leftSliderLayout = (LeftSliderLayout) findViewById(R.id.main_slider_layout);
	    logo_ibt=(ImageButton) findViewById(R.id.logo_ibt);
	    add_ibt=(ImageButton) findViewById(R.id.add_ibt);
	    leftListView=(ListView) findViewById(R.id.left_list);
	    footView = getLayoutInflater().inflate(R.layout.listview_footer, null);
	    dateListView=(PullToRefreshListView) findViewById(R.id.index_list);
	    
//	    mListItems = new LinkedList<String>();
//        mListItems.addAll(Arrays.asList(mStrings));
//	    ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
//                android.R.layout.simple_list_item_1, mListItems);
	    itemAdpter=new ItemAdpter();
	    dateListView.addFooterView(footView);
	    dateListView.setAdapter(itemAdpter);
	    
	    leftListView.setAdapter(new DateAdpter());
	    
//	    dateListView=(ListView) findViewById(R.id.list_1);
	    logo_ibt.setOnClickListener(this);
	    add_ibt.setOnClickListener(this);
	    leftSliderLayout.setOnLeftSliderLayoutListener(this);
	    
	    leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				types=""+(arg2+1);
				page=1;
				dataLists=dbTool.findByPage(page,types);
				itemAdpter=new ItemAdpter();
				dateListView.setAdapter(itemAdpter);
			}
		});
	    
	    
	    System.out.println(dataLists.size()+"--------");
	    dateListView.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				new GetDataTask().execute();
				System.out.println(dataLists.size()+"--------");
				itemAdpter.notifyDataSetChanged();
			}
		});
	    
	    dateListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent=new Intent(MainActivity.this, LookActivity.class);
				intent.putExtra("msg", dataLists.get(arg2-1).getMsg());
				intent.putExtra("qu",  dataLists.get(arg2-1).getQuestion());
				intent.putExtra("type",  dataLists.get(arg2-1).getType());
				startActivity(intent);
			}
		});

	    
	    
	    dateListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
			    
			    if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&
						view.getLastVisiblePosition() == (view.getCount() - 1)) {
					if ("1".equals("")) {
						dateListView.removeFooterView(footView);
						footView.setVisibility(View.GONE);
//						Toast.makeText(Content.this,"亲，已经加载完成，木有更多数据了",2000).show();
					}else {
						final Handler handler=new Handler(){
							@Override
							public void handleMessage(Message msg) {
								if(msg.what==1){
									itemAdpter.notifyDataSetChanged();
//						        	
								}
								if(msg.what==2){
									dateListView.removeFooterView(footView);
									footView.setVisibility(View.GONE);
									Toast.makeText(MainActivity.this, "没有更多数据", Toast.LENGTH_SHORT).show();
								}
							}
						};
						
						new Thread(){
							Message msg=new Message();
							public void run() {
								try {
									System.out.println("-----page="+page);
									List<WhyBean> lists=dbTool.findByPage(page,types);
									
									if(lists!=null){
										if(lists.size()==0){
											msg.what=2;
										}else{
											for(int i=0;i<lists.size();i++){
												dataLists.add(lists.get(i));
											}
											msg.what=1;
											page++;
										}
										
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
			        	
//						
					}
				}
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				if(firstVisibleItem==0){  
                    System.out.println("滑到顶部");  
                }  
                if(visibleItemCount+firstVisibleItem==totalItemCount){  
                	
//		        	dateListView.removeFooterView(footView);
//					footView.setVisibility(View.GONE);
                }  

			}
		});
	    

	    
	    
	}
	
	
	public void initpop(){
		
		List<Map<String, String>> list3=new ArrayList<Map<String,String>>();
        Map<String, String> map1=new HashMap<String, String>();
        Map<String, String> map2=new HashMap<String, String>();
        Map<String, String> map3=new HashMap<String, String>();
        Map<String, String> map4=new HashMap<String, String>();
        map1.put("size", "网络版");
        map2.put("size", "更新数据");
        map3.put("size", "设置");
        map4.put("size", "关于");
        list3.add(map1);
        list3.add(map2);
        list3.add(map3);
        list3.add(map4);
        
		if (window== null) {
			LinearLayout lv=(LinearLayout) LinearLayout.inflate(getApplicationContext(), R.layout.pop, null);
			ListView listView2 =(ListView) lv.findViewById(R.id.listview);
			SimpleAdapter adapter=new SimpleAdapter(MainActivity.this, list3, R.layout.pop_item, new String[]{"size"},new int[]{R.id.pop_tx});
			listView2.setAdapter(adapter);
			listView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int position, long arg3) {
					if(position==3){
						Intent intent=new Intent(MainActivity.this, WelcomeActivity.class);
						startActivity(intent);
					}
					window.dismiss();
				}
			});
			window=new PopupWindow(lv, 210, 70*4+3);
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
	
	private class GetDataTask extends  AsyncTask<Void, Void, List<WhyBean>> {

        @Override
        protected List<WhyBean> doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                ;
            }
            return dataLists;
        }

        @Override
        protected void onPostExecute(List<WhyBean> result) {
        	WhyBean bean=new WhyBean();
        	bean.setMsg("新问题的答案");
        	bean.setQuestion("新的问题");
        	bean.setType("1");
        	dataLists.add(0,bean);
        	System.out.println(dataLists.size()+"--------rs");
//        	mListItems.addFirst("Added after refresh...");
            // Call onRefreshComplete when the list has been refreshed.
//        	itemAdpter=new ItemAdpter();
//        	dateListView.setAdapter(itemAdpter);
        	itemAdpter.notifyDataSetChanged();
            dateListView.onRefreshComplete();
            super.onPostExecute(result);
        }
    }

	
	
	class DateAdpter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return App.mStrings2.length;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view=View.inflate(MainActivity.this, R.layout.left_item, null);
			TextView tx=(TextView) view.findViewById(R.id.left_item_tx);
			tx.setText(App.mStrings2[arg0]);
			return view;
		}
		
	}
	
	
	class ItemAdpter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return dataLists.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			View view=View.inflate(MainActivity.this, R.layout.index_item, null);
			TextView tx=(TextView) view.findViewById(R.id.index_tx);
			tx.setText((arg0+1)+"."+dataLists.get(arg0).getQuestion());
			return view;
		}
		
	}
	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if(leftSliderLayout.isOpen()){
				leftSliderLayout.close();
			}else{
				if ((System.currentTimeMillis() - firstTime) > 2000) // System.currentTimeMillis()无论何时调用，肯定大于2000
				{
					Toast.makeText(getApplicationContext(), "再按一次退出程序",
							Toast.LENGTH_SHORT).show();
					firstTime = System.currentTimeMillis();
				} else {
					finish();
					System.exit(0);
				}
			}
			
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==logo_ibt){
			if(leftSliderLayout.isOpen()){
				leftSliderLayout.close();
			}else{
				leftSliderLayout.open();
			}
//			System.out.println("--------------open");
		
		}
		
		if(v==add_ibt){
//			changPopState(add_ibt);
			initpop();
			window.showAsDropDown(add_ibt, 0, 5);
		}
		
		if(v.getId()==R.id.left_lv_1){
		}
		if(v.getId()==R.id.left_lv_2){
			Intent intent=new Intent(MainActivity.this, CommitActivity.class);
			startActivity(intent);
		}
		if(v.getId()==R.id.left_lv_3){
		}
	}

	@Override
	public void OnLeftSliderLayoutStateChanged(boolean bIsOpen) {
		// TODO Auto-generated method stub
		if (bIsOpen) {
//			Toast.makeText(this, "LeftSliderLayout is open!", Toast.LENGTH_SHORT).show();
		} else {
//			Toast.makeText(this, "LeftSliderLayout is close!", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public boolean OnLeftSliderLayoutInterceptTouch(MotionEvent ev) {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	
}
	  
	  
