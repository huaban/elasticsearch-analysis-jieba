package org.elasticsearch.index.analysis;


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
     */
    public OtherTokenizer() {
        super();
    }

    /**
     * Construct a new OtherTokenizer using a given
     * {@link org.apache.lucene.util.AttributeFactory}.
     *
     * @param factory
     *            the attribute factory to use for this {@link Tokenizer}
     */
    public OtherTokenizer(AttributeFactory factory) {
        super(factory);
    }

    /**
     * Collects only characters which satisfy {@link Character#isOther(int)}.
     */
    @Override
    protected boolean isTokenChar(int c) {
        return true;
    }
}
