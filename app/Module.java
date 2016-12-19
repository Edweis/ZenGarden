import javax.inject.Singleton;

import com.google.inject.AbstractModule;

import controllers.tools.Global;

@Singleton
public class Module extends AbstractModule {

	@Override
	protected void configure() {
		bind(Global.class).asEagerSingleton();
	}

}
