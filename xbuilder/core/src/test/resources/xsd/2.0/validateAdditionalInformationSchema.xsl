<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:ext="urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2" 
	xmlns:sac="urn:sunat:names:specification:ubl:peru:schema:xsd:SunatAggregateComponents-1" 
	xmlns:regexp="http://exslt.org/regular-expressions" 
	xmlns:dp="http://www.datapower.com/extensions" version="1." extension-element-prefixes="dp">
	<xsl:include href="local:///commons/error/error_utils.xsl" dp:ignore-multiple="yes"/>
	<xsl:output omit-xml-declaration="yes"/>
	<xsl:template match="/">
	
        <xsl:variable name="schemaExtensions" >
            
            <xsl:choose>
                <xsl:when test="dp:variable('var://context/cpe/url/schemaExtensions') and normalize-space(dp:variable('var://context/cpe/url/schemaExtensions')) != ''">
                
                    <xsl:value-of select="normalize-space(dp:variable('var://context/cpe/url/schemaExtensions'))"/>
                
                </xsl:when>
                
                <xsl:otherwise>
                    <xsl:value-of select="'local://commons/schemas/UBL/2.0/common/UBLPE-SunatAggregateComponents-1.0.xsd'"/>
                </xsl:otherwise>
            
            </xsl:choose>
        </xsl:variable>
        
        
        <xsl:variable name="sacAdditionalInformation" select="/*/ext:UBLExtensions/ext:UBLExtension/ext:ExtensionContent/sac:AdditionalInformation"/>	
  
		<xsl:if test="count($sacAdditionalInformation/*) &gt; 0">
			<xsl:variable name="result">
				<xsl:copy-of select="dp:schema-validate($schemaExtensions,$sacAdditionalInformation)"/>
			</xsl:variable>
			<xsl:variable name="msg_error" select="regexp:replace(string(dp:variable('var://context/ctx/_extension/error')), '.*\b\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3}\b\:\d{1,}/.*billService', 'i', '')"/>
			<xsl:if test="count($result/*) &lt;= 0">
				<xsl:call-template name="rejectCall">
					<xsl:with-param name="errorCode" select="'0306'"/>
					<xsl:with-param name="errorMessage" select="concat('Error AdditionalInformation: (', $msg_error, ')')"/>
				</xsl:call-template>
			</xsl:if>
		</xsl:if>
	</xsl:template>
</xsl:stylesheet>
