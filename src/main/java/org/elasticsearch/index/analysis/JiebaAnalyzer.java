package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

import java.io.Reader;

public class JiebaAnalyzer extends Analyzer {
	private final ESLogger log = Loggers.getLogger(JiebaAnalyzer.class);
	
	private String ip;
	private Integer port;
	private String type;
	private String key;

	public JiebaAnalyzer(Settings settings) {
		super();
		ip = settings.get("ip", "183.136.223.174");
		port = settings.getAsInt("port", 8000);
		type = settings.get("t", "index");
		key = settings.get("key", "H2jo8vfZk7");
		log.info("ip:{} port:{} type:{}", ip, port, type);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		return new TokenStreamComponents(new JiebaTokenizer(ip, port, type, key, reader));
	}
}
