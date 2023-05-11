package com.mindskip.xzs.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.xzs.domain.excel.ExcelSubject;
import com.mindskip.xzs.domain.excel.ExcelUser;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Rousimian6
 * @ClassName EasyExcelListenerSubject
 * @date 2023/4/28 23:07
 */
@Slf4j
public class EasyExcelListenerSubject extends AnalysisEventListener<ExcelSubject> {
    /**
     * 此方法每一条数据解析都会来调用
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelSubject data, AnalysisContext context) {
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
