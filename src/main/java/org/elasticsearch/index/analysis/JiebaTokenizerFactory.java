package org.elasticsearch.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;

public class JiebaTokenizerFactory extends AbstractTokenizerFactory {
	private final ESLogger log = Loggers.getLogger(JiebaTokenizerFactory.class);

	private String ip;
	private Integer port;
	private String type;
	
	@Inject
	public JiebaTokenizerFactory(Index index, Settings indexSettings,
			String name, Settings settings) {
		super(index, indexSettings, name, settings);
		ip = settings.get("ip", "172.31.255.10");
		port = settings.getAsInt("port", 8000);
		type = settings.get("t", "normal");
		log.info("ip:{} port:{} type:{}", ip, port, type);
	}

	@Override
	public Tokenizer create(Reader reader) {
		return new JiebaTokenizer(this.ip, this.port, this.type, reader);
	}

}
