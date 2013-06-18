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

	private StringBuilder sb;
	
	@Inject
	public JiebaTokenizerFactory(Index index, Settings indexSettings,
			String name, Settings settings) {
		super(index, indexSettings, name, settings);
		String url = settings.get("url");
		if (null == url)
			throw new IllegalArgumentException("url is null!");
		log.info("url:{}", url);
		this.sb = new StringBuilder();
		this.sb.append(url);
		String t = settings.get("t");
		if (t != null)
			this.sb.append("?type=").append(t);
	}

	@Override
	public Tokenizer create(Reader reader) {
		return new JiebaTokenizer(this.sb.toString(), reader);
	}

}
