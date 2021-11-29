# Recommender-System
**Team:** Spark Wars. \
**Authors:** Lada Morozova, Denis Schegletov, Danis Alukaev. \
**Group:** B19-DS-01, Innopolis University.

## Overview
The goal of the assignment was to finalize the implementation of a movie recommendation system in Scala. Moreover, it was required to use Spark framework and implement it not only on local Hadoop cluster but also on Private Network Hadoop cluster.

To finalize implementation, team implement `parseTitle` and `rmse` functions; post-processing of recommendations; method that allow to load user data directly instead of surveying user. Moreover, the team will run the application with different model ranks, compare and evaluate the differences between baseline and prediction for the model with a higher rank. In addition, low-frequency items will be excluded from the training and recommendation process.

## Repositoty structure
The directory [src](./src) contains Scala code for recommender system application. You can run it in the private network by following the [guidelines](https://hackmd.io/@Booguy/BkbZekJFY) we prepared. The [sbt](./build.sbt) file is used to compile the Scala code in `jar` file. The [compiled version](./binary.jar) of source code is uploaded in this repository. You also might be interested on how the sample output look like, so go to the [output.txt](./output.txt) file.
