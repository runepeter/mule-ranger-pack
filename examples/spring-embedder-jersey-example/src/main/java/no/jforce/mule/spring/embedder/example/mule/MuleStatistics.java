package no.jforce.mule.spring.embedder.example.mule;

import org.mule.management.stats.AllStatistics;
import org.mule.management.stats.ServiceStatistics;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Map;

public class MuleStatistics
{
    private final transient AllStatistics statistics;

    public MuleStatistics(final AllStatistics statistics)
    {
        this.statistics = statistics;
    }

    public Map<String, ServiceStatistics> getServiceStatistics()
    {
        try
        {
            Field field= statistics.getClass().getDeclaredField("serviceStats");
            field.setAccessible(true);

            return (Map<String, ServiceStatistics>) field.get(statistics);

        } catch (Exception e)
        {
            throw new RuntimeException("Unable to retrieve service statictics using reflection.", e);
        }

    }

}
