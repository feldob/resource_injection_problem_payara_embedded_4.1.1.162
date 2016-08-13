package bug;

import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Singleton;
import javax.ejb.Startup;

/**
 * Resource created with: asadmin create-custom-resource --restype
 * java.lang.String --factoryclass
 * org.glassfish.resources.custom.factory.PrimitivesAndStringFactory --property
 * value=wontshow customResource
 * 
 * @author felix dobslaw
 *
 */
@Singleton
@Startup
public class InjectResource {

	public InjectResource() {
	}

	@Resource(lookup = "customResource")
	private String customResource;
//	private String customResource = "wontshow";

	@PostConstruct
	private void initValues() {
		Logger.getLogger(InjectResource.class.getName()).log(Level.WARNING, "The String will only show if not injected: " + customResource, customResource);
	}
}