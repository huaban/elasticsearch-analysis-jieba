package org.elasticsearch.plugin.analysis.jieba;

import org.elasticsearch.common.inject.Module;
import org.elasticsearch.index.analysis.AnalysisModule;
import org.elasticsearch.index.analysis.JiebaAnalysisBinderProcessor;
import org.elasticsearch.indices.analysis.JiebaIndicesAnalysisModule;
import org.elasticsearch.plugins.Plugin;

import java.util.Collection;
import java.util.Collections;

public class AnalysisJiebaPlugin extends Plugin {

	@Override
	public String name() {
		return "analysis-jieba";
	}

	@Override
	public String description() {
		return "jieba analysis";
	}

	@Override
	public Collection<Module> nodeModules() {
		return Collections.singletonList(new JiebaIndicesAnalysisModule());
	}


	public void onModule(AnalysisModule module) {
		module.addProcessor(new JiebaAnalysisBinderProcessor());
	}
}
