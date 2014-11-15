package con4.rest;

import con4.data.PlayerRepository;
import con4.internal.CustomWebApplicationException;
import con4.model.Player;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

/**
 * Created by ekor on 15/11/2014.
 */
@Path("/players")
@Produces(MediaType.APPLICATION_JSON)
public class PlayerResource {
    private static PlayerRepository playerRepo = PlayerRepository.getInstance();
    //-------------------------- create players --------------------------

    /** add player and return uri in location
     * @param uriInfo path
     * @param user id of the player. not null
     * @param name name of the player. not null
     * @return response with the created status and location or error response */
    @POST
    public Response addPlayer(@Context UriInfo uriInfo,@QueryParam("user") String user,@QueryParam("name") String name){
        Response response;
        Player player = playerRepo.createPlayer(user, name);
        URI location = uriInfo.getAbsolutePathBuilder().path("{arg1}").build(player.getUser());
        response = Response.created(location).entity(player.toJSON()).build();

        return response;
    }

    //-------------------------- get players --------------------------
    /** get all players
     * @return all players in repository */
    @GET
    public Response getAllPlayers(){
        Response response;
        response = Response.ok().entity(playerRepo.toJSON()).build();
        return  response;
    }

    /** get user by path param
     * @param user user name to get
     * @return player json
     * @throws con4.internal.CustomWebApplicationException returns appropriate response for possible errors */
    @Path("/{user}")
    @GET
    public Response getPlayerApi(@PathParam("user") String user) throws CustomWebApplicationException {
        Player player = playerRepo.getPlayer(user);
        return Response.ok().entity(player.toJSON()).build();
    }

}
