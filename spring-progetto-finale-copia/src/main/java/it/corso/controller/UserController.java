package it.corso.controller;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.glassfish.jersey.message.internal.HttpHeaderReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import it.corso.dto.UserRegisterDTO;
import it.corso.dto.UtenteAggiornamentoDTO;
import it.corso.dto.UtenteLoginRequestDTO;
import it.corso.dto.UtenteLoginResponseDTO;
import it.corso.dto.UtenteShowDTO;
import it.corso.model.Ruolo;
import it.corso.model.Utente;
import it.corso.service.Blacklist;
import it.corso.service.UserService;
import jakarta.validation.Valid;
//import jakarta.validation.constraints.Pattern; //non questo ma import java.util.regex.Pattern; o non funziona il matches
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam; //utilizzare questo import e non l'altro o si potrebbero riscontrare problemi
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/user")
public class UserController 
{
	@Autowired
	private UserService userService;
	
	@Autowired
	private Blacklist blacklist;
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response addUser(@Valid @RequestBody UserRegisterDTO user) 
	{
		try 
		{
			if (!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{8,}$", user.getPassword())) 
			{
				return Response.status(Response.Status.BAD_REQUEST).build();
			}
			
			if (userService.existsUserByEmail(user.getEmail())) 
			{
				return Response.ok().build();
			}
			
			userService.userRegistration(user);
			
			return Response.ok().build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@DELETE
	@Path("/delete/{email}")
	public Response delUserByEmail(@Valid @PathParam("email") String email) 
	{
	    try 
	    {
	    	userService.deleteUser(email);
	    	return Response.ok().build();
	    } 
	    catch (Exception e) 
	    {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@DELETE
	@Path("/deleteById/{id}")
	public Response delUserById(@PathParam("id") int id) 
	{
	    try 
	    {
	    	userService.deleteUserById(id);
	    	return Response.ok().build();
	    } 
	    catch (Exception e) 
	    {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	
	@GET
	@Path("/get")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUser(@QueryParam("id") int id) 
	{
	    try 
	    {
	        return Response.ok(userService.getUserById(id)).build();
	    } 
	    catch (Exception e) 
	    {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@GET
	@Path("/show")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findByEmail(@QueryParam("email") String email) 
	{
		try 
		{
			if (email != null && !email.isEmpty()) 
			{
				UtenteShowDTO utenteShowDTO = userService.getUserByEmail(email);
				
				return Response.status(Response.Status.OK).entity(utenteShowDTO).build();
			}

			return Response.status(Response.Status.BAD_REQUEST).build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateUser(@RequestBody UtenteAggiornamentoDTO utente) 
	{
		try 
		{
			userService.updateUserData(utente);
			
			return Response.status(Response.Status.OK).build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/get-all")
	@Produces(MediaType.APPLICATION_JSON)
	public Response findAllUsers() 
	{
		try 
		{
			return Response.status(Response.Status.OK).entity(userService.getUsers()).build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response loginUtente(@RequestBody UtenteLoginRequestDTO utenteLoginRequestDTO) 
	{
		try 
		{
			if (userService.loginUtente(utenteLoginRequestDTO)) 
			{
				return Response.ok(issueToken(utenteLoginRequestDTO.getEmail())).build();
			}
			
			return Response.status(Response.Status.BAD_REQUEST).build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/logout")
	public Response logoutUtente(ContainerRequestContext containerRequestContext) 
	{
		try 
		{
			String authorizationHeader = containerRequestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
			String token = authorizationHeader.substring("Bearer".length()).trim();
			
			blacklist.invalidateToken(token);
			
			return Response.status(Response.Status.OK).build();
		} 
		catch (Exception e) 
		{
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	private UtenteLoginResponseDTO issueToken(String email) 
	{
		//eseguiamo una cifratura attraverso l'algoritmo di cifratura HMAC
		byte[] secretKey = "efhsrehfsehhfiosejfijseiofjiosejfios".getBytes();
		
		Key key = Keys.hmacShaKeyFor(secretKey);
		
		Utente infoUtente = userService.findByEmail(email);
		
		//claims (""informazioni"")
		Map<String, Object> map = new HashMap<>();
		map.put("email", email);
		map.put("nome", infoUtente.getNome());
		map.put("cognome", infoUtente.getCognome());
		
		List<String> ruoli = new ArrayList<>();
		for (Ruolo ruolo : infoUtente.getRuoli()) 
		{
			ruoli.add(ruolo.getTipologia().name()); //name() è come toString() ma è più user-friendly, dà più l'idea che restituisce il nome della tipologia
		}
		map.put("ruoli", ruoli);
		
		//data di creazione e ttl
		Date creationDate = new Date();
		Date end = java.sql.Timestamp.valueOf(LocalDateTime.now().plusMinutes(15L));
		
		//creazione del token firmato con la chiave segreta creata prima
		String jwtToken = Jwts.builder()
			.setClaims(map)
			.setIssuer("http://localhost:8080")
			.setIssuedAt(creationDate)
			.setExpiration(end)
			.signWith(key)
			.compact();
		
		UtenteLoginResponseDTO token = new UtenteLoginResponseDTO();
		token.setToken(jwtToken);
		token.setTokenCreationTime(creationDate);
		token.setTtl(end);
			
		return token;
	}
}
