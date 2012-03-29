package org.brylex.reschedule.mule;

import org.junit.Test;
import org.junit.runners.Parameterized;
import org.mule.tck.AbstractServiceAndFlowTestCase;

import java.util.Arrays;
import java.util.Collection;

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
    public void testName() throws Exception {
        Thread.sleep(5000);
    }
}
