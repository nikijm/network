package com.jk.ndt.etl.service.logs;

import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.jk.ndt.etl.SpringTest;
import com.jk.ndt.etl.constant.Constant;
import com.jk.ndt.etl.entity.Page;
import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.logs.SheetLog;
import com.jk.ndt.etl.entity.system.Admin;
import com.jk.ndt.etl.entity.system.User;

public class SheetLogServiceTest extends SpringTest {
    @Autowired
    private SheetLogService sheetLogService;

    @Test
    public void testSave() {
        SheetInfo sheetInfo=new SheetInfo();
        sheetInfo.setId(86);

        Admin user=new Admin();
        user.setId(new BigDecimal(1));;
        user.setName("user");
        SheetLog sheetLog = new SheetLog(sheetInfo, user, Constant.SHEET_LOG_CACHED,
                new Date(), null, null, Constant.ORIGINAL_TABLE_PREFIX + sheetInfo.getId());

        sheetLogService.save(sheetLog);
    }
    @Test
    public void testDeleteCacheLogBySheetId() {
        sheetLogService.deleteCacheLogBySheetId(112);
    }


    @Test
    public void testListAll() {
        System.out.println(sheetLogService.listAll());
    }

    @Test
    public void testListByPage() {
        Page page=new Page();
        Map<String, Object> param=new HashMap<>();
        param.put("sheet_id", 86);
        page.setParam(param);
        List<SheetLog> listByPage = sheetLogService.listByPage(page);
        for (SheetLog sheetLog : listByPage) {
            
            System.out.println("%%%%%%%%%%%%%%%"+sheetLog);
        }
    }

}
