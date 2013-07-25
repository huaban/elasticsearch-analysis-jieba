package org.elasticsearch.index.analysis.py;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import java.io.File;
import java.io.Reader;

public class PyJiebaAnalyzer extends Analyzer {
	private final ESLogger log = Loggers.getLogger(PyJiebaAnalyzer.class);
	
	private File configFile;
	private File pluginFile;
	private String type;

	public PyJiebaAnalyzer(Settings indexSettings, Settings settings) {
		super();
		type = settings.get("t", "index");
		Environment env = new Environment(indexSettings);
	    configFile = env.configFile();
	    pluginFile = env.pluginsFile();
		log.info("type:{}", type);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		return new TokenStreamComponents(new PyJiebaTokenizer(type, pluginFile, configFile, reader));
	}
}
