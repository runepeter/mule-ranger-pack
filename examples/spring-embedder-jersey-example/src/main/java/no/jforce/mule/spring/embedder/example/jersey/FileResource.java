package no.jforce.mule.spring.embedder.example.jersey;

import com.sun.jersey.multipart.FormDataParam;
import no.jforce.mule.spring.embedder.example.mule.MuleStatistics;
import org.codehaus.jackson.map.ObjectMapper;
import org.mule.api.MuleContext;
import org.mule.management.stats.AllStatistics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Collections;

@Component
@Path("file")
public class FileResource
{
    @Autowired
    private MuleContext context;

    @Autowired
    private ObjectMapper jsonMapper;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response upload(@FormDataParam("file") File file) throws Exception
    {
        context.getClient().dispatch("file://target/data/in", file, Collections.<String, Object>emptyMap());

        return Response.ok().build();
    }

    @GET
    @Path("statistics.json")
    @Produces("application/json")
    public Response getStatistics()
    {
        AllStatistics statistics = context.getStatistics();

        //statistics.getServiceStatistics().iterator().next().get

        try
        {
            byte[] bytes = jsonMapper.writeValueAsBytes(new MuleStatistics(statistics));

            return Response.ok(bytes).build();

        } catch (IOException e)
        {
            throw new RuntimeException("Unable to serialize JSON data.", e);
        }
    }

}
