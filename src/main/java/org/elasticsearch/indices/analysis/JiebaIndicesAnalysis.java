package org.elasticsearch.indices.analysis;

import com.huaban.analysis.jieba.WordDictionary;
import org.elasticsearch.common.component.AbstractComponent;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.analysis.AnalyzerScope;
import org.elasticsearch.index.analysis.JiebaAnalyzer;
import org.elasticsearch.index.analysis.PreBuiltAnalyzerProviderFactory;

import java.io.File;

/**
 * <p>Title: JiebaIndicesAnalysis</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Solvento Soft</p>
 * <p>Created Date: 2016/7/21 下午4:54</p>
 *
 * @author Rex Chien
 * @version 1.0
 */
public class JiebaIndicesAnalysis extends AbstractComponent {
	private static final String JIEBA_INDEX = "jieba_index";
	private static final String JIEBA_SEARCH = "jieba_search";
	private static final String JIEBA_OTHER = "jieba_other";

	@Inject
	public JiebaIndicesAnalysis(Settings settings, IndicesAnalysisService indicesAnalysisService, Environment env) {
		super(settings);

		String type = settings.get("seg_mode", "index");
		File configFile = env.configFile().toFile();
		boolean stop = settings.getAsBoolean("stop", true);

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_INDEX,
				new PreBuiltAnalyzerProviderFactory(JIEBA_INDEX, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer(type, configFile, stop)));

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_SEARCH,
				new PreBuiltAnalyzerProviderFactory(JIEBA_SEARCH, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer(type, configFile, stop)));

		indicesAnalysisService.analyzerProviderFactories().put(JIEBA_OTHER,
				new PreBuiltAnalyzerProviderFactory(JIEBA_OTHER, AnalyzerScope.GLOBAL,
						new JiebaAnalyzer(type, configFile, stop)));
	}
}
