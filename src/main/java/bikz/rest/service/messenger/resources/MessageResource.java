package bikz.rest.service.messenger.resources;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import javax.ws.rs.BeanParam;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import bikz.rest.service.messenger.model.Message;
import bikz.rest.service.messenger.resources.beans.MessageFilterBeans;
import bikz.rest.service.messenger.service.MessageService;

@Path("/messages")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MessageResource {
	
	MessageService messageservice=new MessageService();
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<Message> getJsonMessages(@BeanParam MessageFilterBeans filterbean)
	{
		
		if(filterbean.getYear()>0)
			return messageservice.getAllMessagesForYear(filterbean.getYear());
		if(filterbean.getStart()>=0 && filterbean.getSize()>0)
			return messageservice.getAllMessagesPaginated(filterbean.getStart(), filterbean.getSize());
		
		return messageservice.getAllMessages();
	}
	

	@GET
	@Produces(MediaType.TEXT_XML)
	public List<Message> getXmlMessages(@BeanParam MessageFilterBeans filterbean)
	{
		
		if(filterbean.getYear()>0)
			return messageservice.getAllMessagesForYear(filterbean.getYear());
		if(filterbean.getStart()>=0 && filterbean.getSize()>0)
			return messageservice.getAllMessagesPaginated(filterbean.getStart(), filterbean.getSize());
		
		return messageservice.getAllMessages();
	}
	
	@POST
	public Response addMessage(Message message,@Context UriInfo uriinfo){
		Message newMessage=messageservice.addMessage(message);
		String newId=String.valueOf(newMessage.getId());
		URI uri=uriinfo.getAbsolutePathBuilder().path(newId).build();
		return Response.created(uri)
				.entity(newMessage)
				.build();
	}
	
	@PUT
	@Path("/{messageId}")
	public Message updateMessage(@PathParam("messageId") long messageId,Message message){
		message.setId(messageId);
		return messageservice.updateMessage(message);
	}
	
	@DELETE
	@Path("/{messageId}")
	public void deleteMessage(@PathParam("messageId") long messageId){
		messageservice.removeMessage(messageId);
	}
	
	@GET
	@Path("/{messageId}")
	public Message getMessage(@PathParam("messageId") long messageId,@Context UriInfo uriinfo){
		Message message=messageservice.getMessage(messageId);
		message.addLink(getUriForSelf(uriinfo, message), "self");
		message.addLink(getUriForProfile(uriinfo, message), "profile");
		message.addLink(getUriForComments(uriinfo, message), "comments");
		return message;
	}

	private String getUriForComments(UriInfo uriinfo, Message message) {
		URI uri = uriinfo.getBaseUriBuilder()
				.path(MessageResource.class)
	       		.path(MessageResource.class, "getCommentResource")
	       		.path(CommentResource.class)
	       		.resolveTemplate("messageId", message.getId())
	            .build();
	    return uri.toString();
	}

	private String getUriForProfile(UriInfo uriinfo, Message message) {
		URI uri = uriinfo.getBaseUriBuilder()
	       		 .path(ProfileResource.class)
	       		 .path(message.getAuthor())
	             .build();
	        return uri.toString();
	}

	private String getUriForSelf(UriInfo uriinfo, Message message) {
		String uri = uriinfo.getBaseUriBuilder()
				 .path(MessageResource.class)
				 .path(Long.toString(message.getId()))
				 .build()
				 .toString();
				return uri;
	}
	

	@Path("/{messageId}/comments")
	public CommentResource getCommentResource(){
		return new CommentResource();
	}
	
}
