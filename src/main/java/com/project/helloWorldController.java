package com.project;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ql on 2019/5/1.
 */
@Controller
public class helloWorldController {
    @RequestMapping("/hello/world")
    public String world(){
        return "index";
    }

    @RequestMapping(path = {"/test","/template"})
    public String template(Model model){
        model.addAttribute("value1","vvvvv1");
        List<String> colors= Arrays.asList(new String[]{"sunbo","yangshen","jibo"});
        model.addAttribute("colors",colors);
        return "home";
    }
}
