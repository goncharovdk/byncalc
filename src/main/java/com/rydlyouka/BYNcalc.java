package com.rydlyouka;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BYNcalc {

    final static String defaultCurrency = "USD";

    @Parameter(names={"--amount", "-a"},
            description = "Money amount")
    BigDecimal amount;

    @Parameter(names={"--currency", "-c"},
            description = "Currency code")
    String currency;

    @Parameter(names={"--date", "-d"},
            description = "Currency rate date",
            converter = DateConverter.class)
    Date date;

    @Parameter(names={"--help", "-h"}, help = true)
    private boolean help;
    
    @Parameter()
    private List<String> unnamedArgs = new ArrayList<>();

    public static void main(String... argv) {
	    BYNcalc byncalc = new BYNcalc();

        JCommander commander = JCommander.newBuilder().addObject(byncalc).build();
        commander.setCaseSensitiveOptions(false);
        commander.parse(argv);

        if (byncalc.help) {
            commander.usage();
            return;
        }

        byncalc.checkParameters();
        byncalc.calculateResult();
    }

    private void checkParameters() {
        if (amount == null) {
            try {
                amount = new BigDecimal(unnamedArgs.get(0)).setScale(2, RoundingMode.HALF_UP);
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
        currency = currency.toUpperCase();

        if (date == null) {
            DateConverter converter = new DateConverter();
            try {
                String unnamedDate = unnamedArgs.get(2);
                date = converter.convert(unnamedDate);
            } catch (Exception e) {
                date = null;
            }
        }
        if (date == null) {
            date = new Date();
            System.out.println(String.format("Use default date of %s (today).", DateConverter.format(date)));
        }
    }

    private void calculateResult() {
        NBRBRate rate = NBRBRate.getRate(date, currency);
        if (rate == null) {
            System.out.println("Exchange rate is not available");
            return;
        }
        
        BigDecimal result = amount
                .multiply(rate.officialRate)
                .divide(rate.scale, RoundingMode.HALF_UP)
                .setScale(2, RoundingMode.HALF_UP);

        System.out.println(rate);
        System.out.println(String.format("%s %s is %s BYN.", amount.setScale(2, RoundingMode.HALF_UP).toString(), currency, result.toString()));
    }

}
