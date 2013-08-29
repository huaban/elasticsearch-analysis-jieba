Jieba Analysis for ElasticSearch
==================================

The Jieba Analysis plugin integrates Lucene / Jieba Analyzer into elasticsearch, support customized dictionary.

    ----------------------------------------------------
    | Jieba Chinese Analysis Plugin | ElasticSearch    |
    ----------------------------------------------------
    | 0.0.1-SNAPSHOT (master)       | 0.90.*           |
    ----------------------------------------------------

The plugin includes the `jieba` analyzer, `jieba` tokenizer, and `jieba` token filter, and have two mode you can choose. one is `index` which means it will be used when you want to index a document. another is `search` mode which used when you want to search something.


## Installation

this project was depends on [jieba-analysis](https://github.com/huaban/jieba-analysis) which you should first clone it then install in your local repository.

``` sh
# clone and install jieba-analysis to your local repository.
git clone https://github.com/huaban/jieba-analysis
cd jieba-analysis
mvn package install

# compile and package current project
git clone https://github.com/huaban/elasticsearch-analysis-jieba
cd elasticsearch-analysis-jieba
mvn package


# make a direcotry in elasticsearch' plugin directory.
cd {your_es_path}
mkdir plugins/jieba

# copy jieba-analysis-0.0.1-SNAPSHOT.jar and elasticsearch-analysis-jieba-0.0.1-SNAPSHOT.jar to plugins/jieba
cd jieba-analysis
cp target/jieba-analysis-0.0.1-SNAPSHOT.jar {your_es_path}/plugins/jieba

cd elasticsearch-analysis-jieba
cp target/elasticsearch-analysis-jieba-0.0.1-SNAPSHOT.jar {your_es_path}/plugins/jieba

# copy user dict to config/jieba
cp -r data/jieba/ {your_es_path}/config/
```

that's all!


## Usage

create mapping

``` sh
#!/bin/bash

curl -XDELETE '0:9200/test/';echo

curl -XPUT '0:9200/test/' -d '
{
    "index" : {
        "number_of_shards": 1,
        "number_of_replicas": 0,
        "analysis" : {
            "analyzer" : {
                "jieba_search" : {
                    "type" : "jieba",
                    "seg_mode" : "search",
                    "stop" : true
                },
                "jieba_index" : {
                    "type" : "jieba",
                    "seg_mode" : "index",
                    "stop" : true
                }
            }
        }
    }
}';echo
```

test

``` sh
# index mode
curl '0:9200/test/_analyze?analyzer=jieba_index' -d '中华人民共和国';echo
```

result

``` javascript
{
    "tokens": [
        {
            "token": "中华",
            "start_offset": 0,
            "end_offset": 2,
            "type": "word",
            "position": 1
        },
        {
            "token": "华人",
            "start_offset": 1,
            "end_offset": 3,
            "type": "word",
            "position": 2
        },
        {
            "token": "人民",
            "start_offset": 2,
            "end_offset": 4,
            "type": "word",
            "position": 3
        },
        {
            "token": "共和",
            "start_offset": 4,
            "end_offset": 6,
            "type": "word",
            "position": 4
        },
        {
            "token": "共和国",
            "start_offset": 4,
            "end_offset": 7,
            "type": "word",
            "position": 5
        },
        {
            "token": "中华人民共和国",
            "start_offset": 0,
            "end_offset": 7,
            "type": "word",
            "position": 6
        }
    ]
}
```

``` sh
# search mode
curl '0:9200/test/_analyze?analyzer=jieba_search' -d '中华人民共和国';echo
```

result

``` javascript
{
    "tokens": [
        {
            "token": "中华人民共和国",
            "start_offset": 0,
            "end_offset": 7,
            "type": "word",
            "position": 1
        }
    ]
}
```

License
-------

    This software is licensed under the Apache 2 license, quoted below.

    Copyright (C) 2013 libin and Huaban Inc<http://www.huaban.com>

    Licensed under the Apache License, Version 2.0 (the "License"); you may not
    use this file except in compliance with the License. You may obtain a copy of
    the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
    WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
    License for the specific language governing permissions and limitations under
    the License.


