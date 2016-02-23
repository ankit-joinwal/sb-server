package com.bitlogic.sociallbox.service.transformers;

public class TransformerFactory {
	
	public enum Transformer_Types{
		MEETUP_TRANS , EVENT_TRANS , 
		MULTIPART_TO_EVENT_IMAGE_TRANFORMER ,
		MULTIPART_TO_MEETUP_IMAGE_TRANFORMER, 
		USER_TO_FRIEND_TRANSFORMER
	}
	
	public static Transformer<?, ?> getTransformer(Transformer_Types types){
		if(types.equals(Transformer_Types.MEETUP_TRANS)){
			return new MeetupTransformer();
		}else if(types.equals(Transformer_Types.EVENT_TRANS)){
			return new EventTransformer();
		}else if(types.equals(Transformer_Types.MULTIPART_TO_EVENT_IMAGE_TRANFORMER)){
			return new MultipartToEventImageTransformer();
		}else if(types.equals(Transformer_Types.MULTIPART_TO_MEETUP_IMAGE_TRANFORMER)){
			return new MultipartToMeetupImageTransformer();
		}else if(types.equals(Transformer_Types.USER_TO_FRIEND_TRANSFORMER)){
			return new UsersToFriendsTransformer();
		}
		
		
		throw new IllegalArgumentException("Wrong input to factory");
	}
}
