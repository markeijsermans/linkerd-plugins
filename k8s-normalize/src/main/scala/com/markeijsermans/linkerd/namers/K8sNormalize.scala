package com.markeijsermans.linkerd.namers

import com.fasterxml.jackson.annotation.JsonIgnore
import com.twitter.finagle.Stack.Params
import com.twitter.finagle.{Name, NameTree, Namer, Path}
import com.twitter.util.Activity
import io.buoyant.namer.{NamerConfig, NamerInitializer}

/**
  * Normalize a path segment to what is a valid kubernetes service name
  *
  * @param segment     which path element to normalize
  * @param serviceOnly drop the package, and use only the service name (after last '.')
  */
class K8sNormalize(segment: Int, serviceOnly: Boolean) extends Namer {

  def lookup(path: Path): Activity[NameTree[Name]] =
    Activity.value(path match {
      case path if path.size > 0 =>
        rewrite(path) match {
          case Some(path) => NameTree.Leaf(Name.Path(path))
          case None => NameTree.Neg
        }
      case _ => NameTree.Neg
    })

  // Assume string doesn't start with a non-valid character as we're most likely parsing
  // a gRPC request (ie. generated from valid proto3)
  private[this] def normalize(s: String) = s.toLowerCase().replaceAll("""[^a-z0-9\-]""", "-")

  protected[this] def rewrite(path: Path): Option[Path] = {
    path.take(segment).drop(segment - 1) match {
      case Path.Utf8(s) =>
        val id = if (serviceOnly) s.substring(s.lastIndexOf('.') + 1) else s
        Some(path.take(segment - 1) ++ Path.Utf8(normalize(id)) ++ path.drop(segment))
      case _ => None
    }
  }
}


case class K8sNormalizeConfig(segment: Option[Int] = None,
                              serviceOnly: Option[Boolean]
                             ) extends NamerConfig {
  @JsonIgnore
  override def defaultPrefix: Path = Path.read("/com.markeijsermans.k8s.normalize")

  @JsonIgnore override protected
  def newNamer(params: Params): Namer = new K8sNormalize(
    segment.getOrElse(2),
    serviceOnly.getOrElse(true))
}


class K8sNormalizeInitializer extends NamerInitializer {
  val configClass: Class[_] = classOf[K8sNormalizeConfig]

  override def configId = "com.markeijsermans.k8s.normalize"
}
