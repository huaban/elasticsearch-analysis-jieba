package org.elasticsearch.index.analysis;

import java.io.File;
import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.analysis.AbstractTokenizerFactory;

public class PyJiebaTokenizerFactory extends AbstractTokenizerFactory {
	private final ESLogger log = Loggers.getLogger(PyJiebaTokenizerFactory.class);

	private String type;
	private File configFile;
	
	@Inject
	public PyJiebaTokenizerFactory(Index index, Settings indexSettings,
			String name, Settings settings) {
		super(index, indexSettings, name, settings);
		type = settings.get("t", "index");
		Environment env = new Environment(indexSettings);
		configFile = env.configFile();
		PyJiebaSegmenter.init(configFile);
		log.info("type:{}", type);
	}

	@Override
	public Tokenizer create(Reader reader) {
		return new PyJiebaTokenizer(type, reader);
	}

}
