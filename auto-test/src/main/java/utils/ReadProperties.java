package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ReadProperties {
	
	public Properties loadProperties(String propertiesPath)
	{
		
		Properties properties = new Properties();
		
		try {		
		
			properties.load(new FileInputStream(propertiesPath));
		
		} catch (IOException e) {
			
			System.out.println("Error while importing logging properties from path "+propertiesPath );
			e.printStackTrace();
		
		}
		
		return properties;
		
	}

}
