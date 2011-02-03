package no.jforce.mule.spring.embedder.example.mule;

import org.mule.management.stats.AllStatistics;
import org.mule.management.stats.FlowConstructStatistics;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class MuleStatistics
{
    private final transient AllStatistics statistics;

    public MuleStatistics(final AllStatistics statistics)
    {
        this.statistics = statistics;
    }

    public Map<String, FlowConstructStatistics> getServiceStatistics()
    {
        Map<String, FlowConstructStatistics> map = new HashMap<String, FlowConstructStatistics>();
        try
        {
            for (Iterator<FlowConstructStatistics> it = statistics.getServiceStatistics().iterator(); it.hasNext();)
            {
                FlowConstructStatistics flowConstructStatistics = it.next();
                map.put(flowConstructStatistics.getName(), flowConstructStatistics);
            }

            return map;

        } catch (Exception e)
        {
            throw new RuntimeException("Unable to retrieve service statictics using reflection.", e);
        }

    }

}
