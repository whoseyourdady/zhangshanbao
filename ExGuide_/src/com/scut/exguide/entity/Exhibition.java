package com.scut.exguide.entity;

/**
 * 这是展会的实体
 * @author fatboy
 *
 */
public class Exhibition {
	
	public static final int TAG = 2;	
	
	public int mID;//对应的ID展览id
	public String name_cn;//展会的名称（中文） name_cn
	public String name_en;//展览名称（英文）name_en
	public boolean onshow;//状态（举办中，筹备中） 
	public String logo_name;//展览Logo文件名  
	public String logo_url;//展览logo URL  
	public String type_id;//展览类型  
	public String period_start;//展览时间（开始）  
	public String period_end;//展览时间（结束）  
	public String day_start;//日开放时间（开始） 
	public String day_end;//日开放时间（结束）  
	public String region;//地区 =》目前定位在中国  
	public String address;//展览详细地址   
	public String hall;//展馆名称   
	public String website;//网址  
	public String organizer;//主办单位   
	public String host;//承办单位   
	public String coorganizer;//协办单位 
	public String supporter;//支持单位 
	public String description;//展览介绍 
	public String contact;//联系人  
	public String contact_add;//联系地址  
	public String postcode;//邮编  
	public String telephone;//固定电话  
	public String fax;//传真  
	public String mobilephone;//手机号码 

	
	public Exhibition(){
		
	}
}
