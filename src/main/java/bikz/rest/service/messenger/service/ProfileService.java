package bikz.rest.service.messenger.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import bikz.rest.service.messenger.database.DatabaseClass;
import bikz.rest.service.messenger.model.Profile;

public class ProfileService {
	public Map<String,Profile> profiles=DatabaseClass.getProfiles();
	
	public ProfileService() {
		profiles.put("Bikram", new Profile(1L, "Bikram", "Bikram", "Modak"));
	}
	
	public List<Profile> getAllProfiles() {
		return new ArrayList<Profile>(profiles.values()); 
	}
	
	public Profile getProfile(String profileName) {
		return profiles.get(profileName);
	}
	
	public Profile addProfile(Profile profile) {
		profile.setId(profiles.size() + 1);
		profiles.put(profile.getProfileName(), profile);
		return profile;
	}
	
	public Profile updateProfile(Profile profile) {
		if (profile.getProfileName().isEmpty()) {
			return null;
		}
		profiles.put(profile.getProfileName(), profile);
		return profile;
	}
	
	public Profile removeProfile(String profileName) {
		return profiles.remove(profileName);
	}
}
