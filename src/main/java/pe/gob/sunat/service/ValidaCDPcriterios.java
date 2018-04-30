
package pe.gob.sunat.service;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for validaCDPcriterios complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="validaCDPcriterios"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="rucEmisor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoCDP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="serieCDP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroCDP" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoDocIdReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="numeroDocIdReceptor" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="fechaEmision" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="importeTotal" type="{http://www.w3.org/2001/XMLSchema}double" minOccurs="0"/&gt;
 *         &lt;element name="nroAutorizacion" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "validaCDPcriterios", propOrder = {
    "rucEmisor",
    "tipoCDP",
    "serieCDP",
    "numeroCDP",
    "tipoDocIdReceptor",
    "numeroDocIdReceptor",
    "fechaEmision",
    "importeTotal",
    "nroAutorizacion"
})
public class ValidaCDPcriterios {

    protected String rucEmisor;
    protected String tipoCDP;
    protected String serieCDP;
    protected String numeroCDP;
    protected String tipoDocIdReceptor;
    protected String numeroDocIdReceptor;
    protected String fechaEmision;
    protected Double importeTotal;
    protected String nroAutorizacion;

    /**
     * Gets the value of the rucEmisor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRucEmisor() {
        return rucEmisor;
    }

    /**
     * Sets the value of the rucEmisor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRucEmisor(String value) {
        this.rucEmisor = value;
    }

    /**
     * Gets the value of the tipoCDP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoCDP() {
        return tipoCDP;
    }

    /**
     * Sets the value of the tipoCDP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoCDP(String value) {
        this.tipoCDP = value;
    }

    /**
     * Gets the value of the serieCDP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSerieCDP() {
        return serieCDP;
    }

    /**
     * Sets the value of the serieCDP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSerieCDP(String value) {
        this.serieCDP = value;
    }

    /**
     * Gets the value of the numeroCDP property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroCDP() {
        return numeroCDP;
    }

    /**
     * Sets the value of the numeroCDP property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroCDP(String value) {
        this.numeroCDP = value;
    }

    /**
     * Gets the value of the tipoDocIdReceptor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoDocIdReceptor() {
        return tipoDocIdReceptor;
    }

    /**
     * Sets the value of the tipoDocIdReceptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoDocIdReceptor(String value) {
        this.tipoDocIdReceptor = value;
    }

    /**
     * Gets the value of the numeroDocIdReceptor property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeroDocIdReceptor() {
        return numeroDocIdReceptor;
    }

    /**
     * Sets the value of the numeroDocIdReceptor property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeroDocIdReceptor(String value) {
        this.numeroDocIdReceptor = value;
    }

    /**
     * Gets the value of the fechaEmision property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getFechaEmision() {
        return fechaEmision;
    }

    /**
     * Sets the value of the fechaEmision property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setFechaEmision(String value) {
        this.fechaEmision = value;
    }

    /**
     * Gets the value of the importeTotal property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getImporteTotal() {
        return importeTotal;
    }

    /**
     * Sets the value of the importeTotal property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setImporteTotal(Double value) {
        this.importeTotal = value;
    }

    /**
     * Gets the value of the nroAutorizacion property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNroAutorizacion() {
        return nroAutorizacion;
    }

    /**
     * Sets the value of the nroAutorizacion property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNroAutorizacion(String value) {
        this.nroAutorizacion = value;
    }

}
