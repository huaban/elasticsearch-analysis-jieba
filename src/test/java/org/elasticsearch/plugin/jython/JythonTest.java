/**
 * 
 */
package org.elasticsearch.plugin.jython;

import static org.junit.Assert.*;

import org.apache.lucene.util.UnicodeUtil;
import org.junit.Test;
import org.python.core.PyArray;
import org.python.core.PyDictionary;
import org.python.core.PyFunction;
import org.python.core.PyInteger;
import org.python.core.PyJavaType;
import org.python.core.PyList;
import org.python.core.PyObject;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

/**
 * @author matrix
 * 
 */
public class JythonTest {

    @Test
    public void test_simple() {
        PythonInterpreter interpreter = new PythonInterpreter();
        interpreter.exec("days=('mod','Tue','Wed','Thu','Fri','Sat','Sun'); ");
        interpreter.exec("print days[1];");
    }

    @Test
    public void test_file() {
        PythonInterpreter interpreter = new PythonInterpreter();  
        interpreter.execfile(JythonTest.class.getResourceAsStream("/test.py"));
        PyFunction func = (PyFunction)interpreter.get("adder",PyFunction.class);  
        PyObject pyobj = func.__call__(new PyInteger(2), new PyInteger(3));  
        System.out.println("anwser = " + pyobj.toString());  
    }
    
    @Test
    public void test_chinese() {
        PythonInterpreter interpreter = new PythonInterpreter();  
        PySystemState sys = interpreter.getSystemState();
        sys.setdefaultencoding("utf-8");
        interpreter.execfile(JythonTest.class.getResourceAsStream("/test_chinese.py"));
        PyFunction func = (PyFunction)interpreter.get("p",PyFunction.class);  
        PyObject pyobj = func.__call__(new PyString("中华人民共和国"));
        String result = pyobj.asString();
        System.out.println("anwser = " + PyString.decode_UnicodeEscape(result, 0, result.length(), "", true));  
    }
    
    @Test
    public void test_segment() {
        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = interpreter.getSystemState();
        sys.setdefaultencoding("utf-8");
        sys.path.add("/home/matrix/Dropbox/es/huaban_segmentation_service/jieba");
        interpreter.execfile(JythonTest.class.getResourceAsStream("/test_jieba.py"));
        PyFunction func = (PyFunction)interpreter.get("segment1",PyFunction.class);  
        PyObject pyobj = func.__call__(new PyString("中华人民共和国"));  
        System.out.println("anwser = " + pyobj.toString());
        long s = System.currentTimeMillis();
        for (int i = 0; i < 10000; ++i)
            pyobj = func.__call__(new PyString("中华人民共和国"));
        System.out.println(String.format("耗时%dms", System.currentTimeMillis() - s));
        String result = pyobj.toString();
        System.out.println("anwser = " + result);  
        PyFunction test = (PyFunction)interpreter.get("test_segment",PyFunction.class);
        test.__call__();
    }
    
    @Test
    public void test_segment_with_wrapper() {
        PythonInterpreter interpreter = new PythonInterpreter();
        PySystemState sys = interpreter.getSystemState();
        sys.setdefaultencoding("utf-8");
        sys.path.add("/home/matrix/Dropbox/es/huaban_segmentation_service/jieba");
        interpreter.execfile(JythonTest.class.getResourceAsStream("/wrapper_jieba.py"));
        PyFunction init = (PyFunction)interpreter.get("initialize",PyFunction.class);  
        init.__call__();  
        PyFunction index = (PyFunction)interpreter.get("cut_for_index", PyFunction.class);
        PyFunction search = (PyFunction)interpreter.get("cut_for_search", PyFunction.class);
        PyFunction simple = (PyFunction)interpreter.get("simple_cut", PyFunction.class);
        System.out.println(((PyList)index.__call__(new PyString("中华人民共和国"))).toString());
        PyList array = (PyList)index.__call__(new PyString("中华人民共和国"));
        for (int i = 0; i < array.size(); ++i) {
            PyDictionary dict = (PyDictionary)array.get(i);
            System.out.println(dict.get(new PyString("word")).asString());
        }
            
        System.out.println(((PyList)search.__call__(new PyString("中华人民共和国"))).toString());
        System.out.println(((PyList)simple.__call__(new PyString("中华人民共和国"))).toString());
    }    	
}
