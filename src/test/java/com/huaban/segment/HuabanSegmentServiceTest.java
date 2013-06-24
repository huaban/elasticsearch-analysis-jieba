package com.huaban.segment;

import static org.junit.Assert.*;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.junit.Test;

import com.huaban.segment.HuabanSegmentService.Client;

public class HuabanSegmentServiceTest {

    @Test
    public void test() {
    try {
      TTransport trans = new TSocket("183.136.223.174", 8000);
      trans.open();
      
      TProtocol protocol = new TBinaryProtocol(trans);
      HuabanSegmentService.Client client = new HuabanSegmentService.Client(protocol);
      client.echoVoid();
      System.out.println(client.segment("search", "中华人民共和国"));
      System.out.println(client.segment("all", "中华人民共和国"));
      System.out.println(client.segment("pos", "中华人民共和国"));
      trans.close();
    }
    catch(TException e) {
      e.printStackTrace();
    }
    }

}
