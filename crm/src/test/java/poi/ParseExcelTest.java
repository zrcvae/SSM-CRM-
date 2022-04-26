package poi;

import org.apache.poi.hssf.usermodel.HSSFCell;

/**
 * 使用apache-poi解析excel文件
 */
public class ParseExcelTest {
    public static void main(String[] args) {

    }

    /**
     * 从指定的HSSFCell对象中获取列的值
     * @return
     */
    public static String getCellValueForStr(HSSFCell cell){
        String ret="";
        if(cell.getCellType()==HSSFCell.CELL_TYPE_STRING){
            ret=cell.getStringCellValue();
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_NUMERIC){
            ret=cell.getNumericCellValue()+"";//把任何类型转化为字符串类型
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_BOOLEAN){
            ret=cell.getBooleanCellValue()+"";
        }else if(cell.getCellType()==HSSFCell.CELL_TYPE_FORMULA){
            ret=cell.getCellFormula();
        }else {
            ret="";
        }
        return ret;
    }
}
