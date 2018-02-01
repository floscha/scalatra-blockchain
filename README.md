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


## End-to-End Tests

Rather than testing the blockchain manually by making HTTP calls using CURL or Postman like in the blog post, end-to-end tests can also be done automated using [this Python script](https://github.com/floscha/blockchain-end2end-test/blob/master/end2end_test.py).

To run the tests, simply following these steps:

1. Make sure the docker image has been built as described in the previous section.

2. Clone the test script from its repository and navigate to the folder:

```
$ git clone https://github.com/floscha/blockchain-end2end-test
$ cd blockchain-end2end-test
```
3. Install the required dependencies:

```
$ pip install -r requirements.txt
```

4. Run the test script as follows:

```
$ python end2end_test.py --nodes 10 --tasks clean setup connect sync-test
```

More details on how the script works can be found [here](https://github.com/floscha/blockchain-end2end-test/blob/master/README.md).
