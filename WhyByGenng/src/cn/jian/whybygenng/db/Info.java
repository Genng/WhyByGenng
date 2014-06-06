package cn.jian.whybygenng.db;

public class Info {

	private int id;
	private String name;//事件
	private int type;//类别
	private int tips;//提醒 0 否 1 是
	private int top; //顶置 0 否 1 是
	private String ms; //备注 
	private String time; //时间
	private String time2; //格式 2013/07/08
	
	public Info() {
		super();
	}
	public Info(int id, String name, int type, int tips, int top, String ms,
			String time, String time2) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.tips = tips;
		this.top = top;
		this.ms = ms;
		this.time = time;
		this.time2 = time2;
	}
	
	
	public Info(String name, int type, int tips, int top, String ms,
			String time, String time2) {
		super();
		this.name = name;
		this.type = type;
		this.tips = tips;
		this.top = top;
		this.ms = ms;
		this.time = time;
		this.time2 = time2;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getTips() {
		return tips;
	}
	public void setTips(int tips) {
		this.tips = tips;
	}
	public int getTop() {
		return top;
	}
	public void setTop(int top) {
		this.top = top;
	}
	public String getMs() {
		return ms;
	}
	public void setMs(String ms) {
		this.ms = ms;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getTime2() {
		return time2;
	}
	public void setTime2(String time2) {
		this.time2 = time2;
	}
	
	
}
