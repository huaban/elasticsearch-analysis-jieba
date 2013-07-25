#! /usr/bin/env python   
# -*- coding: utf-8 -*-   
import jieba
import time
print '中文'
def segment(str):
    print type(str)
    print isinstance(str, unicode)
    print str.encode('utf-8')
    print 'test' + '/'.join(jieba.cut('我是中国人'))  
    print 'test' + '/'.join(jieba.cut('中华人民共和国', cut_all=True))  
    r = '/'.join(jieba.cut(str.encode('utf-8'), cut_all=True))
    return r
def segment1(str):
    return '/'.join(jieba.cut(str.encode('utf-8'), cut_all=True))

def test_segment():
    s = time.time()
    for i in range(200000):
        '/'.join(jieba.cut('中华人民共和国', cut_all=False))
    print 'finish!\n'
    print 'tot time:%.2fs\n' % (time.time() - s)
    
