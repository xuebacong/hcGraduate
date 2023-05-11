package com.mindskip.xzs.listener;

/**
 * @author Rousimian6
 * @ClassName EasyExcelOrderListener
 * @date 2023/4/26 18:14
 */

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.xzs.domain.excel.ExcelUser;
import lombok.extern.slf4j.Slf4j;

/***
 *
 * 监听器
 *
 ***/

@Slf4j
public class EasyExcelListener extends AnalysisEventListener<ExcelUser> {

    /**
     * 此方法每一条数据解析都会来调用
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelUser data, AnalysisContext context) {
        log.info("解析到一条数据："+data);
    }

    /**
     * 所有数据解析完成都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        log.info("所有数据解析完成！！！");
    }
}