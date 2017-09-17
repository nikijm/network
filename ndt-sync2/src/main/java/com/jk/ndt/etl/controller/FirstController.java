package com.jk.ndt.etl.controller;


import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class FirstController {


    @RequestMapping(value = "/add")
    @ResponseBody
    public ResponseEntity add() {
        return ResponseEntity.ok("ok");
    }

}
