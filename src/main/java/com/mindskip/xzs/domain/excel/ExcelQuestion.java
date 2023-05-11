package com.mindskip.xzs.domain.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.mindskip.xzs.viewmodel.admin.question.QuestionEditItemVM;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @author Rousimian6
 * @ClassName ExcelQuestion
 * @date 2023/4/28 0:42
 */
@Data
public class ExcelQuestion {


    @ExcelProperty(value = "题目类型", index = 0)
    private Integer questionType;


    @ExcelProperty(value = "学科id", index =1)
    private Integer subjectId;


    @ExcelProperty(value = "分数", index = 2)
    private Integer score;


    @ExcelProperty(value = "年级", index = 3)
    private Integer gradeLevel;


    @ExcelProperty(value = "难度", index = 4)
    private Integer difficult;


    @ExcelProperty(value = "正确答案", index = 5)
    private String correct;


    @ExcelProperty(value = "文本", index = 6)
    private String infoTextContent;


    @ExcelProperty(value = "出题人", index = 7)
    private Integer createUser;



}
