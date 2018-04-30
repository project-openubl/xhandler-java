
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for sendBillResponse complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="sendBillResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="applicationResponse" type="{http://www.w3.org/2001/XMLSchema}base64Binary" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "sendBillResponse", propOrder = {
    "applicationResponse"
})
public class SendBillResponse {

    protected byte[] applicationResponse;

    /**
     * Gets the value of the applicationResponse property.
     * 
     * @return
     *     possible object is
     *     byte[]
     */
    public byte[] getApplicationResponse() {
        return applicationResponse;
    }

    /**
     * Sets the value of the applicationResponse property.
     * 
     * @param value
     *     allowed object is
     *     byte[]
     */
    public void setApplicationResponse(byte[] value) {
        this.applicationResponse = value;
    }

}
