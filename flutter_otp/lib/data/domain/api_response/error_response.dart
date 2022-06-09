class ErrorResponse {
  String code;
  String message;
  String description;
  dynamic errors;

  ErrorResponse({required this.code, required this.message, required this.description, this.errors});

  factory ErrorResponse.fromJson(Map<String, dynamic> json) {
    return ErrorResponse(
        code : json['code'],
        message : json['message'],
        description : json['description'],
        errors : json['errors']
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['data'] = data;
    data['message'] = message;
    data['description'] = description;
    data['errors'] = errors;
    return data;
  }
}
