package com.bitlogic.sociallbox.service.business.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bitlogic.sociallbox.data.model.EOAdminStatus;
import com.bitlogic.sociallbox.data.model.EventOrganizer;
import com.bitlogic.sociallbox.data.model.EventOrganizerAdmin;
import com.bitlogic.sociallbox.data.model.User;
import com.bitlogic.sociallbox.data.model.response.EOAdminProfile;
import com.bitlogic.sociallbox.data.model.response.EventOrganizerProfile;
import com.bitlogic.sociallbox.service.business.AdminService;
import com.bitlogic.sociallbox.service.dao.EventOrganizerDAO;
import com.bitlogic.sociallbox.service.transformers.Transformer;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory;
import com.bitlogic.sociallbox.service.transformers.TransformerFactory.TransformerTypes;
import com.bitlogic.sociallbox.service.utils.LoggingService;

@Service("adminService")
@Transactional
public class AdminServiceImpl extends LoggingService implements AdminService{

	private static final Logger LOGGER = LoggerFactory.getLogger(AdminServiceImpl.class);
	
	@Override
	public Logger getLogger() {
		return LOGGER;
	}
	@Autowired
	private EventOrganizerDAO eventOrganizerDAO;
	
	@Override
	public List<EOAdminProfile> getPendingProfiles() {
		String LOG_PREFIX = "AdminServiceImpl-getPendingProfiles";
		List<EventOrganizerAdmin> pendingEOs = this.eventOrganizerDAO.getPendingEOAdminProfiles();
		List<EOAdminProfile> pendingProfiles = new ArrayList<EOAdminProfile>();
		logInfo(LOG_PREFIX, "Preparing Pending Profiles ");
		for(EventOrganizerAdmin admin : pendingEOs){
			User user = admin.getUser();
			EventOrganizer organizer = admin.getOrganizer();
			Transformer<EventOrganizerProfile, EventOrganizer> eoProfileTransformer = 
					(Transformer<EventOrganizerProfile, EventOrganizer>) TransformerFactory.getTransformer(TransformerTypes.EO_TO_EO_RESPONSE_TRANSFORMER);
			EventOrganizerProfile eventOrganizerProfile = eoProfileTransformer.transform(organizer);
			EOAdminProfile adminProfile = new EOAdminProfile(eventOrganizerProfile, admin, user);
			pendingProfiles.add(adminProfile);
		}
		return pendingProfiles;
	}
	
	@Override
	public void approveOrRejectProfiles(List<Long> profileIds,EOAdminStatus status) {
		String LOG_PREFIX = "AdminServiceImpl-approveOrRejectProfiles";
		List<EventOrganizerAdmin> eoAdmins = this.eventOrganizerDAO.getEOAdminProfilesByIds(profileIds);
		Date now = new Date();
		if(eoAdmins!=null){
			for(EventOrganizerAdmin organizerAdmin: eoAdmins){
				organizerAdmin.setStatus(status);
				organizerAdmin.setUpdateDt(now);
			}
		}
		logInfo(LOG_PREFIX, "Updated profiles successfully");
	}
}
