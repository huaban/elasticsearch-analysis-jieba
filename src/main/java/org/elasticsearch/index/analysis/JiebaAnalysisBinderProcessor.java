package org.elasticsearch.index.analysis;

public class JiebaAnalysisBinderProcessor extends
	AnalysisModule.AnalysisBinderProcessor {

    @Override
    public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
	tokenFiltersBindings.processTokenFilter("jieba",
		JiebaTokenFilterFactory.class);
	super.processTokenFilters(tokenFiltersBindings);
    }

    @Override
    public void processAnalyzers(AnalyzersBindings analyzersBindings) {
	analyzersBindings.processAnalyzer("jieba", JiebaAnalyzerProvider.class);
	super.processAnalyzers(analyzersBindings);
    }

}
