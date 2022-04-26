package crm.workbench.web.controller;

import crm.commons.contants.Contants;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtils;
import crm.commons.utils.HSSFUtils;
import crm.commons.utils.UUIDUtils;
import crm.settings.domain.User;
import crm.settings.service.UserService;
import crm.workbench.domain.Activity;
import crm.workbench.domain.ActivityRemark;
import crm.workbench.service.ActivityRemarkService;
import crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private ActivityRemarkService activityRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private UserService userService;
    @RequestMapping("/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        //调用service层方法查询所有用户
        List<User> userList=userService.queryAllUsers();
        //把数据保存到request中
        request.setAttribute("userList",userList);
        //请求转发到市场活动主页面
        return "workbench/activity/index";
    }


    @RequestMapping("/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity, HttpSession session){
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateBy(user.getId());
        activity.setCreateTime(DateUtils.formateDateTime(new Date()));
        //调用service方法，保存创建市场活动
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret=activityService.saveCreateActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试....");
            }

        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试....");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name,String owner,String startDate,String endDate,int pageNo,int pageSize){
        Map<String,Object> map=new HashMap<>();
        map.put("name",name);
        map.put("owner",owner);
        map.put("startDate",startDate);
        map.put("endDate",endDate);
        map.put("beginNo",(pageNo-1)*pageSize);
        map.put("pageSize",pageSize);
        //调用service层方法，查询数据
        List<Activity> activityList=activityService.queryActivityByConditionForPage(map);
        int totalRows=activityService.queryCountOfActivityByCondition(map);
        //根据查询结果，生成响应数据
        Map<String,Object> retMap=new HashMap();
        retMap.put("activityList",activityList);
        retMap.put("totalRows",totalRows);
        return retMap;
    }


    @RequestMapping("/workbench/activity/deleteActivityIds.do")
    @ResponseBody
    public Object deleteActivityIds(String[] id){
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret = activityService.deleteActivityBtIds(id);

            if (ret > 0) {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;

    }

    @RequestMapping("/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        Activity activity=activityService.queryActivityById(id);
        return activity;
    }

    @RequestMapping("/workbench/activity/saveEditActivity.do")
    @ResponseBody
    public Object saveEditActivity(Activity activity,HttpSession session){
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditTime(DateUtils.formateDateTime(new Date()));
        activity.setEditBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret=activityService.saveEditActivity(activity);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            }else {
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
            }
        }catch (Exception e){
            e.printStackTrace();
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }

    @RequestMapping("/workbench/activity/fileDownload.do")
    public void fileDownload(HttpServletResponse response) throws Exception{
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        //获取输出流
        OutputStream out=response.getOutputStream();

        //设置响应头信息，使浏览器接收到响应信息之后，直接激活文件下载窗口，即使能打开文件也不打开
        response.addHeader("Content-Disposition","attachment;filename=mystudentList.xls");
        //读取excel文件（InputStream），把输出到浏览器（OutputStream）
        InputStream is=new FileInputStream("/Users/rcz/Desktop/serverDir/studentList.xls");
        byte[] buff=new byte[256];
        int len=0;
        while ((len=is.read(buff))!=-1){
            out.write(buff,0,len);
        }
        //关闭资源
        is.close();
        out.flush();
    }

    @RequestMapping("/workbench/activity/exportALlActivity.do")
    public void exportALlActivity(HttpServletResponse response) throws Exception{
       List<Activity> activityList=activityService.queryAllActivitys();
       //创建excel文件，并且把activityList写入到excel文件中
        HSSFWorkbook wb=new  HSSFWorkbook();
        HSSFSheet sheet=wb.createSheet("市场活动列表");
        HSSFRow row=sheet.createRow(0);
        HSSFCell cell=row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("成本");
        cell=row.createCell(6);
        cell.setCellValue("描述");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");

        //遍历activityList，创建HSSFRow对象，生成所有的数据行
        Activity activity=null;
        for (int i = 0; i < activityList.size(); i++) {
            activity=activityList.get(i);

            //每遍历出一个activity，生成一行
            if(activityList!=null&&activityList.size()>0){
                row=sheet.createRow(i+1);
                //每一场创建11列，每一列数据从activity中获取
                cell=row.createCell(0);
                cell.setCellValue(activity.getId());
                cell=row.createCell(1);
                cell.setCellValue(activity.getOwner());
                cell=row.createCell(2);
                cell.setCellValue(activity.getName());
                cell=row.createCell(3);
                cell.setCellValue(activity.getStartDate());
                cell=row.createCell(4);
                cell.setCellValue(activity.getEndDate());
                cell=row.createCell(5);
                cell.setCellValue(activity.getCost());
                cell=row.createCell(6);
                cell.setCellValue(activity.getDescription());
                cell=row.createCell(7);
                cell.setCellValue(activity.getCreateTime());
                cell=row.createCell(8);
                cell.setCellValue(activity.getCreateBy());
                cell=row.createCell(9);
                cell.setCellValue(activity.getEditTime());
                cell=row.createCell(10);
                cell.setCellValue(activity.getEditBy());

            }
        }
        //根据wb对象，生成excel文件
       /* OutputStream os=new FileOutputStream("/Users/rcz/Desktop/serverDir/activityList.xls");
        wb.write(os);
        os.close();
        wb.close();*/

        //把生成的excel文件下载到客户端
        //设置响应类型
        response.setContentType("application/octet-stream;charset=UTF-8");
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //获取输出流
        OutputStream out=response.getOutputStream();
        /*InputStream is=new FileInputStream("/Users/rcz/Desktop/serverDir/activityList.xls");
        byte [] buff=new byte[256];
        int len=0;
        while ((is.read(buff))!=-1){
            out.write(buff,0,len);
        }
        is.close();*/
        wb.write(out);
        wb.close();
        out.flush();
    }
    @RequestMapping("/workbench/activity/importActivity.do")
    @ResponseBody
    public Object importActivity(MultipartFile activityFile,HttpSession session){
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        ReturnObject returnObject=new ReturnObject();
        try {
            //把excel文件写到磁盘文件中
            /*String originalFilename=activityFile.getOriginalFilename();
            File file=new File("/Users/rcz/Desktop/serverDir/",originalFilename);
            activityFile.transferTo(file);*/
            //解析excel文件，获取文件中的数据，并且封装成activityList
            //InputStream is=new FileInputStream("/Users/rcz/Desktop/serverDir/"+originalFilename);
            InputStream is=activityFile.getInputStream();
            HSSFWorkbook wb=new HSSFWorkbook(is);
            HSSFSheet sheet=wb.getSheetAt(0);
            HSSFRow row=null;
            HSSFCell cell=null;
            Activity activity=null;
            List<Activity> activityList=new ArrayList<>();
            for(int i=1;i<=sheet.getLastRowNum();i++){
                row=sheet.getRow(i);
                activity=new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateUtils.formateDateTime(new Date()));
                activity.setCreateBy(user.getId());
                for(int j=0;j<row.getLastCellNum();j++){
                    cell=row.getCell(j);
                    String cellValue=HSSFUtils.getCellValueForStr(cell);
                    if(j==0){
                        activity.setName(cellValue);
                    }else if(j==1){
                        activity.setStartDate(cellValue);
                    }else if(j==2){
                        activity.setEndDate(cellValue);
                    }else if(j==3){
                        activity.setCost(cellValue);
                    } else if(j==4){
                        activity.setDescription(cellValue);
                    }
                }
                activityList.add(activity);

            }
            int ret=activityService.saveCreateActivityByList(activityList);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
            returnObject.setRetData(ret);

        } catch (IOException e) {
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return  returnObject;
    }

    @RequestMapping("/workbench/activity/detailActivity.do")
    public String detailActivity(String id,HttpServletRequest request){
        Activity activity=activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList=activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        //把数据保存到request中国呢
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        //请求转发
        return "workbench/activity/detail";
    }
}
