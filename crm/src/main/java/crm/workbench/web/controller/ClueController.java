package crm.workbench.web.controller;

import crm.commons.contants.Contants;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtils;
import crm.commons.utils.UUIDUtils;
import crm.settings.domain.DicValue;
import crm.settings.domain.User;
import crm.settings.service.DicValueService;
import crm.settings.service.UserService;
import crm.workbench.domain.Activity;
import crm.workbench.domain.Clue;
import crm.workbench.domain.ClueActivityRelation;
import crm.workbench.domain.ClueRemark;
import crm.workbench.service.ActivityService;
import crm.workbench.service.ClueActivityRelationService;
import crm.workbench.service.ClueRemarkService;
import crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    @Autowired
    private ClueService clueService;
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;

    @RequestMapping("/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> userList=userService.queryAllUsers();
        List<DicValue> appellationList=dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList=dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("userList",userList);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    @RequestMapping("/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session){
        User user= (User) session.getAttribute(Contants.SESSION_USER);
        clue.setId(UUIDUtils.getUUID());
        clue.setCreateTime(DateUtils.formateDateTime(new Date()));
        clue.setCreateBy(user.getId());
        ReturnObject returnObject=new ReturnObject();
        //调用service，保存线索信息
        try {
            int ret=clueService.saveCreateClue(clue);
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

    @RequestMapping("/workbench/clue/detailClue.do")
    public String detailClue(String id,HttpServletRequest request){
        Clue clue=clueService.queryClueForDetailById(id);
        List<ClueRemark> remarkList=clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList=activityService.queryActivityForDetailByClueId(id);
        //把数据保存到request中
        request.setAttribute("clue",clue);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("activityList",activityList);
        //请求转发
        return "workbench/clue/detail";
    }

    @RequestMapping("/workbench/clue/queryActivityForDetailByNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByNameClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        //调用service层方法，查询市场活动
        List<Activity> activityList=activityService.queryActivityForDetailByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/saveBund.do")
    @ResponseBody
    public Object saveBund(String[] activityId,String clueId){
        ClueActivityRelation car=null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for (String ai:activityId){
            car=new ClueActivityRelation();
            car.setActivityId(ai);
            car.setClueId(clueId);
            car.setId(UUIDUtils.getUUID());
            relationList.add(car);
        }
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret=clueActivityRelationService.saveCreateClueActivityRelationByList(relationList);
            if(ret>0){
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
                List<Activity> activityList=activityService.queryActivityForDetailByIds(activityId);
                returnObject.setRetData(activityList);
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

    @RequestMapping("/workbench/clue/saveUnbund.do")
    @ResponseBody
    public Object saveUnbund(ClueActivityRelation clueActivityRelation){
        ReturnObject returnObject=new ReturnObject();
        try {
            int ret=clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(clueActivityRelation);
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

    @RequestMapping("/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){//这里没有使用@ResponseBody 是一个疑问
        Clue clue=clueService.queryClueForDetailById(id);
        System.out.println(id);
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("clue",clue);
        request.setAttribute("stageList",stageList);
        return "workbench/clue/convert";
    }

    @RequestMapping("/workbench/clue/queryActivityForConvertByNameClueId.do")
    @ResponseBody
    public Object queryActivityForConvertByNameClueId(String activityName,String clueId){
        Map<String,Object> map=new HashMap<>();
        map.put("activityName",activityName);
        map.put("clueId",clueId);
        List<Activity> activityList=activityService.queryActivityForConvertByNameClueId(map);
        return activityList;
    }

    @RequestMapping("/workbench/clue/convertClue.do")
    @ResponseBody
    public Object convertClue(String clueId,String money,String name,String expectedDate,String stage,String activityId,String isCreateTran,HttpSession session){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("clueId",clueId);
        map.put("name",name);
        map.put("expectedDate",expectedDate);
        map.put("stage",stage);
        map.put("activityId",activityId);
        map.put("isCreateTran",isCreateTran);
        map.put("money",money);
        map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
        try {
            //调用service层方法
            clueService.saveConvert(map);
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("系统忙，请稍后重试");
        }
        return returnObject;
    }
}
