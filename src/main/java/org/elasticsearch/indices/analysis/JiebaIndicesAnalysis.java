package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.JiebaAnalyzer;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;

public class JiebaIndicesAnalysis extends AbstractComponent {
	private static final String JIEBA_INDEX = "jieba_index";
	private static final String JIEBA_SEARCH = "jieba_search";
	private static final String JIEBA_OTHER = "jieba_other";

	@Inject
	public JiebaIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService, Environment env) {
		super(settings);

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_INDEX,
				new PreBuiltAnalyzerProviderFactory(JIEBA_INDEX, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer("index", env.pluginsFile().resolve("jieba/dic"), true)));

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_SEARCH,
				new PreBuiltAnalyzerProviderFactory(JIEBA_SEARCH, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer("search", env.pluginsFile().resolve("jieba/dic"), true)));

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_OTHER,
				new PreBuiltAnalyzerProviderFactory(JIEBA_OTHER, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer("other", env.pluginsFile().resolve("jieba/dic"), true)));

	}
}
