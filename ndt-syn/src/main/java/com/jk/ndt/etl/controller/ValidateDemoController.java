package com.jk.ndt.etl.controller;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jk.ndt.etl.entity.ValidateDemo;

@Controller
public class ValidateDemoController {
    /**
     * 
     * @Description: 后台验证demo  
     * @author fangwei
     * @date 2017年5月22日 上午11:04:01 
     * @return
     */
    @RequestMapping(value="/validate/demo",method=RequestMethod.POST)
    @ResponseBody
    public Object validate(@Valid ValidateDemo demo,BindingResult result){

        return demo;
    }
    /**
     * 
     * @Description: 后台验证demo  
     * @author fangwei
     * @date 2017年5月22日 上午11:04:01 
     * @return
     */
    @RequestMapping(value="/validate/demo",method=RequestMethod.GET)
    @ResponseBody
    public ModelAndView validatePage(){
        return new ModelAndView("validate_demo");
    }
}
