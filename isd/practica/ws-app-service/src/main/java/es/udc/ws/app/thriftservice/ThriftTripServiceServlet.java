package es.udc.ws.app.thriftservice;

import es.udc.ws.app.thrift.ThriftTripService;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.server.TServlet;

public class ThriftTripServiceServlet extends TServlet {

    public ThriftTripServiceServlet() {
        super(createProcessor(), createProtocolFactory());
    }

    private static TProcessor createProcessor() {
        return new ThriftTripService.Processor<ThriftTripService.Iface>(new ThriftTripServiceImpl());
    }

    private static TProtocolFactory createProtocolFactory() {
        return new TBinaryProtocol.Factory();
    }
}
