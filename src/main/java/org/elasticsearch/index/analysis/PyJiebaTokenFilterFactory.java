package org.elasticsearch.index.analysis;


import java.io.File;

import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenFilterFactory;

public class PyJiebaTokenFilterFactory extends AbstractTokenFilterFactory {
	private final ESLogger log = Loggers.getLogger(PyJiebaTokenFilterFactory.class);
	private String type;
	private File configFile;

	@Inject
	public PyJiebaTokenFilterFactory(Index index, Settings indexSettings,
			String name, Settings settings) {
		super(index, indexSettings, name, settings);
		type = settings.get("t", "index");
		Environment env = new Environment(indexSettings);
		configFile = env.configFile();
		PyJiebaSegmenter.init(configFile);
		log.info("type:{}", type);
	}

	@Override
	public TokenStream create(TokenStream input) {
		return new PyJiebaTokenFilter(type, input);
	}

}
