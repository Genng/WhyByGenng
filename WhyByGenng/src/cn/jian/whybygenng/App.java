package cn.jian.whybygenng;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import cn.jian.whybygenng.bean.WhyBean;
import android.app.Application;
import android.content.Context;
import android.util.Xml;

public class App extends Application {

	public static Context appContext;
	public static List<WhyBean> datas;
	public static String[] mStrings2 = {"科学知识",
			  "信息科技",
			  "男左女右",
			  "历史齿轮",
			  "Genng原创",
			  "安卓派",
			  "苹果派",
			  "移动互联网"
	};
	public static String[] popStrs = {
			"设置",
			"更新数据",
			"导入数据",
			"提交素材",
			"关于"
           };
	
	/**
	 * 资源文件 xml文档
	 * @return
	 */
	public static ArrayList<WhyBean> getProductsInfo(String name) {
		ArrayList<WhyBean> whyList = new ArrayList<WhyBean>();
		WhyBean why = null;
		try {
			InputStream input = appContext.getResources().getAssets().open(name);
			XmlPullParser parser = Xml.newPullParser();
			parser.setInput(input, "utf-8");
			int event = parser.getEventType();// 产生第一个
			while (event != XmlPullParser.END_DOCUMENT) {// 判断是否是文档结束事件
				switch (event) {
				case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
					whyList = new ArrayList<WhyBean>();
					break;
				case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
					if ("why".equals(parser.getName())) {// 判断开始标签元素是否是book
						why = new WhyBean();
					}
					if (why != null) {
						if ("question".equals(parser.getName())) {// 判断开始标签元素是否是name
							why.setQuestion(parser.nextText());
						} else if ("msg".equals(parser.getName())) {
							why.setMsg(parser.nextText());
						} else if ("id".equals(parser.getName())) {
							why.setId(Integer.parseInt(parser.nextText()));
						}else if ("type".equals(parser.getName())) {
							why.setType(parser.nextText());
						}
					}
					break;
				case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
					if ("why".equals(parser.getName())) {// 判断结束标签元素是否是book
						if (why != null) {
							whyList.add(why);// 将book添加到books集合
							why = null;
						}
					}
					break;
				}
				event = parser.next();// 进入下一个元素并触发相应事件
			}// end while
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return whyList;
	}
}
