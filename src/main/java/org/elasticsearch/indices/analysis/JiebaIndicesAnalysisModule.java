package org.elasticsearch.indices.analysis;

import org.elasticsearch.common.inject.AbstractModule;

/**
 * <p>Title: JiebaIndicesAnalysisModule</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2016</p>
 * <p>Company: Solvento Soft</p>
 * <p>Created Date: 2016/7/21 下午4:53</p>
 *
 * @author Rex Chien
 * @version 1.0
 */
public class JiebaIndicesAnalysisModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(JiebaIndicesAnalysis.class).asEagerSingleton();
	}
}
