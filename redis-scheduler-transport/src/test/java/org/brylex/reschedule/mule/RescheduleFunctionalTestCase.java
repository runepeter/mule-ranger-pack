package org.brylex.reschedule.mule;

import org.jforce.reschedule.Job;
import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mule.api.MuleMessage;
import org.mule.api.client.LocalMuleClient;
import org.mule.api.client.MuleClient;
import org.mule.client.DefaultLocalMuleClient;
import org.mule.tck.AbstractServiceAndFlowTestCase;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class RescheduleFunctionalTestCase extends AbstractServiceAndFlowTestCase {

    public RescheduleFunctionalTestCase(ConfigVariant variant, String configResources) {
        super(variant, configResources);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> parameters() {
        return Arrays.asList(new Object[][]{
                {ConfigVariant.FLOW, "reschedule-functional-test-flow.xml"}
        });
    }

    @Test
    public void testJalla() throws Exception {

        MuleClient muleClient = new DefaultLocalMuleClient(muleContext);
        muleClient.dispatch("vm://test", new Job("jalla"), Collections.<String, Object>emptyMap());
        Thread.sleep(5000);
    }
}
