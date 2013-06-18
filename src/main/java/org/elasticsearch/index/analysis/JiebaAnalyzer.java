package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;

import java.io.Reader;

public class JiebaAnalyzer extends Analyzer {
	private final ESLogger log = Loggers.getLogger(JiebaAnalyzer.class);

	private StringBuilder sb;

	public JiebaAnalyzer(Settings settings) {
		super();
		String url = settings.get("url", "http://183.136.223.174:8000/_segment");
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
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		return new TokenStreamComponents(new JiebaTokenizer(this.sb.toString(), reader));
	}
}
