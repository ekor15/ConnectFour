package con4.rest;

import con4.data.GameRepository;
import con4.data.PlayerRepository;
import con4.internal.CustomWebApplicationException;
import con4.model.Game;
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
@Path("/games")
@Produces(MediaType.APPLICATION_JSON)
public class GameResource {

    private static GameRepository gameRepo = GameRepository.getInstance();
    private static PlayerRepository playerRepo = PlayerRepository.getInstance();

    //------- create games ------------------------------------------------------------------------------------------------
    /**
     * add game
     * @param uriInfo the path from the link will be used for location
     * @param user creator player
     * @return response with the game location or an error response
     */
    @POST
    public Response addGame(@Context UriInfo uriInfo , @QueryParam("user") String user){
        Response response;
        Game game = gameRepo.createGame(user);
        URI location = uriInfo.getAbsolutePathBuilder().path("{arg1}").build(game.getGameId());
        response = Response.created(location).entity(game.toJSON()).build();

        return response;
    }

    //------- get games ------------------------------------------------------------------------------------------------

    /** get all games
     * @return json array with all the games */
    @GET
    public Response getAllGames(){
        Response response;
        response = Response.ok().entity(gameRepo.toJSON()).build();
        return  response;
    }

    @Path("/{gameId}")
    @GET
    public Response getGameApi(@PathParam("gameId") int gameId) throws CustomWebApplicationException {
        Game game = gameRepo.getGame(gameId);
        return Response.ok().entity(game.toJSON()).build();
    }

    //------- game  ------------------------------------------------------------------------------------------------
    @Path("/{gameId}/{col}")
    @POST
    public Response takeTurn(@Context UriInfo uriInfo, @PathParam("gameId") int gameId,@PathParam("col") int col, @QueryParam("user") String user){
        Player player = playerRepo.getPlayer(user);
        Game game = gameRepo.getGame(gameId);
        checkGameOver(game);
        game.takeTurn(col,player);
        URI location = uriInfo.getBaseUriBuilder().path("games").path("{gameId}").build(game.getGameId());
        return Response.status(Response.Status.SEE_OTHER).location(location).build();
    }

    /** add other player to game
     * @param gameId game id
     * @param user player user
     * @return new game with added player
     * @throws CustomWebApplicationException returns appropriate response for possible errors */
    @Path("/{gameId}")
    @POST
    public Response joinGame(@PathParam("gameId") int gameId,@QueryParam("user") String user) throws CustomWebApplicationException{
        Player player = playerRepo.getPlayer(user);
        Game game = gameRepo.getGame(gameId);
        checkGameOver(game);
        game.joinGame(player);
        return Response.status(Response.Status.OK).entity(game.toJSON()).build();
    }

    public void checkGameOver(Game game){
        if (game.isGameOver()){
            throw new CustomWebApplicationException("game is over",Response.Status.BAD_REQUEST);
        }
    }

}

