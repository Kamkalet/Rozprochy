

import Bank.*;
import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.LocalException;

import com.zeroc.Ice.ObjectPrx;
import com.zeroc.Ice.Util;

public class Client {
    public static void main(String[] args) {
        int status = 0;
        Communicator communicator = null;
        AccountPrx accountObj = null;

        try {
            communicator = Util.initialize(args);

            // 2. Uzyskanie referencji obiektu na podstawie linii w pliku konfiguracyjnym
            //Ice.ObjectPrx base = communicator.propertyToProxy("Calc1.Proxy");
            // 2. To samo co powy�ej, ale mniej �adnie
            ObjectPrx base = communicator.stringToProxy("bank/bank1:tcp -h localhost -p 10000:udp -h localhost -p 10000");

            BankServicePrx obj = BankServicePrxHelper.checkedCast(base);
            if (obj == null) throw new Error("Invalid proxy");

            String line = null;
            java.io.BufferedReader in = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            do {
                try {
                    System.out.print("==> ");
                    System.out.flush();
                    line = in.readLine();
                    if (line == null) {
                        break;
                    }
//                    if (line.equals("create")) {
//                        PersonData data = new PersonData("Jaroslaw", "Bez S", "88444444");
//                        MoneyAmount moneyAmount = xnew MoneyAmount(10000, CurrencyIce.USD);
//                        accountObj = obj.createAccount(data, moneyAmount);
//                        System.out.println(data);
//                    }
                    if (line.equals("getAmount") && accountObj != null) {
                        System.out.println(moneyAmountString(accountObj.getActualAmount()));
                    }
                    if (line.equals("creditCost") && accountObj != null) {
                        CreditCostPair creditCost = accountObj.getCreditCost(
                                new MoneyAmount(100000, CurrencyIce.USD),
                                new Date((short) 12, (short) 12, 2018));
                        System.out.println(moneyAmountString(creditCost.actualCurrencyMoney) + " "
                                + moneyAmountString(creditCost.nativeCurrencyMoney));
                    }
                } catch (java.io.IOException ex) {
                    System.err.println(ex);
                }
            }
            while (!line.equals("x"));
        } catch (LocalException e) {
            e.printStackTrace();
            status = 1;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                System.err.println(e.getMessage());
                status = 1;
            }
        }
        System.exit(status);
    }

    private static String moneyAmountString(MoneyAmount moneyAmount) {
        return "" + moneyAmount.amount + " " + moneyAmount.currency;
    }
}