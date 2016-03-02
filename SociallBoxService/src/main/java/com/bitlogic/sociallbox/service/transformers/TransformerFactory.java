package com.bitlogic.sociallbox.service.transformers;

public class TransformerFactory {
	
	public enum Transformer_Types{
		MEETUP_TRANS , EVENT_TRANS , 
		MULTIPART_TO_EVENT_IMAGE_TRANFORMER ,
		MULTIPART_TO_MEETUP_IMAGE_TRANFORMER, 
		USER_TO_FRIEND_TRANSFORMER , 
		CREATE_EO_TO_EO_TRANSFORMER,
		EO_TO_EO_RESPONSE_TRANSFORMER
	}
	
	public static Transformer<?, ?> getTransformer(Transformer_Types types){
		if(types.equals(Transformer_Types.MEETUP_TRANS)){
			return MeetupTransformer.getInstance();
		}else if(types.equals(Transformer_Types.EVENT_TRANS)){
			return EventTransformer.getInstance();
		}else if(types.equals(Transformer_Types.MULTIPART_TO_EVENT_IMAGE_TRANFORMER)){
			return MultipartToEventImageTransformer.getInstance();
		}else if(types.equals(Transformer_Types.MULTIPART_TO_MEETUP_IMAGE_TRANFORMER)){
			return MultipartToMeetupImageTransformer.getInstance();
		}else if(types.equals(Transformer_Types.USER_TO_FRIEND_TRANSFORMER)){
			return  UsersToFriendsTransformer.getInstance();
		}else if (types.equals(Transformer_Types.CREATE_EO_TO_EO_TRANSFORMER)){
			return  CreateEOReqToEOTransformer.getInstance();
		}else if(types.equals(Transformer_Types.EO_TO_EO_RESPONSE_TRANSFORMER)){
			return  EOToEOResponseTransformer.getInstance();
		}
		
		
		throw new IllegalArgumentException("Wrong input to factory");
	}
}
