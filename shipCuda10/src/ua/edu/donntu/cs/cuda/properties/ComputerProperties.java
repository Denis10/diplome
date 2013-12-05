package ua.edu.donntu.cs.cuda.properties;

/**
 * Класс для вывода свойств в консоль. Не используется
 * 
 * @author Denis Vodolazskiy
 */
public class ComputerProperties {
	public ComputerProperties() {
		java.util.Properties properties = System.getProperties();
		// Вывод всех свойств.
		// properties.list(System.out);
		//
		// System.out.println(properties.getProperty("user.home"));
		System.out.println("User name: " + properties.getProperty("user.name"));
		// System.out.println("User country: "+properties.getProperty("user.country"));
		System.out.println("OS: " + properties.getProperty("os.name"));
		System.out.println("OS version: "
				+ properties.getProperty("os.version"));
		System.out.println("OS architecture: "
				+ properties.getProperty("os.arch"));
		System.out.println("Java version: "
				+ properties.getProperty("java.version"));
	}
}
