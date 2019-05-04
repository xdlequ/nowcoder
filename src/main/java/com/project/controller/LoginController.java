package com.project.controller;

import com.project.async.EventModel;
import com.project.async.EventProducer;
import com.project.async.EventType;
import com.project.dao.LoginTicketDao;
import com.project.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by ql on 2019/5/2.
 */
@Controller
public class LoginController {
    private static final Logger log= LoggerFactory.getLogger(LoginController.class);
    @Autowired
    UserService userService;
    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = {"/reg/"},method = {RequestMethod.POST})
    public String register(Model model, @RequestParam("username")String username, @RequestParam("password")String password,
                           @RequestParam(value ="next",required = false)String next, @RequestParam(value = "remember",defaultValue = "false")boolean rememberme,
                           HttpServletResponse response){
        try {
            Map<String,Object>map=userService.register(username,password);
            if (map.containsKey("ticket")){
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());
                //.可在同一应用服务器内共享方法：设置cookie.setPath("/");
                cookie.setPath("/");
                if (rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                if (StringUtils.isNotBlank(next)){
                    return "redirect:"+next;
                }
                return "redirect:/";
            }
            else{
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
        }catch (Exception e){
            log.info("注册异常"+e.getMessage());
            model.addAttribute("msg","服务器错误");
            return "login";
        }
        //return "";
    }
    @RequestMapping(path = {"/login/"},method = {RequestMethod.POST})
    public String login(Model model, @RequestParam("username")String username, @RequestParam("password")String password,
                           @RequestParam(value ="next",required = false)String next, @RequestParam(value = "remember",defaultValue = "false")boolean rememberme,
                           HttpServletResponse response){
        try {
            Map<String,Object>map=userService.login(username,password);
            if (!map.containsKey("ticket")){
                model.addAttribute("msg",map.get("msg"));
                return "login";
            }
            else{
                Cookie cookie=new Cookie("ticket",map.get("ticket").toString());

                cookie.setPath("/");
                //.可在同一应用服务器内共享方法：设置cookie.setPath("/");
                if (rememberme){
                    cookie.setMaxAge(3600*24*5);
                }
                response.addCookie(cookie);
                //此处需要添加事件处理机制
                eventProducer.fireEvent(new EventModel(EventType.LOGIN)
                        .setExt("username", username).setExt("email", "ql085211ok@163.com")
                        .setActorId((int)map.get("userId")));

                if (StringUtils.isNotBlank(next)){
                    return "redirect"+next;
                }
                return "redirect:/";
            }
        }catch (Exception e){
            log.info("登录异常"+e.getMessage());
            model.addAttribute("msg","服务器错误");
            return "login";
        }
    }

    @RequestMapping(path = {"/reglogin"},method = {RequestMethod.GET})
    public String regLoginPage(Model model,@RequestParam(value ="next",defaultValue = "false")String next){
        model.addAttribute("next",next);
        return "login";
    }

    @RequestMapping(path = {"/logout"},method = {RequestMethod.GET,RequestMethod.POST})
    public String logout(@CookieValue("ticket")String ticket){
        userService.logout(ticket);
        return "redirect:/";
    }

}
