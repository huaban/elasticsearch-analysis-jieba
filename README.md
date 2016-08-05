Jieba Analysis for ElasticSearch
================================

The Jieba Analysis plugin integrates Lucene / Jieba Analyzer into elasticsearch, support customized dictionary.

Based on [huaban/elasticsearch-analysis-jieba](https://github.com/huaban/elasticsearch-analysis-jieba), try to support ES 2.3.X.


| Jieba Chinese Analysis Plugin | ElasticSearch | Analyzer       |
|-------------------------------|---------------|----------------|
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
