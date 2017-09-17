package com.jk.ndt.etl.service.mirror;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.entity.mirror.MirrorTable;

public class MirrorTableServiceTest extends SpringTest{
    @Autowired
    private MirrorTableService mirrorTableService;
    @Test
    public void listAllTest(){
        List<MirrorTable> listAll = mirrorTableService.listAll();
        System.out.println(listAll);
    }
}
