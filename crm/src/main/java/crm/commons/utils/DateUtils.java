package crm.commons.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/*
* 对日期类型数据进行处理到工具类
* */
public class DateUtils {
    public static String formateDateTime(Date date){
        /*
        对指定的Date对象进行格式化
         */
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr=sdf.format(date);
        return dateStr;
    }
}
