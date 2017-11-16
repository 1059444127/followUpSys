package com.demo.index;

import com.demo.common.CommonUtils;
import com.demo.common.Conts;
import com.demo.common.model.*;
import com.demo.interceptor.DepartmentInterceptor;
import com.jfinal.aop.Before;
import com.jfinal.aop.Clear;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.json.JSONObject;

import java.io.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Before(DepartmentInterceptor.class)
public class IndexController extends Controller {
	
	public void index() {
		setData();
		render("index.html");
	}
	
	@Clear
	public void login() {
		setData();
		render("login.html");
	}
	
	
	public void out() {
		this.setAttr("softinfo", Info.dao.findById("1"));
		render("login.html");
	}
	
	@Clear
	public void onlogin(){
		Map<String, String> m = new HashMap<String, String>();
		
		String user = this.getPara("name").toString().trim();
		String pwd = this.getPara("pwd").toString().trim();
		
		if("".equals(user)||"".equals(pwd)){
			m.put("code", "404");
			m.put("msg", "用户名或密码不能为空！");
			this.renderJson(m);
			return;
		}
		
		Record r = Department.dao.checkAdmin(user,pwd);
		if(r==null){
			m.put("code", "401");
			m.put("message", "用户名或密码不正确！");
			this.renderJson(m);
			return;
		}
		// 保存Session 登陆状态
		this.setSessionAttr("admin_user", r);

		// 返回登陆成功及跳转网址
		m.put("code", "200");
		m.put("message", "/admin/index");
		this.renderJson(m);
		
	}
	
	
	private void setData() {
		//网站具体信息，存于id1中
		this.setAttr("softinfo", Info.dao.findById("1"));
		this.setAttr("admin_user", this.getSessionAttr("admin_user"));
		
	}
	
	public void imageUpload(){
		List<UploadFile> u = this.getFiles();
		JSONObject json = new JSONObject();
		File uploadFile = null;
		for(int i = 0; i<u.size();i++){
			uploadFile = u.get(i).getFile(); // 最大上传20M的图片
			// 异步上传时，无法通过uploadFile.getFileName()获取文件名
	        String fileName = uploadFile.getName();
	        //fileName = fileName.substring(fileName.lastIndexOf("\\") + 1); // 去掉路径

	        // 异步上传时，无法通过File source = uploadFile.getFile();获取文件
	        File source = new File(PathKit.getWebRootPath() + "/upload/" + fileName); // 获取临时文件对象

	        String extension = fileName.substring(fileName.lastIndexOf("."));
	        String savePath = PathKit.getWebRootPath() + "/upload";
	        

	        if (".png".equals(extension) || ".jpg".equals(extension)
	                || ".gif".equals(extension) || "jpeg".equals(extension)
	                || "bmp".equals(extension)) {
	            fileName = CommonUtils.getCurrentTime() + extension;

	            try {
	                FileInputStream fis = new FileInputStream(source);

	                File targetDir = new File(savePath);
	                if (!targetDir.exists()) {
	                    targetDir.mkdirs();
	                }

	                File target = new File(targetDir, fileName);
	                if (!target.exists()) {
	                    target.createNewFile();
	                }

	                FileOutputStream fos = new FileOutputStream(target);
	                byte[] bts = new byte[1024 * 20];
	                while (fis.read(bts, 0, 1024 * 20) != -1) {
	                    fos.write(bts, 0, 1024 * 20);
	                }
	                
	                try {
	                    Thread.sleep(1000);              
	                } catch(InterruptedException ex) {
	                    Thread.currentThread().interrupt();
	                }   
	                fos.close();
	                fis.close();
	                json.put("code"+i, 200);
	                json.put("src"+i, "/upload/" + fileName); // 相对地址，显示图片用
	                source.delete();

	            } catch (FileNotFoundException e) {
	                json.put("code"+i, 500);
	                json.put("message"+i, "上传出现错误，请稍后再上传");
	            } catch (IOException e) {
	            	json.put("code"+i, 500);
	                json.put("message"+i, "文件写入服务器出现错误，请稍后再上传");
	            }
	        } else {
	            source.delete();
	            json.put("code"+i, 500);
	            json.put("message"+i, "只允许上传png,jpg,jpeg,gif,bmp类型的图片文件");
	        }
		}
        

        renderJson(json.toString());
    }
	
	public void add() {
		setData();
		String s = this.getPara("type");
		this.setAttr("type", s);
		//最初的时候这会出现问题，数据库为空
		Record r = Db.findFirst("select * from patientinfo order by id desc");
		if(r!=null){
			this.setAttr("id", r.get("id"));
		}
		
		
		this.setAttr("reimbursement", Db.find("select * from reimbursement"));
		this.setAttr("ks", Db.find("select * from department"));
		this.setAttr("patient", Db.find("select * from patientinfo"));
		render("add.html");
	}
	
	/**
	 * 添加功能
	 */
	public void doadd() {
		if("1".equals(this.getPara("type"))){
			Patientinfo p = this.getModel(Patientinfo.class,"dao");
			p.set("time", new Date());
			p.save();
			this.redirect("add?type=3");
		}
		else{
			Patientrecord p = this.getModel(Patientrecord.class,"dao");
			p.set("time", new Date());
			p.set("istrue", 0);
			p.save();
			this.redirect("record");
		}
		
		
	}
	
	/**
	 * 病患列表
	 */
	public void search() {
		setData();
		if(this.getPara("search")!=null){
			Record r = this.getSessionAttr("admin_user");
			String sql = getsql("patientinfo","name",this.getPara("search"))+" and departmentid="+r.get("id");
			System.out.println(sql);
			
			List<Record> r1 = Db.find(sql);
			setAttr("list", r1);
			
		}
		setAttr("pagetitle", "患者信息表");
		setAttr("addtype", "1");
		render("list1.html");
	}
	public String getsql(String tabname,String ziduan,String search){
		StringBuffer sql = new StringBuffer();
		sql.append("select * from ").append(tabname).append(" where ").append(ziduan).append(" like '%").append(search).append("%'");
		
		return sql.toString();
	}
	/**
	 * 病患列表
	 */
	public void list() {
		setData();
		if(this.getAttr("id")==null||"".equals(this.getAttr("id"))){
			Record r = this.getSessionAttr("admin_user");
			
			setAttr("list", Db.paginate(getParaToInt(0, 1), Conts.PageSize,
				"select * ","from patientinfo where departmentid=?",r.get("id").toString()));
		}
		else{
		setAttr("list", Db.paginate(getParaToInt(0, 1), Conts.PageSize,
				"select * ","from patientinfo where id=?",this.getPara("id")));
		}
		setAttr("pagetitle", "患者信息表");
		setAttr("addtype", "1");
		render("list.html");
	}
	
	/**
	 * 病史表
	 */
	public void history() {
		setData();
		Record r = this.getSessionAttr("admin_user");
		if(this.getPara("id")==null||"".equals(this.getPara("id"))){
			
			setAttr("history", Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select *,patienthistory.time as time,patienthistory.id as id ",
					"from patienthistory,patientinfo where patienthistory.patientid=patientinfo.id and patienthistory.departmentid=?",r.get("id").toString()));
		
		}
		else{
			String sql = " and patientinfo.id="+this.getPara("id");
			System.out.println(sql);
			setAttr("history", Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select *,patienthistory.time as time,patienthistory.id as id ",
					"from patienthistory,patientinfo where patienthistory.patientid=patientinfo.id and patienthistory.departmentid=?"+sql,r.get("id").toString()));
			
		}
		setAttr("pagetitle", "患者病史表");
		setAttr("addtype", "2");
		this.redirect("record");
	}
	
	/**
	 * 随访记录表
	 */
	public void record() {
		setData();
		Record r = this.getSessionAttr("admin_user");
		if(this.getPara("id")==null||"".equals(this.getPara("id"))){
			setAttr("record", Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select *,patientrecord.time as time,patientrecord.id as id ",
					"from patientrecord,patientinfo where patientrecord.patientid=patientinfo.id and patientrecord.departmentid=? order by istrue asc,stime asc",r.get("id").toString()));
		}
		else{
			String sql = " and patientinfo.id="+this.getPara("id");
			setAttr("record", Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select *,patientrecord.time as time,patientrecord.id as id ",
					"from patientrecord,patientinfo where patientrecord.patientid=patientinfo.id and patientrecord.departmentid=?"+sql+" order by istrue asc,stime asc",r.get("id").toString()));
		}
		setAttr("pagetitle", "随访记录表");
		setAttr("addtype", "3");
		render("record.html");
	}
	

	/**
	 * 随访搜索
	 */
	public void searchname() {
		setData();
		Record r = this.getSessionAttr("admin_user");
		String sql = " and patientinfo.name='"+this.getPara("search")+"'";
		System.out.println(sql);
		setAttr("record", Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select *,patientrecord.time as time,patientrecord.id as id ",
				"from patientrecord,patientinfo where patientrecord.patientid=patientinfo.id and patientrecord.departmentid=?"+sql+" order by istrue asc,stime asc",r.get("id").toString()));
		setAttr("pagetitle", "随访记录表");
		setAttr("addtype", "3");
		render("record.html");
		
	}
	
	
	/**
	 * 根据id删除
	 */
	public void del() {
		setData();
		String tablename = "";
		Map<String, String> m = new HashMap<String, String>();
		if("1".equals(this.getPara("type"))){
			 tablename = "patientinfo";
		}
		else if("2".equals(this.getPara("type"))){
			tablename = "patienthistory";
		}
		else if("3".equals(this.getPara("type"))){
			tablename = "patientrecord";
		}
		Record r = Db.findById(tablename, this.getPara("id"));
		
		if(Db.delete(tablename, r)){
			m.put("code", "200");
			m.put("msg", "删除成功！");
		}else{
			m.put("code", "500");
			m.put("msg", "删除失败，请联系管理员！");
			}
		
		renderJson(m);
	}
	
	/**
	 * 根据id更新
	 */
	public void done() {
		setData();
		Map<String, String> m = new HashMap<String, String>();
		if("3".equals(this.getPara("type"))){
			Record r = Db.findById("patientrecord", this.getPara("id"));
			r.set("istrue", 1);
			Db.update("patientrecord", r);
			m.put("code", "200");
			m.put("msg", "结束随访！");
		}else{
			m.put("code", "500");
			m.put("msg", "结束失败，请联系管理员！");
			}
		
		renderJson(m);
	}
	/**
	 * 根据id查询病患
	 */
	public void ser() {
		setData();
		String id = this.getPara("id");
		Record r = Db.findById("patientinfo",id );
		
		renderJson(r);
	}
	
	/**
	 * 根据id编辑信息
	 */
	public void edit() {
		setData();

		this.setAttr("type", this.getPara("type"));
		this.setAttr("reimbursement", Db.find("select * from reimbursement"));
		this.setAttr("ks", Db.find("select * from department"));
		this.setAttr("patient", Db.find("select * from patientinfo"));
		
		Record r = new Record();
		if("1".equals(this.getPara("type"))){
			r = Db.findById("patientinfo", this.getPara("id"));
			this.setAttr("info", r);
			render("edit.html");
		}else if("2".equals(this.getPara("type"))){
			r = Db.findById("patienthistory", this.getPara("id"));
			this.setAttr("info", r);
			render("edithistory.html");
		}else if("3".equals(this.getPara("type"))){
			r = Db.findById("patientrecord", this.getPara("id"));
			List<Record> r1 = Db.find("select * from patientrecord where patientid=? order by id asc", r.get("patientid"));
			System.out.println(r1.size());
			
			this.setAttr("info", r1);
			render("editrecord.html");
		}else if("4".equals(this.getPara("type"))){
			r = Db.findById("patientrecord", this.getPara("id"));
			List<Record> r1 = Db.find("select * from patientrecord where patientid=? order by id asc", r.get("patientid"));
			
			this.setAttr("id", r.get("patientid"));
			this.setAttr("info", r1);
			render("editrecord1.html");
		}
		
		
	}
		public void showedit() {
		setData();

		this.setAttr("type", this.getPara("type"));
		this.setAttr("reimbursement", Db.find("select * from reimbursement"));
		this.setAttr("ks", Db.find("select * from department"));
		this.setAttr("patient", Db.find("select * from patientinfo"));
		
		Record r = new Record();
		if("3".equals(this.getPara("type"))){
			r = Db.findById("patientrecord", this.getPara("id"));
			List<Record> r1 = Db.find("select * from patientrecord where patientid=? order by id asc", r.get("patientid"));
			System.out.println(r1.size());
			
			this.setAttr("info", r1);
			render("showeditrecord.html");
		}
		
		
	}
	
	public void doedit() {
		this.setAttr("type", this.getPara("type"));
		this.setAttr("id", "");
		
		if("1".equals(this.getPara("type"))){
			Patientinfo p = this.getModel(Patientinfo.class, "dao");
			p.update();
			this.redirect("list");
		}else if("2".equals(this.getPara("type"))){
			Patienthistory p = this.getModel(Patienthistory.class, "dao");
			p.update();
			this.redirect("history");
		}else if("3".equals(this.getPara("type"))){
			Patientrecord p = this.getModel(Patientrecord.class, "dao");
			p.update();
			this.redirect("record");
		}
	}
	
	/**
	 * 根据患者姓名，住院号搜索患者信息表
	 */
	public void selectlist() {
		setData();
		Map<String,String> m = new HashMap<String, String>();
		Page<Record> r = Db.paginate(getParaToInt(0, 1), Conts.PageSize,"select * ","from patientinfo where name=? or number=?",this.getAttr("type"));
		if(r==null){
			m.put("code", "500");
			m.put("msg", "没有相应数据，请联系管理员！");
		}
		else{
		setAttr("selectlist",r);
		}
		render("record.html");
	}
	
	/**
	 * 系统信息
	 */
	public void systeminfo() {
		getSystemInfo();
		this.renderJson((String) this.getAttr("SystemInfo"));
	}
	
	public void getSystemInfo() {
		double total = (Runtime.getRuntime().totalMemory()) / (1024.0 * 1024);
		double max = (Runtime.getRuntime().maxMemory()) / (1024.0 * 1024);
		double free = (Runtime.getRuntime().freeMemory()) / (1024.0 * 1024);

		Map<String, Double> m = new HashMap<String, Double>();

		m.put("maxMemory", max);
		m.put("totalMemory", total);
		m.put("freeMemory", free);
		m.put("JVM", (max - total + free));

		File[] roots = File.listRoots();

		double usedisk = 0;
		double totaldisk = 0;
		double freedisk = 0;

		// 获取磁盘分区列表
		for (File file : roots) {
			usedisk += file.getUsableSpace() / 1024 / 1024 / 1024;
			freedisk += file.getFreeSpace() / 1024 / 1024 / 1024;
			totaldisk += file.getTotalSpace() / 1024 / 1024 / 1024;
		}

		m.put("usedisk", usedisk);
		m.put("totaldisk", totaldisk);
		m.put("freedisk", freedisk);

		this.setAttr("SystemInfo", m);
	}
}





