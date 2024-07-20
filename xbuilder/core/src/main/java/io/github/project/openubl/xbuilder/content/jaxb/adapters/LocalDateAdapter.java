package io.github.project.openubl.xbuilder.content.jaxb.adapters;

import jakarta.xml.bind.annotation.adapters.XmlAdapter;
import java.time.LocalDate;

public class LocalDateAdapter extends XmlAdapter<String, LocalDate> {
    @Override
    public LocalDate unmarshal(String v) throws Exception {
        return LocalDate.parse(v);
    }

    @Override
    public String marshal(LocalDate v) throws Exception {
        return v.toString();
    }
}
