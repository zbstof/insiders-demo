import com.google.inject.AbstractModule
import services.{AmazonMappingService, BoostingService}

class Module extends AbstractModule {

  override def configure() = {
    bind(classOf[BoostingService]).asEagerSingleton()
  }
}