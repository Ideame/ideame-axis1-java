/*
 * Copyright 2004,2005 The Apache Software Foundation.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
 
package org.apache.axis.engine;

//todo

import javax.xml.namespace.QName;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import junit.framework.TestCase;

import org.apache.axis.addressing.AddressingConstants;
import org.apache.axis.addressing.EndpointReference;
import org.apache.axis.clientapi.AsyncResult;
import org.apache.axis.clientapi.Callback;
import org.apache.axis.context.MessageContext;
import org.apache.axis.description.AxisOperation;
import org.apache.axis.description.AxisService;
import org.apache.axis.encoding.EncodingTest.Echo;
import org.apache.axis.integration.UtilServer;
import org.apache.axis.om.OMElement;
import org.apache.axis.om.OMFactory;
import org.apache.axis.om.OMNamespace;
import org.apache.axis.om.OMNode;
import org.apache.axis.om.SOAPEnvelope;
import org.apache.axis.providers.RawXMLProvider;
import org.apache.axis.transport.http.SimpleHTTPServer;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class EchoRawXMLTest extends TestCase {
    private Log log = LogFactory.getLog(getClass());
    private QName serviceName = new QName("", "EchoXMLService");
    private QName operationName = new QName("http://localhost/my", "echoOMElement");
    private QName transportName = new QName("http://localhost/my", "NullTransport");

    private EngineRegistry engineRegistry;
    private MessageContext mc;
    private Thread thisThread;
    private SimpleHTTPServer sas;

    private boolean finish = false;

    public EchoRawXMLTest() {
        super(EchoRawXMLTest.class.getName());
    }

    public EchoRawXMLTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        UtilServer.start();
        AxisService service = new AxisService(serviceName);
        service.setClassLoader(Thread.currentThread().getContextClassLoader());
        service.setServiceClass(Echo.class);
        service.setProvider(new RawXMLProvider());
        AxisOperation operation = new AxisOperation(operationName);

        service.addOperation(operation);
        UtilServer.deployService(service);
    }


    protected void tearDown() throws Exception {
        UtilServer.unDeployService(serviceName);
        UtilServer.stop();
    }


    public void testEchoXMLSync() throws Exception {
        OMFactory fac = OMFactory.newInstance();

        SOAPEnvelope reqEnv = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace("http://localhost/my", "my");
        OMElement method = fac.createOMElement("echoOMElement", omNs);
        OMElement value = fac.createOMElement("myValue", omNs);
        value.setValue("Isaac Assimov, the foundation Sega");
        method.addChild(value);
        reqEnv.getBody().addChild(method);

        org.apache.axis.clientapi.Call call = new org.apache.axis.clientapi.Call();
        EndpointReference targetEPR = new EndpointReference(AddressingConstants.WSA_TO, "http://127.0.0.1:" + UtilServer.TESTING_PORT + "/axis/services/EchoXMLService");
        call.setTo(targetEPR);
        SOAPEnvelope resEnv = call.sendReceive(reqEnv);

        resEnv.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(System.out), true);
        OMNode omNode = resEnv.getBody().getFirstChild();
        assertNotNull(omNode);
    }

    public void testEchoXMLASync() throws Exception {
        OMFactory fac = OMFactory.newInstance();

        SOAPEnvelope reqEnv = fac.getDefaultEnvelope();
        OMNamespace omNs = fac.createOMNamespace("http://localhost/my", "my");
        OMElement method = fac.createOMElement("echoOMElement", omNs);
        OMElement value = fac.createOMElement("myValue", omNs);
        value.setValue("Isaac Assimov, the foundation Sega");
        method.addChild(value);
        reqEnv.getBody().addChild(method);

        org.apache.axis.clientapi.Call call = new org.apache.axis.clientapi.Call();
        EndpointReference targetEPR = new EndpointReference(AddressingConstants.WSA_TO, "http://127.0.0.1:" + UtilServer.TESTING_PORT + "/axis/services/EchoXMLService");
        call.setTo(targetEPR);
        call.setListenerTransport("http", true);

        Callback callback = new Callback() {
            public void onComplete(AsyncResult result) {
                try {
                    result.getResponseEnvelope().serialize(XMLOutputFactory.newInstance()
                            .createXMLStreamWriter(System.out), true);
                } catch (XMLStreamException e) {
                    reportError(e);
                } finally {
                    finish = true;
                }
            }

            public void reportError(Exception e) {
                e.printStackTrace();
                finish = true;
            }
        };

        call.sendReceiveAsync(reqEnv, callback);
        while (!finish) {
            Thread.sleep(1000);
        }

        log.info("send the reqest");
    }

}