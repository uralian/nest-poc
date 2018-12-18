organization in ThisBuild := "com.uralian"
version in ThisBuild := "0.1.0-SNAPSHOT"

scalaVersion in ThisBuild := "2.12.7"

scalacOptions in ThisBuild ++= Seq(
  "-feature",
  "-unchecked",
  "-deprecation",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-language:_",
  "-target:jvm-1.8",
  "-encoding", "UTF-8"
)

// scoverage options
coverageMinimum in ThisBuild := 80
coverageFailOnMinimum in ThisBuild := true

lazy val root = (project in file("."))
  .settings(name := "nest-poc")
  .aggregate(nest_dslink)

lazy val nest_dslink = (project in file("dslink-scala-nest"))
  .configs(IntegrationTest)
  .settings(Defaults.itSettings: _*)
  .settings(
    libraryDependencies ++= commonDependencies ++ testDependencies ++ itDependencies ++ Seq(
      "com.softwaremill.sttp" %% "core" % "1.5.1",
      "com.softwaremill.sttp" %% "json4s" % "1.5.1",
      "com.softwaremill.sttp" %% "akka-http-backend" % "1.5.1",
      "com.typesafe.akka" %% "akka-stream" % "2.5.11",
      "org.json4s" %% "json4s-native" % "3.6.2",
      "com.uralian" %% "sdk-dslink-scala" % "0.6.1"
    )
  )

lazy val commonDependencies = Seq(
)

lazy val testDependencies = Seq(
  "org.scalatest"         %% "scalatest"           % "3.0.5"     % Test,
  "org.scalacheck"        %% "scalacheck"          % "1.14.0"    % Test,
  "org.mockito"            % "mockito-core"        % "2.23.4"    % Test
)

lazy val itDependencies = Seq(
  "org.scalatest"         %% "scalatest"           % "3.0.5"     % IntegrationTest,
  "org.scalacheck"        %% "scalacheck"          % "1.14.0"    % IntegrationTest,
  "org.mockito"            % "mockito-core"        % "2.23.4"    % IntegrationTest
)
