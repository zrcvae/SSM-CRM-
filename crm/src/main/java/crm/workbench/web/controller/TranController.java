package crm.workbench.web.controller;

import crm.commons.contants.Contants;
import crm.commons.domain.ReturnObject;
import crm.settings.domain.DicValue;
import crm.settings.domain.User;
import crm.settings.service.DicValueService;
import crm.settings.service.UserService;
import crm.workbench.domain.Tran;
import crm.workbench.domain.TranHistory;
import crm.workbench.domain.TranRemark;
import crm.workbench.service.CustomerService;
import crm.workbench.service.TranHistoryService;
import crm.workbench.service.TranRemarkService;
import crm.workbench.service.TranService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@Controller
public class TranController {
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private UserService userService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private TranService tranService;
    @Autowired
    private TranHistoryService tranHistoryService;
    @Autowired
    private TranRemarkService tranRemarkService;
    @RequestMapping("/workbench/transaction/index.do")
    public String index(HttpServletRequest request){
        //调用service查询动态数据
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        //把数据保存到request作用域中
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        //请求转发
        return "workbench/transaction/index";
    }

    @RequestMapping("/workbench/transaction/toSave.do")
    public String toSave(HttpServletRequest request){
        List<User> userList=userService.queryAllUsers();
        List<DicValue> transactionTypeList=dicValueService.queryDicValueByTypeCode("transactionType");
        List<DicValue> sourceList=dicValueService.queryDicValueByTypeCode("source");
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("transactionTypeList",transactionTypeList);
        request.setAttribute("sourceList",sourceList);
        request.setAttribute("stageList",stageList);
        request.setAttribute("userList",userList);
        return "workbench/transaction/save";
    }

    @RequestMapping("/workbench/transaction/getPossibilityByStage.do")
    @ResponseBody
    public Object getPossibilityByStage(String stageValue){
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString("ä»·å\u0080¼å»ºè®®");
        return possibility;

    }
    @RequestMapping("/workbench/transaction/queryAllCustomerName.do")
    @ResponseBody
    public Object queryAllCustomerName(String customerName){
        List<String> customerNameList=customerService.queryCustomerNameByName(customerName);
        return customerNameList;
    }

    @RequestMapping("/workbench/transaction/saveCreateTran.do")
    @ResponseBody
    public Object saveCreateTran(@RequestParam Map<String,Object> map, HttpSession session){
        System.out.println("开始了");
       map.put(Contants.SESSION_USER,session.getAttribute(Contants.SESSION_USER));
        ReturnObject returnObject=new ReturnObject();
       try {
           tranService.saveCreateTran(map);
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);
       }catch (Exception e){
           e.printStackTrace();
           returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
           returnObject.setMessage("系统忙，请稍后重试");
       }
       return returnObject;
    }

    @RequestMapping("/workbench/transaction/detailTran.do")
    public String detailTran(String id,HttpServletRequest request){
        Tran tran=tranService.queryTranForDetailById(id);
        List<TranRemark> remarkList=tranRemarkService.queryTranRemarkDetailByTranId(id);
        List<TranHistory> historyList=tranHistoryService.queryTranHistoryForDetailByTranId(id);
        ResourceBundle bundle=ResourceBundle.getBundle("possibility");
        String possibility=bundle.getString("ä»·å\u0080¼å»ºè®®");
        tran.setPossibility(possibility);
        request.setAttribute("tran",tran);
        request.setAttribute("remarkList",remarkList);
        request.setAttribute("historyList",historyList);

        //调用service方法，查询交易所有的阶段
        List<DicValue> stageList=dicValueService.queryDicValueByTypeCode("stage");
        request.setAttribute("stageList",stageList);
        return "workbench/transaction/detail";

    }
}
