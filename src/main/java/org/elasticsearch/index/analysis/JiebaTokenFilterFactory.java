package org.elasticsearch.index.analysis;


import org.apache.lucene.analysis.TokenStream;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;

public class JiebaTokenFilterFactory extends AbstractTokenFilterFactory {
	private final ESLogger log = Loggers.getLogger(JiebaTokenFilterFactory.class);

	private StringBuilder sb;

	@Inject
	public JiebaTokenFilterFactory(Index index, Settings indexSettings,
			String name, Settings settings) {
		super(index, indexSettings, name, settings);
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
	public TokenStream create(TokenStream input) {
		return new JiebaTokenFilter(this.sb.toString(), input);
	}

}
