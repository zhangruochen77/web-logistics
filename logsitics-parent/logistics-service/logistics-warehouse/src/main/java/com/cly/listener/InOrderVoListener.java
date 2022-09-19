package com.cly.listener;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.cly.vo.warehouse.InOrderVo;

public class InOrderVoListener extends AnalysisEventListener<InOrderVo> {
    @Override
    public void invoke(InOrderVo inOrderVo, AnalysisContext analysisContext) {
        System.out.println("->>> " + inOrderVo);
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
        System.out.println("doAfterAllAnalysed");
    }
}
