/**
 * 
 */
package org.elasticsearch.index.analysis.py;

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;

import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.env.Environment;
import org.python.core.PyFunction;
import org.python.core.PyList;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;


/**
 * @author matrix
 *
 */
public class PyJiebaSegmenter {
    private static String SOUGOU_DICT = "jieba/sougou.dict";
    private static String USER_DICT = "jieba/user.dict";
    private static String STOP_WORDS_DICT = "jieba/chinese_stopwords.dict";
    private static String JIEBA_PLUGIN = "jieba";
    private static PyJiebaSegmenter instance;
    private static PythonInterpreter interpreter = new PythonInterpreter();
    private static PyFunction load_user_dict;
    private static PyFunction load_stop_words;
    private static PyFunction index;
    private static PyFunction search;
    
    private static boolean isLoaded = false;
    
    static {
        instance = new PyJiebaSegmenter();
    }
    
    private PyJiebaSegmenter() {
    }
    
    public static PyJiebaSegmenter getInstance(File pluginFile, File configFile) {
        if (!isLoaded) {
            PySystemState sys = interpreter.getSystemState();
            sys.setdefaultencoding("utf-8");
            sys.path.add(new File(pluginFile, JIEBA_PLUGIN).getAbsolutePath());
            interpreter.execfile(PyJiebaSegmenter.class.getResourceAsStream("/wrapper_jieba.py"));	
            PyFunction init = (PyFunction)interpreter.get("initialize",PyFunction.class);  
            init.__call__();  
            load_user_dict = (PyFunction)interpreter.get("load_user_dict", PyFunction.class);
            load_stop_words = (PyFunction)interpreter.get("load_stop_words", PyFunction.class);
            index = (PyFunction)interpreter.get("cut_for_index", PyFunction.class);
            search = (PyFunction)interpreter.get("cut_for_search", PyFunction.class);            
            File sougou_dict = new File(configFile, SOUGOU_DICT);
            File user_dict = new File(configFile, USER_DICT);
            File stop_words_dict = new File(configFile, STOP_WORDS_DICT);
            load_user_dict.__call__(new PyString(sougou_dict.getAbsolutePath()));
            load_user_dict.__call__(new PyString(user_dict.getAbsolutePath()));
            load_stop_words.__call__(new PyString(stop_words_dict.getAbsolutePath()));
            isLoaded = true;
        }
        return instance;
    }
    
    private String readerToString(Reader in) {
		CharArrayWriter caw = new CharArrayWriter();
		try {
    		int len = 4096;
    		char[] buff = new char[len];
    		while ((len = in.read(buff)) != -1) {
    			caw.write(buff, 0, len);
    		}
    		return caw.toString();
		}
		catch (IOException e) {
		    return "";
		}
		finally {
		    caw.close();
		}
    }
    
    public PyList cut_for_index(String sentence) {
        return (PyList) index.__call__(new PyString(sentence));
    }

    public PyList cut_for_search(String sentence) {
        return (PyList) search.__call__(new PyString(sentence));
    }
    public PyList cut_for_index(Reader in) {
        String sentence = readerToString(in);
        return (PyList) index.__call__(new PyString(sentence));
    }

    public PyList cut_for_search(Reader in) {
        String sentence = readerToString(in);
        return (PyList) search.__call__(new PyString(sentence));
    }

}
