package com.markeijsermans.linkerd.namers

import com.twitter.finagle._
import com.twitter.util.Activity
import com.twitter.util.Activity.Ok
import org.scalatest.FunSuite

class K8sNormalizeTest extends FunSuite {

  def runNamer(namer: Namer, path: Path): NameTree[Name] = {
    namer.lookup(path) match {
      case Activity(a) => {
        a.sample() match {
          case Ok(v) => v
          case _ => throw new Exception(s"Got a non OK response")
        }
      }
    }
  }

  test("K8sNormalize full segment") {
    val path = Path.read("/srv/packageName.ServiceName/MethodName")
    val n = runNamer(new K8sNormalize(2, false), path)
    assert(n == NameTree.Leaf(Name.Path(Path.Utf8("srv", "packagename-servicename", "MethodName"))))
  }

  test("K8sNormalize  segment 3") {
    val path = Path.read("/srv/extra-seg/packageName.ServiceName/MethodName")
    val n = runNamer(new K8sNormalize(3, false), path)
    assert(n == NameTree.Leaf(Name.Path(Path.Utf8("srv", "extra-seg", "packagename-servicename", "MethodName"))))
  }

  test("K8sNormalize service only") {
    val path = Path.read("/srv/packageName.ServiceName/MethodName")
    val n = runNamer(new K8sNormalize(2, true), path)
    assert(n == NameTree.Leaf(Name.Path(Path.Utf8("srv", "servicename", "MethodName"))))
  }

}
