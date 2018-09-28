package io.github.carlosthe19916.webservices.managers;

import io.github.carlosthe19916.webservices.providers.BillServiceCallback;
import io.github.carlosthe19916.webservices.providers.BillServiceModel;

import javax.xml.ws.soap.SOAPFaultException;

public class MockBillServiceCallback implements BillServiceCallback {

    private final StatusWrapper statusWrapper;

    public MockBillServiceCallback() {
        this.statusWrapper = new StatusWrapper();
    }

    @Override
    public void onSuccess(int code, String description, byte[] cdr) {
        statusWrapper.setStatus(BillServiceModel.Status.ACEPTADO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
        statusWrapper.setCdr(cdr);
    }

    @Override
    public void onError(int code, String description, byte[] cdr) {
        statusWrapper.setStatus(BillServiceModel.Status.RECHAZADO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
        statusWrapper.setCdr(cdr);
    }

    @Override
    public void onProcess(int code, String description) {
        statusWrapper.setStatus(BillServiceModel.Status.EN_PROCESO);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
    }

    @Override
    public void onException(int code, String description) {
        statusWrapper.setStatus(BillServiceModel.Status.EXCEPCION);
        statusWrapper.setCode(code);
        statusWrapper.setDescription(description);
    }

    @Override
    public void onThrownException(SOAPFaultException exception) {

    }

    public StatusWrapper getStatusWrapper() {
        return statusWrapper;
    }

    public static class StatusWrapper {
        private BillServiceModel.Status status;
        private Integer code;
        private String description;
        private byte[] cdr;

        public BillServiceModel.Status getStatus() {
            return status;
        }

        public void setStatus(BillServiceModel.Status status) {
            this.status = status;
        }

        public Integer getCode() {
            return code;
        }

        public void setCode(Integer code) {
            this.code = code;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public byte[] getCdr() {
            return cdr;
        }

        public void setCdr(byte[] cdr) {
            this.cdr = cdr;
        }
    }
}
