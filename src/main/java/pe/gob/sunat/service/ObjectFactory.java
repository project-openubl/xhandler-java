
package pe.gob.sunat.service;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the pe.gob.sunat.service package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _GetStatus_QNAME = new QName("http://service.sunat.gob.pe", "getStatus");
    private final static QName _GetStatusResponse_QNAME = new QName("http://service.sunat.gob.pe", "getStatusResponse");
    private final static QName _SendBill_QNAME = new QName("http://service.sunat.gob.pe", "sendBill");
    private final static QName _SendBillResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendBillResponse");
    private final static QName _SendPack_QNAME = new QName("http://service.sunat.gob.pe", "sendPack");
    private final static QName _SendPackResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendPackResponse");
    private final static QName _SendSummary_QNAME = new QName("http://service.sunat.gob.pe", "sendSummary");
    private final static QName _SendSummaryResponse_QNAME = new QName("http://service.sunat.gob.pe", "sendSummaryResponse");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: pe.gob.sunat.service
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link GetStatus }
     * 
     */
    public GetStatus createGetStatus() {
        return new GetStatus();
    }

    /**
     * Create an instance of {@link GetStatusResponse }
     * 
     */
    public GetStatusResponse createGetStatusResponse() {
        return new GetStatusResponse();
    }

    /**
     * Create an instance of {@link SendBill }
     * 
     */
    public SendBill createSendBill() {
        return new SendBill();
    }

    /**
     * Create an instance of {@link SendBillResponse }
     * 
     */
    public SendBillResponse createSendBillResponse() {
        return new SendBillResponse();
    }

    /**
     * Create an instance of {@link SendPack }
     * 
     */
    public SendPack createSendPack() {
        return new SendPack();
    }

    /**
     * Create an instance of {@link SendPackResponse }
     * 
     */
    public SendPackResponse createSendPackResponse() {
        return new SendPackResponse();
    }

    /**
     * Create an instance of {@link SendSummary }
     * 
     */
    public SendSummary createSendSummary() {
        return new SendSummary();
    }

    /**
     * Create an instance of {@link SendSummaryResponse }
     * 
     */
    public SendSummaryResponse createSendSummaryResponse() {
        return new SendSummaryResponse();
    }

    /**
     * Create an instance of {@link StatusResponse }
     * 
     */
    public StatusResponse createStatusResponse() {
        return new StatusResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatus }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatus")
    public JAXBElement<GetStatus> createGetStatus(GetStatus value) {
        return new JAXBElement<GetStatus>(_GetStatus_QNAME, GetStatus.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GetStatusResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "getStatusResponse")
    public JAXBElement<GetStatusResponse> createGetStatusResponse(GetStatusResponse value) {
        return new JAXBElement<GetStatusResponse>(_GetStatusResponse_QNAME, GetStatusResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendBill }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendBill")
    public JAXBElement<SendBill> createSendBill(SendBill value) {
        return new JAXBElement<SendBill>(_SendBill_QNAME, SendBill.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendBillResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendBillResponse")
    public JAXBElement<SendBillResponse> createSendBillResponse(SendBillResponse value) {
        return new JAXBElement<SendBillResponse>(_SendBillResponse_QNAME, SendBillResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPack }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendPack")
    public JAXBElement<SendPack> createSendPack(SendPack value) {
        return new JAXBElement<SendPack>(_SendPack_QNAME, SendPack.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendPackResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendPackResponse")
    public JAXBElement<SendPackResponse> createSendPackResponse(SendPackResponse value) {
        return new JAXBElement<SendPackResponse>(_SendPackResponse_QNAME, SendPackResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSummary }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendSummary")
    public JAXBElement<SendSummary> createSendSummary(SendSummary value) {
        return new JAXBElement<SendSummary>(_SendSummary_QNAME, SendSummary.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link SendSummaryResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.sunat.gob.pe", name = "sendSummaryResponse")
    public JAXBElement<SendSummaryResponse> createSendSummaryResponse(SendSummaryResponse value) {
        return new JAXBElement<SendSummaryResponse>(_SendSummaryResponse_QNAME, SendSummaryResponse.class, null, value);
    }

}
