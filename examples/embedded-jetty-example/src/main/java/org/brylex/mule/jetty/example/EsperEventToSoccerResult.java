package org.brylex.mule.jetty.example;

import com.espertech.esper.event.bean.BeanEventBean;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.SimpleDataType;

import java.util.Map;

public class EsperEventToSoccerResult extends AbstractMessageTransformer {

    public EsperEventToSoccerResult() {
        setReturnDataType(new SimpleDataType<SoccerResult>(SoccerResult.class));
    }

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {
        return (SoccerResult) ((BeanEventBean) message.getPayload(Map.class).get("r")).getUnderlying();
    }
}
