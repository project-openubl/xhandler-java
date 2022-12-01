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
import io.github.project.openubl.xsender.camel.routes.CxfRouteBuilder;
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
                .addBeanClasses(XSender.class, CxfRouteBuilder.class)
                .setDefaultScope(DotNames.APPLICATION_SCOPED)
                .build();
    }

    @BuildStep
    void registerTemplates(BuildProducer<NativeImageResourceBuildItem> resource) throws URISyntaxException {
        resource.produce(
                new NativeImageResourceBuildItem(
                        "wsdl/billService.wsdl"
                )
        );
    }

    @BuildStep
    ReflectiveClassBuildItem reflection() {
        return new ReflectiveClassBuildItem(
                true,
                true,
                org.apache.cxf.binding.soap.SoapFault.class,

                io.github.project.openubl.xsender.camel.routes.CxfEndpointConfiguration.class,
                io.github.project.openubl.xsender.camel.routes.CxfRouteBuilder.class,
                io.github.project.openubl.xsender.camel.routes.SunatErrorResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.SunatResponseProcessor.class,
                io.github.project.openubl.xsender.camel.routes.TicketResponseType.class,
                io.github.project.openubl.xsender.camel.utils.CamelData.class,
                io.github.project.openubl.xsender.camel.utils.CamelData.CamelDataBuilder.class,
                io.github.project.openubl.xsender.camel.utils.CamelUtils.class,
                io.github.project.openubl.xsender.company.CompanyCredentials.class,
                io.github.project.openubl.xsender.company.CompanyCredentials.CompanyCredentialsBuilder.class,
                io.github.project.openubl.xsender.company.CompanyURLs.class,
                io.github.project.openubl.xsender.company.CompanyURLs.CompanyURLsBuilder.class,
                io.github.project.openubl.xsender.files.FileAnalyzer.class,
                io.github.project.openubl.xsender.files.FileDestination.class,
                io.github.project.openubl.xsender.files.FileDestination.FileDestinationBuilder.class,
                io.github.project.openubl.xsender.files.TicketDestination.class,
                io.github.project.openubl.xsender.files.TicketDestination.TicketDestinationBuilder.class,
                io.github.project.openubl.xsender.files.XMLFileAnalyzer.class,
                io.github.project.openubl.xsender.files.ZipFile.class,
                io.github.project.openubl.xsender.files.ZipFile.ZipFileBuilder.class,
                io.github.project.openubl.xsender.files.exceptions.UnsupportedXMLFileException.class,
                io.github.project.openubl.xsender.files.xml.DocumentType.class,
                io.github.project.openubl.xsender.files.xml.XmlContentModel.class,
                io.github.project.openubl.xsender.files.xml.XmlContentProvider.class,
                io.github.project.openubl.xsender.files.xml.XmlHandler.class,
                io.github.project.openubl.xsender.models.Metadata.class,
                io.github.project.openubl.xsender.models.Metadata.MetadataBuilder.class,
                io.github.project.openubl.xsender.models.Status.class,
                io.github.project.openubl.xsender.models.Sunat.class,
                io.github.project.openubl.xsender.models.Sunat.SunatBuilder.class,
                io.github.project.openubl.xsender.models.SunatResponse.class,
                io.github.project.openubl.xsender.models.SunatResponse.SunatResponseBuilder.class,
                io.github.project.openubl.xsender.sunat.catalog.Catalog1.class,
                io.github.project.openubl.xsender.utils.ByteUtils.class,
                io.github.project.openubl.xsender.utils.CdrReader.class
        );
    }
}
