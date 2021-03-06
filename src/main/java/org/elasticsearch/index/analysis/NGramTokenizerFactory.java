/*
 * Licensed to ElasticSearch and Shay Banon under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. ElasticSearch licenses this
 * file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.index.analysis;

import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.ngram.NGramTokenizer;
import org.apache.lucene.analysis.ngram.XNGramTokenizer;
import org.apache.lucene.util.Version;
import org.elasticsearch.common.inject.Inject;
import org.elasticsearch.common.inject.assistedinject.Assisted;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.Index;
import org.elasticsearch.index.settings.IndexSettings;

import java.io.Reader;

/**
 *
 */
public class NGramTokenizerFactory extends AbstractTokenizerFactory {

    private final int minGram;

    private final int maxGram;

    @Inject
    public NGramTokenizerFactory(Index index, @IndexSettings Settings indexSettings, @Assisted String name, @Assisted Settings settings) {
        super(index, indexSettings, name, settings);
        this.minGram = settings.getAsInt("min_gram", NGramTokenizer.DEFAULT_MIN_NGRAM_SIZE);
        this.maxGram = settings.getAsInt("max_gram", NGramTokenizer.DEFAULT_MAX_NGRAM_SIZE);
    }

    @Override
    public Tokenizer create(Reader reader) {
        if (this.version.onOrAfter(Version.LUCENE_42)) {
            // LUCENE MONITOR: this token filter is a copy from lucene trunk and should go away once we upgrade to lucene 4.4 
            return new XNGramTokenizer(version, reader, minGram, maxGram);
        }
        return new NGramTokenizer(reader, minGram, maxGram);
    }
}