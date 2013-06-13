package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.plugin.analysis.jieba.JiebaTokenizer;
import org.elasticsearch.plugin.analysis.jieba.SentenceTokenizer;

import java.io.Reader;


public class JiebaAnalyzer extends Analyzer {

  private String url;


  public JiebaAnalyzer(Settings settings) {
    super();
    this.url = settings.get("url");
  }

  @Override
  protected TokenStreamComponents createComponents(String fieldName, Reader reader) {
    Tokenizer tokenizer = new SentenceTokenizer(reader);
    TokenStream result = new JiebaTokenizer(this.url, tokenizer);
    return new TokenStreamComponents(tokenizer, result);
  }
}
