#ifndef BANK_ICE
#define BANK_ICE

module Bank
{
  enum CurrencyIce { PLN, EUR, USD, GBP };

  struct MoneyAmount {
    long amount;
    CurrencyIce currency;
  };

  struct Date {
    short day;
    short month;
    int year;
  };

  struct PersonData {
    string name;
    string lastName;
    string pesel;
  };

  struct CreditCostPair {
    MoneyAmount nativeCurrencyMoney;
    MoneyAmount actualCurrencyMoney;
  };

  exception NotSupportedCurrencyException {
    string message = "Not supported currency was used";
  };

  exception AccountTypeException {
    string message = "User with standard account cannot ask for credit";
  };

  interface Account {
    CreditCostPair getCreditCost(MoneyAmount amount, Date finishDate)
        throws NotSupportedCurrencyException, AccountTypeException;
    MoneyAmount getActualAmount();
  };

  interface BankService {
    string createAccount(PersonData pD, MoneyAmount monthIncome) throws NotSupportedCurrencyException;
    Account* loginStandard(string pesel, string password);
    Account* loginPremium(string pesel, string password);
  };
};

#endif