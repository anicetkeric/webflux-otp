class SuccessResponse {
  dynamic data;
  String message;

  SuccessResponse({required this.data, required this.message});

  factory SuccessResponse.fromJson(Map<String, dynamic> json) {
    return SuccessResponse(
        data : json['data'],
        message : json['message']
    );
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['data'] = this.data.toJson();
    data['message'] = message;
    return data;
  }
}
