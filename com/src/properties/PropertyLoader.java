package src.properties ;

import java.io.FileInputStream ;
import java.io.IOException ;
import java.util.Properties ;

public class PropertyLoader
{
	static
	{
		load();
	}

	static Properties properties ;
				
	public static void load()
	{
		properties = new Properties();

		// Try loading via ClassLoader (works when packaged or loaded from output folder)
		try(java.io.InputStream stream = PropertyLoader.class.getClassLoader().getResourceAsStream("src/properties/config.properties"))
		{
			if(stream != null)
			{
				properties.load(stream);
				return;
			}
		}
		catch(Exception ignored) {}

		// Fallback to relative file paths
		String[] paths = {
			"src/properties/config.properties",
			"com/src/properties/config.properties",
			"properties/config.properties",
			"config.properties"
		};

		for(String p : paths)
		{
			java.io.File f = new java.io.File(p);
			if(f.exists())
			{
				try(FileInputStream file = new FileInputStream(f))
				{
					properties.load(file);
					return;
				}
				catch(IOException err)
				{
					System.err.println(err.getMessage());
				}
			}
		}
	}

	public static String getProperty(String key)
	{
		return properties.getProperty(key);
	}
}