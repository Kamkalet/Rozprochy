from __future__ import print_function

import grpc

import currency_pb2
import currency_pb2_grpc

from enum import Enum
import currency_pb2


class CurrencyType(Enum):
    EUR = 0
    GBP = 1
    USD = 2
    CHF = 3


def get_rates(stub, currency_list):
    request = currency_pb2.SubscribeRequest()
    for currency_type in currency_list:
        request.type.append(currency_type.value)

    response = stub.Subscribe(request)
    for rate in response:
        print(rate)


def run():
    channel = grpc.insecure_channel('localhost:50051')
    stub = currency_pb2_grpc.CurrencyRateSubscriptionStub(channel)
    get_rates(stub, [CurrencyType.EUR])
    # repeated_response = stub.GetExchangeRates(currency_pb2.ExchangeRateRequest(value=))
    # print(repeated_response)


if __name__ == '__main__':
    run()
