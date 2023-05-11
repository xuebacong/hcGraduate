package com.mindskip.xzs.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * @author Rousimian6
 * @ClassName Excel
 * @date 2023/4/27 23:59
 */
@Data
public class ExcelSubject {
    @ExcelProperty(value = "学科名", index = 0)
    private String name;
    @ExcelProperty(value = "年级id", index = 1)
    private Integer level;
    @ExcelProperty(value = "年级", index = 2)
    private String levelName;
    private Boolean deleted;
}
