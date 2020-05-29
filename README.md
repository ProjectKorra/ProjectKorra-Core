<p align="center">
	<a href="https://github.com/ProjectKorra/ProjectKorra-Core/"><img src="http://i.imgur.com/8XB8XHF.png" alt="ProjectKorra - Core"></a>
</p>

<p align="center"><a href="https://projectkorra.com/">ProjectKorra</a> | Minecraft Reimagined - Master the elements from <i>Avatar: The Last Airbender</i> and <i>The Legend of Korra</i>i></p>

<p align="center">
	<a href="https://travis-ci.org/ProjectKorra/ProjectKorra-Core" target="_blank">
		<img src="https://travis-ci.org/ProjectKorra/ProjectKorra-Core.svg?branch=master" alt="Build Status">
	</a>
	<a href="https://discord.gg/pPJe5p3" target="_blank">
		<img src="https://img.shields.io/badge/discord-join-7289DA.svg?logo=discord&longCache=true&style=flat"/>
	</a>
</p>

## Getting Started

If you're looking for already compiled versions, visit our [GitHub Releases](https://github.com/ProjectKorra/ProjectKorra-Core/releases) page for standard releases. Otherwise, read on to learn how you can compile the plugin yourself.

### Prerequisites

In order to build ProjectKorra-Core, you need to have the [Java JDK](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed on your system (version 8u251 or later).
Additionally, ProjectKorra-Core utilizes [Gradle](https://gradle.org/) as its source control system. You can either download and install Gradle yourself or use the provided Gradle wrapper (`gradlew`).

### Building

You can start by either downloading the zipped [source code](https://github.com/ProjectKorra/ProjectKorra-Core/archive/master.zip) directly and unpacking it on your system or cloning it via Git (recommended):

```console
git clone https://github.com/ProjectKorra/ProjectKorra-Core.git
```

Now, navigate to your new `ProjectKorra-Core` directory in a terminal and execute the corresponding Gradle task to get your desired result.

Compile the plugin:
```console
gradle build
```

Run all JUnit tests:
```console
gradle test
```

Compile all documentation:
```console
gradle javadoc
```

Start a local test server with a newly built binary.
```console
gradle runServer
```

*Note: If you are going to use the provided gradle wrapper, replace `gradle` with `gradlew` in the above commands*

## Contributing
We are always eager to see active members in the community posting issues or making pull requests and invite you to do so. We just ask that all incoming pull requests be made to the `wip` branch and that you adhere to our issue / pull request templates.