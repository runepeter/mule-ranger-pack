package org.brylex.mule.jetty.example;

import com.espertech.esper.event.bean.BeanEventBean;
import com.espertech.esper.event.bean.BeanEventType;
import org.mule.api.MuleMessage;
import org.mule.api.transformer.DataType;
import org.mule.api.transformer.TransformerException;
import org.mule.transformer.AbstractMessageTransformer;
import org.mule.transformer.types.SimpleDataType;

import java.util.Map;

public class EsperEventToWebSocketTransformer extends AbstractMessageTransformer {

    private final SoccerResultsCache cache;

    public EsperEventToWebSocketTransformer(final SoccerResultsCache cache) {
        this.cache = cache;
        setReturnDataType(new SimpleDataType<WebSocketData>(WebSocketData.class));
    }

    @Override
    public Object transformMessage(MuleMessage message, String outputEncoding) throws TransformerException {

        Object payload = ((BeanEventBean) message.getPayload(Map.class).get("r")).getUnderlying();

        cache.apply((SoccerResult) payload);

        return new WebSocketData(payload);
    }
}
