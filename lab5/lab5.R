library("tidyverse")
library("ggplot2")

#zadanie 1
results <- read.csv("C:/Users/Szymon/MOWNiT/lab4/results.csv") %>% filter(operation != "better")
avg_res <- aggregate(time ~ length:operation, data=results, FUN=mean) 
avg_res$time_sd <- aggregate(time ~ length:operation, data=results, FUN="sd")$time
avg_res

#zadanie 2
plot <- ggplot(avg_res, aes(x = length, y = time, color = operation)) + geom_point()# + geom_line()
plot <- plot + ylab("time [ms]") + xlab("length of an array") + ggtitle("matrix multiplication methods and their run time")
plot

#zadanie 3
plot <- plot + geom_errorbar(aes(ymin = time - time_sd, ymax = time + time_sd))
plot

#zaadanie 4
bavg_res <- avg_res %>% filter(operation == "blas")
navg_res <- avg_res %>% filter(operation == "naive")
bfit <- lm(time ~ poly(length, 3, raw=TRUE), data=bavg_res)
nfit <- lm(time ~ poly(length, 3, raw=TRUE), data=navg_res)
bpoints <- data.frame(length = seq(100, 500, 1))
npoints <- data.frame(length = seq(100, 500, 1))
bpoints$time <- predict(bfit, bpoints)
bpoints$operation <- rep("blas", 401);
npoints$operation <- rep("naive", 401);
npoints$time <- predict(nfit, npoints)
plot <- plot + geom_line(data=bpoints, aes(x = length, y = time)) + geom_line(data=npoints, aes(x = length, y = time))
plot

#zadanie 5
#data <- read.csv("C:/Users/Szymon/MOWNiT/lab5/covid_polska.csv") %>% filter(location == "Poland") %>% select(location, date, new_cases)
#data
#covid <- ggplot(data, aes(x = as.Date(date), y = new_cases)) + geom_point()
#covid <- covid + ylab("new cases") + xlab("date") + ggtitle("New covid cases in Poland")
#covid