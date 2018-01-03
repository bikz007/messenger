package bikz.rest.service.messenger.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import bikz.rest.service.messenger.database.DatabaseClass;
import bikz.rest.service.messenger.exception.DataNotFoundException;
import bikz.rest.service.messenger.model.Message;

public class MessageService {
	
	public static Map<Long , Message> messages=DatabaseClass.getMessages();
	
	public MessageService(){
		messages.put(1L,new Message(1L,"Hello World","Bikz"));
		messages.put(2L,new Message(2L,"Hello Jersey","B-BuddyCool"));
		messages.put(3L,new Message(3L,"Hello Jax-RS","B-BuddyCool"));
	}
	public List<Message> getAllMessages(){
		return new ArrayList<Message>(messages.values());
	}
	
	
	public List<Message> getAllMessagesForYear(int year){
		
		List<Message> messagesForYear=new ArrayList<>();
		Calendar cal=Calendar.getInstance();
		for(Message message:messages.values()){
			cal.setTime(message.getCreated());
			if(cal.get(Calendar.YEAR)==year)
				messagesForYear.add(message);
		}
		return messagesForYear;
	}
	
	public List<Message> getAllMessagesPaginated(int start,int size){
		ArrayList<Message> messagePage=new ArrayList<>(messages.values());
		if(start+size>messagePage.size()) return new ArrayList<Message>();
		return messagePage.subList(start, start+size);
		
	}
	public Message getMessage(long id){
		 Message message=messages.get(id);
		 if(message==null){
			 throw new DataNotFoundException("Message with id "+id+" not found");
		 }
		 return message;
	}
	
	public Message addMessage(Message message){
		message.setId(messages.size()+1);
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message updateMessage(Message message){
		if(message.getId()<=0)
			return null;
		
		messages.put(message.getId(), message);
		return message;
	}
	
	public Message removeMessage(long id){
		return messages.remove(id);
	}
}
