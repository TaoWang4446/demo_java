package com.wst.demo_java.lamda;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description:
 * @author: pilaf
 * @create: 2019-10-09 20:56
 */
public class StreamApiDemo {

    public static void main(String[] args) {
        Trader raoul = new Trader("Raoul", "Cambridge");
        Trader mario = new Trader("Mario", "Milan");
        Trader alan = new Trader("Alan", "Cambridge");
        Trader brian = new Trader("Brain", "Cambridge");

        List<Transaction> transactions = Arrays.asList(
                new Transaction(brian, 2011, 300),
                new Transaction(raoul, 2012, 1000),
                new Transaction(raoul, 2011, 400),
                new Transaction(mario, 2012, 710),
                new Transaction(mario, 2012, 700),
                new Transaction(alan, 2012, 950)
        );

        //找出2011年发生的所有交易，并按交易额排序(从低到高)
        List<Transaction> transactions1 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparingInt(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(transactions1);
        //找出2011年发生的所有交易，并按交易额排序(从低到高)
        List<Transaction> transactions2 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                .sorted(Comparator.comparing(Transaction::getValue))
                .collect(Collectors.toList());
        System.out.println(transactions2);
        //找出2011年发生的所有交易，并按交易额排序(从高到低)
        List<Transaction> transactions3 = transactions.stream()
                .filter(t -> t.getYear() == 2011)
                //Comparator.comparing默认是升序排序，用reversed改成降序
                .sorted(Comparator.comparing(Transaction::getValue).reversed())
                .collect(Collectors.toList());
        System.out.println(transactions3);
    }
}


/**
 * @description: 交易员
 * @author: pilaf
 * @create: 2019-10-09 20:52
 */
@AllArgsConstructor
@Data
@ToString
class Trader {

    private final String name;

    private final String city;
}


/**
 * @description: 交易
 * @author: pilaf
 * @create: 2019-10-09 20:53
 */
@Data
@AllArgsConstructor
@ToString
class Transaction {

    private final Trader trader;

    private final int year;

    private final int value;
}
