package ar.com.tandilweb.ApiServer.transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ar.com.tandilweb.ApiServer.dataTypesObjects.generic.ValidationException;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfile;
import ar.com.tandilweb.ApiServer.dataTypesObjects.users.UserProfileEdited;
import ar.com.tandilweb.ApiServer.persistence.domain.Users;
import ar.com.tandilweb.ApiServer.persistence.repository.UsersRepository;

@Service
public class UsersAdapter {
	
	@Autowired
	UsersRepository usersRepository;
	
	public UserProfile getUserByID(long id) throws ValidationException {
		
		Users user = usersRepository.findById(id);
		if(user == null) {
			throw new ValidationException(2, "User doesn't exists.");
		}
		UserProfile uP = new UserProfile();
		uP.operationSuccess = true;

		uP.idUser = user.getId_user();
		uP.nick = user.getNick_name();
		uP.photo = user.getPhoto();
		uP.chips = user.getChips();
		
		return uP;
	}
	
	public UserProfile updateProfileByID(long id, UserProfileEdited userProfile) throws ValidationException {
		
		Users user = usersRepository.findById(id);
		if(user == null) {
			throw new ValidationException(2, "User doesn't exists.");
		}
		
		boolean changed = false;
		
		if(userProfile.email != null && userProfile.email.length() > 0) {
			user.setEmail(userProfile.email);
			changed = true;
		}
		
		if(userProfile.password != null && userProfile.password.length() > 0) {
			user.setPassword(userProfile.password);
			changed = true;
		}
		
		if(userProfile.photo != null && userProfile.photo.length() > 0) {
			user.setPhoto(userProfile.photo);
			changed = true;
		}
		
		if(changed) {
			usersRepository.update(user);
		}
		
		UserProfile uP = new UserProfile();
		uP.operationSuccess = true;
		uP.idUser = user.getId_user();
		uP.nick = user.getNick_name();
		uP.photo = user.getPhoto();
		return uP;
		
	}

}
