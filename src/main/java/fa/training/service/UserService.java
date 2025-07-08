package fa.training.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fa.training.model.User;
import fa.training.repository.UserRepository;
import fa.training.util.Encryption;
import fa.training.util.FormatValidation;

@Service
public class UserService {


	@Autowired
	private UserRepository userRepository;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}
	
	public void save(User user) {
		userRepository.save(user);
	}

	public Optional<User> findByEmail(String email) {
        	return userRepository.findByEmail(email);
    	}
	
	public boolean isValidUser(String email, String password) {
		Optional<User> userOptional = userRepository.findByEmail(email);
		if(userOptional.isPresent()) {
			User user= userOptional.get();
		return user.getPassword().equals(password);
		}
		return false;
	}
	

    public Map<String, String> registerUser(User user, String confirmPassword) {
        Map<String, String> errors = new HashMap<>();

        if (!FormatValidation.isValidNameFormat(user.getFirstName()) && !FormatValidation.isValidNameFormat(user.getLastName())) {
            errors.put("name", "Name only contains alphabetic characters.");
        }
        
        // Check email exist
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            errors.put("email", "Email has been used.");
        }

        // Check valid email
        if (!FormatValidation.isValidEmailFormat(user.getEmail())) {
            errors.put("email", "Email format is incorrect.");
        }

        // Check valid phone number
        if (!FormatValidation.isValidPhoneFormat(user.getPhone())) {
            errors.put("phone", "Phone number must contain 10 digits.");
        }

        if (userRepository.findByPhone(user.getPhone()).isPresent()) {
            errors.put("phone", "Phone number has been used.");
        }
        
        if (!FormatValidation.isValidPasswordFormat(user.getPassword())) {
            errors.put("password", "Password must be in range 8-16 characters.");
        }

        // Check password and confirmPassword
        if (!user.getPassword().equals(confirmPassword)) {
            errors.put("confirmpass", "Password and Confirm Password do not match.");
        }

        if (!errors.isEmpty()) {
            return errors;
        }
        user.setRole("Customer");
        user.setPassword(Encryption.sha256(user.getPassword()));
        // Save user
        userRepository.save(user);

        return Map.of("success", "Registration successfully!");
    }

	public void update(User updatedUser) {
		updatedUser.setPassword(Encryption.sha256(updatedUser.getPassword()));
		userRepository.save(updatedUser);
	}
	
	public boolean updatePasswordByEmail(String email, String newPassword) {
	    Optional<User> optionalUser = userRepository.findByEmail(email);
	    if (optionalUser.isEmpty()) return false;
	    User user = optionalUser.get();
	    user.setPassword(Encryption.sha256(newPassword)); 
	    userRepository.save(user);
	    return true;
	}

}
