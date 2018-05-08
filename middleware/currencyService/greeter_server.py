# Copyright 2015 gRPC authors.
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
from __future__ import print_function

"""The Python implementation of the GRPC helloworld.Greeter server."""

from concurrent import futures
import time

import grpc


from ExchangeRateProvider import ExchangeRateProvider

from currency_pb2_grpc import CurrencyRateSubscriptionServicer
from currency_pb2_grpc import add_CurrencyRateSubscriptionServicer_to_server

from currency_pb2 import CurrencyType
from currency_pb2 import ExchangeRateReply

TIME_TO_SLEEP = 5


class CurrencyService(CurrencyRateSubscriptionServicer):
    def __init__(self, provider, server):
        self.provider = provider
        self.server = server

    def Subscribe(self, request, context):
        print(request)
        while True:
            rates = self.provider.get_exchange_rate()
            for currency in request.type:
                reply = ExchangeRateReply(type=CurrencyType.Name(currency), value=rates[CurrencyType.Name(currency)])
                yield reply
                time.sleep(1)


def serve():
    print("Starting currency service server...")
    server = grpc.server(futures.ThreadPoolExecutor(max_workers=2))
    service = CurrencyService(ExchangeRateProvider(), server)
    add_CurrencyRateSubscriptionServicer_to_server(service, server)
    server.add_insecure_port('[::]:50051')
    server.start()


if __name__ == '__main__':
    serve()
