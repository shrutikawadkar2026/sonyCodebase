package com.sony.core.prefill;


import com.adobe.forms.common.service.*;
import com.day.cq.reporting.Data;
import org.osgi.service.component.annotations.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Component(service = DataXMLProvider.class, immediate = true,
property = {
        "service.name=basicDataform",
        "service.description=basicData"
})


public class BasicPersonalDetailStatic implements DataXMLProvider{


    @Override
    public InputStream getDataXMLForDataRef(DataXMLOptions dataXMLOptions) throws FormsException {
        String xml =
            "<afData>" +
                    "<firstName>Harry</firstName>"+
                    "<lastName>Potter</lastName>" +
                    "<email>harry@gmail.com</email>" +
                    "<mobile>8987786778</mobile>" +
            "</afData>";

        return new ByteArrayInputStream(
                xml.getBytes(StandardCharsets.UTF_8)
        );
    }

    @Override
    public String getServiceName() {
        return "basicDataform";
    }

    @Override
    public String getServiceDescription() {
        return "basicData";
    }
}
