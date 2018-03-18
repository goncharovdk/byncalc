package com.rydlyouka;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Main {

    final static String defaultCurrency = "USD";

    @Parameter(names={"--amount", "-a"},
            description = "Money amount")
    BigDecimal amount;

    @Parameter(names={"--currency", "-c"},
            description = "Currency code")
    String currency;

    @Parameter(names={"--date", "-d"},
            description = "Currency rate date")
    Date date;

    @Parameter()
    private List<String> unnamedArgs = new ArrayList<>();

    public static void main(String... argv) {
	    Main main = new Main();

        JCommander commander = JCommander.newBuilder().addObject(main).build();
        commander.setCaseSensitiveOptions(false);
        commander.parse(argv);

        main.checkParameters();
        main.calculateResult();
    }

    private void checkParameters() {
        if (amount == null) {
            try {
                amount = new BigDecimal(unnamedArgs.get(0));
            } catch (Exception e) {
                amount = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
                System.out.println(String.format("Use default amount of %s.", amount.toString()));
            }
        }

        if (currency == null) {
            try {
                currency = unnamedArgs.get(1);
            } catch (Exception e) {
                currency = defaultCurrency;
                System.out.println(String.format("Use default currency of %s.", currency));
            }
        }

        if (date == null) {
            SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
            try {
                String unnamedDate = unnamedArgs.get(2);
                date = df.parse(unnamedDate);
            } catch (Exception e) {
                date = new Date();
                System.out.println(String.format("Use default date of %s.", df.format(date)));
            }
        }
    }

    private void calculateResult() {
        NBRBRate rate = NBRBRate.getRate(date, currency);
        BigDecimal result = amount
                .multiply(rate.officialRate)
                .divide(rate.scale, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println(rate);
        System.out.println(String.format("%s %s is %s BYN.", amount.toString(), currency, result.toString()));
    }

}
