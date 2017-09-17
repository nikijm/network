package com.jk.ndt.etl.utility;

import com.jk.ndt.etl.entity.access.SheetInfo;
import com.jk.ndt.etl.entity.access.UploadInfo;

/**
 * 
 * @ClassName: TransStatusUtil 
 * @Description: int类型的状态转换为文字的工具类
 * @author lianhanwen 
 * @date 2017年5月26日 上午11:05:11 
 *
 */
public class TransStatusUtil {
    
    public static SheetInfo sheetsStatusToString(SheetInfo sheetInfo) {
        Integer status = Integer.valueOf((sheetInfo.getStatus().toString()));
        // 状态,-1:未验证,0:已验证,1:已清洗,2:已转换
        String str = null;
        switch (status) {
        case -1:
            str = "未验证";
            break;
        case 0:
            str = "已验证";
            break;
        case 1:
            str = "已清洗";
            break;
        case 2:
            str = "已转换";
            break;
        }
        sheetInfo.setStatus(str);
        return sheetInfo;
    }
    public static UploadInfo uploadsStatusToString(UploadInfo uploadInfo) {
        Integer status = Integer.valueOf((uploadInfo.getStatus().toString()));
        // -1:删除,0:未缓存,1:已缓存,2:处理中,3:清洗完毕,4:全部处理完毕;
        String str = null;
        switch (status) {
        case -1:
            str = "删除";
            break;
        case 0:
            str = "未缓存";
            break;
        case 1:
            str = "已缓存";
            break;
        case 2:
            str = "处理中";
            break;
        case 3:
            str = "清洗完毕";
            break;
        case 4:
            str = "全部处理完毕";
            break;
        }
        uploadInfo.setStatus(str);
        return uploadInfo;
    }
}
