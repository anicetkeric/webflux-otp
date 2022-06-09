import 'dart:convert';
import 'dart:io';
import 'package:flutter_otp/core/exceptions/api_exceptions.dart';
import 'package:http/http.dart' as http;
import 'dart:developer' as developer;

class ApiClient {
  final Map<String, String> _jsonHeaders = {'Content-Type':'application/json'};

  Future<dynamic> get(Uri url) async {
    dynamic responseJson;
    try {
        final response = await http.get(url);
        responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api get received!');
    return responseJson;
  }

  Future<dynamic> post(Uri url, dynamic body) async {
    dynamic responseJson;
    try {
        final response = await http.post(url, headers: _jsonHeaders , body: jsonEncode(body));
        responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api post data!');
    return responseJson;
  }

  Future<dynamic> put(Uri url, dynamic body) async {
    dynamic responseJson;
    try {
        final response = await http.put(url, headers: _jsonHeaders , body: jsonEncode(body));
        responseJson = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api put data!');
    return responseJson;
  }

  Future<dynamic> delete(Uri url) async {
    dynamic apiResponse;
    try {
        final response = await http.delete(url);
        apiResponse = _returnResponse(response);
    } on SocketException {
      throw NetworkStatusException();
    }
    developer.log('api delete data!');
    return apiResponse;
  }
}


dynamic _returnResponse(http.Response response) {
  switch (response.statusCode) {
    case 200:
      var responseJson = jsonDecode(utf8.decode(response.bodyBytes));
      return responseJson;
    case 201:
      var responseJson = jsonDecode(utf8.decode(response.bodyBytes));
      return responseJson;
    case 400:
      throw BadRequestException(response.body.toString());
    case 401:
    case 403:
      throw UnauthorisedException(response.body.toString());
    case 404:
      throw NotFoundDataException(response.body.toString());
    case 500:
      throw InternalErrorException(response.body.toString());
    default:
      return FetchDataException(
          'Error occurred while Communication with Server with StatusCode : ${response.statusCode}');
  }
}