package com.hebace.service;

import com.hebace.domain.Customer;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Yinka Erinle
 */

@Path("/customers")
public class CustomerResource {

    private Map<Integer, Customer> customerDB = new ConcurrentHashMap<Integer, Customer>();
    private AtomicInteger idCounter = new AtomicInteger();

    @POST
    @Consumes("application/json")
    public Response createCustomer(Customer customer) {
        customer.setId(idCounter.incrementAndGet());
        customerDB.put(customer.getId(), customer);
        System.out.println("Created customer " + customer.getId());
        return Response.created(URI.create("/customers/"
                + customer.getId())).build();
    }

    @GET
    @Path("{id}")
    @Produces("application/json")
    public Customer getCustomer(@PathParam("id") int id) {
        System.out.println("Calling get customer");
        final Customer customer = customerDB.get(id);
        if (customer == null) {
            throw new WebApplicationException(
                    Response.Status.NOT_FOUND);
        }
        return  customer;
    }

    @PUT
    @Path("{id}")
    @Consumes("application/json")
    public void updateCustomer(@PathParam("id") int id,
                               Customer update) {
        Customer current = customerDB.get(id);
        if (current == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        current.setFirstName(update.getFirstName());
        current.setLastName(update.getLastName());
    }
}
