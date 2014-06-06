package cn.jian.whybygenng;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

public class CommitActivity extends Activity implements OnClickListener{

	ImageButton logo_ibt;
	ImageButton add_ibt;
	TextView title_tx;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_commit);
		logo_ibt=(ImageButton) findViewById(R.id.logo_ibt);
	    add_ibt=(ImageButton) findViewById(R.id.add_ibt);
	    title_tx=(TextView) findViewById(R.id.title_tx);
	    logo_ibt.setBackgroundResource(R.drawable.back_selector);
	    add_ibt.setBackgroundResource(R.drawable.commit_selector);
	    title_tx.setText("贡献问题");
	    add_ibt.setOnClickListener(this);
	    logo_ibt.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==add_ibt){
			
		}
		if(v==logo_ibt){
			finish();
		}
	}
}
