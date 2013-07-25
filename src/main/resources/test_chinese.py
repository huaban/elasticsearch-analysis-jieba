#! /usr/bin/env python   
# -*- coding: utf-8 -*-  

print '中文测试程序'

def p(str):
    print '是否unicode:%s' % isinstance(str, unicode)
    chinese = "我是中文"
    print '%s 是否unicode:%s' % (chinese, isinstance(chinese, unicode))
    print chinese.decode("utf-8")
    print str.encode('utf-8')
    return str
