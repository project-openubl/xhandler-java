package io.github.project.openubl.xsender.sunat;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.HashMap;
import java.util.Map;

/**
 * Converts {@link BillServiceDestination}s to and from the targets of the
 * xhandler core: {@code {protocol: soap|rest, url, action?}}.
 */
public final class Destinations {

    private Destinations() {
    }

    static Map<String, Object> target(BillServiceDestination destination) {
        Map<String, Object> target = new HashMap<>();
        target.put("url", destination.getUrl());
        if (destination.getRestOperation() != null) {
            target.put("protocol", "rest");
            return target;
        }
        target.put("protocol", "soap");
        switch (destination.getSoapOperation()) {
            case SEND_BILL -> target.put("action", "bill");
            case SEND_SUMMARY -> target.put("action", "summary");
            case SEND_PACK -> target.put("action", "pack");
            case GET_STATUS -> {
                // verifying a ticket: no file action
            }
        }
        return target;
    }

    /**
     * @param target a target of the core for sending a file
     */
    public static BillServiceDestination sendFile(JsonNode target) {
        BillServiceDestination.BillServiceDestinationBuilder destination = BillServiceDestination.builder()
                .url(target.get("url").asText());
        if (target.get("protocol").asText().equals("rest")) {
            return destination.restOperation(BillServiceDestination.RestOperation.SEND_DOCUMENT).build();
        }
        return destination.soapOperation(switch (target.get("action").asText()) {
            case "summary" -> BillServiceDestination.SoapOperation.SEND_SUMMARY;
            case "pack" -> BillServiceDestination.SoapOperation.SEND_PACK;
            default -> BillServiceDestination.SoapOperation.SEND_BILL;
        }).build();
    }

    /**
     * @param target a target of the core for verifying a ticket
     */
    public static BillServiceDestination verifyTicket(JsonNode target) {
        BillServiceDestination.BillServiceDestinationBuilder destination = BillServiceDestination.builder()
                .url(target.get("url").asText());
        return target.get("protocol").asText().equals("rest")
                ? destination.restOperation(BillServiceDestination.RestOperation.VERIFY_TICKET).build()
                : destination.soapOperation(BillServiceDestination.SoapOperation.GET_STATUS).build();
    }
}
