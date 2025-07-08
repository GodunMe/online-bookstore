package fa.training.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

@Component
public class FormatValidation {
	
	private static final String NAME_REGEX = "^[a-zA-Z ]+$";
	private static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final String PHONE_REGEX = "^[0-9]{10}$";
	private static final String PASS_REGEX = "^.{8,16}$";


	public static boolean isValidNameFormat(String s) {
		Pattern p = Pattern.compile(NAME_REGEX);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	public static boolean isValidEmailFormat(String s) {
		Pattern p = Pattern.compile(EMAIL_REGEX);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	public static boolean isValidPhoneFormat(String s) {
		Pattern p = Pattern.compile(PHONE_REGEX);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
	public static boolean isValidPasswordFormat(String s) {
		Pattern p = Pattern.compile(PASS_REGEX);
		Matcher m = p.matcher(s);
		return m.matches();
	}
	
}
