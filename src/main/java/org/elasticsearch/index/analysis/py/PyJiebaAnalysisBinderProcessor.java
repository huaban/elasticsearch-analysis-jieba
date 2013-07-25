package org.elasticsearch.index.analysis.py;

import org.elasticsearch.index.analysis.AnalysisModule;


public class PyJiebaAnalysisBinderProcessor extends
		AnalysisModule.AnalysisBinderProcessor {

	@Override
	public void processTokenFilters(TokenFiltersBindings tokenFiltersBindings) {
		tokenFiltersBindings.processTokenFilter("jieba", PyJiebaTokenFilterFactory.class);
		super.processTokenFilters(tokenFiltersBindings);
	}	
	
	

	@Override
	public void processTokenizers(TokenizersBindings tokenizersBindings) {
		tokenizersBindings.processTokenizer("jieba", PyJiebaTokenizerFactory.class);
		super.processTokenizers(tokenizersBindings);
	}



	@Override
	public void processAnalyzers(AnalyzersBindings analyzersBindings) {
		analyzersBindings.processAnalyzer("jieba", PyJiebaAnalyzerProvider.class);
		super.processAnalyzers(analyzersBindings);
	}

}
