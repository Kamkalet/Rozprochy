# Generated by the protocol buffer compiler.  DO NOT EDIT!
# source: currency.proto

import sys
_b=sys.version_info[0]<3 and (lambda x:x) or (lambda x:x.encode('latin1'))
from google.protobuf.internal import enum_type_wrapper
from google.protobuf import descriptor as _descriptor
from google.protobuf import message as _message
from google.protobuf import reflection as _reflection
from google.protobuf import symbol_database as _symbol_database
from google.protobuf import descriptor_pb2
# @@protoc_insertion_point(imports)

_sym_db = _symbol_database.Default()




DESCRIPTOR = _descriptor.FileDescriptor(
  name='currency.proto',
  package='currency',
  syntax='proto3',
  serialized_pb=_b('\n\x0e\x63urrency.proto\x12\x08\x63urrency\"8\n\x10SubscribeRequest\x12$\n\x04type\x18\x01 \x03(\x0e\x32\x16.currency.CurrencyType\"H\n\x11\x45xchangeRateReply\x12$\n\x04type\x18\x01 \x01(\x0e\x32\x16.currency.CurrencyType\x12\r\n\x05value\x18\x02 \x01(\x01*2\n\x0c\x43urrencyType\x12\x07\n\x03\x45UR\x10\x00\x12\x07\n\x03GBP\x10\x01\x12\x07\n\x03USD\x10\x02\x12\x07\n\x03\x43HF\x10\x03\x32\x64\n\x18\x43urrencyRateSubscription\x12H\n\tSubscribe\x12\x1a.currency.SubscribeRequest\x1a\x1b.currency.ExchangeRateReply\"\x00\x30\x01\x42&\n\x0bsr.grpc.genB\x0f\x43\x61lculatorProtoP\x01\xa2\x02\x03HLWb\x06proto3')
)

_CURRENCYTYPE = _descriptor.EnumDescriptor(
  name='CurrencyType',
  full_name='currency.CurrencyType',
  filename=None,
  file=DESCRIPTOR,
  values=[
    _descriptor.EnumValueDescriptor(
      name='EUR', index=0, number=0,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='GBP', index=1, number=1,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='USD', index=2, number=2,
      options=None,
      type=None),
    _descriptor.EnumValueDescriptor(
      name='CHF', index=3, number=3,
      options=None,
      type=None),
  ],
  containing_type=None,
  options=None,
  serialized_start=160,
  serialized_end=210,
)
_sym_db.RegisterEnumDescriptor(_CURRENCYTYPE)

CurrencyType = enum_type_wrapper.EnumTypeWrapper(_CURRENCYTYPE)
EUR = 0
GBP = 1
USD = 2
CHF = 3



_SUBSCRIBEREQUEST = _descriptor.Descriptor(
  name='SubscribeRequest',
  full_name='currency.SubscribeRequest',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='type', full_name='currency.SubscribeRequest.type', index=0,
      number=1, type=14, cpp_type=8, label=3,
      has_default_value=False, default_value=[],
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=28,
  serialized_end=84,
)


_EXCHANGERATEREPLY = _descriptor.Descriptor(
  name='ExchangeRateReply',
  full_name='currency.ExchangeRateReply',
  filename=None,
  file=DESCRIPTOR,
  containing_type=None,
  fields=[
    _descriptor.FieldDescriptor(
      name='type', full_name='currency.ExchangeRateReply.type', index=0,
      number=1, type=14, cpp_type=8, label=1,
      has_default_value=False, default_value=0,
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
    _descriptor.FieldDescriptor(
      name='value', full_name='currency.ExchangeRateReply.value', index=1,
      number=2, type=1, cpp_type=5, label=1,
      has_default_value=False, default_value=float(0),
      message_type=None, enum_type=None, containing_type=None,
      is_extension=False, extension_scope=None,
      options=None, file=DESCRIPTOR),
  ],
  extensions=[
  ],
  nested_types=[],
  enum_types=[
  ],
  options=None,
  is_extendable=False,
  syntax='proto3',
  extension_ranges=[],
  oneofs=[
  ],
  serialized_start=86,
  serialized_end=158,
)

_SUBSCRIBEREQUEST.fields_by_name['type'].enum_type = _CURRENCYTYPE
_EXCHANGERATEREPLY.fields_by_name['type'].enum_type = _CURRENCYTYPE
DESCRIPTOR.message_types_by_name['SubscribeRequest'] = _SUBSCRIBEREQUEST
DESCRIPTOR.message_types_by_name['ExchangeRateReply'] = _EXCHANGERATEREPLY
DESCRIPTOR.enum_types_by_name['CurrencyType'] = _CURRENCYTYPE
_sym_db.RegisterFileDescriptor(DESCRIPTOR)

SubscribeRequest = _reflection.GeneratedProtocolMessageType('SubscribeRequest', (_message.Message,), dict(
  DESCRIPTOR = _SUBSCRIBEREQUEST,
  __module__ = 'currency_pb2'
  # @@protoc_insertion_point(class_scope:currency.SubscribeRequest)
  ))
_sym_db.RegisterMessage(SubscribeRequest)

ExchangeRateReply = _reflection.GeneratedProtocolMessageType('ExchangeRateReply', (_message.Message,), dict(
  DESCRIPTOR = _EXCHANGERATEREPLY,
  __module__ = 'currency_pb2'
  # @@protoc_insertion_point(class_scope:currency.ExchangeRateReply)
  ))
_sym_db.RegisterMessage(ExchangeRateReply)


DESCRIPTOR.has_options = True
DESCRIPTOR._options = _descriptor._ParseOptions(descriptor_pb2.FileOptions(), _b('\n\013sr.grpc.genB\017CalculatorProtoP\001\242\002\003HLW'))

_CURRENCYRATESUBSCRIPTION = _descriptor.ServiceDescriptor(
  name='CurrencyRateSubscription',
  full_name='currency.CurrencyRateSubscription',
  file=DESCRIPTOR,
  index=0,
  options=None,
  serialized_start=212,
  serialized_end=312,
  methods=[
  _descriptor.MethodDescriptor(
    name='Subscribe',
    full_name='currency.CurrencyRateSubscription.Subscribe',
    index=0,
    containing_service=None,
    input_type=_SUBSCRIBEREQUEST,
    output_type=_EXCHANGERATEREPLY,
    options=None,
  ),
])
_sym_db.RegisterServiceDescriptor(_CURRENCYRATESUBSCRIPTION)

DESCRIPTOR.services_by_name['CurrencyRateSubscription'] = _CURRENCYRATESUBSCRIPTION

# @@protoc_insertion_point(module_scope)
