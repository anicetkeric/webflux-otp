
class ApiException implements Exception {
  final message;
  final prefix;

  ApiException([this.message, this.prefix]);

  @override
  String toString() {
    return "$prefix$message";
  }
}

class FetchDataException extends ApiException {
  FetchDataException([String? message])
      : super(message, "Error During Communication: ");
}

class BadRequestException extends ApiException {
  BadRequestException([message]) : super(message, "Invalid Request: ");
}

class InternalErrorException extends ApiException {
  InternalErrorException([message]) : super(message, "Internal Error: ");
}

class UnauthorisedException extends ApiException {
  UnauthorisedException([message]) : super(message, "Unauthorised: ");
}

class InvalidInputException extends ApiException {
  InvalidInputException([String? message]) : super(message, "Invalid Input: ");
}

class NotFoundDataException extends ApiException {
  NotFoundDataException([String? message]) : super(message, "Not Found Data: ");
}

class NetworkStatusException extends ApiException {
  NetworkStatusException()
      : super('Something went wrong.', "Oups!: ");
}