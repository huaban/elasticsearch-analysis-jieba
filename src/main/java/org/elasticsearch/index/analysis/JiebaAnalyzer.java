package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.analysis.jieba.JiebaTokenizer;
import org.elasticsearch.plugin.analysis.jieba.SentenceTokenizer;

import java.io.Reader;

public class JiebaAnalyzer extends Analyzer {
	private final ESLogger log = Loggers.getLogger(JiebaAnalyzer.class);

	private StringBuilder sb;

	public JiebaAnalyzer(Settings settings) {
		super();
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
	protected TokenStreamComponents createComponents(String fieldName,
			Reader reader) {
		Tokenizer tokenizer = new SentenceTokenizer(reader);
		TokenStream result = new JiebaTokenizer(this.sb.toString(), tokenizer);
		return new TokenStreamComponents(tokenizer, result);
	}
}
