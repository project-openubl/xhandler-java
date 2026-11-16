package io.github.project.openubl.quarkus.xsender.it;

import io.quarkus.test.common.WithTestResource;
import io.quarkus.test.junit.QuarkusIntegrationTest;

@WithTestResource(FakeSunatResource.class)
@QuarkusIntegrationTest
public class QuarkusSenderResourceIT extends QuarkusXSenderResourceTest {
}
