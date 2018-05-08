from threading import Thread
import time
import random


class ExchangeRateProvider:
    def __init__(self, server):
        # initial values ( dummy )
        self.service = server
        self.rates = {'EUR': 4.2, 'GBP': 3.8, 'USD': 3.5, 'CHF': 3.5}
        self.thread = Thread(target=self.quake_market)
        self.thread.start()

    def get_exchange_rate(self):
        return self.rates

    def quake_market(self):
        try:
            while True:
                time.sleep(5)
                for key, value in self.rates.items():
                    delta = self.rates[key] * random.uniform(-0.01, 0.01)
                    print(delta)
                    self.rates[key] = self.rates[key] + delta
                print_rates_update(self.rates)
        except KeyboardInterrupt:
            pass


def print_rates_update(rates):
    print("Watchout! Exchange rate quaked again!")
    print(rates)
