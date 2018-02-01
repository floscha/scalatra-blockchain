# Scalatra Blockchain

This simple Blockchain implementation is based on Daniel van Flymen's blog post [Building a Blockchain](https://medium.com/p/117428612f46) and its respective [source code](https://github.com/dvf/blockchain).
Unlike the original Python implementation, this one is based on the [Scala](https://www.scala-lang.org/) language and the [Scalatra](http://scalatra.org/) web micro-framework.

## Build & Run

We strongly recommend using [Docker](https://www.docker.com/) containers to run *Scalatra Blockchain*.
Therefore a Docker daemon (can be downloaded [here](https://www.docker.com/community-edition#/download)) needs to be running on your machine.

### Build Provided Docker Image

The easiest way to build the Docker image is by using the provided Dockerfile:

```
$ docker build . -t scalatra-blockchain
```


### Build Docker Image From Source

When making changes to the codebase, you can also build the Docker image using sbt:

```
$ sbt docker:publishLocal
```


### Run Docker Image

Once the Docker image has been built, it can easily be run on port 8080 like so:

```
$ docker run -p 8080:8080 scalatra-blockchain
```
