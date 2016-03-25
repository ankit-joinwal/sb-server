package com.bitlogic.sociallbox.data.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "EVENT_DETAILS",indexes = { @Index(name = "IDX_EVENT_DETAILS", columnList = "ORGANIZER_ADMIN_ID") })
@XmlRootElement(name="event_details")
public class EventDetails {

	@Id
	@Column(name = "EVENT_ID",length=50)
	@GeneratedValue(generator = "gen")
	@GenericGenerator(name = "gen", strategy = "foreign", parameters = @Parameter(name = "property", value = "event"))
	private String id;

	@OneToOne
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private Event event;
	
	@Embedded
	@Column(name="LOCATION")
	@XmlElement
	private Location location;

	@ManyToOne(fetch=FetchType.EAGER)
	@JoinColumn(name="ORGANIZER_ADMIN_ID")
	private EventOrganizerAdmin organizerAdmin;
	
	@OneToMany(mappedBy="eventDetails",fetch=FetchType.LAZY)
	@Cascade(value=CascadeType.ALL)
	@JsonIgnore
	private Set<EventAddressInfo> addressComponents = new HashSet<>();
	
	@XmlTransient
	@JsonIgnore
	@Column(nullable = false,name="CREATE_DT")
	private Date createDt;

	@XmlTransient
	@JsonIgnore
	@Column(name="UPD_DT")
	private Date updateDt;
	
	public Date getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(Date updateDt) {
		this.updateDt = updateDt;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public Set<EventAddressInfo> getAddressComponents() {
		return addressComponents;
	}

	public void setAddressComponents(Set<EventAddressInfo> addressComponents) {
		this.addressComponents = addressComponents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public EventOrganizerAdmin getOrganizerAdmin() {
		return organizerAdmin;
	}

	public void setOrganizerAdmin(EventOrganizerAdmin organizer) {
		this.organizerAdmin = organizer;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	
	@Override
	public String toString() {
		return "EventDetails [location="+this.location+ " , organizer="+this.organizerAdmin+" ]";
	}

}
