package com.mindskip.xzs.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.mindskip.xzs.domain.excel.ExcelQuestion;
import com.mindskip.xzs.domain.excel.ExcelSubject;

/**
 * @author Rousimian6
 * @ClassName EasyExcelListenerQuestion
 * @date 2023/5/2 18:40
 */
public class EasyExcelListenerQuestion extends AnalysisEventListener<ExcelQuestion> {
    /**
     * 此方法每一条数据解析都会来调用
     *
     * @param data
     * @param context
     */
    @Override
    public void invoke(ExcelQuestion data, AnalysisContext context){
    }

    /**
     * 所有数据解析完成都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
