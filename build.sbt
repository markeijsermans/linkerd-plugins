
def twitterUtil(mod: String) =
  "com.twitter" %% s"util-$mod" %  "6.40.0"

def finagle(mod: String) =
  "com.twitter" %% s"finagle-$mod" % "6.41.0"

def linkerd(mod: String) =
  "io.buoyant" %% s"linkerd-$mod" % "0.9.1"

val k8sNormalize =
  project.in(file("k8s-normalize")).
    settings(
      scalaVersion := "2.11.7",
      organization := "com.markeijsermans",
      name := "k8s-normalize",
      version := "1.0.3",
      resolvers ++= Seq(
        "twitter" at "https://maven.twttr.com",
        "local-m2" at ("file:" + Path.userHome.absolutePath + "/.m2/repository")
      ),
      libraryDependencies ++=
        finagle("http") % "provided" ::
        twitterUtil("core") % "provided" ::
        linkerd("core") % "provided" ::
        linkerd("protocol-http") % "provided" ::
        "org.scalatest" %% "scalatest" % "2.2.4" % "provided" ::
        Nil,
      assemblyOption in assembly := (assemblyOption in assembly).value.copy(includeScala = false)
    )
