package no.jforce.mule.spring.embedder.example.jersey;

import com.sun.jersey.multipart.FormDataParam;
import org.mule.api.MuleContext;
import org.mule.management.stats.AllStatistics;
import org.mule.management.stats.ServiceStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.util.Collections;
import java.util.Date;

@Component
@Path("file")
public class FileResource
{
    @Autowired
    private MuleContext context;

    private long avgExecutionTime = 0;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") File file) throws Exception
    {
        context.getClient().dispatch("file://target/data/in", file, Collections.<String, Object>emptyMap());

        return Response.ok().build();
    }

    @GET
    @Path("statistics/avg-execution-time.json")
    @Produces("application/json")
    public Response getAverageExecutionTime() {

        // TODO rpb: Provide the whole AllStatistics as JSON object.

        AllStatistics statistics = context.getStatistics();

        StringBuffer buf = new StringBuffer("{\"mygauge\":");
        for (ServiceStatistics serviceStatistics : statistics.getServiceStatistics())
        {
            if ("file-service".equals(serviceStatistics.getName())) {
                long time = serviceStatistics.getAverageExecutionTime();
                buf.append(String.format("[{\"startval\": \"%s\", \"endval\": \"%s\"}]", avgExecutionTime, time));
                this.avgExecutionTime = time;
            }
        }
        buf.append("}");

        return Response.ok(buf.toString()).build();
    }

}
