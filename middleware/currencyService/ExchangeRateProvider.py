from threading import Thread
import time
import random


class ExchangeRateProvider:
    def __init__(self, server):
        # initial values ( dummy )
        self.server = server
        self.rates = {'EUR': 4.2, 'GBP': 3.8, 'USD': 3.5, 'CHF': 3.5}
        self.thread = Thread(target = self.quake_market)
        self.thread.start()

    def get_exchange_rate(self):
        return self.rates

    def quake_market(self):
        try:
            while True:
                time.sleep(5)
                for key, value in self.rates.items():
                    self.rates[key] = self.rates[key] * random.uniform(0, 1)
                print_rates_update(self.rates)

        except KeyboardInterrupt:
            self.server.stop(0)


def print_rates_update(rates):
    print("Watchout! Exchange rate quaked again!")
    print(rates)
