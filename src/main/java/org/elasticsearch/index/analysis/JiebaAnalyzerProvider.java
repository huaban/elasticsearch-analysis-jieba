package org.elasticsearch.index.analysis;

import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettingsService;

public class JiebaAnalyzerProvider extends
        AbstractIndexAnalyzerProvider<JiebaAnalyzer> {
    private final JiebaAnalyzer analyzer;

    @Inject
    public JiebaAnalyzerProvider(Index index, IndexSettingsService indexSettings, Environment env, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings.getSettings(), name, settings);
        analyzer = new JiebaAnalyzer(indexSettings, settings);
    }

    @Override
    public JiebaAnalyzer get() {
        return this.analyzer;
    }
}
