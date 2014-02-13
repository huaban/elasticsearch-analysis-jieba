package org.elasticsearch.index.analysis;

import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.util.CharArraySet;
import org.apache.lucene.analysis.util.WordlistLoader;
import org.apache.lucene.util.IOUtils;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.logging.ESLogger;
import org.elasticsearch.common.logging.Loggers;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;

import com.huaban.analysis.jieba.WordDictionary;

public class JiebaAnalyzer extends Analyzer {
    private final ESLogger log = Loggers.getLogger(JiebaAnalyzer.class);

    private final CharArraySet stopWords;

    private static final String DEFAULT_STOPWORD_FILE = "stopwords.txt";

    private static final String STOPWORD_FILE_COMMENT = "//";

    /**
     * Returns an unmodifiable instance of the default stop-words set.
     *
     * @return an unmodifiable instance of the default stop-words set.
     */
    public static CharArraySet getDefaultStopSet() {
        return DefaultSetHolder.DEFAULT_STOP_SET;
    }

    /**
     * Atomically loads the DEFAULT_STOP_SET in a lazy fashion once the outer class accesses the
     * static final set the first time.;
     */
    private static class DefaultSetHolder {
        static final CharArraySet DEFAULT_STOP_SET;

        static {
            try {
                DEFAULT_STOP_SET = loadDefaultStopWordSet();
            } catch (IOException ex) {
                // default set should always be present as it is part of the
                // distribution (JAR)
                throw new RuntimeException("Unable to load default stopword set");
            }
        }

        static CharArraySet loadDefaultStopWordSet() throws IOException {
            // make sure it is unmodifiable as we expose it in the outer class
            return CharArraySet.unmodifiableSet(WordlistLoader.getWordSet(IOUtils
                    .getDecodingReader(JiebaAnalyzer.class, DEFAULT_STOPWORD_FILE,
                            IOUtils.CHARSET_UTF_8), STOPWORD_FILE_COMMENT, Version.LUCENE_CURRENT));
        }
    }

    private File configFile;
    private String type;

    public JiebaAnalyzer(Settings indexSettings, Settings settings) {
        super();
        type = settings.get("seg_mode", "index");
        boolean stop = settings.getAsBoolean("stop", true);
        stopWords = stop ? DefaultSetHolder.DEFAULT_STOP_SET : CharArraySet.EMPTY_SET;

        Environment env = new Environment(indexSettings);
        configFile = env.configFile();
        WordDictionary.getInstance().init(configFile);
    }

    public JiebaAnalyzer(String segMode, File configFile, boolean isStop) {
        super();
        this.type = segMode;
        this.stopWords = isStop ? DefaultSetHolder.DEFAULT_STOP_SET : CharArraySet.EMPTY_SET;
        this.configFile = configFile;
        WordDictionary.getInstance().init(new File(configFile, "jieba"));
    }

    @Override
    protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
        Tokenizer tokenizer;
        if (type.equals("other")) {
            tokenizer = new OtherTokenizer(Version.LUCENE_CURRENT, reader);
        } else {
            tokenizer = new SentenceTokenizer(reader);
        }
        TokenStream result = new JiebaTokenFilter(type, tokenizer);
        if (!type.equals("other") && !stopWords.isEmpty()) {
            result = new StopFilter(Version.LUCENE_CURRENT, result, stopWords);
        }
        return new TokenStreamComponents(tokenizer, result);
    }
}
