package org.elasticsearch.index.analysis;

import java.io.Reader;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.util.CharTokenizer;
import org.apache.lucene.util.AttributeFactory;
import org.apache.lucene.util.Version;

/**
 * A OtherTokenizer is a tokenizer that do nothing with text.
 * <p>
 * <a name="version"/> You must specify the required {@link Version}
 * compatibility when creating {@link OtherTokenizer}:
 * <ul>
 * <li>As of 3.1, {@link CharTokenizer} uses an int based API to normalize and
 * detect token characters. See {@link CharTokenizer#isTokenChar(int)} and
 * {@link CharTokenizer#normalize(int)} for details.</li>
 * </ul>
 * </p>
 */

public class OtherTokenizer extends CharTokenizer {

    /**
     * Construct a new OtherTokenizer.
     * 
     * @param matchVersion
     *            Lucene version to match See
     *            {@link <a href="#version">above</a>}
     * @param in
     *            the input to split up into tokens
     */
    public OtherTokenizer(Version matchVersion, Reader in) {
        super(matchVersion, in);
    }

    /**
     * Construct a new OtherTokenizer using a given
     * {@link org.apache.lucene.util.AttributeSource.AttributeFactory}.
     * 
     * @param matchVersion
     *            Lucene version to match See
     *            {@link <a href="#version">above</a>}
     * @param factory
     *            the attribute factory to use for this {@link Tokenizer}
     * @param in
     *            the input to split up into tokens
     */
    public OtherTokenizer(Version matchVersion, AttributeFactory factory,
            Reader in) {
        super(matchVersion, factory, in);
    }

    /**
     * Collects only characters which satisfy {@link Character#isOther(int)}.
     */
    @Override
    protected boolean isTokenChar(int c) {
        return true;
    }
}
