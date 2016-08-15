package org.bug;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.glassfish.embeddable.BootstrapProperties;
import org.glassfish.embeddable.CommandResult;
import org.glassfish.embeddable.CommandRunner;
import org.glassfish.embeddable.GlassFish;
import org.glassfish.embeddable.GlassFishException;
import org.glassfish.embeddable.GlassFishProperties;
import org.glassfish.embeddable.GlassFishRuntime;

public class PayaraBootstrap

{
	private final static Logger LOGGER = Logger.getLogger(PayaraBootstrap.class.getName());

	public static void main(String[] args) throws IOException {
		try {
			GlassFish glassfish = bootstrapAndStartServer();
			createCustomResource(glassfish);
			deployServiceTo(glassfish, args[0]);
		}

		catch (GlassFishException ex) {
			LOGGER.log(Level.FINE, null, ex);
		}
	}

	private static GlassFish bootstrapAndStartServer() throws GlassFishException {
		BootstrapProperties bootstrap = new BootstrapProperties();
		GlassFishRuntime runtime = GlassFishRuntime.bootstrap();
		GlassFishProperties glassfishProperties = new GlassFishProperties();
		glassfishProperties.setPort("http-listener", 8085);
		glassfishProperties.setPort("https-listener", 8086);
		GlassFish glassfish = runtime.newGlassFish(glassfishProperties);
		glassfish.start();
		return glassfish;
	}

	private static void createCustomResource(GlassFish glassfish) throws GlassFishException {
		CommandRunner runner = glassfish.getCommandRunner();
		CommandResult result = runner.run("create-custom-resource", "--restype", "java.lang.String",
				"--factoryclass", "org.glassfish.resources.custom.factory.PrimitivesAndStringFactor", "--property",
				"value=wontshow", "customResource");
		LOGGER.log(Level.INFO, "exit status for the addition of custom resource: " + result.getExitStatus().name(),
				result);
	}
	
	private static void deployServiceTo(GlassFish glassfish, String serviceJarPath) throws GlassFishException {
		glassfish.getDeployer().deploy(new File(serviceJarPath));
	}
}