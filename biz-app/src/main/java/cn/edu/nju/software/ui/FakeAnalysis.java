package cn.edu.nju.software.ui;

import cn.edu.nju.software.common.pojo.TracingItemInfo;
import cn.edu.nju.software.common.pojo.bizservice.FakeAnalysisResult;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Daniel
 * @since 2018/4/28 16:52
 * 赝品分析
 */
@Service
public interface FakeAnalysis {
    /**
     * 对商品的历史信息进行赝品分析
     * @param tracingItemInfoList
     * @return
     */
    FakeAnalysisResult fakeAnalysis(List<TracingItemInfo> tracingItemInfoList);
}
