package org.elasticsearch.plugin.analysis.jieba;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.py.PyJiebaAnalysisBinderProcessor;
import org.elasticsearch.plugins.AbstractPlugin;


public class AnalysisJiebaPlugin extends AbstractPlugin {

    @Override public String name() {
        return "analysis-jieba";
    }


    @Override public String description() {
        return "jieba analysis";
    }


    @Override public void processModule(Module module) {
        if (module instanceof AnalysisModule) {
            AnalysisModule analysisModule = (AnalysisModule) module;
            analysisModule.addProcessor(new PyJiebaAnalysisBinderProcessor());
        }
    }
}
