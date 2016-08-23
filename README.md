结巴分词 ElasticSearch 插件
===========================

集成 Lucene / Jieba Analyzer，支持自定义词典。


| Jieba Chinese Analysis Plugin | ElasticSearch | Analyzer       |
|-------------------------------|---------------|----------------|
| 0.0.2                         | 1.0.0RC2      | 0.0.2          |
| 0.0.3-SNAPSHOT                | 1.3.0         | 1.0.0          |
| 0.0.4                         | 1.5.x         | 1.0.2          |
| 2.3.3                         | 2.3.3         | 1.0.2          |
| 2.3.4                         | 2.3.4         | 1.0.2          |


> 本插件包括 `jieba analyzer`、`jieba tokenizer`、`jieba token filter`，有三种模式供选择。

-	index 主要用于索引分词，分词粒度较细
-	search 主要用于查询分词，分词粒度较粗
-	other 全角转半角、大写转小写、字符分词

安装
----

## ES 2.x 以上版本

> 插件版本跟 ES 版本保持一致

**2.3.3**
```sh
./bin/plugin install https://github.com/huaban/elasticsearch-analysis-jieba/releases/download/v2.3.3/elasticsearch-analysis-jieba-2.3.3-bin.zip
```

## ES 2.x 以下版本

> 请使用插件 0.0.4 版本编译安装

```sh
cd {your_es_path}
mkdir plugins/jieba

# 拷贝 jar
copy jieba-analysis-1.0.2.jar and elasticsearch-analysis-jieba-0.0.4.jar to plugins/jieba

# 拷贝用户字典
cp -r data/jieba {your_es_path}/config/
```

测试
----

```sh
curl -XPUT 127.0.0.1:9200/test -d '{
    "settings" : {
        "number_of_shards" : 1,
        "number_of_replicas" : 0

    },
    "mappings" : {
        "test" : {
            "_all" : { "enabled" : false },
            "properties" : {
                "name" : { "type" : "string", "analyzer" : "jieba_index", "search_analyzer" : "jieba_search" }
            }
        }
    }
}';echo



curl 'http://127.0.0.1:9200/test/_analyze?analyzer=jieba_index' -d '中华人民共和国';echo
curl 'http://127.0.0.1:9200/test/_analyze?analyzer=jieba_search' -d '中华人民共和国';echo
curl 'http://127.0.0.1:9200/test/_analyze?analyzer=jieba_other' -d '中华人民共和国 HelLo';echo
```

如何发布一个版本
------


```
github-release release \
    --user huaban \
    --repo elasticsearch-analysis-jieba \
    --tag v2.3.4 \
    --name "v2.3.4" \
    --description "支持 ES v2.3.4"

github-release upload \
    --user huaban \
    --repo elasticsearch-analysis-jieba \
    --tag v2.3.4 \
    --name "v2.3.4" \
    --label "ES2.3.4" \
    --file target/releases/elasticsearch-analysis-jieba-2.3.4-bin.zip
```

License
-------

```
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
```
