# Ndle

A [Wordle](https://www.nytimes.com/games/wordle/index.html) [^1] solver that finds the minimum number of total guesses recursively. Each step, for each allowed guess, we count the number of groups the possible answers are partitioned into. Those with the largest number of groups are listed as candidates for the best guess and the process continues recursively. Other approaches to shortlist candidates are also considered, such as by entropy, by maximum bucket size, and by quadratic sum. 

The Wordle solver is fairly accurate and efficient, able to find a best solution(minimum average number of solves) in ~15 seconds. You may play with an interactive version [here](https://freshman.dev/wordle/leaderboard/qqljM5N/xiaorui), under Xiaorui. Variants of Wordle are also explored, in particular, an efficient [Dordle](https://dordlegame.io/) solver is still a work in progress. 

[^1]: New York Times modified the answer list slightly after acquiring Wordle, but the answer list is using the old one. 


