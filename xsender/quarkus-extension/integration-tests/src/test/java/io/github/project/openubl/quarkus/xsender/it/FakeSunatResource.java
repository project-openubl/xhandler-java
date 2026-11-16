package io.github.project.openubl.quarkus.xsender.it;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import java.util.Map;

/** Points the extension at a {@link FakeSunat} */
public class FakeSunatResource implements QuarkusTestResourceLifecycleManager {

    private FakeSunat sunat;

    @Override
    public Map<String, String> start() {
        sunat = new FakeSunat();
        return Map.of("quarkus.xsender.company.invoice-url", sunat.billServiceUrl());
    }

    @Override
    public void stop() {
        sunat.close();
    }
}
