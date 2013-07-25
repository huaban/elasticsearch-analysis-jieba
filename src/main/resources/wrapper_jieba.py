#!/usr/bin/python
# -*- coding: utf-8 -*-


import jieba
import jieba.posseg as pseg

stop_words = set()
    
def initialize():
    jieba.initialize()


def load_user_dict(dict_path):
    jieba.load_userdict(dict_path)
    print 'finish load user dict[%s].' % dict_path

def load_stop_words(dict_path):
    with file(dict_path) as f:
        content = f.read().decode('utf-8')
        for line in content.split("\n"):
            stop_words.add(line.strip())
        stop_words.add(' ')
    f.closed
    print 'finish load stop words.'

def __q2b(ustring):
    """把字符串全角转半角"""
    if not isinstance(ustring, unicode):
        try:
            ustring = ustring.decode('utf-8')
        except:
            ustring = ustring.decode('gbk','ignore')
    rstring = ""
    for uchar in ustring:
        inside_code=ord(uchar)
        # 全角字符unicode编码从65281~65374 （十六进制 0xFF01 ~ 0xFF5E）
        # 半角字符unicode编码从33~126 （十六进制 0x21~ 0x7E）
        # 而且除空格外,全角/半角按unicode编码排序在顺序上是对应的
        if inside_code==0x3000:
            # 空格 全角为0x3000 半角为0x0020
            inside_code=0x0020
        elif inside_code == 0x3002:
            # 中文句号
            inside_code=0x2e

        else:
            inside_code-=0xfee0
        if inside_code<0x0020 or inside_code>0x7e:      
            rstring += uchar
        else:
            rstring += unichr(inside_code)
    return rstring

def __filter(result):
    """
    """
    for w in result:
        if w["word"] in stop_words:
            result.remove(w)
    return result

def __prepare(raw):
    return __q2b(raw.encode('utf-8'))


def cut_for_index(raw):
    """
    """
    raw = __prepare(raw)
    if raw == "":
        return []
    result = jieba.tokenize(raw, mode="search")
    r = []
    for w in result:
        r.append({"word":w[0], "start_offset":int(w[1]), "end_offset":int(w[2])})
    return __filter(r)



def cut_for_search(raw):
    """
    """
    raw = __prepare(raw)
    if raw == "":
        return []
    result = jieba.tokenize(raw)
    r = []
    for w in result:
        r.append({"word":w[0], "start_offset":int(w[1]), "end_offset":int(w[2])})
    return __filter(r)

def cut_with_pos(raw, stop=True):
    """
    part of speech(词性)
    """
    raw = __prepare(raw)
    if raw == "":
        return []
    result = pseg.cut(raw)
    r = []
    for w in result:
        r.append({"word":w.word, "pos":w.flag})
    if stop:
        return __filter(r)
    else:
        return r


def simple_cut(raw, stop=False):
    """
    cut_all mode
    """
    raw = __prepare(raw)
    if raw == "":
        return []
    result = jieba.cut(raw, cut_all=True)
    r = []
    for w in result:
        r.append({"word":w})
    if stop:
        return __filter(r)
    else:
        return r