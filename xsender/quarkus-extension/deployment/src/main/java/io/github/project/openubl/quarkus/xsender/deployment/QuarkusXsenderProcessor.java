package io.github.project.openubl.quarkus.xsender.deployment;

import io.github.project.openubl.quarkus.xsender.XSender;
import io.github.project.openubl.xsender.Constants;
import io.github.project.openubl.xsender.camel.routes.*;
import io.github.project.openubl.xsender.camel.utils.CamelData;
import io.github.project.openubl.xsender.camel.utils.CamelUtils;
import io.github.project.openubl.xsender.company.CompanyCredentials;
import io.github.project.openubl.xsender.company.CompanyURLs;
import io.github.project.openubl.xsender.files.BillServiceFileAnalyzer;
import io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer;
import io.github.project.openubl.xsender.files.ZipFile;
import io.github.project.openubl.xsender.files.exceptions.UnsupportedXMLFileException;
import io.github.project.openubl.xsender.files.xml.DocumentType;
import io.github.project.openubl.xsender.files.xml.XmlContent;
import io.github.project.openubl.xsender.files.xml.XmlContentProvider;
import io.github.project.openubl.xsender.files.xml.XmlHandler;
import io.github.project.openubl.xsender.models.Metadata;
import io.github.project.openubl.xsender.models.Status;
import io.github.project.openubl.xsender.models.Sunat;
import io.github.project.openubl.xsender.models.SunatResponse;
import io.github.project.openubl.xsender.models.rest.PayloadDocumentDto;
import io.github.project.openubl.xsender.models.rest.ResponseAccessTokenSuccessDto;
import io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto;
import io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto;
import io.github.project.openubl.xsender.sunat.BillConsultServiceDestination;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.github.project.openubl.xsender.sunat.catalog.Catalog1;
import io.github.project.openubl.xsender.utils.ByteUtils;
import io.github.project.openubl.xsender.utils.CdrReader;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import org.apache.camel.http.base.HttpOperationFailedException;
import org.apache.cxf.binding.soap.SoapFault;
import org.apache.cxf.transport.http.HTTPException;

import java.net.URISyntaxException;

class QuarkusXsenderProcessor {

    private static final String FEATURE = "quarkus-xsender";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    AdditionalBeanBuildItem additionalBeans() {
        return AdditionalBeanBuildItem
                .builder()
                .setUnremovable()
                .addBeanClasses(XSender.class, SunatRouteBuilder.class)
                .setDefaultScope(DotNames.APPLICATION_SCOPED)
                .build();
    }

    @BuildStep
    void registerTemplates(BuildProducer<NativeImageResourceBuildItem> resource) throws URISyntaxException {
        resource.produce(new NativeImageResourceBuildItem(
                "wsdl/billService.wsdl",
                "wsdl/billConsultService.wsdl",
                "wsdl/billValidService.wsdl"));
    }

    @BuildStep
    ReflectiveClassBuildItem projectReflection() {
        return ReflectiveClassBuildItem.builder(
                Constants.class,
                CxfEndpointConfiguration.class,
                RestSunatErrorResponseProcessor.class,
                RestSunatResponseProcessor.class,
                SunatRouteBuilder.class,
                SoapSunatErrorResponseProcessor.class,
                SoapSunatResponseProcessor.class,
                TicketResponseType.class,
                CamelData.class,
                CamelData.CamelDataBuilder.class,
                CamelUtils.class,
                CompanyCredentials.class,
                CompanyCredentials.CompanyCredentialsBuilder.class,
                CompanyURLs.class,
                CompanyURLs.CompanyURLsBuilder.class,
                BillServiceFileAnalyzer.class,
                BillServiceXMLFileAnalyzer.class,
                ZipFile.class,
                ZipFile.ZipFileBuilder.class,
                UnsupportedXMLFileException.class,
                DocumentType.class,
                XmlContent.class,
                XmlContentProvider.class,
                XmlHandler.class,
                PayloadDocumentDto.class,
                PayloadDocumentDto.PayloadDocumentDtoBuilder.class,
                PayloadDocumentDto.Archivo.class,
                PayloadDocumentDto.Archivo.ArchivoBuilder.class,
                ResponseAccessTokenSuccessDto.class,
                ResponseAccessTokenSuccessDto.ResponseAccessTokenSuccessDtoBuilder.class,
                ResponseDocumentErrorDto.class,
                ResponseDocumentErrorDto.ResponseDocumentErrorDtoBuilder.class,
                ResponseDocumentErrorDto.Error.class,
                ResponseDocumentErrorDto.Error.ErrorBuilder.class,
                ResponseDocumentSuccessDto.class,
                ResponseDocumentSuccessDto.ResponseDocumentSuccessDtoBuilder.class,
                ResponseDocumentSuccessDto.Error.class,
                ResponseDocumentSuccessDto.Error.ErrorBuilder.class,
                Metadata.class,
                Metadata.MetadataBuilder.class,
                Status.class,
                Sunat.class,
                Sunat.SunatBuilder.class,
                SunatResponse.class,
                SunatResponse.SunatResponseBuilder.class,
                BillServiceDestination.class,
                BillServiceDestination.BillServiceDestinationBuilder.class,
                BillServiceDestination.SoapOperation.class,
                BillServiceDestination.RestOperation.class,
                BillConsultServiceDestination.class,
                BillConsultServiceDestination.BillConsultServiceDestinationBuilder.class,
                BillConsultServiceDestination.Operation.class,
                Catalog1.class,
                ByteUtils.class,
                CdrReader.class).methods().build();
    }

    @BuildStep
    ReflectiveClassBuildItem soapReflection() {
        return ReflectiveClassBuildItem.builder(SoapFault.class, HTTPException.class)
                .methods().build();
    }

    @BuildStep
    ReflectiveClassBuildItem restReflection() {
        return ReflectiveClassBuildItem.builder(HttpOperationFailedException.class)
                .methods().build();
    }

    @BuildStep
    ReflectiveClassBuildItem sunatReflection() {
        return ReflectiveClassBuildItem.builder(
                service.sunat.gob.pe.billservice.BillService.class,
                service.sunat.gob.pe.billservice.StatusResponse.class,
                service.sunat.gob.pe.billservice.GetStatus.class,
                service.sunat.gob.pe.billservice.GetStatusResponse.class,
                service.sunat.gob.pe.billservice.SendBill.class,
                service.sunat.gob.pe.billservice.SendBillResponse.class,
                service.sunat.gob.pe.billservice.SendPack.class,
                service.sunat.gob.pe.billservice.SendPackResponse.class,
                service.sunat.gob.pe.billservice.SendSummary.class,
                service.sunat.gob.pe.billservice.SendSummaryResponse.class,
                service.sunat.gob.pe.billservice.ObjectFactory.class,
                service.sunat.gob.pe.billconsultservice.BillService.class,
                service.sunat.gob.pe.billconsultservice.StatusResponse.class,
                service.sunat.gob.pe.billconsultservice.GetStatus.class,
                service.sunat.gob.pe.billconsultservice.GetStatusResponse.class,
                service.sunat.gob.pe.billconsultservice.GetStatusCdr.class,
                service.sunat.gob.pe.billconsultservice.GetStatusCdrResponse.class,
                service.sunat.gob.pe.billconsultservice.ObjectFactory.class,
                service.sunat.gob.pe.billvalidservice.BillValidService.class,
                service.sunat.gob.pe.billvalidservice.StatusResponse.class,
                service.sunat.gob.pe.billvalidservice.ValidaCDPcriterios.class,
                service.sunat.gob.pe.billvalidservice.ValidaCDPcriteriosResponse.class,
                service.sunat.gob.pe.billvalidservice.VerificaCPEarchivo.class,
                service.sunat.gob.pe.billvalidservice.VerificaCPEarchivoResponse.class,
                service.sunat.gob.pe.billvalidservice.ObjectFactory.class).methods().fields().build();
    }
}
