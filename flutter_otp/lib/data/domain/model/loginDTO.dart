class LoginDTO {
  late String username;
  late String jwtValue;

  LoginDTO(
      {required this.username,
        required this.jwtValue});

  LoginDTO.fromJson(Map<String, dynamic> json) {
    username = json['username'];
    jwtValue = json['password'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['username'] = username;
    data['password'] = jwtValue;
    return data;
  }
}