package com.jk.ndt.etl.dao.access;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.access.UploadInfo;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.service.access.SheetInfoService;

public class AccessMapperTest extends SpringTest {
    @Autowired
    private SheetInfoDao sheetInfoMapper;
    @Autowired
    private SheetInfoService sheetInfoService;
    @Autowired
    private UploadInfoDao uploadInfoMapper;

    @Test
    public void testInsert() {
         UploadInfo uploadInfo = new UploadInfo();
         Admin user=new Admin();
//         user.setUser_id(new BigDecimal(1));
         user.setName("user");
         uploadInfo.setUser(user);
         uploadInfo.setFileName("test1");
         uploadInfo.setStatus(1);
         uploadInfo.setUploadDate(new Date());
         uploadInfoMapper.save(uploadInfo);

        // SheetInfo sheetInfo=new SheetInfo();
        // sheetInfo.setStatus(20);
        // sheetInfo.setUploadId(5);
        // sheetInfoService.save(sheetInfo);
        // sheetInfoService.save(sheetInfo);
        // sheetInfoService.save(sheetInfo);
        // sheetInfoService.save(sheetInfo);
        // sheetInfoService.save(sheetInfo);
    }

    @Test
    public void testDelete() {
        // uploadInfoMapper.deleteById(4);
        // sheetInfoMapper.deleteById(3);
    }

    @Test
    public void testGetById() {
        // System.out.println(uploadInfoMapper.getById(5));
        // System.out.println(sheetInfoMapper.getById(3));
    }

    @Test
    public void testGetBySha1() {
        // System.out.println(uploadInfoMapper.getBySha1("544ca2fddf99af2f33c19e3892680cd9109adf6b"));
        // System.out.println(sheetInfoMapper.getById(3));
    }

    @Test
    public void testListAll() {
        // System.out.println(uploadInfoMapper.listAll());
        // System.out.println(sheetInfoMapper.listAll());
    }

    @Test
    public void testListByPage() {
        Page page = new Page();
//        Map<String, Object> param=new HashMap<>();
//        param.put("upload_id", 27);
//        page.setParam(param);
        // System.out.println(uploadInfoMapper.listAll());
        PageHelper.startPage(page.getCurrent(), page.getPage_size());
        List<SheetInfo> listByPage = sheetInfoMapper.listByPage(page);
        PageInfo pageInfo = new PageInfo(listByPage);
        List list = pageInfo.getList();
        System.out.println("=============="+list);
    }

}
