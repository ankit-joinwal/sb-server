package com.bitlogic.sociallbox.service.exception;

public enum RestErrorCodes {
	ERR_001, //Request Data Validation Failed
	ERR_002, //Unauthorized
	ERR_003,//Invalid Request
	ERR_010 , //Google API Errors
	ERR_020, //Entity Not Found
	ERR_050, //Server Error in service,
	ERR_051, //SQL Exceptions & DataAccessAcceptions
	ERR_052 //Unable to upload image
}
