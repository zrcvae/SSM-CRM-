package crm.settings.web.controller;

import com.sun.deploy.net.HttpResponse;
import crm.commons.contants.Contants;
import crm.commons.domain.ReturnObject;
import crm.commons.utils.DateUtils;
import crm.settings.domain.User;
import crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /*
    url要和controller方法处理完请求之后，响应信息返回的页面资源目录保持一致

     */
    @RequestMapping("/settings/qx/user/toLogin.do")
    public String toLogin(){
        //请求转发到登陆页面
        return "settings/qx/user/login";
    }

    @RequestMapping("/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd, HttpServletRequest request, HttpSession session, HttpServletResponse response){
        //封装参数
        Map<String,Object> map=new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user=userService.queryUserByLoginActAndPwd(map);
        //根据查询结果生成响应信息
        ReturnObject returnObject=new ReturnObject();
        if(user==null){
            //登陆失败，用户或者密码错误
            returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
            returnObject.setMessage("用户或者密码错误");
        }else{
            String nowStr=DateUtils.formateDateTime(new Date());
            if(nowStr.compareTo(user.getExpireTime())>0){
                //登陆失败，账号已过期
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("账号已过期");
            }else if("0".equals(user.getLockState())){
                //登陆失败，状态被锁定
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("状态被锁定");
            }else if(!user.getAllowIps().contains(request.getRemoteAddr())){
                //登陆失败，ip受限
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_FAIL);
                returnObject.setMessage("ip受限");
            }else {
                //登陆成功
                returnObject.setCode(Contants.RETURN_OBJECT_CODE_SUCCESS);

                //把user保存到session中，因为user对象数据需要出现在多次请求中
                session.setAttribute("sessionUser",user);

                //如果需要记住密码，则往外写cookie
                if("true".equals(isRemPwd)){
                    Cookie c1=new Cookie("loginAct",user.getLoginAct());
                    c1.setMaxAge(10*24*60*60);
                    response.addCookie(c1);
                    Cookie c2=new Cookie("loginPwd",user.getLoginPwd());
                    c2.setMaxAge(10*24*60*60);
                    response.addCookie(c2);

                }else {
                    //把没有过期的cookie删除
                    Cookie c1=new Cookie("loginAct","1");
                    c1.setMaxAge(0);
                    response.addCookie(c1);
                    Cookie c2=new Cookie("loginPwd","1");
                    c2.setMaxAge(0);
                    response.addCookie(c2);
                }


            }
        }
        return returnObject;
    }

    @RequestMapping("/settings/qx/user/logout.do")
    public String logout(HttpServletResponse response,HttpSession session){
        //清空cookie
        Cookie c1=new Cookie("loginAct","1");
        c1.setMaxAge(0);
        response.addCookie(c1);
        Cookie c2=new Cookie("loginPwd","1");
        c2.setMaxAge(0);
        response.addCookie(c2);
        //销毁session
        session.invalidate();
        //跳转到首页，用重定向
        return "redirect:/";
    }

}
