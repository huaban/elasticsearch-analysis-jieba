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

	public JiebaAnalyzer(Settings settings) {
		super();
		ip = settings.get("ip", "172.31.255.10");
		port = settings.getAsInt("port", 8000);
		type = settings.get("t", "normal");
		log.info("ip:{} port:{} type:{}", ip, port, type);
	}

	@Override
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		return new TokenStreamComponents(new JiebaTokenizer(ip, port, type, reader));
	}
}
