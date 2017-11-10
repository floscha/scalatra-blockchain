import org.scalatra._
import javax.servlet.ServletContext

import controllers.{BlockchainController, NodesController}

class ScalatraBootstrap extends LifeCycle {
  override def init(context: ServletContext) {
    context.mount(new BlockchainController, "/*")
    context.mount(new NodesController, "/nodes/*")
  }
}
