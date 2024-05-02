package it.corso.controller;

import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;

import it.corso.dto.AddCategoriaDTO;
import it.corso.dto.CategoriaShowDTO;
import it.corso.dto.UserRegisterDTO;
import it.corso.dto.UtenteAggiornamentoDTO;
import it.corso.service.CategoriaService;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/categoria")
public class CategoriaController {
	@Autowired
	private CategoriaService categoriaService;
	
	@GET
	@Path("/categoria/{id}")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategoriaById(@PathParam("id") int id) {
		try {
			return Response.ok(categoriaService.getCategoriaById(id)).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/categorie")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getCategorie(@QueryParam("filter") String filterText) {
	    try {
	        List<CategoriaShowDTO> categorie;
	        
	        if (filterText != null && !filterText.isEmpty()) {
	            categorie = categoriaService.getCategorieFiltrate(filterText);
	        } else {
	        	categorie = categoriaService.getCategorie();
	        }

	        return Response.ok(categorie).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@POST
	@Path("/add")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response addCategoria(@RequestBody AddCategoriaDTO categoria) {
		try {			
			categoriaService.creaCategoria(categoria);
			
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@DELETE
	@Path("/delete/{id}")
	public Response delCategoria(@PathParam("id") int id) {
	    try {
	    	categoriaService.deleteCategoriaById(id);
	    	return Response.status(Response.Status.OK).build();
	    } catch (Exception e) {
	        return Response.status(Response.Status.BAD_REQUEST).build();
	    }
	}
	
	@PUT
	@Path("/update")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response updateCategoria(@RequestBody CategoriaShowDTO categoria) {
		try {
			categoriaService.updateCategoria(categoria);
			
			return Response.status(Response.Status.OK).build();
		} catch (Exception e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
}
