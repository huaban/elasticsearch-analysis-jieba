Jieba Analysis for ElasticSearch
================================

The Jieba Analysis plugin integrates Lucene / Jieba Analyzer into elasticsearch, support customized dictionary.

Based on [huaban/elasticsearch-analysis-jieba](https://github.com/huaban/elasticsearch-analysis-jieba), try to support ES 2.3.X.


| Jieba Chinese Analysis Plugin | ElasticSearch | Analyzer       |
|-------------------------------|---------------|----------------|
| 0.0.2                         | 1.0.0RC2      | 0.0.2          |
| 0.0.3-SNAPSHOT                | 1.3.0         | 1.0.0          |
| 0.0.4                         | 1.5.x         | 1.0.2          |
| 1.0.0                         | 2.3.3         | 1.0.2          |

The plugin includes the `jieba` analyzer, `jieba` tokenizer, and `jieba` token filter, and have two mode you can choose. one is `index` which means it will be used when you want to index a document. another is `search` mode which used when you want to search something.

Installation
------------

**compile and package current project**

```
mvn package
```

**make a direcotry in elasticsearch' plugin directory**

```
cd {your_es_path}
mkdir plugins/jieba
```

**unzip released zip file to plugins/jieba**

```
unzip target/releases elasticsearch-analysis-jieba-1.0.0.zip -d {your_es_path}/plugins/jieba
```

that's all!

Add Analysis Configuration (elasticsearch.yml)
------------------------------------------------------

```
index :
  analysis :
    analyzer :
      jieba_search :
        type : jieba
        seg_mode : search
        stop : true
      jieba_other :
        type : jieba
        seg_mode : other
        stop : true
      jieba_index :
        type : jieba
        seg_mode : index
        stop : true
```

test

```sh
# index mode
curl 'http://localhost/test/_analyze?analyzer=jieba_index' -d '中华人民共和国';echo
```

result

```javascript
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

```sh
# search mode
curl '0:9200/test/_analyze?analyzer=jieba_search' -d '中华人民共和国';echo
```

result

```javascript
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

```sh
# other mode 大小写全半角
curl '0:9200/test/_analyze?analyzer=jieba_other' -d '中华人民共和国 HeLlo';echo
```

result

```javascript
{
    "tokens": [
        {
            "token": "中华人民共和国 hello",
            "start_offset": 0,
            "end_offset": 13,
            "type": "word",
            "position": 1
        }
    ]
}
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
