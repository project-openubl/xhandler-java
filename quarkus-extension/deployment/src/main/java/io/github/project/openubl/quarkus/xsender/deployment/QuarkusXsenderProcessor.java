/*
 * Copyright 2019 Project OpenUBL, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License - 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.project.openubl.quarkus.xsender.deployment;

import io.github.project.openubl.quarkus.xsender.XSender;
import io.github.project.openubl.xsender.camel.routes.SoapSunatErrorResponseProcessor;
import io.github.project.openubl.xsender.camel.routes.SoapSunatResponseProcessor;
import io.github.project.openubl.xsender.camel.routes.SunatRouteBuilder;
import io.github.project.openubl.xsender.sunat.BillServiceDestination;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.arc.processor.DotNames;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourceBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;

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
        resource.produce(
                new NativeImageResourceBuildItem(
                        "wsdl/billService.wsdl",
                        "wsdl/billConsultService.wsdl",
                        "wsdl/billValidService.wsdl"
                )
        );
    }

    @BuildStep
    ReflectiveClassBuildItem projectReflection() {
        return new ReflectiveClassBuildItem(
                true,
                false,
                io.github.project.openubl.xsender.Constants.class,
                io.github.project.openubl.xsender.camel.routes.CxfEndpointConfiguration.class,
                io.github.project.openubl.xsender.camel.routes.RestSunatErrorResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.RestSunatResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.SunatRouteBuilder.class,
                io.github.project.openubl.xsender.camel.routes.SoapSunatErrorResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.SoapSunatResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.TicketResponseType.class,

                io.github.project.openubl.xsender.camel.utils.CamelData.class,
                io.github.project.openubl.xsender.camel.utils.CamelData.CamelDataBuilder.class,
                io.github.project.openubl.xsender.camel.utils.CamelUtils.class,

                io.github.project.openubl.xsender.company.CompanyCredentials.class,
                io.github.project.openubl.xsender.company.CompanyCredentials.CompanyCredentialsBuilder.class,
                io.github.project.openubl.xsender.company.CompanyURLs.class,
                io.github.project.openubl.xsender.company.CompanyURLs.CompanyURLsBuilder.class,

                io.github.project.openubl.xsender.files.BillServiceFileAnalyzer.class,
                io.github.project.openubl.xsender.files.BillServiceXMLFileAnalyzer.class,
                io.github.project.openubl.xsender.files.ZipFile.class,
                io.github.project.openubl.xsender.files.ZipFile.ZipFileBuilder.class,
                io.github.project.openubl.xsender.files.exceptions.UnsupportedXMLFileException.class,
                io.github.project.openubl.xsender.files.xml.DocumentType.class,
                io.github.project.openubl.xsender.files.xml.XmlContent.class,
                io.github.project.openubl.xsender.files.xml.XmlContentProvider.class,
                io.github.project.openubl.xsender.files.xml.XmlHandler.class,

                io.github.project.openubl.xsender.models.rest.PayloadDocumentDto.class,
                io.github.project.openubl.xsender.models.rest.PayloadDocumentDto.PayloadDocumentDtoBuilder.class,
                io.github.project.openubl.xsender.models.rest.PayloadDocumentDto.Archivo.class,
                io.github.project.openubl.xsender.models.rest.PayloadDocumentDto.Archivo.ArchivoBuilder.class,
                io.github.project.openubl.xsender.models.rest.ResponseAccessTokenSuccessDto.class,
                io.github.project.openubl.xsender.models.rest.ResponseAccessTokenSuccessDto.ResponseAccessTokenSuccessDtoBuilder.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto.ResponseDocumentErrorDtoBuilder.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto.Error.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentErrorDto.Error.ErrorBuilder.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto.ResponseDocumentSuccessDtoBuilder.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto.Error.class,
                io.github.project.openubl.xsender.models.rest.ResponseDocumentSuccessDto.Error.ErrorBuilder.class,
                io.github.project.openubl.xsender.models.Metadata.class,
                io.github.project.openubl.xsender.models.Metadata.MetadataBuilder.class,
                io.github.project.openubl.xsender.models.Status.class,
                io.github.project.openubl.xsender.models.Sunat.class,
                io.github.project.openubl.xsender.models.Sunat.SunatBuilder.class,
                io.github.project.openubl.xsender.models.SunatResponse.class,
                io.github.project.openubl.xsender.models.SunatResponse.SunatResponseBuilder.class,

                io.github.project.openubl.xsender.sunat.BillServiceDestination.class,
                io.github.project.openubl.xsender.sunat.BillServiceDestination.BillServiceDestinationBuilder.class,
                io.github.project.openubl.xsender.sunat.BillServiceDestination.SoapOperation.class,
                io.github.project.openubl.xsender.sunat.BillServiceDestination.RestOperation.class,
                io.github.project.openubl.xsender.sunat.BillConsultServiceDestination.class,
                io.github.project.openubl.xsender.sunat.BillConsultServiceDestination.BillConsultServiceDestinationBuilder.class,
                io.github.project.openubl.xsender.sunat.BillConsultServiceDestination.Operation.class,
                io.github.project.openubl.xsender.sunat.catalog.Catalog1.class,
                io.github.project.openubl.xsender.utils.ByteUtils.class,
                io.github.project.openubl.xsender.utils.CdrReader.class
        );
    }

    @BuildStep
    ReflectiveClassBuildItem soapReflection() {
        return new ReflectiveClassBuildItem(
                true,
                false,
                org.apache.cxf.binding.soap.SoapFault.class
        );
    }

    @BuildStep
    ReflectiveClassBuildItem restReflection() {
        return new ReflectiveClassBuildItem(
                true,
                false,
                org.apache.camel.http.base.HttpOperationFailedException.class
        );
    }

    @BuildStep
    ReflectiveClassBuildItem sunatReflection() {
        return new ReflectiveClassBuildItem(
                true,
                true,
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
                service.sunat.gob.pe.billvalidservice.ObjectFactory.class
        );
    }
}
