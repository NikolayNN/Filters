#data <- read.csv("./src/test/resources/data.csv", sep=",", encoding = "UTF-8")
#plot(tab$X130.45, tab$X130.45.1, pch=20, xlim = "initial", ylim="filtered") #display points
#lines(tab$X130.45, tab$X130.45.1) #display lines

library(ggplot2)
options(stringsAsFactors = FALSE)

dataframe <- read.csv("./src/test/resources/data.csv", sep=",", encoding = "UTF-8", header = TRUE)

ggplot(data = dataframe, aes(x = datetime, y = initial)) +
  geom_point() + labs(x = "datetime", y = "initial")

ggplot(data = dataframe, aes(x = datetime, y = filtered)) +
  geom_point() + labs(x = "datetime", y = "filtered")
