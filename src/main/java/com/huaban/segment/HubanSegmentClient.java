package com.huaban.segment;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;

import com.huaban.segment.HuabanSegmentService.Client;
import com.huaban.segment.HuabanSegmentService.Iface;
import com.huaban.thrift.ConnectionManager;
import com.huaban.thrift.GenericMethodInvocation;

public class HubanSegmentClient implements Iface {
    private ConnectionManager connManager;
    private GenericMethodInvocation echo;
    private GenericMethodInvocation segment;

    public class InnerClient implements Iface {

        @Override
        public void echoVoid() throws TException {
            TProtocol protocol = new TBinaryProtocol(connManager.getSocket());
            Client client = new Client(protocol);
            client.echoVoid();
        }

        @Override
        public String segment(String type, String text) throws TException {
            TProtocol protocol = new TBinaryProtocol(connManager.getSocket());
            Client client = new Client(protocol);
            return client.segment(type, text);
        }

    }

    public HubanSegmentClient(ConnectionManager connManager) {
        this.connManager = connManager;
        InnerClient client = new HubanSegmentClient.InnerClient();
        try {
            echo = new GenericMethodInvocation(client, client.getClass().getMethod("echoVoid"),
                            null);
            segment = new GenericMethodInvocation(client, client.getClass().getMethod("segment",
                            String.class, String.class), null);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    public void echoVoid() throws TException {
        try {
            connManager.invoke(echo);
        } catch (Throwable e) {
            throw new TException(e);
        }
    }

    @Override
    public String segment(String type, String text) throws TException {
        try {
            segment.setArgs(new Object[] {type, text});
            return (String) connManager.invoke(segment);
        } catch (Throwable e) {
            throw new TException(e);
        }
    }

}
