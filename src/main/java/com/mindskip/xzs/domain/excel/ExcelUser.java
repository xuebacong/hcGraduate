package com.mindskip.xzs.domain.excel;
import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Rousimian6
 * @ClassName User
 * @date 2023/4/26 16:56
 */
@Data
public class ExcelUser {
    @ExcelProperty(value = "用户名", index = 0)
    private String userName;
    @ExcelProperty(value = "密码", index = 1)
    private String password;

    /**
     * 真实姓名
     */
    @ExcelProperty(value = "真实姓名", index = 2)
    private String realName;
    @ExcelProperty(value = "年龄", index = 3)
    private Integer age;

    /**
     * 1.男 2女
     */
    @ExcelProperty(value = "性别", index = 4)
    private Integer sex;
    @ExcelProperty(value = "生日", index = 5)
    private String birthDay;

    /**
     * 学生年级(1-12)
     */
    @ExcelProperty(value = "年级", index = 6)
    private Integer userLevel;
    @ExcelProperty(value = "手机号码", index = 7)
    private String phone;
}
